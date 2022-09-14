package com.hc.application;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.params.ExcelExportEntity;
import com.hc.application.ExcelMadel.HjExcleModel;
import com.hc.application.ExcelMadel.OtherExcleModel;
import com.hc.application.ExcelMadel.PyxExcleModel;
import com.hc.clickhouse.po.Monitorequipmentlastdata;
import com.hc.clickhouse.repository.MonitorequipmentlastdataRepository;
import com.hc.command.labmanagement.model.HospitalEquipmentTypeModel;
import com.hc.command.labmanagement.model.HospitalMadel;
import com.hc.command.labmanagement.model.QueryInfoModel;
import com.hc.constants.LabMonEnumError;
import com.hc.device.ProbeRedisApi;
import com.hc.device.SnDeviceRedisApi;
import com.hc.dto.*;
import com.hc.hospital.HospitalInfoApi;
import com.hc.labmanagent.HospitalEquipmentTypeApi;
import com.hc.labmanagent.MonitorEquipmentApi;
import com.hc.my.common.core.constant.enums.CurrentProbeInfoEnum;
import com.hc.my.common.core.constant.enums.ProbeOutlierMt310;
import com.hc.my.common.core.exception.IedsException;
import com.hc.my.common.core.redis.command.EquipmentInfoCommand;
import com.hc.my.common.core.redis.dto.MonitorequipmentlastdataDto;
import com.hc.my.common.core.redis.dto.SnDeviceDto;
import com.hc.my.common.core.util.BeanConverter;
import com.hc.my.common.core.util.DateUtils;
import com.hc.my.common.core.util.FileUtil;
import com.hc.service.EquipmentInfoService;
import com.hc.service.InstrumentMonitorInfoService;
import com.hc.service.InstrumentParamConfigService;
import com.hc.util.EquipmentInfoServiceHelp;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author hc
 */
@Component
public class EquipmentInfoApplication {

    @Autowired
    private EquipmentInfoService equipmentInfoService;

    @Autowired
    private InstrumentMonitorInfoService instrumentMonitorInfoService;

    @Autowired
    private InstrumentParamConfigService instrumentParamConfigService;

    @Autowired
    private MonitorequipmentlastdataRepository monitorequipmentlastdataRepository;

    @Autowired
    private SnDeviceRedisApi snDeviceRedisApi;

    @Autowired
    private HospitalInfoApi hospitalInfoApi;

    @Autowired
    private HospitalEquipmentTypeApi hospitalEquipmentTypeApi;

    @Autowired
    private MonitorEquipmentApi monitorEquipmentApi;

    @Autowired
    private ProbeRedisApi probeRedisApi;

