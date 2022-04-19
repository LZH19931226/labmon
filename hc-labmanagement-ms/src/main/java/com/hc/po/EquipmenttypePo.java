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
@TableName("equipmenttype")
public class EquipmenttypePo  implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 设备类型编号
	 */
	@TableId(type = IdType.AUTO)
		private String equipmenttypeno;
	/**
	 * 医院编号
	 */
		private String hospitalcode;
	/**
	 * 设备类型名称
	 */
		private String equipmenttypename;
	/**
	 * 图片路径
	 */
		private String picturefilename;
	/**
	 * 是否显示
	 */
		private String isvisible;
	/**
	 * 排序
	 */
		private Integer orderno;
	/**
	 * 设备项目编号
	 */
		private String equipmentitem;

}

