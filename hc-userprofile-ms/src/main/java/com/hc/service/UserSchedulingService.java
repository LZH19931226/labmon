package com.hc.service;

import com.hc.appliction.command.UserScheduleCommand;
import com.hc.dto.UserSchedulingDto;

import java.util.Date;
import java.util.List;

/**
 * @author hc
 */
public interface UserSchedulingService {
    /**
     * 保存排班
     * @param userScheduleCommand 用户排班命令
     */
    void saveSchedule(UserScheduleCommand userScheduleCommand);

    /**
     * 获取当月的排班记录
     * @param hospitalCode 医院编码
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
     * 按代码查找排班本周
     * @param hospitalCode 医院编码
     * @return
     */
    List<UserSchedulingDto> selectScheduleWeekByCode(String hospitalCode);
}