    /**
     * 查询所有设备当前值信息
     * @param hospitalCode 医院id
     * @param equipmentTypeId 设备类型id
     * @return 监控设备集合
     */
    public List<MonitorEquipmentDto> findEquipmentCurrentData(String hospitalCode, String equipmentTypeId) {

        //获取监控设备的信息
        List<MonitorEquipmentDto> monitorEquipmentDtoList = equipmentInfoService.getEquipmentInfoByCodeAndTypeId(hospitalCode,equipmentTypeId);
        if(CollectionUtils.isEmpty(monitorEquipmentDtoList)){
            throw new IedsException(LabMonEnumError.DEVICE_INFORMATION_NOT_FOUND.getMessage());
        }
        List<String> equipmentNoList = monitorEquipmentDtoList.stream().map(MonitorEquipmentDto::getEquipmentno).collect(Collectors.toList());
        Map<String, List<MonitorEquipmentDto>> monitorEquipmentMap = monitorEquipmentDtoList.stream().collect(Collectors.groupingBy(MonitorEquipmentDto::getEquipmentno));

        //获取仪器监控的信息
        List<InstrumentMonitorInfoDto> instrumentMonitorInfoDtoList  = instrumentMonitorInfoService.selectInstrumentMonitorInfoByEqNo(equipmentNoList);
        Map<String, List<InstrumentMonitorInfoDto>> InstrumentMonitorInfoDtoMap = new HashMap<>();
        if(CollectionUtils.isNotEmpty(instrumentMonitorInfoDtoList)){
            InstrumentMonitorInfoDtoMap = instrumentMonitorInfoDtoList.stream().collect(Collectors.groupingBy(InstrumentMonitorInfoDto::getEquipmentno));
        }

        //查询lowlimit的值
        List<MonitorinstrumentDto> monitorEquipmentLowLimitList = equipmentInfoService.getLowLimitList(equipmentNoList);
        Map<String, List<MonitorinstrumentDto>> monitorEquipmentLowMap = new HashMap<>();
        if(CollectionUtils.isNotEmpty(monitorEquipmentLowLimitList)){
            monitorEquipmentLowMap = monitorEquipmentLowLimitList.stream().collect(Collectors.groupingBy(MonitorinstrumentDto::getEquipmentno));
        }
        EquipmentInfoCommand equipmentInfoCommand = new EquipmentInfoCommand();
        equipmentInfoCommand.setEquipmentNoList(equipmentNoList);
        equipmentInfoCommand.setHospitalCode(hospitalCode);

        //查询设备号当前的信息值
        List<MonitorequipmentlastdataDto> resultList = snDeviceRedisApi.getTheCurrentValueOfTheDeviceInBatches(equipmentInfoCommand).getResult();

        Map<String, List<MonitorequipmentlastdataDto>> resultMap = resultList.stream().collect(Collectors.groupingBy(MonitorequipmentlastdataDto::getEquipmentno));

        for (MonitorEquipmentDto monitorEquipmentDto : monitorEquipmentDtoList) {
            String equipmentNo = monitorEquipmentDto.getEquipmentno();
            List<MonitorEquipmentDto> monitorEquipmentDtos = monitorEquipmentMap.get(equipmentNo);
            if(CollectionUtils.isNotEmpty(monitorEquipmentDtos)){
                monitorEquipmentDto.setSn(monitorEquipmentDtos.get(0).getSn());
            }
            List<MonitorequipmentlastdataDto> monitorEquipmentLastDataDTOs = resultMap.get(equipmentNo);
            if(CollectionUtils.isNotEmpty(monitorEquipmentLastDataDTOs)){
                MonitorequipmentlastdataDto monitorequipmentlastdataDto = monitorEquipmentLastDataDTOs.get(0);
                if(StringUtils.isNotBlank(monitorequipmentlastdataDto.getCurrentdoorstate())){
                    // 查找这个设备下开关量的最低值
                    String lowlimit = monitorEquipmentLowMap.get(equipmentNo).get(0).getLowlimit();
                    monitorEquipmentDto.setLowlimit(lowlimit);
                }
                monitorEquipmentDto.setMonitorequipmentlastdataDto(monitorequipmentlastdataDto);
            }
            List<InstrumentMonitorInfoDto> resultIMList = InstrumentMonitorInfoDtoMap.get(equipmentNo);
            if(CollectionUtils.isNotEmpty(resultIMList)){
                monitorEquipmentDto.setInstrumentMonitorInfoDtoList(resultIMList);
            }
        }
        return monitorEquipmentDtoList;
    }

    /**
     * 获取曲线信息
     * @param equipmentNo 设备id
     * @param date 时间
     * @param sn sn号
     * @return 曲线信息对象
     */
    public CurveInfoDto getCurveFirst(String equipmentNo, String date,String sn) {
        List<Monitorequipmentlastdata> lastDataModelList  =  monitorequipmentlastdataRepository.getMonitorEquipmentLastDataInfo(date,equipmentNo);
        if(CollectionUtils.isEmpty(lastDataModelList)) {
            throw new IedsException(LabMonEnumError.NO_DATA_FOR_CURRENT_TIME.getMessage());
        }
        Map<String,List<InstrumentParamConfigDto>>  map = instrumentParamConfigService.getInstrumentParamConfigByENo(equipmentNo);
        boolean flag = false;
        if(StringUtils.isNotEmpty(sn) && ProbeOutlierMt310.THREE_ONE.getCode().equals(sn.substring(4,6))){
            flag = true;
        }
        return flag ?
                EquipmentInfoServiceHelp.getCurveFirstByMT300DC(lastDataModelList,map,false):
                EquipmentInfoServiceHelp.getCurveFirst(lastDataModelList,map,false);
    }

