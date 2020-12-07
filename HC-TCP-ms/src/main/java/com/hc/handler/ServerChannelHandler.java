package com.hc.handler;


import com.hc.bean.MTOnlineBean;
import com.hc.config.RedisTemplateUtil;
import com.hc.my.common.core.bean.ParamaterModel;
import com.hc.service.MTOnlineBeanService;
import com.hc.service.MessagePushService;
import com.hc.socketServer.IotServer;
import com.hc.util.JsonUtil;
import com.hc.util.MathUtil;
import com.hc.util.NettyUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.ReadTimeoutException;
import io.netty.util.ReferenceCountUtil;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Random;

@Component
@Sharable
public class  ServerChannelHandler extends ChannelInboundHandlerAdapter{
	
	
	private static final Logger log = LoggerFactory.getLogger(ServerChannelHandler.class);
	
	@Autowired
	private MTOnlineBeanService service;

//
//	@Value("${delphi.offKey}")
//	private  String offKey;

	
	@Autowired
	private MessagePushService msgservice;

	//用与随机发送通道
	public  int  a =0;

//	//用与随机发送通道
//	public  int  b =0;

	
	//private ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

	
	@Autowired
	private RedisTemplateUtil redisDao;
//	@Autowired
//	private Executor executor;

	@Autowired
	private NettyUtil nettyUtil;
	
	// 连接成功后，向server发送消息 
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		//将新建立的通道放入group中
		IotServer.onlineChannels.add(ctx.channel());
		log.info("建立长连接的通道id:"+ctx.channel().id().asShortText());
		super.channelActive(ctx);
	}
	
	// 当客户端主动断开服务端的链接后，这个通道就是不活跃的。也就是说客户端与服务端的关闭了通信通道并且不可以传输数据
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		//当连接关闭时，移除在线列表
		closeChannel(ctx);
		String asShortText = ctx.channel().id().asShortText();
		log.info("移除了该通道的连接:"+asShortText);
		super.channelInactive(ctx);

	}
	
	//通道内发生异常数据
	@Override  
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		try {
			if (!(cause instanceof ReadTimeoutException)) {
				String asShortText = ctx.channel().id().asShortText();
				cause.printStackTrace();
				log.error("通道[" + asShortText + "]数据处理异常", cause.fillInStackTrace());
			}
		}catch (Exception e){
			log.error("数据处理异常发生错误"+e);
		}finally {
			//当连接关闭时，移除在线列表
			closeChannel(ctx);
		}

	}
	
	
	/**
	 * 接收发送的消息，最后需要手工release接收数据的ByteBuf
	 */
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		ByteBuf buf  = (ByteBuf)msg;
		String dataStr=null;
		String asShortText=null;
		try{
			byte[] receiveMsgBytes = new byte[buf.readableBytes()];
			buf.readBytes(receiveMsgBytes);
			//将接收到的字节数据转换为指定编码的字符串
		     dataStr = Hex.encodeHexString(receiveMsgBytes);

	         //通道id
	         asShortText = ctx.channel().id().asShortText();
	        log.info("通道["+asShortText+"]接收数据:"+dataStr);


			//解析数据
			List<ParamaterModel> paseData = service.paseData(dataStr);

			if (CollectionUtils.isEmpty(paseData)) {
				return;
			}

	        for (ParamaterModel model : paseData) {

				//是否是心跳
				if (StringUtils.equals(model.getCmdid(), "88")) {
					//应答消息48 43 08 00 03 23
					String charString="484308000323";
					nettyUtil.sendData(ctx,charString);
					continue;
				}
				//sn是否是MT600
				if (StringUtils.isNotEmpty(MathUtil.ruleMT(model.getSN()))) {
					//将SN号和通道id一起绑定 存入redis
					try {
						redisDao.boundValueOps(model.getSN() + ":" + MTOnlineBean.class.getSimpleName()).set(asShortText);
						redisDao.boundValueOps(asShortText + ":" + ParamaterModel.class.getSimpleName()).set(model.getSN());
					}catch (Exception e) {
						log.error("redis服务异常,请管理员查询服务");

					}

				}
	        	//将通道发送
	        	model.setChannelId(asShortText);
	        	model.setNowTime(new Date());
				randomPush(model);
	        	log.info("推送给RabbitMQ的模型为:"+JsonUtil.toJson(model)+"通道数据为："+dataStr);
			}
	        
		}catch (Exception e) {
			log.error("数据接收转发异常"+e+"异常数据为:"+dataStr);
			redisDao.delete(asShortText + ":" + ParamaterModel.class.getSimpleName());
		}finally{
			//从InBound里读取的ByteBuf要手动释放
			ReferenceCountUtil.release(msg);
		}
		
	}
 
	public  void randomPush(ParamaterModel model){

		  Random  random = new Random();

		  a=random.nextInt(3);

		if (a==0){
			msgservice.pushMessage(JsonUtil.toJson(model));
		}
		if (a==1){
			msgservice.pushMessage1(JsonUtil.toJson(model));
		}
		if (a==2){
			msgservice.pushMessage2(JsonUtil.toJson(model));
		}

	}

