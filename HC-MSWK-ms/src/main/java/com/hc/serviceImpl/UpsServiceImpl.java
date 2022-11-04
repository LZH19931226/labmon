package com.hc.serviceImpl;

import com.hc.SmsApi;
import com.hc.command.labmanagement.model.UserSchedulingModel;
import com.hc.device.ProbeRedisApi;
import com.hc.device.SnDeviceRedisApi;
import com.hc.hospital.HospitalInfoApi;
import com.hc.hospital.HospitalRedisApi;
import com.hc.labmanagent.MonitorEquipmentApi;
import com.hc.my.common.core.redis.dto.*;
import com.hc.my.common.core.util.BeanConverter;
import com.hc.po.UserScheduLing;
import com.hc.service.UpsService;
import com.hc.user.UserRightInfoApi;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UpsServiceImpl implements UpsService {

    @Autowired
    private ProbeRedisApi probeRedisApi;

    @Autowired
    private HospitalInfoApi hospitalInfoApi;

    @Autowired
    private UserRightInfoApi userRightInfoApi;

    @Autowired
    private SnDeviceRedisApi snDeviceRedisApi;

    @Autowired
    private HospitalRedisApi hospitalRedisApi;

    @Autowired
    private SmsApi smsApi;

    @Override
    public void sendInfo(ParamaterModel model, String equipmentno, String hospitalcode) {
        String ups = model.getUPS();
        String cmdid = model.getCmdid();
        String sn = model.getSN();
        if (StringUtils.equalsIgnoreCase(cmdid,"89")){
            //协议为89时
            //判断当前ups的值，异常变为正常时继续
            if(StringUtils.equalsAnyIgnoreCase(ups,"1","2","5")){
                return;
            }
            //此处根据设备判断设备有没有开启恢复断电报警功能,没有的话,则不需要后续
            boolean noticeForOpen = upsNoticeForOpen(sn);
            if(!noticeForOpen){
                return;
            }

            List<ProbeInfoDto> result = probeRedisApi.getCurrentProbeValueInfo(hospitalcode, equipmentno).getResult();
            if(CollectionUtils.isEmpty(result)){
                return;
            }
            for (ProbeInfoDto probeInfoDto : result) {
                String probeEName = probeInfoDto.getProbeEName();
                if("currentups".equals(probeEName)){
                    String value = probeInfoDto.getValue();
                    if (StringUtils.equalsAnyIgnoreCase(value,"1","2","5")){
                        sendSms(model,equipmentno,hospitalcode);
                    }
                }
            }
        }
        if (StringUtils.equalsIgnoreCase(cmdid,"a4")){
            //协议为a4时
            //判断当前ups的值，异常变为正常时继续
            if(StringUtils.equalsAnyIgnoreCase(ups,"1")){
                return;
            }

            //此处根据设备判断设备有没有开启恢复断电报警功能,没有的话,则不需要后续
            boolean noticeForOpen = upsNoticeForOpen(sn);
            if(!noticeForOpen){
                return;
            }

            List<ProbeInfoDto> result = probeRedisApi.getCurrentProbeValueInfo(hospitalcode, equipmentno).getResult();
            if(CollectionUtils.isEmpty(result)){
                return;
            }
            for (ProbeInfoDto probeInfoDto : result) {
                String probeEName = probeInfoDto.getProbeEName();
                if("currentups".equals(probeEName)){
                    String value = probeInfoDto.getValue();
                    if (StringUtils.equalsAnyIgnoreCase(value,"1")){
                        sendSms(model,equipmentno,hospitalcode);
                    }
                }
            }
        }
    }

    public void sendSms(ParamaterModel model, String equipmentno, String hospitalcode){
        List<UserRightRedisDto> userList = addUserScheduLing(hospitalcode);
        //获取设备缓存信息
        SnDeviceDto snDeviceDto = snDeviceRedisApi.getSnDeviceDto(model.getSN()).getResult();
        if(CollectionUtils.isNotEmpty(userList)){
            String equipmentName = snDeviceDto.getEquipmentName();
            //调用短信接口
            for (UserRightRedisDto userRightRedisDto : userList) {
                String role = userRightRedisDto.getRole();
                if (StringUtils.isNotEmpty(role) && StringUtils.equals(role, "1")) {
                    HospitalInfoDto hospitalInfoDto = hospitalRedisApi.findHospitalRedisInfo(hospitalcode).getResult();
                    equipmentName = hospitalInfoDto.getHospitalName() + equipmentName;
                }
                smsApi.upsRemind(userRightRedisDto.getPhoneNum(),equipmentName);
            }
        }
    }

    private List<UserRightRedisDto> addUserScheduLing(String hospitalcode) {
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

    /** 探头是开启市电恢复提醒 */
    public boolean upsNoticeForOpen(String sn){
        SnDeviceDto snDeviceDto = snDeviceRedisApi.getSnDeviceDto(sn).getResult();
        String upsNotice = snDeviceDto.getUpsNotice();
        //0和空表示未开启
        if(StringUtils.isBlank(upsNotice) || "0".equals(upsNotice)){
            return false;
        }
        return true;
    }
}
