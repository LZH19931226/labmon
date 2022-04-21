package com.hc.service.serviceimpl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.PageRowBounds;
import com.hc.config.RedisTemplateUtil;
import com.hc.infrastructure.dao.HospitaDao;
import com.hc.infrastructure.dao.InstrumentparamconfigDao;
import com.hc.entity.*;
import com.hc.mapper.laboratoryFrom.EquipmentInfoMapper;
import com.hc.mapper.laboratoryFrom.InstrumentMonitorInfoMapper;
import com.hc.mapper.laboratoryFrom.InstrumentParamConfigInfoMapper;
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
import com.hc.utils.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by 16956 on 2018-08-01.
 */
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
    private InstrumentParamConfigInfoMapper instrumentParamConfigInfoMapper;
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
            synchronized (this) {
                InstrumentInfoModel one1 = monitorInstrumentMapper.getInstrumentInfoByNo(instrumentparamconfig.getInstrumentparamconfigno());
                instrumentparamconfig.setWarningphone(warningphone);
                instrumentparamconfigDao.save(instrumentparamconfig);
                InstrumentInfoModel one2 = monitorInstrumentMapper.getInstrumentInfoByNo(instrumentparamconfig.getInstrumentparamconfigno());
                String instrumentparamconfigNO = instrumentparamconfig.getInstrumentparamconfigno();
                InstrumentMonitorInfoModel instrumentMonitorInfoModel = instrumentMonitorInfoMapper.selectInstrumentOneInfo(instrumentparamconfigNO);
                instrumentMonitorInfoModel.setWarningphone(warningphone);
                ShowModel showModel = monitorInstrumentMapper.getHospitalNameEquipmentNameByNo(instrumentparamconfig.getInstrumentparamconfigno());
                String instrumentName = monitorInstrumentMapper.getInstrumentName(instrumentparamconfigNO);
                updateRecordService.updateInstrumentMonitor(instrumentName, showModel.getEquipmentname(), showModel.getHospitalname(), batchModel.getUsername(), one1, one2, "1", "1");
                objectObjectObjectHashOperations.put("insprobe" + hospitalcode, instrumentMonitorInfoModel.getInstrumentno() + ":" + instrumentMonitorInfoModel.getInstrumentconfigid(), JsonUtil.toJson(instrumentMonitorInfoModel));
            }
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
            List<String> collect = monitorequipmentList.stream().map(Monitorequipment::getEquipmentno).collect(Collectors.toList());
            List<InstrumentMonitorInfoModel> instrumentMonitorInfoModels = instrumentParamConfigInfoMapper.selectInstrumentByEqNo(collect);
            Map<String, List<InstrumentMonitorInfoModel>> collect1 = instrumentMonitorInfoModels.stream().collect(Collectors.groupingBy(InstrumentMonitorInfoModel::getEquipmentno));
            List<Monitorinstrument> sns = equipmentInfoMapper.getSns(collect);
            Map<String, List<Monitorinstrument>> collect2 = sns.stream().collect(Collectors.groupingBy(Monitorinstrument::getEquipmentno));
            HashOperations<Object, Object, Object> objectObjectObjectHashOperations = redisTemplateUtil.opsForHash();
            Collection<Object> keys = new ArrayList<>(collect);
            List<Object> lastdatas = objectObjectObjectHashOperations.multiGet("LASTDATA" + hospitalcode, keys);
            List<Monitorequipmentlastdata> monitorequipmentlastdatas = new ArrayList<>();
            lastdatas.forEach(s -> {
                if (null != s) {
                    String lasts = (String) s;
                    Monitorequipmentlastdata monitorequipmentlastdata = JsonUtil.toBean(lasts, Monitorequipmentlastdata.class);
                    monitorequipmentlastdatas.add(monitorequipmentlastdata);
                }
            });
            Map<String, List<Monitorequipmentlastdata>> collect3 = monitorequipmentlastdatas.stream().collect(Collectors.groupingBy(Monitorequipmentlastdata::getEquipmentno));
            for (Monitorequipment monitorequipment : monitorequipmentList) {
                String equipmentno = monitorequipment.getEquipmentno();
                List<Monitorinstrument> monitorinstruments = collect2.get(equipmentno);
                if (CollectionUtils.isNotEmpty(monitorinstruments)) {
                    monitorequipment.setSn(monitorinstruments.get(0).getSn());
                }
                List<Monitorequipmentlastdata> monitorequipmentlastdatass = collect3.get(equipmentno);
                if (CollectionUtils.isNotEmpty(monitorequipmentlastdatass)) {
                    Monitorequipmentlastdata monitorequipmentlastdata = monitorequipmentlastdatass.get(0);
                    if (StringUtils.isNotEmpty(monitorequipmentlastdata.getCurrentdoorstate())) {
                        // 查找这个设备下开关量的最低值
                        String lowlimit = equipmentInfoMapper.getLowlimit(monitorequipment.getEquipmentno());
                        monitorequipment.setLowlimit(lowlimit);
                    }
                    monitorequipment.setMonitorequipmentlastdata(monitorequipmentlastdata);
                }
                List<InstrumentMonitorInfoModel> instrumentMonitorInfoModels1 = collect1.get(equipmentno);
                if (CollectionUtils.isNotEmpty(instrumentMonitorInfoModels1)) {
                    monitorequipment.setInstrumentMonitorInfoModel(instrumentMonitorInfoModels1);
                }
            }
            //针对设备类型异常值代表未接入传感器属性设置为空在做过滤
            monitorequipmentList.forEach(s -> {
                String sn = s.getSn();
                Monitorequipmentlastdata monitorequipmentlastdata = s.getMonitorequipmentlastdata();
                if (null != monitorequipmentlastdata && StringUtils.isNotEmpty(sn)) {
                    Monitorequipmentlastdata monitorequipmentlastdata1 = MtUnConnectedSensorFilter.mtCheckUnCs(sn, monitorequipmentlastdata);
                    s.setMonitorequipmentlastdata(monitorequipmentlastdata1);
                }
            });
            apiResponse.setMessage("查询成功");
            apiResponse.setCode(ApiResponse.SUCCESS);
            apiResponse.setResult(monitorequipmentList);
            return apiResponse;
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("失败：" + e);
            apiResponse.setMessage("服务异常");
            apiResponse.setCode(ApiResponse.FAILED);
            return apiResponse;
        }
    }

    @Override
    public ApiResponse<Page<Monitorequipment>> getEquipmentCurrentDataPage(String hospitalcode, String equipmenttypeid, Integer pagesize, Integer pagenum, String equipmentname) {

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
            List<String> collect = monitorequipmentList.stream().map(Monitorequipment::getEquipmentno).collect(Collectors.toList());
            List<InstrumentMonitorInfoModel> instrumentMonitorInfoModels = instrumentParamConfigInfoMapper.selectInstrumentByEqNo(collect);
            Map<String, List<InstrumentMonitorInfoModel>> collect1 = instrumentMonitorInfoModels.stream().collect(Collectors.groupingBy(InstrumentMonitorInfoModel::getEquipmentno));
            List<Monitorinstrument> sns = equipmentInfoMapper.getSns(collect);
            Map<String, List<Monitorinstrument>> collect2 = sns.stream().collect(Collectors.groupingBy(Monitorinstrument::getEquipmentno));
            HashOperations<Object, Object, Object> objectObjectObjectHashOperations = redisTemplateUtil.opsForHash();

            Collection<Object> keys = new ArrayList<>(collect);
            List<Object> lastdatas = objectObjectObjectHashOperations.multiGet("LASTDATA" + hospitalcode, keys);
            List<Monitorequipmentlastdata> monitorequipmentlastdatas = new ArrayList<>();
            lastdatas.forEach(s -> {
                if (null != s) {
                    String lasts = (String) s;
                    Monitorequipmentlastdata monitorequipmentlastdata = JsonUtil.toBean(lasts, Monitorequipmentlastdata.class);
                    monitorequipmentlastdatas.add(monitorequipmentlastdata);
                }
            });
            Map<String, List<Monitorequipmentlastdata>> collect3 = monitorequipmentlastdatas.stream().collect(Collectors.groupingBy(Monitorequipmentlastdata::getEquipmentno));
            for (Monitorequipment monitorequipment : monitorequipmentList) {
                String equipmentno = monitorequipment.getEquipmentno();
                List<Monitorinstrument> monitorinstruments = collect2.get(equipmentno);
                if (CollectionUtils.isNotEmpty(monitorinstruments)) {
                    monitorequipment.setSn(monitorinstruments.get(0).getSn());
                }
                List<Monitorequipmentlastdata> monitorequipmentlastdatass = collect3.get(equipmentno);
                if (CollectionUtils.isNotEmpty(monitorequipmentlastdatass)) {
                    Monitorequipmentlastdata monitorequipmentlastdata = monitorequipmentlastdatass.get(0);
                    if (StringUtils.isNotEmpty(monitorequipmentlastdata.getCurrentdoorstate())) {
                        // 查找这个设备下开关量的最低值
                        String lowlimit = equipmentInfoMapper.getLowlimit(monitorequipment.getEquipmentno());
                        monitorequipment.setLowlimit(lowlimit);
                    }
                    monitorequipment.setMonitorequipmentlastdata(monitorequipmentlastdata);
                }
                List<InstrumentMonitorInfoModel> instrumentMonitorInfoModels1 = collect1.get(equipmentno);
                if (CollectionUtils.isNotEmpty(instrumentMonitorInfoModels1)) {
                    monitorequipment.setInstrumentMonitorInfoModel(instrumentMonitorInfoModels1);
                }
            }
            monitorequipmentList.forEach(s -> {
                String sn = s.getSn();
                Monitorequipmentlastdata monitorequipmentlastdata = s.getMonitorequipmentlastdata();
                if (null != monitorequipmentlastdata && StringUtils.isNotEmpty(sn)) {
                    Monitorequipmentlastdata monitorequipmentlastdata1 = MtUnConnectedSensorFilter.mtCheckUnCs(sn, monitorequipmentlastdata);
                    s.setMonitorequipmentlastdata(monitorequipmentlastdata1);
                }
            });
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
            String lastdata = (String) objectObjectObjectHashOperations.get("LASTDATA" + hospitalcode, equipmentno);
            LOGGER.info("市电信息：" + lastdata);
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
        try {
            List<Monitorequipmentlastdata> lastDataModelList;
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
            Monitorequipment equipmentByNo = equipmentInfoMapper.getEquipmentByNo(equipmentno);
            String equipmentname = equipmentByNo.getEquipmentname();
            EquipmentInfoServiceHelp.getCurvelFirst(lastDataModelList, curveInfoModel, equipmentname);
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

    @Override
    public ApiResponse<CurveInfoModel> getCurveSecond(String date, List<String> equipmentnoList, String type) {
        ApiResponse<CurveInfoModel> apiResponse = new ApiResponse<CurveInfoModel>();
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
            String lastdata = (String) objectObjectObjectHashOperations.get("LASTDATA" + monitorequipment.getHospitalcode(), monitorequipment.getEquipmentno());
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
    public ApiResponse<String> exportExcleOne(HttpServletResponse response, String hospitalcode, String operationdate, String type) {
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
        if (StringUtils.equals(type, "月")) {
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
                if (StringUtils.isNotEmpty(equipmenttypeid)) {
                    switch (equipmenttypeid) {
                        case "1":
                            HjExcleModel hj = EquipmentInfoServiceHelp.getHj(sing);
                            hjExcleModels.add(hj);
                            break;
                        case "2":
                            PyxExcleModel pyx = EquipmentInfoServiceHelp.getPyx(sing);
                            pyxExcleModels.add(pyx);
                            break;
                        case "3":
                            OtherExcleModel other = EquipmentInfoServiceHelp.getOther(sing);
                            otherExcleModels.add(other);
                            break;
                        case "4":
                            OtherExcleModel otherOne = EquipmentInfoServiceHelp.getOther(sing);
                            otherExcleModelone.add(otherOne);
                            break;
                        case "5":
                            OtherExcleModel otherTwo = EquipmentInfoServiceHelp.getOther(sing);
                            otherExcleModeltwo.add(otherTwo);
//                        }
                            break;
                        default:
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
                    one.getHospitalname() + year + "-" + month + "月监控数据.xls", response);
            return apiResponse;

        }
        params.setStartdate(s);
        params.setEnddate(operationdate);
        //当天数据
        List<SingleTimeEquipmentModel> infos = equipmentInfoMapper.getSingleEquipmentInfoByHospitalCodeByDay(params);
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
            if (StringUtils.isNotEmpty(equipmenttypeid)) {
                switch (equipmenttypeid) {
                    case "1":
                        HjExcleModel hj = EquipmentInfoServiceHelp.getHj(sing);
                        hjExcleModelss.add(hj);
                        break;
                    case "2":
                        PyxExcleModel pyx = EquipmentInfoServiceHelp.getPyx(sing);
                        pyxExcleModelss.add(pyx);
                        break;
                    case "3":
                        OtherExcleModel other = EquipmentInfoServiceHelp.getOther(sing);
                        otherExcleModelss.add(other);
//                        }
                        break;
                    case "4":
                        OtherExcleModel otherOne = EquipmentInfoServiceHelp.getOther(sing);
                        otherExcleModelones.add(otherOne);
//                        }
                        break;
                    case "5":
                        OtherExcleModel otherTwo = EquipmentInfoServiceHelp.getOther(sing);
                        otherExcleModeltwos.add(otherTwo);
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
                one.getHospitalname() + operationdate + "监控数据.xls", response);
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
        if (CollectionUtils.isEmpty(info)) {
            apiResponse.setCode(ApiResponse.FAILED);
            return apiResponse;
        }
        CurveInfoModel curveInfoModel = new CurveInfoModel();
        EquipmentInfoServiceHelp.getCurveInfoByMonthTime(info, curveInfoModel);
        apiResponse.setResult(curveInfoModel);
        return apiResponse;
    }


}

