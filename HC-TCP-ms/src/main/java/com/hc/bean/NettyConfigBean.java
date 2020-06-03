package com.hc.bean;


import org.springframework.boot.context.properties.ConfigurationProperties;



@ConfigurationProperties(prefix = "netty")
public class NettyConfigBean {

	//服务端口
	private int port;
	//主进程总数.负责请求的accept操作
	private int bossGroupCount;
	//工作进程总数.负责请求的read、write和处理操作
	private int wokerGroupCount;
	//TCP连接队列数.服务器请求处理线程全满时，用于临时存放请求队列的最大长度,默认值50
	private int backlog;
	//是否启用心跳保活机制.在双方TCP套接字建立连接后（即都进入ESTABLISHED状态）并且在两个小时左右上层没有任何数据传输的情况下，这套机制才会被激活
	private boolean keepAlive;
	
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public int getBossGroupCount() {
		return bossGroupCount;
	}
	public void setBossGroupCount(int bossGroupCount) {
		this.bossGroupCount = bossGroupCount;
	}
	public int getWokerGroupCount() {
		return wokerGroupCount;
	}
	public void setWokerGroupCount(int wokerGroupCount) {
		this.wokerGroupCount = wokerGroupCount;
	}
	public int getBacklog() {
		return backlog;
	}
	public void setBacklog(int backlog) {
		this.backlog = backlog;
	}
	public boolean isKeepAlive() {
		return keepAlive;
	}
	public void setKeepAlive(boolean keepAlive) {
		this.keepAlive = keepAlive;
	}
	
	
}
