package com.hc.po;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by 15350 on 2020/5/20.
 */
@Data
@Table(name = "operationlogdetail")
@Entity
public class Operationlogdetail {

    @Id
    @ApiModelProperty("记录id")
    private String detailid;
    @ApiModelProperty("日志id")
    private String logid;
    @ApiModelProperty("字段名称")
    private String filedname;
    @ApiModelProperty("字段标题")
    private String filedcaption;
    @ApiModelProperty("历史值")
    private String filedvalueprev;
    @ApiModelProperty("字段值")
    private String filedvalue;
    @ApiModelProperty("注释")
    private String comment;

}