    /**
     * 获取医院信息
     * @param hospitalCode
     * @return
     */
    public HospitalMadel getHospitalInfO(String hospitalCode) {
        HospitalMadel hospitalInfo = hospitalInfoApi.findHospitalInfo(hospitalCode).getResult();
        if(ObjectUtils.isEmpty(hospitalInfo)){
            return null;
        }
        String timeoutRedDuration = hospitalInfo.getTimeoutRedDuration();
        if (StringUtils.isEmpty(timeoutRedDuration)) {
            hospitalInfo.setTimeoutRedDuration("60");
        }
        List<HospitalEquipmentTypeModel> hospitalEquipmentTypeModelList = hospitalEquipmentTypeApi.findHospitalEquipmentTypeByCode(hospitalCode).getResult();
        if(CollectionUtils.isNotEmpty(hospitalEquipmentTypeModelList)){
            hospitalInfo.setHospitalEquipmentTypeModelList(hospitalEquipmentTypeModelList);
        }
        return hospitalInfo;
    }

    /**
     * 获取当前市电信息
     *  先获取当前医院所有的设备
     *      再获取所有设备的获取当前值的信息
     *          过滤掉没有ups的数据，取最新集合中的第一条数据
     * @param hospitalCode
     * @return
     */
    public List<MonitorUpsInfoDto>  getCurrentUpsInfo(String hospitalCode,String equipmentTypeId) {
        List<SnDeviceDto> equipmentList = monitorEquipmentApi.getEquipmentNoList(hospitalCode,equipmentTypeId).getResult();
        if(CollectionUtils.isEmpty(equipmentList)){
          return null;
        }
        //获取equipmentNo的集合
        List<String> equipmentNoList = equipmentList.stream().map(SnDeviceDto::getEquipmentNo).collect(Collectors.toList());
        //以equipmentNo分组
        Map<String, List<SnDeviceDto>> equipmentNoMap = equipmentList.stream().collect(Collectors.groupingBy(SnDeviceDto::getEquipmentNo));
        EquipmentInfoCommand equipmentInfoCommand = new EquipmentInfoCommand();
        equipmentInfoCommand.setHospitalCode(hospitalCode);
        equipmentInfoCommand.setEquipmentNoList(equipmentNoList);
        List<MonitorequipmentlastdataDto> monitorEquipmentLastDataList = snDeviceRedisApi.getTheCurrentValueOfTheDeviceInBatches(equipmentInfoCommand).getResult();
        if (CollectionUtils.isEmpty(monitorEquipmentLastDataList)) {
            return null;
        }
        Map<String, List<MonitorequipmentlastdataDto>> lastDateEquipmentNoMap =
                monitorEquipmentLastDataList.stream().collect(Collectors.groupingBy(MonitorequipmentlastdataDto::getEquipmentno));
        if (MapUtils.isEmpty(lastDateEquipmentNoMap)) {
            return null;
        }
        List<MonitorUpsInfoDto> upsInfoList = new ArrayList<>();
        equipmentNoList.forEach(no->{
            if (lastDateEquipmentNoMap.containsKey(no) && equipmentNoMap.containsKey(no)) {
                MonitorUpsInfoDto monitorUpsInfoDto = new MonitorUpsInfoDto();
                MonitorequipmentlastdataDto monitorequipmentlastdataDto = lastDateEquipmentNoMap.get(no).get(0);
                monitorUpsInfoDto.setEquipmentNo(no);
                SnDeviceDto snDeviceDto = equipmentNoMap.get(no).get(0);
                monitorUpsInfoDto.setEquipmentName(snDeviceDto.getEquipmentName());
                String currentUps = monitorequipmentlastdataDto.getCurrentups();
                String voltage = monitorequipmentlastdataDto.getVoltage();
                monitorUpsInfoDto.setSn(monitorequipmentlastdataDto.getSn());
                if (StringUtils.isNotBlank(currentUps)) {
                    monitorUpsInfoDto.setCurrentUps(currentUps);
                }
                if (StringUtils.isNotBlank(voltage)) {
                    monitorUpsInfoDto.setVoltage(voltage);
                }
                upsInfoList.add(monitorUpsInfoDto);
            }
        });
        if(CollectionUtils.isEmpty(upsInfoList)) {
            throw new IedsException(LabMonEnumError.NO_UTILITY_RECORD.getMessage());
        }
        return upsInfoList;
    }

