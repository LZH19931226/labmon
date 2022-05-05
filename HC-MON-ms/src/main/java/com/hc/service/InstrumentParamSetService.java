package com.hc.service;

import com.github.pagehelper.Page;
import com.hc.po.Instrumentparamconfig;
import com.hc.model.InstrumentMonitorInfoModel;
import com.hc.model.PushSetModel;
import com.hc.model.ResponseModel.InstrumentParamConfigInfos;
import com.hc.model.UpdateDeviceTokenModel;
import com.hc.model.UpsModel;
import com.hc.utils.ApiResponse;

import java.util.List;

/**
 * Created by 16956 on 2018-08-05.
 */
public interface InstrumentParamSetService {

    /**
     * 为MT100设备加QC电量探头
     */
    ApiResponse<String> addMt100Qc();


    /**
     * 报警信息回执   半小时后提醒   1小时后再提醒  24小时后再提醒  屏蔽该类型消息
     * type : 1 半小时 2 :1小时  3 ： 24小时   4 ： 屏蔽该类型消息
     */
    ApiResponse<String> updatePushTime(PushSetModel pushSetModel);

    /**
     * 启用禁用探头报警
     */
    ApiResponse<String> updateWarningPhone(Instrumentparamconfig instrumentparamconfig);

    /**
     * 展示所有设备探头监控类型可用禁用信息
     */
    ApiResponse<Page<InstrumentParamConfigInfos>> showInsStart(String hospitalcode,String equipmenttypeid,Integer pagesize,Integer pagenum,String equipmentinfo);

    /**
     * 修改设备显示还是不显示
     * @param equipmentno
     * @param clientvisible
     * @return
     */
    ApiResponse<String> updateEquipmentClientvisible(String equipmentno,String clientvisible,String username);

    /**
     * 报警消息推送   测试用接口
     */
    String warningMessageSend(String pkid);

    /**
     * ios  调用修改devicetoken 接口
     * @param updateDeviceTokenModel
     * @return
     */
    ApiResponse<String> updateDeviceToken(UpdateDeviceTokenModel updateDeviceTokenModel);


    ApiResponse<List<UpsModel>> getCurrentUps(String hospitalcode, String equipmenttypeid);

    ApiResponse<List<InstrumentMonitorInfoModel>> getLowHighLimit(String equipmentno);
}
