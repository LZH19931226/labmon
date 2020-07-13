package com.hc.serviceimpl;

import com.hc.bean.WarningModel;
import com.hc.config.RedisTemplateUtil;
import com.hc.dao.InstrumentparamconfigDao;
import com.hc.entity.Hospitalofreginfo;
import com.hc.model.ResponseModel.InstrumentMonitorInfoModel;
import com.hc.service.WarningRuleService;
import com.hc.utils.JsonUtil;
import com.hc.utils.TimeHelper;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by 16956 on 2018-08-10.
 */
@Service
public class WarningRuleServiceImpl implements WarningRuleService {
    private static final Logger LOGGER = LoggerFactory.getLogger(WarningRuleServiceImpl.class);
    @Autowired
    private RedisTemplateUtil redisTemplateUtil;
   
    @Autowired
    private InstrumentparamconfigDao instrumentparamconfigDao;

    /**
     * 进来的默认都是启用报警的
     * 先判断医院   、  在进行判断是否三次报警
     */
    @Override
    public WarningModel warningRule(String hospitalcode, String pkid, String data, InstrumentMonitorInfoModel instrumentMonitorInfoModel,String remark) {
        try {
            Hospitalofreginfo hospitalofreginfo;
            WarningModel warningModel = new WarningModel();
            if (StringUtils.isNotEmpty(remark)) {
                if (StringUtils.equals("市电异常",remark)) {
                    warningModel.setPkid(pkid);
                    warningModel.setValue(data);
                    warningModel.setEquipmentname(instrumentMonitorInfoModel.getEquipmentname());
                    warningModel.setUnit(instrumentMonitorInfoModel.getInstrumentconfigname());
                    warningModel.setHospitalcode(hospitalcode);
                    return warningModel;
                }
            }
            BoundHashOperations<Object, Object, Object> objectObjectObjectBoundHashOperations = redisTemplateUtil.boundHashOps("hospital:info");
                String o = (String)objectObjectObjectBoundHashOperations.get(hospitalcode);
                if (StringUtils.isNotEmpty(o)){
                    hospitalofreginfo = JsonUtil.toBean(o,Hospitalofreginfo.class);
                }else{
                    LOGGER.info("不存在当前医院信息，医院编号："+hospitalcode);
                    return null;
                }
            // 增加判断
            //为满足三次报警次数不推送
            Integer alarmtime = instrumentMonitorInfoModel.getAlarmtime();
            //当前测试报警次数
            if (alarmtime == null){
                alarmtime = 3;
            }
            //判断医院是否为全天报警
            if ("1".equals(hospitalofreginfo.getAlwayalarm())) {
                //判断是否三次报警
                redisTemplateUtil.boundListOps(instrumentMonitorInfoModel.getInstrumentparamconfigNO()).rightPush(data);
                if (redisTemplateUtil.boundListOps(instrumentMonitorInfoModel.getInstrumentparamconfigNO()).size() < alarmtime) {
                    return null;
                } else {
                    //全天报警
                    //根据医院编号查询报警联系人电话号码
                    //第三次报警
                    redisTemplateUtil.delete(instrumentMonitorInfoModel.getInstrumentparamconfigNO());
                    warningModel.setPkid(pkid);
                    warningModel.setValue(data);
                    warningModel.setEquipmentname(instrumentMonitorInfoModel.getEquipmentname());
                    warningModel.setUnit(instrumentMonitorInfoModel.getInstrumentconfigname());
                    warningModel.setHospitalcode(hospitalcode);
                }
            } else {
                //判断是否在当前禁用报警时间段内
                Date starttime = hospitalofreginfo.getBegintime();
                Date endtime = hospitalofreginfo.getEndtime();
                Integer compareToStart = TimeHelper.dateHelp(new Date()).compareTo(TimeHelper.dateHelp(starttime));
                Integer compareToEnd = TimeHelper.dateHelp(new Date()).compareTo(TimeHelper.dateHelp(endtime));
                if (compareToStart < 0 || compareToEnd > 0) {
                    //开始报警
                    //全天报警
                    //根据医院编号查询报警联系人电话号码.
                    redisTemplateUtil.boundListOps(instrumentMonitorInfoModel.getInstrumentparamconfigNO()).rightPush(data);
                    if (redisTemplateUtil.boundListOps(instrumentMonitorInfoModel.getInstrumentparamconfigNO()).size() < alarmtime) {
                        return null;
                    } else {
                        redisTemplateUtil.delete(instrumentMonitorInfoModel.getInstrumentparamconfigNO());
                        warningModel.setPkid(pkid);
                        warningModel.setValue(data);
                        warningModel.setEquipmentname(instrumentMonitorInfoModel.getEquipmentname());
                        warningModel.setUnit(instrumentMonitorInfoModel.getInstrumentconfigname());
                        warningModel.setHospitalcode(hospitalcode);
                    }
                } else {
                    redisTemplateUtil.boundListOps(instrumentMonitorInfoModel.getInstrumentparamconfigNO()).rightPush(data);
                    if (redisTemplateUtil.boundListOps(instrumentMonitorInfoModel.getInstrumentparamconfigNO()).size() < alarmtime) {
                        return null;
                    } else {
                        redisTemplateUtil.delete(instrumentMonitorInfoModel.getInstrumentparamconfigNO());
                    }
                }
            }
            //半小时内不重复报警
            //判断半小时内是否报警
            if (ObjectUtils.allNotNull(instrumentMonitorInfoModel.getWarningtime())){
                double poor = TimeHelper.getDatePoorMin(new Date(),instrumentMonitorInfoModel.getWarningtime());
                if (poor > 60){
                    //可以报警
                    instrumentparamconfigDao.updateWarnTime(new Date(),instrumentMonitorInfoModel.getInstrumentparamconfigNO());
                    instrumentMonitorInfoModel.setWarningtime(new Date());
                    //同步缓存
                    HashOperations<Object, Object, Object> objectObjectObjectHashOperations = redisTemplateUtil.opsForHash();
                    objectObjectObjectHashOperations.put("hospital:instrumentparam",instrumentMonitorInfoModel.getInstrumentno() + ":" + instrumentMonitorInfoModel.getInstrumentconfigid().toString(),JsonUtil.toJson(instrumentMonitorInfoModel));
                }else{

                    return null;

                }
            }else{
                instrumentparamconfigDao.updateWarnTime(new Date(),instrumentMonitorInfoModel.getInstrumentparamconfigNO());
                instrumentMonitorInfoModel.setWarningtime(new Date());
                //同步缓存
                HashOperations<Object, Object, Object> objectObjectObjectHashOperations = redisTemplateUtil.opsForHash();
                objectObjectObjectHashOperations.put("hospital:instrumentparam",instrumentMonitorInfoModel.getInstrumentno() + ":" + instrumentMonitorInfoModel.getInstrumentconfigid().toString(),JsonUtil.toJson(instrumentMonitorInfoModel));
            }
            return warningModel;
        } catch (Exception e) {
            LOGGER.error("报警规则异常：原因：" + e+JsonUtil.toJson(instrumentMonitorInfoModel));
            return null;
        }


    }
}
