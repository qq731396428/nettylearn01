package com.code.chapter04Netty.nettyEchoDemo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;

public class NetyEchoServerDemo {

    /**
     * 这里实现一个简单的：从服务器端读取客户端数据，然后将数据直接回显到console控制台：
     * （1）服务器端serverBootstrap的装配和使用
     * （2）服务器端NettyEchoServerHandler 入站处理器的channelRead 入站处理方法编写
     */


    ServerBootstrap b=new ServerBootstrap();
    /**
     * 服务器端的ServerBootstrap装配和启动过程：
     */
    public void runServer(){
        //创建反应器线程组
        EventLoopGroup boosLoopGroup=new NioEventLoopGroup(1);
        EventLoopGroup workerLoopGroup=new NioEventLoopGroup();
        //伪代码省略配置：1反应器线程组 ；2通道类型；3通道选项；
        //4 装配子通道流水线
        b.childHandler(new ChannelInitializer<SocketChannel>(){
            //有连接到达时会创建一个通道
            @Override
            protected void initChannel(SocketChannel ch) throws Exception{
                //流水线管理子通道中的Handler业务处理器
                //向子通道流水线添加一个Handler业务处理器
                ch.pipeline().addLast(NettyEchoServerHandlerDemo.INSTANCE);
            }
        });
        //伪代码：省略：启动，等待，优雅关闭
    }
    //省略main方法


}
