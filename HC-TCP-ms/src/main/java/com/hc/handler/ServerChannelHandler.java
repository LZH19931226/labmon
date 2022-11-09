package com.hc.handler;


import cn.hutool.json.JSONUtil;
import com.hc.my.common.core.redis.dto.ParamaterModel;
import com.hc.my.common.core.probe.EquipmentCommand;
import com.hc.service.MTOnlineBeanService;
import com.hc.service.MessagePushService;
import com.hc.socketServer.IotServer;
import com.hc.tcp.TcpClientApi;
import com.hc.util.JsonUtil;
import com.hc.util.MathUtil;
import com.hc.util.NettyUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.ReadTimeoutException;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

@Component
@Sharable
@Slf4j
public class ServerChannelHandler extends ChannelInboundHandlerAdapter {
    @Autowired
    private MTOnlineBeanService service;
    @Autowired
    private NettyUtil nettyUtil;
    @Autowired
    private TcpClientApi tcpClientApi;
    @Autowired
    private MessagePushService messagePushService;


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
                snData.setChannelId(asShortText);
                snData.setNowTime(new Date());
                snData.setData(dataStr);
                //是否是心跳需要应答
                checkIsHeartbeat(sn, asShortText, cmdid, ctx);
                //判断sn是否是mt600/mt1100,需要缓存通道与sn的关联
                saveChannelIdSn(snData);
            });
            ParamaterModel paramaterModel =  new ParamaterModel();
            paramaterModel.setData(dataStr);
            messagePushService.pushMessage(JsonUtil.toJson(paramaterModel));
            log.info("通道:{},原始数据:{},推送给消息队列的模型为:{}", asShortText, dataStr, JsonUtil.toJson(paramaterModel));
        } catch (Exception e) {
            log.error("通道:{},数据接收异常:{}", ctx.channel().id().asShortText(), Hex.encodeHexString(receiveMsgBytes));
        } finally {
            //从InBound里读取的ByteBuf要手动释放
            ReferenceCountUtil.release(msg);
        }
    }

    public void saveChannelIdSn(ParamaterModel snData){
        String sn = snData.getSN();
        String channelId = snData.getChannelId();
        if (StringUtils.isNotEmpty(MathUtil.ruleMT(sn))){
            tcpClientApi.saveChannelIdSn(sn,channelId);
            log.info("设备通道绑定{}", JSONUtil.toJsonStr(snData));
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
    }
    //应答心跳包
    public void checkIsHeartbeat(String sn, String channelId, String cmdId, ChannelHandlerContext ctx) {
        if (StringUtils.equals(cmdId, EquipmentCommand.CMD88.getCmdId())) {
            //应答消息48 43 08 00 03 23
            nettyUtil.sendData(ctx, EquipmentCommand.HEART_RATE_RESPONSE.getCmdId());
            log.info("sn号:{},通道:{},心跳包应答:{}", sn, channelId, EquipmentCommand.HEART_RATE_RESPONSE.getCmdId());
        }
    }


}
