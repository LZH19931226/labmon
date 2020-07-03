package com.hc.service.serviceimpl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.PageRowBounds;
import com.hc.config.RedisTemplateUtil;
import com.hc.dao.HospitaDao;
import com.hc.dao.InstrumentparamconfigDao;
import com.hc.entity.*;
import com.hc.mapper.laboratoryFrom.EquipmentInfoMapper;
import com.hc.mapper.laboratoryFrom.InstrumentMonitorInfoMapper;
import com.hc.mapper.laboratoryFrom.MonitorInstrumentMapper;
import com.hc.model.*;
import com.hc.model.ExcleInfoModel.*;
import com.hc.model.ResponseModel.EquipmentConfigInfoModel;
import com.hc.model.SingleTimeExcle.HjExcleModel;
import com.hc.model.SingleTimeExcle.OtherExcleModel;
import com.hc.model.SingleTimeExcle.PyxExcleModel;
import com.hc.model.SingleTimeExcle.SingleTimeEquipmentModel;
import com.hc.service.EquipmentInfoService;
import com.hc.service.UpdateRecordService;
import com.hc.utils.ApiResponse;
import com.hc.utils.FileUtil;
import com.hc.utils.JsonUtil;
import com.hc.utils.TimeHelper;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by 16956 on 2018-08-01.
 */
//  从redis缓存中取数据，当redis服务挂了的时候，则从数据库中取数据。  2018-08-01
//  一年过去了，终于好像实现了，不过当前值没从数据库里面读取。哈哈哈    2019-05-26
@Service
public class EquipmentInfoServiceImpl implements EquipmentInfoService {
    private static final Logger LOGGER = LoggerFactory.getLogger(EquipmentInfoServiceImpl.class);
    @Autowired
    private EquipmentInfoMapper equipmentInfoMapper;
    @Autowired
    private RedisTemplateUtil redisTemplateUtil;
    @Autowired
    private HospitaDao hospitaDao;
    @Autowired
    private InstrumentparamconfigDao instrumentparamconfigDao;
    @Autowired
    private InstrumentMonitorInfoMapper instrumentMonitorInfoMapper;
    @Autowired
    private MonitorInstrumentMapper monitorInstrumentMapper;
    @Autowired
    private UpdateRecordService updateRecordService;

    @Override
    public ApiResponse<String> batchOperationType(BatchModel batchModel) {
        ApiResponse<String> apiResponse = new ApiResponse<>();
        String equipmenttypeid = batchModel.getEquipmenttypeid();
        String hospitalcode = batchModel.getHospitalcode();
        String warningphone = batchModel.getWarningphone();
        HashOperations<Object, Object, Object> objectObjectObjectHashOperations = redisTemplateUtil.opsForHash();
        List<Instrumentparamconfig> equipmentTypeAllConfig = equipmentInfoMapper.getEquipmentTypeAllConfig(hospitalcode, equipmenttypeid);
        if (CollectionUtils.isEmpty(equipmentTypeAllConfig)) {
            apiResponse.setCode(ApiResponse.NOT_FOUND);
            apiResponse.setMessage("无信息");
            return apiResponse;
        }
        for (Instrumentparamconfig instrumentparamconfig : equipmentTypeAllConfig) {
            InstrumentInfoModel one1 = monitorInstrumentMapper.getInstrumentInfoByNo(instrumentparamconfig.getInstrumentparamconfigno());

            instrumentparamconfig.setWarningphone(warningphone);
            instrumentparamconfigDao.save(instrumentparamconfig);
            InstrumentInfoModel one2 = monitorInstrumentMapper.getInstrumentInfoByNo(instrumentparamconfig.getInstrumentparamconfigno());

            String instrumentparamconfigNO = instrumentparamconfig.getInstrumentparamconfigno();
            InstrumentMonitorInfoModel instrumentMonitorInfoModel = instrumentMonitorInfoMapper.selectInstrumentOneInfo(instrumentparamconfigNO);
            ShowModel showModel = monitorInstrumentMapper.getHospitalNameEquipmentNameByNo(instrumentparamconfig.getInstrumentparamconfigno());
            String instrumentName = monitorInstrumentMapper.getInstrumentName(instrumentparamconfigNO);
            updateRecordService.updateInstrumentMonitor(instrumentName,showModel.getEquipmentname(), showModel.getHospitalname(), batchModel.getUsername(), one1, one2, "1", "1");
            objectObjectObjectHashOperations.put("hospital:instrumentparam", instrumentMonitorInfoModel.getInstrumentno() + ":" + instrumentMonitorInfoModel.getInstrumentconfigid().toString(), JsonUtil.toJson(instrumentMonitorInfoModel));
        }

        return apiResponse;
    }

    @Override
    public ApiResponse<List<Monitorequipment>> getEquipmentByType(String hospitalcode, String equipmenttypeid) {
        ApiResponse<List<Monitorequipment>> apiResponse = new ApiResponse<List<Monitorequipment>>();
        List<Monitorequipment> monitorequipmentList;
        try {
            monitorequipmentList = equipmentInfoMapper.getEquipmentByType(hospitalcode, equipmenttypeid);
            if (CollectionUtils.isEmpty(monitorequipmentList)) {
                apiResponse.setCode(ApiResponse.FAILED);
                apiResponse.setMessage("当前设备类型无设备信息");
                return apiResponse;
            }
            apiResponse.setCode(ApiResponse.SUCCESS);
            apiResponse.setMessage("查询成功");
            apiResponse.setResult(monitorequipmentList);
            return apiResponse;
        } catch (Exception e) {
            LOGGER.error("失败：" + e.getMessage());
            apiResponse.setMessage("服务异常");
            apiResponse.setCode(ApiResponse.FAILED);
            return apiResponse;
        }

    }

    @Override
    public ApiResponse<List<Monitorequipment>> getEquipmentCurrentData(String hospitalcode, String equipmenttypeid) {
        ApiResponse<List<Monitorequipment>> apiResponse = new ApiResponse<List<Monitorequipment>>();
        List<Monitorequipment> monitorequipmentList;
        try {
            // 获取设备编号
            monitorequipmentList = equipmentInfoMapper.getEquipmentByType(hospitalcode, equipmenttypeid);
            if (CollectionUtils.isEmpty(monitorequipmentList)) {
                apiResponse.setCode(ApiResponse.FAILED);
                apiResponse.setMessage("当前设备类型无设备信息");
                return apiResponse;
            }
            HashOperations<Object, Object, Object> objectObjectObjectHashOperations = redisTemplateUtil.opsForHash();
            for (Monitorequipment monitorequipment : monitorequipmentList) {
                String sn = equipmentInfoMapper.getSn(monitorequipment.getEquipmentno());
                monitorequipment.setSn(sn);
                String lastdata = (String) objectObjectObjectHashOperations.get("LASTDATA", monitorequipment.getEquipmentno());
                if (StringUtils.isNotEmpty(lastdata)) {
                    Monitorequipmentlastdata monitorequipmentlastdata = JsonUtil.toBean(lastdata, Monitorequipmentlastdata.class);
                    if (StringUtils.isNotEmpty(monitorequipmentlastdata.getCurrentdoorstate())) {
                        // 查找这个设备下开关量的最低值
                        String lowlimit = equipmentInfoMapper.getLowlimit(monitorequipment.getEquipmentno());
                        monitorequipment.setLowlimit(lowlimit);
                    }
                    monitorequipment.setMonitorequipmentlastdata(monitorequipmentlastdata);
                }
            }
            apiResponse.setMessage("查询成功");
            apiResponse.setCode(ApiResponse.SUCCESS);
            apiResponse.setResult(monitorequipmentList);
            return apiResponse;
        } catch (Exception e) {
            LOGGER.error("失败：" + e.getMessage());
            apiResponse.setMessage("服务异常");
            apiResponse.setCode(ApiResponse.FAILED);
            return apiResponse;
        }
    }

    @Override
    public ApiResponse<Page<Monitorequipment>> getEquipmentCurrentDataPage(String hospitalcode, String equipmenttypeid, Integer pagesize, Integer pagenum,String equipmentname) {

        ApiResponse<Page<Monitorequipment>> apiResponse = new ApiResponse<>();
        List<Monitorequipment> monitorequipmentList;

        PageUserModel pageUserModel = new PageUserModel();
        try {
            Integer start = (pagenum - 1) * pagesize;
            Integer end = pagesize;
            PageRowBounds page = new PageRowBounds(start, end);
            pageUserModel.setHospitalcode(hospitalcode);
            pageUserModel.setEquipmenttypeid(equipmenttypeid);
            if (StringUtils.isNotEmpty(equipmentname)) {
                pageUserModel.setEquipmentname(equipmentname);
            }
            // 获取设备编号
            monitorequipmentList = equipmentInfoMapper.getEquipmentByTypePageInfo(page, pageUserModel);
            if (CollectionUtils.isEmpty(monitorequipmentList)) {
                apiResponse.setCode(ApiResponse.FAILED);
                apiResponse.setMessage("当前设备类型无设备信息");
                return apiResponse;
            }
            HashOperations<Object, Object, Object> objectObjectObjectHashOperations = redisTemplateUtil.opsForHash();
            for (Monitorequipment monitorequipment : monitorequipmentList) {
                String sn = equipmentInfoMapper.getSn(monitorequipment.getEquipmentno());
                monitorequipment.setSn(sn);
                String lastdata = (String) objectObjectObjectHashOperations.get("LASTDATA", monitorequipment.getEquipmentno());
                if (StringUtils.isNotEmpty(lastdata)) {
                    Monitorequipmentlastdata monitorequipmentlastdata = JsonUtil.toBean(lastdata, Monitorequipmentlastdata.class);
                    monitorequipment.setMonitorequipmentlastdata(monitorequipmentlastdata);
                    if (StringUtils.isNotEmpty(monitorequipmentlastdata.getCurrentdoorstate())) {
                        // 查找这个设备下开关量的最低值
                        String lowlimit = equipmentInfoMapper.getLowlimit(monitorequipment.getEquipmentno());
                        monitorequipment.setLowlimit(lowlimit);
                    }
                }
            }
            PageInfo<Monitorequipment> pageInfo = new PageInfo<Monitorequipment>(monitorequipmentList);
            apiResponse.setPage(pageInfo);
            return apiResponse;
        } catch (Exception e) {
            LOGGER.error("失败：" + e.getMessage());
            apiResponse.setMessage("服务异常");
            apiResponse.setCode(ApiResponse.FAILED);
            return apiResponse;
        }

    }

    @Override
    public ApiResponse<Monitorupsrecord> getCurrentUps(String hospitalcode, String equipmenttypeid) {
        ApiResponse<Monitorupsrecord> apiResponse = new ApiResponse<Monitorupsrecord>();
        List<Monitorequipment> monitorequipmentList;
        try {
            monitorequipmentList = equipmentInfoMapper.getEquipmentByType(hospitalcode, equipmenttypeid);
            if (CollectionUtils.isEmpty(monitorequipmentList)) {
                apiResponse.setCode(ApiResponse.FAILED);
                apiResponse.setMessage("当前设备类型无设备信息");
                return apiResponse;
            }
            HashOperations<Object, Object, Object> objectObjectObjectHashOperations = redisTemplateUtil.opsForHash();
            String equipmentno = monitorequipmentList.get(0).getEquipmentno();
            Monitorupsrecord monitorupsrecord = new Monitorupsrecord();
            String lastdata = (String) objectObjectObjectHashOperations.get("LASTDATA", equipmentno);
            if (StringUtils.isNotEmpty(lastdata)) {
                Monitorequipmentlastdata monitorequipmentlastdata = JsonUtil.toBean(lastdata, Monitorequipmentlastdata.class);
                monitorupsrecord.setUps(monitorequipmentlastdata.getCurrentups());
            }
            if (ObjectUtils.isEmpty(monitorupsrecord)) {
                apiResponse.setMessage("无市电记录");
                apiResponse.setCode(ApiResponse.FAILED);
                return apiResponse;
            }
            apiResponse.setResult(monitorupsrecord);
            return apiResponse;
        } catch (Exception e) {
            LOGGER.error("失败：" + e.getMessage());
            apiResponse.setMessage("服务异常");
            apiResponse.setCode(ApiResponse.FAILED);
            return apiResponse;
        }
    }

