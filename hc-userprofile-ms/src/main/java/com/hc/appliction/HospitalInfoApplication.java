package com.hc.appliction;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hc.appliction.command.UserScheduleCommand;
import com.hc.command.labmanagement.model.HospitalMadel;
import com.hc.command.labmanagement.model.UserSchedulingModel;
import com.hc.command.labmanagement.model.hospital.HospitalCommand;
import com.hc.command.labmanagement.operation.HospitalOperationLogCommand;
import com.hc.dto.HospitalRegistrationInfoDto;
import com.hc.dto.LabHosWarningTimeDto;
import com.hc.dto.UserBackDto;
import com.hc.dto.UserSchedulingDto;
import com.hc.hospital.HospitalRedisApi;
import com.hc.labmanagent.OperationlogApi;
import com.hc.my.common.core.constant.enums.OperationLogEunm;
import com.hc.my.common.core.constant.enums.OperationLogEunmDerailEnum;
import com.hc.my.common.core.exception.IedsException;
import com.hc.my.common.core.exception.LabSystemEnum;
import com.hc.my.common.core.redis.dto.HospitalInfoDto;
import com.hc.my.common.core.struct.Context;
import com.hc.my.common.core.util.BeanConverter;
import com.hc.my.common.core.util.DateUtils;
import com.hc.service.HospitalRegistrationInfoService;
import com.hc.service.LabHosWarningTimeService;
import com.hc.service.UserBackService;
import com.hc.service.UserSchedulingService;
import com.hc.vo.hospital.HospitalInfoVo;
import com.hc.vo.user.UserSchedulingVo;
import io.seata.spring.annotation.GlobalTransactional;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

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
    private LabHosWarningTimeService labHosWarningTimeService;

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

        List<LabHosWarningTimeDto> labHosWarningTimes =  labHosWarningTimeService.getAll();
        boolean notEmpty = CollectionUtils.isNotEmpty(labHosWarningTimes);
        Map<String,List<LabHosWarningTimeDto>> hosWaringTimeMap = new HashMap<>();
        if(notEmpty){
            hosWaringTimeMap = labHosWarningTimes.stream().collect(Collectors.groupingBy(LabHosWarningTimeDto::getHospitalCode));
        }
        List<HospitalInfoVo> list = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(hospitalInfos)) {
            for (HospitalRegistrationInfoDto res : hospitalInfos) {
                List<HospitalInfoVo.LabHosWarningTime> convert = new ArrayList<>();
                //判断医院是否有报警时段
                if(notEmpty && null != hosWaringTimeMap.get(res.getHospitalCode())){
                    List<LabHosWarningTimeDto> labHosWarningTimeDtos = hosWaringTimeMap.get(res.getHospitalCode());
                    convert = BeanConverter.convert(labHosWarningTimeDtos, HospitalInfoVo.LabHosWarningTime.class);
                }
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
                        .labHosWarningTimes(convert)
                        .build();
                list.add(build);
            }
        }
        page.setRecords(list);
        return page;
    }

    /**
     * 插入医院信息
     *
     * @param hospitalCommand 医院视图对象
     */
    @GlobalTransactional
    public void insertHospitalInfo(HospitalCommand hospitalCommand) {
        hospitalCommand.setUpdateTime(new Date());
        hospitalCommand.setOrderBy(Context.getUserId());
        String hospitalCode = UUID.randomUUID().toString().replaceAll("-", "");
        hospitalCommand.setHospitalCode(hospitalCode);
        String timeInterval = hospitalCommand.getTimeInterval();
        if(StringUtils.isEmpty(timeInterval)) timeInterval = "60";
        //插入医院信息
        hospitalRegistrationInfoService.insertHospitalInfo(hospitalCommand);
        //判断是否需要插入医院报警时段信息
        List<HospitalCommand.LabHosWarningTime> hosWarningTimes = hospitalCommand.getHosWarningTimes();
        if(CollectionUtils.isNotEmpty(hosWarningTimes) && hosWarningTimes.get(0) != null && hosWarningTimes.size()<=3){
            //判断时间段是否正常
            checkTime(hosWarningTimes);
            List<LabHosWarningTimeDto> labHosWarningTimes = BeanConverter.convert(hosWarningTimes, LabHosWarningTimeDto.class);
            labHosWarningTimes.forEach(res->res.setHospitalCode(hospitalCode));
            labHosWarningTimeService.saveObj(labHosWarningTimes);
        }
        //添加日志信息
        operationlogApi.addHospitalOperationlog(buildHospitalOperationLogCommand(Context.getUserId(),new HospitalCommand(),hospitalCommand,
                OperationLogEunm.HOSPITAL_MANAGENT.getCode(), OperationLogEunmDerailEnum.ADD.getCode()));
        //添加缓存信息
        HospitalInfoDto hospitalInfoDto =  BeanConverter.convert(hospitalCommand, HospitalInfoDto.class);
        hospitalRedisApi.addHospitalRedisInfo(hospitalInfoDto);
    }

    private void checkTime(List<HospitalCommand.LabHosWarningTime> hosWarningTimes) {
        List<LocalDateTime> list = new ArrayList<>();
        for (HospitalCommand.LabHosWarningTime hosWarningTime : hosWarningTimes) {
            LocalDateTime beginTime = hosWarningTime.getBeginTime();
            LocalDateTime endTime = hosWarningTime.getEndTime();
            if(beginTime ==null || endTime == null){
                continue;
            }
            if(beginTime.compareTo(endTime)>0){
                throw new IedsException(LabSystemEnum.START_TIME_AND_END_TIME_ARE_ABNORMAL);
            }
            list.add(beginTime);
            list.add(endTime);
        }
        if(CollectionUtils.isNotEmpty(list)){
            int size = list.size();
            switch (size){
                case 4:
                    Boolean aBoolean = checkTimesHasOverlap(list.get(0), list.get(1), list.get(2), list.get(3));
                    if(aBoolean){
                        throw new IedsException(LabSystemEnum.THERE_IS_AN_OVERLAP_BETWEEN_THE_TWO_TIME_PERIODS);
                    }
                    break;
                case 6:
                    //有三段时间需要比三次
                    Boolean one = checkTimesHasOverlap(list.get(0), list.get(1), list.get(2), list.get(3));
                    Boolean two = checkTimesHasOverlap(list.get(0), list.get(1), list.get(4), list.get(5));
                    Boolean three = checkTimesHasOverlap(list.get(2), list.get(3), list.get(4), list.get(5));
                    if(one || two || three){
                        throw new IedsException(LabSystemEnum.THERE_IS_AN_OVERLAP_OF_THE_THREE_TIME_PERIODS);
                    }
                    break;
                default:
                    break;
            }
        }
    }

    /**
     *
     * @param localDateTime
     * @param localDateTime1
     * @param localDateTime2
     * @param localDateTime3
     * @return
     */
    private Boolean checkTimesHasOverlap(LocalDateTime localDateTime, LocalDateTime localDateTime1, LocalDateTime localDateTime2, LocalDateTime localDateTime3) {
        return !(localDateTime.compareTo(localDateTime3)>0 || localDateTime1.compareTo(localDateTime2)<0);
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
    @GlobalTransactional
    public void editHospitalInfo(HospitalCommand hospitalCommand) {
        HospitalMadel hospitalInfo = findHospitalInfoByCode(hospitalCommand.getHospitalCode());
        String hospitalCode = hospitalInfo.getHospitalCode();
        HospitalCommand hospitalCommand1 = BeanConverter.convert(hospitalInfo,HospitalCommand.class);

        //修改医院信息
        hospitalRegistrationInfoService.editHospitalInfo(hospitalCommand);
        //判断是否需要更新医院报警时段
        List<HospitalCommand.LabHosWarningTime> hosWarningTimes = hospitalCommand.getHosWarningTimes();
        if(CollectionUtils.isNotEmpty(hosWarningTimes) && hosWarningTimes.get(0)!=null){
            //不为空时先校验新的报警时段是否合法，再去删除和插入
            checkTime(hosWarningTimes);
            labHosWarningTimeService.removeObjByCode(hospitalCode);
            List<LabHosWarningTimeDto> labHosWarningTimeDtos = BeanConverter.convert(hosWarningTimes, LabHosWarningTimeDto.class);
            labHosWarningTimeDtos.forEach(res->res.setHospitalCode(hospitalCode));
            labHosWarningTimeService.saveObj(labHosWarningTimeDtos);
        }else {
            //为空时直接删除
            labHosWarningTimeService.removeObjByCode(hospitalCode);
        }
        //添加日志信息
        HospitalOperationLogCommand hospitalOperationLogCommand = buildHospitalOperationLogCommand(Context.getUserId(),hospitalCommand1 ,hospitalCommand,
                OperationLogEunm.HOSPITAL_MANAGENT.getCode(), OperationLogEunmDerailEnum.EDIT.getCode());
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
    @GlobalTransactional
    public void deleteHospitalInfoByCode(String hospitalCode) {
        HospitalRegistrationInfoDto hospitalInfoByCode = hospitalRegistrationInfoService.findHospitalInfoByCode(hospitalCode);
        hospitalRegistrationInfoService.deleteHospitalInfoByCode(hospitalCode);
        //同时删除医院报警信息
        labHosWarningTimeService.removeObjByCode(hospitalCode);


        //添加日志信息
        HospitalCommand convert = BeanConverter.convert(hospitalInfoByCode, HospitalCommand.class);
        HospitalOperationLogCommand hospitalOperationLogCommand =
                buildHospitalOperationLogCommand(Context.getUserId(), convert, new HospitalCommand(),
                        OperationLogEunm.HOSPITAL_MANAGENT.getCode(), OperationLogEunmDerailEnum.REMOVE.getCode());
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

    /**
     * 获取当天的排班信息
     * @param hospitalCode
     * @return
     */
    public List<UserSchedulingModel> getHospitalScheduleInfo(String hospitalCode) {
        Date date = new Date();
        String today = DateUtils.paseDate(date);
        List<UserSchedulingDto> hospitalScheduleInfo = userSchedulingService.getHospitalScheduleInfo(hospitalCode, today, DateUtils.getYesterday(date));
        List<UserSchedulingModel> list = new ArrayList<>();
        hospitalScheduleInfo.forEach(res->{
            UserSchedulingModel userSchedulingModel = new UserSchedulingModel();
            userSchedulingModel.setUsid(Math.toIntExact(res.getUsid()));
            userSchedulingModel.setUserphone(res.getUserPhone());
            userSchedulingModel.setUsername(res.getUsername());
            userSchedulingModel.setUserid(res.getUserid());
            userSchedulingModel.setStarttime(res.getStartTime());
            userSchedulingModel.setReminders(res.getReminders());
            userSchedulingModel.setHospitalcode(res.getHospitalCode());
            userSchedulingModel.setEndtime(res.getEndTime());
            userSchedulingModel.setCreateuser(res.getCreateUser());
            userSchedulingModel.setCreatetime(res.getCreateTime());
            list.add(userSchedulingModel);
        });
        return list;
    }
}