    /**
     * 查询当前值信息(查询导出页面)
     * @param equipmentNo
     * @param startTime
     * @param endTime
     * @return
     */
    public QueryInfoModel getQueryInfo(String equipmentNo, String startTime, String endTime ){
        MonitorEquipmentDto equipmentInfo = equipmentInfoService.getEquipmentInfoByNo(equipmentNo);
        if (ObjectUtils.isEmpty(equipmentInfo)) {
            return null;
        }
        List<Monitorequipmentlastdata> monitorEquipmentLastDataInfo =
                monitorequipmentlastdataRepository.getMonitorEquipmentLastDataInfo(startTime, endTime, equipmentNo);
        QueryInfoModel queryInfoModel = new QueryInfoModel();
        queryInfoModel.setEquipmentName(equipmentInfo.getEquipmentname());
        queryInfoModel.setInstrumentTypeId(equipmentInfo.getInstrumenttypeid());
        //查询标题
        List<String> eNameList = queryTitle(equipmentNo);
        queryInfoModel.setProbeENameList(eNameList);
        queryInfoModel.setEquipmentTypeId(equipmentInfo.getEquipmenttypeid());
        List<MonitorequipmentlastdataDto> convert = BeanConverter.convert(monitorEquipmentLastDataInfo, MonitorequipmentlastdataDto.class);
        queryInfoModel.setMonitorEquipmentLastDataDTOList(convert);
        return queryInfoModel;
    }

    /**
     * 查询标题
     * @param equipmentNo
     * @return
     */
    private List<String> queryTitle(String equipmentNo) {
        MonitorEquipmentDto equipmentInfoByNo = equipmentInfoService.getEquipmentInfoByNo(equipmentNo);
        String hospitalCode = equipmentInfoByNo.getHospitalcode();
        List<Integer> list;
        //mt400时取缓存其他取数据库
        if ("1".equals(equipmentInfoByNo.getEquipmenttypeid()) && "8".equals(equipmentInfoByNo.getInstrumenttypeid())) {
            list = probeRedisApi.getEquipmentMonitorInfo(hospitalCode, equipmentNo).getResult();
        }else {
            list = equipmentInfoService.selectInstrumentConfigId(equipmentNo);
        }
        if(CollectionUtils.isEmpty(list)){
            throw new IedsException(LabMonEnumError.THE_DEVICE_HAS_NO_PROBE_INFORMATION.getMessage());
        }
        List<String> eNameList = new ArrayList<>();
        for (Integer instrumentTypeId : list) {
            CurrentProbeInfoEnum from = CurrentProbeInfoEnum.from(instrumentTypeId);
            String probeEName = from.getProbeEName();
            eNameList.add(probeEName);
        }
        return eNameList;
    }


