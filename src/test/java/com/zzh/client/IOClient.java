package com.zzh.client;

import java.io.IOException;
import java.net.Socket;
import java.util.Date;

/**
 * @author zzh
 * @version 1.0
 * @date 2019/12/18 17:21
 */
public class IOClient {
    public static void main(String[] args) {
        new Thread(() -> {
            try {
                Socket socket = new Socket("127.0.0.1", 8000);
                while(true) {
                    socket.getOutputStream().write((new Date() + ": Hello World").getBytes());
                    Thread.sleep(2000);
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }

        }).start();
    }
}
