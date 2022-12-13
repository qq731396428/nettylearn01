package com.code.chapter06JsonAndProtoBuf;


import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;

import java.nio.charset.Charset;

/**
 * 模拟半包
 */
public class NettyDumpSendClient {
    private int serverPort;
    private String serverIp;
    Bootstrap b=new Bootstrap();
    NettyDumpSendClient(String ip,int port){
        this.serverPort=port;
        this.serverIp=ip;
    }

    public void runClient() throws InterruptedException {
        //创建反应器线程组
        //....省却Bootstrap启动器配置和启动
        ChannelFuture f=b.connect();
        //阻塞，直到连接完成
        f.sync();
        Channel channel=f.channel();
            //发送文字
            String content="lalalalalala";
            byte[] bytes=content.getBytes(Charset.forName("UTF-8"));
            for(int i=0;i<1000 ;i++){
                //发送ByteBuf
                ByteBuf buffer=channel.alloc().buffer();
                buffer.writeBytes(bytes);
                channel.writeAndFlush(buffer);

            }
        //省略关闭客户端
    }

    public static void main(String[] args) throws InterruptedException {
        int port =8899;
        String ip="127.0.0.1";
        new NettyDumpSendClient(ip,port).runClient();
    }

}
