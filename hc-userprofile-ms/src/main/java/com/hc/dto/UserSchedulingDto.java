package com.hc.dto;

import com.hc.po.UserSchedulingPo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.Set;

/**
 * 用户排班传输对象
 * @author hc
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class UserSchedulingDto extends UserSchedulingPo {
    /**
     * 主键id
     */
    private Long usid;

    /**
     * 用户ID
     */
    private String userid;

    /**
     * 医院代码
     */
    private  String  hospitalCode;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 用户名
     */
    private String username;

    /**
     * 用户电话
     */
    private String userPhone;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建者
     */
    private String createUser;

    /**
     * 报警方式
     */
    private String reminders;

    /**
     * 该人员的排班日期
     */
    private Set<Integer> integerSet;
}
