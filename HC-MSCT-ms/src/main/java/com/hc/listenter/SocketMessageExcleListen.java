//package com.hc.listenter;
//
//import com.hc.entity.Monitorequipment;
//import com.hc.exchange.SocketMessage;
//import com.hc.mapper.EquipmentInfoMapper;
//import com.hc.model.ExcleInfoModel.*;
//import com.hc.model.Params;
//import com.hc.utils.FileUtil;
//import com.hc.utils.JsonUtil;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.cloud.stream.annotation.EnableBinding;
//import org.springframework.cloud.stream.annotation.StreamListener;
//import org.springframework.stereotype.Component;
//import org.springframework.web.context.request.RequestContextHolder;
//import org.springframework.web.context.request.ServletRequestAttributes;
//
//import javax.servlet.http.HttpServletResponse;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Comparator;
//import java.util.List;
//
///**
// * Created by xxf on 2019-01-05.
// */
//@Component
//@EnableBinding(SocketMessage.class)
//public class SocketMessageExcleListen {
//    @Autowired
//    private EquipmentInfoMapper equipmentInfoMapper;
//
//    /**
//     * 监听  单个设备数据导出excle事件
//     *
//     * @param message
//     */
//    @StreamListener(SocketMessage.EXCHANGE_EXPORE)
//    public void message(String message) {
//        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
//        Params param = JsonUtil.toBean(message, Params.class);
//        String equipmentno = param.getEquipmentno();
//        Monitorequipment monitorequipment = new Monitorequipment();
//        monitorequipment = equipmentInfoMapper.getEquipmentByNo(equipmentno);
//        if (StringUtils.isEmpty(monitorequipment.getEquipmenttypeid())) {
//            return;
//        }
//        String equipmenttypeid = monitorequipment.getEquipmenttypeid();
//        if (StringUtils.equals("1", equipmenttypeid)) {
//            // 环境
//            EnvironmentListModel hjExcleInfo = equipmentInfoMapper.getHJExcleInfo(param);
//            List<EnvironmentModel> environmentModelList = hjExcleInfo.getEnvironmentModelList();
//            // 导出excle
//            String type = param.getType();
//            // HttpServletResponse response = param.getResponse();
//            FileUtil.exportExcel(environmentModelList, monitorequipment.getEquipmentname() + type + "监控数据汇总", "sheet1",
//                    EnvironmentModel.class, monitorequipment.getEquipmentname() + "-" + type + "监控数据汇总.xls", response);
//        } else if (StringUtils.equals("2", equipmenttypeid)) {
//            //培养箱
//            IncubatorListModel pyxExcleInfo = equipmentInfoMapper.getPyxExcleInfo(param);
//            List<IncubatorModel> incubatorModelList = pyxExcleInfo.getIncubatorModelList();
//            String type = param.getType();
//            //  HttpServletResponse response = param.getResponse();
//            FileUtil.exportExcel(incubatorModelList, monitorequipment.getEquipmentname() + type + "监控数据汇总", "sheet1",
//                    IncubatorModel.class, monitorequipment.getEquipmentname() + "-" + type + "监控数据汇总.xls", response);
//        } else {
//            //其余
//            OtherListModel otherExcleInfo = equipmentInfoMapper.getOtherExcleInfo(param);
//            List<OtherModel> otherModels = otherExcleInfo.getOtherModels();
//            String type = param.getType();
//            //    HttpServletResponse response = param.getResponse();
//            FileUtil.exportExcel(otherModels, monitorequipment.getEquipmentname() + type + "监控数据汇总", "sheet1",
//                    OtherModel.class, monitorequipment.getEquipmentname() + "-" + type + "监控数据汇总.xls", response);
//        }
//
//    }
//
//    /**
//     * 监听单个设备类型下所有设备导出
//     *
//     * @param message
//     */
//    @StreamListener(SocketMessage.EXCHANGE_EXPORE1)
//    public void message1(String message) {
//        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
//        Params param = JsonUtil.toBean(message, Params.class);
//        String equipmenttypeid = param.getEquipmenttypeid();
//        //       HttpServletResponse response = param.getResponse();
//        String type = param.getType();
//        String equipmenttypename = "环境";
//        switch (equipmenttypeid) {
//            case "1":
//                equipmenttypename = "环境";
//                break;
//            case "2":
//                equipmenttypename = "培养箱";
//                break;
//            case "3":
//                equipmenttypename = "液氮罐";
//                break;
//            case "4":
//                equipmenttypename = "冰箱";
//                break;
//            case "5":
//                equipmenttypename = "操作台";
//                break;
//        }
//        List<List<?>> lists = new ArrayList<List<?>>();
//        List<String> sheetList = new ArrayList<String>();
//        List<String> titleList = new ArrayList<String>();
//        if (StringUtils.equals("1", equipmenttypeid)) {
//            List<EnvironmentListModel> hjExcleInfoById = equipmentInfoMapper.getHJExcleInfoById(param);
//            for (EnvironmentListModel environmentListModel : hjExcleInfoById) {
//                sheetList.add(environmentListModel.getEquipmentname() + "-" + type + "监控数据汇总");
//                titleList.add(environmentListModel.getEquipmentname());
//                List<EnvironmentModel> environmentModels = environmentListModel.getEnvironmentModelList();
//                // 时间排序
//                Collections.sort(environmentModels, new Comparator<EnvironmentModel>() {
//                    @Override
//                    public int compare(EnvironmentModel o1, EnvironmentModel o2) {
//                        return o1.getInputdatetime().compareTo(o2.getInputdatetime());
//                    }
//                });
//                lists.add(environmentModels);
//            }
//            FileUtil.exportExcleSheets(lists, titleList, sheetList, EnvironmentModel.class, equipmenttypename + "-" + type + "监控数据汇总.xls", response);
//        } else if (StringUtils.equals("2", equipmenttypeid)) {
//            List<IncubatorListModel> pyxExcleInfoById = equipmentInfoMapper.getPyxExcleInfoById(param);
//            for (IncubatorListModel incubatorListModel : pyxExcleInfoById) {
//                sheetList.add(incubatorListModel.getEquipmentname() + "-" + type + "监控数据汇总");
//                titleList.add(incubatorListModel.getEquipmentname());
//                List<IncubatorModel> incubatorModelList  = incubatorListModel.getIncubatorModelList();
//                Collections.sort(incubatorModelList, new Comparator<IncubatorModel>() {
//                    @Override
//                    public int compare(IncubatorModel o1, IncubatorModel o2) {
//                        return o1.getInputdatetime().compareTo(o2.getInputdatetime());
//                    }
//                });
//                lists.add(incubatorModelList);
//            }
//            FileUtil.exportExcleSheets(lists, titleList, sheetList, IncubatorModel.class, equipmenttypename + "-" + type + "监控数据汇总.xls", response);
//        } else {
//            List<OtherListModel> otherExcleInfoById = equipmentInfoMapper.getOtherExcleInfoById(param);
//            for (OtherListModel otherListModel : otherExcleInfoById) {
//                sheetList.add(otherListModel.getEquipmentname() + "-" + type + "监控数据汇总");
//                titleList.add(otherListModel.getEquipmentname());
//                List<OtherModel> otherModels  = otherListModel.getOtherModels();
//                Collections.sort(otherModels, new Comparator<OtherModel>() {
//                    @Override
//                    public int compare(OtherModel o1, OtherModel o2) {
//                        return o1.getInputdatetime().compareTo(o2.getInputdatetime());
//                    }
//                });
//                lists.add(otherModels);
//            }
//            FileUtil.exportExcleSheets(lists, titleList, sheetList, OtherModel.class, equipmenttypename + "-" + type + "监控数据汇总.xls", response);
//        }
//    }
//}
