package com.hc.service.serviceimpl;

import com.hc.config.RedisTemplateUtil;
import com.hc.dao.HospitalofreginfoDao;
import com.hc.entity.Hospitalofreginfo;
import com.hc.mapper.laboratoryFrom.HospitalInfoMapper;
import com.hc.model.AbnormalDataModel;
import com.hc.model.ResponseModel.*;
import com.hc.service.AlarmNumberService;
import com.hc.service.AlarmService;
import com.hc.units.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by xxf on 2018/9/28.
 */
@Service
public class AlarmServiceImpl implements AlarmService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AlarmServiceImpl.class);
    @Autowired
    private HospitalInfoMapper hospitalInfoMapper;
    @Autowired
    private RedisTemplateUtil redisTemplateUtil;
    @Autowired
    private HospitalofreginfoDao hospitalofreginfoDao;
    @Autowired
    private AlarmNumberService alarmNumberService;

    @Override
    public ApiResponse<List<AlarmHospitalInfo>> showAllHospitalAbInfo() {
        ApiResponse<List<AlarmHospitalInfo>> apiResponse = new ApiResponse<List<AlarmHospitalInfo>>();
        List<AlarmHospitalInfo> alarmHospitalInfos = new ArrayList<AlarmHospitalInfo>();
        List<Hospitalofreginfo> all = hospitalofreginfoDao.findAll();
        //   LOGGER.info("所有医院信息：" + JsonUtil.toJson(all));
        HashOperations<Object, Object, Object> objectObjectObjectHashOperations = redisTemplateUtil.opsForHash();
        List<String> list = new ArrayList<String>();
        list.add("A");
        list.add("B");
        list.add("C");
        list.add("D");
        list.add("E");
        list.add("F");
        if (redisTemplateUtil.hasKey("abnormal")) {
            redisTemplateUtil.delete("abnormal");
        }
        List<AbnormalDataModel> list1 = new ArrayList<>();
        try {
            for (Hospitalofreginfo hospitalofreginfo : all) {
                try {
                    if (redisTemplateUtil.hasKey(hospitalofreginfo.getHospitalcode() + "+1")) {
                        redisTemplateUtil.delete(hospitalofreginfo.getHospitalcode() + "+1");
                    }
                    if (redisTemplateUtil.hasKey(hospitalofreginfo.getHospitalcode() + "+2")) {
                        redisTemplateUtil.delete(hospitalofreginfo.getHospitalcode() + "+2");
                    }
                    if (redisTemplateUtil.hasKey(hospitalofreginfo.getHospitalcode() + "+3")) {
                        redisTemplateUtil.delete(hospitalofreginfo.getHospitalcode() + "+3");
                    }
                    if (redisTemplateUtil.hasKey(hospitalofreginfo.getHospitalcode() + "+4")) {
                        redisTemplateUtil.delete(hospitalofreginfo.getHospitalcode() + "+4");
                    }
                    if (redisTemplateUtil.hasKey(hospitalofreginfo.getHospitalcode() + "+5")) {
                        redisTemplateUtil.delete(hospitalofreginfo.getHospitalcode() + "+5");
                    }
                    if (redisTemplateUtil.hasKey(hospitalofreginfo.getHospitalcode() + "+qc")) {
                        redisTemplateUtil.delete(hospitalofreginfo.getHospitalcode() + "+qc");
                    }
                } catch (Exception e) {
                    LOGGER.error("redis服务异常，原因：" + e.getMessage());
                    apiResponse.setCode(ApiResponse.FAILED);
                    apiResponse.setMessage("redis服务异常");
                    return apiResponse;
                }
                //   LOGGER.info("服务异常1" + JsonUtil.toJson(hospitalofreginfo));
                AlarmHospitalInfo alarmHospitalInfo = new AlarmHospitalInfo();
                String hospitalcode = hospitalofreginfo.getHospitalcode();
                alarmHospitalInfo.setHospitalcode(hospitalcode);
                String hospitalName = hospitalofreginfo.getHospitalname();
                alarmHospitalInfo.setHospitalname(hospitalName);
                //    LOGGER.info("服务异常2" + JsonUtil.toJson(hospitalofreginfo));
                // 查找当前医院的所有探头信息
                List<AlarmEquipmentInfo> allAlarmEquipmentInfo = hospitalInfoMapper.getAllAlarmEquipmentInfo(hospitalcode);
                //     LOGGER.info("服务异常3" + JsonUtil.toJson(hospitalofreginfo));
                //     LOGGER.info("医院信息：" + JsonUtil.toJson(allAlarmEquipmentInfo));
//            if (CollectionUtils.isEmpty(alarmHospitalInfos)) {
//                break;
//            }
                AlarmEquipmentTypeInfo alarmEquipmentTypeInfo1 = new AlarmEquipmentTypeInfo();
                alarmEquipmentTypeInfo1.setEquipmenttypeid(1);
                AlarmEquipmentTypeInfo alarmEquipmentTypeInfo2 = new AlarmEquipmentTypeInfo();
                alarmEquipmentTypeInfo2.setEquipmenttypeid(2);
                AlarmEquipmentTypeInfo alarmEquipmentTypeInfo3 = new AlarmEquipmentTypeInfo();
                alarmEquipmentTypeInfo3.setEquipmenttypeid(3);
                AlarmEquipmentTypeInfo alarmEquipmentTypeInfo4 = new AlarmEquipmentTypeInfo();
                alarmEquipmentTypeInfo4.setEquipmenttypeid(4);
                AlarmEquipmentTypeInfo alarmEquipmentTypeInfo5 = new AlarmEquipmentTypeInfo();
                alarmEquipmentTypeInfo5.setEquipmenttypeid(5);
                AlarmEquipmentTypeInfo alarmEquipmentTypeInfo6 = new AlarmEquipmentTypeInfo();
                alarmEquipmentTypeInfo6.setEquipmenttypeid(6);
                for (AlarmEquipmentInfo alarmEquipmentInfo : allAlarmEquipmentInfo) {
                    String equipmentTypeID = alarmEquipmentInfo.getEquipmenttypeid();
                    if (equipmentTypeID == null) {
                        continue;
                    }
                    // 确认是哪一个设备类型
                    switch (alarmEquipmentInfo.getEquipmenttypeid()) {
                        case "1":
                            try {
                                alarmNumberService.equipmentType1(hospitalofreginfo.getHospitalname(), hospitalofreginfo.getHospitalcode(), alarmEquipmentInfo.getEquipmentno(), alarmEquipmentInfo.getEquipmentname(), alarmEquipmentTypeInfo1);
                            } catch (Exception e) {
                                LOGGER.error("服务异常，原因：" + e.getMessage());
                                apiResponse.setCode(ApiResponse.FAILED);
                                apiResponse.setMessage("环境服务异常");
                                return apiResponse;
                            }
                            break;
                        case "2":

                            alarmNumberService.equipmentType2(hospitalofreginfo.getHospitalname(), hospitalofreginfo.getHospitalcode(), alarmEquipmentInfo.getEquipmentno(), alarmEquipmentInfo.getEquipmentname(), alarmEquipmentTypeInfo2, list);

                            break;
                        case "3":
                            alarmNumberService.equipmentType3(hospitalofreginfo.getHospitalname(), hospitalofreginfo.getHospitalcode(), alarmEquipmentInfo.getEquipmentno(), alarmEquipmentInfo.getEquipmentname(), alarmEquipmentTypeInfo3, list);
                            break;
                        case "4":
                            alarmNumberService.equipmentType4(hospitalofreginfo.getHospitalname(), hospitalofreginfo.getHospitalcode(), alarmEquipmentInfo.getEquipmentno(), alarmEquipmentInfo.getEquipmentname(), alarmEquipmentTypeInfo4, list);
                            break;
                        case "5":
                            alarmNumberService.equipmentType5(hospitalofreginfo.getHospitalname(), hospitalofreginfo.getHospitalcode(), alarmEquipmentInfo.getEquipmentno(), alarmEquipmentInfo.getEquipmentname(), alarmEquipmentTypeInfo5, list);
                            break;
                        case "6":
                            try {
                                alarmNumberService.equipmentType6(alarmEquipmentInfo.getEquipmentno(), alarmEquipmentInfo.getEquipmentname(), alarmEquipmentTypeInfo6);
                            } catch (Exception e) {
                                LOGGER.error("服务异常，原因：" + e.getMessage());
                                apiResponse.setCode(ApiResponse.FAILED);
                                apiResponse.setMessage("市电服务异常");
                                return apiResponse;
                            }
                            break;
                    }
                }
                try {
                    if (alarmEquipmentTypeInfo1.getNumber() != 0) {
                        if (alarmEquipmentTypeInfo1.getDatanone() != 0 || alarmEquipmentTypeInfo1.getDataovertime() != 0 || alarmEquipmentTypeInfo1.getInstrumentab() != 0) {
                            alarmHospitalInfo.setUnusual(true);
                        }
                        alarmHospitalInfo.setEnvir(alarmEquipmentTypeInfo1);
                    }
                } catch (Exception e) {
                    LOGGER.error("服务异常，原因：" + e.getMessage() + JsonUtil.toJson(alarmEquipmentTypeInfo1));
                    apiResponse.setCode(ApiResponse.FAILED);
                    apiResponse.setMessage("环境服务异常");
                    return apiResponse;
                }
                try {
                    if (alarmEquipmentTypeInfo2.getNumber() != 0) {
                        if (alarmEquipmentTypeInfo2.getDatanone() != 0 || alarmEquipmentTypeInfo2.getDataovertime() != 0 || alarmEquipmentTypeInfo2.getInstrumentab() != 0) {
                            alarmHospitalInfo.setUnusual(true);
                        }
                        alarmHospitalInfo.setIncubator(alarmEquipmentTypeInfo2);
                    }
                } catch (Exception e) {
                    LOGGER.error("服务异常，原因：" + e.getMessage() + JsonUtil.toJson(alarmEquipmentTypeInfo1));
                    apiResponse.setCode(ApiResponse.FAILED);
                    apiResponse.setMessage("培养箱服务异常");
                    return apiResponse;
                }
                try {
                    if (alarmEquipmentTypeInfo3.getNumber() != 0) {
                        if (alarmEquipmentTypeInfo3.getDatanone() != 0 || alarmEquipmentTypeInfo3.getDataovertime() != 0 || alarmEquipmentTypeInfo3.getInstrumentab() != 0) {
                            alarmHospitalInfo.setUnusual(true);
                        }
                        alarmHospitalInfo.setNitrogen(alarmEquipmentTypeInfo3);
                    }
                } catch (Exception e) {
                    LOGGER.error("服务异常，原因：" + e.getMessage() + JsonUtil.toJson(alarmEquipmentTypeInfo1));
                    apiResponse.setCode(ApiResponse.FAILED);
                    apiResponse.setMessage("液氮罐服务异常");
                    return apiResponse;
                }

                if (alarmEquipmentTypeInfo4.getNumber() != 0) {
                    if (alarmEquipmentTypeInfo4.getDatanone() != 0 || alarmEquipmentTypeInfo4.getDataovertime() != 0 || alarmEquipmentTypeInfo4.getInstrumentab() != 0) {
                        alarmHospitalInfo.setUnusual(true);
                    }
                    alarmHospitalInfo.setIcebox(alarmEquipmentTypeInfo4);
                }
                if (alarmEquipmentTypeInfo5.getNumber() != 0) {
                    if (alarmEquipmentTypeInfo5.getDatanone() != 0 || alarmEquipmentTypeInfo5.getDataovertime() != 0 || alarmEquipmentTypeInfo5.getInstrumentab() != 0) {
                        alarmHospitalInfo.setUnusual(true);
                    }
                    alarmHospitalInfo.setFloor(alarmEquipmentTypeInfo5);

                }
                try {
                    if (alarmEquipmentTypeInfo6.getNumber() != 0) {
                        if (StringUtils.equals("1", alarmEquipmentTypeInfo6.getType())) {
                            alarmHospitalInfo.setUnusual(true);
                        }
                        alarmHospitalInfo.setUps(alarmEquipmentTypeInfo6);

                    }
                    if (redisTemplateUtil.hasKey(hospitalcode + "+qc")) {
                        long size = (long) redisTemplateUtil.boundListOps(hospitalcode + "+qc").size();
                        int size1 = (int) size;

                        alarmHospitalInfo.setMt100(size1);
                    }
                    alarmHospitalInfos.add(alarmHospitalInfo);
                } catch (Exception e) {
                    LOGGER.error("服务异常，原因：" + e.getMessage() + JsonUtil.toJson(alarmEquipmentTypeInfo1));
                    apiResponse.setCode(ApiResponse.FAILED);
                    apiResponse.setMessage("市电服务异常");
                    return apiResponse;
                }

            }

            apiResponse.setResult(alarmHospitalInfos);

            return apiResponse;
        } catch (Exception e) {
            LOGGER.error("服务异常，原因：" + e.getMessage());
            apiResponse.setCode(ApiResponse.FAILED);
            apiResponse.setMessage("服务异常");
            return apiResponse;
        }
    }

    @Override
    public ApiResponse<List<AlarmData>> showHospitalAlarmStatistics(String hospitalcode, String equipmenttypeid, String type) {
        ApiResponse<List<AlarmData>> apiResponse = new ApiResponse<List<AlarmData>>();
        List<Object> list = new ArrayList<Object>();
        try {
            if (StringUtils.isEmpty(equipmenttypeid)) {
                if (redisTemplateUtil.hasKey(hospitalcode + "+1")) {
                    BoundListOperations listRedisTemplate = redisTemplateUtil.boundListOps(hospitalcode + "+1");
                    List<Object> range = listRedisTemplate.range(0, -1);
                    list.addAll(range);
                }
                if (redisTemplateUtil.hasKey(hospitalcode + "+2")) {
                    BoundListOperations listRedisTemplate = redisTemplateUtil.boundListOps(hospitalcode + "+2");
                    List<Object> range = listRedisTemplate.range(0, -1);
                    list.addAll(range);
                }
                if (redisTemplateUtil.hasKey(hospitalcode + "+3")) {
                    BoundListOperations listRedisTemplate = redisTemplateUtil.boundListOps(hospitalcode + "+3");
                    List<Object> range = listRedisTemplate.range(0, -1);
                    //     LOGGER.info("range:" + JsonUtil.toJson(range));
                    list.addAll(range);
                }
                if (redisTemplateUtil.hasKey(hospitalcode + "+4")) {
                    BoundListOperations listRedisTemplate = redisTemplateUtil.boundListOps(hospitalcode + "+4");
                    List<Object> range = listRedisTemplate.range(0, -1);
                    list.addAll(range);
                }
                if (redisTemplateUtil.hasKey(hospitalcode + "+5")) {
                    BoundListOperations listRedisTemplate = redisTemplateUtil.boundListOps(hospitalcode + "+5");
                    List<Object> range = listRedisTemplate.range(0, -1);
                    list.addAll(range);
                }

            } else {
                if (redisTemplateUtil.hasKey(hospitalcode + "+" + equipmenttypeid)) {
                    List<Object> range = redisTemplateUtil.boundListOps(hospitalcode + "+" + equipmenttypeid).range(0, -1);
                    list.addAll(range);
                }
            }
            List<AlarmData> list1 = new ArrayList<AlarmData>();
            for (Object o : list) {
                String s = (String) o;
                AlarmData alarmData = JsonUtil.toBean(s, AlarmData.class);
                if ("0".equals(type)) {
                    list1.add(alarmData);
                } else {
                    if (StringUtils.equals(type, alarmData.getType())) {
                        list1.add(alarmData);
                    }
                }

            }
            apiResponse.setResult(list1);
            return apiResponse;
        } catch (Exception e) {
            LOGGER.error("服务异常，原因：" + e.getMessage());
            apiResponse.setCode(ApiResponse.FAILED);
            apiResponse.setMessage("服务异常");
            return apiResponse;
        }
    }

    @Override
    public ApiResponse<List<ShowData>> showAllData() {
        ApiResponse<List<ShowData>> apiResponse = new ApiResponse<List<ShowData>>();
        List<ShowData> list = new ArrayList<ShowData>();
        try {
            List<Hospitalofreginfo> all = hospitalofreginfoDao.findAll();
            HashOperations<Object, Object, Object> objectObjectObjectHashOperations = redisTemplateUtil.opsForHash();
            for (Hospitalofreginfo hospitalofreginfo : all) {
                List<AlarmEquipmentInfo> allAlarmEquipmentInfo = hospitalInfoMapper.getAllAlarmEquipmentInfo(hospitalofreginfo.getHospitalcode());
                for (AlarmEquipmentInfo alarmEquipmentInfo : allAlarmEquipmentInfo) {
                    String equipmenttypeid = alarmEquipmentInfo.getEquipmenttypeid();
                    if (equipmenttypeid == null) {
                        continue;
                    }
                    switch (alarmEquipmentInfo.getEquipmenttypeid()) {

                        case "1":
                            List<ShowData> one = alarmNumberService.one(alarmEquipmentInfo.getHospitalname(), alarmEquipmentInfo.getEquipmentname(), alarmEquipmentInfo.getEquipmentno());
                            if (CollectionUtils.isNotEmpty(one)) {
                                list.addAll(one);
                            }
                            break;
                        case "2":
                            List<ShowData> two = alarmNumberService.two(alarmEquipmentInfo.getHospitalname(), alarmEquipmentInfo.getEquipmentname(), alarmEquipmentInfo.getEquipmentno());
                            if (CollectionUtils.isNotEmpty(two)) {
                                list.addAll(two);
                            }
                            break;
                        case "3":
                            List<ShowData> three = alarmNumberService.three(alarmEquipmentInfo.getHospitalname(), alarmEquipmentInfo.getEquipmentname(), alarmEquipmentInfo.getEquipmentno());
                            if (CollectionUtils.isNotEmpty(three)) {
                                list.addAll(three);
                            }
                            break;
                        case "4":
                            List<ShowData> four = alarmNumberService.four(alarmEquipmentInfo.getHospitalname(), alarmEquipmentInfo.getEquipmentname(), alarmEquipmentInfo.getEquipmentno());
                            if (CollectionUtils.isNotEmpty(four)) {
                                list.addAll(four);
                            }
                            break;
                        case "5":
                            List<ShowData> five = alarmNumberService.five(alarmEquipmentInfo.getHospitalname(), alarmEquipmentInfo.getEquipmentname(), alarmEquipmentInfo.getEquipmentno());
                            if (CollectionUtils.isNotEmpty(five)) {
                                list.addAll(five);
                            }
                            break;
                        case "6":
                            List<ShowData> six = alarmNumberService.six(alarmEquipmentInfo.getHospitalname(), alarmEquipmentInfo.getEquipmentname(), alarmEquipmentInfo.getEquipmentno());
                            if (CollectionUtils.isNotEmpty(six)) {
                                list.addAll(six);
                            }
                            break;
                    }
                }
            }
            // 时间排序
            Collections.sort(list, new Comparator<ShowData>() {
                @Override
                public int compare(ShowData o1, ShowData o2) {
                    return o1.getInputdatetime().compareTo(o2.getInputdatetime());
                }
            });
            apiResponse.setResult(list);
            return apiResponse;

        } catch (Exception e) {
            LOGGER.error("失败，原因：" + e);
            apiResponse.setCode(ApiResponse.FAILED);
            apiResponse.setMessage("失败，原因：" + e);
            return apiResponse;
        }

    }

    @Override
    public ApiResponse<String> exportExcle(HttpServletResponse response) {
        try {

            //1.创建工作簿
            HSSFWorkbook workbook = new HSSFWorkbook();

            //1.1创建合并单元格对象
            CellRangeAddress callRangeAddress = new CellRangeAddress(0, 0, 0, 21);//起始行,结束行,起始列,结束列
            CellRangeAddress callRangeAddress1 = new CellRangeAddress(1, 1, 0, 21);//起始行,结束行,起始列,结束列


            CellRangeAddress callRangeAddress20 = new CellRangeAddress(3, 4, 0, 0);//起始行,结束行,起始列,结束列
            CellRangeAddress callRangeAddress21 = new CellRangeAddress(3, 4, 1, 1);//起始行,结束行,起始列,结束列


            CellRangeAddress callRangeAddress22 = new CellRangeAddress(3, 3, 2, 5);//起始行,结束行,起始列,结束列
            CellRangeAddress callRangeAddress23 = new CellRangeAddress(3, 3, 6, 9);//起始行,结束行,起始列,结束列
            CellRangeAddress callRangeAddress24 = new CellRangeAddress(3, 3, 10, 13);//起始行,结束行,起始列,结束列
            CellRangeAddress callRangeAddress25 = new CellRangeAddress(3, 3, 14, 17);//起始行,结束行,起始列,结束列
            CellRangeAddress callRangeAddress26 = new CellRangeAddress(3, 3, 18, 21);//起始行,结束行,起始列,结束列


            HSSFCellStyle headStyle = FileUtil.createCellStyle(workbook, (short) 10, false, true, (short) 1);

            HSSFCellStyle erStyle = FileUtil.createCellStyle(workbook, (short) 13, true, true, (short) 1);
            //
            HSSFCellStyle sanStyle = FileUtil.createCellStyle(workbook, (short) 10, false, false, (short) 1);
            //
            HSSFCellStyle colStyle = FileUtil.createCellStyle(workbook, (short) 10, true, true, (short) 1);
            //
            HSSFCellStyle cellStyle = FileUtil.createCellStyle(workbook, (short) 10, false, true, (short) 0);
            //
            HSSFSheet sheet = workbook.createSheet("监控");

            sheet.addMergedRegion(callRangeAddress);
            sheet.addMergedRegion(callRangeAddress1);
            sheet.addMergedRegion(callRangeAddress20);
            sheet.addMergedRegion(callRangeAddress21);
            sheet.addMergedRegion(callRangeAddress22);
            sheet.addMergedRegion(callRangeAddress23);
            sheet.addMergedRegion(callRangeAddress24);
            sheet.addMergedRegion(callRangeAddress25);
            sheet.addMergedRegion(callRangeAddress26);

            //设置默认列宽
            sheet.setDefaultColumnWidth(10);
            //3.创建行
            //3.1创建头标题行;并且设置头标题
            HSSFRow row = sheet.createRow(0);
            HSSFCell cell = row.createCell(0);
            //加载单元格样式
            cell.setCellStyle(headStyle);
            cell.setCellValue("互创联合科技有限公司");

            HSSFRow rower = sheet.createRow(1);
            HSSFCell celler = rower.createCell(0);
            //加载单元格样式
            celler.setCellStyle(erStyle);
            celler.setCellValue("报警监控");

            HSSFRow rowsan = sheet.createRow(2);
            HSSFCell cellsan = rowsan.createCell(0);
            HSSFCell cellsan2 = rowsan.createCell(5);

            //加载单元格样式
            cellsan.setCellStyle(sanStyle);
            cellsan.setCellValue("研发部：软件研发部");
            cellsan2.setCellStyle(sanStyle);
            cellsan2.setCellValue("时间:" + DateUtils.getToday());


            //3.2创建列标题;并且设置列标题
            HSSFRow row2 = sheet.createRow(3);
            HSSFCell cell2 = row2.createCell(0);
            //加载单元格样式
            cell2.setCellStyle(colStyle);
            cell2.setCellValue("医院名称");

            HSSFCell cell10 = row2.createCell(1);
            //加载单元格样式
            cell10.setCellStyle(colStyle);
            cell10.setCellValue("市电");


            HSSFCell cell11 = row2.createCell(2);
            //加载单元格样式
            cell11.setCellStyle(colStyle);
            cell11.setCellValue("培养箱");

            HSSFCell cell3 = row2.createCell(6);
            //加载单元格样式
            cell3.setCellStyle(colStyle);
            cell3.setCellValue("液氮罐");

            HSSFCell cell4 = row2.createCell(10);
            //加载单元格样式
            cell4.setCellStyle(colStyle);
            cell4.setCellValue("环境");

            HSSFCell cell5 = row2.createCell(14);
            //加载单元格样式
            cell5.setCellStyle(colStyle);
            cell5.setCellValue("冰箱");

            HSSFCell cell6 = row2.createCell(18);
            //加载单元格样式
            cell6.setCellStyle(colStyle);
            cell6.setCellValue("操作台");


            HSSFRow rowfour = sheet.createRow(4);
            String[] titlefour = {"总数", "数据超时(1h)", "探头异常", "无数据"};
            for (int i = 0; i < titlefour.length; i++) {

                HSSFCell cell7 = rowfour.createCell(i + 2);
                //加载单元格样式
                cell7.setCellStyle(colStyle);
                cell7.setCellValue(titlefour[i]);

                HSSFCell cell8 = rowfour.createCell(i + 6);
                //加载单元格样式
                cell8.setCellStyle(colStyle);
                cell8.setCellValue(titlefour[i]);

                HSSFCell cell9 = rowfour.createCell(i + 10);
                //加载单元格样式
                cell9.setCellStyle(colStyle);
                cell9.setCellValue(titlefour[i]);

                HSSFCell cell20 = rowfour.createCell(i + 14);
                //加载单元格样式
                cell20.setCellStyle(colStyle);
                cell20.setCellValue(titlefour[i]);

                HSSFCell cel21 = rowfour.createCell(i + 18);
                //加载单元格样式
                cel21.setCellStyle(colStyle);
                cel21.setCellValue(titlefour[i]);
            }

            ApiResponse<List<AlarmHospitalInfo>> listApiResponse = showAllHospitalAbInfo();
            List<AlarmHospitalInfo> result = listApiResponse.getResult();

            for (int a = 0; a < result.size(); a++) {

                AlarmHospitalInfo alarmHospitalInfo = result.get(a);

                //获取医院名称
                String hospitalname = alarmHospitalInfo.getHospitalname();
                //市电
                AlarmEquipmentTypeInfo ups = alarmHospitalInfo.getUps();
                //培养箱
                AlarmEquipmentTypeInfo incubator = alarmHospitalInfo.getIncubator();
                //液氮罐
                AlarmEquipmentTypeInfo nitrogen = alarmHospitalInfo.getNitrogen();
                //环境
                AlarmEquipmentTypeInfo envir = alarmHospitalInfo.getEnvir();
                //冰箱
                AlarmEquipmentTypeInfo icebox = alarmHospitalInfo.getIcebox();
                //操作台
                AlarmEquipmentTypeInfo floor = alarmHospitalInfo.getFloor();

                HSSFRow row1 = sheet.createRow(5 + a);

                //将医院名称写入表格
                HSSFCell cell31 = row1.createCell(0);
                cell31.setCellValue(hospitalname);
                cell31.setCellStyle(colStyle);
                //判断市电
                HSSFCell cell32 = row1.createCell(1);
                if (ups == null) {
                    cell32.setCellValue("无");
                    cell32.setCellStyle(colStyle);
                } else {
                    if (StringUtils.equals(ups.getType(), "0")) {
                        cell32.setCellValue("正常");
                        cell32.setCellStyle(colStyle);
                    } else {
                        cell32.setCellValue("异常");
                        cell32.setCellStyle(colStyle);
                    }
                }
                //将培养箱写入表格
                if (incubator != null) {
                    HSSFCell cell33 = row1.createCell(2);
                    //总数
                    Integer number = incubator.getNumber();
                    cell33.setCellValue(number);
                    cell33.setCellStyle(colStyle);
                    //数据超时
                    HSSFCell cell34 = row1.createCell(3);
                    Integer dataovertime = incubator.getDataovertime();
                    cell34.setCellValue(dataovertime);
                    if (dataovertime != 0) {
                        cell34.setCellStyle(cellStyle);
                    } else {
                        cell34.setCellStyle(colStyle);
                    }
                    //探头异常
                    HSSFCell cell35 = row1.createCell(4);
                    Integer instrumentab = incubator.getInstrumentab();
                    cell35.setCellValue(instrumentab);
                    if (instrumentab != 0) {
                        cell35.setCellStyle(cellStyle);
                    } else {
                        cell35.setCellStyle(colStyle);
                    }
                    //无数据
                    HSSFCell cell36 = row1.createCell(5);
                    Integer datanone = incubator.getDatanone();
                    cell36.setCellValue(datanone);
                    if (datanone != 0) {
                        cell36.setCellStyle(cellStyle);
                    } else {
                        cell36.setCellStyle(colStyle);
                    }
                }
                //液氮罐
                if (nitrogen != null) {
                    //总数
                    HSSFCell cell37 = row1.createCell(6);
                    Integer number = nitrogen.getNumber();
                    cell37.setCellValue(number);
                    cell37.setCellStyle(colStyle);
                    //数据超时
                    HSSFCell cell38 = row1.createCell(7);
                    Integer dataovertime = nitrogen.getDataovertime();
                    cell38.setCellValue(dataovertime);
                    if (dataovertime != 0) {
                        cell38.setCellStyle(cellStyle);
                    } else {
                        cell38.setCellStyle(colStyle);
                    }
                    //探头异常
                    HSSFCell cell39 = row1.createCell(8);
                    Integer instrumentab = nitrogen.getInstrumentab();
                    cell39.setCellValue(instrumentab);
                    if (instrumentab != 0) {
                        cell39.setCellStyle(cellStyle);
                    } else {
                        cell39.setCellStyle(colStyle);
                    }
                    //无数据
                    HSSFCell cell40 = row1.createCell(9);
                    Integer datanone = nitrogen.getDatanone();
                    cell40.setCellValue(datanone);
                    if (datanone != 0) {
                        cell40.setCellStyle(cellStyle);
                    } else {
                        cell40.setCellStyle(colStyle);
                    }
                }
                //环境
                if (envir != null) {
                    //总数
                    HSSFCell cell41 = row1.createCell(10);
                    Integer number = envir.getNumber();
                    cell41.setCellValue(number);
                    cell41.setCellStyle(colStyle);
                    //数据超时
                    HSSFCell cell42 = row1.createCell(11);
                    Integer dataovertime = envir.getDataovertime();
                    cell42.setCellValue(dataovertime);
                    if (dataovertime != 0) {
                        cell42.setCellStyle(cellStyle);
                    } else {
                        cell42.setCellStyle(colStyle);
                    }
                    //探头异常
                    HSSFCell cell43 = row1.createCell(12);
                    Integer instrumentab = envir.getInstrumentab();
                    cell43.setCellValue(instrumentab);
                    if (instrumentab != 0) {
                        cell43.setCellStyle(cellStyle);
                    } else {
                        cell43.setCellStyle(colStyle);
                    }
                    //无数据
                    HSSFCell cell44 = row1.createCell(13);
                    Integer datanone = envir.getDatanone();
                    cell44.setCellValue(datanone);
                    if (datanone != 0) {
                        cell44.setCellStyle(cellStyle);
                    } else {
                        cell44.setCellStyle(colStyle);
                    }
                }
                //冰箱
                if (icebox != null) {
                    //总数
                    HSSFCell cell45 = row1.createCell(14);
                    Integer number = icebox.getNumber();
                    cell45.setCellValue(number);
                    cell45.setCellStyle(colStyle);
                    //数据超时
                    HSSFCell cell46 = row1.createCell(15);
                    Integer dataovertime = icebox.getDataovertime();
                    cell46.setCellValue(dataovertime);
                    if (dataovertime != 0) {
                        cell46.setCellStyle(cellStyle);
                    } else {
                        cell46.setCellStyle(colStyle);
                    }
                    //探头异常
                    HSSFCell cell47 = row1.createCell(16);
                    Integer instrumentab = icebox.getInstrumentab();
                    cell47.setCellValue(instrumentab);
                    if (instrumentab != 0) {
                        cell47.setCellStyle(cellStyle);
                    } else {
                        cell47.setCellStyle(colStyle);
                    }
                    //无数据
                    HSSFCell cell48 = row1.createCell(17);
                    Integer datanone = icebox.getDatanone();
                    cell48.setCellValue(datanone);
                    if (datanone != 0) {
                        cell48.setCellStyle(cellStyle);
                    } else {
                        cell48.setCellStyle(colStyle);
                    }
                }
                //操作台
                if (floor != null) {
                    //总数
                    HSSFCell cell49 = row1.createCell(18);
                    Integer number = floor.getNumber();
                    cell49.setCellValue(number);
                    cell49.setCellStyle(colStyle);
                    //数据超时
                    HSSFCell cell50 = row1.createCell(19);
                    Integer dataovertime = floor.getDataovertime();
                    cell50.setCellValue(dataovertime);
                    if (dataovertime != 0) {
                        cell50.setCellStyle(cellStyle);
                    } else {
                        cell50.setCellStyle(colStyle);
                    }
                    //探头异常
                    HSSFCell cell51 = row1.createCell(20);
                    Integer instrumentab = floor.getInstrumentab();
                    cell51.setCellValue(instrumentab);
                    if (instrumentab != 0) {
                        cell51.setCellStyle(cellStyle);
                    } else {
                        cell51.setCellStyle(colStyle);
                    }
                    //无数据
                    HSSFCell cell52 = row1.createCell(21);
                    Integer datanone = floor.getDatanone();
                    cell52.setCellValue(datanone);
                    if (datanone != 0) {
                        cell52.setCellStyle(cellStyle);
                    } else {
                        cell52.setCellStyle(colStyle);
                    }
                }

            }

            //定义表格导出时默认文件名 时间戳
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            OutputStream os = response.getOutputStream();
            String fileName = df.format(new Date()) + "_HC.xls";
            response.reset();
            //作用：在前端作用显示为调用浏览器下载弹窗
            String headStr = "attachment; filename=\"" + fileName + "\"";
            response.setContentType("application/octet-streem");
            response.setHeader("Content-Disposition", headStr);
            workbook.write(os);
            os.flush();
            workbook.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ApiResponse.success();
    }

    @Override
    public ApiResponse<String> exportExcles(HttpServletResponse response) {
        String abnormal = (String) redisTemplateUtil.boundValueOps("abnormal").get();
        List<List<?>> lists = new ArrayList<List<?>>();
        List<String> titleList = new ArrayList<>();
        List<String> sheetList = new ArrayList<>();
        List<AbnormalDataModel> listModel = new ArrayList<>();

        if (StringUtils.isNotEmpty(abnormal)) {
            listModel = JsonUtil.toList(abnormal, AbnormalDataModel.class);
            titleList.add("数据异常");
            sheetList.add("数据异常");
            lists.add(listModel);
        }
        //获取氧气探头初次上传时间
        List<AbnormalDataModel> firstTimeO2 = hospitalInfoMapper.getFirstTimeO2();
        if (CollectionUtils.isNotEmpty(firstTimeO2)) {
            titleList.add("O2探头初次上传");
            sheetList.add("O2探头初次上传");
            lists.add(firstTimeO2);
        }
        List<AbnormalDataModel> warningInfo = hospitalInfoMapper.getWarningInfo(TimeHelper.getCurrentDate());
        if (CollectionUtils.isNotEmpty(warningInfo)) {
            titleList.add("当天报警最新数据");
            sheetList.add("当天报警最新数据");
            lists.add(warningInfo);
        }
//        FileUtil.exportExcel(listModel, "异常数据明细", "sheet1",
//                AbnormalDataModel.class,  "异常数据明细.xls", response);

        FileUtil.exportExcleSheets(lists, titleList, sheetList, AbnormalDataModel.class, TimeHelper.getCurrentDate() + "---监控异常数据汇总.xls", response);

        ApiResponse<String> apiResponse = new ApiResponse<>();
        return apiResponse;
    }


}
