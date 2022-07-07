package com.hc.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@TableName(value = "warningrecordinfo")
@Data
public class WarningRecordInfoDto {

    /**
     * pkid
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 医院id
     */
    private String info;

    /**
     * 探头编号
     */
    private String warningrecordid;

    /**
     * 创建人
     */
    private String createuser;

    /**
     * 修改人
     */
    private String updateuser;

    /**
     * 创建时间
     */
    private Date createtime;

    /**
     * 更新时间
     */
    private Date updatetime;
}
