package com.code.chapter01NIO.channel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//tcp：ServerSocketChannel服务器 嵌套字通道，允许我们监听TCP连接请求，为每个监听到的请求，创建一个SocketChannel套接字通道
public class ServerSocketChannelDemo {
    private static ExecutorService executorService = Executors.newCachedThreadPool();


    public static void main(String[] args) throws IOException {

        // 实例化
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        // 监听 8007 端口
        serverSocketChannel.socket().bind(new InetSocketAddress(8007));

        while(true) {
            final SocketChannel socketChannel = serverSocketChannel.accept();

            //设置非阻塞模式
            socketChannel.configureBlocking(false);

            executorService.submit(new Runnable() {
                public void run() {
                    try {
                        //此时buffer为写入状态
                        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                        // 此处会阻塞线程..
                        while(true) {
                            /**
                             * 读数据时候，因为是异步的，所以我们必须检查read的返回值，以便判断当前是否读取到了数据
                             * read() 的返回值是读取的字节数；-1表示读取到了对方的结束标志
                             */

                            socketChannel.read(byteBuffer);
                            //把channel数据写到了buffer之后，就要flip了
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
