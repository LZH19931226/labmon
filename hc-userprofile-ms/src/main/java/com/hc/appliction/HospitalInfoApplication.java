package com.hc.appliction;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hc.appliction.command.UserScheduleCommand;
import com.hc.command.labmanagement.model.hospital.HospitalCommand;
import com.hc.command.labmanagement.model.HospitalMadel;
import com.hc.command.labmanagement.operation.HospitalOperationLogCommand;
import com.hc.dto.HospitalRegistrationInfoDto;
import com.hc.dto.UserBackDto;
import com.hc.dto.UserSchedulingDto;
import com.hc.hospital.HospitalRedisApi;
import com.hc.labmanagent.OperationlogApi;
import com.hc.my.common.core.constant.enums.OperationLogEunm;
import com.hc.my.common.core.constant.enums.OperationLogEunmDerailEnum;
import com.hc.my.common.core.redis.dto.HospitalInfoDto;
import com.hc.my.common.core.struct.Context;
import com.hc.my.common.core.util.BeanConverter;
import com.hc.service.HospitalRegistrationInfoService;
import com.hc.service.UserBackService;
import com.hc.service.UserSchedulingService;
import com.hc.vo.hospital.HospitalInfoVo;
import com.hc.vo.user.UserSchedulingVo;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * 医院信息应用层
 *
 * @author hc
 */
@Component
public class HospitalInfoApplication {

    @Autowired
    private HospitalRegistrationInfoService hospitalRegistrationInfoService;

    @Autowired
    private UserSchedulingService userSchedulingService;

    @Autowired
    private OperationlogApi operationlogApi;

    @Autowired
    private UserBackService userBackService;

    @Autowired
    private HospitalRedisApi hospitalRedisApi;

