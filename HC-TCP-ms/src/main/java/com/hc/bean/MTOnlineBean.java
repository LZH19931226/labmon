package com.hc.bean;


import java.io.Serializable;



/**
 *
 * @author Liu
 *
 */
public class MTOnlineBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2975934295202949313L;
	//硬件SN号
	private String tid;
	//硬件连接通道ID
	private String cid;
	//最后在线时间
	private long olTime;
	
	public String getTid() {
		return tid;
	}
	public void setTid(String tid) {
		this.tid = tid;
	}
	public String getCid() {
		return cid;
	}
	public void setCid(String cid) {
		this.cid = cid;
	}
	public long getOlTime() {
		return olTime;
	}
	public void setOlTime(long olTime) {
		this.olTime = olTime;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	

}
