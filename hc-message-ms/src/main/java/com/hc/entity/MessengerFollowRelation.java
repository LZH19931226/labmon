package com.hc.entity;

/**
 * @author LiuZhiHao
 * @date 2019/10/24 9:33
 * 描述:
 **/

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@EqualsAndHashCode
@TableName("lab_messengerfollowrelation")
public class MessengerFollowRelation implements Serializable {
    private static final long serialVersionUID = -5942001083603777205L;
    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 订阅关系编号
     */
    @TableField(value = "relation_no")
    private String relationNo;

    /**
     * 订阅者用户ID
     */
    @TableField(value = "follower_id")
    private String followerId;

    /**
     * 订阅类型/TOPIC/USER
     */
    @TableField(value = "follow_type")
    private String followType;

    /**
     * 订阅关键字/topic/userId
     */
    @TableField(value = "follow_key")
    private String followKey;

    /**
     * 订阅名称
     */
    @TableField(value = "follow_name")
    private String followName;

    /**
     * 订阅说明
     */
    @TableField(value = "follow_introduction")
    private String followIntroduction;

    /**
     * 创建人
     */
    @TableField(value = "create_by")
    private String createBy;

    /**
     * 更新人
     */
    @TableField(value = "update_by")
    private String updateBy;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @TableField(value = "update_time")
    private LocalDateTime updateTime;

    /**
     * 备注
     */
    @TableField(value = "remark")
    private String remark;

    /**
     * 删除( 0 删除  1 未删除)
     */
    @TableField(value = "del_flag")
    private Boolean delFlag;
}