    /**
     * 时间点查询
     * @param hospitalCode
     * @param operationDate
     * @param type
     */
    public void exportSingle(String hospitalCode, String operationDate, String type,HttpServletResponse response) {
        //判断该医院有哪些设备类型
        List<HospitalEquipmentTypeModel> result =
                hospitalEquipmentTypeApi.findHospitalEquipmentTypeByCode(hospitalCode).getResult();
        if (CollectionUtils.isEmpty(result)) {
            return;
        }
        String hospitalName = result.get(0).getHospitalName();
        //获取医院的设备信息集合
        List<SnDeviceDto> monitorInfo = monitorEquipmentApi.getMonitorEquipmentInfoByHCode(hospitalCode).getResult();
        if (CollectionUtils.isEmpty(monitorInfo)) {
            return;
        }
        String equipmentName;
        String hjs = "环境";
        String pyxs = "培养箱";
        String ydgs = "液氮罐";
        String bxs = "冰箱";
        String czts = "操作台";
        //月
        if(StringUtils.equals(type,"month")){
            Date date = DateUtils.parseDate(operationDate);
            String startTime = DateUtils.getPreviousHourHHmm(date);
            String endTime = DateUtils.dateReduceHHmm(date);
            String yearMonth = DateUtils.getYearMonth(date);
            List<Monitorequipmentlastdata> lastDateList =
                    monitorequipmentlastdataRepository.getMonitorEquipmentLastDataInfoByPeriod(hospitalCode,startTime,endTime,yearMonth);
            if(CollectionUtils.isEmpty(lastDateList)){
                return;
            }
            Map<String, List<Monitorequipmentlastdata>> equipmentNoMap =
                    lastDateList.stream().collect(Collectors.groupingBy(Monitorequipmentlastdata::getEquipmentno));
            List<List<?>> lists = new ArrayList<List<?>>();
            List<String> sheetList = new ArrayList<String>();
            List<String> titleList = new ArrayList<String>();
            List<Class<?>> classList = new ArrayList<Class<?>>();
            List<HjExcleModel> hjExcleModels = new ArrayList<HjExcleModel>();
            List<PyxExcleModel> pyxExcleModels = new ArrayList<PyxExcleModel>();
            //液氮罐
            List<OtherExcleModel> otherExcleModels = new ArrayList<OtherExcleModel>();
            //冰箱
            List<OtherExcleModel> otherExcleModelone = new ArrayList<OtherExcleModel>();
            //操作台
            List<OtherExcleModel> otherExcleModeltwo= new ArrayList<OtherExcleModel>();
            for (SnDeviceDto snDeviceDto : monitorInfo) {
                String equipmentTypeId = snDeviceDto.getEquipmentTypeId();
                if (StringUtils.isBlank(equipmentTypeId)) {
                    continue;
                }
                equipmentName = snDeviceDto.getEquipmentName();
                String equipmentNo = snDeviceDto.getEquipmentNo();
                List<Monitorequipmentlastdata> list = new ArrayList<>();
                if (!equipmentNoMap.containsKey(equipmentNo)) {
                    continue;
                }
                list = equipmentNoMap.get(equipmentNo);
                switch (equipmentTypeId) {
                    case "1":
                        List<HjExcleModel> hj = EquipmentInfoServiceHelp.getHj(list, equipmentName);
                        if (CollectionUtils.isEmpty(hj)) {
                            break;
                        }
                        if (CollectionUtils.isEmpty(hjExcleModels)) {
                            hjExcleModels = hj;
                        } else {
                            hjExcleModels.addAll(hj);
                        }
                        break;
                    case "2":
                        List<PyxExcleModel> pyx = EquipmentInfoServiceHelp.getPyx(list, equipmentName);
                        if (CollectionUtils.isEmpty(pyx)) {
                            break;
                        }
                        if (CollectionUtils.isEmpty(pyxExcleModels)) {
                            pyxExcleModels = pyx;
                        } else {
                            pyxExcleModels.addAll(pyx);
                        }
                        break;
                    case "3":
                        List<OtherExcleModel> other1 = EquipmentInfoServiceHelp.getOther(list, equipmentName);
                        if (CollectionUtils.isEmpty(other1)) {
                            break;
                        }
                        if (CollectionUtils.isEmpty(otherExcleModels)) {
                            otherExcleModels = other1;
                        } else {
                            otherExcleModels.addAll(other1);
                        }
                        break;
                    case "4":
                        List<OtherExcleModel> other2 = EquipmentInfoServiceHelp.getOther(list, equipmentName);
                        if (CollectionUtils.isEmpty(other2)) {
                            break;
                        }
                        if (CollectionUtils.isEmpty(otherExcleModels)) {
                            otherExcleModels = other2;
                        } else {
                            otherExcleModels.addAll(other2);
                        }
                        break;
                    case "5":
                        List<OtherExcleModel> other3 = EquipmentInfoServiceHelp.getOther(list, equipmentName);
                        if (CollectionUtils.isEmpty(other3)) {
                            break;
                        }
                        if (CollectionUtils.isEmpty(otherExcleModels)) {
                            otherExcleModels = other3;
                        } else {
                            otherExcleModels.addAll(other3);
                            break;
                        }
                    default:
                        break;
                }
            }
            if (CollectionUtils.isNotEmpty(hjExcleModels)) {
                sheetList.add(hjs);
                titleList.add(hjs);
                classList.add(HjExcleModel.class);
                lists.add(hjExcleModels);
            }
            if (CollectionUtils.isNotEmpty(pyxExcleModels)) {
                sheetList.add(pyxs);
                titleList.add(pyxs);
                classList.add(PyxExcleModel.class);
                lists.add(pyxExcleModels);
            }
            if (CollectionUtils.isNotEmpty(otherExcleModels)) {
                sheetList.add(ydgs);
                titleList.add(ydgs);
                classList.add(OtherExcleModel.class);
                lists.add(otherExcleModels);

            }
            if (CollectionUtils.isNotEmpty(otherExcleModelone)) {
                sheetList.add(bxs);
                titleList.add(bxs);
                classList.add(OtherExcleModel.class);
                lists.add(otherExcleModelone);
            }

            if (CollectionUtils.isNotEmpty(otherExcleModeltwo)) {
                sheetList.add(czts);
                titleList.add(czts);
                classList.add(OtherExcleModel.class);
                lists.add(otherExcleModeltwo);
            }
            FileUtil.exportExcleUnSheets(lists, titleList, sheetList, classList,
                    hospitalName+yearMonth+"月监控数据.xls", response);
            return;
        }
        //日
        Date date = DateUtils.parseDate(operationDate);
        String startTime = DateUtils.getPreviousHourHHmmss(date);
        String endTime = DateUtils.paseDateHHmmss(date);
        String time = DateUtils.paseDate(date);
        List<Monitorequipmentlastdata> lastDateList =
                monitorequipmentlastdataRepository.getMonitorEquipmentLastDataInfoByDate(hospitalCode,startTime,endTime,time);
        if (CollectionUtils.isEmpty(lastDateList)) {
            return;
        }
        Map<String, List<Monitorequipmentlastdata>> equipmentNoMap =
                lastDateList.stream().collect(Collectors.groupingBy(Monitorequipmentlastdata::getEquipmentno));

        List<List<?>> lists = new ArrayList<List<?>>();
        List<String> sheetList = new ArrayList<String>();
        List<String> titleList = new ArrayList<String>();
        List<Class<?>> classList = new ArrayList<Class<?>>();
        List<HjExcleModel> hjExcleModels = new ArrayList<HjExcleModel>();
        List<PyxExcleModel> pyxExcleModels= new ArrayList<PyxExcleModel>();
        //液氮罐
        List<OtherExcleModel> otherExcleModels = new ArrayList<OtherExcleModel>();
        //冰箱
        List<OtherExcleModel> otherExcleModelone = new ArrayList<OtherExcleModel>();
        //操作台
        List<OtherExcleModel> otherExcleModeltwo = new ArrayList<OtherExcleModel>();

        for (SnDeviceDto snDeviceDto : monitorInfo) {
            String equipmentTypeId = snDeviceDto.getEquipmentTypeId();
            if (StringUtils.isBlank(equipmentTypeId)) {
                continue;
            }
            String equipmentNo = snDeviceDto.getEquipmentNo();
            if (!equipmentNoMap.containsKey(equipmentNo)) {
                continue;
            }
            equipmentName = snDeviceDto.getEquipmentName();
            List<Monitorequipmentlastdata> list = equipmentNoMap.get(equipmentNo);
            switch (equipmentTypeId){
                case "1":
                    List<HjExcleModel> hj = EquipmentInfoServiceHelp.getHj(list, equipmentName);
                    if (CollectionUtils.isEmpty(hj)) {
                        break;
                    }
                    if (CollectionUtils.isEmpty(hjExcleModels)) {
                        hjExcleModels = hj;
                    } else {
                        hjExcleModels.addAll(hj);
                    }
                    break;
                case "2":
                    List<PyxExcleModel> pyx = EquipmentInfoServiceHelp.getPyx(list, equipmentName);
                    if (CollectionUtils.isEmpty(pyx)) {
                        break;
                    }
                    if (CollectionUtils.isEmpty(pyxExcleModels)) {
                        pyxExcleModels = pyx;
                    } else {
                        pyxExcleModels.addAll(pyx);
                    }
                    break;
                case "3":
                    List<OtherExcleModel> other1 = EquipmentInfoServiceHelp.getOther(list, equipmentName);
                    if (CollectionUtils.isEmpty(other1)) {
                        break;
                    }
                    if (CollectionUtils.isEmpty(otherExcleModels)) {
                        otherExcleModels = other1;
                    } else {
                        otherExcleModels.addAll(other1);
                    }
                    break;
                case "4":
                    List<OtherExcleModel> other2 = EquipmentInfoServiceHelp.getOther(list, equipmentName);
                    if (CollectionUtils.isEmpty(other2)) {
                        break;
                    }
                    if (CollectionUtils.isEmpty(otherExcleModels)) {
                        otherExcleModels = other2;
                    } else {
                        otherExcleModels.addAll(other2);
                    }
                    break;
                case "5":
                    List<OtherExcleModel> other3 = EquipmentInfoServiceHelp.getOther(list, equipmentName);
                    if (CollectionUtils.isEmpty(other3)) {
                        break;
                    }
                    if (CollectionUtils.isEmpty(otherExcleModels)) {
                        otherExcleModels = other3;
                    } else {
                        otherExcleModels.addAll(other3);
                        break;
                    }
                default:
                    break;
            }

        }
        if (CollectionUtils.isNotEmpty(hjExcleModels)) {
            sheetList.add(hjs);
            titleList.add(hjs);
            classList.add(HjExcleModel.class);
            lists.add(hjExcleModels);
        }
        if (CollectionUtils.isNotEmpty(pyxExcleModels)) {
            sheetList.add(pyxs);
            titleList.add(pyxs);
            classList.add(PyxExcleModel.class);
            lists.add(pyxExcleModels);
        }
        if (CollectionUtils.isNotEmpty(otherExcleModels)) {
            sheetList.add(ydgs);
            titleList.add(ydgs);
            classList.add(OtherExcleModel.class);
            lists.add(otherExcleModels);
        }
        if (CollectionUtils.isNotEmpty(otherExcleModelone)) {
            sheetList.add(bxs);
            titleList.add(bxs);
            classList.add(OtherExcleModel.class);
            lists.add(otherExcleModelone);
        }
        if (CollectionUtils.isNotEmpty(otherExcleModeltwo)) {
            sheetList.add(czts);
            titleList.add(czts);
            classList.add(OtherExcleModel.class);
            lists.add(otherExcleModeltwo);
        }
        FileUtil.exportExcleUnSheets(lists, titleList, sheetList, classList,
                hospitalName+operationDate+"监控数据.xls", response);
    }

