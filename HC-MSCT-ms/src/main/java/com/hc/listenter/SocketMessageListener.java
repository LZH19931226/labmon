package com.hc.listenter;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.hc.clickhouse.repository.WarningrecordRepository;
import com.hc.command.labmanagement.model.UserSchedulingModel;
import com.hc.device.SnDeviceRedisApi;
import com.hc.exchange.BaoJinMsg;
import com.hc.hospital.HospitalEquipmentTypeIdApi;
import com.hc.hospital.HospitalInfoApi;
import com.hc.hospital.HospitalRedisApi;
import com.hc.mapper.SendrecordDao;
import com.hc.mapper.UserrightDao;
import com.hc.model.HospitalEquipmentTypeInfoModel;
import com.hc.model.TimeoutEquipment;
import com.hc.model.WarningModel;
import com.hc.model.WarningMqModel;
import com.hc.my.common.core.constant.enums.DictEnum;
import com.hc.my.common.core.constant.enums.SysConstants;
import com.hc.my.common.core.esm.EquipmentState;
import com.hc.my.common.core.redis.dto.*;
import com.hc.my.common.core.util.BeanConverter;
import com.hc.my.common.core.util.SoundLightUtils;
import com.hc.po.*;
import com.hc.service.MessageSendService;
import com.hc.service.SendMesService;
import com.hc.service.WarningService;
import com.hc.tcp.SoundLightApi;
import com.hc.user.UserRightInfoApi;
import com.hc.utils.JsonUtil;
import com.hc.utils.UnitCase;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.*;
import java.util.stream.Collectors;

@Component
@EnableBinding(BaoJinMsg.class)
@Slf4j
public class SocketMessageListener {

    @Autowired
    private SendMesService sendMesService;
    @Autowired
    private WarningService warningService;
    @Autowired
    private SendrecordDao sendrecordDao;
    @Autowired
    private WarningrecordRepository warningrecordRepository;
    @Autowired
    private UserrightDao userrightDao;
    @Autowired
    private UserRightInfoApi userRightInfoApi;
    @Autowired
    private SnDeviceRedisApi snDeviceRedisSync;
    @Autowired
    private HospitalEquipmentTypeIdApi hospitalEquipmentTypeIdApi;
    @Autowired
    private SoundLightApi soundLightApi;
    @Autowired
    private HospitalRedisApi hospitalRedisApi;
    @Autowired
    private HospitalInfoApi hospitalInfoApi;
    @Autowired
    private MessageSendService messageSendService;
    /**
     * 监听报警信息
     */
    @StreamListener(BaoJinMsg.EXCHANGE_NAME)
    public void onMessage1(String messageContent) {
        log.info("从通道" + BaoJinMsg.EXCHANGE_NAME + ":" + messageContent);
        msctMessage(messageContent);

    }


    /**
     * 监听报警信息
     */
    @StreamListener(BaoJinMsg.EXCHANGE_NAME1)
    public void onMessage2(String messageContent) {
        log.info("从通道" + BaoJinMsg.EXCHANGE_NAME1 + ":" + JsonUtil.toJson(messageContent));
        msctMessage(messageContent);

    }


    /**
     * 监听报警信息
     */
    @StreamListener(BaoJinMsg.EXCHANGE_NAME2)
    public void onMessage3(String messageContent) {
        log.info("从通道" + BaoJinMsg.EXCHANGE_NAME2 + ":" + messageContent);
        msctMessage(messageContent);
    }

