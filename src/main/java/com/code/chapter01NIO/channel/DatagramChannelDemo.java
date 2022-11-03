package com.code.chapter01NIO.channel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;


/***
 *
 * 这个是UDP报文的channel
 * @return
 * @author 韩明轩
 * <br/>date 2022-11-03
 */
public class DatagramChannelDemo {
    public static void main(String[] args) throws IOException {
        DatagramChannel channel = DatagramChannel.open();
        channel.socket().bind(new InetSocketAddress(9090));

        ByteBuffer buf = ByteBuffer.allocate(1024);


        // api不是read 而是 receive
        channel.receive(buf);

        System.out.println("服务器收到UDP报文，内容为：");
        System.out.println(new String(buf.array()));

        /*切换读模式*/
        buf.flip();
        channel.send(buf,new InetSocketAddress(9090));


        //最后直接关掉
        channel.close();

    }
}
