package com.hc.my.message.entity;

/**
 * @author LiuZhiHao
 * @date 2019/10/24 9:33
 * 描述:
 **/
import java.io.Serializable;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode
@TableName("MessengerFollowRelation")
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
    private String relationNo;

    /**
     * 订阅者用户ID
     */
    private String followerId;

    /**
     * 订阅类型/TOPIC/USER
     */
    private String followType;

    /**
     * 订阅关键字/topic/userId
     */
    private String followKey;

    /**
     * 订阅名称
     */
    private String followName;

    /**
     * 订阅说明
     */
    private String followIntroduction;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;

    /**
     * 备注
     */
    private String remark;

    /**
     * 删除( 0 删除  1 未删除)
     */
    private Boolean delFlag;
}