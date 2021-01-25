package com.hc.scheduledfuturethread;

import com.hc.config.RedisTemplateUtil;
import com.hc.dao.MonitorequipmentDao;
import com.hc.dao.WarningrecordSortDao;
import com.hc.entity.*;
import com.hc.service.SendMesService;
import com.hc.utils.JsonUtil;
import com.hc.utils.TimeHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by 15350 on 2020/5/26.
 */
@Component
@Slf4j
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
    @Autowired
    private WarningrecordSortDao warningrecordSortDao;


    @Scheduled(cron = "0 0 20 * * *")
    public void timing() {
        List<Monitorequipment> monitorequipmentByHospitalcode = monitorequipmentDao.getMonitorequipmentByHospitalcode();
        if (CollectionUtils.isEmpty(monitorequipmentByHospitalcode)) {
            return;
        }
        int total = 0;//设备总数
        int normaltotal = 0; // 正常设备总数
        int abnormal = 0;// 异常设备总数
        List<String> list = new ArrayList<>();


        for (Monitorequipment monitorequipment : monitorequipmentByHospitalcode) {
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

                if (StringUtils.equals(equipmenttypeid, "3")) {
                    time = 65;
                }
                if (datePoorMin > time) {
                    //数据超时
                    abnormal++;
                    switch (equipmenttypeid) {
                        case "1":
                            if (!list.contains("环境")) {
                                list.add("环境");
                            }
                            break;
                        case "2":
                            if (!list.contains("培养箱")) {
                                list.add("培养箱");
                            }
                            break;
                        case "3":
                            if (!list.contains("液氮罐")) {
                                list.add("液氮罐");
                            }
                            break;
                        case "4":
                            if (!list.contains("冰箱")) {
                                list.add("冰箱");
                            }
                            break;
                        case "5":
                            if (!list.contains("操作台")) {
                                list.add("操作台");
                            }
                            break;
                        case "6":
                            if (!list.contains("市电")) {
                                list.add("市电");
                            }
                            break;
                        default:
                            break;
                    }


                } else {
                    normaltotal++;
                }

            }
        }
        //发送短信给信息科老师
        Integer totals = total;
        Integer normaltotals = normaltotal;
        Integer abnormals = abnormal;
        String type = "";
        if (CollectionUtils.isNotEmpty(list)) {
            String join = StringUtils.join(list, "-");
            type = join;
        } else {
            type = "无";
        }
        sendMesService.timingsms(totals.toString(), normaltotals.toString(), abnormals.toString(), phone1, type);
        sendMesService.timingsms(totals.toString(), normaltotals.toString(), abnormals.toString(), phone2, type);
    }


    @Scheduled(cron = "0 */9 * * * ?")
    public void zfbwarningRuleSend() {
        String hospitalcode = "5e6d5037c93a4d619368553b09f5a657";
        List<WarningrecordSort> warRead = warningrecordSortDao.getWarningrecordSortByHospitalcodeAAndisRead(hospitalcode);
        if (CollectionUtils.isNotEmpty(warRead)) {
            Map<String, List<WarningrecordSort>> collect = warRead.stream().collect(Collectors.groupingBy(WarningrecordSort::getWarningStatus));
            BoundHashOperations<Object, Object, Object> hospitalphonenum = redisTemplateUtil.boundHashOps("hospital:phonenum");
            String o = (String) hospitalphonenum.get(hospitalcode);
            List<Userright> userrights = JsonUtil.toList(o, Userright.class);
            if (CollectionUtils.isEmpty(userrights)) {
                return;
            }
            //获取报警序号得联系人员
            Map<Integer, Userright> userrightMap = new HashMap<>();
            userrights.forEach(s -> {
                String username = s.getUsername();
                if (StringUtils.isNotEmpty(username)) {
                    String lastString = username.substring(username.length() - 1, username.length());
                    if (StringUtils.equalsAny(lastString, "1","2","3","4","5","6","7","8","9")) {
                        userrightMap.put(Integer.parseInt(lastString), s);
                    }

                }
            });
            Set<Integer> integers = userrightMap.keySet();
            List<Integer> collect1 = integers.stream().sorted(Integer::compareTo).collect(Collectors.toList());
            List<WarningrecordSort> warningrecordSorts = new ArrayList<>();
            collect.forEach((k, v) -> {
                switch (k) {
                    case "1":
                        v.forEach(s -> {
                            Date inputdatetime = s.getInputdatetime();
                            double datePoor = TimeHelper.getDatePoor(inputdatetime, new Date());
                            //大于5分钟未确认 给下一位电话 同时修改状态
                            if (datePoor >= 10) {
                                Integer integer = collect1.get(1);
                                Userright userright = userrightMap.get(integer);
                                sendMesService.callPhone(userright.getPhonenum(), s.getEquipmentname());
                                s.setWarningStatus("2");
                                warningrecordSorts.add(s);
                            }
                        });
                        break;
                    case "2":
                        v.forEach(s -> {
                            Date inputdatetime = s.getInputdatetime();
                            double datePoor = TimeHelper.getDatePoor(inputdatetime, new Date());
                            //大于5分钟未确认 给下一位电话 同时修改状态
                            if (datePoor >= 20) {
                                Integer integer = collect1.get(2);
                                Userright userright = userrightMap.get(integer);
                                sendMesService.callPhone(userright.getPhonenum(), s.getEquipmentname());
                                s.setWarningStatus("3");
                                warningrecordSorts.add(s);
                            }
                        });
                        break;
                    case "3":
                        v.forEach(s -> {
                            Date inputdatetime = s.getInputdatetime();
                            double datePoor = TimeHelper.getDatePoor(inputdatetime, new Date());
                            //大于5分钟未确认 给下一位电话 同时修改状态
                            if (datePoor >= 30) {
                                Integer integer = collect1.get(3);
                                Userright userright = userrightMap.get(integer);
                                sendMesService.callPhone(userright.getPhonenum(), s.getEquipmentname());
                                s.setWarningStatus("4");
                                warningrecordSorts.add(s);
                            }
                        });
                        break;
                    case "4":
                        v.forEach(s -> {
                            Date inputdatetime = s.getInputdatetime();
                            double datePoor = TimeHelper.getDatePoor(inputdatetime, new Date());
                            //大于20分钟未确认 给下一位电话 同时修改状态
                            if (datePoor >= 40) {
                                Integer integer = collect1.get(4);
                                Userright userright = userrightMap.get(integer);
                                sendMesService.callPhone(userright.getPhonenum(), s.getEquipmentname());
                                s.setWarningStatus("5");
                                warningrecordSorts.add(s);
                            }
                        });
                        break;
                    case "5":
                        v.forEach(s -> {
                            Date inputdatetime = s.getInputdatetime();
                            double datePoor = TimeHelper.getDatePoor(inputdatetime, new Date());
                            if (datePoor >= 50) {
                                //都未确认给所有人拨打电话
                                for (Userright userright : userrights) {
                                    String phonenum = userright.getPhonenum();
                                    if (StringUtils.isNotEmpty(phonenum)) {
                                        sendMesService.callPhone(userright.getPhonenum(), s.getEquipmentname());
                                    }
                                }
                                s.setIsRead("1");
                                warningrecordSorts.add(s);
                            }
                        });
                        break;
                    default:
                        break;
                }
            });
            warningrecordSortDao.save(warningrecordSorts);
        }

    }
}
