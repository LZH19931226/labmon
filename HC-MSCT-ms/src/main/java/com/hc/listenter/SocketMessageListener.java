package com.hc.listenter;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.hc.dao.*;
import com.hc.entity.*;
import com.hc.bean.WarningModel;
import com.hc.bean.WarningMqModel;
import com.hc.config.RedisTemplateUtil;
import com.hc.exchange.BaoJinMsg;
import com.hc.model.ResponseModel.HospitalEquipmentTypeInfoModel;
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
    private MonitorequipmentDao monitorequipmentDao;

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

                /**
                 * <option value="0">电话+短信</option>
                 * <option value="1">电话</option>
                 * <option value="2">短信</option>
                 * <option value="3">不报警</option>
                 */
                String timeoutwarning = userright.getTimeoutwarning();//超时报警方式
                String unit = "超时";
                switch (disabletype) {
                    case "2":
                        // 超时报警
//                        sendMesService.sendMes1(phonenum, equipmentname, "超时", hospitalname, timeouttime);
                        if (StringUtils.equals(timeoutwarning, "0")) {
                            LOGGER.info("拨打电话发送短信对象：" + JsonUtil.toJson(userright));
                            sendMesService.callPhone(userright.getPhonenum(), equipmentname);
                            SendSmsResponse sendSmsResponse = sendMesService.sendMes1(phonenum, equipmentname, "超时", hospitalname, timeouttime);
                            LOGGER.info("发送短信对象:" + JsonUtil.toJson(userright) + sendSmsResponse.getCode());
                        } else if (StringUtils.equals(timeoutwarning, "1")) {
                            LOGGER.info("拨打电话发送短信对象：" + JsonUtil.toJson(userright));
                            sendMesService.callPhone(userright.getPhonenum(), equipmentname);
                        } else if (StringUtils.equals(timeoutwarning, "2")) {
                            SendSmsResponse sendSmsResponse = sendMesService.sendMes1(phonenum, equipmentname, "超时", hospitalname, timeouttime);
                            LOGGER.info("发送短信对象:" + JsonUtil.toJson(userright) + sendSmsResponse.getCode());
                        }
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
            LOGGER.info("解析数据为null,不发送报警!");
            return;
        }
        String equipmentname = model.getEquipmentname();
        String unit = UnitCase.caseUint(model.getUnit());
        String value = UnitCase.caseUint(model.getValue());
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
            //当前时间在报警的时间段内,开启警报信息
            boolean warningTimeBlockRule = warningTimeBlockRule(monitorinstrument);
            if(!warningTimeBlockRule){
                //当前时间不在报警时间段内, 不用报警.直接返回
                return;
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
                String lastdata = (String) objectObjectObjectHashOperations.get("LASTDATA"+hospitalcode, monitorinstrument.getEquipmentno());
                if (StringUtils.isNotEmpty(lastdata)) {
                    //报警电话信息处理
                    Monitorequipmentlastdata monitorequipmentlastdata1 = JsonUtil.toBean(lastdata, Monitorequipmentlastdata.class);
                    monitorequipmentlastdata1.setPkid(UUID.randomUUID().toString().replaceAll("-", ""));
                    monitorequipmentlastdata1.setInputdatetime(new Date());
                    monitorequipmentlastdata1.setEquipmentlastdata("1");
                    monitorequipmentlastdataDao.saveAndFlush(monitorequipmentlastdata1);
                    objectObjectObjectHashOperations.put("LASTDATA"+hospitalcode, monitorinstrument.getEquipmentno(), JsonUtil.toJson(monitorequipmentlastdata1));
                }
                //友盟推送
                sendMesService.sendYmMessage(model.getPkid());
            }
        }
    }

    /**
     * 报警时间段报警逻辑
     *  1.去设备找是否全天报警.
     *      Y : 根据设备和联系人方式直接发送警报
     *      N : 查询时间段.判断当前时间是否在报警的区间内
     *          Y : 报警
     *          N : 不报警,设备没有配置时间段再根据设备类型找是否要报警
     *              2.设备类型是否全天报警
     *                  Y : 根据设备类型和联系人方式直接发送警报
     *                  N : 查询时间段.判断当前时间是否在报警的区间内
     */
    private boolean warningTimeBlockRule(Monitorinstrument monitorinstrument){
        Object hospitalSN = redisTemplateUtil.boundHashOps("hospital:sn").get(monitorinstrument.getSn());
        if(hospitalSN != null){
            Monitorinstrument monitorinstrumentObj = JsonUtil.toBean((String) hospitalSN, Monitorinstrument.class);
            String eqipmentAlwayalarm = monitorinstrumentObj.getAlwayalarm();
            //全天报警
            if("1".equals(eqipmentAlwayalarm)){
                return true;
            }else{
                //当前设备有配置时段,但是当前时间不在时段内.不报警
                if(!currentTimeInWarningBlock(monitorinstrumentObj)){
                  return false;
                  //没有配置报警时间区间
                } else if(monitorinstrumentObj.getWarningTimeList() == null
                        || monitorinstrumentObj.getWarningTimeList().isEmpty()){
                    //没有配置时间段,在设备类型查找
                    String equipmenttypeid = monitorinstrument.getEquipmenttypeid();
                    if(StringUtils.isEmpty(equipmenttypeid)){
                        String queryEquipmenttypeid = monitorequipmentDao.getByEquipmentno(monitorinstrument.getEquipmentno());
                        if(StringUtils.isNotEmpty(queryEquipmenttypeid)) equipmenttypeid = queryEquipmenttypeid;
                    }
                    Object equipmentTypeObject = redisTemplateUtil.boundHashOps("hospital:equipmenttype").get(
                            equipmenttypeid +
                                    "@"+monitorinstrument.getHospitalcode());
                    HospitalEquipmentTypeInfoModel equipmentTypeInfoModel =
                            JsonUtil.toBean((String)equipmentTypeObject, HospitalEquipmentTypeInfoModel.class);
                    String alwayalarm = equipmentTypeInfoModel.getAlwayalarm();
                    //设备类型全天报警,直接发送警报
                    if("1".equals(alwayalarm)){
                        return true;
                    }else{
                        //设备类型非全天报警
                        List<MonitorEquipmentWarningTime> warningTimeList = equipmentTypeInfoModel.getWarningTimeList();
                        if(warningTimeList != null && !warningTimeList.isEmpty()){
                            return currentTimeInWarningBlock(equipmentTypeInfoModel);
                        }
                    }
                }else{
                    //根据时间段数据报警
                    return currentTimeInWarningBlock(monitorinstrumentObj) ;
                }
            }
        }
        return false;
    }

    /**
     * 当前时间是否存在配置的时间段中
     * @param monitorinstrumentObj
     * @return
     */
    private boolean currentTimeInWarningBlock(Object monitorinstrumentObj){
        Monitorinstrument ruleObj = new Monitorinstrument();
        List<MonitorEquipmentWarningTime> warningEquipmentTimeList = new ArrayList<MonitorEquipmentWarningTime>();
        if(monitorinstrumentObj instanceof Monitorinstrument){
            ruleObj = (Monitorinstrument)monitorinstrumentObj;
            warningEquipmentTimeList = ruleObj.getWarningTimeList();
        }

        if(monitorinstrumentObj instanceof HospitalEquipmentTypeInfoModel){
            HospitalEquipmentTypeInfoModel equipmentRuleObj = (HospitalEquipmentTypeInfoModel)monitorinstrumentObj;
            warningEquipmentTimeList = equipmentRuleObj.getWarningTimeList();
        }
        Date nowDate = new Date();
        List<Date> nowTime = sameDate(nowDate);
        //设备类型配置时间区间,在设备类型里面拿时间进行比较
        if(warningEquipmentTimeList != null && !warningEquipmentTimeList.isEmpty()){
            int successDate = 0;
            for(int i = 0;i<warningEquipmentTimeList.size();i++){
                MonitorEquipmentWarningTime item = warningEquipmentTimeList.get(i);
                if(item != null){
                    Date begintime = item.getBegintime();
                    Date endtime = item.getEndtime();
                    List<Date> dateInterval = sameDate(begintime, endtime);
                    if(isEffectiveDate(nowTime.get(0),dateInterval.get(0),dateInterval.get(1))){
                        successDate += 1;
                    }
                }
            }
            if(successDate > 0){
                //说明当前时间在时间区间内,可以发送短信或者拨电话
                return true;
            }
        }
        return false;
    }

    /**
     * 修改时间的年月日
     */
    private List<Date> sameDate(Date... dates){
        if(dates != null){
            Calendar nowCalendar = Calendar.getInstance();
            List<Date> dateList = new ArrayList<Date>();
            for(int i=0;i<dates.length;i++){
                Date date = dates[i];
                if(date == null) continue;
                nowCalendar.setTime(date);
                nowCalendar.set(Calendar.YEAR,1970);
                nowCalendar.set(Calendar.MONTH,12);
                nowCalendar.set(Calendar.DAY_OF_MONTH,12);
                Date nowTime = nowCalendar.getTime();
                dateList.add(i,nowTime);
            }
            return dateList;
        }
        return null;
    }

    /**
     * 当前时间是否在此时间区间内
     * @param nowTime
     * @param startTime
     * @param endTime
     * @return
     */
    public boolean isEffectiveDate(Date nowTime, Date startTime, Date endTime) {
        if (nowTime.getTime() == startTime.getTime()
                || nowTime.getTime() == endTime.getTime()){
            return true;
        }

        Calendar date = Calendar.getInstance();
        date.setTime(nowTime);

        Calendar begin = Calendar.getInstance();
        begin.setTime(startTime);

        Calendar end = Calendar.getInstance();
        end.setTime(endTime);

        if (date.after(begin) && date.before(end)) {
            return true;
        } else {
            return false;
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
