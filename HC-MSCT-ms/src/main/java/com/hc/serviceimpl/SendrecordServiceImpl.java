package com.hc.serviceimpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hc.MessageApi;
import com.hc.clickhouse.po.Warningrecord;
import com.hc.clickhouse.repository.WarningrecordRepository;
import com.hc.mapper.SendrecordDao;
import com.hc.model.WarningModel;
import com.hc.my.common.core.constant.enums.DictEnum;
import com.hc.my.common.core.constant.enums.ElkLogDetail;
import com.hc.my.common.core.constant.enums.NotifyChannel;
import com.hc.my.common.core.constant.enums.PushType;
import com.hc.my.common.core.domain.P2PNotify;
import com.hc.my.common.core.redis.dto.HospitalInfoDto;
import com.hc.my.common.core.redis.dto.UserRightRedisDto;
import com.hc.my.common.core.util.ElkLogDetailUtil;
import com.hc.po.Sendrecord;
import com.hc.po.Userright;
import com.hc.service.SendrecordService;
import com.hc.utils.JsonUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SendrecordServiceImpl extends ServiceImpl<SendrecordDao, Sendrecord> implements SendrecordService {


    @Autowired
    private WarningrecordRepository warningrecordRepository;

    @Autowired
    private MessageApi messageApi;


    @Override
    public void pushNotification(List<UserRightRedisDto> list, WarningModel warningModel, HospitalInfoDto hospitalInfoDto) {
        String logId = warningModel.getLogId();
        //获取电话.
        List<Sendrecord> sendrecords = new ArrayList<>();
        StringBuilder phoneCallUser = new StringBuilder();
        StringBuilder mailCallUser = new StringBuilder();
        for (UserRightRedisDto userright : list) {
            String reminders = userright.getReminders();
            String phonenum = userright.getPhoneNum();
            String role = userright.getRole();
            String equipmentName = warningModel.getEquipmentname();
            String hospitalName = hospitalInfoDto.getHospitalName();
            String hospitalcode = warningModel.getHospitalcode();
            String unit = warningModel.getUnit();
            String value = warningModel.getValue();
            //1为运维后台人员
            if (StringUtils.isNotEmpty(role) && StringUtils.equals(role, "1")) {
                equipmentName = hospitalName + equipmentName;
            }
            //不报警
            if (StringUtils.equals(reminders, DictEnum.UNOPENED_CONTACT_DETAILS.getCode()) || StringUtils.isEmpty(phonenum)) {
                continue;
            }
            if (StringUtils.isEmpty(reminders) || StringUtils.equals(DictEnum.PHONE_SMS.getCode(), reminders)) {
                //拨打电话短信
                buildP2PNotify(phonenum, equipmentName, unit, value, Arrays.asList(NotifyChannel.SMS, NotifyChannel.PHONE));
                mailCallUser.append(phonenum).append("/");
                Sendrecord sendrecord = producePhoneRecord(phonenum, hospitalcode, equipmentName, unit, "1");
                sendrecords.add(sendrecord);
                phoneCallUser.append(phonenum).append("/");
                Sendrecord sendrecord1 = producePhoneRecord(phonenum, hospitalcode, equipmentName, unit, "0");
                sendrecords.add(sendrecord1);
                ElkLogDetailUtil.buildElkLogDetail(ElkLogDetail.from(ElkLogDetail.MSCT_SERIAL_NUMBER17.getCode()), JsonUtil.toJson(userright), logId);
            } else if (StringUtils.equals(reminders, DictEnum.PHONE.getCode())) {
                buildP2PNotify(phonenum, equipmentName, unit, value, Collections.singletonList(NotifyChannel.PHONE));
                phoneCallUser.append(phonenum).append("/");
                Sendrecord sendrecord = producePhoneRecord(userright.getPhoneNum(), hospitalcode, equipmentName, unit, "1");
                sendrecords.add(sendrecord);
                ElkLogDetailUtil.buildElkLogDetail(ElkLogDetail.from(ElkLogDetail.MSCT_SERIAL_NUMBER15.getCode()), JsonUtil.toJson(userright), logId);
            } else if (StringUtils.equals(reminders, DictEnum.SMS.getCode())) {
                buildP2PNotify(phonenum, equipmentName, unit, value, Collections.singletonList(NotifyChannel.SMS));
                mailCallUser.append(phonenum).append("/");
                Sendrecord sendrecord = producePhoneRecord(userright.getPhoneNum(), hospitalcode, equipmentName, unit, "0");
                sendrecords.add(sendrecord);
                ElkLogDetailUtil.buildElkLogDetail(ElkLogDetail.from(ElkLogDetail.MSCT_SERIAL_NUMBER16.getCode()), JsonUtil.toJson(userright), logId);
            }
        }
        if (CollectionUtils.isNotEmpty(sendrecords)) {
            this.saveBatch(sendrecords);
        }
        //修改报警通知人
        Warningrecord warningrecord = warningModel.getWarningrecord();
        warningrecord.setPkid(warningrecord.getPkid());

        if(mailCallUser.length()>0
                && !"null".equals(mailCallUser.toString())
                && !"".equals(mailCallUser.toString())){
            mailCallUser.deleteCharAt(mailCallUser.length()-1);
            warningrecord.setMailCallUser(mailCallUser.toString());
        }

        if(phoneCallUser.length()>0
                && !"null".equals(phoneCallUser.toString())
                && !"".equals(phoneCallUser.toString())){
            phoneCallUser.deleteCharAt(phoneCallUser.length()-1);
        }{
            phoneCallUser.deleteCharAt(phoneCallUser.length()-1);
            warningrecord.setPhoneCallUser(phoneCallUser.toString());
        }

        warningrecordRepository.saveWarningInfo(warningrecord);

    }

    @Override
    public void pushTimeOutNotification(List<Userright> userrights, String hospitalName, String eqTypeName, String count) {
        for (Userright userright : userrights) {
            String phonenum = userright.getPhonenum();
            if (StringUtils.isEmpty(phonenum)) {
                continue;
            }
            String timeoutwarning = userright.getTimeoutwarning();//超时报警方式
            // 超时报警
            if (StringUtils.isBlank(timeoutwarning) || StringUtils.equals(timeoutwarning, "0")) {
                buildTimeOutP2PNotify(phonenum, eqTypeName, "超时", hospitalName,Arrays.asList(NotifyChannel.SMS, NotifyChannel.PHONE),count);
                ElkLogDetailUtil.buildElkLogDetail(ElkLogDetail.from(ElkLogDetail.MSCT_SERIAL_NUMBER21.getCode()), JsonUtil.toJson(userright), null);
            } else if (StringUtils.equals(timeoutwarning, "1")) {
                buildTimeOutP2PNotify(phonenum, eqTypeName, "超时", hospitalName,Collections.singletonList(NotifyChannel.PHONE),count);
                ElkLogDetailUtil.buildElkLogDetail(ElkLogDetail.from(ElkLogDetail.MSCT_SERIAL_NUMBER19.getCode()), JsonUtil.toJson(userright), null);
            } else if (StringUtils.equals(timeoutwarning, "2")) {
                buildTimeOutP2PNotify(phonenum, eqTypeName, "超时", hospitalName,Collections.singletonList(NotifyChannel.SMS),count);
                ElkLogDetailUtil.buildElkLogDetail(ElkLogDetail.from(ElkLogDetail.MSCT_SERIAL_NUMBER20.getCode()), JsonUtil.toJson(userright), null);
            }
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

    public void buildP2PNotify(String phone, String equipmentname, String unit, String value, List<NotifyChannel> notifyChannels) {
        P2PNotify p2PNotify = new P2PNotify();
        p2PNotify.setUserId(phone);
        p2PNotify.setMessageTitle(equipmentname);
        p2PNotify.setMessageCover(unit);
        p2PNotify.setMessageIntro(value);
        p2PNotify.setChannels(notifyChannels);
        p2PNotify.setMessageBodys(PushType.USUAL_ALARM.name());
        p2PNotify.setServiceNo("1");
        messageApi.send(p2PNotify);
    }

    public void buildTimeOutP2PNotify(String phone,String equipmentname,String unit,String hospitalName,List<NotifyChannel> notifyChannels,String count) {
        P2PNotify p2PNotify = new P2PNotify();
        p2PNotify.setUserId(phone);
        p2PNotify.setMessageTitle(equipmentname);
        p2PNotify.setMessageCover(unit);
        p2PNotify.setMessageIntro(hospitalName);
        p2PNotify.setChannels(notifyChannels);
        p2PNotify.setMessageBodys(PushType.TIMEOUT_ALARM.name());
        Map<String, String> paramsMap = new HashMap<String, String>() {
            {
                put("timeout", count);
            }
        };
        p2PNotify.setServiceNo("1");
        p2PNotify.setParams(paramsMap);
        messageApi.send(p2PNotify);
    }


}
