package com.hc.my.message.entity;

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
@TableName("MessengerServiceDefine")
public class MessengerServiceDefine implements Serializable {
    private static final long serialVersionUID = 7795914558540604720L;
    /** 主键ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 渠道ID */
    private String channelId;

    /** 消息服务编号 */
    private String serviceNo;

    /** 消息服务名称 */
    private String serviceName;

    /** 渠道业务源 */
    private String businessSrc;

    /** 业务编号 */
    private String businessNo;

    /** 业务模板 */
    private String businessTpl;

    /** 发送策略 */
    private String strategy;

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
}
