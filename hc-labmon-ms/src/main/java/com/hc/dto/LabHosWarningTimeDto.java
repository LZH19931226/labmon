package com.hc.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "lab_hos_warningtime")
public class LabHosWarningTimeDto {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField(value = "begintime")
    private Date beginTime;

    @TableField(value = "endtime")
    private Date endTime;

    @TableField(value = "hospitalcode")
    private String hospitalCode;
}
