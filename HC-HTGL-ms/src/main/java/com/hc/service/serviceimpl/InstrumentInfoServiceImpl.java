package com.hc.service.serviceimpl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.PageRowBounds;
import com.hc.config.RedisTemplateUtil;
import com.hc.dao.InstrumentParamConfigDao;
import com.hc.dao.MonitorInstrumentDao;
import com.hc.dao.MonitorInstrumentTypeDao;
import com.hc.entity.Instrumentparamconfig;
import com.hc.entity.Monitorequipmentlastdata;
import com.hc.entity.Monitorinstrument;
import com.hc.entity.Monitorinstrumenttype;
import com.hc.mapper.laboratoryFrom.InstrumentMonitorInfoMapper;
import com.hc.mapper.laboratoryFrom.MonitorInstrumentMapper;
import com.hc.model.MapperModel.PageUserModel;
import com.hc.model.RequestModel.InstrumentInfoModel;
import com.hc.model.RequestModel.InstrumentPageModel;
import com.hc.model.ResponseModel.AllInstrumentInfoModel;
import com.hc.model.ShowModel;
import com.hc.my.common.core.bean.InstrumentMonitorInfoModel;
import com.hc.service.InstrumentInfoService;
import com.hc.service.UpdateRecordService;
import com.hc.units.ApiResponse;
import com.hc.units.JsonUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Created by 16956 on 2018-08-07.
 */
