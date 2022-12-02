package com.hc.util;


import com.hc.socketServer.IotServer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

@Component
@Slf4j
public class NettyUtil {


    public void sendData(ChannelHandlerContext ctx, Object sendData) {
        writeDataByte(ctx, sendData);
    }

    public void sendData(Channel channel, Object sendData) {
        writeDataByte(channel, sendData);
    }




    public Channel getChannelByTid(String channelId) {
        for (Channel channel : IotServer.onlineChannels) {
            if (channel.id().asShortText().equals(channelId)) {
                return channel;
            }
        }
        return null;
    }


    //在EventLoop的支持线程外使用channel
    private void writeDataByte(ChannelHandlerContext ctx, Object sendData) {
        if (ctx == null) return;
        Channel channel = ctx.channel();
        channel.eventLoop().execute(() -> {
            ByteBuf data = null;
            try {
                data = buildData(sendData);
            } catch (DecoderException e) {
                e.printStackTrace();
            }
            channel.writeAndFlush(data);
        });
//        try {
//            ByteBuf data = buildData(sendData);
//            ctx.write(data);
//            ctx.flush();
//        } catch (Exception e) {
//            log.error("发送数据异常", e);
//        }
    }

    private void writeDataByte(Channel channel, Object sendData) {
        if (channel == null) return;
        channel.eventLoop().execute(() -> {
            ByteBuf data = null;
            try {
                data = buildData(sendData);
            } catch (DecoderException e) {
                e.printStackTrace();
            }
            channel.writeAndFlush(data);
        });
//        try {
//            ByteBuf data = buildData(sendData);
//            channel.write(data);
//            channel.flush();
//        } catch (Exception e) {
//            log.error("发送数据异常", e);
//        }
    }

    private static ByteBuf buildData(Object sendData) throws DecoderException {

        String content = (String) sendData;
        //将发送的数据转换成16进制
        byte[] bytes = Hex.decodeHex(content.toCharArray());

        return Unpooled.copiedBuffer(bytes);
    }

    private static ByteBuf buildDatadelphi(byte[] sendData) throws DecoderException {

        return Unpooled.copiedBuffer(sendData);
    }


    public static void main(String[] args) throws Exception {

//        String charString = "484308000323";
//
//        String fromHex = HexStringUtils.fromHex(charString);
//
//        ByteBuf buildData = buildData(fromHex);
//
//        byte[] receiveMsgBytes = new byte[buildData.readableBytes()];
//        buildData.readBytes(receiveMsgBytes);
//        String encodeHexString = Hex.encodeHexString(receiveMsgBytes);
//
//        System.out.println(encodeHexString);

        String s = "你好吗";
        SocketSend("39.104.53.171","4001",s.getBytes());
        System.out.println("发送成功");
    }

    public  static void SocketSend(String ip, String port, byte[] data) {
        Socket socket =null;
        OutputStream outputStream=null;
          try {
               socket = new Socket(ip, Integer.parseInt(port));
               Thread.sleep(100);
               outputStream = socket.getOutputStream();
               outputStream.write(data);
          }catch (Exception e){


            }finally {
              try {
                  outputStream.flush();
              } catch (IOException e) {
                  e.printStackTrace();
              }
              try {
                  outputStream.close();
              } catch (IOException e) {
                  e.printStackTrace();
              }
              try {
                  socket.close();
              } catch (IOException e) {
                  e.printStackTrace();
              }

          }


    }

}
