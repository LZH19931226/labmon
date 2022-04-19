package com.hc.appliction;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hc.appliction.command.HospitalCommand;
import com.hc.appliction.command.UserScheduleCommand;
import com.hc.dto.HospitalRegistrationInfoDto;
import com.hc.dto.UserSchedulingDto;
import com.hc.service.HospitalRegistrationInfoService;
import com.hc.service.UserSchedulingService;
import com.hc.vo.User.UserSchedulingVo;
import com.hc.vo.hospital.HospitalInfoVo;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 医院信息应用层
 * @author hc
 */
@Component
public class HospitalInfoApplication {

    @Autowired
    private HospitalRegistrationInfoService hospitalRegistrationInfoService;

    @Autowired
    private UserSchedulingService userSchedulingService;
    /**
     * 根据分页条件查询医院信息
     * @param hospitalCommand 医院传输对象
     * @param pageSize      分页大小
     * @param pageCurrent   当前页数
     * @return
     */
    public Page<HospitalInfoVo> selectHospitalInfo(HospitalCommand hospitalCommand, Long pageSize, Long pageCurrent) {
        Page<HospitalInfoVo> page = new Page<>(pageCurrent,pageSize);
        List<HospitalRegistrationInfoDto> hospitalInfos = hospitalRegistrationInfoService.selectHospitalInfo( page, hospitalCommand);
        List<HospitalInfoVo> list = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(hospitalInfos)) {
            hospitalInfos.forEach(res->{
                HospitalInfoVo build = HospitalInfoVo.builder()
                        .hospitalCode(res.getHospitalCode())
                        .hospitalFullName(res.getHospitalFullName())
                        .hospitalName(res.getHospitalName())
                        .isEnable(res.getIsEnable())
                        .updateTime(res.getUpdateTime())
                        .build();
                list.add(build);
            });
        }
        page.setRecords(list);
        return page;
    }

    /**
     * 插入医院信息
     * @param hospitalCommand 医院视图对象
     * @return
     */
    public void insertHospitalInfo(HospitalCommand hospitalCommand) {
        hospitalRegistrationInfoService.insertHospitalInfo(hospitalCommand);
    }

    /**
     * 更新医院信息
     * @param hospitalCommand
     */
    public void editHospitalInfo(HospitalCommand hospitalCommand) {
        hospitalRegistrationInfoService.editHospitalInfo(hospitalCommand);
    }

    /**
     * 根据医院编码删除医院信息
     * @param hospitalCode
     */
    public void deleteHospitalInfoByCode(String hospitalCode) {
        hospitalRegistrationInfoService.deleteHospitalInfoByCode(hospitalCode);
    }

    /**
     * 获取医院名称列表
     * @return 医院名称集合
     */
    public List<HospitalInfoVo> selectHospitalNameList() {
        List<HospitalRegistrationInfoDto> dtoList =  hospitalRegistrationInfoService.selectHospitalNameList();
        List<HospitalInfoVo> list = new ArrayList<>();
        dtoList.forEach(res->{
            HospitalInfoVo hospitalInfoVo = HospitalInfoVo.builder()
                    .hospitalFullName(res.getHospitalFullName()).build();
            list.add(hospitalInfoVo);
        });
        return list;
    }

    /**
     * 保存排班
     * @param userScheduleCommand 用户排班对象
     */
    public void saveSchedule(UserScheduleCommand userScheduleCommand) {
        userSchedulingService.saveSchedule(userScheduleCommand);
    }

    public List<UserSchedulingVo> searchScByHosMon(String hospitalCode, String startMonth, String endMonth) {
        List<UserSchedulingDto> userSchedulingDtoList = userSchedulingService.searchScByHosMon(hospitalCode,startMonth,endMonth);
        List<UserSchedulingVo> list = new ArrayList<>();
        userSchedulingDtoList.forEach(res->{
            UserSchedulingVo build = UserSchedulingVo
                    .builder()
                    .hospitalCode(res.getHospitalCode())
                    .usid(res.getUsid())
                    .userid(res.getUserid())
                    .username(res.getUsername())
                    .userPhone(res.getUserPhone())
                    .createTime(res.getCreateTime())
                    .createUser(res.getCreateUser())
                    .startTime(res.getStartTime())
                    .endTime(res.getEndTime())
                    .reminders(res.getReminders())
                    .build();
            list.add(build);
        });
        return list;
    }

    /**
     * 复制医院排班时间
     * @param hospitalCode 医院编号
     * @param oldStartTime  旧开始时间
     * @param oldEndTime 旧结束时间
     * @param newStartTime 新开始时间
     * @param newEndTime 新结束时间
     */
    public void editScheduleInfo(String hospitalCode, Date oldStartTime, Date oldEndTime, Date newStartTime, Date newEndTime) {
        userSchedulingService.editScheduleInfo(hospitalCode,oldStartTime,oldEndTime,newStartTime,newEndTime);
    }

    /**
     * 按代码查找排班本周
     * @param hospitalCode 医院编码
     */
    public List<UserSchedulingVo> selectScheduleWeekByCode(String hospitalCode) {
        List<UserSchedulingDto> userSchedulingDtoList = userSchedulingService.selectScheduleWeekByCode(hospitalCode);
        //元素合并
        List<UserSchedulingDto> listDto = mergeElements(userSchedulingDtoList);
        List<UserSchedulingVo> list = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(listDto)){
            listDto.forEach(res->{
                UserSchedulingVo build = UserSchedulingVo.builder()
                        .username(res.getUsername())
                        .userPhone(res.getUserPhone())
                        .reminders(res.getReminders())
                        .integerSet(res.getIntegerSet())
                        .build();
                list.add(build);
            });
        }
        return list;
    }

    /**
     * 将具有相同特性的元素合并
     * @param userSchedulingDtoList
     * @return
     */
    public List<UserSchedulingDto> mergeElements(List<UserSchedulingDto> userSchedulingDtoList){
        List<UserSchedulingDto> listDto = new ArrayList<>();
        Map<String,UserSchedulingDto> map = new HashedMap();
        if(CollectionUtils.isNotEmpty(userSchedulingDtoList)){
            for (UserSchedulingDto user : userSchedulingDtoList) {
                String str = ""+user.getUsername();
                if(map.containsKey(str)){
                    Set<Integer> integerSet = map.get(str).getIntegerSet();
                    Set<Integer> integerSet1 = user.getIntegerSet();
                    integerSet1.addAll(integerSet);
                    user.setIntegerSet(integerSet1);
                    map.put(str,user);
                }else{
                    map.put(str,user);
                }
            }
            for (Map.Entry<String, UserSchedulingDto> stringUserSchedulingDtoEntry : map.entrySet()) {
                listDto.add(stringUserSchedulingDtoEntry.getValue());
            }
        }
        return listDto;
    }
}
