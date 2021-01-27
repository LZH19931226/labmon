package com.hc.listenter;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.hc.dao.*;
import com.hc.entity.*;
import com.hc.bean.WarningModel;
import com.hc.bean.WarningMqModel;
import com.hc.config.RedisTemplateUtil;
import com.hc.exchange.BaoJinMsg;
import com.hc.model.TimeoutEquipment;
import com.hc.my.common.core.util.DateUtils;
import com.hc.service.SendMesService;
import com.hc.service.WarningService;
import com.hc.utils.JsonUtil;
import com.hc.utils.TimeHelper;
import com.hc.utils.UnitCase;
import org.apache.commons.collections4.CollectionUtils;
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
import java.util.stream.Collectors;

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
    private SendrecordDao sendrecordDao;
    @Autowired
    private WarningrecordDao warningrecordDao;
    @Autowired
    private UserrightDao userrightDao;
    @Autowired
    private UserScheduLingDao userScheduLingDao;
//    @Autowired
//    private WarningrecordSortDao warningrecordSortDao;


    @Autowired
    private MonitorequipmentlastdataDao monitorequipmentlastdataDao;

    /**
     * 监听报警信息
     */
    @StreamListener(BaoJinMsg.EXCHANGE_NAME)
    public void onMessage1(String messageContent) {
        LOGGER.info("从通道" + BaoJinMsg.EXCHANGE_NAME + ":" + messageContent);
        msctMessage(messageContent);

    }


    /**
     * 监听报警信息
     */
    @StreamListener(BaoJinMsg.EXCHANGE_NAME1)
    public void onMessage2(String messageContent) {
        LOGGER.info("从通道" + BaoJinMsg.EXCHANGE_NAME1 + ":" + JsonUtil.toJson(messageContent));
        msctMessage(messageContent);

    }


    /**
     * 监听报警信息
     */
    @StreamListener(BaoJinMsg.EXCHANGE_NAME2)
    public void onMessage3(String messageContent) {
        LOGGER.info("从通道" + BaoJinMsg.EXCHANGE_NAME2 + ":" + messageContent);
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
        String equipmentname = model.getEquipmentname();
        String unit = UnitCase.caseUint(model.getUnit());
        String value = model.getValue();
        String hospitalcode = model.getHospitalcode();
        //不走排班逻辑,定制报警需求
       // Boolean scIsTrue = warningRuleSend(model);
        Boolean scIsTrue=true;
        if (scIsTrue) {
            boolean flag = false;
            List<Userright> list = new ArrayList<>();
            List<String> phones = new ArrayList<>();
            //判断该医院当天是否有人员排班
            Date date = new Date();
            String today = DateUtils.paseDate(date);
            List<UserScheduLing> userScByHosSt1 = userScheduLingDao.findUserScByHosSt(hospitalcode, today, DateUtils.getYesterday(date));
            if (CollectionUtils.isNotEmpty(userScByHosSt1)) {
                List<UserScheduLing> lings = new ArrayList<>();
                UserScheduLing userScheduLing = userScByHosSt1.get(userScByHosSt1.size() - 1);
                Date starttime = userScheduLing.getStarttime();
                Date endtime = userScheduLing.getEndtime();
                UserScheduLing userScheduLing1 = userScByHosSt1.get(0);
                Date endtime1 = userScheduLing1.getEndtime();
                //大于今天最晚时间不处理
                //处于今天
                if (date.compareTo(starttime) >= 0 && date.compareTo(endtime) <= 0) {
                    lings = userScByHosSt1.stream().filter(s -> s.getStarttime().compareTo(starttime) == 0 && s.getEndtime().compareTo(endtime) == 0).collect(Collectors.toList());
                    //位于昨天
                } else if (date.compareTo(starttime) < 0) {
                    lings = userScByHosSt1.stream().filter(s -> s.getEndtime().compareTo(endtime1) == 0).collect(Collectors.toList());
                }
                if (CollectionUtils.isNotEmpty(lings)) {
                    for (UserScheduLing s : lings) {
                        Userright userright = new Userright();
                        //排班的人默认都是电话+短信
                        userright.setReminders(null);
                        String userphone = s.getUserphone();
                        if (StringUtils.isNotEmpty(userphone)) {
                            userright.setPhonenum(userphone);
                            phones.add(userphone);
                        }
                        list.add(userright);
                    }
                }
            }
            BoundHashOperations<Object, Object, Object> hospitalphonenum = redisTemplateUtil.boundHashOps("hospital:phonenum");
            String o = (String) hospitalphonenum.get(hospitalcode);
            if (StringUtils.isEmpty(o)) {
                LOGGER.info("查询不到当前医院用户信息,医院编号：" + hospitalcode);
                return;
            }
            //未排班的人
            if (CollectionUtils.isEmpty(list)) {
                list = JsonUtil.toList(o, Userright.class);
            } else {
                //有排班的人和未排班的人
                if (CollectionUtils.isNotEmpty(phones)) {
                    List<Userright> allusers = JsonUtil.toList(o, Userright.class);
                    if (CollectionUtils.isNotEmpty(allusers)) {
                        List<Userright> collect = allusers.stream().filter(s -> !phones.contains(s.getPhonenum())).collect(Collectors.toList());
                        list.addAll(collect);
                    }
                }
            }
            String pkid = model.getPkid();
            warningrecordDao.updatePhone(pkid);
            //获取电话
            List<Sendrecord> list1 = new ArrayList<>();
            for (Userright userright : list) {
                String reminders = userright.getReminders();
                String phonenum = userright.getPhonenum();
                //不报警
                if (StringUtils.equals(reminders, "3") || StringUtils.isEmpty(phonenum)) {
                    continue;
                }
                // 发送短信
                flag = true;
                if (StringUtils.isEmpty(reminders)) {
                    LOGGER.info("拨打电话发送短信对象：" + JsonUtil.toJson(userright));
                    sendMesService.callPhone(userright.getPhonenum(), equipmentname);
                    Sendrecord sendrecord = producePhoneRecord(userright.getPhonenum(), hospitalcode, equipmentname, unit, "1");
                    list1.add(sendrecord);
                    SendSmsResponse sendSmsResponse = sendMesService.sendMes(userright.getPhonenum(), equipmentname, unit, value);
                    LOGGER.info("发送短信对象:" + JsonUtil.toJson(userright) + sendSmsResponse.getCode());
                    Sendrecord sendrecord1 = producePhoneRecord(userright.getPhonenum(), hospitalcode, equipmentname, unit, "0");
                    list1.add(sendrecord1);
                } else if (StringUtils.equals(reminders, "1")) {
                    LOGGER.info("拨打电话发送短信对象：" + JsonUtil.toJson(userright));
                    sendMesService.callPhone(userright.getPhonenum(), equipmentname);
                    Sendrecord sendrecord = producePhoneRecord(userright.getPhonenum(), hospitalcode, equipmentname, unit, "1");
                    list1.add(sendrecord);
                } else if (StringUtils.equals(reminders, "2")) {
                    SendSmsResponse sendSmsResponse = sendMesService.sendMes(userright.getPhonenum(), equipmentname, unit, value);
                    LOGGER.info("发送短信对象:" + JsonUtil.toJson(userright) + sendSmsResponse.getCode());
                    Sendrecord sendrecord = producePhoneRecord(userright.getPhonenum(), hospitalcode, equipmentname, unit, "0");
                    list1.add(sendrecord);
                }
            }
            if (CollectionUtils.isNotEmpty(list1)) {
                sendrecordDao.save(list1);
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
                //友盟推送
                sendMesService.sendYmMessage(model.getPkid());
            }
        }
    }

    public Sendrecord producePhoneRecord(String phone, String hospitalcode, String equipmentname, String
            unit, String type) {
        Sendrecord sendrecord = new Sendrecord();
        sendrecord.setPhonenum(phone);
        sendrecord.setCreatetime(new Date());
        sendrecord.setHospitalcode(hospitalcode);
        sendrecord.setSendtype(type);
        sendrecord.setEquipmentname(equipmentname);
        sendrecord.setUnit(unit);
        sendrecord.setPkid(UUID.randomUUID().toString().replaceAll("-", ""));
        return sendrecord;
    }


