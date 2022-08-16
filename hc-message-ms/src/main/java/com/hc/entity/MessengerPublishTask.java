package com.hc.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author LiuZhiHao
 * @date 2019/10/24 9:34
 * 描述:
 **/
@Data
@NoArgsConstructor
@EqualsAndHashCode
@TableName("lab_messengerpublishtask")
public class MessengerPublishTask implements Serializable {
    private static final long serialVersionUID = -688619952457136134L;
    /** 主键ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 批次号 */
    @TableField(value = "batch_no")
    private String batchNo;

    /** 任务编号 */
    @TableField(value = "task_no")
    private String taskNo;

    /** 订阅者用户ID */
    @TableField(value = "consumer_id")
    private String consumerId;

    /** 发布者用户ID */
    @TableField(value = "supplier_id")
    private String supplierId;

    /** 消息服务编号 */
    @TableField(value = "service_no")
    private String serviceNo;

    /** 消息标题 */
    @TableField(value = "message_title")
    private String messageTitle;

    /** 消息封面 */
    @TableField(value = "message_cover")
    private String messageCover;

    /** 消息简介 */
    @TableField(value = "message_intro")
    private String messageIntro;

    /** 发布关键字/userId/mobile/mail */
    @TableField(value = "publish_key")
    private String publishKey;

    /** 发布类型/notify/sms/mail/umpush */
    @TableField(value = "publish_type")
    private String publishType;

    /** 发布时间 */
    @TableField(value = "publish_time")
    private LocalDateTime publishTime;

    /** 任务执行状态SUCCESS(2)/FAIL(4)/ALL(8) */
    @TableField(value = "status")
    private Long status;

    /** 失败次数 */
    @TableField(value = "failure_times")
    private Integer failureTimes;

    /** 创建人 */
    @TableField(value = "create_by")
    private String createBy;

    /** 更新人 */
    @TableField(value = "update_by")
    private String updateBy;

    /** 创建时间 */
    @TableField(value = "create_time")
    private LocalDateTime createTime;

    /** 修改时间 */
    @TableField(value = "update_time")
    private LocalDateTime updateTime;

    /** 备注 */
    @TableField(value = "remark")
    private String remark;

    /** 删除( 0 删除  1 未删除) */
    @TableField(value = "del_flag")
    private Boolean delFlag;

    /** 消息内容 */
    @TableField(value = "message_bodys")
    private String messageBodys;
}