    @Override
    public ApiResponse<CurveInfoModel> getCurvelFirst(String date, String equipmentno) {
        HashOperations<Object, Object, Object> objectObjectObjectHashOperations = redisTemplateUtil.opsForHash();
        String o = (String) objectObjectObjectHashOperations.get("equipmentno:lastdata", equipmentno + ":" + date);
        LOGGER.info("当前值：" + o);
        boolean flags = false;
        if (StringUtils.isNotEmpty(o)) {
            flags = false;
        }
        ApiResponse<CurveInfoModel> apiResponse = new ApiResponse<CurveInfoModel>();
        CurveInfoModel curveInfoModel = new CurveInfoModel();
//        List<LastDataModel> lastDataModelList = new ArrayList<LastDataModel>();
        String search = "monitorequipmentlastdata";
        // 判断当前日期是否为当前月份
        boolean flag = TimeHelper.isCurrentMonth(date);
        if (!flag) {
            // 当前月份是几月就是几月
            String year = date.substring(0, 4);
            String month = date.substring(5, 7);
            search = "monitorequipmentlastdata" + "_" + year + month;
        }
        CurvelReqeustModel curvelReqeustModel = new CurvelReqeustModel();
        curvelReqeustModel.setSearch(search);
        curvelReqeustModel.setDate(date);
        curvelReqeustModel.setEquipmentno(equipmentno);
        List<String> exceptionData = new ArrayList<String>();
        exceptionData.add("A");
        exceptionData.add("B");
        exceptionData.add("C");
        exceptionData.add("D");
        exceptionData.add("E");
        exceptionData.add("F");
        try {
            List<Monitorequipmentlastdata> lastDataModelList = new ArrayList<Monitorequipmentlastdata>();
            if (flags) {
                lastDataModelList = JsonUtil.toList(o, Monitorequipmentlastdata.class);
            } else {
                lastDataModelList = equipmentInfoMapper.getCurveInfo(curvelReqeustModel);
            }

            if (CollectionUtils.isEmpty(lastDataModelList)) {
                apiResponse.setMessage("当前时间探头无监控数据");
                apiResponse.setCode(ApiResponse.FAILED);
                return apiResponse;
            }
//            LastDataModel lastDataModel1 = lastDataModelList.get(0);
//            String equipmentname = lastDataModel1.getEquipmentname();
            Monitorequipment equipmentByNo = equipmentInfoMapper.getEquipmentByNo(equipmentno);
            String equipmentname = equipmentByNo.getEquipmentname();
            List<String> legend = new ArrayList<String>();
            legend.add(equipmentname);
            List<String> temp = new ArrayList<String>();
            List<String> tempTime = new ArrayList<String>();
            List<String> CO2 = new ArrayList<String>();
            List<String> CO2Time = new ArrayList<String>();
            List<String> O2 = new ArrayList<String>();
            List<String> O2Time = new ArrayList<String>();
            List<String> VOC = new ArrayList<String>();
            List<String> VOCTime = new ArrayList<String>();
            List<String> RH = new ArrayList<String>();
            List<String> RHTime = new ArrayList<String>();
            List<String> PM5 = new ArrayList<String>();
            List<String> PM5Time = new ArrayList<String>();
            List<String> PM05 = new ArrayList<String>();
            List<String> PM05Time = new ArrayList<String>();
            List<String> PM25 = new ArrayList<String>();
            List<String> PM25Time = new ArrayList<String>();
            List<String> PM10 = new ArrayList<String>();
            List<String> PM10Time = new ArrayList<String>();
            List<String> JQ = new ArrayList<String>();
            List<String> JQTime = new ArrayList<String>();
            List<String> PRESS = new ArrayList<String>();
            List<String> PRESSTime = new ArrayList<String>();
            List<String> airflow = new ArrayList<String>();
            List<String> airflowTime = new ArrayList<String>();
            List<String> lefttemp = new ArrayList<String>();
            List<String> lefttempTime = new ArrayList<String>();
            List<String> righttemp = new ArrayList<String>();
            List<String> righttempTime = new ArrayList<String>();
            List<String> temp1 = new ArrayList<String>();
            List<String> temp1Time = new ArrayList<String>();
            List<String> temp2 = new ArrayList<String>();
            List<String> temp2Time = new ArrayList<String>();
            List<String> temp3 = new ArrayList<String>();
            List<String> temp3Time = new ArrayList<String>();
            List<String> temp4 = new ArrayList<String>();
            List<String> temp4Time = new ArrayList<String>();
            List<String> temp5 = new ArrayList<String>();
            List<String> temp5Time = new ArrayList<String>();
            List<String> temp6 = new ArrayList<String>();
            List<String> temp6Time = new ArrayList<String>();
            List<String> temp7 = new ArrayList<String>();
            List<String> temp7Time = new ArrayList<String>();
            List<String> temp8 = new ArrayList<String>();
            List<String> temp8Time = new ArrayList<String>();
            List<String> temp9 = new ArrayList<String>();
            List<String> temp9Time = new ArrayList<String>();
            List<String> temp10 = new ArrayList<String>();
            List<String> temp10Time = new ArrayList<String>();
            List<String> tempdiff = new ArrayList<String>();
            List<String> tempdiffTime = new ArrayList<String>();
            List<String> leftcovertemp = new ArrayList<String>();
            List<String> leftcovertempTime = new ArrayList<String>();
            List<String> leftendtemp = new ArrayList<String>();
            List<String> leftendTime = new ArrayList<String>();
            List<String> leftair = new ArrayList<String>();
            List<String> leftairTime = new ArrayList<String>();
            List<String> rightcovertemp = new ArrayList<String>();
            List<String> rightcovertempTime = new ArrayList<String>();
            List<String> rightendtemp = new ArrayList<String>();
            List<String> rightendtempTime = new ArrayList<String>();
            List<String> rightair = new ArrayList<String>();
            List<String> rightairTime = new ArrayList<String>();
            List<String> n2 = new ArrayList<String>();
            List<String> n2Time = new ArrayList<String>();
            List<String> leftCompartmentHumidity = new ArrayList<String>();
            List<String> leftCompartmentHumidityTime = new ArrayList<String>();
            List<String> rightCompartmentHumidity = new ArrayList<String>();
            List<String> rightCompartmentHumidityTime = new ArrayList<String>();
            for (Monitorequipmentlastdata lastDataModel : lastDataModelList) {
                //       lastDataModel.setInputdatetime(TimeHelper.formats(lastDataModel.getInputdatetime()));
                //液氮罐是否存在差值
                String da = TimeHelper.formats(lastDataModel.getInputdatetime());
                if (StringUtils.isNotEmpty(lastDataModel.getCurrenttemperaturediff())) {
                    tempdiff.add(lastDataModel.getCurrenttemperaturediff());
                    tempdiffTime.add(da);
                }
                //培养箱气流有无值
                if (StringUtils.isNotEmpty(lastDataModel.getCurrentairflow1()) && !exceptionData.contains(lastDataModel.getCurrentairflow1())) {
                    airflow.add(lastDataModel.getCurrentairflow1());
                    airflowTime.add(da);
                }
                //是否存在左舱室温度
                if (StringUtils.isNotEmpty(lastDataModel.getCurrentlefttemperature()) && !exceptionData.contains(lastDataModel.getCurrentlefttemperature())) {
                    lefttemp.add(lastDataModel.getCurrentlefttemperature());
                    lefttempTime.add(da);
                }
                //是否存在右舱室温度
                if (StringUtils.isNotEmpty(lastDataModel.getCurrentrigthtemperature()) && !exceptionData.contains(lastDataModel.getCurrentrigthtemperature())) {
                    righttemp.add(lastDataModel.getCurrentrigthtemperature());
                    righttempTime.add(da);
                }
                //温度1有无值
                if (StringUtils.isNotEmpty(lastDataModel.getCurrenttemperature1()) && !exceptionData.contains(lastDataModel.getCurrenttemperature1())) {
                    temp1.add(lastDataModel.getCurrenttemperature1());
                    temp1Time.add(da);
                }
                //温度2有无值
                if (StringUtils.isNotEmpty(lastDataModel.getCurrenttemperature2()) && !exceptionData.contains(lastDataModel.getCurrenttemperature2())) {
                    temp2.add(lastDataModel.getCurrenttemperature2());
                    temp2Time.add(da);
                }
                //温度3有无值
                if (StringUtils.isNotEmpty(lastDataModel.getCurrenttemperature3()) && !exceptionData.contains(lastDataModel.getCurrenttemperature3())) {
                    temp3.add(lastDataModel.getCurrenttemperature3());
                    temp3Time.add(da);
                }
                //温度4有无值
                if (StringUtils.isNotEmpty(lastDataModel.getCurrenttemperature4()) && !exceptionData.contains(lastDataModel.getCurrenttemperature4())) {
                    temp4.add(lastDataModel.getCurrenttemperature4());
                    temp4Time.add(da);
                }
                //温度5有无值
                if (!StringUtils.isEmpty(lastDataModel.getCurrenttemperature5()) && !exceptionData.contains(lastDataModel.getCurrenttemperature5())) {
                    temp5.add(lastDataModel.getCurrenttemperature5());
                    temp5Time.add(da);
                }
                //温度6有无值
                if (StringUtils.isNotEmpty(lastDataModel.getCurrenttemperature6()) && !exceptionData.contains(lastDataModel.getCurrenttemperature6())) {
                    temp6.add(lastDataModel.getCurrenttemperature6());
                    temp6Time.add(da);
                }
                //温度7有无值
                if (StringUtils.isNotEmpty(lastDataModel.getCurrenttemperature7()) && !exceptionData.contains(lastDataModel.getCurrenttemperature7())) {
                    temp7.add(lastDataModel.getCurrenttemperature7());
                    temp7Time.add(da);
                }
                //温度8有无值
                if (StringUtils.isNotEmpty(lastDataModel.getCurrenttemperature8()) && !exceptionData.contains(lastDataModel.getCurrenttemperature8())) {
                    temp8.add(lastDataModel.getCurrenttemperature8());
                    temp8Time.add(da);
                }
                //温度9有无值
                if (!StringUtils.isEmpty(lastDataModel.getCurrenttemperature9()) && !exceptionData.contains(lastDataModel.getCurrenttemperature9())) {
                    temp9.add(lastDataModel.getCurrenttemperature9());
                    temp9Time.add(da);
                }
                //温度10有无值
                if (StringUtils.isNotEmpty(lastDataModel.getCurrenttemperature10()) && !exceptionData.contains(lastDataModel.getCurrenttemperature10())) {
                    temp10.add(lastDataModel.getCurrenttemperature10());
                    temp10Time.add(da);
                }
                //温度有无值
                if (StringUtils.isNotEmpty(lastDataModel.getCurrenttemperature()) && !exceptionData.contains(lastDataModel.getCurrenttemperature())) {
                    // if (!"A".equals() && !"B".equals(lastDataModel.getCurrenttemperature()) && !"C".equals(lastDataModel.getCurrenttemperature()) && !"D".equals(lastDataModel.getCurrenttemperature()) && !"E".equals(lastDataModel.getCurrenttemperature())) {
                    temp.add(lastDataModel.getCurrenttemperature());
                    tempTime.add(da);

                }
                //CO2有无值
                if (StringUtils.isNotEmpty(lastDataModel.getCurrentcarbondioxide()) && !exceptionData.contains(lastDataModel.getCurrentcarbondioxide())) {
                    //if (!"A".equals(lastDataModel.getCurrentcarbondioxide()) && !"B".equals(lastDataModel.getCurrentcarbondioxide()) && !"C".equals(lastDataModel.getCurrentcarbondioxide()) && !"D".equals(lastDataModel.getCurrentcarbondioxide()) && !"E".equals(lastDataModel.getCurrentcarbondioxide())) {
                    CO2.add(lastDataModel.getCurrentcarbondioxide());
                    CO2Time.add(da);
                    //}
                }
                //氧气有无值
                if (StringUtils.isNotEmpty(lastDataModel.getCurrento2()) && !exceptionData.contains(lastDataModel.getCurrento2())) {
                    // if (!"A".equals(lastDataModel.getCurrento2()) && !"B".equals(lastDataModel.getCurrento2()) && !"C".equals(lastDataModel.getCurrento2()) && !"D".equals(lastDataModel.getCurrento2()) && !"E".equals(lastDataModel.getCurrento2())) {
                    O2.add(lastDataModel.getCurrento2());
                    O2Time.add(da);
                    //}
                }
                //voc 有无值
                if (StringUtils.isNotEmpty(lastDataModel.getCurrentvoc()) && !exceptionData.contains(lastDataModel.getCurrentvoc())) {
                    VOC.add(lastDataModel.getCurrentvoc());
                    VOCTime.add(da);
                }
                //湿度有无值
                if (StringUtils.isNotEmpty(lastDataModel.getCurrenthumidity()) && !exceptionData.contains(lastDataModel.getCurrenthumidity())) {
                    RH.add(lastDataModel.getCurrenthumidity());
                    RHTime.add(da);
                }
                //pm2.5有无值
                if (StringUtils.isNotEmpty(lastDataModel.getCurrentpm25()) && !exceptionData.contains(lastDataModel.getCurrentpm25())) {
                    PM25.add(lastDataModel.getCurrentpm25());
                    PM25Time.add(da);
                }
                //pm5有无值
                if (StringUtils.isNotEmpty(lastDataModel.getCurrentpm5()) && !exceptionData.contains(lastDataModel.getCurrentpm5())) {
                    PM5.add(lastDataModel.getCurrentpm5());
                    PM5Time.add(da);
                }
                //pm05有无值
                if (StringUtils.isNotEmpty(lastDataModel.getCurrentpm05()) && !exceptionData.contains(lastDataModel.getCurrentpm05())) {
                    PM05.add(lastDataModel.getCurrentpm05());
                    PM05Time.add(da);
                }
                //PM10有无值
                if (StringUtils.isNotEmpty(lastDataModel.getCurrentpm10()) && !exceptionData.contains(lastDataModel.getCurrentpm10())) {
                    PM10.add(lastDataModel.getCurrentpm10());
                    PM10Time.add(da);
                }
                //甲醛有无值
                if (StringUtils.isNotEmpty(lastDataModel.getCurrentformaldehyde()) && !exceptionData.contains(lastDataModel.getCurrentformaldehyde())) {
                    JQ.add(lastDataModel.getCurrentformaldehyde());
                    JQTime.add(da);
                }
                //压力有无值
                if (StringUtils.isNotEmpty(lastDataModel.getCurrentairflow()) && !exceptionData.contains(lastDataModel.getCurrentairflow())) {
                    PRESS.add(lastDataModel.getCurrentairflow());
                    PRESSTime.add(da);
                }
                //左盖板温度有无值
                if (StringUtils.isNotEmpty(lastDataModel.getCurrentleftcovertemperature()) && !exceptionData.contains(lastDataModel.getCurrentleftcovertemperature())) {
                    leftcovertemp.add(lastDataModel.getCurrentleftcovertemperature());
                    leftcovertempTime.add(da);
                }
                // 左底板温度有无值
                if (StringUtils.isNotEmpty(lastDataModel.getCurrentleftendtemperature()) && !exceptionData.contains(lastDataModel.getCurrentleftendtemperature())) {
                    leftendtemp.add(lastDataModel.getCurrentleftendtemperature());
                    leftendTime.add(da);
                }
                //左气流
                if (StringUtils.isNotEmpty(lastDataModel.getCurrentleftairflow()) && !exceptionData.contains(lastDataModel.getCurrentleftairflow())) {
                    leftair.add(lastDataModel.getCurrentleftairflow());
                    leftairTime.add(da);
                }
                //右盖板温度
                if (StringUtils.isNotEmpty(lastDataModel.getCurrentrightcovertemperature())) {
                    rightcovertemp.add(lastDataModel.getCurrentrightcovertemperature());
                    rightcovertempTime.add(da);
                }
                //右底板温度
                if (StringUtils.isNotEmpty(lastDataModel.getCurrentrightendtemperature()) && !exceptionData.contains(lastDataModel.getCurrentrightendtemperature())) {
                    rightendtemp.add(lastDataModel.getCurrentrightendtemperature());
                    rightendtempTime.add(da);
                }
                //右气流
                if (StringUtils.isNotEmpty(lastDataModel.getCurrentrightairflow()) && !exceptionData.contains(lastDataModel.getCurrentrightairflow())) {
                    rightair.add(lastDataModel.getCurrentrightairflow());
                    rightairTime.add(da);
                }
                //N2 有无值
                if (StringUtils.isNotEmpty(lastDataModel.getCurrentn2()) && !exceptionData.contains(lastDataModel.getCurrentn2())) {
                    n2.add(lastDataModel.getCurrentn2());
                    n2Time.add(da);
                }
                if (StringUtils.isNotEmpty(lastDataModel.getLeftCompartmentHumidity())&& !exceptionData.contains(lastDataModel.getLeftCompartmentHumidity())) {
                    leftCompartmentHumidity.add(lastDataModel.getLeftCompartmentHumidity());
                    leftCompartmentHumidityTime.add(da);
                }
                if (StringUtils.isNotEmpty(lastDataModel.getRightCompartmentHumidity())&& !exceptionData.contains(lastDataModel.getRightCompartmentHumidity())) {
                    rightCompartmentHumidity.add(lastDataModel.getRightCompartmentHumidity());
                    rightCompartmentHumidityTime.add(da);
                }
            }
            if (CollectionUtils.isNotEmpty(n2)) {
                CurveDataModel curveDataModel = generateCurveDataModel(n2,n2Time);
                curveInfoModel.setN2(curveDataModel);
            }
            if (CollectionUtils.isNotEmpty(leftcovertemp)) {
                CurveDataModel curveDataModel = generateCurveDataModel(leftcovertemp,leftcovertempTime);
                curveInfoModel.setLeftcovertemp(curveDataModel);
            }
            if (CollectionUtils.isNotEmpty(leftendtemp)) {
                CurveDataModel curveDataModel = generateCurveDataModel(leftendtemp,leftendTime);
                curveInfoModel.setLeftendtemp(curveDataModel);
            }
            if (CollectionUtils.isNotEmpty(leftair)) {
                CurveDataModel curveDataModel = generateCurveDataModel(leftair,leftairTime);
                curveInfoModel.setLeftair(curveDataModel);
            }
            if (CollectionUtils.isNotEmpty(rightcovertemp)) {
                CurveDataModel curveDataModel = generateCurveDataModel(rightcovertemp,rightcovertempTime);
                curveInfoModel.setRightcovertemp(curveDataModel);
            }
            if (CollectionUtils.isNotEmpty(rightendtemp)) {
                CurveDataModel curveDataModel = generateCurveDataModel(rightendtemp, rightendtempTime);
                curveInfoModel.setRightendtemp(curveDataModel);
            }
            if (CollectionUtils.isNotEmpty(rightair)) {
                CurveDataModel curveDataModel = generateCurveDataModel(rightair, rightairTime);
                curveInfoModel.setRightair(curveDataModel);
            }
            if (CollectionUtils.isNotEmpty(tempdiff)) {
                CurveDataModel curveDataModel = generateCurveDataModel(tempdiff, tempdiffTime);
                curveInfoModel.setDifftemp(curveDataModel);
            }
            if (CollectionUtils.isNotEmpty(airflow)) {
                CurveDataModel curveDataModel = generateCurveDataModel(airflow, airflowTime);
                curveInfoModel.setAirflow(curveDataModel);
            }
            if (CollectionUtils.isNotEmpty(lefttemp)) {
                CurveDataModel curveDataModel = generateCurveDataModel(lefttemp, lefttempTime);
                curveInfoModel.setLefttemp(curveDataModel);
            }
            if (CollectionUtils.isNotEmpty(righttemp)) {
                CurveDataModel curveDataModel = generateCurveDataModel(righttemp, righttempTime);
                curveInfoModel.setRighttemp(curveDataModel);
            }
            if (CollectionUtils.isNotEmpty(temp1)) {
                CurveDataModel curveDataModel = generateCurveDataModel(temp1, temp1Time);
                curveInfoModel.setTemp1(curveDataModel);
            }
            if (CollectionUtils.isNotEmpty(temp2)) {
                CurveDataModel curveDataModel = generateCurveDataModel(temp2, temp2Time);
                curveInfoModel.setTemp2(curveDataModel);
            }
            if (CollectionUtils.isNotEmpty(temp3)) {
                CurveDataModel curveDataModel = generateCurveDataModel(temp3, temp3Time);
                curveInfoModel.setTemp3(curveDataModel);
            }
            if (CollectionUtils.isNotEmpty(temp4)) {
                CurveDataModel curveDataModel = generateCurveDataModel(temp4, temp4Time);
                curveInfoModel.setTemp4(curveDataModel);
            }
            if (CollectionUtils.isNotEmpty(temp5)) {
                CurveDataModel curveDataModel = generateCurveDataModel(temp5, temp5Time);
                curveInfoModel.setTemp5(curveDataModel);
            }
            if (CollectionUtils.isNotEmpty(temp6)) {
                CurveDataModel curveDataModel = generateCurveDataModel(temp6, temp6Time);
                curveInfoModel.setTemp6(curveDataModel);
            }
            if (CollectionUtils.isNotEmpty(temp7)) {
                CurveDataModel curveDataModel = generateCurveDataModel(temp7, temp7Time);
                curveInfoModel.setTemp7(curveDataModel);
            }
            if (CollectionUtils.isNotEmpty(temp8)) {
                CurveDataModel curveDataModel = generateCurveDataModel(temp8, temp8Time);
                curveInfoModel.setTemp8(curveDataModel);
            }
            if (CollectionUtils.isNotEmpty(temp9)) {
                CurveDataModel curveDataModel = generateCurveDataModel(temp9, temp9Time);
                curveInfoModel.setTemp9(curveDataModel);
            }
            if (CollectionUtils.isNotEmpty(temp10)) {
                CurveDataModel curveDataModel = generateCurveDataModel(temp10, temp10Time);
                curveInfoModel.setTemp10(curveDataModel);
            }
            if (CollectionUtils.isNotEmpty(temp)) {
                CurveDataModel curveDataModel = generateCurveDataModel(temp, tempTime);
                curveInfoModel.setTemp(curveDataModel);
            }
            if (CollectionUtils.isNotEmpty(CO2)) {
                CurveDataModel curveDataModel = generateCurveDataModel(CO2, CO2Time);
                curveInfoModel.setCo2(curveDataModel);
            }
            if (CollectionUtils.isNotEmpty(O2)) {
                CurveDataModel curveDataModel = generateCurveDataModel(O2, O2Time);
                curveInfoModel.setO2(curveDataModel);
            }
            if (CollectionUtils.isNotEmpty(VOC)) {
                CurveDataModel curveDataModel = generateCurveDataModel(VOC, VOCTime);
                curveInfoModel.setVoc(curveDataModel);
            }
            if (CollectionUtils.isNotEmpty(RH)) {
                CurveDataModel curveDataModel = generateCurveDataModel(RH, RHTime);
                curveInfoModel.setRh(curveDataModel);
            }
            if (CollectionUtils.isNotEmpty(PM25)) {
                CurveDataModel curveDataModel = generateCurveDataModel(PM25, PM25Time);
                curveInfoModel.setPm25(curveDataModel);
            }
            if (CollectionUtils.isNotEmpty(PM5)) {
                CurveDataModel curveDataModel = generateCurveDataModel(PM5, PM5Time);
                curveInfoModel.setPm5(curveDataModel);
            }
            if (CollectionUtils.isNotEmpty(PM05)) {
                CurveDataModel curveDataModel = generateCurveDataModel(PM05, PM05Time);
                curveInfoModel.setPm05(curveDataModel);
            }
            if (CollectionUtils.isNotEmpty(PM10)) {
                CurveDataModel curveDataModel = generateCurveDataModel(PM10, PM10Time);
                curveInfoModel.setPm10(curveDataModel);
            }
            if (CollectionUtils.isNotEmpty(JQ)) {
                CurveDataModel curveDataModel = generateCurveDataModel(JQ, JQTime);
                curveInfoModel.setJq(curveDataModel);
            }
            if (CollectionUtils.isNotEmpty(PRESS)) {
                CurveDataModel curveDataModel = generateCurveDataModel(PRESS, PRESSTime);
                curveInfoModel.setPress(curveDataModel);
            }
            if (CollectionUtils.isNotEmpty(leftCompartmentHumidity)) {
                CurveDataModel curveDataModel = generateCurveDataModel(leftCompartmentHumidity, leftCompartmentHumidityTime);
                curveInfoModel.setLeftCompartmentHumidity(curveDataModel);
            }
            if (CollectionUtils.isNotEmpty(rightCompartmentHumidity)) {
                CurveDataModel curveDataModel = generateCurveDataModel(rightCompartmentHumidity, rightCompartmentHumidityTime);
                curveInfoModel.setRightCompartmentHumidity(curveDataModel);
            }
            if (ObjectUtils.isEmpty(curveInfoModel)) {
                apiResponse.setCode(ApiResponse.FAILED);
                apiResponse.setMessage("无当前曲线数据");
                return apiResponse;
            }
            apiResponse.setResult(curveInfoModel);
            return apiResponse;
        } catch (Exception e) {
            LOGGER.error("失败：" + e.getMessage());
            apiResponse.setMessage("服务异常");
            apiResponse.setCode(ApiResponse.FAILED);
            return apiResponse;
        }
    }

