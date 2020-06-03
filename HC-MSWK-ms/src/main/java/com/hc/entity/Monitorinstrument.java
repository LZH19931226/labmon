package com.hc.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Table(name = "monitorinstrument")
@Entity
@Getter
@Setter
@ToString
public class Monitorinstrument implements Serializable {
	
	
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 4186073419054465771L;

	@Id
    private String instrumentno;

    private String instrumentname;

    private String equipmentno;

    private Integer instrumenttypeid;
    private String hospitalcode;
    private String sn;
    //智能报警限制次数
    private Integer alarmtime;
    @ApiModelProperty("开关量")
    private String channel;




}