package com.hc.service.serviceimpl;

import com.hc.mapper.OperationlogDao;
import com.hc.mapper.OperationlogdetailDao;
import com.hc.entity.Operationlog;
import com.hc.entity.Operationlogdetail;
import com.hc.mapper.laboratoryFrom.OperationlogdetaiMapper;
import com.hc.model.EquipmentInfoModel;
import com.hc.model.InstrumentInfoModel;
import com.hc.service.OperationlogService;
import com.hc.service.UpdateRecordService;
import com.hc.utils.JsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by 15350 on 2020/5/21.
 */
@Service
public class UpdateRecordServiceImpl implements UpdateRecordService {
    @Autowired
    private OperationlogdetailDao operationlogdetailDao;
    @Autowired
    private OperationlogDao operationlogDao;
    private static final Logger LOGGER = LoggerFactory.getLogger(UpdateRecordServiceImpl.class);

    @Override
    @Async
    public void updateEquipmentMonitor(String equipmentnames, String hospitalname, String name, EquipmentInfoModel oldEquipmentInfoModel, EquipmentInfoModel nowEquipmentInfoModel, String type, String operationType) {
            LOGGER.info("当前值："+ JsonUtil.toJson(nowEquipmentInfoModel + " 历史值："+JsonUtil.toJson(oldEquipmentInfoModel)));
            List<Operationlogdetail> operationlogdetails = new ArrayList<>();
            boolean flag = false;
            String equipmentname = oldEquipmentInfoModel.getEquipmentname();//设备名称
            String equipmentname1 = nowEquipmentInfoModel.getEquipmentname();//当前设备名称
            if (!StringUtils.equals(equipmentname1, equipmentname)) {
                flag = true;
                Operationlogdetail operationlogdetail = new Operationlogdetail();
                operationlogdetail.setFiledname("equipmentname");
                operationlogdetail.setFiledcaption("设备名称");
                operationlogdetail.setFiledvalue(equipmentname1);//当前值
                operationlogdetail.setFiledvalueprev(equipmentname);//历史值
                operationlogdetails.add(operationlogdetail);
            }
            Boolean clientvisible = oldEquipmentInfoModel.isClientvisible();//是否可用
            if (null == clientvisible) {
                clientvisible = false;
            }
            Boolean clientvisible1 = nowEquipmentInfoModel.isClientvisible();//是否可用
            if (null == clientvisible1) {
                clientvisible1 = false;
            }
            if (! (clientvisible && clientvisible1)) {
                flag = true;
                String old = "可用";
                String now = "可用";
                if (!clientvisible) {
                    old = "不可用";
                }
                if (!clientvisible1) {
                    now = "不可用";
                }
                Operationlogdetail operationlogdetail = new Operationlogdetail();
                operationlogdetail.setFiledname("clientvisible");
                operationlogdetail.setFiledcaption("是否可用");
                operationlogdetail.setFiledvalue(now);//当前值
                operationlogdetail.setFiledvalueprev(old);//历史值
                operationlogdetails.add(operationlogdetail);
            }
            if (flag) {
                LOGGER.info("执行日志操作");
                Operationlog operationlog = new Operationlog();
                operationlog.setFunctionname("设备管理");
                operationlog.setHospitalname(hospitalname);
                operationlog.setOpeartiontype(operationType);
                operationlog.setOperationtime(new Date());
                operationlog.setTablename("monitorequipment");
                operationlog.setUsername(name);
                operationlog.setEquipmentname(equipmentnames);
                operationlog.setPlatform(type);
                //operationlogService.addOperationLogInfo(operationlog, operationlogdetails);
                operationlog.setLogid(UUID.randomUUID().toString().replaceAll("-", ""));
                operationlogDao.saveAndFlush(operationlog);
                //执行日志信息表插入操作
                for (Operationlogdetail operationlogdetail : operationlogdetails) {
                    operationlogdetail.setDetailid(UUID.randomUUID().toString().replaceAll("-", ""));
                    operationlogdetail.setLogid(operationlog.getLogid());
                    operationlogdetailDao.saveAndFlush(operationlogdetail);
                }
            }
    }

