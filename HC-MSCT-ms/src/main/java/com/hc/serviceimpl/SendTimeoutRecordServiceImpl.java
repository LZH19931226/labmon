package com.hc.serviceimpl;

import com.hc.mapper.SendTimeoutRecordDao;
import com.hc.my.common.core.util.DateUtils;
import com.hc.po.SendTimeoutRecord;
import com.hc.po.Userright;
import com.hc.service.SendTimeoutRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SendTimeoutRecordServiceImpl implements SendTimeoutRecordService {

    @Autowired
    private SendTimeoutRecordDao sendTimeoutRecordDao;

    @Override
    public void saveTimeOutRecord(List<Userright> userrights, String hospitalcode, String eqTypeName, String count) {
        List<SendTimeoutRecord> list = new ArrayList<>();
        for (Userright userright : userrights) {
            SendTimeoutRecord sendTimeoutRecord = new SendTimeoutRecord();
            sendTimeoutRecord.setSendType(userright.getReminders());
            sendTimeoutRecord.setCount(count);
            sendTimeoutRecord.setEquipmentType(eqTypeName);
            sendTimeoutRecord.setHospitalCode(hospitalcode);
            sendTimeoutRecord.setPhoneNum(userright.getCode()+userright.getPhoneNum());
            sendTimeoutRecord.setCreateTime(DateUtils.getNowDate());
            list.add(sendTimeoutRecord);
        }
        sendTimeoutRecordDao.insertBatchSomeColumn(list);
    }
}
