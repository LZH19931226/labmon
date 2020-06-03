package com.hc.service;

import com.hc.bean.ApiResponse;
import com.hc.bean.MTOnlineBean;
import com.hc.bean.ParamaterModel;

import java.util.List;

public interface MTOnlineBeanService {
	
	/*
	 * 向制定sn号的设备发送内容
	 */
    ApiResponse<String> sendMsg(String MId,String cmd);
    
    
    /*
       * 解析硬件上传数据
     */
    List<ParamaterModel> paseData(String data);

    List<MTOnlineBean> getall();
}