    @Override
    @Async
    @Transactional(rollbackOn = Exception.class)
    public void updateInstrumentMonitor(String instrumentname,String equipmentname, String hospitalname, String name, InstrumentInfoModel oldInstrumentInfoModel, InstrumentInfoModel nowInstrumentInfoModel, String type, String operationType) {
            List<Operationlogdetail> operationlogdetails = new ArrayList<>();
            boolean flag = false;
            String sn = oldInstrumentInfoModel.getSn();
            String sn1 = nowInstrumentInfoModel.getSn();
            if (!StringUtils.equals(sn1, sn)) {
                flag = true;
                Operationlogdetail operationlogdetail = new Operationlogdetail();
                operationlogdetail.setFiledname("sn");
                operationlogdetail.setFiledcaption("SN");
                operationlogdetail.setFiledvalue(sn1);//当前值
                operationlogdetail.setFiledvalueprev(sn);//历史值
                operationlogdetail.setComment(instrumentname);
                operationlogdetails.add(operationlogdetail);
            }

            BigDecimal lowlimit = oldInstrumentInfoModel.getLowlimit();//最低限值

            BigDecimal lowlimit1 = nowInstrumentInfoModel.getLowlimit();
            if (lowlimit != null && lowlimit1 != null) {
                if (lowlimit.compareTo(lowlimit1) != 0) {
                    //值发生了改变
                    flag = true;
                    Operationlogdetail operationlogdetail = new Operationlogdetail();
                    operationlogdetail.setFiledname("lowlimit");
                    operationlogdetail.setFiledcaption("最低值");
                    operationlogdetail.setFiledvalue(lowlimit1.toString());//当前值
                    operationlogdetail.setFiledvalueprev(lowlimit.toString());//历史值
                    operationlogdetail.setComment(instrumentname);
                    operationlogdetails.add(operationlogdetail);
                }
            } else {
                //有值存在空
                flag = true;
                Operationlogdetail operationlogdetail = new Operationlogdetail();
                operationlogdetail.setFiledname("lowlimit");
                operationlogdetail.setFiledcaption("最低值");
                if (null != lowlimit1) {
                    operationlogdetail.setFiledvalue(lowlimit1.toString());//当前值
                }
                if (null != lowlimit) {
                    operationlogdetail.setFiledvalueprev(lowlimit.toString());//历史值
                }
                operationlogdetails.add(operationlogdetail);
            }


            BigDecimal highlimit = oldInstrumentInfoModel.getHighlimit();//最高限值
            BigDecimal highlimit1 = nowInstrumentInfoModel.getHighlimit();//最高限值（当前）
            if (highlimit != null && highlimit1 != null) {
                if (highlimit.compareTo(highlimit1) != 0) {
                    //值发生了改变
                    flag = true;
                    Operationlogdetail operationlogdetail = new Operationlogdetail();
                    operationlogdetail.setFiledname("highlimit");
                    operationlogdetail.setFiledcaption("最高值");
                    operationlogdetail.setFiledvalue(highlimit1.toString());//当前值
                    operationlogdetail.setFiledvalueprev(highlimit.toString());//历史值
                    operationlogdetail.setComment(instrumentname);
                    operationlogdetails.add(operationlogdetail);
                }
            } else {
                //有值存在空
                flag = true;
                Operationlogdetail operationlogdetail = new Operationlogdetail();
                operationlogdetail.setFiledname("highlimit");
                operationlogdetail.setFiledcaption("最高值");
                if (null != highlimit1) {
                    operationlogdetail.setFiledvalue(highlimit1.toString());//当前值
                }
                if (null != highlimit) {
                    operationlogdetail.setFiledvalueprev(highlimit.toString());//历史值
                }
                operationlogdetails.add(operationlogdetail);
            }


            String calibration = oldInstrumentInfoModel.getCalibration();//矫正正负值
            String calibration1 = nowInstrumentInfoModel.getCalibration();
            if (!StringUtils.equals(calibration1, calibration)) {
                flag = true;
                Operationlogdetail operationlogdetail = new Operationlogdetail();
                operationlogdetail.setFiledname("calibration");
                operationlogdetail.setFiledcaption("校正正负值");
                operationlogdetail.setFiledvalue(calibration1);//当前值
                operationlogdetail.setFiledvalueprev(calibration);//历史值
                operationlogdetail.setComment(instrumentname);
                operationlogdetails.add(operationlogdetail);
            }
            String channel = oldInstrumentInfoModel.getChannel();//通道
            String channel1 = nowInstrumentInfoModel.getChannel();
            if (!StringUtils.equals(channel, channel1)) {
                flag = true;
                Operationlogdetail operationlogdetail = new Operationlogdetail();
                operationlogdetail.setFiledname("channel");
                operationlogdetail.setFiledcaption("通道");
                operationlogdetail.setFiledvalue(channel1);//当前值
                operationlogdetail.setFiledvalueprev(channel);//历史值
                operationlogdetail.setComment(instrumentname);
                operationlogdetails.add(operationlogdetail);
            }
            Integer alarmtime = oldInstrumentInfoModel.getAlarmtime();//智能报警次数
            if (alarmtime == null) {
                alarmtime = 0;
            }
            Integer alarmtime1 = nowInstrumentInfoModel.getAlarmtime();
            if (alarmtime1 == null) {
                alarmtime1 = 0;
            }
            if (alarmtime != alarmtime1) {
                flag = true;
                Operationlogdetail operationlogdetail = new Operationlogdetail();
                operationlogdetail.setFiledname("alarmtime");
                operationlogdetail.setFiledcaption("智能报警次数");
                operationlogdetail.setFiledvalue(channel1);//当前值
                operationlogdetail.setFiledvalueprev(channel);//历史值
                operationlogdetail.setComment(instrumentname);
                operationlogdetails.add(operationlogdetail);
            }
            String warningphone = oldInstrumentInfoModel.getWarningphone();//禁用启用报警
            String warningphone1 = nowInstrumentInfoModel.getWarningphone();
            if (!StringUtils.equals(warningphone1, warningphone)) {
                flag = true;
                Operationlogdetail operationlogdetail = new Operationlogdetail();
                operationlogdetail.setFiledname("warningphone");
                operationlogdetail.setFiledcaption("报警禁用启用");
                operationlogdetail.setFiledvalue(warningphone1);//当前值
                operationlogdetail.setFiledvalueprev(warningphone);//历史值
                operationlogdetail.setComment(instrumentname);
                operationlogdetails.add(operationlogdetail);
            }
            if (flag){
                Operationlog operationlog = new Operationlog();
                operationlog.setFunctionname("探头管理");
                operationlog.setHospitalname(hospitalname);
                operationlog.setEquipmentname(equipmentname);
                operationlog.setOpeartiontype(operationType);
                operationlog.setOperationtime(new Date());
                operationlog.setTablename("instrumentparamconfig");
                operationlog.setUsername(name);
                operationlog.setPlatform(type);
              //  operationlogService.addOperationLogInfo(operationlog, operationlogdetails);
                operationlog.setLogid(UUID.randomUUID().toString().replaceAll("-", ""));
                operationlogDao.saveAndFlush(operationlog);
                //执行日志信息表插入操作
                for (Operationlogdetail operationlogdetail : operationlogdetails) {
                    operationlogdetail.setDetailid(UUID.randomUUID().toString().replaceAll("-", ""));
                    operationlogdetail.setLogid(operationlog.getLogid());
                    operationlogdetailDao.saveAndFlush(operationlogdetail);
                }
            }

    }

}