    /**
     * 设备通过月份获取每个时间点数据
     * @param equipmentNo
     * @param operationDate
     * @return
     */
    public CurveInfoDto getCurveInfoByMonthTime(String equipmentNo, String operationDate) {
        //日
        Date newDate = DateUtils.parseDate(operationDate);
        String startTime = DateUtils.getPreviousHourHHmm(newDate);
        String endTime = DateUtils.parseDatetime(newDate);
        String date = DateUtils.getYearMonth(newDate);
        List<Monitorequipmentlastdata> lastDateList =
                monitorequipmentlastdataRepository.getLastDataByEnoAndMonth(equipmentNo,startTime,endTime,date);
        if(CollectionUtils.isEmpty(lastDateList)) {
            return null;
        }
        Map<String, List<InstrumentParamConfigDto>> map = instrumentParamConfigService.getInstrumentParamConfigByENo(equipmentNo);
        String sn = lastDateList.get(0).getSn();
        boolean flag = false;
        if(StringUtils.isNotEmpty(sn) && ProbeOutlierMt310.THREE_ONE.getCode().equals(sn.substring(4,6))){
            flag = true;
        }
        return flag ?
                EquipmentInfoServiceHelp.getCurveFirstByMT300DC(lastDateList,map,true):
                EquipmentInfoServiceHelp.getCurveFirst(lastDateList,map,true);
    }

