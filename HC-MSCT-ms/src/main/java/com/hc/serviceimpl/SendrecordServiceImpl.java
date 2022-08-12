package com.hc.serviceimpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hc.clickhouse.po.Warningrecord;
import com.hc.clickhouse.repository.WarningrecordRepository;
import com.hc.mapper.SendrecordDao;
import com.hc.model.WarningModel;
import com.hc.my.common.core.constant.enums.DictEnum;
import com.hc.my.common.core.constant.enums.ElkLogDetail;
import com.hc.my.common.core.redis.dto.HospitalInfoDto;
import com.hc.my.common.core.redis.dto.UserRightRedisDto;
import com.hc.my.common.core.util.ElkLogDetailUtil;
import com.hc.po.Sendrecord;
import com.hc.service.SendMesService;
import com.hc.service.SendrecordService;
import com.hc.utils.JsonUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class SendrecordServiceImpl extends ServiceImpl<SendrecordDao, Sendrecord> implements SendrecordService {

    @Autowired
    private SendMesService sendMesService;

    @Autowired
    private WarningrecordRepository warningrecordRepository;


    @Override
    @Async
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
            String username = userright.getUsername();
            //1为运维后台人员
            if (StringUtils.isNotEmpty(role)&&StringUtils.equals(role,"1")){
                equipmentName = hospitalName + equipmentName;
            }
            //不报警
            if (StringUtils.equals(reminders, DictEnum.UNOPENED_CONTACT_DETAILS.getCode()) || StringUtils.isEmpty(phonenum)) {
                continue;
            }
            if (StringUtils.isEmpty(reminders) || StringUtils.equals(DictEnum.PHONE_SMS.getCode(),reminders)) {
                sendMesService.callPhone(phonenum, equipmentName);
                mailCallUser.append(username).append("/");
                Sendrecord sendrecord = producePhoneRecord(phonenum, hospitalcode, equipmentName, unit, "1");
                sendrecords.add(sendrecord);
                sendMesService.sendMes(phonenum, equipmentName, unit, value);
                phoneCallUser.append(username).append("/");
                Sendrecord sendrecord1 = producePhoneRecord(phonenum, hospitalcode, equipmentName, unit, "0");
                sendrecords.add(sendrecord1);
                ElkLogDetailUtil.buildElkLogDetail(ElkLogDetail.from(ElkLogDetail.MSCT_SERIAL_NUMBER17.getCode()), JsonUtil.toJson(userright),logId);
            } else if (StringUtils.equals(reminders, DictEnum.PHONE.getCode())) {
                sendMesService.callPhone(userright.getPhoneNum(), equipmentName);
                phoneCallUser.append(username).append("/");
                Sendrecord sendrecord = producePhoneRecord(userright.getPhoneNum(), hospitalcode, equipmentName, unit, "1");
                sendrecords.add(sendrecord);
                ElkLogDetailUtil.buildElkLogDetail(ElkLogDetail.from(ElkLogDetail.MSCT_SERIAL_NUMBER15.getCode()),JsonUtil.toJson(userright),logId);
            } else if (StringUtils.equals(reminders,DictEnum.SMS.getCode())) {
                sendMesService.sendMes(userright.getPhoneNum(), equipmentName, unit, value);
                mailCallUser.append(username).append("/");
                Sendrecord sendrecord = producePhoneRecord(userright.getPhoneNum(), hospitalcode, equipmentName, unit, "0");
                sendrecords.add(sendrecord);
                ElkLogDetailUtil.buildElkLogDetail(ElkLogDetail.from(ElkLogDetail.MSCT_SERIAL_NUMBER16.getCode()),JsonUtil.toJson(userright),logId);

            }
        }
        if (CollectionUtils.isNotEmpty(sendrecords)) {
            this.saveBatch(sendrecords);
            //修改报警通知人
            Warningrecord warningrecord = warningModel.getWarningrecord();
            Warningrecord warningrecord1  = new Warningrecord();
            warningrecord1.setPkid(warningrecord.getPkid());
            warningrecord1.setMailCallUser(mailCallUser.toString());
            warningrecord1.setPhoneCallUser(phoneCallUser.toString());
            warningrecordRepository.updateWarningCallUser(warningrecord1);
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



}
