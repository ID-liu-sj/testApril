package com.webserver.core;

import com.sun.jnlp.FileOpenServiceNSBImpl;
import com.webserver.http.HttpServletRequest;

import java.io.*;
import java.lang.reflect.Field;
import java.net.Socket;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
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
            HttpServletRequest request = new HttpServletRequest(socket);

            //2 处理请求
            String path = request.getUri();
            //3 发送响应
            File root = new File(
                    ClientHandler.class.getClassLoader().getResource(".").toURI());
            File staticDir = new File(root, "static");
            File file = new File(staticDir, path);
            System.out.println("资源是否存在:" + file.exists());
            int statusCode;
            String statusReason;
            if (file.isFile()) {
                statusCode = 200;
                statusReason = "OK";
            } else {
                statusCode = 404;
                statusReason = "NotFound";
                file = new File(staticDir, "root/404.html");
            }

            //3.1发送状态行
            println("HTTP/1.1 "+statusCode+" "+statusReason);
            //3.2发送响应头
            println("Content-Type: text/html");
            println("Content-Length: " + file.length());
            OutputStream out = socket.getOutputStream();
            out.write(13);
            out.write(10);
            //3.3发送响应正文
            FileInputStream fis = new FileInputStream(file);
            int len;
            byte[] data = new byte[1024 * 10];
            while ((len = fis.read(data)) != -1) {
                out.write(data, 0, len);
            }


        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    private void println(String line) throws IOException {
        OutputStream out = socket.getOutputStream();
        byte[] data = line.getBytes(StandardCharsets.ISO_8859_1);
        out.write(data);
        out.write(13);
        out.write(10);

    }


}
