package com.lgspeak.code.chapter01.channel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;


public class DatagramChannelDemo {
    public static void main(String[] args) throws IOException {
        DatagramChannel channel = DatagramChannel.open();
        channel.socket().bind(new InetSocketAddress(9090));

        ByteBuffer buf = ByteBuffer.allocate(1024);

        while(true) {
            channel.receive(buf);

            System.out.println("服务器收到UDP报文，内容为：");
            System.out.println(new String(buf.array()));
            buf.clear();
        }

    }
}
