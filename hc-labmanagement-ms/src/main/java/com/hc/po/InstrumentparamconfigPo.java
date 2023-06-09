package com.hc.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author liuzhihao
 * @email 1969994698@qq.com
 * @date 2022-04-18 15:27:01
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode
@TableName("instrumentparamconfig")
public class InstrumentparamconfigPo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 监控参数编号
     */
    @TableId(value = "instrumentparamconfigNO",type = IdType.INPUT)
    private String instrumentparamconfigno;
    /**
     * 探头编号
     */
    private String instrumentno;
    /**
     * 监控参数类型编码
     */
    private Integer instrumentconfigid;
    /**
     * 探头名称
     */
    private String instrumentname;
    /**
     * 最低限值
     */
    private BigDecimal lowlimit;
    /**
     * 最高限值
     */
    private BigDecimal highlimit;
    /**
     * 探头类型编码
     */
    private Integer instrumenttypeid;
    /**
     * 是否启用电话/短信/App推送报警
     */
    private String warningphone;
    /**
     * 推送消息时间
     */
    private Date pushtime;
    /**
     * 报警时间
     */
    private Date warningtime;
    /**
     *
     */
    private String channel;
    /**
     *
     */
    private Integer alarmtime;
    /**
     *
     */
    private String calibration;
    /**
     *
     */
    private Date firsttime;
    /**
     *
     */
    private BigDecimal saturation;

    /**
     * 设备状态
     */
    private String state;

    /**
     * 单位
     */
    private String unit;

    /**
     * 样式最小值
     */
    @TableField(value = "style_min")
    private String styleMin;

    /**
     * 样式最大值
     */
    @TableField(value = "style_max")
    private String styleMax;

    /**
     * 探头类型分组
     * 同于前端展示
     */
    @TableField(value = "ins_group")
    private String insGroup;

}