    private CurveDataModel generateCurveDataModel(List<String> listdata, List<String> listtime){
        CurveDataModel curveDataModel = new CurveDataModel();
        curveDataModel.setXaxis(listtime);
        SeriesDataModel seriesDataModel = new SeriesDataModel();
        seriesDataModel.setDate(listdata);
        List<SeriesDataModel> seriesDataModelList = new ArrayList<SeriesDataModel>();
        seriesDataModelList.add(seriesDataModel);
        curveDataModel.setSeries(seriesDataModelList);
        return  curveDataModel;
    }

    @Override
    public ApiResponse<CurveInfoModel> getCurveSecond(String date, List<String> equipmentnoList, String type) {
        ApiResponse<CurveInfoModel> apiResponse = new ApiResponse<CurveInfoModel>();
//        CurveInfoModel curveInfoModel = new CurveInfoModel();
//        List<List<LastDataModel>> lastDataModelLists = new ArrayList<List<LastDataModel>>();
//        CurvelReqeustModel curvelReqeustModel = new CurvelReqeustModel();
//        curvelReqeustModel.setDate(date);
//        try {
//            for (String equipmentno : equipmentnoList) {
//                curvelReqeustModel.setEquipmentno(equipmentno);
//                List<LastDataModel> lastDataModelList = new ArrayList<LastDataModel>();
//                lastDataModelList = equipmentInfoMapper.getCurveInfo(curvelReqeustModel);
//                if (!CollectionUtils.isEmpty(lastDataModelList)) {
//                    lastDataModelLists.add(lastDataModelList);
//                }
//            }
//            if (CollectionUtils.isEmpty(lastDataModelLists)) {
//                apiResponse.setCode(ApiResponse.FAILED);
//                apiResponse.setMessage("无当前曲线数据");
//                return apiResponse;
//            }
//            List<String> tempTime = new ArrayList<String>();
//            List<String> CO2Time = new ArrayList<String>();
//            List<String> O2Time = new ArrayList<String>();
//            List<String> legend = new ArrayList<String>();
//            List<SeriesDataModel> seriesDataModelTEMPList = new ArrayList<SeriesDataModel>();
//            List<SeriesDataModel> seriesDataModelCO2List = new ArrayList<SeriesDataModel>();
//            List<SeriesDataModel> seriesDataModelO2List = new ArrayList<SeriesDataModel>();
//            for (List<LastDataModel> lastDataModels : lastDataModelLists) {
//                legend.add(lastDataModels.get(0).getEquipmentname());
//                List<String> temp = new ArrayList<String>();
//                List<String> CO2 = new ArrayList<String>();
//                List<String> O2 = new ArrayList<String>();
//                tempTime = new ArrayList<String>();
//                O2Time = new ArrayList<String>();
//                CO2Time = new ArrayList<String>();
//                for (LastDataModel model : lastDataModels) {
//
//                    switch (type) {
//                        case "0":
//                            //温度有无值
//                            if (!StringUtils.isEmpty(model.getCurrenttemperature())) {
//                                temp.add(model.getCurrenttemperature());
//                                tempTime.add(model.getInputdatetime());
//                            }
//                            //CO2有无值
//                            if (!StringUtils.isEmpty(model.getCurrentcarbondioxide())) {
//                                CO2.add(model.getCurrentcarbondioxide());
//                                CO2Time.add(model.getInputdatetime());
//                            }
//                            //氧气有无值
//                            if (!StringUtils.isEmpty(model.getCurrento2())) {
//                                O2.add(model.getCurrento2());
//                                O2Time.add(model.getInputdatetime());
//                            }
//                            break;
//                        case "1":
//                            //氧气有无值
//                            if (!StringUtils.isEmpty(model.getCurrento2())) {
//                                O2.add(model.getCurrento2());
//                                O2Time.add(model.getInputdatetime());
//                            }
//                            break;
//                        case "2":
//                            if (!StringUtils.isEmpty(model.getCurrentcarbondioxide())) {
//                                CO2.add(model.getCurrentcarbondioxide());
//                                CO2Time.add(model.getInputdatetime());
//                            }
//                            break;
//                        case "3":
//                            //温度有无值
//                            if (!StringUtils.isEmpty(model.getCurrenttemperature())) {
//                                temp.add(model.getCurrenttemperature());
//                                tempTime.add(model.getInputdatetime());
//                            }
//
//                    }
//                }
//                if (!CollectionUtils.isEmpty(temp)) {
//                    SeriesDataModel seriesDataModel = new SeriesDataModel();
//                    seriesDataModel.setName(lastDataModels.get(0).getEquipmentname());
//                    seriesDataModel.setDate(temp);
//                    seriesDataModelTEMPList.add(seriesDataModel);
//
//                }
//                if (!CollectionUtils.isEmpty(CO2)) {
//                    SeriesDataModel seriesDataModel = new SeriesDataModel();
//                    seriesDataModel.setName(lastDataModels.get(0).getEquipmentname());
//                    seriesDataModel.setDate(CO2);
//                    seriesDataModelCO2List.add(seriesDataModel);
//
//                }
//                if (!CollectionUtils.isEmpty(O2)) {
//                    SeriesDataModel seriesDataModel = new SeriesDataModel();
//                    seriesDataModel.setName(lastDataModels.get(0).getEquipmentname());
//                    seriesDataModel.setDate(O2);
//                    seriesDataModelO2List.add(seriesDataModel);
//
//                }
//
//            }
//            if (!CollectionUtils.isEmpty(seriesDataModelTEMPList)) {
//                CurveDataModel curveDataModel = new CurveDataModel();
//                curveDataModel.setLegend(legend);
//                curveDataModel.setXaxis(tempTime);
//                curveDataModel.setSeries(seriesDataModelTEMPList);
//                curveInfoModel.setTemp(curveDataModel);
//            }
//            if (!CollectionUtils.isEmpty(seriesDataModelO2List)) {
//                CurveDataModel curveDataModel = new CurveDataModel();
//                curveDataModel.setLegend(legend);
//                curveDataModel.setXaxis(O2Time);
//                curveDataModel.setSeries(seriesDataModelO2List);
//                curveInfoModel.setO2(curveDataModel);
//            }
//            if (!CollectionUtils.isEmpty(seriesDataModelCO2List)) {
//                CurveDataModel curveDataModel = new CurveDataModel();
//                curveDataModel.setLegend(legend);
//                curveDataModel.setXaxis(CO2Time);
//                curveDataModel.setSeries(seriesDataModelCO2List);
//                curveInfoModel.setCo2(curveDataModel);
//            }
//            apiResponse.setResult(curveInfoModel);
//            return apiResponse;
//        } catch (Exception e) {
//            LOGGER.error("失败：" + e.getMessage());
//            apiResponse.setMessage("服务异常");
//            apiResponse.setCode(ApiResponse.FAILED);
//            return apiResponse;
//        }
        return null;
    }