    /**
     * 监听超时报警信息
     *
     * @param message
     */
    @StreamListener(BaoJinMsg.EXCHANGE_NAME4)
    public void onMessage5(String message) {
            JSONArray objects = JSONUtil.parseArray(message);
            List<TimeoutEquipment> timeoutEquipmentList = JSONUtil.toList(objects, TimeoutEquipment.class);
            if (CollectionUtils.isEmpty(timeoutEquipmentList)) {
                return;
            }
            String eqTypeName = "";
            String count = "";
            String hospitalName = timeoutEquipmentList.get(0).getHospitalname();
             String hospitalcode = timeoutEquipmentList.get(0).getHospitalcode();
             int size = timeoutEquipmentList.size();
            for (int i = 0; i < size; i++) {
                String equipmentTypeName = timeoutEquipmentList.get(i).getEquipmenttypename();
                String count1 = timeoutEquipmentList.get(i).getCount();
                if(StringUtils.isBlank(equipmentTypeName) || StringUtils.isBlank(count1)){
                    return;
                }
                if(i == size-1){
                    eqTypeName += equipmentTypeName;
                    count += count1;
                }else {
                    eqTypeName+=equipmentTypeName+"/";
                    count+=count1+"/";
                }
            }
            if(StringUtils.isBlank(hospitalName) || StringUtils.isBlank(eqTypeName) || StringUtils.isBlank(count)){
                return;
            }
            log.info("进入超时报警队列：" + message);
            // 根据hospitalcode查找设置超时报警的联系人
            List<Userright> userrightByHospitalcodeAAndTimeout = userrightDao.getUserrightByHospitalcodeAAndTimeout(hospitalcode);
            if (org.apache.commons.collections4.CollectionUtils.isEmpty(userrightByHospitalcodeAAndTimeout)) {
                return;
            }
            log.info("进入超时报警队列联系人：" + JsonUtil.toJson(userrightByHospitalcodeAAndTimeout));
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
                // 超时报警
                if (StringUtils.isBlank(timeoutwarning) || StringUtils.equals(timeoutwarning, "0")){
                    log.info("拨打电话发送短信对象:{}",JsonUtil.toJson(userright));
                    sendMesService.callPhone2(userright.getPhonenum(),hospitalName, eqTypeName);
                    SendSmsResponse sendSmsResponse = sendMesService.sendMes1(phonenum, eqTypeName, "超时", hospitalName, count);
                    log.info("发送短信对象:{}",JsonUtil.toJson(userright) + sendSmsResponse.getCode());
                } else if (StringUtils.equals(timeoutwarning, "1")) {
                    log.info("拨打电话发送短信对象:{}",JsonUtil.toJson(userright));
                    sendMesService.callPhone2(userright.getPhonenum(),hospitalName, eqTypeName);
                } else if (StringUtils.equals(timeoutwarning, "2")) {
                    SendSmsResponse sendSmsResponse = sendMesService.sendMes1(phonenum, eqTypeName, "超时", hospitalName, count);
                    log.info("发送短信对象:{}",JsonUtil.toJson(userright) + sendSmsResponse.getCode());
                }
            }
    }

    public void msctMessage(String message) {
        WarningMqModel mQmodel = JsonUtil.toBean(message, WarningMqModel.class);
        assert mQmodel != null;
        Monitorinstrument monitorinstrument = mQmodel.getMonitorinstrument();
        WarningModel model = warningService.produceWarn(mQmodel, mQmodel.getMonitorinstrument(), mQmodel.getDate(), mQmodel.getInstrumentconfigid(), mQmodel.getUnit());
        if (ObjectUtils.isEmpty(model)) {
            return;
        }
        String equipmentname = model.getEquipmentname();
        String unit = UnitCase.caseUint(model.getUnit());
        String value = UnitCase.caseUint(model.getValue());
        String hospitalcode = model.getHospitalcode();
        HospitalInfoDto hospitalInfoDto = hospitalRedisApi.findHospitalRedisInfo(hospitalcode).getResult();
        if (null == hospitalInfoDto){
            log.info("医院信息不存在:{}",hospitalcode);
            return;
        }
        String hospitalName = hospitalInfoDto.getHospitalName();
        //当前时间在报警的时间段内,开启警报信息
        boolean warningTimeBlockRule = warningTimeBlockRule(monitorinstrument);
        if (!warningTimeBlockRule) {
            //当前时间不在报警时间段内, 不用报警.直接返回
            log.info("当前时间不在报警时间段内,医院编号:{},数据模型:{}",hospitalcode,JsonUtil.toJson(monitorinstrument));
            return;
        }
        //判断该医院当天是否有人员排班,给判断和未排班的人员集合赋值
        List<UserRightRedisDto> list =addUserScheduLing(hospitalcode);
        if (CollectionUtils.isEmpty(list)){
            return;
        }
        //获取电话
        List<Sendrecord> list1 = new ArrayList<>();
        for (UserRightRedisDto userright : list) {
            String reminders = userright.getReminders();
            String phonenum = userright.getPhoneNum();
            String role = userright.getRole();
            String equipmentName = equipmentname;
            //1为运维后台人员
            if (StringUtils.isNotEmpty(role)&&StringUtils.equals(role,"1")){
                equipmentName = hospitalName + equipmentname;
            }
            //不报警
            if (StringUtils.equals(reminders,DictEnum.UNOPENED_CONTACT_DETAILS.getCode()) || StringUtils.isEmpty(phonenum)) {
                continue;
            }
            if (StringUtils.isEmpty(reminders) || StringUtils.equals(DictEnum.PHONE_SMS.getCode(),reminders)) {
                log.info("拨打电话对象:{}",JsonUtil.toJson(userright));
                sendMesService.callPhone(userright.getPhoneNum(), equipmentName);
                Sendrecord sendrecord = producePhoneRecord(userright.getPhoneNum(), hospitalcode, equipmentName, unit, "1");
                list1.add(sendrecord);
                SendSmsResponse sendSmsResponse = sendMesService.sendMes(userright.getPhoneNum(), equipmentName, unit, value);
                log.info("发送短信对象:{}",JsonUtil.toJson(userright) + sendSmsResponse.getCode());
                Sendrecord sendrecord1 = producePhoneRecord(userright.getPhoneNum(), hospitalcode, equipmentName, unit, "0");
                list1.add(sendrecord1);
            } else if (StringUtils.equals(reminders, DictEnum.PHONE.getCode())) {
                log.info("拨打电话对象:{}",JsonUtil.toJson(userright));
                sendMesService.callPhone(userright.getPhoneNum(), equipmentName);
                Sendrecord sendrecord = producePhoneRecord(userright.getPhoneNum(), hospitalcode, equipmentName, unit, "1");
                list1.add(sendrecord);
            } else if (StringUtils.equals(reminders,DictEnum.SMS.getCode())) {
                SendSmsResponse sendSmsResponse = sendMesService.sendMes(userright.getPhoneNum(), equipmentName, unit, value);
                log.info("发送短信对象{}",JsonUtil.toJson(userright) + sendSmsResponse.getCode());
                Sendrecord sendrecord = producePhoneRecord(userright.getPhoneNum(), hospitalcode, equipmentName, unit, "0");
                list1.add(sendrecord);
            }
        }
        if (CollectionUtils.isNotEmpty(list1)) {
            list1.forEach(s -> {
                sendrecordDao.insert(s);
            });
        }
        //报警通知完毕之后,修改此条报警数据状态为已经推送
        String pkid = model.getPkid();
        warningrecordRepository.updateIsPhoneInfo(pkid,"1");
        //如果该医院开启了声光报警则需要推送声光报警指令
        if(StringUtils.isBlank(hospitalInfoDto.getSoundLightAlarm()) || !StringUtils.equals(hospitalInfoDto.getSoundLightAlarm(), DictEnum.TURN_ON.getCode())){
            String sn = monitorinstrument.getSn();
            soundLightApi.sendMsg(sn,SoundLightUtils.TURN_ON_ROUND_LIGHT_COMMAND);
        }
        //将设备状态信息推送到mq
        EquipmentState equipmentState = new EquipmentState();
        equipmentState.setState(SysConstants.IN_ALARM);
        equipmentState.setEquipmentNo(monitorinstrument.getEquipmentno());
        equipmentState.setInstrumentNo(monitorinstrument.getInstrumentno());
        equipmentState.setInstrumentConfigNo(model.getInstrumentparamconfigNO());
        String json = JsonUtil.toJson(equipmentState);
        messageSendService.send(json);
        log.info("推送报警设备状态{}",JsonUtil.toJson(json));
    }

    /**
     *  根据医院id获取用户集合
     *   当有排班信息时，
     * @param hospitalcode
     * @return
     */
    private List<UserRightRedisDto>  addUserScheduLing(String hospitalcode){
        List<UserRightRedisDto> list = new ArrayList<>();
        List<String> phones = new ArrayList<>();
        Date date = new Date();
        List<UserSchedulingModel> userSchedulingModels = hospitalInfoApi.getHospitalScheduleInfo(hospitalcode).getResult();
        List<UserScheduLing> userScByHosSt1 = BeanConverter.convert(userSchedulingModels,UserScheduLing.class);
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
                    UserRightRedisDto userright = new UserRightRedisDto();
                    //排班的人默认都是电话+短信
                    userright.setReminders(null);
                    String userphone = s.getUserphone();
                    if (StringUtils.isNotEmpty(userphone)) {
                        userright.setPhoneNum(userphone);
                        phones.add(userphone);
                    }
                    list.add(userright);
                }
            }
        }
        List<UserRightRedisDto> users = userRightInfoApi.findALLUserRightInfoByHC(hospitalcode).getResult();
        if (CollectionUtils.isEmpty(users)) {
            log.info("查询不到当前医院用户信息,医院编号:{}" , hospitalcode);
            return null;
        }
        //未排班的人
        if (CollectionUtils.isEmpty(list)) {
            list.addAll(users);
        } else {
            //有排班的人和未排班的人
            if (CollectionUtils.isNotEmpty(phones)) {
                List<UserRightRedisDto> userRights = users.stream().filter(s -> !phones.contains(s.getPhoneNum())).collect(Collectors.toList());
                list.addAll(userRights);
            }
        }
        return list;
    }



    /**
     * 报警时间段报警逻辑
     * 1.去设备找是否全天报警.
     * Y : 根据设备和联系人方式直接发送警报
     * N : 查询时间段.判断当前时间是否在报警的区间内
     * Y : 报警
     * N : 不报警,设备没有配置时间段再根据设备类型找是否要报警
     * 2.设备类型是否全天报警
     * Y : 根据设备类型和联系人方式直接发送警报
     * N : 查询时间段.判断当前时间是否在报警的区间内
     */
    private boolean warningTimeBlockRule(Monitorinstrument monitorinstrument) {
        String sn = monitorinstrument.getSn();
        String hospitalcode = monitorinstrument.getHospitalcode();
        SnDeviceDto snDeviceDto = snDeviceRedisSync.getSnDeviceDto(sn).getResult();
        if (null!=snDeviceDto) {
            Monitorinstrument monitorinstrumentObj = objectConversion(snDeviceDto);
            assert monitorinstrumentObj != null;
            String eqipmentAlwayalarm = monitorinstrumentObj.getAlwayalarm();
            //全天报警
            if (StringUtils.isEmpty(eqipmentAlwayalarm) || DictEnum.TURN_ON.getCode().equals(eqipmentAlwayalarm)) {
                return true;
            } else {
                //当前设备有配置时段,但是当前时间不在时段内.不报警
                if (CollectionUtils.isNotEmpty(monitorinstrumentObj.getWarningTimeList())) {
                    return currentTimeInWarningBlock(monitorinstrumentObj);
                    //没有配置报警时间区间
                } else if (monitorinstrumentObj.getWarningTimeList() == null
                        || monitorinstrumentObj.getWarningTimeList().isEmpty()) {
                    //没有配置时间段,在设备类型查找
                    String equipmenttypeid = monitorinstrument.getEquipmenttypeid();
                    if (StringUtils.isEmpty(equipmenttypeid)) {
                        String queryEquipmenttypeid = snDeviceDto.getEquipmentTypeId();
                        if (StringUtils.isNotEmpty(queryEquipmenttypeid)) {
                            equipmenttypeid = queryEquipmenttypeid;
                        }
                    }
                    HospitalEquipmentTypeInfoDto hosEqType = hospitalEquipmentTypeIdApi.findHospitalEquipmentTypeRedisInfo(hospitalcode, equipmenttypeid).getResult();
                    HospitalEquipmentTypeInfoModel equipmentTypeInfoModel = BeanConverter.convert(hosEqType, HospitalEquipmentTypeInfoModel.class);
                    String alwayalarm = equipmentTypeInfoModel.getAlwayalarm();
                    //设备类型全天报警,直接发送警报
                    if (DictEnum.TURN_ON.getCode().equals(alwayalarm)) {
                        return true;
                    } else {
                        //设备类型非全天报警
                        List<MonitorEquipmentWarningTime> warningTimeList = equipmentTypeInfoModel.getWarningTimeList();
                        if (warningTimeList != null && !warningTimeList.isEmpty()) {
                            return currentTimeInWarningBlock(equipmentTypeInfoModel);
                        }
                    }
                } else {
                    //根据时间段数据报警
                    return currentTimeInWarningBlock(monitorinstrumentObj);
                }
            }
        }
        return false;
    }

    /**
     * 当前时间是否存在配置的时间段中
     *
     * @param monitorinstrumentObj
     * @return
     */
    private boolean currentTimeInWarningBlock(Object monitorinstrumentObj) {
        Monitorinstrument ruleObj = new Monitorinstrument();
        List<MonitorEquipmentWarningTime> warningEquipmentTimeList = new ArrayList<MonitorEquipmentWarningTime>();
        if (monitorinstrumentObj instanceof Monitorinstrument) {
            ruleObj = (Monitorinstrument) monitorinstrumentObj;
            warningEquipmentTimeList = ruleObj.getWarningTimeList();
        }

        if (monitorinstrumentObj instanceof HospitalEquipmentTypeInfoModel) {
            HospitalEquipmentTypeInfoModel equipmentRuleObj = (HospitalEquipmentTypeInfoModel) monitorinstrumentObj;
            warningEquipmentTimeList = equipmentRuleObj.getWarningTimeList();
        }
        Date nowDate = new Date();
        List<Date> nowTime = sameDate(nowDate);
        //设备类型配置时间区间,在设备类型里面拿时间进行比较
        if (warningEquipmentTimeList != null && !warningEquipmentTimeList.isEmpty()) {
            int successDate = 0;
            for (int i = 0; i < warningEquipmentTimeList.size(); i++) {
                MonitorEquipmentWarningTime item = warningEquipmentTimeList.get(i);
                if (item != null) {
                    Date begintime = item.getBegintime();
                    Date endtime = item.getEndtime();
                    List<Date> dateInterval = sameDate(begintime, endtime);
                    if (isEffectiveDate(nowTime.get(0), dateInterval.get(0), dateInterval.get(1))) {
                        successDate += 1;
                    }
                }
            }
            if (successDate == warningEquipmentTimeList.size()) {
                //说明当前时间在时间区间内,可以发送短信或者拨电话
                return true;
            }
        }
        return false;
    }

    /**
     * 修改时间的年月日
     */
    private List<Date> sameDate(Date... dates) {
        if (dates != null) {
            Calendar nowCalendar = Calendar.getInstance();
            List<Date> dateList = new ArrayList<Date>();
            for (int i = 0; i < dates.length; i++) {
                Date date = dates[i];
                if (date == null) {
                    continue;
                }
                nowCalendar.setTime(date);
                nowCalendar.set(Calendar.YEAR, 1970);
                nowCalendar.set(Calendar.MONTH, 12);
                nowCalendar.set(Calendar.DAY_OF_MONTH, 12);
                Date nowTime = nowCalendar.getTime();
                dateList.add(i, nowTime);
            }
            return dateList;
        }
        return null;
    }

    /**
     * 当前时间是否在此时间区间内
     *
     * @param nowTime
     * @param startTime
     * @param endTime
     * @return
     */
    public boolean isEffectiveDate(Date nowTime, Date startTime, Date endTime) {
        if (nowTime.getTime() == startTime.getTime()
                || nowTime.getTime() == endTime.getTime()) {
            return true;
        }

        Calendar date = Calendar.getInstance();
        date.setTime(nowTime);

        Calendar begin = Calendar.getInstance();
        begin.setTime(startTime);

        Calendar end = Calendar.getInstance();
        end.setTime(endTime);

        if (date.after(begin) && date.before(end)) {
            return false;
        } else {
            return true;
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

    private Monitorinstrument objectConversion(SnDeviceDto snDeviceDto) {
        if(ObjectUtils.isEmpty(snDeviceDto)){
            return null;
        }
        String instrumentTypeId = snDeviceDto.getInstrumentTypeId();
        Monitorinstrument monitorinstrument = new Monitorinstrument();
        monitorinstrument.setAlwayalarm(snDeviceDto.getAlwaysAlarm());
        monitorinstrument.setChannel(snDeviceDto.getChannel());
        monitorinstrument.setEquipmentno(snDeviceDto.getEquipmentNo());
        monitorinstrument.setHospitalcode(snDeviceDto.getHospitalCode());
        monitorinstrument.setInstrumentno(snDeviceDto.getInstrumentNo());
        monitorinstrument.setInstrumenttypeid(StringUtils.isEmpty(instrumentTypeId)?null:Integer.valueOf(instrumentTypeId));
        monitorinstrument.setSn(snDeviceDto.getSn());
        monitorinstrument.setInstrumentname(snDeviceDto.getInstrumentName());
        List<MonitorEquipmentWarningTimeDto> warningTimeList = snDeviceDto.getWarningTimeList();
        if (CollectionUtils.isNotEmpty(warningTimeList)){
            monitorinstrument.setWarningTimeList(BeanConverter.convert(warningTimeList,MonitorEquipmentWarningTime.class));
        }
        return monitorinstrument;
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


    public static void main(String[] args) {
        String username = "xiaoliu3";
        String lastString = username.substring(username.length() - 1, username.length());
        if (StringUtils.equalsAny(lastString, "1", "2", "3", "4", "5", "6", "7", "8", "9")) {
            System.out.println("sada");

        }

    }
}
