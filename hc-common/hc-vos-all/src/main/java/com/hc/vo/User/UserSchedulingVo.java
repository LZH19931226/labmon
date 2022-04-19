package com.hc.vo.User;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Set;

/**
 * @author
 */
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserSchedulingVo {

    private Long usid;

    /**
     * 用户ID
     */
    private String userid;



    /**
     * 医院名称
     */
    private String hospitalCode;


    /**
     * 开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;


    /**
     * 结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;


    /**
     * 用户名称
     */
    private String username;


    /**
     * 用户手机
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
     * 排班日期集合
     */
    private Set<Integer> integerSet;

}