    @Override
    public ApiResponse<List<QueryInfoModel>> queryEquipmentMonitorInfo(String equipmenttypeid, String equipmentno, String hospitalcode, String startdate, String enddate) {
        ApiResponse<List<QueryInfoModel>> apiResponse = new ApiResponse<List<QueryInfoModel>>();
        List<QueryInfoModel> queryInfoModels = new ArrayList<QueryInfoModel>();
        List<Monitorequipment> monitorequipmentList = new ArrayList<Monitorequipment>();
        String search = "monitorequipmentlastdata";
        Monitorequipment monitorequipment = new Monitorequipment();
        try {
            boolean flag = TimeHelper.isCurrentMonth(startdate);
            if (!flag) {
                // 当前月份是几月就是几月
                String year = startdate.substring(0, 4);
                String month = startdate.substring(5, 7);
                search = "monitorequipmentlastdata" + "_" + year + month;
            }
            //根据设备编号查询设备信息
            monitorequipment = equipmentInfoMapper.getEquipmentByNo(equipmentno);
            if (ObjectUtils.isEmpty(monitorequipment)) {
                apiResponse.setMessage("查询不到设备信息");
                apiResponse.setCode(ApiResponse.FAILED);
                return apiResponse;
            }
            monitorequipmentList.add(monitorequipment);
            for (Monitorequipment monitorequipment1 : monitorequipmentList) {
                QueryInfoModel queryInfoModel = new QueryInfoModel();
                queryInfoModel.setEquipmentname(monitorequipment1.getEquipmentname());
                CurvelReqeustModel curvelReqeustModel = new CurvelReqeustModel();
                curvelReqeustModel.setEquipmentno(monitorequipment1.getEquipmentno());
                curvelReqeustModel.setStartdate(startdate);
                curvelReqeustModel.setEnddate(enddate);
                curvelReqeustModel.setSearch(search);
                List<Monitorequipmentlastdata1> monitorequipmentlastdataList = new ArrayList<Monitorequipmentlastdata1>();
                monitorequipmentlastdataList = equipmentInfoMapper.getSearchInfo(curvelReqeustModel);
                queryInfoModel.setMonitorequipmentlastdata(monitorequipmentlastdataList);
                queryInfoModels.add(queryInfoModel);
            }
            if (CollectionUtils.isEmpty(queryInfoModels)) {
                apiResponse.setMessage("查询不到设备监控信息值");
                apiResponse.setCode(ApiResponse.FAILED);
                return apiResponse;
            }
            apiResponse.setResult(queryInfoModels);
            return apiResponse;
        } catch (Exception e) {
            LOGGER.error("失败：" + e.getMessage());
            apiResponse.setMessage("服务异常");
            apiResponse.setCode(ApiResponse.FAILED);
            return apiResponse;
        }
    }

