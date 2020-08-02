package com.hc.model.RequestModel;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hc.entity.UserScheduLing;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * @author LiuZhiHao
 * @date 2020/7/31 15:31
 * 描述:
 **/
@Data
public class UserScheduLingPostModel {

    /**
     * 开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date starttime;


    /**
     * 结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endtime;



    /**
     * 创建者
     */
    private String createuser;



    /**
     * 医院名称
     */
    private String hospitalcode;



    private List<UserScheduLing> userScheduLings;
}
