package com.hc.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

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
@TableName("hospitalequiment")
public class HospitalequimentPo  implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 设备类型编码
	 */
	@TableId(type = IdType.AUTO)
		private String equipmenttypeid;
	/**
	 * 医院编号
	 */
		private String hospitalcode;
	/**
	 * 是否显示
	 */
		private String isvisible;
	/**
	 * 排序
	 */
		private Integer orderno;
	/**
	 * 设备类型超时报警设置
	 */
		private String timeout;
	/**
	 * 设备类型超时时间设置
	 */
		private Integer timeouttime;
	/**
	 * 全天报警
	 */
		private String alwayalarm;

}

