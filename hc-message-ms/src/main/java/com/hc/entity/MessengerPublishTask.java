package com.hc.entity;

import com.baomidou.mybatisplus.annotation.IdType;
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
    private String batchNo;

    /** 任务编号 */
    private String taskNo;

    /** 订阅者用户ID */
    private String consumerId;

    /** 发布者用户ID */
    private String supplierId;

    /** 消息服务编号 */
    private String serviceNo;

    /** 消息标题 */
    private String messageTitle;

    /** 消息封面 */
    private String messageCover;

    /** 消息简介 */
    private String messageIntro;

    /** 发布关键字/userId/mobile/mail */
    private String publishKey;

    /** 发布类型/notify/sms/mail/umpush */
    private String publishType;

    /** 发布时间 */
    private LocalDateTime publishTime;

    /** 任务执行状态SUCCESS(2)/FAIL(4)/ALL(8) */
    private Long status;

    /** 失败次数 */
    private Integer failureTimes;

    /** 创建人 */
    private String createBy;

    /** 更新人 */
    private String updateBy;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 修改时间 */
    private LocalDateTime updateTime;

    /** 备注 */
    private String remark;

    /** 删除( 0 删除  1 未删除) */
    private Boolean delFlag;

    /** 消息内容 */
    private String messageBodys;
}