@Service
public class InstrumentInfoServiceImpl implements InstrumentInfoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(InstrumentInfoServiceImpl.class);
    @Autowired
    private MonitorInstrumentTypeDao monitorInstrumentTypeDao;
    @Autowired
    private MonitorInstrumentMapper monitorInstrumentMapper;
    @Autowired
    private MonitorInstrumentDao monitorInstrumentDao;
    @Autowired
    private InstrumentMonitorInfoMapper instrumentMonitorInfoMapper;
    @Autowired
    private InstrumentParamConfigDao instrumentParamConfigDao;
    @Autowired
    private RedisTemplateUtil redisTemplateUtil;
    @Autowired
    private UpdateRecordService updateRecordService;

    /**
     * 当前探头默认为 添加有线探头
     *
     * @param instrumentInfoModel
     * @return
     */
    @Override
    public ApiResponse<String> addInstrument(InstrumentInfoModel instrumentInfoModel) {
        ApiResponse<String> apiResponse = new ApiResponse<String>();
        Monitorinstrument monitorinstrument = new Monitorinstrument();
        String equipmentno = instrumentInfoModel.getEquipmentno();
        String hospitalcode = instrumentInfoModel.getHospitalcode();
        Integer instrumenttypeid = instrumentInfoModel.getInstrumenttypeid();
        String sn = instrumentInfoModel.getSn();
        Integer alarmtime = instrumentInfoModel.getAlarmtime();
        String instrumentname = instrumentInfoModel.getInstrumentname();
        try {
            //判断sn是否存在
            Integer J = monitorInstrumentMapper.isExist(sn);
            if (J > 0) {
                apiResponse.setMessage("添加探头失败，当前探头已经被占用");
                apiResponse.setCode(ApiResponse.FAILED);
                return apiResponse;
            }
            monitorinstrument.setInstrumentname(instrumentname);
            monitorinstrument.setEquipmentno(equipmentno);
            monitorinstrument.setHospitalcode(hospitalcode);
            monitorinstrument.setAlarmtime(alarmtime);
            monitorinstrument.setSn(sn);
            monitorinstrument.setInstrumenttypeid(instrumenttypeid);
            monitorinstrument.setInstrumentno(UUID.randomUUID().toString().replaceAll("-", ""));
            monitorinstrument = monitorInstrumentDao.save(monitorinstrument);
            //同步缓存
            HashOperations<Object, Object, Object> objectObjectObjectHashOperations = redisTemplateUtil.opsForHash();
            objectObjectObjectHashOperations.put("hospital:sn", monitorinstrument.getSn(), JsonUtil.toJson(monitorinstrument));
            return apiResponse;
        } catch (Exception e) {
            LOGGER.error("失败：" + e.getMessage());
            apiResponse.setMessage("服务异常");
            apiResponse.setCode(ApiResponse.FAILED);
            return apiResponse;
        }
    }

    /**
     * 添加探头监控类型
     *
     * @param instrumentInfoModel
     * @return
     */
    @Override
    public ApiResponse<String> addInstrumentParam(InstrumentInfoModel instrumentInfoModel) {
        ApiResponse<String> apiResponse = new ApiResponse<String>();
        String instrumentno = instrumentInfoModel.getInstrumentno();
        String instrumentname = instrumentInfoModel.getInstrumentname();
        Instrumentparamconfig instrumentparamconfig = new Instrumentparamconfig();
        Integer instrumentconfigid = instrumentInfoModel.getInstrumentconfigid();
        BigDecimal lowlimit = instrumentInfoModel.getLowlimit();
        BigDecimal highlimit = instrumentInfoModel.getHighlimit();
        String warningphone = instrumentInfoModel.getWarningphone();
        BigDecimal saturation = instrumentInfoModel.getSaturation();
        Integer instrumenttypeid = instrumentInfoModel.getInstrumenttypeid();
        String hospitalcode = instrumentInfoModel.getHospitalcode();
        instrumentparamconfig.setWarningphone(warningphone);
        instrumentparamconfig.setInstrumentparamconfigno(UUID.randomUUID().toString().replaceAll("-", ""));
        instrumentparamconfig.setInstrumentconfigid(instrumentconfigid);
        instrumentparamconfig.setHighlimit(highlimit);
        instrumentparamconfig.setLowlimit(lowlimit);
        instrumentparamconfig.setInstrumentno(instrumentno);
        instrumentparamconfig.setInstrumenttypeid(instrumenttypeid);
        instrumentparamconfig.setInstrumentname(instrumentname);
        instrumentparamconfig.setSaturation(saturation);
        try {
            instrumentparamconfig = instrumentParamConfigDao.save(instrumentparamconfig);
            //执行同步缓存
            HashOperations<Object, Object, Object> objectObjectObjectHashOperations = redisTemplateUtil.opsForHash();
            objectObjectObjectHashOperations.put("insprobe"+hospitalcode, instrumentparamconfig.getInstrumentno() + ":" + instrumentparamconfig.getInstrumentconfigid(), JsonUtil.toJson(instrumentparamconfig));
            return apiResponse;
        } catch (Exception e) {
            LOGGER.error("添加探头类型失败，原因：" + e.getMessage());
            apiResponse.setMessage("添加探头类型失败");
            apiResponse.setCode(ApiResponse.FAILED);
            return apiResponse;
        }
    }

    @Override
    public ApiResponse<String> deleteInstrument(InstrumentInfoModel instrumentInfoModel) {
        ApiResponse<String> apiResponse = new ApiResponse<String>();
        try {
            InstrumentMonitorInfoModel instrumentMonitorInfoModel = instrumentMonitorInfoMapper.selectInstrumentOneInfo(instrumentInfoModel.getInstrumentparamconfigNO());
            if (ObjectUtils.isEmpty(instrumentMonitorInfoModel)) {
                apiResponse.setMessage("当前探头已经被删除");
                return apiResponse;
            }
            String instrumentName = monitorInstrumentMapper.getInstrumentName(instrumentInfoModel.getInstrumentparamconfigNO());
            String instrumentparamconfigNO = instrumentInfoModel.getInstrumentparamconfigNO();
            String usernames = instrumentInfoModel.getUsernames();
            //获取医院名称
            ShowModel showModel = monitorInstrumentMapper.getHospitalNameEquipmentNameByNo(instrumentparamconfigNO);
            InstrumentInfoModel one = monitorInstrumentMapper.getInstrumentInfoByNo(instrumentparamconfigNO);
            String hospitalcode = one.getHospitalcode();
            InstrumentInfoModel instrumentInfoModel1 = new InstrumentInfoModel();
            updateRecordService.updateInstrumentMonitor(instrumentName,showModel.getEquipmentname(),showModel.getHospitalname(),usernames,one,instrumentInfoModel1,"0","2");
            instrumentParamConfigDao.delete(instrumentInfoModel.getInstrumentparamconfigNO());
            HashOperations<Object, Object, Object> objectObjectObjectHashOperations = redisTemplateUtil.opsForHash();
            objectObjectObjectHashOperations.delete("insprobe"+hospitalcode, instrumentMonitorInfoModel.getInstrumentno() + ":" + instrumentMonitorInfoModel.getInstrumentconfigid());
        } catch (Exception e) {
            LOGGER.error("失败：" + e.getMessage());
            apiResponse.setMessage("删除探头失败，请联系管理员");
            apiResponse.setCode(ApiResponse.FAILED);
            return apiResponse;
        }
        //判断当前探头类型是否不存在了
        Integer count = instrumentMonitorInfoMapper.getCount(instrumentInfoModel.getInstrumentno());
        try {
            if (count == 0) {
                //redis缓存删除
                Monitorinstrument one = monitorInstrumentDao.findOne(instrumentInfoModel.getInstrumentno());

                HashOperations<Object, Object, Object> objectObjectObjectHashOperations = redisTemplateUtil.opsForHash();
                if(org.apache.commons.lang3.StringUtils.isNotEmpty(one.getChannel())){
                    if ("1".equals(one.getChannel())){
                        objectObjectObjectHashOperations.delete("hospital:sn", one.getSn());
                        objectObjectObjectHashOperations.delete("DOOR:"+one.getChannel(),one.getSn());
                        monitorInstrumentDao.delete(instrumentInfoModel.getInstrumentno());
                    }else{
                        objectObjectObjectHashOperations.delete("DOOR:"+one.getChannel(),one.getSn());
                        monitorInstrumentDao.delete(instrumentInfoModel.getInstrumentno());
                    }
                }else{
                    objectObjectObjectHashOperations.delete("hospital:sn", one.getSn());
                    //探头类型删除完了，将探头表里面那一行探头也删除了吧
                    monitorInstrumentDao.delete(instrumentInfoModel.getInstrumentno());
                }

            }
        } catch (Exception e) {
            LOGGER.error("删除探头失败,原因:" + e.getMessage());
            apiResponse.setCode(ApiResponse.FAILED);
            apiResponse.setMessage("删除总探头失败，请联系管理员");
            return apiResponse;
        }
        return apiResponse;

    }

    /**
     * 修改sn号，最高值 、 最低值  启用  禁用 报警  、通道设置
     *
     * @param instrumentInfoModel
     * @return
     */
    @Override
    @Transactional(rollbackOn = Exception.class)
    public synchronized ApiResponse<String> updateInstrument(InstrumentInfoModel instrumentInfoModel) {
        ApiResponse<String> apiResponse = new ApiResponse<String>();
        Monitorinstrument monitorinstrument = new Monitorinstrument();
        String instrumentname = instrumentInfoModel.getInstrumentname();
        String equipmentno = instrumentInfoModel.getEquipmentno();
        String hospitalcode = instrumentInfoModel.getHospitalcode();
        Integer instrumenttypeid = instrumentInfoModel.getInstrumenttypeid();
        String sn = instrumentInfoModel.getSn();
        Integer alarmtime = instrumentInfoModel.getAlarmtime();
        String instrumentno = instrumentInfoModel.getInstrumentno();
        Instrumentparamconfig instrumentparamconfig = new Instrumentparamconfig();
        String instrumentparamconfigNO = instrumentInfoModel.getInstrumentparamconfigNO();
        Integer instrumentconfigid = instrumentInfoModel.getInstrumentconfigid();
        BigDecimal lowlimit = instrumentInfoModel.getLowlimit();
        BigDecimal highlimit = instrumentInfoModel.getHighlimit();
        String warningphone = instrumentInfoModel.getWarningphone();
        String calibration = instrumentInfoModel.getCalibration();
        String channel = instrumentInfoModel.getChannel();
        BigDecimal saturation = instrumentInfoModel.getSaturation();
        try {
            //修改sn时候查询当前修改后的sn是否在其余位置存在
            Monitorinstrument one = monitorInstrumentMapper.isSn(instrumentno);
            if (!StringUtils.equals(sn,one.getSn())){
                Monitorinstrument sn2 = monitorInstrumentMapper.isSns(sn);
                if (null!=sn2) {
                    apiResponse.setMessage("当前sn已被其他设备占用");
                    apiResponse.setCode(ApiResponse.FAILED);
                    return apiResponse;
                }
            }
            HashOperations<Object, Object, Object> objectObjectObjectHashOperations = redisTemplateUtil.opsForHash();
            String channel1 = one.getChannel();
            if (StringUtils.isNotEmpty(channel1)) {
                if (!StringUtils.equals(channel1,channel)) {
                    LOGGER.info("删除之前开关量缓存");
                    objectObjectObjectHashOperations.delete("DOOR:"+channel1,sn);
                }
            }
            monitorinstrument.setChannel(channel);
            monitorinstrument.setInstrumentname(instrumentname);
            monitorinstrument.setEquipmentno(equipmentno);
            monitorinstrument.setHospitalcode(hospitalcode);
            monitorinstrument.setAlarmtime(alarmtime);
            monitorinstrument.setSn(sn);
            monitorinstrument.setInstrumenttypeid(instrumenttypeid);
            monitorinstrument.setInstrumentno(instrumentno);
            String usernames = instrumentInfoModel.getUsernames();
            InstrumentInfoModel one1 = monitorInstrumentMapper.getInstrumentInfoByNoNew(instrumentparamconfigNO);
            String instrumentName = one1.getInstrumentconfigname();
            String hospitalname = one1.getHospitalname();
            String equipmentname = one1.getEquipmentname();
            updateRecordService.updateInstrumentMonitor(instrumentName,equipmentname,hospitalname,usernames,one1,instrumentInfoModel,"0","1");
            monitorInstrumentDao.save(monitorinstrument);
            //更新开关量以及报警次数缓存
            Monitorinstrument monitorinstrument1 = JsonUtil.toBean((String) objectObjectObjectHashOperations.get("hospital:sn", monitorinstrument.getSn()), Monitorinstrument.class);
            if (null!=monitorinstrument1){
                monitorinstrument1.setChannel(channel);
                monitorinstrument1.setInstrumentname(instrumentname);
                monitorinstrument1.setEquipmentno(equipmentno);
                monitorinstrument1.setHospitalcode(hospitalcode);
                monitorinstrument1.setAlarmtime(alarmtime);
                monitorinstrument1.setSn(sn);
                monitorinstrument1.setInstrumenttypeid(instrumenttypeid);
                monitorinstrument1.setInstrumentno(instrumentno);
            }
            if (StringUtils.isNotEmpty(monitorinstrument.getChannel())){
                //同步缓存
                objectObjectObjectHashOperations.put("DOOR:"+monitorinstrument.getChannel(),monitorinstrument.getSn(), JsonUtil.toJson(monitorinstrument));
                if ("1".equals(monitorinstrument.getChannel())){
                    //默认通道一绑定监控co2 o2 温度
                    objectObjectObjectHashOperations.put("hospital:sn", monitorinstrument.getSn(), JsonUtil.toJson(monitorinstrument1));
                }
            }else {
                objectObjectObjectHashOperations.put("hospital:sn", monitorinstrument.getSn(), JsonUtil.toJson(monitorinstrument1));
            }
            instrumentparamconfig.setInstrumenttypeid(instrumenttypeid);
            instrumentparamconfig.setInstrumentno(instrumentno);
            instrumentparamconfig.setInstrumentname(instrumentname);
            instrumentparamconfig.setLowlimit(lowlimit);
            instrumentparamconfig.setHighlimit(highlimit);
            instrumentparamconfig.setWarningphone(warningphone);
            instrumentparamconfig.setInstrumentconfigid(instrumentconfigid);
            instrumentparamconfig.setInstrumentparamconfigno(instrumentparamconfigNO);
            instrumentparamconfig.setAlarmtime(alarmtime);
            instrumentparamconfig.setCalibration(calibration);
            instrumentparamconfig.setSaturation(saturation);
            Instrumentparamconfig one2 = instrumentParamConfigDao.getOne(instrumentparamconfigNO);
            instrumentparamconfig.setFirsttime(one2.getFirsttime());
            instrumentParamConfigDao.save(instrumentparamconfig);
            InstrumentMonitorInfoModel instrumentMonitorInfoModel = instrumentMonitorInfoMapper.selectInstrumentOneInfo(instrumentparamconfigNO);
            //强制修改只能操作的元素覆盖查询的元素
            instrumentMonitorInfoModel.setAlarmtime(instrumentparamconfig.getAlarmtime());
            instrumentMonitorInfoModel.setWarningphone(instrumentparamconfig.getWarningphone());
            instrumentMonitorInfoModel.setHighlimit(instrumentparamconfig.getHighlimit());
            instrumentMonitorInfoModel.setLowlimit(instrumentparamconfig.getLowlimit());
            if (StringUtils.isNotEmpty(calibration)){
                instrumentMonitorInfoModel.setCalibration(calibration);
            }
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
    public ApiResponse<Page<AllInstrumentInfoModel>> selectInstrumentPage(InstrumentPageModel instrumentPageModel) {
        ApiResponse<Page<AllInstrumentInfoModel>> apiResponse = new ApiResponse<Page<AllInstrumentInfoModel>>();
        List<AllInstrumentInfoModel> monitorEquipmentInfoModels = new ArrayList<AllInstrumentInfoModel>();
        PageUserModel pageUserModel = new PageUserModel();
        Integer pagenum = instrumentPageModel.getPagenum();
        Integer pagesize = instrumentPageModel.getPagesize();
        String fuzzy = instrumentPageModel.getFuzzy();
        String equipmenttypeid = instrumentPageModel.getEquipmenttypeid();
        String hospitalcode = instrumentPageModel.getHospitalcode();
        String equipmentno = instrumentPageModel.getEquipmentno();
        try {
            Integer start = (pagenum - 1) * pagesize;
            Integer end = pagesize;
            PageRowBounds page = new PageRowBounds(start, end);
            pageUserModel.setHospitalcode(hospitalcode);
            if (!StringUtils.isEmpty(fuzzy)) {
                fuzzy = "%" + fuzzy + "%";
            }
            pageUserModel.setFuzzy(fuzzy);
            pageUserModel.setEquipmenttypeid(equipmenttypeid);
            pageUserModel.setEquipmentno(equipmentno);
            monitorEquipmentInfoModels = instrumentMonitorInfoMapper.selectInstrumentPage(page, pageUserModel);
            if (CollectionUtils.isEmpty(monitorEquipmentInfoModels)) {
                apiResponse.setCode(ApiResponse.FAILED);
                apiResponse.setMessage("无探头信息");
                return apiResponse;
            }
            PageInfo<AllInstrumentInfoModel> pageInfo = new PageInfo<AllInstrumentInfoModel>(monitorEquipmentInfoModels);
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
    public ApiResponse<List<Monitorinstrumenttype>> showInstrumentType() {
        ApiResponse<List<Monitorinstrumenttype>> apiResponse = new ApiResponse<List<Monitorinstrumenttype>>();
        List<Monitorinstrumenttype> list = new ArrayList<Monitorinstrumenttype>();
        try{
            list = monitorInstrumentTypeDao.findAll();

            if (CollectionUtils.isNotEmpty(list)){
                for (int i = list.size() -1;i >=0;i--) {
                    String instrumenttypename = list.get(i).getInstrumenttypename();
                    if ("有线设备".equals(instrumenttypename)) {
                        list.remove(i);
                    }

                }
                apiResponse.setResult(list);
            }else{
                apiResponse.setMessage("不存在监控设备信息");
                apiResponse.setCode(ApiResponse.FAILED);
            }
            return apiResponse;
        }catch (Exception e){
            LOGGER.error("显示所有探头监控设备类型失败，原因："+e.getMessage());
            apiResponse.setCode(ApiResponse.FAILED);
            apiResponse.setMessage("服务异常，请联系管理员");
            return apiResponse;
        }

    }

    /**
     * 清理设备缓存数据
     *
     * @param instrumentInfoModel
     * @return
     */
    @Override
    public ApiResponse<String> cleanEquipment(AllInstrumentInfoModel instrumentInfoModel) {
        String hospitalcode = instrumentInfoModel.getHospitalcode();
        String equipmentno = instrumentInfoModel.getEquipmentno();
        ApiResponse<String> apiResponse = new ApiResponse<String>();
        if(StringUtils.isEmpty(hospitalcode)){
            apiResponse.setMessage("清理缓存失败,医院编码错误");
            apiResponse.setCode(ApiResponse.FAILED);
            return apiResponse;
        }
        if(StringUtils.isEmpty(equipmentno)){
            apiResponse.setMessage("清理缓存失败,设备编码错误");
            apiResponse.setCode(ApiResponse.FAILED);
            return apiResponse;
        }
        HashOperations<Object, Object, Object> objectObjectObjectHashOperations = redisTemplateUtil.opsForHash();
        StringBuffer lastKey = new StringBuffer("LASTDATA"+hospitalcode);
        String lastDataKey = lastKey.toString();
        Set<Object> keys = objectObjectObjectHashOperations.keys(lastDataKey);
        if(!objectObjectObjectHashOperations.hasKey(lastDataKey,equipmentno)){
            apiResponse.setMessage("清理缓存失败,数据不存在");
            apiResponse.setCode(ApiResponse.FAILED);
            return apiResponse;
        }
        Long delete = objectObjectObjectHashOperations.delete(lastDataKey, equipmentno);
        if(delete > 0){
            apiResponse.setMessage("清理缓存成功");
            apiResponse.setCode(ApiResponse.SUCCESS);
        }else{
            apiResponse.setMessage("清理缓存失败");
            apiResponse.setCode(ApiResponse.FAILED);
        }
        return apiResponse;
    }
}
