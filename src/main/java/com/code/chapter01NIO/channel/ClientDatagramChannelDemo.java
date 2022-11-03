package com.code.chapter01NIO.channel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;


public class ClientDatagramChannelDemo {
    public static void main(String[] args) throws IOException {
        DatagramChannel channel = DatagramChannel.open();
        channel.socket().bind(new InetSocketAddress(8848));

        String newData = "New String to write to file..."
                + System.currentTimeMillis();

        ByteBuffer buf = ByteBuffer.allocate(1024);
        buf.put(newData.getBytes());
        buf.flip();

        int bytesSent = channel.send(buf, new InetSocketAddress("127.0.0.1", 9090));
    }
}
