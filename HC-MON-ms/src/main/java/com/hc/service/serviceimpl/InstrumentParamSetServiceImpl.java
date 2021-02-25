package com.hc.service.serviceimpl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.PageRowBounds;
import com.hc.config.RedisTemplateUtil;
import com.hc.dao.InstrumentparamconfigDao;
import com.hc.dao.UserrightDao;
import com.hc.entity.Instrumentparamconfig;
import com.hc.entity.Monitorequipment;
import com.hc.entity.Monitorequipmentlastdata;
import com.hc.entity.Monitorinstrument;
import com.hc.mapper.laboratoryFrom.EquipmentInfoMapper;
import com.hc.mapper.laboratoryFrom.InstrumentMonitorInfoMapper;
import com.hc.mapper.laboratoryFrom.InstrumentParamConfigInfoMapper;
import com.hc.mapper.laboratoryFrom.MonitorInstrumentMapper;
import com.hc.mapper.laboratoryMain.InstrumentparamConfigSetMapper;
import com.hc.model.*;
import com.hc.model.ResponseModel.EquipmentConfigInfoModel;
import com.hc.model.ResponseModel.InstrumentParamConfigInfo;
import com.hc.model.ResponseModel.InstrumentParamConfigInfos;
import com.hc.model.ResponseModel.MessageSendModel;
import com.hc.my.common.core.bean.InstrumentMonitorInfoModel;
import com.hc.service.InstrumentParamSetService;
import com.hc.service.UpdateRecordService;
import com.hc.utils.ApiResponse;
import com.hc.utils.JsonUtil;
import com.hc.utils.TimeHelper;
import com.hc.utils.push.UmengPushUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by 16956 on 2018-08-05.
 */