    /**
     * 导出excel
     * @param equipmentNo
     * @param startDate
     * @param endDate
     * @param response
     */
    public void getQueryResult(String equipmentNo, String startDate, String endDate, HttpServletResponse response) {
        //获取数据库数据
        List<Monitorequipmentlastdata> monitorEquipmentLastDataInfo =
                monitorequipmentlastdataRepository.getMonitorEquipmentLastDataInfo(startDate, endDate, equipmentNo);
        //获取标头
        List<String> list = queryTitle(equipmentNo);
        if(CollectionUtils.isEmpty(list)){
           return;
        }
        MonitorEquipmentDto equipmentInfoByNo = equipmentInfoService.getEquipmentInfoByNo(equipmentNo);
        //标头映射
        List<ExcelExportEntity> beanList = headerMapping(list,equipmentInfoByNo);
        //获取属性map
        List<Map<String,Object>> mapList = new ArrayList<>();
        for (Monitorequipmentlastdata monitorequipmentlastdata : monitorEquipmentLastDataInfo) {
            String currentups = monitorequipmentlastdata.getCurrentups();
            if("1".equals(currentups)){
                monitorequipmentlastdata.setCurrentups("异常");
            }
            else{
                monitorequipmentlastdata.setCurrentups("正常");
            }
            Map<String, Object> objectMap = getObjectToMap(monitorequipmentlastdata);
            //过滤map 只取list存在的key的map
            filterMap(objectMap,list);
            mapList.add(objectMap);
        }
        String equipmentname = equipmentInfoByNo.getEquipmentname();
        String title = equipmentname+startDate+"-"+endDate+"监控数据汇总";
        Workbook workbook =  ExcelExportUtil.exportExcel(new ExportParams(title,"sheet1"),beanList,mapList);
        FileUtil.downLoadExcel(title+".xls",response,workbook);
    }
    /**
     * 过滤map
     * @param objectMap
     */
    private void filterMap(Map<String, Object> objectMap,List<String> list) {
        list.add("inputdatetime");
        Iterator<String> iterator = objectMap.keySet().iterator();
        while (iterator.hasNext()) {
            String next = iterator.next();
            if(!list.contains(next)){
                iterator.remove();
                objectMap.remove(next);
            }
        }
    }

