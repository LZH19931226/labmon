package com.hc.po;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;
import java.util.List;

/**
 * Created by 15350 on 2020/5/20.
 */
@Data
@Table(name = "operationlog")
@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
public class Operationlog {


    @Id
    @ApiModelProperty("日志id")
    private String logid;
    @ApiModelProperty("用户ID")
    private String userid;
    @ApiModelProperty("用户姓名")
    private String username;
    @ApiModelProperty("用户类型")
    private String usertype;
    @ApiModelProperty("医院名称")
    private String hospitalname;
    @ApiModelProperty("操作类型")
    private String opeartiontype;
    @ApiModelProperty("表名称")
    private String tablename;
    @ApiModelProperty("操作时间")
    private Date operationtime;
    @ApiModelProperty("操作平台")
    private String platform;
    @ApiModelProperty("IP地址")
    private String operationip;
    @ApiModelProperty("功能菜单")
    private String functionname;
    @ApiModelProperty("操作说明")
    private String loginfo;
    private String equipmentname;
    @Transient
    private List<Operationlogdetail> operationlogdetails;
    @Transient
    private Integer pagesize;
    @Transient
    private Integer pagenum;
    @Transient
    private String begintime;
    @Transient
    private String endtime;


}