@Service
public class InstrumentParamSetServiceImpl implements InstrumentParamSetService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EquipmentInfoServiceImpl.class);
    @Autowired
    private InstrumentparamConfigSetMapper instrumentparamConfigSetMapper;
    @Autowired
    private InstrumentParamConfigInfoMapper instrumentParamConfigInfoMapper;
    @Autowired
    private InstrumentMonitorInfoMapper instrumentMonitorInfoMapper;
    @Autowired
    private RedisTemplateUtil redisTemplateUtil;
    @Autowired
    private UserrightDao userrightDao;
    @Autowired
    private InstrumentparamconfigDao instrumentparamconfigDao;
    @Autowired
    private MonitorInstrumentMapper monitorInstrumentMapper;
    @Autowired
    private UpdateRecordService updateRecordService;
    @Autowired
    private EquipmentInfoMapper equipmentInfoMapper;
    @Override
    public ApiResponse<String> addMt100Qc() {
        //查询所有的MT100设备
        ApiResponse<String> apiResponse = new ApiResponse<>();
        List<Monitorinstrument> allMt100 = instrumentparamConfigSetMapper.getAllMt100();
        if (CollectionUtils.isNotEmpty(allMt100)) {
            for (Monitorinstrument monitorinstrument : allMt100) {
                String instrumentno = monitorinstrument.getInstrumentno();
                Monitorinstrument qcInstrument = instrumentparamConfigSetMapper.getQcInstrument(instrumentno);
                if (null == qcInstrument) {
                    //新增
                    Instrumentparamconfig instrumentparamconfig = new Instrumentparamconfig();
                    instrumentparamconfig.setInstrumentparamconfigno(UUID.randomUUID().toString().replaceAll("-", ""));
                    instrumentparamconfig.setInstrumentname(monitorinstrument.getInstrumentname());
                    instrumentparamconfig.setHighlimit(new BigDecimal("100"));
                    instrumentparamconfig.setLowlimit(new BigDecimal("20"));
                    instrumentparamconfig.setInstrumentconfigid(7);
                    instrumentparamconfig.setInstrumentno(instrumentno);
                    instrumentparamconfig.setWarningphone("1");
                    instrumentparamconfig.setInstrumenttypeid(monitorinstrument.getInstrumenttypeid());
                    instrumentparamconfig.setAlarmtime(3);
                    instrumentparamconfigDao.save(instrumentparamconfig);
                }

            }
        }
        return apiResponse;
    }

    @Override
    public ApiResponse<String> updatePushTime(PushSetModel pushSetModel) {
        ApiResponse<String> apiResponse = new ApiResponse<String>();
        Instrumentparamconfig instrumentparamconfig = new Instrumentparamconfig();
        InstrumentMonitorInfoModel instrumentMonitorInfoModel = new InstrumentMonitorInfoModel();
        String type = pushSetModel.getType();
        String instrumentparamconfigNO = pushSetModel.getInstrumentparamconfigNO();
        String pushtime = null;
        String warningphone = null;
        try {
            switch (type) {
                case "1":
                    //半小时后提醒
                    pushtime = TimeHelper.getCurrentDateAdd(30);
                    instrumentparamconfig.setPushtime(pushtime);
                    warningphone = "1";
                    instrumentparamconfig.setWarningphone(warningphone);
                    instrumentparamconfig.setInstrumentparamconfigno(instrumentparamconfigNO);

                    break;
                case "2":
                    pushtime = TimeHelper.getCurrentDateAdd(60);
                    instrumentparamconfig.setPushtime(pushtime);
                    warningphone = "1";
                    instrumentparamconfig.setWarningphone(warningphone);
                    instrumentparamconfig.setInstrumentparamconfigno(instrumentparamconfigNO);
                    break;
                case "3":
                    pushtime = TimeHelper.getCurrentDateAdd(1440);
                    instrumentparamconfig.setPushtime(pushtime);
                    warningphone = "1";
                    instrumentparamconfig.setWarningphone(warningphone);
                    instrumentparamconfig.setInstrumentparamconfigno(instrumentparamconfigNO);
                    break;
                case "4":
                    warningphone = "0";
                    instrumentparamconfig.setWarningphone(warningphone);
                    instrumentparamconfig.setInstrumentparamconfigno(instrumentparamconfigNO);
                    break;
            }
            instrumentparamConfigSetMapper.updateWarningState(instrumentparamconfig);
            HashOperations<Object, Object, Object> objectObjectObjectHashOperations = redisTemplateUtil.opsForHash();
            instrumentMonitorInfoModel = instrumentMonitorInfoMapper.selectInstrumentOneInfo(instrumentparamconfig.getInstrumentparamconfigno());
            String hospitalcode = instrumentMonitorInfoModel.getHospitalcode();
            objectObjectObjectHashOperations.put("insprobe"+hospitalcode, instrumentMonitorInfoModel.getInstrumentno() + ":" + instrumentMonitorInfoModel.getInstrumentconfigid(), JsonUtil.toJson(instrumentMonitorInfoModel));
            return apiResponse;
        } catch (Exception e) {
            LOGGER.error("失败：" + e.getMessage());
            apiResponse.setMessage("服务异常");
            apiResponse.setCode(ApiResponse.FAILED);
            return apiResponse;
        }
    }

    @Override
    public ApiResponse<String> updateWarningPhone(Instrumentparamconfig instrumentparamconfig) {
        ApiResponse<String> apiResponse = new ApiResponse<String>();
        InstrumentMonitorInfoModel instrumentMonitorInfoModel = new InstrumentMonitorInfoModel();
        try {
            synchronized (this) {
                InstrumentInfoModel one1 = monitorInstrumentMapper.getInstrumentInfoByNo(instrumentparamconfig.getInstrumentparamconfigno());
                String instrumentName = monitorInstrumentMapper.getInstrumentName(instrumentparamconfig.getInstrumentparamconfigno());
                instrumentparamConfigSetMapper.updateWarnPhone(instrumentparamconfig);
                InstrumentInfoModel one2 = monitorInstrumentMapper.getInstrumentInfoByNo(instrumentparamconfig.getInstrumentparamconfigno());
                one2.setWarningphone(instrumentparamconfig.getWarningphone());
                //修改后存redis缓存
                HashOperations<Object, Object, Object> objectObjectObjectHashOperations = redisTemplateUtil.opsForHash();
                instrumentMonitorInfoModel = instrumentMonitorInfoMapper.selectInstrumentOneInfo(instrumentparamconfig.getInstrumentparamconfigno());
                String hospitalcode = instrumentMonitorInfoModel.getHospitalcode();
                instrumentMonitorInfoModel.setWarningphone(instrumentparamconfig.getWarningphone());
                LOGGER.info("手机APP禁用启用报警：对象为：" + JsonUtil.toJson(instrumentMonitorInfoModel));
                objectObjectObjectHashOperations.put("insprobe"+hospitalcode, instrumentMonitorInfoModel.getInstrumentno() + ":" + instrumentMonitorInfoModel.getInstrumentconfigid(), JsonUtil.toJson(instrumentMonitorInfoModel));
                ShowModel showModel = monitorInstrumentMapper.getHospitalNameEquipmentNameByNo(instrumentparamconfig.getInstrumentparamconfigno());
                updateRecordService.updateInstrumentMonitor(instrumentName, showModel.getEquipmentname(), showModel.getHospitalname(), instrumentparamconfig.getUserName(), one1, one2, "1", "1");
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
    public ApiResponse<Page<InstrumentParamConfigInfos>> showInsStart(String hospitalcode, String equipmenttypeid, Integer pagesize, Integer pagenum) {
        ApiResponse<Page<InstrumentParamConfigInfos>> apiResponse = new ApiResponse<Page<InstrumentParamConfigInfos>>();
        List<InstrumentParamConfigInfos> instrumentParamConfigInfosList = new ArrayList<InstrumentParamConfigInfos>();
        List<InstrumentParamConfigInfo> instrumentParamConfigInfoList = new ArrayList<InstrumentParamConfigInfo>();
        EquipmentConfigInfoModel equipmentConfigInfoModel = new EquipmentConfigInfoModel();
        equipmentConfigInfoModel.setHospitalcode(hospitalcode);
        equipmentConfigInfoModel.setEquipmenttypeid(equipmenttypeid);
        Integer start = (pagenum - 1) * pagesize;
        Integer end = pagesize;
        PageRowBounds page = new PageRowBounds(start, end);
        try {
            //分页插件
            instrumentParamConfigInfosList = instrumentParamConfigInfoMapper.ShowInsParamConfigInfos(page, equipmentConfigInfoModel);
            if (CollectionUtils.isEmpty(instrumentParamConfigInfosList)) {
                apiResponse.setCode(ApiResponse.FAILED);
                apiResponse.setMessage("当前设备类型无设备，展示失败");
                return apiResponse;
            }
            PageInfo<InstrumentParamConfigInfos> pageInfo = new PageInfo<>(instrumentParamConfigInfosList);
            for (InstrumentParamConfigInfos instrumentParamConfigInfos : pageInfo.getList()) {
                instrumentParamConfigInfoList = instrumentParamConfigInfoMapper.ShowInsParamConfigInfo(instrumentParamConfigInfos.getEquipmentno());
                instrumentParamConfigInfos.setInstrumentParamConfigInfoList(instrumentParamConfigInfoList);
            }
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
    public ApiResponse<String> updateEquipmentClientvisible(String equipmentno, String clientvisible,String username) {
        EquipmentInfoModel one = monitorInstrumentMapper.getEquipmentInfoByEquipmentno(equipmentno);
        instrumentparamConfigSetMapper.updateEquipmentClientvisible(new Integer(clientvisible), equipmentno);
        EquipmentInfoModel one1 = monitorInstrumentMapper.getEquipmentInfoByEquipmentno(equipmentno);
        boolean flag = true;
        if (StringUtils.equals("0",clientvisible)){
            flag = false ;
        }
        one1.setClientvisible(flag);
        //设备禁用需要redis缓存同步
        String type = "启用";
        if (StringUtils.equals(clientvisible, "0")) {
            type = "禁用";
        }
        LOGGER.info("开始设备禁用启用");
        LOGGER.info("当前值："+JsonUtil.toJson(one1)+"  历史值："+JsonUtil.toJson(one));
        String equipmentName = instrumentparamConfigSetMapper.getEquipmentName(equipmentno);
        String hospitalNameByEquipmentno = monitorInstrumentMapper.getHospitalNameByEquipmentno(equipmentno);

        updateRecordService.updateEquipmentMonitor(equipmentName,hospitalNameByEquipmentno,username,one,one1,"1","1");
        ApiResponse<String> apiResponse = new ApiResponse<>();
        return apiResponse;
    }

    @Override
    public String warningMessageSend(String pkid) {

        // 获取当前pkid 获取患者信息
        List<MessageSendModel> messageSendModels = instrumentMonitorInfoMapper.selectSendInfoByPkid(pkid);
        if (CollectionUtils.isEmpty(messageSendModels)) {
            return "当前报警记录不存在";
        }
        try {
            int i = 0;
            for (MessageSendModel messageSendModel : messageSendModels) {
                // 当存在 devicetoken  、devicetype 时候
                if (StringUtils.isNoneEmpty(messageSendModel.getDevice_type(), messageSendModel.getDeviceToken())) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("device_type", messageSendModel.getDevice_type());
                    map.put("DeviceToken", messageSendModel.getDeviceToken());
                    map.put("equipmentTypeName", messageSendModel.getEquipmentTypeName());
                    map.put("content", messageSendModel.getWarningremark());
                    map.put("title", messageSendModel.getEquipmentTypeName() + "异常！");
                    map.put("inputDateTime", messageSendModel.getInputdatetime());
                    map.put("equipmentTypeNo", messageSendModel.getEquipmentTypeNo());
                    map.put("equipmentNo", messageSendModel.getEquipmentNo());
                    map.put("equipmentName", messageSendModel.getEquipmentName());
                    map.put("instrumentNo", messageSendModel.getInstrumentNo());
                    boolean device_type = UmengPushUtil.sendUnicastMessage(map.get("device_type").toString(), map);
                    LOGGER.info("友盟发送对象:" + JsonUtil.toJson(map));
                    LOGGER.info("友盟是否发送成功：" + device_type);
                    if (device_type) {
                        i++;
                    }
                }
            }
            if (i > 0) {
                LOGGER.info("友盟发送数量：" + i);
                return "发送成功" + "友盟发送数量：" + i;
            } else {

                return "当前报警信息无人员账户用app登录";
            }


        } catch (Exception e) {
            LOGGER.error("发送失败：原因" + e.getMessage());

            return "未知原因异常，请联系管理员";
        }

    }

    @Override
    public ApiResponse<String> updateDeviceToken(UpdateDeviceTokenModel updateDeviceTokenModel) {
        ApiResponse<String> apiResponse = new ApiResponse<String>();
        String userid = updateDeviceTokenModel.getUserid();
        String devicetoken = updateDeviceTokenModel.getDevicetoken();
        try {
            userrightDao.updateDeviceTokenById(devicetoken, userid);
        } catch (Exception e) {
            LOGGER.error("修改devicetoken失败：" + e.getMessage());
            apiResponse.setMessage("修改失败");
            apiResponse.setCode(ApiResponse.FAILED);
            return apiResponse;
        }
        return null;
    }

    @Override
    public ApiResponse<List<UpsModel>> getCurrentUps(String hospitalcode, String equipmenttypeid) {
        ApiResponse<List<UpsModel>> apiResponse = new ApiResponse<>();
        List<UpsModel> upsModels = new ArrayList<>();
        List<Monitorequipment> monitorequipmentList;
        try {
            monitorequipmentList = equipmentInfoMapper.getEquipmentByType(hospitalcode, equipmenttypeid);
            if (CollectionUtils.isEmpty(monitorequipmentList)) {
                apiResponse.setCode(ApiResponse.FAILED);
                apiResponse.setMessage("当前设备类型无设备信息");
                return apiResponse;
            }
            Map<String, List<Monitorequipment>> collect1 = monitorequipmentList.stream().collect(Collectors.groupingBy(Monitorequipment::getEquipmentno));
            HashOperations<Object, Object, Object> objectObjectObjectHashOperations = redisTemplateUtil.opsForHash();
            Set<String> collect = monitorequipmentList.stream().map(Monitorequipment::getEquipmentno).collect(Collectors.toSet());
            collect.forEach(s->{
                UpsModel upsModel = new UpsModel();
                String lastdata = (String) objectObjectObjectHashOperations.get("LASTDATA"+hospitalcode, s);
                if (StringUtils.isNotEmpty(lastdata)) {
                    Monitorequipmentlastdata monitorequipmentlastdata = JsonUtil.toBean(lastdata, Monitorequipmentlastdata.class);
                    String equipmentno = monitorequipmentlastdata.getEquipmentno();
                    Monitorequipment monitorequipment = collect1.get(equipmentno).get(0);
                    upsModel.setEquipmentno(equipmentno);
                    upsModel.setEquipmentname(monitorequipment.getEquipmentname());
                    String currentups = monitorequipmentlastdata.getCurrentups();
                    if (StringUtils.isNotEmpty(currentups)){
                        upsModel.setCurrentups(currentups);
                    }
                    String voltage = monitorequipmentlastdata.getVoltage();
                    if (StringUtils.isNotEmpty(voltage)){
                        upsModel.setVoltage(voltage);
                    }
                    upsModels.add(upsModel);
                }
            });
            if (ObjectUtils.isEmpty(upsModels)) {
                apiResponse.setMessage("无市电记录");
                apiResponse.setCode(ApiResponse.FAILED);
                return apiResponse;
            }
            apiResponse.setResult(upsModels);
            return apiResponse;
        } catch (Exception e) {
            LOGGER.info(e.getMessage());
            apiResponse.setMessage("服务异常");
            apiResponse.setCode(ApiResponse.FAILED);
            return apiResponse;
        }
    }

    @Override
    public ApiResponse<List<InstrumentMonitorInfoModel>> getLowHighLimit(String equipmentno) {
        ApiResponse<List<InstrumentMonitorInfoModel>> apiResponse = new ApiResponse<>();
        List<InstrumentMonitorInfoModel> instrumentMonitorInfoModels = instrumentParamConfigInfoMapper.selectInstrumentByEqNo(Collections.singletonList(equipmentno));
        apiResponse.setResult(instrumentMonitorInfoModels);
        return apiResponse;
    }


}
