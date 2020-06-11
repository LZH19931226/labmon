package com.hc.model;

import lombok.Data;

import java.util.List;

/**
 * @author LiuZhiHao
 * @date 2020/6/11 9:39
 * 描述:
 **/
@Data
public class MonitortlastdataTypeModel {

    private String type;

    private String tableName;

    private String equipmentno;

    private String startTime;

    private String endTime;


    private List<String>  instruMentParamConfigNos;


}
