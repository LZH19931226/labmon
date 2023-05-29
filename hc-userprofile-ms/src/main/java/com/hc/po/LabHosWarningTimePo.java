package com.hc.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 医院
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "lab_hos_warningtime")
public class LabHosWarningTimePo {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField(value = "begintime")
    private LocalDateTime beginTime;

    @TableField(value = "endtime")
    private LocalDateTime endTime;

    @TableField(value = "hospitalcode")
    private String hospitalCode;

}
