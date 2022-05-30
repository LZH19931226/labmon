package com.hc.service.impl;

import com.hc.appliction.command.UserScheduleCommand;
import com.hc.dto.UserSchedulingDto;
import com.hc.infrastructure.dao.UserSchedulingDao;
import com.hc.repository.UserSchedulingRepository;
import com.hc.service.UserSchedulingService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 用户排班
 * @author user
 */
@Service
public class UserSchedulingServiceImpl implements UserSchedulingService {

    @Autowired
    private UserSchedulingRepository userSchedulingRepository;
    @Autowired
    private UserSchedulingDao userSchedulingDao;

    @Override
    public void saveSchedule(UserScheduleCommand userScheduleCommand) {
        String hospitalCode = userScheduleCommand.getHospitalCode();
        Date startTime = userScheduleCommand.getStartTime();
        String createUser = userScheduleCommand.getCreateUser();
        Date endTime = userScheduleCommand.getEndTime();
        List<UserSchedulingDto> userSchedulingDtoList = userScheduleCommand.getUserSchedulingDtoList();
        userSchedulingRepository.deleteInfo(hospitalCode,startTime,endTime);
        if(CollectionUtils.isNotEmpty(userSchedulingDtoList)){
            userSchedulingDtoList.forEach(res->{
                res.setHospitalCode(hospitalCode);
                res.setCreateTime(new Date());
                res.setCreateUser(createUser);
            });
            userSchedulingRepository.insertUserSchedulingInfo(userSchedulingDtoList);
        }
    }

    @Override
    public List<UserSchedulingDto> searchScByHosMon(String hospitalCode, String startMonth, String endMonth) {
        return userSchedulingRepository.searchScByHosMon(hospitalCode,startMonth,endMonth);
    }

    @Override
    public void editScheduleInfo(String hospitalCode, Date oldStartTime, Date oldEndTime, Date newStartTime, Date newEndTime) {
        userSchedulingRepository.editScheduleInfo(hospitalCode,oldStartTime,oldEndTime,newStartTime,newEndTime);
    }

    /**
     * 按代码查找排班本周
     *  分别发查询
     *      ·本周的日期对应的星期数map集合
     *      ·排班人员的排班开始时间和结束时间
     * @param hospitalCode 医院编码
     */
    @Override
    public List<UserSchedulingDto> selectScheduleWeekByCode(String hospitalCode) {
        List<UserSchedulingDto> userSchedulingDtoList = userSchedulingRepository.selectScheduleWeekByCode(hospitalCode);
        if(CollectionUtils.isNotEmpty(userSchedulingDtoList)){
            //获取本周的日期集合
            Map<String, Integer> theWeekDateList = getTheWeekDateList();
            for (UserSchedulingDto userSchedulingDto : userSchedulingDtoList) {
                Set<Integer> set = new HashSet<>();
                List<String> dateList = getDateList(userSchedulingDto.getStartTime(), userSchedulingDto.getEndTime());
                Set<String> strings = getTheWeekDateList().keySet();
                dateList.forEach(res->{
                    if(strings.contains(res)){
                        set.add(theWeekDateList.get(res));
                    }
                });
                userSchedulingDto.setIntegerSet(set);
            }
            //去除集合中没有星期的元素
            List<UserSchedulingDto> collect =
                    userSchedulingDtoList.stream().filter(res -> res.getIntegerSet() == null || res.getIntegerSet().size() == 0).collect(Collectors.toList());
            userSchedulingDtoList.removeAll(collect);
        }
        return userSchedulingDtoList;
    }


    /**
     * 获取本周的日期集合
     * @return
     */
    public static Map<String, Integer> getTheWeekDateList(){
        Calendar cal = Calendar.getInstance();
        // 设置第一天是星期一
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        // 获得当前日期是一个星期的第几天
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);
        if(dayWeek==1){
            dayWeek = 8;
        }
        // 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
        cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - dayWeek);
        Date mondayDate = cal.getTime();
        cal.add(Calendar.DATE, 4 +cal.getFirstDayOfWeek());
        Date sundayDate = cal.getTime();
        Calendar calBegin = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间
        calBegin.setTime(mondayDate);
        Calendar calEnd = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间
        calEnd.setTime(sundayDate);

        Map<String,Integer> map = new HashMap<>();
        //国外是0-11月,所以要加一
        int month = calBegin.get(Calendar.MONTH)+1;
        //获取月的第几日
        int day = calBegin.get(Calendar.DAY_OF_MONTH);
        //获取周的第几天 1为星期天，2为星期一
        int week = calBegin.get(Calendar.DAY_OF_WEEK)-1;
        //这值星期一
        map.put(""+month+day,week);

        //测试此日期是否在指定日期之后
        while (sundayDate.after(calBegin.getTime())) {
            // 根据日历的规则，为给定的日历字段添加或减去指定的时间量
            calBegin.add(Calendar.DAY_OF_MONTH, 1);
            //国外是0-11月,所以要加一
             month = calBegin.get(Calendar.MONTH)+1;
            //获取月的第几日
             day = calBegin.get(Calendar.DAY_OF_MONTH);
            //获取周的第几天 1为星期天，2为星期一
             week = calBegin.get(Calendar.DAY_OF_WEEK)-1;
            if(week==0){
                week=7;
            }
            map.put(""+month+day,week);
        }
        return map;
    }

    /**
     * 获取时间段的日期集合
     * @param startTime
     * @param endTime
     * @return
     */
    public static List<String> getDateList(Date startTime,Date endTime){
        Calendar calStart = Calendar.getInstance();
        calStart.setTime(startTime);
        Calendar calEnd = Calendar.getInstance();
        calEnd.setTime(endTime);
        List<Date> list = new ArrayList<>();
        list.add(calStart.getTime());
        List<String> stringList = new ArrayList<>();
        //国外是0-11月,所以要加一
        int month = calStart.get(Calendar.MONTH)+1;
        //获取月的第几日
        int day = calStart.get(Calendar.DAY_OF_MONTH);
        stringList.add(""+month+day);
        while (calEnd.after(calStart)){
            // 根据日历的规则，为给定的日历字段添加或减去指定的时间量
            calStart.add(Calendar.DAY_OF_MONTH, 1);
            month = calStart.get(Calendar.MONTH)+1;
            day = calStart.get(Calendar.DAY_OF_MONTH);
            stringList.add(""+month+day);
        }
        return stringList;
    }

}
