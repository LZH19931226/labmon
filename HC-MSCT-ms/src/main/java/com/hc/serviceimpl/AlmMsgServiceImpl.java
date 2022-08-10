package com.hc.serviceimpl;

import com.hc.clickhouse.po.Warningrecord;
import com.hc.command.labmanagement.model.UserSchedulingModel;
import com.hc.device.SnDeviceRedisApi;
import com.hc.hospital.HospitalEquipmentTypeIdApi;
import com.hc.hospital.HospitalInfoApi;
import com.hc.model.HospitalEquipmentTypeInfoModel;
import com.hc.my.common.core.constant.enums.DictEnum;
import com.hc.my.common.core.domain.MonitorinstrumentDo;
import com.hc.my.common.core.redis.dto.HospitalEquipmentTypeInfoDto;
import com.hc.my.common.core.redis.dto.MonitorEquipmentWarningTimeDto;
import com.hc.my.common.core.redis.dto.SnDeviceDto;
import com.hc.my.common.core.redis.dto.UserRightRedisDto;
import com.hc.my.common.core.util.BeanConverter;
import com.hc.my.common.core.util.DateUtils;
import com.hc.po.MonitorEquipmentWarningTime;
import com.hc.po.Monitorinstrument;
import com.hc.po.UserScheduLing;
import com.hc.service.AlmMsgService;
import com.hc.user.UserRightInfoApi;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AlmMsgServiceImpl implements AlmMsgService {

    @Autowired
    private UserRightInfoApi userRightInfoApi;

    @Autowired
    private HospitalInfoApi hospitalInfoApi;

    @Autowired
    private SnDeviceRedisApi snDeviceRedisSync;
    @Autowired
    private HospitalEquipmentTypeIdApi hospitalEquipmentTypeIdApi;


    @Override
    public List<UserRightRedisDto> addUserScheduLing(String hospitalcode) {
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
    @Override
    public boolean warningTimeBlockRule(MonitorinstrumentDo monitorinstrument, Warningrecord warningrecord) {
        String sn = monitorinstrument.getSn();
        String hospitalcode = monitorinstrument.getHospitalcode();
        SnDeviceDto snDeviceDto = snDeviceRedisSync.getSnDeviceDto(sn).getResult();
        if (null!=snDeviceDto) {
            Monitorinstrument monitorinstrumentObj = objectConversion(snDeviceDto);
            String eqipmentAlwayalarm = monitorinstrumentObj.getAlwayalarm();
            //全天报警
            if (StringUtils.isEmpty(eqipmentAlwayalarm) || DictEnum.TURN_ON.getCode().equals(eqipmentAlwayalarm)) {
                warningrecord.setAlwayalarm(DictEnum.TURN_ON.getCode());
                return true;
            } else {
                //当前设备有配置时段,但是当前时间不在时段内.不报警
                if (CollectionUtils.isNotEmpty(monitorinstrumentObj.getWarningTimeList())) {
                    return currentTimeInWarningBlockEQ(monitorinstrumentObj,warningrecord);
                } else{
                    //没有配置时间段,则读取设备类型得报警规则
                    String equipmenttypeid =  snDeviceDto.getEquipmentTypeId();
                    HospitalEquipmentTypeInfoDto hosEqType = hospitalEquipmentTypeIdApi.findHospitalEquipmentTypeRedisInfo(hospitalcode, equipmenttypeid).getResult();
                    HospitalEquipmentTypeInfoModel equipmentTypeInfoModel = BeanConverter.convert(hosEqType, HospitalEquipmentTypeInfoModel.class);
                    String alwayalarm = equipmentTypeInfoModel.getAlwayalarm();
                    //设备类型全天报警,直接发送警报
                    if (DictEnum.TURN_ON.getCode().equals(alwayalarm)) {
                        warningrecord.setAlwayalarm(DictEnum.TURN_ON.getCode());
                        return true;
                    } else {
                        //设备类型非全天报警又未设置时段,则不报警
                        List<MonitorEquipmentWarningTime> warningTimeList = equipmentTypeInfoModel.getWarningTimeList();
                        if (CollectionUtils.isEmpty(warningTimeList)){
                            warningrecord.setAlwayalarm(DictEnum.TURN_ON.getCode());
                            return false;
                        }else {
                            return currentTimeInWarningBlockEQTYPE(equipmentTypeInfoModel,warningrecord);
                        }
                    }
                }
            }
        }
        return false;
    }


    private boolean currentTimeInWarningBlockEQ(Monitorinstrument monitorinstrument,Warningrecord warningrecord){
        List<MonitorEquipmentWarningTime> warningEquipmentTimeList =monitorinstrument.getWarningTimeList();
        warningrecord.setAlwayalarm(DictEnum.OFF.getCode());
        warningrecord.setAlarmTime(buildHhSsTimeFormart(warningEquipmentTimeList));
        return currentTimeInWarningBlock(warningEquipmentTimeList);
    }

    private boolean currentTimeInWarningBlockEQTYPE(HospitalEquipmentTypeInfoModel hospitalEquipmentTypeInfoModel,Warningrecord warningrecord){
        List<MonitorEquipmentWarningTime> warningEquipmentTimeList = hospitalEquipmentTypeInfoModel.getWarningTimeList();
        warningrecord.setAlwayalarm(DictEnum.OFF.getCode());
        warningrecord.setAlarmTime(buildHhSsTimeFormart(warningEquipmentTimeList));
        return currentTimeInWarningBlock(warningEquipmentTimeList);
    }

    private boolean currentTimeInWarningBlock(List<MonitorEquipmentWarningTime> warningEquipmentTimeList) {
        Date nowDate = new Date();
        List<Date> nowTime = sameDate(nowDate);
        //设备类型配置时间区间,在设备类型里面拿时间进行比较
        if (warningEquipmentTimeList != null && !warningEquipmentTimeList.isEmpty()) {
            int successDate = 0;
            for (MonitorEquipmentWarningTime item : warningEquipmentTimeList) {
                if (item != null) {
                    Date begintime = item.getBegintime();
                    Date endtime = item.getEndtime();
                    List<Date> dateInterval = sameDate(begintime, endtime);
                    if (DateUtils.isEffectiveDate(nowTime.get(0), dateInterval.get(0), dateInterval.get(1))) {
                        successDate += 1;
                    }
                }
            }
            //说明当前时间在时间区间内,可以发送短信或者拨电话
            return successDate == warningEquipmentTimeList.size();
        }
        return false;
    }

    /**
     * 将日期转换为字符串,拼接对应时间格式
     */
    public String buildHhSsTimeFormart(List<MonitorEquipmentWarningTime> monitorEquipmentWarningTimes){
         if (CollectionUtils.isNotEmpty(monitorEquipmentWarningTimes)){
             StringBuilder timeBuffer = new StringBuilder();
             for (int i = 0; i < monitorEquipmentWarningTimes.size(); i++) {
                 MonitorEquipmentWarningTime monitorEquipmentWarningTime = monitorEquipmentWarningTimes.get(i);
                 Date endtime = monitorEquipmentWarningTime.getEndtime();
                 Date begintime = monitorEquipmentWarningTime.getBegintime();
                 if (null!=begintime && null!= endtime){
                     timeBuffer.append(DateUtils.parseDatetime(begintime));
                     timeBuffer.append("~");
                     timeBuffer.append(DateUtils.parseDatetime(endtime));
                     if(i != monitorEquipmentWarningTimes.size()-1){
                         timeBuffer.append(",");
                     }
                 }
             }
             return timeBuffer.toString();
         }
         return  null;
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

}
