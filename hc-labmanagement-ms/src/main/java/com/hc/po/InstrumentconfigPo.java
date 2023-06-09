package com.hc.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 *
 *
 * @author liuzhihao
 * @email 1969994698@qq.com
 * @date 2022-04-18 15:27:01
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode
@TableName("instrumentconfig")
public class InstrumentconfigPo  implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 监控参数类型编码
	 */
	@TableId(type = IdType.INPUT)
	private Integer instrumentconfigid;

	/**
	 * 监控参数类型名称
	 */
	private String instrumentconfigname;

	/**
	 * 探头类型分组
	 * 同于前端展示
	 */
	@TableField(value = "ins_group")
	private String insGroup;

}

