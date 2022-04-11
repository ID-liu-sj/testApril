package com.webserver.core;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
/**
 * 该线程任务负责与指定客户端完成HTTP交互
 * 与客户端交流的流程分成三步:
 * 1:解析请求
 * 2:处理请求
 * 3:发送响应
 */
public class ClientHandler implements Runnable {
    private Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            //读取客户端发过来的消息
            InputStream in = socket.getInputStream();
            char pre = 'a', cur = 'a';//pre上次读取的字符，cur本次读取的字符
            StringBuilder builder = new StringBuilder();
            int d;
            while ((d = in.read()) != -1) {
                cur = (char)d;
                if (pre==13&&cur==10){
                    break;
                }
                builder.append(cur);
                pre = cur;
            }
            String line = builder.toString().trim();
            System.out.println(line);
            //请求行相关信息
            String method;
            String uri;
            String protocol;

            String[] array = line.split("\\s");
            method = array[0];
            uri = array[1];
            protocol = array[2];
            System.out.println("method:"+method);
            System.out.println("uri:"+uri);
            System.out.println("protocol:"+protocol);








        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
