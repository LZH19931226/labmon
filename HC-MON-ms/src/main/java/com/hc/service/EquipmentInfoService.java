package com.hc.service;

import com.github.pagehelper.Page;
import com.hc.entity.Monitorequipment;
import com.hc.entity.Monitorupsrecord;
import com.hc.model.BatchModel;
import com.hc.model.CurveInfoModel;
import com.hc.model.QueryInfoModel;
import com.hc.model.ResponseModel.EquipmentConfigInfoModel;
import com.hc.utils.ApiResponse;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by 16956 on 2018-08-01.
 */
public interface EquipmentInfoService {


    /**
     * 批量禁用与启用当前设备类型下所有设备探头
     */
    ApiResponse<String> batchOperationType(BatchModel batchModel);


    /**
     * 获取所有当前设备类型所有设备信息
     */
    ApiResponse<List<Monitorequipment>> getEquipmentByType(String hospitalcode, String equipmenttypeid);

    /**
     * 获取所有设备当前值
     */
    ApiResponse<List<Monitorequipment>> getEquipmentCurrentData(String hospitalcode, String equipmenttypeid);

    /**
     * 分页获取当前设备类型所有当前值
     */
    ApiResponse<Page<Monitorequipment>> getEquipmentCurrentDataPage(String hospitalcode, String equipmenttypeid, Integer pagesize, Integer pagenum, String equipmentname);

    /**
     * 获取市电当前值
     */
    ApiResponse<Monitorupsrecord> getCurrentUps(String hospitalcode, String equipmenttypeid);

    /**
     * 获取APP曲线值，web环境曲线值
     */
    ApiResponse<CurveInfoModel> getCurvelFirst(String date, String equipmentno);

    /**
     * 获取web端 非环境曲线值   type   0:全部  1：O2 2:CO2 3:TEMP(温度)
     */
    ApiResponse<CurveInfoModel> getCurveSecond(String date, List<String> equipmentno, String type);

    /**
     * 查询业务
     */
    ApiResponse<List<QueryInfoModel>> queryEquipmentMonitorInfo(String equipmenttypeid, String equipmentno, String hospitalcode, String startdate, String enddate);

    /**
     * 导出数据业务   date 表示月份 格式为201808
     */
    ApiResponse<String> exportExcle(String equipmenttypeid, String equipmentno, String hospitalcode, String startdate, String enddate, HttpServletResponse response);

    /**
     * 展示当前医院所有设备配置信息
     */
    ApiResponse<List<EquipmentConfigInfoModel>> showEquipmentConfigInfo(String hospitalcode);

    /**
     * 根据设备编号查询设备当前值信息
     *
     * @param equipmentno
     * @return
     */
    ApiResponse<Monitorequipment> showInfoByEquipmentNo(String equipmentno);

    /**
     * 获取当前医院当前时间节点所有设备数据 -- excle导出
     */
    ApiResponse<String> exportExcleOne(HttpServletResponse response, String hospitalcode, String operationdate, String type);

    /**
     * 获取当前医院该设备所有探头该月份时间点位数据
     */
    ApiResponse<CurveInfoModel> getCurveInfoByMonthTime(String equipmentno, String time);

}
