package com.hc.utils;


import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class NettyUtil {





    public  static void SocketSend(String ip, String port, byte[] data) {
        Socket socket =null;
        OutputStream outputStream=null;
          try {
               socket = new Socket(ip, Integer.parseInt(port));
               Thread.sleep(50);
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