    @Override
    public ApiResponse<String> exportExcle(String equipmenttypeid, String equipmentno, String hospitalcode, String startdate, String enddate, HttpServletResponse response) {
        ApiResponse<String> apiResponse = new ApiResponse<String>();
        try {
            Params param = new Params();
            if (StringUtils.isNotEmpty(equipmenttypeid)) {
                param.setEquipmenttypeid(equipmenttypeid);
            }
            param.setEquipmentno(equipmentno);
            param.setHospitalcode(hospitalcode);
            param.setStartdate(startdate);
            param.setEnddate(enddate);
            String search = "monitorequipmentlastdata";
            // 判断当前日期是否为当前月份
            boolean flag = TimeHelper.isCurrentMonth(startdate);
            if (!flag) {
                // 当前月份是几月就是几月
                String year = startdate.substring(0, 4);
                String month = startdate.substring(5, 7);
                search = "monitorequipmentlastdata" + "_" + year + month;
            }
            String type = null;
            if (startdate.equals(enddate)) {
                type = startdate;
            } else {
                type = startdate + "----" + enddate;
            }
            param.setSearch(search);
            param.setType(type);
            String s = JsonUtil.toJson(param);
            LOGGER.info(s);
            if (StringUtils.isEmpty(equipmenttypeid)) {
                // 单个设备导出 ：
                // HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();

                //  String equipmentno = param.getEquipmentno();
                Monitorequipment monitorequipment = new Monitorequipment();
                monitorequipment = equipmentInfoMapper.getEquipmentByNo(equipmentno);
                //   String equipmenttypeid = monitorequipment.getEquipmenttypeid();
                if (StringUtils.equals("1", monitorequipment.getEquipmenttypeid())) {
                    // 环境
                    EnvironmentListModel hjExcleInfo = equipmentInfoMapper.getHJExcleInfo(param);
                    if (hjExcleInfo == null) {
                        apiResponse.setMessage("当前设备查询时间段内无数据");
                        apiResponse.setCode(ApiResponse.FAILED);
                        return apiResponse;
                    }
                    List<EnvironmentModel> environmentModelList = hjExcleInfo.getEnvironmentModelList();

                    // 导出excle
                    //       String type = param.getType();
                    // HttpServletResponse response = param.getResponse();
                    FileUtil.exportExcel(environmentModelList, monitorequipment.getEquipmentname() + type + "监控数据汇总", "sheet1",
                            EnvironmentModel.class, monitorequipment.getEquipmentname() + "-" + type + "监控数据汇总.xls", response);
                } else if (StringUtils.equals("2", monitorequipment.getEquipmenttypeid())) {
                    //培养箱
                    IncubatorListModel pyxExcleInfo = equipmentInfoMapper.getPyxExcleInfo(param);
                    if (pyxExcleInfo == null) {
                        apiResponse.setMessage("当前设备查询时间段内无数据");
                        apiResponse.setCode(ApiResponse.FAILED);
                        return apiResponse;
                    }
                    List<IncubatorModel> incubatorModelList = pyxExcleInfo.getIncubatorModelList();
                    //    String type = param.getType();
                    //  HttpServletResponse response = param.getResponse();
                    FileUtil.exportExcel(incubatorModelList, monitorequipment.getEquipmentname() + type + "监控数据汇总", "sheet1",
                            IncubatorModel.class, monitorequipment.getEquipmentname() + "-" + type + "监控数据汇总.xls", response);
                } else {
                    //其余
                    OtherListModel otherExcleInfo = equipmentInfoMapper.getOtherExcleInfo(param);
                    if (otherExcleInfo == null) {
                        apiResponse.setMessage("当前设备查询时间段内无数据");
                        apiResponse.setCode(ApiResponse.FAILED);
                        return apiResponse;
                    }
                    List<OtherModel> otherModels = otherExcleInfo.getOtherModels();

                    //       String type = param.getType();
                    //    HttpServletResponse response = param.getResponse();
                    FileUtil.exportExcel(otherModels, monitorequipment.getEquipmentname() + type + "监控数据汇总", "sheet1",
                            OtherModel.class, monitorequipment.getEquipmentname() + "-" + type + "监控数据汇总.xls", response);
                }

            } else {
                //       HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
                //       Params param = JsonUtil.toBean(message, Params.class);
                //        String equipmenttypeid = param.getEquipmenttypeid();
                //       HttpServletResponse response = param.getResponse();
                //        String type = param.getType();
                String equipmenttypename = "环境";
                switch (equipmenttypeid) {
                    case "1":
                        equipmenttypename = "环境";
                        break;
                    case "2":
                        equipmenttypename = "培养箱";
                        break;
                    case "3":
                        equipmenttypename = "液氮罐";
                        break;
                    case "4":
                        equipmenttypename = "冰箱";
                        break;
                    case "5":
                        equipmenttypename = "操作台";
                        break;
                }
                List<List<?>> lists = new ArrayList<List<?>>();
                List<String> sheetList = new ArrayList<String>();
                List<String> titleList = new ArrayList<String>();
                if (StringUtils.equals("1", equipmenttypeid)) {
                    List<EnvironmentListModel> hjExcleInfoById = equipmentInfoMapper.getHJExcleInfoById(param);
                    if (CollectionUtils.isEmpty(hjExcleInfoById)) {
                        apiResponse.setMessage("当前设备查询时间段内无数据");
                        apiResponse.setCode(ApiResponse.NOT_FOUND);
                        return apiResponse;
                    }
                    for (EnvironmentListModel environmentListModel : hjExcleInfoById) {
                        sheetList.add(environmentListModel.getEquipmentname() + "-" + type + "监控数据汇总");
                        titleList.add(environmentListModel.getEquipmentname());
                        List<EnvironmentModel> environmentModels = environmentListModel.getEnvironmentModelList();

                        // 时间排序
                        Collections.sort(environmentModels, new Comparator<EnvironmentModel>() {
                            @Override
                            public int compare(EnvironmentModel o1, EnvironmentModel o2) {
                                return o1.getInputdatetime().compareTo(o2.getInputdatetime());
                            }
                        });
                        lists.add(environmentModels);
                    }
                    FileUtil.exportExcleSheets(lists, titleList, sheetList, EnvironmentModel.class, equipmenttypename + "-" + type + "监控数据汇总.xls", response);
                } else if (StringUtils.equals("2", equipmenttypeid)) {
                    List<IncubatorListModel> pyxExcleInfoById = equipmentInfoMapper.getPyxExcleInfoById(param);
                    if (CollectionUtils.isEmpty(pyxExcleInfoById)) {
                        apiResponse.setMessage("当前设备查询时间段内无数据");
                        apiResponse.setCode(ApiResponse.NOT_FOUND);
                        return apiResponse;
                    }
                    for (IncubatorListModel incubatorListModel : pyxExcleInfoById) {
                        sheetList.add(incubatorListModel.getEquipmentname() + "-" + type + "监控数据汇总");
                        titleList.add(incubatorListModel.getEquipmentname());
                        List<IncubatorModel> incubatorModelList = incubatorListModel.getIncubatorModelList();
                        Collections.sort(incubatorModelList, new Comparator<IncubatorModel>() {
                            @Override
                            public int compare(IncubatorModel o1, IncubatorModel o2) {
                                return o1.getInputdatetime().compareTo(o2.getInputdatetime());
                            }
                        });
                        lists.add(incubatorModelList);
                    }
                    FileUtil.exportExcleSheets(lists, titleList, sheetList, IncubatorModel.class, equipmenttypename + "-" + type + "监控数据汇总.xls", response);
                } else {
                    List<OtherListModel> otherExcleInfoById = equipmentInfoMapper.getOtherExcleInfoById(param);
                    if (CollectionUtils.isEmpty(otherExcleInfoById)) {
                        apiResponse.setMessage("当前设备查询时间段内无数据");
                        apiResponse.setCode(ApiResponse.NOT_FOUND);
                        return apiResponse;
                    }
                    for (OtherListModel otherListModel : otherExcleInfoById) {
                        sheetList.add(otherListModel.getEquipmentname() + "-" + type + "监控数据汇总");
                        titleList.add(otherListModel.getEquipmentname());
                        List<OtherModel> otherModels = otherListModel.getOtherModels();
                        Collections.sort(otherModels, new Comparator<OtherModel>() {
                            @Override
                            public int compare(OtherModel o1, OtherModel o2) {
                                return o1.getInputdatetime().compareTo(o2.getInputdatetime());
                            }
                        });
                        lists.add(otherModels);
                    }
                    FileUtil.exportExcleSheets(lists, titleList, sheetList, OtherModel.class, equipmenttypename + "-" + type + "监控数据汇总.xls", response);
                }
            }
            return apiResponse;
        } catch (Exception e) {
            LOGGER.error("失败：" + e.getMessage());
            apiResponse.setMessage("服务异常");
            apiResponse.setCode(ApiResponse.FAILED);
            return apiResponse;
        }
    }

    @Override
    public ApiResponse<List<EquipmentConfigInfoModel>> showEquipmentConfigInfo(String hospitalcode) {
        ApiResponse<List<EquipmentConfigInfoModel>> apiResponse = new ApiResponse<List<EquipmentConfigInfoModel>>();
        List<EquipmentConfigInfoModel> equipmentConfigInfoModelList = new ArrayList<EquipmentConfigInfoModel>();
        try {
            equipmentConfigInfoModelList = equipmentInfoMapper.showEquipmentConfigInfo(hospitalcode);
            if (CollectionUtils.isEmpty(equipmentConfigInfoModelList)) {
                apiResponse.setMessage("当前医院无设备");
                apiResponse.setCode(ApiResponse.FAILED);
                return apiResponse;
            }
            apiResponse.setResult(equipmentConfigInfoModelList);
            return apiResponse;

        } catch (Exception e) {
            LOGGER.error("失败：" + e.getMessage());
            apiResponse.setMessage("服务异常");
            apiResponse.setCode(ApiResponse.FAILED);
            return apiResponse;
        }
    }

    @Override
    public ApiResponse<Monitorequipment> showInfoByEquipmentNo(String equipmentno) {
        ApiResponse<Monitorequipment> apiResponse = new ApiResponse<Monitorequipment>();
        List<Monitorinstrument> list = new ArrayList<Monitorinstrument>();
        Monitorequipment monitorequipment = new Monitorequipment();
//        if (CollectionUtils.isEmpty(list)){
//            apiResponse.setMessage("当前无数据");
//            apiResponse.setCode(ApiResponse.FAILED);
//            return apiResponse;
//        }
        //循环去取redis里面数据
        HashOperations<Object, Object, Object> objectObjectObjectHashOperations = redisTemplateUtil.opsForHash();
        try {
            monitorequipment = equipmentInfoMapper.getEquipmentByNo(equipmentno);
            if (ObjectUtils.isEmpty(monitorequipment)) {
                apiResponse.setMessage("不存在当前设备信息");
                apiResponse.setCode(ApiResponse.FAILED);
                return apiResponse;
            }
            String sn = equipmentInfoMapper.getSn(monitorequipment.getEquipmentno());
            monitorequipment.setSn(sn);
            String lastdata = (String) objectObjectObjectHashOperations.get("LASTDATA", monitorequipment.getEquipmentno());
            if (StringUtils.isNotEmpty(lastdata)) {
                Monitorequipmentlastdata monitorequipmentlastdata = JsonUtil.toBean(lastdata, Monitorequipmentlastdata.class);
                monitorequipment.setMonitorequipmentlastdata(monitorequipmentlastdata);
            } else {
                apiResponse.setMessage("不存在当前值信息");
                apiResponse.setCode(ApiResponse.FAILED);
                return apiResponse;
            }
            apiResponse.setResult(monitorequipment);
            return apiResponse;
        } catch (Exception e) {
            LOGGER.error("MON中从redis取环境当前值发生异常，异常原因：" + e.getMessage());
            apiResponse.setMessage("服务异常，请联系管理员");
            apiResponse.setCode(ApiResponse.FAILED);
            return apiResponse;
        }
    }

