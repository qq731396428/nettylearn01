package com.lgspeak.code.chapter01.channel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ServerSocketChannelDemo {
    private static ExecutorService executorService = Executors.newCachedThreadPool();

    public static void main(String[] args) throws IOException {

        // 实例化
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        // 监听 8007 端口
        serverSocketChannel.socket().bind(new InetSocketAddress(8007));

        while(true) {
            final SocketChannel socketChannel = serverSocketChannel.accept();

            executorService.submit(new Runnable() {
                public void run() {
                    try {
                        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                        // 此处会阻塞线程..
                        while(true) {
                            socketChannel.read(byteBuffer);
                            byteBuffer.flip();
                            socketChannel.write(byteBuffer);
                            byteBuffer.clear();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        if(socketChannel.isOpen() || socketChannel.isConnected()) {
                            try {
                                socketChannel.close();
                            } catch (IOException e) {
                            }
                        }
                    }
                }
            });
        }
    }
}
