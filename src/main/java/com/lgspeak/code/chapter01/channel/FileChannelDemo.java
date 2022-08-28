package com.lgspeak.code.chapter01.channel;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;


public class FileChannelDemo {
    public static void main(String[] args) throws IOException {
        // 这里的"rw"是指支持读和写
        RandomAccessFile randomAccessFile = new RandomAccessFile("C:\\Users\\Administrator\\Desktop\\data.txt","rw");
        FileChannel fileChannel = randomAccessFile.getChannel();

        // 读取文件内容：
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        int num = fileChannel.read(buffer);
        System.out.println("读取的数据量：" + num + "，内容为：\r\n" + new String(buffer.array()));

        buffer.clear();

        // 向文件内追加内容：祝源码班各位大帅比迎娶白富美！
        buffer.put("\r\n".getBytes());
        buffer.put("祝源码班各位大帅比迎娶白富美！".getBytes());

        buffer.flip();

        while(buffer.hasRemaining()) {
            // 将 Buffer 中的内容写入文件
            fileChannel.write(buffer);
        }
    }
}
