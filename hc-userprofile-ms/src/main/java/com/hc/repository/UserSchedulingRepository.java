package com.hc.repository;

import com.hc.dto.UserSchedulingDto;

import java.util.Date;
import java.util.List;

/**
 * @author hc
 */
public interface UserSchedulingRepository {
    /**
     * 根据医院编码和时间删除用户排班信息
     * @param hospitalCode 医院编码
     * @param startTime 开始时间
     * @param endTime 结束时间
     */
    void deleteInfo(String hospitalCode, Date startTime,Date endTime);

    /**
     * 插入用户排班信息
     * @param userSchedulingDtoList 用户排班信息集合
     */
    void insertUserSchedulingInfo(List<UserSchedulingDto> userSchedulingDtoList);

    /**
     * 获取当月的排班信息
     * @param hospitalCode 医院信息
     * @param startMonth 开始月份
     * @param endMonth 结束月份
     * @return
     */
    List<UserSchedulingDto> searchScByHosMon(String hospitalCode, String startMonth, String endMonth);

    /**
     * 更新排班时间
     * @param hospitalCode 医院编码
     * @param oldStartTime 旧开始时间
     * @param oldEndTime  旧结束时间
     * @param newStartTime 新开始时间
     * @param newEndTime 新结束时间
     */
    void editScheduleInfo(String hospitalCode, Date oldStartTime, Date oldEndTime, Date newStartTime, Date newEndTime);

    /**
     * 查询本周排班信息
     * @param hospitalCode 医院编码
     * @return
     */
    List<UserSchedulingDto> selectScheduleWeekByCode(String hospitalCode);
}
