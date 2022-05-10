package com.hc.handler;


import com.hc.my.common.core.bean.ParamaterModel;
import com.hc.my.common.core.probe.EquipmentCommand;
import com.hc.my.common.core.redis.namespace.TcpServiceEnum;
import com.hc.service.MTOnlineBeanService;
import com.hc.service.MessagePushService;
import com.hc.socketServer.IotServer;
import com.hc.util.JsonUtil;
import com.hc.util.MathUtil;
import com.hc.util.NettyUtil;
import com.redis.util.RedisTemplateUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.ReadTimeoutException;
import io.netty.util.ReferenceCountUtil;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.Random;

@Component
@Sharable
public class ServerChannelHandler extends ChannelInboundHandlerAdapter {
    private static final Logger log = LoggerFactory.getLogger(ServerChannelHandler.class);
    @Autowired
    private MTOnlineBeanService service;
    @Autowired
    private MessagePushService msgservice;

    @Autowired
    private RedisTemplateUtil redisDao;
    @Autowired
    private NettyUtil nettyUtil;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //将新建立的通道放入group中
        IotServer.onlineChannels.add(ctx.channel());
        log.info("建立长连接的通道id:" + ctx.channel().id().asShortText());
        super.channelActive(ctx);
    }

    // 当客户端主动断开服务端的链接后，这个通道就是不活跃的。也就是说客户端与服务端的关闭了通信通道并且不可以传输数据
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        //当连接关闭时，移除在线列表
        closeChannel(ctx);
        String asShortText = ctx.channel().id().asShortText();
        log.info("移除了该通道的连接:" + asShortText);
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
        } catch (Exception e) {
            log.error("数据处理异常发生错误" + e);
        } finally {
            //当连接关闭时，移除在线列表
            closeChannel(ctx);
        }
    }

    /**
     * 接收发送的消息，最后需要手工release接收数据的ByteBuf
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf buf = (ByteBuf) msg;
        byte[] receiveMsgBytes = new byte[buf.readableBytes()];
        buf.readBytes(receiveMsgBytes);
        try {
            //将接收到的字节数据转换为指定编码的字符串
            String dataStr = Hex.encodeHexString(receiveMsgBytes);
            //通道id
            String asShortText = ctx.channel().id().asShortText();
            log.info("通道[" + asShortText + "]接收数据:" + dataStr);
            //解析数据
            List<ParamaterModel> paseData = service.paseData(dataStr);
            if (CollectionUtils.isEmpty(paseData)) {
                log.info("数据解析错误:{}", dataStr);
                return;
            }
            paseData.forEach(snData -> {
                String sn = snData.getSN();
                String cmdid = snData.getCmdid();
                //是否是心跳需要应答
                checkIsHeartbeat(cmdid,ctx);
                //sn是否是MT600
                isMiansClient(sn, asShortText);
                //将通道发送
                snData.setChannelId(asShortText);
                snData.setNowTime(new Date());
                randomPush(snData);
                log.info("通道:{},推送给RabbitMQ的模型为:{}",dataStr,JsonUtil.toJson(snData));
            });
        } catch (Exception e) {
            log.error("通道:{},数据接收异常:{}",ctx.channel().id().asShortText(),Hex.encodeHexString(receiveMsgBytes));
        } finally {
            //从InBound里读取的ByteBuf要手动释放
            ReferenceCountUtil.release(msg);
        }
    }

    public void randomPush(ParamaterModel model) {
        Random random = new Random();
        int a = random.nextInt(3);
        if (a == 0) {
            msgservice.pushMessage(JsonUtil.toJson(model));
        }
        if (a == 1) {
            msgservice.pushMessage1(JsonUtil.toJson(model));
        }
        if (a == 2) {
            msgservice.pushMessage2(JsonUtil.toJson(model));
        }
    }

    /**
     * 关闭通道，同时移除当前通道在线列表信息
     *
     * @param ctx
     */
    public void closeChannel(ChannelHandlerContext ctx) {
        //从group中移除关闭的通道
        IotServer.onlineChannels.remove(ctx.channel());
        //关闭通道
        ctx.close();
        //主动移除通道在redis里面
        String asShortText = ctx.channel().id().asShortText();
        String sn = (String) redisDao.boundHashOps(TcpServiceEnum.SNCLIENT.getCode()).get(asShortText);
        redisDao.boundHashOps(TcpServiceEnum.TCPCLIENT.getCode()).delete(sn);
        redisDao.boundHashOps(TcpServiceEnum.SNCLIENT.getCode()).delete(asShortText);
    }

    //判断是否sn号为市电600或者1100sn号,是的话需要双向绑定通道缓存
    public void isMiansClient(String sn, String channeId) {
        if (StringUtils.isNotEmpty(MathUtil.ruleMT(sn)))    {
            //将SN号和通道id一起绑定 存入redis
            redisDao.boundHashOps(TcpServiceEnum.TCPCLIENT.getCode()).put(sn, channeId);
            redisDao.boundHashOps(TcpServiceEnum.SNCLIENT.getCode()).put(channeId, sn);
        }
    }
    //应答心跳包
    public void checkIsHeartbeat(String cmdId,ChannelHandlerContext ctx){
        if (StringUtils.equals(cmdId, EquipmentCommand.CMD88.getCmdId())) {
            //应答消息48 43 08 00 03 23
            nettyUtil.sendData(ctx, EquipmentCommand.HEART_RATE_RESPONSE.getCmdId());
        }
    }

}