    @Override
    public ApiResponse<String> exportExcleOne(HttpServletResponse response, String hospitalcode, String operationdate,String type) {
        String search = "monitorequipmentlastdata";
        // 判断当前日期是否为当前月份
        boolean flag = TimeHelper.isCurrentMonth(operationdate);
        String year = operationdate.substring(0, 4);
        String month = operationdate.substring(5, 7);
        if (!flag) {
            // 当前月份是几月就是几月

            search = "monitorequipmentlastdata" + "_" + year + month;
        }
        //获取上一小时时间：
        String s = TimeHelper.dateReduce(operationdate);
        String s1 = TimeHelper.dateReduceHHmm(operationdate);
        String s2 = TimeHelper.dateReduceHHmm(s);
        Params params = new Params(hospitalcode, s2, s1, search);
        ApiResponse<String> apiResponse = new ApiResponse<>();
        String hjs = "环境";
        String pyxs = "培养箱";
        String ydgs = "液氮罐";
        String bxs = "冰箱";
        String czts = "操作台";
        Hospitalofreginfo one = hospitaDao.getOne(hospitalcode);
        if (StringUtils.equals(type,"月")) {
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
            List<OtherExcleModel> otherExcleModeltwo = new ArrayList<OtherExcleModel>();
            //当月数据
            List<SingleTimeEquipmentModel> info = equipmentInfoMapper.getSingleEquipmentInfoByHospitalCode(params);
            for (SingleTimeEquipmentModel sing : info) {
                String equipmenttypeid = sing.getEquipmenttypeid();
                //      String equipmentname = sing.getEquipmentname();
                if (StringUtils.isNotEmpty(equipmenttypeid)) {
                    switch (equipmenttypeid) {
                        case "1":
                            HjExcleModel hj = getHj(sing);
                            hjExcleModels.add(hj);
//                        if (!sheetList.contains(hjs)) {
//                            sheetList.add(hjs);
//                            titleList.add(hjs);
//                        }
                            break;
                        case "2":
                            PyxExcleModel pyx = getPyx(sing);
                            pyxExcleModels.add(pyx);
//                        if (!sheetList.contains(pyxs)) {
//                            sheetList.add(pyxs);
//                            titleList.add(pyxs);
//                        }
                            break;

                        case "3":
                            OtherExcleModel other = getOther(sing);
                            otherExcleModels.add(other);
//                        if (!sheetList.contains(ydgs)) {
//                            sheetList.add(ydgs);
//                            titleList.add(ydgs);
//                        }
                            break;
                        case "4":
                            OtherExcleModel otherOne = getOther(sing);
                            otherExcleModelone.add(otherOne);
//                        if (!sheetList.contains(bxs)) {
//                            sheetList.add(bxs);
//                            titleList.add(bxs);
//                        }
                            break;
                        case "5":
                            OtherExcleModel otherTwo = getOther(sing);
                            otherExcleModeltwo.add(otherTwo);
//                        if (!sheetList.contains(czts)) {
//                            sheetList.add(czts);
//                            titleList.add(czts);
//                        }
                            break;
                    }
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
                    one.getHospitalname() +year +"-"+ month+ "月监控数据.xls", response);
            return apiResponse;

        }

        params.setStartdate(s);
        params.setEnddate(operationdate);
        //当天数据
        List<SingleTimeEquipmentModel> infos = equipmentInfoMapper.getSingleEquipmentInfoByHospitalCodeByDay(params);
//        if (info.isEmpty()) {
//            apiResponse.setCode(ApiResponse.NOT_FOUND);
//            apiResponse.setMessage("当前时间医院设备未上传数据");
//            return apiResponse;
//        }
        //根据医院编号获取医院名称

//        FileUtil.exportExcel(info, one.getHospitalname() + "监控数据汇总", one.getHospitalname(),
//                SingleTimeEquipmentModel.class, one.getHospitalname() + "监控数据汇总.xls", response);
        List<List<?>> listss = new ArrayList<List<?>>();
        List<String> sheetLists = new ArrayList<String>();
        List<String> titleLists = new ArrayList<String>();
        List<Class<?>> classLists = new ArrayList<Class<?>>();
        List<HjExcleModel> hjExcleModelss = new ArrayList<HjExcleModel>();
        List<PyxExcleModel> pyxExcleModelss = new ArrayList<PyxExcleModel>();
        //液氮罐
        List<OtherExcleModel> otherExcleModelss = new ArrayList<OtherExcleModel>();
        //冰箱
        List<OtherExcleModel> otherExcleModelones = new ArrayList<OtherExcleModel>();
        //操作台
        List<OtherExcleModel> otherExcleModeltwos = new ArrayList<OtherExcleModel>();

        for (SingleTimeEquipmentModel sing : infos) {
            String equipmenttypeid = sing.getEquipmenttypeid();
            //      String equipmentname = sing.getEquipmentname();
            if (StringUtils.isNotEmpty(equipmenttypeid)) {
                switch (equipmenttypeid) {
                    case "1":
                        HjExcleModel hj = getHj(sing);
                        hjExcleModelss.add(hj);
//                        if (!sheetList.contains(hjs)) {
//                            sheetList.add(hjs);
//                            titleList.add(hjs);
//                        }
                        break;
                    case "2":
                        PyxExcleModel pyx = getPyx(sing);
                        pyxExcleModelss.add(pyx);
//                        if (!sheetList.contains(pyxs)) {
//                            sheetList.add(pyxs);
//                            titleList.add(pyxs);
//                        }
                        break;

                    case "3":
                        OtherExcleModel other = getOther(sing);
                        otherExcleModelss.add(other);
//                        if (!sheetList.contains(ydgs)) {
//                            sheetList.add(ydgs);
//                            titleList.add(ydgs);
//                        }
                        break;
                    case "4":
                        OtherExcleModel otherOne = getOther(sing);
                        otherExcleModelones.add(otherOne);
//                        if (!sheetList.contains(bxs)) {
//                            sheetList.add(bxs);
//                            titleList.add(bxs);
//                        }
                        break;
                    case "5":
                        OtherExcleModel otherTwo = getOther(sing);
                        otherExcleModeltwos.add(otherTwo);
//                        if (!sheetList.contains(czts)) {
//                            sheetList.add(czts);
//                            titleList.add(czts);
//                        }
                        break;
                }
            }
        }
        if (CollectionUtils.isNotEmpty(hjExcleModelss)) {
            sheetLists.add(hjs);
            titleLists.add(hjs);
            classLists.add(HjExcleModel.class);
            listss.add(hjExcleModelss);

        }
        if (CollectionUtils.isNotEmpty(pyxExcleModelss)) {
            sheetLists.add(pyxs);
            titleLists.add(pyxs);
            classLists.add(PyxExcleModel.class);
            listss.add(pyxExcleModelss);
        }
        if (CollectionUtils.isNotEmpty(otherExcleModelss)) {
            sheetLists.add(ydgs);
            titleLists.add(ydgs);
            classLists.add(OtherExcleModel.class);
            listss.add(otherExcleModelss);

        }
        if (CollectionUtils.isNotEmpty(otherExcleModelones)) {
            sheetLists.add(bxs);
            titleLists.add(bxs);
            classLists.add(OtherExcleModel.class);
            listss.add(otherExcleModelones);
        }

        if (CollectionUtils.isNotEmpty(otherExcleModeltwos)) {
            sheetLists.add(czts);
            titleLists.add(czts);
            classLists.add(OtherExcleModel.class);
            listss.add(otherExcleModeltwos);

        }
        FileUtil.exportExcleUnSheets(listss, titleLists, sheetLists, classLists,
                one.getHospitalname() +operationdate + "监控数据.xls", response);





        return apiResponse;

    }

    @Override
    public ApiResponse<CurveInfoModel> getCurveInfoByMonthTime(String equipmentno, String operationdate) {
        String search = "monitorequipmentlastdata";
        // 判断当前日期是否为当前月份
        boolean flag = TimeHelper.isCurrentMonth(operationdate);
        if (!flag) {
            // 当前月份是几月就是几月
            String year = operationdate.substring(0, 4);
            String month = operationdate.substring(5, 7);
            search = "monitorequipmentlastdata" + "_" + year + month;
        }
        //获取上一小时时间：
        String s = TimeHelper.dateReduce(operationdate);
        String s1 = TimeHelper.dateReduceHHmm(operationdate);
        String s2 = TimeHelper.dateReduceHHmm(s);
        Params params = new Params();
        params.setEquipmentno(equipmentno);
        params.setStartdate(s2);
        params.setEnddate(s1);
        params.setSearch(search);
        List<SingleTimeEquipmentModel> info = equipmentInfoMapper.getSingleEquipmentInfoByEquipmentNo(params);
        ApiResponse<CurveInfoModel> apiResponse = new ApiResponse<>();
        if(CollectionUtils.isEmpty(info)){
            apiResponse.setCode(ApiResponse.FAILED);
            return   apiResponse;
        }
        CurveInfoModel curveInfoModel = new CurveInfoModel();
        List<String> temp = new ArrayList<String>();
        List<String> tempTime = new ArrayList<String>();
        List<String> CO2 = new ArrayList<String>();
        List<String> CO2Time = new ArrayList<String>();
        List<String> O2 = new ArrayList<String>();
        List<String> O2Time = new ArrayList<String>();
        List<String> VOC = new ArrayList<String>();
        List<String> VOCTime = new ArrayList<String>();
        List<String> RH = new ArrayList<String>();
        List<String> RHTime = new ArrayList<String>();
        List<String> PM5 = new ArrayList<String>();
        List<String> PM5Time = new ArrayList<String>();
        List<String> PM05 = new ArrayList<String>();
        List<String> PM05Time = new ArrayList<String>();
        List<String> PM25 = new ArrayList<String>();
        List<String> PM25Time = new ArrayList<String>();
        List<String> PM10 = new ArrayList<String>();
        List<String> PM10Time = new ArrayList<String>();
        List<String> JQ = new ArrayList<String>();
        List<String> JQTime = new ArrayList<String>();
        List<String> PRESS = new ArrayList<String>();
        List<String> PRESSTime = new ArrayList<String>();
        List<String> airflow = new ArrayList<String>();
        List<String> airflowTime = new ArrayList<String>();
        List<String> lefttemp = new ArrayList<String>();
        List<String> lefttempTime = new ArrayList<String>();
        List<String> righttemp = new ArrayList<String>();
        List<String> righttempTime = new ArrayList<String>();
        List<String> temp1 = new ArrayList<String>();
        List<String> temp1Time = new ArrayList<String>();
        List<String> temp2 = new ArrayList<String>();
        List<String> temp2Time = new ArrayList<String>();
        List<String> temp3 = new ArrayList<String>();
        List<String> temp3Time = new ArrayList<String>();
        List<String> temp4 = new ArrayList<String>();
        List<String> temp4Time = new ArrayList<String>();
        List<String> temp5 = new ArrayList<String>();
        List<String> temp5Time = new ArrayList<String>();
        List<String> temp6 = new ArrayList<String>();
        List<String> temp6Time = new ArrayList<String>();
        List<String> temp7 = new ArrayList<String>();
        List<String> temp7Time = new ArrayList<String>();
        List<String> temp8 = new ArrayList<String>();
        List<String> temp8Time = new ArrayList<String>();
        List<String> temp9 = new ArrayList<String>();
        List<String> temp9Time = new ArrayList<String>();
        List<String> temp10 = new ArrayList<String>();
        List<String> temp10Time = new ArrayList<String>();
        List<String> tempdiff = new ArrayList<String>();
        List<String> tempdiffTime = new ArrayList<String>();
        List<String> leftcovertemp = new ArrayList<String>();
        List<String> leftcovertempTime = new ArrayList<String>();
        List<String> leftendtemp = new ArrayList<String>();
        List<String> leftendTime = new ArrayList<String>();
        List<String> leftair = new ArrayList<String>();
        List<String> leftairTime = new ArrayList<String>();
        List<String> rightcovertemp = new ArrayList<String>();
        List<String> rightcovertempTime = new ArrayList<String>();
        List<String> rightendtemp = new ArrayList<String>();
        List<String> rightendtempTime = new ArrayList<String>();
        List<String> rightair = new ArrayList<String>();
        List<String> rightairTime = new ArrayList<String>();
        List<String> n2 = new ArrayList<String>();
        List<String> n2Time = new ArrayList<String>();
        List<String> leftCompartmentHumidity = new ArrayList<String>();
        List<String> leftCompartmentHumidityTime = new ArrayList<String>();
        List<String> rightCompartmentHumidity = new ArrayList<String>();
        List<String> rightCompartmentHumidityTime = new ArrayList<String>();
        for (SingleTimeEquipmentModel lastDataModel : info) {
            //       lastDataModel.setInputdatetime(TimeHelper.formats(lastDataModel.getInputdatetime()));
            //液氮罐是否存在差值
            String da = lastDataModel.getInputdatetime();
            //培养箱气流有无值
            if (StringUtils.isNotEmpty(lastDataModel.getCurrentairflow1())) {
                airflow.add(lastDataModel.getCurrentairflow1());
                airflowTime.add(da);
            }
            //是否存在左舱室温度
            if (StringUtils.isNotEmpty(lastDataModel.getCurrentlefttemperature())) {
                lefttemp.add(lastDataModel.getCurrentlefttemperature());
                lefttempTime.add(da);
            }
            //是否存在右舱室温度
            if (StringUtils.isNotEmpty(lastDataModel.getCurrentrigthtemperature())) {
                righttemp.add(lastDataModel.getCurrentrigthtemperature());
                righttempTime.add(da);
            }
            //温度1有无值
            if (StringUtils.isNotEmpty(lastDataModel.getCurrenttemperature1())) {
                temp1.add(lastDataModel.getCurrenttemperature1());
                temp1Time.add(da);
            }
            //温度2有无值
            if (StringUtils.isNotEmpty(lastDataModel.getCurrenttemperature2())) {
                temp2.add(lastDataModel.getCurrenttemperature2());
                temp2Time.add(da);
            }
            //温度3有无值
            if (StringUtils.isNotEmpty(lastDataModel.getCurrenttemperature3())) {
                temp3.add(lastDataModel.getCurrenttemperature3());
                temp3Time.add(da);
            }
            //温度4有无值
            if (StringUtils.isNotEmpty(lastDataModel.getCurrenttemperature4())) {
                temp4.add(lastDataModel.getCurrenttemperature4());
                temp4Time.add(da);
            }
            //温度5有无值
            if (!StringUtils.isEmpty(lastDataModel.getCurrenttemperature5())) {
                temp5.add(lastDataModel.getCurrenttemperature5());
                temp5Time.add(da);
            }
            //温度6有无值
            if (StringUtils.isNotEmpty(lastDataModel.getCurrenttemperature6())) {
                temp6.add(lastDataModel.getCurrenttemperature6());
                temp6Time.add(da);
            }
            //温度7有无值
            if (StringUtils.isNotEmpty(lastDataModel.getCurrenttemperature7())) {
                temp7.add(lastDataModel.getCurrenttemperature7());
                temp7Time.add(da);
            }
            //温度8有无值
            if (StringUtils.isNotEmpty(lastDataModel.getCurrenttemperature8())) {
                temp8.add(lastDataModel.getCurrenttemperature8());
                temp8Time.add(da);
            }
            //温度9有无值
            if (!StringUtils.isEmpty(lastDataModel.getCurrenttemperature9())) {
                temp9.add(lastDataModel.getCurrenttemperature9());
                temp9Time.add(da);
            }
            //温度10有无值
            if (StringUtils.isNotEmpty(lastDataModel.getCurrenttemperature10())) {
                temp10.add(lastDataModel.getCurrenttemperature10());
                temp10Time.add(da);
            }
            //温度有无值
            if (StringUtils.isNotEmpty(lastDataModel.getCurrenttemperature())) {
                // if (!"A".equals() && !"B".equals(lastDataModel.getCurrenttemperature()) && !"C".equals(lastDataModel.getCurrenttemperature()) && !"D".equals(lastDataModel.getCurrenttemperature()) && !"E".equals(lastDataModel.getCurrenttemperature())) {
                temp.add(lastDataModel.getCurrenttemperature());
                tempTime.add(da);

            }

            //CO2有无值
            if (StringUtils.isNotEmpty(lastDataModel.getCurrentcarbondioxide())) {
                //if (!"A".equals(lastDataModel.getCurrentcarbondioxide()) && !"B".equals(lastDataModel.getCurrentcarbondioxide()) && !"C".equals(lastDataModel.getCurrentcarbondioxide()) && !"D".equals(lastDataModel.getCurrentcarbondioxide()) && !"E".equals(lastDataModel.getCurrentcarbondioxide())) {
                CO2.add(lastDataModel.getCurrentcarbondioxide());
                CO2Time.add(da);
                //}
            }
            //氧气有无值
            if (StringUtils.isNotEmpty(lastDataModel.getCurrento2())) {
                // if (!"A".equals(lastDataModel.getCurrento2()) && !"B".equals(lastDataModel.getCurrento2()) && !"C".equals(lastDataModel.getCurrento2()) && !"D".equals(lastDataModel.getCurrento2()) && !"E".equals(lastDataModel.getCurrento2())) {
                O2.add(lastDataModel.getCurrento2());
                O2Time.add(da);
                //}
            }
            //voc 有无值
            if (StringUtils.isNotEmpty(lastDataModel.getCurrentvoc())) {
                VOC.add(lastDataModel.getCurrentvoc());
                VOCTime.add(da);
            }
            //湿度有无值
            if (StringUtils.isNotEmpty(lastDataModel.getCurrenthumidity())) {
                RH.add(lastDataModel.getCurrenthumidity());
                RHTime.add(da);
            }
            //pm2.5有无值
            if (StringUtils.isNotEmpty(lastDataModel.getCurrentpm25())) {
                PM25.add(lastDataModel.getCurrentpm25());
                PM25Time.add(da);
            }
            //pm5有无值
            if (StringUtils.isNotEmpty(lastDataModel.getCurrentpm5())) {
                PM5.add(lastDataModel.getCurrentpm5());
                PM5Time.add(da);
            }
            //pm05有无值
            if (StringUtils.isNotEmpty(lastDataModel.getCurrentpm05())) {
                PM05.add(lastDataModel.getCurrentpm05());
                PM05Time.add(da);
            }
            //PM10有无值
            if (StringUtils.isNotEmpty(lastDataModel.getCurrentpm10())) {
                PM10.add(lastDataModel.getCurrentpm10());
                PM10Time.add(da);
            }
            //甲醛有无值
            if (StringUtils.isNotEmpty(lastDataModel.getCurrentformaldehyde())) {
                JQ.add(lastDataModel.getCurrentformaldehyde());
                JQTime.add(da);
            }
            //压力有无值
            if (StringUtils.isNotEmpty(lastDataModel.getCurrentairflow())) {
                PRESS.add(lastDataModel.getCurrentairflow());
                PRESSTime.add(da);
            }
            //左盖板温度有无值
            if (StringUtils.isNotEmpty(lastDataModel.getCurrentleftcovertemperature())) {
                leftcovertemp.add(lastDataModel.getCurrentleftcovertemperature());
                leftcovertempTime.add(da);
            }
            // 左底板温度有无值
            if (StringUtils.isNotEmpty(lastDataModel.getCurrentleftendtemperature())) {
                leftendtemp.add(lastDataModel.getCurrentleftendtemperature());
                leftendTime.add(da);
            }
            //左气流
            if (StringUtils.isNotEmpty(lastDataModel.getCurrentleftairflow())) {
                leftair.add(lastDataModel.getCurrentleftairflow());
                leftairTime.add(da);
            }
            //右盖板温度
            if (StringUtils.isNotEmpty(lastDataModel.getCurrentrightcovertemperature())) {
                rightcovertemp.add(lastDataModel.getCurrentrightcovertemperature());
                rightcovertempTime.add(da);
            }
            //右底板温度
            if (StringUtils.isNotEmpty(lastDataModel.getCurrentrightendtemperature())) {
                rightendtemp.add(lastDataModel.getCurrentrightendtemperature());
                rightendtempTime.add(da);
            }
            //右气流
            if (StringUtils.isNotEmpty(lastDataModel.getCurrentrightairflow()) ) {
                rightair.add(lastDataModel.getCurrentrightairflow());
                rightairTime.add(da);
            }
            if (StringUtils.isNotEmpty(lastDataModel.getCurrentn2())) {
                n2.add(lastDataModel.getCurrentn2());
                n2Time.add(da);
            }
            if (StringUtils.isNotEmpty(lastDataModel.getLeftCompartmentHumidity())) {
                leftCompartmentHumidity.add(lastDataModel.getLeftCompartmentHumidity());
                leftCompartmentHumidityTime.add(da);
            }
            if (StringUtils.isNotEmpty(lastDataModel.getRightCompartmentHumidity())) {
                rightCompartmentHumidity.add(lastDataModel.getRightCompartmentHumidity());
                rightCompartmentHumidityTime.add(da);
            }

        }
        if (CollectionUtils.isNotEmpty(n2)) {
            CurveDataModel curveDataModel = generateCurveDataModel(n2,n2Time);
            curveInfoModel.setN2(curveDataModel);
        }
        if (CollectionUtils.isNotEmpty(leftcovertemp)) {
            CurveDataModel curveDataModel = generateCurveDataModel(leftcovertemp,leftcovertempTime);
            curveInfoModel.setLeftcovertemp(curveDataModel);
        }
        if (CollectionUtils.isNotEmpty(leftendtemp)) {
            CurveDataModel curveDataModel = generateCurveDataModel(leftendtemp,leftendTime);
            curveInfoModel.setLeftendtemp(curveDataModel);
        }
        if (CollectionUtils.isNotEmpty(leftair)) {
            CurveDataModel curveDataModel = generateCurveDataModel(leftair,leftairTime);
            curveInfoModel.setLeftair(curveDataModel);
        }
        if (CollectionUtils.isNotEmpty(rightcovertemp)) {
            CurveDataModel curveDataModel = generateCurveDataModel(rightcovertemp,rightcovertempTime);
            curveInfoModel.setRightcovertemp(curveDataModel);
        }
        if (CollectionUtils.isNotEmpty(rightendtemp)) {
            CurveDataModel curveDataModel = generateCurveDataModel(rightendtemp, rightendtempTime);
            curveInfoModel.setRightendtemp(curveDataModel);
        }
        if (CollectionUtils.isNotEmpty(rightair)) {
            CurveDataModel curveDataModel = generateCurveDataModel(rightair, rightairTime);
            curveInfoModel.setRightair(curveDataModel);
        }
        if (CollectionUtils.isNotEmpty(tempdiff)) {
            CurveDataModel curveDataModel = generateCurveDataModel(tempdiff, tempdiffTime);
            curveInfoModel.setDifftemp(curveDataModel);
        }
        if (CollectionUtils.isNotEmpty(airflow)) {
            CurveDataModel curveDataModel = generateCurveDataModel(airflow, airflowTime);
            curveInfoModel.setAirflow(curveDataModel);
        }
        if (CollectionUtils.isNotEmpty(lefttemp)) {
            CurveDataModel curveDataModel = generateCurveDataModel(lefttemp, lefttempTime);
            curveInfoModel.setLefttemp(curveDataModel);
        }
        if (CollectionUtils.isNotEmpty(righttemp)) {
            CurveDataModel curveDataModel = generateCurveDataModel(righttemp, righttempTime);
            curveInfoModel.setRighttemp(curveDataModel);
        }
        if (CollectionUtils.isNotEmpty(temp1)) {
            CurveDataModel curveDataModel = generateCurveDataModel(temp1, temp1Time);
            curveInfoModel.setTemp1(curveDataModel);
        }
        if (CollectionUtils.isNotEmpty(temp2)) {
            CurveDataModel curveDataModel = generateCurveDataModel(temp2, temp2Time);
            curveInfoModel.setTemp2(curveDataModel);
        }
        if (CollectionUtils.isNotEmpty(temp3)) {
            CurveDataModel curveDataModel = generateCurveDataModel(temp3, temp3Time);
            curveInfoModel.setTemp3(curveDataModel);
        }
        if (CollectionUtils.isNotEmpty(temp4)) {
            CurveDataModel curveDataModel = generateCurveDataModel(temp4, temp4Time);
            curveInfoModel.setTemp4(curveDataModel);
        }
        if (CollectionUtils.isNotEmpty(temp5)) {
            CurveDataModel curveDataModel = generateCurveDataModel(temp5, temp5Time);
            curveInfoModel.setTemp5(curveDataModel);
        }
        if (CollectionUtils.isNotEmpty(temp6)) {
            CurveDataModel curveDataModel = generateCurveDataModel(temp6, temp6Time);
            curveInfoModel.setTemp6(curveDataModel);
        }
        if (CollectionUtils.isNotEmpty(temp7)) {
            CurveDataModel curveDataModel = generateCurveDataModel(temp7, temp7Time);
            curveInfoModel.setTemp7(curveDataModel);
        }
        if (CollectionUtils.isNotEmpty(temp8)) {
            CurveDataModel curveDataModel = generateCurveDataModel(temp8, temp8Time);
            curveInfoModel.setTemp8(curveDataModel);
        }
        if (CollectionUtils.isNotEmpty(temp9)) {
            CurveDataModel curveDataModel = generateCurveDataModel(temp9, temp9Time);
            curveInfoModel.setTemp9(curveDataModel);
        }
        if (CollectionUtils.isNotEmpty(temp10)) {
            CurveDataModel curveDataModel = generateCurveDataModel(temp10, temp10Time);
            curveInfoModel.setTemp10(curveDataModel);
        }
        if (CollectionUtils.isNotEmpty(temp)) {
            CurveDataModel curveDataModel = generateCurveDataModel(temp, tempTime);
            curveInfoModel.setTemp(curveDataModel);
        }
        if (CollectionUtils.isNotEmpty(CO2)) {
            CurveDataModel curveDataModel = generateCurveDataModel(CO2, CO2Time);
            curveInfoModel.setCo2(curveDataModel);
        }
        if (CollectionUtils.isNotEmpty(O2)) {
            CurveDataModel curveDataModel = generateCurveDataModel(O2, O2Time);
            curveInfoModel.setO2(curveDataModel);
        }
        if (CollectionUtils.isNotEmpty(VOC)) {
            CurveDataModel curveDataModel = generateCurveDataModel(VOC, VOCTime);
            curveInfoModel.setVoc(curveDataModel);
        }
        if (CollectionUtils.isNotEmpty(RH)) {
            CurveDataModel curveDataModel = generateCurveDataModel(RH, RHTime);
            curveInfoModel.setRh(curveDataModel);
        }
        if (CollectionUtils.isNotEmpty(PM25)) {
            CurveDataModel curveDataModel = generateCurveDataModel(PM25, PM25Time);
            curveInfoModel.setPm25(curveDataModel);
        }
        if (CollectionUtils.isNotEmpty(PM5)) {
            CurveDataModel curveDataModel = generateCurveDataModel(PM5, PM5Time);
            curveInfoModel.setPm5(curveDataModel);
        }
        if (CollectionUtils.isNotEmpty(PM05)) {
            CurveDataModel curveDataModel = generateCurveDataModel(PM05, PM05Time);
            curveInfoModel.setPm05(curveDataModel);
        }
        if (CollectionUtils.isNotEmpty(PM10)) {
            CurveDataModel curveDataModel = generateCurveDataModel(PM10, PM10Time);
            curveInfoModel.setPm10(curveDataModel);
        }
        if (CollectionUtils.isNotEmpty(JQ)) {
            CurveDataModel curveDataModel = generateCurveDataModel(JQ, JQTime);
            curveInfoModel.setJq(curveDataModel);
        }
        if (CollectionUtils.isNotEmpty(PRESS)) {
            CurveDataModel curveDataModel = generateCurveDataModel(PRESS, PRESSTime);
            curveInfoModel.setPress(curveDataModel);
        }
        if (CollectionUtils.isNotEmpty(leftCompartmentHumidity)) {
            CurveDataModel curveDataModel = generateCurveDataModel(leftCompartmentHumidity, leftCompartmentHumidityTime);
            curveInfoModel.setLeftCompartmentHumidity(curveDataModel);
        }
        if (CollectionUtils.isNotEmpty(rightCompartmentHumidity)) {
            CurveDataModel curveDataModel = generateCurveDataModel(rightCompartmentHumidity, rightCompartmentHumidityTime);
            curveInfoModel.setRightCompartmentHumidity(curveDataModel);
        }
        apiResponse.setResult(curveInfoModel);
        return apiResponse;
    }


    public OtherExcleModel getOther(SingleTimeEquipmentModel sing) {
        OtherExcleModel otherExcleModel = new OtherExcleModel();
        List<String> list = new ArrayList<String>();
        list.add("A");
        list.add("B");
        list.add("C");
        list.add("D");
        list.add("E");
        list.add("F");
        String equipmentname = sing.getEquipmentname();
        String inputdatetime = sing.getInputdatetime();
        String currenttemperature = sing.getCurrenttemperature();
        if (StringUtils.isNotEmpty(currenttemperature)) {
            if (list.contains(currenttemperature)) {
                currenttemperature = "探头获取数据异常";
            }
            otherExcleModel.setCurrenttemperature(currenttemperature);
        }
        otherExcleModel.setInputdatetime(inputdatetime);
        otherExcleModel.setEquipmentname(equipmentname);
        return otherExcleModel;

    }

    public PyxExcleModel getPyx(SingleTimeEquipmentModel sing) {
        PyxExcleModel pyxExcleModel = new PyxExcleModel();
        List<String> list = new ArrayList<String>();
        list.add("A");
        list.add("B");
        list.add("C");
        list.add("D");
        list.add("E");
        list.add("F");
        String equipmentname = sing.getEquipmentname();
        String inputdatetime = sing.getInputdatetime();
        String currenttemperature = sing.getCurrenttemperature();
        String currento2 = sing.getCurrento2();
        String currentcarbondioxide = sing.getCurrentcarbondioxide();
        String currenthumidity = sing.getCurrenthumidity();
        String currentairflow1 = sing.getCurrentairflow1();
        String currenttemperature1 = sing.getCurrenttemperature1();
        String currenttemperature2 = sing.getCurrenttemperature2();
        String currenttemperature3 = sing.getCurrenttemperature3();
        String currenttemperature4 = sing.getCurrenttemperature4();
        String currenttemperature5 = sing.getCurrenttemperature5();
        String currenttemperature6 = sing.getCurrenttemperature6();
        String currenttemperature7 = sing.getCurrenttemperature7();
        String currenttemperature8 = sing.getCurrenttemperature8();
        String currenttemperature9 = sing.getCurrenttemperature9();
        String currenttemperature10 = sing.getCurrenttemperature10();
        String currentleftcovertemperature = sing.getCurrentleftcovertemperature();
        String currentrightcovertemperature = sing.getCurrentrightcovertemperature();
        String currentleftendtemperature = sing.getCurrentleftendtemperature();
        String currentrightendtemperature = sing.getCurrentrightendtemperature();
        String currentlefttemperature = sing.getCurrentlefttemperature();
        String currentrigthtemperature = sing.getCurrentrigthtemperature();
        if (StringUtils.isNotEmpty(currenttemperature)) {
            if (list.contains(currenttemperature)) {
                currenttemperature = "探头获取数据异常";
            }
            pyxExcleModel.setCurrenttemperature(currenttemperature);
        }
        if (StringUtils.isNotEmpty(currento2)) {
            if (list.contains(currento2)) {
                currento2 = "探头获取数据异常";
            }
            pyxExcleModel.setCurrento2(currento2);
        }
        if (StringUtils.isNotEmpty(currentcarbondioxide)) {
            if (list.contains(currentcarbondioxide)) {
                currentcarbondioxide = "探头获取数据异常";
            }
            pyxExcleModel.setCurrentcarbondioxide(currentcarbondioxide);
        }
        if (StringUtils.isNotEmpty(currenttemperature1)) {
            pyxExcleModel.setCurrenttemperature1(currenttemperature1);
        }
        if (StringUtils.isNotEmpty(currenttemperature2)) {
            pyxExcleModel.setCurrenttemperature2(currenttemperature2);
        }
        if (StringUtils.isNotEmpty(currenttemperature3)) {
            pyxExcleModel.setCurrenttemperature3(currenttemperature3);
        }
        if (StringUtils.isNotEmpty(currenttemperature4)) {
            pyxExcleModel.setCurrenttemperature4(currenttemperature4);
        }
        if (StringUtils.isNotEmpty(currenttemperature5)) {
            pyxExcleModel.setCurrenttemperature5(currenttemperature5);
        }
        if (StringUtils.isNotEmpty(currenttemperature6)) {
            pyxExcleModel.setCurrenttemperature6(currenttemperature6);
        }
        if (StringUtils.isNotEmpty(currenttemperature7)) {
            pyxExcleModel.setCurrenttemperature7(currenttemperature7);
        }
        if (StringUtils.isNotEmpty(currenttemperature8)) {
            pyxExcleModel.setCurrenttemperature8(currenttemperature8);
        }
        if (StringUtils.isNotEmpty(currenttemperature9)) {
            pyxExcleModel.setCurrenttemperature9(currenttemperature9);
        }
        if (StringUtils.isNotEmpty(currenttemperature10)) {
            pyxExcleModel.setCurrenttemperature10(currenttemperature10);
        }
        if (StringUtils.isNotEmpty(currentleftcovertemperature)) {
            pyxExcleModel.setCurrentleftcovertemperature(currentleftcovertemperature);
        }
        if (StringUtils.isNotEmpty(currentrightcovertemperature)) {
            pyxExcleModel.setCurrentrightcovertemperature(currentrightcovertemperature);
        }
        if (StringUtils.isNotEmpty(currentleftendtemperature)) {
            pyxExcleModel.setCurrentleftendtemperature(currentleftendtemperature);
        }
        if (StringUtils.isNotEmpty(currentrightendtemperature)) {
            pyxExcleModel.setCurrentrightendtemperature(currentrightendtemperature);
        }
        if (StringUtils.isNotEmpty(currenthumidity)) {
            pyxExcleModel.setCurrenthumidity(currenthumidity);
        }
        if (StringUtils.isNotEmpty(currentairflow1)) {
            if (list.contains(currentairflow1)) {
                currentairflow1 = "设备获取数据异常";
            }
            pyxExcleModel.setCurrentairflow1(currentairflow1);
        }
        if (StringUtils.isNotEmpty(currentlefttemperature)) {
            if (list.contains(currentlefttemperature)) {
                currentlefttemperature = "设备获取数据异常";
            }
            pyxExcleModel.setCurrentlefttemperature(currentlefttemperature);
        }
        if (StringUtils.isNotEmpty(currentrigthtemperature)) {
            if (list.contains(currentrigthtemperature)) {
                currentrigthtemperature = "设备获取数据异常";
            }
            pyxExcleModel.setCurrentrigthtemperature(currentrigthtemperature);
        }
        pyxExcleModel.setInputdatetime(inputdatetime);
        pyxExcleModel.setEquipmentname(equipmentname);
        return pyxExcleModel;

    }

    public HjExcleModel getHj(SingleTimeEquipmentModel sing) {
        HjExcleModel hjExcleModel = new HjExcleModel();
        String equipmentname = sing.getEquipmentname();
        String inputdatetime = sing.getInputdatetime();
        String currenttemperature = sing.getCurrenttemperature();
        String currento2 = sing.getCurrento2();
        String currentcarbondioxide = sing.getCurrentcarbondioxide();
        String currentformaldehyde = sing.getCurrentformaldehyde();
        String currenthumidity = sing.getCurrenthumidity();
        String currentpm5 = sing.getCurrentpm5();
        String currentpm05 = sing.getCurrentpm05();
        String currentpm10 = sing.getCurrentpm10();
        String currentpm25 = sing.getCurrentpm25();
        String currentvoc = sing.getCurrentvoc();
        String currentairflow = sing.getCurrentairflow();
        if (StringUtils.isNotEmpty(currenttemperature)) {
            hjExcleModel.setCurrenttemperature(currenttemperature);
        }
        if (StringUtils.isNotEmpty(currento2)) {
            hjExcleModel.setCurrento2(currento2);
        }
        if (StringUtils.isNotEmpty(currentcarbondioxide)) {
            hjExcleModel.setCurrentcarbondioxide(currentcarbondioxide);
        }
        if (StringUtils.isNotEmpty(currentformaldehyde)) {
            hjExcleModel.setCurrentformaldehyde(currentformaldehyde);
        }
        if (StringUtils.isNotEmpty(currenthumidity)) {
            hjExcleModel.setCurrenthumidity(currenthumidity);
        }
        if (StringUtils.isNotEmpty(currentpm5)) {
            hjExcleModel.setCurrentpm5(currentpm5);
        }
        if (StringUtils.isNotEmpty(currentpm05)) {
            hjExcleModel.setCurrentpm05(currentpm05);
        }
        if (StringUtils.isNotEmpty(currentpm10)) {
            hjExcleModel.setCurrentpm10(currentpm10);
        }
        if (StringUtils.isNotEmpty(currentpm25)) {
            hjExcleModel.setCurrentpm25(currentpm25);
        }
        if (StringUtils.isNotEmpty(currentvoc)) {
            hjExcleModel.setCurrentvoc(currentvoc);
        }
        if (StringUtils.isNotEmpty(currentairflow)) {
            hjExcleModel.setCurrentairflow(currentairflow);
        }
        hjExcleModel.setInputdatetime(inputdatetime);
        hjExcleModel.setEquipmentname(equipmentname);
        return hjExcleModel;

    }


}

