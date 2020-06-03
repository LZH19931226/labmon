package com.hc.bean;


import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by 16956 on 2018-07-31.
 */

@ApiModel("数据解析模型")
@Getter
@Setter
@ToString
public class ParamaterModel {
    @ApiModelProperty("设备sn号")
    private String SN;
    @ApiModelProperty("命令id")
    private String cmdid;
    //%
    @ApiModelProperty("CO2")
    private String CO2;
    //%
    @ApiModelProperty("O2")
    private String O2;
    //PPM
    @ApiModelProperty("空气质量")
    private String VOC;
    //℃
    @ApiModelProperty("一路温度")
    private String TEMP;
    @ApiModelProperty("二路温度")
    private String TEMP2;
	@ApiModelProperty("三路温度")
	private String TEMP3;
	@ApiModelProperty("四路温度")
	private String TEMP4;
	@ApiModelProperty("五路温度")
	private String TEMP5;
	@ApiModelProperty("六路温度")
	private String TEMP6;
	@ApiModelProperty("七路温度")
	private String TEMP7;
	@ApiModelProperty("八路温度")
	private String TEMP8;
	@ApiModelProperty("九路温度")
	private String TEMP9;
	@ApiModelProperty("十路温度")
	private String TEMP10;

	@ApiModelProperty("气流")
    private  String airflow;
    //单位%
    @ApiModelProperty("湿度")
    private String RH;
    //MBAR
    @ApiModelProperty("压力")
    private String PRESS; 
    //单位%
    @ApiModelProperty("电量")
    private String QC;
    //ug/m3
    @ApiModelProperty("PM2.5")
    private String PM25;
    //ug/m3
    @ApiModelProperty("PM10")
    private String PM10;

    @ApiModelProperty("PM0.5")
    private String PM05;

    @ApiModelProperty("PM5.0")
    private String PM50;

    @ApiModelProperty("N2压力")
    private String N2;
    //1：市电通->市电断 2：市电断->市电断 3：市电断->市电通  4：市电正常 5：市电异常
    @ApiModelProperty("市电") 
    private String UPS;
    //开关量编号（目前只支持两路开关量 1 和 2）
    @ApiModelProperty("开关门记录")
    private String DOOR;
    //开关量编号报警类型/状态 1：开->关 2：关->开 3：持续关 4：持续开
    private String DOORZ;
    //mg/m3
    @ApiModelProperty("甲醛")
    private String OX ;
    
    @ApiModelProperty("时间")
    private Date nowTime ;
    
    @ApiModelProperty("通道id")
    private String channelId ;

}
