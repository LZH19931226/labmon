package com.hc.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sun.org.apache.xpath.internal.objects.XString;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@TableName(value = "lab_messengerpublishtask")
public class LabMessengerPublishTaskDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键id */
    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField(value = "batch_no")
    private String batchNo;

    @TableField(value = "task_no")
    private String taskNo;

    @TableField(value = "consumer_id")
    private String consumerId;

    @TableField(value = "supplier_id")
    private String supplierId;

    @TableField(value = "service_no")
    private String serviceNo;

    @TableField(value = "message_title")
    private String messageTitle;

    @TableField(value = "message_cover")
    private String messageCover;

    @TableField(value = "message_intro")
    private String messageIntro;

    @TableField(value = "publish_key")
    private String publishKey;

    @TableField(value = "publish_type")
    private String publishType;

    @TableField(value = "publish_time")
    private String publishTime;

    @TableField(value = "status")
    private Integer status;

    @TableField(value = "failure_times")
    private String failureTimes;

    @TableField(value = "create_by")
    private String createBy;

    @TableField(value = "update_by")
    private String updateBy;

    @TableField(value = "create_time")
    private String createTime;

    @TableField(value = "update_time")
    private String updateTime;

    @TableField(value = "remark")
    private String remark;

    @TableField(value = "del_flag")
    private String delFlag;

    @TableField(value = "message_bodys")
    private String messageBodys;
}
