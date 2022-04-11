package com.webserver.core;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

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
            //1 解析请求
            //1.1 解析请求行
            String line = readLine();
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
            //1.2解析消息头
            Map<String,String >hearers = new HashMap<>();
            while (true){
                line = readLine();
                if (line.isEmpty()) {
                    break;
                }
                System.out.println("消息头:"+line);
                //将消息头按照"冒号空格"拆分为消息头的名字和值并以key,value存入headers
                array = line.split(":\\s");
                hearers.put(array[0], array[1]);


            }
            System.out.println("headers:"+ hearers);




        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    private String readLine() throws IOException {
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
        }return builder.toString().trim();
    }

}
