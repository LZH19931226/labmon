package com.hc.util;


import com.hc.bean.MTOnlineBean;
import com.hc.config.RedisTemplateUtil;
import com.hc.my.common.core.bean.ParamaterModel;
import com.hc.socketServer.IotServer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

@Component
public class NettyUtil {

    private static final Logger log = LoggerFactory.getLogger(NettyUtil.class);


    @Autowired
    private RedisTemplateUtil redisDao;

    //private static ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);


    public void sendData(ChannelHandlerContext ctx, Object sendData) {
        writeDataByte(ctx, sendData);
    }

    public void sendData(Channel channel, Object sendData) {
        writeDataByte(channel, sendData);
    }

    public void sendData(Channel channel, byte[] sendData) {
        writeDataBytedelphi(channel, sendData);
    }

    public List<Channel> getChannelList(String tid) {
        List<Channel> channels = new ArrayList<>();
        for (Channel channel : IotServer.onlineChannels) {
            channels.add(channel);
        }
        return channels;
    }

    public List<Channel> getAllChannel() {
        List<Channel> channels = new ArrayList<>();
        for (Channel channel : IotServer.onlineChannels) {
            channels.add(channel);
        }
        return channels;
    }

    public Channel getChannelByTid(String tid) {
//        Set<String> keys =  redisDao.keys(tid+"-*-"+MTOnlineBean.class.getSimpleName());
//        if (keys.size() == 0)return null;
//        Iterator<String> keyIt = keys.iterator();
//        String key = keyIt.next();
//        String[] part = key.split("-");
//        String cid = part[1];
//        System.out.println(cid);
        String string = (String) redisDao.boundValueOps(tid + ":" + MTOnlineBean.class.getSimpleName()).get();
        for (Channel channel : IotServer.onlineChannels) {
            if (channel.id().asShortText().equals(string)) {
                return channel;
            }
        }
        return null;
    }


      public String getSnByCid(String cid){
          Object o = redisDao.boundValueOps(cid + ":" + ParamaterModel.class.getSimpleName()).get();
          String s = (String) o;
          return  s;
      }
//
//    public Map<String,Object> getAllChannelMap(){
//        Set<String> keys =  redisDao.keys("*-"+MTOnlineBean.class.getSimpleName());
//        if (keys.size() == 0)return null;
//        Map<String,Object> tidChannelMap = new HashMap<>();
//        Map<String,Channel> channelMap = new HashMap<>();
//        for (String key : keys) {
//            String[] part = key.split("-");
//            String tid = part[0];
//            String cid = part[1];
//            channelMap.put(cid,null);
//            tidChannelMap.put(tid,cid);
//        }
//        for (Channel channel : IotServer.onlineChannels) {
//            String id = channel.id().asShortText();
//            if (channelMap.containsKey(id)){
//                channelMap.put(id,channel);
//            }
//        }
//        for (Map.Entry<String, Object> entry : tidChannelMap.entrySet()) {
//            String cid = (String) entry.getValue();
//            if (!channelMap.containsKey(cid))continue;
//            tidChannelMap.put(entry.getKey(),channelMap.get(cid));
//        }
//        return tidChannelMap;
//    }
//    public List<Channel> getChannelByTids(List<String> tids){
//
//    	List<Channel>  channels =new ArrayList<>();
//    	for (String tid : tids) {
//    		for (Channel channel : IotServer.onlineChannels) {
//        		String cid = redisDao.boundValueOps(tid+"-*-"+MTOnlineBean.class.getSimpleName()).get();
//        		 if (channel.id().asShortText().equals(cid)) {
//        			 channels.add(channel);
//                 }
//
//        	}
//		}
//
//        Set<String> keys =  redisDao.keys("*-"+MTOnlineBean.class.getSimpleName());
//        if (keys.size() == 0)return null;
//        Map<String,Object> tidChannelMap = new HashMap<>(tids.size());
//        Map<String,Channel> channelMap = new HashMap<>(tids.size());
//        for (String key : keys) {
//            String[] part = key.split("-");
//            String tid = part[0];
//            if (!tids.contains(tid))continue;
//            String cid = part[1];
//            channelMap.put(cid,null);
//            tidChannelMap.put(tid,cid);
//        }
//        for (Channel channel : IotServer.onlineChannels) {
//            String id = channel.id().asShortText();
//            if (channelMap.containsKey(id)){
//                channelMap.put(id,channel);
//            }
//        }
//        for (Map.Entry<String, Object> entry : tidChannelMap.entrySet()) {
//            String cid = (String) entry.getValue();
//            if (!channelMap.containsKey(cid))continue;
//            tidChannelMap.put(entry.getKey(),channelMap.get(cid));
//        }
//        return channels;
//    }

    private void writeDataByte(ChannelHandlerContext ctx, Object sendData) {
        if (ctx == null) return;
        try {
            ByteBuf data = buildData(sendData);
            ctx.write(data);
            ctx.flush();
        } catch (Exception e) {
            log.error("发送数据异常", e);
        }
    }

    private void writeDataByte(Channel channel, Object sendData) {
        if (channel == null) return;
        try {
            ByteBuf data = buildData(sendData);
            channel.write(data);
            channel.flush();
        } catch (Exception e) {
            log.error("发送数据异常", e);
        }
    }


    private void writeDataBytedelphi(Channel channel, byte[] sendData) {
        if (channel == null) return;
        try {
            ByteBuf data = buildDatadelphi(sendData);
            channel.write(data);
            channel.flush();
        } catch (Exception e) {
            log.error("发送数据异常", e);
        }
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
