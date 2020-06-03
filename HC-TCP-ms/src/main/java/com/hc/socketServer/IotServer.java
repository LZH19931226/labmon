package com.hc.socketServer;


import java.net.InetSocketAddress;


import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * socket服务端
 * @author Liu
 *
 */
@Configuration
public class IotServer {
	
	private static final Logger log = LoggerFactory.getLogger(IotServer.class);

	
	@Autowired
	private ServerBootstrap server;
	@Autowired
	private InetSocketAddress tcpSocket;
	
	private Channel serverChannel;
	//在线通道列表
	//public static Map<String, Channel> liveChannels = new ConcurrentSkipListMap<String, Channel>();	
	public static ChannelGroup onlineChannels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
	
	@PostConstruct
	public void start() throws Exception{
		log.info("启动服务器 " + tcpSocket);
		new Thread(new Runnable() {
			@Override
			public void run() {
				try{
					serverChannel = server.bind(tcpSocket).sync().channel().closeFuture().sync().channel();	
				}catch(Exception e){
					log.info(e+"");
				}
			}
		}).start();
	}
	
	@PreDestroy
	public void stop() throws Exception{
		log.info("停止服务器 " + tcpSocket);
		serverChannel.close();
		serverChannel.parent().close();
	}
}
