package com.hc.scheduledfuturethread;

import com.hc.config.RedisTemplateUtil;
import com.hc.dao.MonitorequipmentDao;
import com.hc.entity.Monitorequipment;
import com.hc.entity.Monitorequipmentlastdata;
import com.hc.service.SendMesService;
import com.hc.utils.JsonUtil;
import com.hc.utils.TimeHelper;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by 15350 on 2020/5/26.
 */
@Component
public class TimingService {
    @Autowired
    private MonitorequipmentDao monitorequipmentDao;
    @Autowired
    private RedisTemplateUtil redisTemplateUtil;
    @Autowired
    private SendMesService sendMesService;
    @Value("${timing.phone1}")
    private String phone1;
    @Value("${timing.phone2}")
    private String phone2;
    @Scheduled(cron = "0 0 20 * * *")
    public void timing(){
        List<Monitorequipment> monitorequipmentByHospitalcode = monitorequipmentDao.getMonitorequipmentByHospitalcode();
        if (CollectionUtils.isEmpty(monitorequipmentByHospitalcode)){
            return;
        }
        int total = 0;//设备总数
        int normaltotal = 0; // 正常设备总数
        int abnormal = 0;// 异常设备总数
        List<String> list = new ArrayList<>();


        for (Monitorequipment monitorequipment : monitorequipmentByHospitalcode){
            total++;
            //获取数据
            String equipmentno = monitorequipment.getEquipmentno();
            HashOperations<Object, Object, Object> objectObjectObjectHashOperations = redisTemplateUtil.opsForHash();
            String lastdata = (String) objectObjectObjectHashOperations.get("LASTDATA", monitorequipment.getEquipmentno());
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
                    switch (equipmenttypeid){
                        case "1":
                            if (!list.contains("环境")){
                                list.add("环境");
                            }
                            break;
                        case "2":
                            if (!list.contains("培养箱")){
                                list.add("培养箱");
                            }
                            break;
                        case "3":
                            if (!list.contains("液氮罐")){
                                list.add("液氮罐");
                            }
                            break;
                        case "4":
                            if (!list.contains("冰箱")){
                                list.add("冰箱");
                            }
                            break;
                        case "5":
                            if (!list.contains("操作台")){
                                list.add("操作台");
                            }
                            break;
                        case "6":
                            if (!list.contains("市电")){
                                list.add("市电");
                            }
                            break;
                    }


                }else {
                    normaltotal++;
                }

            }
        }
        //发送短信给信息科老师
        Integer totals = total;
        Integer normaltotals = normaltotal;
        Integer abnormals = abnormal;
        String type = "";
        if (CollectionUtils.isNotEmpty(list)){
            String join = StringUtils.join(list, "-");
            type = join;
        }else {
            type = "无";
        }
        sendMesService.timingsms(totals.toString(),normaltotals.toString(),abnormals.toString(),phone1,type);
        sendMesService.timingsms(totals.toString(),normaltotals.toString(),abnormals.toString(),phone2,type);
    }


}
