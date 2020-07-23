package com.hc.listenter;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.hc.entity.Monitorequipmentlastdata;
import com.hc.bean.WarningModel;
import com.hc.bean.WarningMqModel;
import com.hc.config.RedisTemplateUtil;
import com.hc.dao.MonitorequipmentlastdataDao;
import com.hc.dao.SendrecordDao;
import com.hc.dao.UserrightDao;
import com.hc.dao.WarningrecordDao;
import com.hc.entity.Monitorinstrument;
import com.hc.entity.Sendrecord;
import com.hc.entity.Userright;
import com.hc.exchange.BaoJinMsg;
import com.hc.model.TimeoutEquipment;
import com.hc.service.SendMesService;
import com.hc.service.WarningService;
import com.hc.utils.HttpUtil;
import com.hc.utils.JsonUtil;
import com.hc.utils.TimeHelper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.*;

@Component
@EnableBinding(BaoJinMsg.class)
public class SocketMessageListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(SocketMessageListener.class);

    @Autowired
    private SendMesService sendMesService;
    @Autowired
    private RedisTemplateUtil redisTemplateUtil;
    @Autowired
    private WarningService warningService;

    @Autowired
    private WarningrecordDao warningrecordDao;
    @Autowired
    private UserrightDao userrightDao;


    @Autowired
    private MonitorequipmentlastdataDao monitorequipmentlastdataDao;

    /**
     * 监听报警信息
     */
    @StreamListener(BaoJinMsg.EXCHANGE_NAME)
    public void onMessage1(String messageContent) {
        LOGGER.info("从通道" + BaoJinMsg.EXCHANGE_NAME + ":{}" + messageContent);
        msctMessage(messageContent);

    }


    /**
     * 监听报警信息
     */
    @StreamListener(BaoJinMsg.EXCHANGE_NAME1)
    public void onMessage2(String messageContent) {
        LOGGER.info("从通道" + BaoJinMsg.EXCHANGE_NAME1 + ":{}" + JsonUtil.toJson(messageContent));
        msctMessage(messageContent);

    }


    /**
     * 监听报警信息
     */
    @StreamListener(BaoJinMsg.EXCHANGE_NAME2)
    public void onMessage3(String messageContent) {
        LOGGER.info("从通道" + BaoJinMsg.EXCHANGE_NAME2 + ":{}" + messageContent);
        msctMessage(messageContent);

    }

    /**
     * 监听超时报警信息
     *
     * @param message
     */
    @StreamListener(BaoJinMsg.EXCHANGE_NAME4)
    public void onMessage5(String message) {
        try {
            TimeoutEquipment timeoutEquipment = JsonUtil.toBean(message, TimeoutEquipment.class);
            LOGGER.info("进入超时报警队列：" + message);
            String hospitalcode = timeoutEquipment.getHospitalcode();
            // 根据hospitalcode查找设置超时报警的联系人
            List<Userright> userrightByHospitalcodeAAndTimeout = userrightDao.getUserrightByHospitalcodeAAndTimeout(hospitalcode);
            if (org.apache.commons.collections4.CollectionUtils.isEmpty(userrightByHospitalcodeAAndTimeout)) {
                return;
            }
            String equipmentname = timeoutEquipment.getEquipmentname();
            String hospitalname = timeoutEquipment.getHospitalname();
            String disabletype = timeoutEquipment.getDisabletype();
            Integer timeouttime = timeoutEquipment.getTimeouttime();
            LOGGER.info("进入超时报警队列联系人：" + JsonUtil.toJson(userrightByHospitalcodeAAndTimeout));
            for (Userright userright : userrightByHospitalcodeAAndTimeout) {
                String phonenum = userright.getPhonenum();
                if (StringUtils.isEmpty(phonenum)) {
                    continue;
                }
                switch (disabletype) {
                    case "2":
                        // 超时报警
                        sendMesService.sendMes1(phonenum, equipmentname, "超时", hospitalname, timeouttime);
                        break;
//                    case "3":
//                        //设备禁用
//                        sendMesService.sendMes1(phonenum, equipmentname, "禁用", hospitalname, 1);
//                        break;
//                    case "4":
//                        sendMesService.sendMes1(phonenum, equipmentname, "解除", hospitalname, 1);
                    default:
                        break;
                }
            }
        } catch (Exception e) {
            LOGGER.error("超时报警异常：" + e.getMessage());
        }

    }

    //监听当前值信息
    @StreamListener(BaoJinMsg.EXCHANGE_NAME3)
    public void onMessage4(String messageContent) {
        Monitorequipmentlastdata monitorequipmentlastdata = JsonUtil.toBean(messageContent, Monitorequipmentlastdata.class);
        LOGGER.info("当前值服务进来：" + messageContent);
        //头天信息删除
        //当前日期
        Date inputdatetime = monitorequipmentlastdata.getInputdatetime();

        String currentDate = TimeHelper.getDateString(inputdatetime);
        HashOperations<Object, Object, Object> objectObjectObjectHashOperations = redisTemplateUtil.opsForHash();
        String o = (String) objectObjectObjectHashOperations.get("equipmentno:lastdata", monitorequipmentlastdata.getEquipmentno() + ":" + currentDate);
        List<Monitorequipmentlastdata> list = new ArrayList<Monitorequipmentlastdata>();
        if (StringUtils.isEmpty(o)) {
            //删除昨天的数据
            String s = TimeHelper.dateDD();
            String o1 = (String) objectObjectObjectHashOperations.get("equipmentno:lastdata", monitorequipmentlastdata.getEquipmentno() + ":" + s);
            if (StringUtils.isNotEmpty(o1)) {
                //删除
                objectObjectObjectHashOperations.delete("equipmentno:lastdata", monitorequipmentlastdata.getEquipmentno() + ":" + s);
            }
            list.add(monitorequipmentlastdata);
            objectObjectObjectHashOperations.put("equipmentno:lastdata", monitorequipmentlastdata.getEquipmentno() + ":" + currentDate, JsonUtil.toJson(list));
        } else {
            list = JsonUtil.toList(o, Monitorequipmentlastdata.class);
            list.add(monitorequipmentlastdata);
            objectObjectObjectHashOperations.put("equipmentno:lastdata", monitorequipmentlastdata.getEquipmentno() + ":" + currentDate, JsonUtil.toJson(list));
        }


    }

    public void msctMessage(String message) {
        WarningMqModel mQmodel = JsonUtil.toBean(message, WarningMqModel.class);
        Monitorinstrument monitorinstrument = mQmodel.getMonitorinstrument();
        WarningModel model = warningService.produceWarn(mQmodel, mQmodel.getMonitorinstrument(), mQmodel.getDate(), mQmodel.getInstrumentconfigid(), mQmodel.getUnit());
        if (ObjectUtils.isEmpty(model)) {
            return;
        }
        String hospitalcode = model.getHospitalcode();
        List<Userright> list;
        BoundHashOperations<Object, Object, Object> hospitalphonenum = redisTemplateUtil.boundHashOps("hospital:phonenum");
        String o = (String) hospitalphonenum.get(hospitalcode);
        if (StringUtils.isNotEmpty(o)) {
            list = JsonUtil.toList(o, Userright.class);
        } else {
            LOGGER.info("查询不到当前医院用户信息,医院编号：" + hospitalcode);
            return;
        }

        String equipmentname = model.getEquipmentname();
        String unit = model.getUnit();
        String value = model.getValue();
        try {
            String pkid = model.getPkid();

            warningrecordDao.updatePhone(pkid);
        } catch (Exception e) {
            LOGGER.error("更新报警信息失败：" + e.getMessage());
        }
        //获取电话
        boolean flag = false;
        for (Userright userright : list) {
            // 发送短信
            if (StringUtils.isNotEmpty(userright.getPhonenum())) {
                flag = true;
                String reminders = userright.getReminders();
                if (StringUtils.isEmpty(reminders)){
                    LOGGER.info("拨打电话发送短信对象：" + JsonUtil.toJson(userright));
                    sendMesService.callPhone(userright.getPhonenum(), equipmentname);
                    SendSmsResponse sendSmsResponse = sendMesService.sendMes(userright.getPhonenum(), equipmentname, unit, value);
                    LOGGER.info("发送短信对象:" + JsonUtil.toJson(userright) + sendSmsResponse.getCode());
                }else if (StringUtils.equals(reminders, "1")) {
                    LOGGER.info("拨打电话发送短信对象：" + JsonUtil.toJson(userright));
                    sendMesService.callPhone(userright.getPhonenum(), equipmentname);
                } else if (StringUtils.equals(reminders, "2")) {
                    SendSmsResponse sendSmsResponse = sendMesService.sendMes(userright.getPhonenum(), equipmentname, unit, value);
                    LOGGER.info("发送短信对象:" + JsonUtil.toJson(userright) + sendSmsResponse.getCode());
                }
            }
        }
        if (flag) {
            //拨打电话
            HashOperations<Object, Object, Object> objectObjectObjectHashOperations = redisTemplateUtil.opsForHash();
            String lastdata = (String) objectObjectObjectHashOperations.get("LASTDATA", monitorinstrument.getEquipmentno());
            if (StringUtils.isNotEmpty(lastdata)) {
                //报警电话信息处理
                Monitorequipmentlastdata monitorequipmentlastdata1 = JsonUtil.toBean(lastdata, Monitorequipmentlastdata.class);
                monitorequipmentlastdata1.setPkid(UUID.randomUUID().toString().replaceAll("-", ""));
                monitorequipmentlastdata1.setInputdatetime(new Date());
                monitorequipmentlastdata1.setEquipmentlastdata("1");
                monitorequipmentlastdataDao.saveAndFlush(monitorequipmentlastdata1);
                objectObjectObjectHashOperations.put("LASTDATA", monitorinstrument.getEquipmentno(), JsonUtil.toJson(monitorequipmentlastdata1));
            }

        }
        try {
            if (StringUtils.isNotEmpty(model.getPkid())) {
                //推送APP
                Map<String, String> map = new HashMap<String, String>();
                map.put("pkid", model.getPkid());
                String s = HttpUtil.get("http://www.sosum.net:8087/api-mon/api/insParamSet/sendMessage", map);
                LOGGER.info("友盟发送状态：" + s);
            } else {
                LOGGER.info("不存在推送信息");
            }
        } catch (Exception e) {
            LOGGER.error("友盟推送失败：原因：" + e.getMessage() + "数据结构为：" + JsonUtil.toJson(model));
        }

    }


}
