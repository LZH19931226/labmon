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
@TableName("lab_messengerservicedefine")
public class MessengerServiceDefine implements Serializable {
    private static final long serialVersionUID = 7795914558540604720L;
    /** 主键ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 渠道ID */
    @TableField(value = "channel_id")
    private String channelId;

    /** 消息服务编号 */
    @TableField(value = "service_no")
    private String serviceNo;

    /** 消息服务名称 */
    @TableField(value = "service_name")
    private String serviceName;

    /** 渠道业务源 */
    @TableField(value = "business_src")
    private String businessSrc;

    /** 业务编号 */
    @TableField(value = "business_no")
    private String businessNo;

    /** 业务模板 */
    @TableField(value = "business_tpl")
    private String businessTpl;

    /** 发送策略 */
    @TableField(value = "strategy")
    private String strategy;

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
}
