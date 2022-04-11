package com.webserver.http;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * HTTP请求对象
 * 该类的每一个实例用于表示一个HTTP请求内容
 * 每个请求由三部分构成:
 * 请求行，消息头，消息正文
 */
public class HttpServletRequest {
    private Socket socket;
    private String method;
    private String uri;
    private String protocol;

    private Map<String,String>headers = new HashMap<>();
    public HttpServletRequest(Socket socket) throws IOException {
        this.socket = socket;
        //解析请求行
        parseRequestLine();
        //解析消息头
        parseHeaders();
        //解析消息正文
        parseContent();


    }

    private void parseRequestLine() throws IOException {
        //1.1 解析请求行
        String line = readLine();
        System.out.println(line);
        //请求行相关信息

        String[] array = line.split("\\s");
        method = array[0];
        uri = array[1];
        protocol = array[2];
        System.out.println("method:" + method);
        System.out.println("uri:" + uri);
        System.out.println("protocol:" + protocol);
    }

    private void parseHeaders() throws IOException {
        //1.2解析消息头
        Map<String, String> hearers = new HashMap<>();
        while (true) {

            String line = readLine();
            if (line.isEmpty()) {
                break;
            }
            System.out.println("消息头:" + line);
            //将消息头按照"冒号空格"拆分为消息头的名字和值并以key,value存入headers
            String[] array = line.split(":\\s");
            hearers.put(array[0], array[1]);


        }
        System.out.println("headers:" + hearers);

    }

    private void parseContent(){}
    private String readLine() throws IOException {
        InputStream in = socket.getInputStream();
        char pre = 'a', cur = 'a';//pre上次读取的字符，cur本次读取的字符
        StringBuilder builder = new StringBuilder();
        int d;
        while ((d = in.read()) != -1) {
            cur = (char) d;
            if (pre == 13 && cur == 10) {
                break;
            }
            builder.append(cur);
            pre = cur;
        }
        return builder.toString().trim();
    }

    public String getMethod() {
        return method;
    }

    public String getUri() {
        return uri;
    }

    public String getProtocol() {
        return protocol;
    }
    public String getHeaders(String name){
        return headers.get(name);
    }
}