//    /**
//     * 浙妇保医院定制需求
//     */
//    public Boolean warningRuleSend(WarningModel model) {
//        String hospitalcode = model.getHospitalcode();
//        String equipmentname = model.getEquipmentname();
//        String unit = UnitCase.caseUint(model.getUnit());
//        String value = model.getValue();
//        String pkid = model.getPkid();
//        if (!StringUtils.equalsAny(hospitalcode, "5e6d5037c93a4d619368553b09f5a657")) {
//            return true;
//        }
//        BoundHashOperations<Object, Object, Object> hospitalphonenum = redisTemplateUtil.boundHashOps("hospital:phonenum");
//        String o = (String) hospitalphonenum.get(hospitalcode);
//        if (StringUtils.isEmpty(o)) {
//            LOGGER.info("查询不到当前医院用户信息,医院编号：" + hospitalcode);
//            return false;
//        }
//        List<Userright> userrights = JsonUtil.toList(o, Userright.class);
//        if (CollectionUtils.isEmpty(userrights)) {
//            LOGGER.info("查询不到当前医院用户信息,医院编号：" + hospitalcode);
//            return false;
//        }
//        //给所有人发短信
//        List<Sendrecord> list1 = new ArrayList<>();
//        for (Userright userright : userrights) {
//            String phonenum = userright.getPhonenum();
//            // 发送短信
//            if (StringUtils.isNotEmpty(phonenum)) {
//                SendSmsResponse sendSmsResponse = sendMesService.sendMes(phonenum, equipmentname, unit, value);
//                LOGGER.info("发送短信对象:" + JsonUtil.toJson(userright) + sendSmsResponse.getCode());
//                Sendrecord sendrecord1 = producePhoneRecord(phonenum, hospitalcode, equipmentname, unit, "0");
//                list1.add(sendrecord1);
//            }
//        }
//        Map<Integer, Userright> userrightMap = new HashMap<>();
//        userrights.forEach(s -> {
//            String username = s.getUsername();
//            if (StringUtils.isNotEmpty(username)) {
//                String lastString = username.substring(username.length() - 1, username.length());
//                if (StringUtils.equalsAny(lastString, "1","2","3","4","5","6","7","8","9")) {
//                    userrightMap.put(Integer.parseInt(lastString), s);
//                }
//
//            }
//        });
//        if (userrightMap.isEmpty()) {
//            //若无规则联系人则给所有人打电话
//            for (Userright userright : userrights) {
//                String phonenum = userright.getPhonenum();
//                if (StringUtils.isNotEmpty(phonenum)) {
//                    LOGGER.info("拨打电话发送短信对象：" + JsonUtil.toJson(userright));
//                    sendMesService.callPhone(userright.getPhonenum(), equipmentname);
//                    Sendrecord sendrecord = producePhoneRecord(userright.getPhonenum(), hospitalcode, equipmentname, unit, "1");
//                    list1.add(sendrecord);
//                }
//            }
//
//        }else {
//            Set<Integer> integers = userrightMap.keySet();
//            Integer integer = integers.stream().sorted(Integer::compareTo).findFirst().get();
//            Userright userright = userrightMap.get(integer);
//            LOGGER.info("拨打电话发送短信对象：" + JsonUtil.toJson(userright));
//            sendMesService.callPhone(userright.getPhonenum(), equipmentname);
//            Sendrecord sendrecord = producePhoneRecord(userright.getPhonenum(), hospitalcode, equipmentname, unit, "1");
//            list1.add(sendrecord);
//            //生成定时报警逻辑
//            WarningrecordSort warningrecordSort = new WarningrecordSort();
//            warningrecordSort.setPkid(pkid);
//            warningrecordSort.setIsRead("0");
//            warningrecordSort.setEquipmentname(equipmentname);
//            warningrecordSort.setWarningStatus("1");
//            warningrecordSort.setInputdatetime(new Date());
//            warningrecordSort.setHospitalcode("5e6d5037c93a4d619368553b09f5a657");
//            warningrecordSortDao.save(warningrecordSort);
//        }
//        warningrecordDao.updatePhone(pkid);
//        if (CollectionUtils.isNotEmpty(list1)) {
//            sendrecordDao.save(list1);
//        }
//        return false;
//    }


    public static void main(String[] args){
        String username ="xiaoliu3";
        String lastString = username.substring(username.length() - 1, username.length());
        if (StringUtils.equalsAny(lastString, "1","2","3","4","5","6","7","8","9")) {
            System.out.println("sada");

        }

    }
}