    private List<ExcelExportEntity> headerMapping(List<String> list,MonitorEquipmentDto equipmentInfo) {
        List<ExcelExportEntity> excelExportEntities = new ArrayList<>();
        excelExportEntities.add(new ExcelExportEntity("记录时间","inputdatetime"));
        for (String fieldName : list) {
            CurrentProbeInfoEnum from = CurrentProbeInfoEnum.from(fieldName);
            String name = from.getProbeCName();
            if (!StringUtils.isBlank(from.getUnit())) {
                name = name+"("+from.getUnit()+")";
            }
            //设备特殊值判断
            name = deviceSpecialValue(fieldName,name,equipmentInfo);
            excelExportEntities.add(new ExcelExportEntity(name,fieldName));
        }
        return excelExportEntities;
    }

    private String deviceSpecialValue(String fieldName,String name , MonitorEquipmentDto equipmentInfo) {
        switch (fieldName){
            //当设备为Mt300MIX时为状态
            case "currentairflow1":
                if("7".equals(equipmentInfo.getInstrumenttypeid())){
                    name = "状态";
                }
                break;
            //当设备类型为2(培养箱)时
            case "currentairflow":
                if("2".equals(equipmentInfo.getEquipmenttypeid())){
                    name = "CO2压力(Mpa)";
                }
                break;
            default:
                break;
        }
        return name;
    }

    /**
     * 对象转map
     * @param obj
     * @return
     */
    public static Map<String, Object> getObjectToMap(Object obj)  {
        Map<String, Object> map = new HashMap<String, Object>();
        Class<?> cla = obj.getClass();
        Field[] fields = cla.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            String keyName = field.getName();
            Object value = null;
            try {
                value = field.get(obj);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            if (value == null) {
                value = "";
            }
            if(value instanceof Date){
                value = DateUtils.paseDatetime((Date) value);
            }
            map.put(keyName, value);
        }
        return map;
    }


}
