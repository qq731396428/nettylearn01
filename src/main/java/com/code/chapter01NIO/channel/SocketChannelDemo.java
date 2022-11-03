package com.code.chapter01NIO.channel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;


/**
 * SocketChannel负责连接传输，ServerSocketChannel负责连接的接听（用于服务器端）
 *
 */
public class SocketChannelDemo {
    public static void main(String[] args) throws IOException {
        // 打开一个通道
        SocketChannel socketChannel = SocketChannel.open();

        //这样可以设置非阻塞模式 ，ps：阻塞模式就是OIO那种
        socketChannel.configureBlocking(false);


        // 发起连接，连接到本机的 EchoServer
        socketChannel.connect(new InetSocketAddress("127.0.0.1", 8007));



        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        byteBuffer.put("abc".getBytes());
        // 接下来需要将buffer中的数据读取到channel内，其实底层是 socket 缓冲区..
        // 所以需要将buffer从写模式切换到读模式
        byteBuffer.flip();

        socketChannel.write(byteBuffer);

        ByteBuffer echoBuffer = ByteBuffer.allocate(1024);
        socketChannel.read(echoBuffer);

        System.out.println(new String(echoBuffer.array()));
    }
}
