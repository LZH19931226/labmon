package com.hc.repository.impl;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hc.constant.UserScheduleEnumCode;
import com.hc.dto.UserSchedulingDto;
import com.hc.infrastructure.dao.UserSchedulingDao;
import com.hc.my.common.core.exception.IedsException;
import com.hc.my.common.core.util.BeanConverter;
import com.hc.po.UserSchedulingPo;
import com.hc.repository.UserSchedulingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @author hc
 */
@Repository
public class UserSchedulingRepositoryImpl extends ServiceImpl<UserSchedulingDao, UserSchedulingPo> implements UserSchedulingRepository {

    @Autowired
    private UserSchedulingDao userSchedulingDao;

    /**
     * 根据医院编码和时间删除用户排班信息
     * @param hospitalCode 医院编码
     * @param startTime 开始时间
     */
    @Override
    public void deleteInfo(String hospitalCode, Date startTime ,Date endTime) {
        userSchedulingDao.deleteInfo(hospitalCode,startTime,endTime);
    }

    /**
     * 插入用户排班信息
     * @param userSchedulingDtoList 用户排班信息集合
     */
    @Override
    public void insertUserSchedulingInfo(List<UserSchedulingDto> userSchedulingDtoList) {
        List<UserSchedulingPo> userSchedulingPos = BeanConverter.convert(userSchedulingDtoList, UserSchedulingPo.class);
        for (UserSchedulingPo userSchedulingPo : userSchedulingPos) {
            userSchedulingDao.insert(userSchedulingPo);
        }

    }

    /**
     * 获取当月的排班信息
     *
     * @param hospitalCode 医院信息
     * @param startMonth   开始月份
     * @param endMonth     结束月份
     * @return
     */
    @Override
    public List<UserSchedulingDto> searchScByHosMon(String hospitalCode, String startMonth, String endMonth) {
        return  userSchedulingDao.searchScByHosMon(hospitalCode,startMonth,endMonth);
    }

    /**
     * 更新排班时间
     *
     * @param hospitalCode 医院编码
     * @param oldStartTime 旧开始时间
     * @param oldEndTime   旧结束时间
     * @param newStartTime 新开始时间
     * @param newEndTime   新结束时间
     */
    @Override
    public void editScheduleInfo(String hospitalCode, Date oldStartTime, Date oldEndTime, Date newStartTime, Date newEndTime) {
        //判断时间段是否统一
        long time1 =  oldEndTime.getTime()-oldStartTime.getTime();
        long time2 =  newEndTime.getTime()-newStartTime.getTime();
        if(time1!=time2){
            throw new IedsException(UserScheduleEnumCode.TIME_PERIOD_DISAGREE.getMessage());
        }
        List<UserSchedulingPo> userSchedulingPos = userSchedulingDao.selectTimePeriod( hospitalCode,oldStartTime,oldEndTime);
        if(CollectionUtils.isEmpty(userSchedulingPos)){
            throw new IedsException(UserScheduleEnumCode.NO_SCHEDULE_INFORMATION_FOUND.getMessage());
        }
        //计算新时间段与旧时间段差值
        long days = newStartTime.getTime()-oldStartTime.getTime();

        for (UserSchedulingPo userSchedulingPo : userSchedulingPos) {

                long start = userSchedulingPo.getStartTime().getTime() + days;
                Date startDate = new Date();
                startDate.setTime(start);

                long end =  userSchedulingPo.getEndTime().getTime() + days;
                Date endDate = new Date();
                endDate.setTime(end);

                userSchedulingPo.setStartTime(startDate);
                userSchedulingPo.setEndTime(endDate);
                userSchedulingPo.setUsid(null);
                userSchedulingDao.insert(userSchedulingPo);
        }
    }

    /**
     * 查询排班信息
     *
     * @param hospitalCode   医院编码
     */
    @Override
    public List<UserSchedulingDto> selectScheduleWeekByCode(String hospitalCode) {
        List<UserSchedulingPo> userSchedulingPos =
                userSchedulingDao.selectList(Wrappers.lambdaQuery(new UserSchedulingPo()).eq(UserSchedulingPo::getHospitalCode, hospitalCode));
        return CollectionUtils.isEmpty(userSchedulingPos) ? null :BeanConverter.convert(userSchedulingPos,UserSchedulingDto.class);
    }
}
