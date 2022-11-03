package com.code.chapter01NIO.channel;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;


/**
 * filechannel不能设置为非阻塞模式
 * 这个main里面的业务不是很高效，去看看FileNIOFastCopyDemo
 *
 * 客户端想传文件：首先传入文件名称，其次是文件大小，然后是文件内容
 */
public class FileChannelDemo {
    public static void main(String[] args) throws IOException {
        // 这里的"rw"是指支持读和写
        RandomAccessFile randomAccessFile = new RandomAccessFile("C:\\Users\\韩明轩\\Desktop\\根据roi计算补贴金额入参.txt","rw");
        FileChannel fileChannel = randomAccessFile.getChannel();

        // 读取文件内容：注意：新建的buffer默认是写入模式
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        int num = fileChannel.read(buffer);
        // 读方法其实是buffer读channel，
        System.out.println("读取的数据量：" + num + "，内容为：\r\n" + new String(buffer.array()));

        buffer.clear();

        // 向文件内追加内容：lalalalala！
        buffer.put("\r\n".getBytes());
        buffer.put("lalalalala！".getBytes());

        buffer.flip();

        while(buffer.hasRemaining()) {
            // 将 Buffer 中的内容写入文件
            fileChannel.write(buffer);
        }
    }
}