//	public  void randomPush2(byte[] bytes){
//
//		Random  random = new Random();
//
//		b=random.nextInt(3);
//
//		if (b==0){
//			msgservice.pushMessage3(bytes);
//		}
//		if (b==1){
//			msgservice.pushMessage4(bytes);
//		}
//		if (b==2){
//			msgservice.pushMessage5(bytes);
//		}
//
//	}

//	public static void main(String[] args)throws IOException {
//
//		ServerSocket  serverSocket = new ServerSocket(10241,3);
//	  while (true){
//		  StringBuilder s = new StringBuilder();
//		  Socket socket = serverSocket.accept();
//		  System.out.println("New connection accepted "+ socket.getInetAddress()+":"+socket.getPort());
//		  InputStream inputStream = socket.getInputStream();
//		  byte buf[] = new byte[1024];
//		  int len = 0;
//		  while((len=(inputStream.read(buf)))>0){
//			  s.append(new String(buf,0,len));
//		  }
//		  //打印客户端的消息
//		  System.out.println(s.toString());
//	  }
//
//	}

//
//	/**
//	 * 服务器向终端发起上传数据请求，根据服务器请求的终端ID，从在线列表中找到该终端，使用终端通道发送数据请求命令
//	 * @param ctx
//	 * @param receiveMsg
//	 */
//	private void sendData(ChannelHandlerContext ctx, Map<String, String> receiveMsg){
//		ApiResponse<String> result = new ApiResponse<String>();
//		Channel existChannel = IotServer.liveChannels.get(receiveMsg.getTid());
//		if (existChannel != null){
//			IotMessageBean sendMsg = new IotMessageBean();
//			sendMsg.setTid(receiveMsg.getTid());
//			sendMsg.setCmdType(receiveMsg.getCmdType());
//			sendMsg.setData(Utils.asciiToValue(receiveMsg.getData()));
//			//等待结果执行完成
//			ChannelFuture future = writeDataByteByChannel(existChannel, sendMsg);
//			try {
//				//使用阻塞方式等待结果执行
//				future.await();
//			} catch (InterruptedException e) {
//				
//			}
//			
//			if(future.isSuccess()) {
//				
//				result.setMessage("请求发送完成");
//			} else {
//				
//				result = ApiResponse.fail("请求发送失败");
//			}
//		}else{
//			
//			//终端不在线
//			result = ApiResponse.fail("终端不在线");
//		}
//		
//		ObjectMapper mapper = new ObjectMapper();
//		try {
//			ctx.write(Unpooled.copiedBuffer(mapper.writeValueAsString(result), CharsetUtil.UTF_8));
//		} catch (JsonProcessingException e) {
//			
//		}
//		
//		ctx.flush();
//	}
	
	/**
	 * 向客户端发送数据，发送的数据必须转换成ByteBuf数组    
	 * @param ctx
	 */
//	private void writeDataByte(ChannelHandlerContext ctx, Map<String, String> sendData){
//		nettyUtil.sendData(ctx,sendData);
//	}
//	
//	/**
//	 * 向客户端发送数据，发送的数据必须转换成ByteBuf数组    
//	 */
//	private ChannelFuture writeDataByteByChannel(Channel channel, Map<String, String> sendData){
//        //发送数据同时刷新通道，数据发送完后，由netty自行负责释放该ByteBuf对象
//		return channel.writeAndFlush(sendData);
//	}
	
	/**
	 * 关闭通道，同时移除当前通道在线列表信息
	 * @param ctx
	 */
	public void closeChannel(ChannelHandlerContext ctx){ 
		//从group中移除关闭的通道
		IotServer.onlineChannels.remove(ctx.channel());	
		//关闭通道
		ctx.close();
		//主动移除通道在redis里面
		String asShortText = ctx.channel().id().asShortText();
		String sn = (String) redisDao.boundValueOps(asShortText+":"+ParamaterModel.class.getSimpleName()).get();
		redisDao.delete(sn+":"+MTOnlineBean.class.getSimpleName());
		redisDao.delete(asShortText+":"+ParamaterModel.class.getSimpleName());
	}
}
