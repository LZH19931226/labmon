package com.hc.scheduledfuturethread;

import com.hc.bean.ApiResponse;
import com.hc.config.RedisTemplateUtil;
import com.hc.dao.MonitorequipmentDao;
import com.hc.entity.Monitorequipment;
import com.hc.entity.Monitorequipmentlastdata;
import com.hc.interfaceService.InterfaceMonService;
import com.hc.service.SendMesService;
import com.hc.utils.JsonUtil;
import com.hc.utils.TimeHelper;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;

import java.util.Date;
import java.util.List;

/**
 * @author LiuZhiHao
 * @date 2020/5/22 16:39
 * 描述:
 **/
public class MyRunnable1 implements Runnable {
    @Autowired
    private MonitorequipmentDao monitorequipmentDao;
    @Autowired
    private RedisTemplateUtil redisTemplateUtil;
    @Autowired
    private SendMesService sendMesService;
//    @Autowired
//    private InterfaceMonService interfaceMonService;
    @Override
    public void run() {
        // 查询当前医院所有设备当前值
//        ApiResponse<List<Monitorequipment>> equipmentInfoByHospitalcode = interfaceMonService.getEquipmentInfoByHospitalcode();
//        List<Monitorequipment> result = equipmentInfoByHospitalcode.getResult();
        List<Monitorequipment> monitorequipmentByHospitalcode = monitorequipmentDao.getMonitorequipmentByHospitalcode();
        if (CollectionUtils.isEmpty(monitorequipmentByHospitalcode)){
            return;
        }
        int total = 0;//设备总数
        int normaltotal = 0; // 正常设备总数
        int abnormal = 0;// 异常设备总数



        for (Monitorequipment monitorequipment : monitorequipmentByHospitalcode){
            total++;
            //获取数据
            String equipmentno = monitorequipment.getEquipmentno();
            String hospitalcode = monitorequipment.getHospitalcode();
            HashOperations<Object, Object, Object> objectObjectObjectHashOperations = redisTemplateUtil.opsForHash();
            String lastdata = (String) objectObjectObjectHashOperations.get("LASTDATA"+hospitalcode, monitorequipment.getEquipmentno());
            if (StringUtils.isNotEmpty(lastdata)) {
                Monitorequipmentlastdata monitorequipmentlastdata = JsonUtil.toBean(lastdata, Monitorequipmentlastdata.class);
                //时间判断，是否超时
                Date inputdatetime = monitorequipmentlastdata.getInputdatetime();
                double datePoorMin = TimeHelper.getDatePoorMin(new Date(), inputdatetime);
                String equipmenttypeid = monitorequipment.getEquipmenttypeid();
                int time = 31;

                if (StringUtils.equals(equipmenttypeid,"3")){
                    time = 65;
                }
                if (datePoorMin > time){
                    //数据超时
                    abnormal++;
                }else {
                    normaltotal++;
                }

            }
        }
        //发送短信给信息科老师
        Integer totals = total;
        Integer normaltotals = normaltotal;
        Integer abnormals = abnormal;
      //  sendMesService.timingsms(totals.toString(),normaltotals.toString(),abnormals.toString());

    }
}