    /**
     * 根据分页条件查询医院信息
     *
     * @param hospitalCommand 医院传输对象
     * @param pageSize        分页大小
     * @param pageCurrent     当前页数
     * @return 分页视图对象
     */
    public Page<HospitalInfoVo> selectHospitalInfo(HospitalCommand hospitalCommand, Long pageSize, Long pageCurrent) {
        Page<HospitalInfoVo> page = new Page<>(pageCurrent, pageSize);
        List<HospitalRegistrationInfoDto> hospitalInfos = hospitalRegistrationInfoService.selectHospitalInfo(page, hospitalCommand);
        List<HospitalInfoVo> list = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(hospitalInfos)) {
            hospitalInfos.forEach(res -> {
                HospitalInfoVo build = HospitalInfoVo.builder()
                        .hospitalCode(res.getHospitalCode())
                        .hospitalFullName(res.getHospitalFullName())
                        .hospitalName(res.getHospitalName())
                        .isEnable(res.getIsEnable())
                        .updateTime(res.getUpdateTime())
                        .timeInterval(res.getTimeInterval())
                        .timeoutRedDuration(res.getTimeoutRedDuration())
                        .factor(StringUtils.isEmpty(res.getFactor()) ? "0" : res.getFactor())
                        .soundLightAlarm(StringUtils.isEmpty(res.getSoundLightAlarm())? "0" : res.getSoundLightAlarm())
                        .build();
                list.add(build);
            });
        }
        page.setRecords(list);
        return page;
    }

    /**
     * 插入医院信息
     *
     * @param hospitalCommand 医院视图对象
     */
    @Transactional(rollbackFor = Exception.class)
    public void insertHospitalInfo(HospitalCommand hospitalCommand) {
        hospitalCommand.setUpdateTime(new Date());
        hospitalCommand.setOrderBy(Context.getUserId());
        hospitalCommand.setHospitalCode(UUID.randomUUID().toString().replaceAll("-", ""));
        String timeInterval = hospitalCommand.getTimeInterval();
        if(StringUtils.isEmpty(timeInterval)) timeInterval = "60";
        hospitalRegistrationInfoService.insertHospitalInfo(hospitalCommand);
        //添加日志信息
        operationlogApi.addHospitalOperationlog(buildHospitalOperationLogCommand(Context.getUserId(),new HospitalCommand(),hospitalCommand,
                OperationLogEunm.HOSPITALMANAGENT.getCode(), OperationLogEunmDerailEnum.ADD.getCode()));
        //添加缓存信息
        HospitalInfoDto hospitalInfoDto =  BeanConverter.convert(hospitalCommand, HospitalInfoDto.class);
        hospitalRedisApi.addHospitalRedisInfo(hospitalInfoDto);
    }

    public HospitalOperationLogCommand buildHospitalOperationLogCommand(String userId,HospitalCommand oldHospitalCommand,HospitalCommand newHospitalCommand,String type,String operationType){
        HospitalOperationLogCommand hospitalOperationLogCommand = new HospitalOperationLogCommand();
        hospitalOperationLogCommand.setUserId(userId);
        hospitalOperationLogCommand.setType(type);
        hospitalOperationLogCommand.setOperationType(operationType);
        hospitalOperationLogCommand.setNewHospitalCommand(newHospitalCommand);
        hospitalOperationLogCommand.setOldHospitalCommand(oldHospitalCommand);
        UserBackDto userBackDto =  userBackService.selectUserBackByUserId(userId);
        if(!ObjectUtils.isEmpty(userBackDto)){
            hospitalOperationLogCommand.setUsername(userBackDto.getUsername());
        }
        return hospitalOperationLogCommand;
    }

    /**
     * 更新医院信息
     *
     * @param hospitalCommand 医院信息参数
     */
    @Transactional(rollbackFor = Exception.class)
    public void editHospitalInfo(HospitalCommand hospitalCommand) {
        HospitalMadel hospitalInfo = findHospitalInfoByCode(hospitalCommand.getHospitalCode());
        HospitalCommand hospitalCommand1 = BeanConverter.convert(hospitalInfo,HospitalCommand.class);

        hospitalRegistrationInfoService.editHospitalInfo(hospitalCommand);
        //添加日志信息
        HospitalOperationLogCommand hospitalOperationLogCommand = buildHospitalOperationLogCommand(Context.getUserId(),hospitalCommand1 ,hospitalCommand,
                OperationLogEunm.HOSPITALMANAGENT.getCode(), OperationLogEunmDerailEnum.EDIT.getCode());
        operationlogApi.addHospitalOperationlog(hospitalOperationLogCommand);

        //更新缓存信息
        HospitalInfoDto hospitalInfoDto =  BeanConverter.convert(hospitalCommand,HospitalInfoDto.class);
        hospitalRedisApi.addHospitalRedisInfo(hospitalInfoDto);
    }

    /**
     * 根据医院编码删除医院信息
     *
     * @param hospitalCode 医院id
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteHospitalInfoByCode(String hospitalCode) {
        HospitalRegistrationInfoDto hospitalInfoByCode = hospitalRegistrationInfoService.findHospitalInfoByCode(hospitalCode);
        hospitalRegistrationInfoService.deleteHospitalInfoByCode(hospitalCode);

        //添加日志信息
        HospitalCommand convert = BeanConverter.convert(hospitalInfoByCode, HospitalCommand.class);
        HospitalOperationLogCommand hospitalOperationLogCommand =
                buildHospitalOperationLogCommand(Context.getUserId(), convert, new HospitalCommand(),
                        OperationLogEunm.HOSPITALMANAGENT.getCode(), OperationLogEunmDerailEnum.REMOVE.getCode());
        operationlogApi.addHospitalOperationlog(hospitalOperationLogCommand);

        //移除缓存信息
        hospitalRedisApi.removeHospitalRedisInfo(hospitalCode);
    }

    /**
     * 获取医院名称列表
     *
     * @return 医院名称集合
     */
    public List<HospitalInfoVo> selectHospitalNameList() {
        List<HospitalRegistrationInfoDto> dtoList = hospitalRegistrationInfoService.selectHospitalNameList();
        List<HospitalInfoVo> list = new ArrayList<>();
        dtoList.forEach(res -> {
            HospitalInfoVo hospitalInfoVo = HospitalInfoVo.builder()
                    .hospitalFullName(res.getHospitalFullName()).build();
            list.add(hospitalInfoVo);
        });
        return list;
    }

    /**
     * 保存排班
     *
     * @param userScheduleCommand 用户排班对象
     */
    public void saveSchedule(UserScheduleCommand userScheduleCommand) {
        userSchedulingService.saveSchedule(userScheduleCommand);
    }

    /**
     * 通过医院编码和月份信息查找用户当月的排班信息
     *
     * @param hospitalCode 医院编码
     * @param startMonth   开始月份
     * @param endMonth     结束月份
     * @return 用户信息集合
     */
    public List<UserSchedulingVo> searchScByHosMon(String hospitalCode, String startMonth, String endMonth) {
        List<UserSchedulingDto> userSchedulingDtoList = userSchedulingService.searchScByHosMon(hospitalCode, startMonth, endMonth);
        List<UserSchedulingVo> list = new ArrayList<>();
        userSchedulingDtoList.forEach(res -> {
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
     *
     * @param hospitalCode 医院编号
     * @param oldStartTime 旧开始时间
     * @param oldEndTime   旧结束时间
     * @param newStartTime 新开始时间
     * @param newEndTime   新结束时间
     */
    public void editScheduleInfo(String hospitalCode, Date oldStartTime, Date oldEndTime, Date newStartTime, Date newEndTime) {
        userSchedulingService.editScheduleInfo(hospitalCode, oldStartTime, oldEndTime, newStartTime, newEndTime);
    }

    /**
     * 按代码查找本周排班信息
     *
     * @param hospitalCode 医院编码
     * @return 用户排班集合
     */
    public List<UserSchedulingVo> selectScheduleWeekByCode(String hospitalCode) {
        List<UserSchedulingDto> userSchedulingDtoList = userSchedulingService.selectScheduleWeekByCode(hospitalCode);
        //元素合并
        List<UserSchedulingDto> listDto = mergeElements(userSchedulingDtoList);
        List<UserSchedulingVo> list = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(listDto)) {
            listDto.forEach(res -> {
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
     *
     * @param userSchedulingDtoList 用户排班信息集合
     * @return 合并后用户排班信息集合
     */
    public List<UserSchedulingDto> mergeElements(List<UserSchedulingDto> userSchedulingDtoList) {
        List<UserSchedulingDto> listDto = new ArrayList<>();
        Map<String, UserSchedulingDto> map = new HashMap<>();
        if (CollectionUtils.isNotEmpty(userSchedulingDtoList)) {
            for (UserSchedulingDto user : userSchedulingDtoList) {
                String str = "" + user.getUsername();
                if (map.containsKey(str)) {
                    Set<Integer> integerSet = map.get(str).getIntegerSet();
                    Set<Integer> integerSet1 = user.getIntegerSet();
                    integerSet1.addAll(integerSet);
                    user.setIntegerSet(integerSet1);
                    map.put(str, user);
                } else {
                    map.put(str, user);
                }
            }
            for (Map.Entry<String, UserSchedulingDto> stringUserSchedulingDtoEntry : map.entrySet()) {
                listDto.add(stringUserSchedulingDtoEntry.getValue());
            }
        }
        return listDto;
    }

    /**
     * 通过医院code获取医院信息
     * @param hospitalCode 医院id
     * @return 医院信息模型
     */
    public HospitalMadel findHospitalInfoByCode(String hospitalCode) {
        HospitalRegistrationInfoDto hospitalRegistrationInfoDto = hospitalRegistrationInfoService.findHospitalInfoByCode(hospitalCode);
       return BeanConverter.convert(hospitalRegistrationInfoDto,HospitalMadel.class);
    }

    /**
     * 获取医院code集合
     * @return 医院code集合
     */
    public List<String> selectHospitalCodeList() {
       return hospitalRegistrationInfoService.selectHospitalCodeList();
    }

    /**
     * 获取医院的所有信息
     * @return 医院信息集合
     */
    public List<HospitalInfoDto> getAllHospitalInfo() {
        List<HospitalRegistrationInfoDto> list =  hospitalRegistrationInfoService.getAllHospitalInfo();
        if(org.springframework.util.CollectionUtils.isEmpty(list)){
                return null;
        }
        return BeanConverter.convert(list,HospitalInfoDto.class);
    }
}
