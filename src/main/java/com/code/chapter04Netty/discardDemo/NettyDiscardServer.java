package com.code.chapter04Netty.discardDemo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.sctp.nio.NioSctpServerChannel;
import io.netty.channel.socket.SocketChannel;

/**
 * netty 是基于反应器模式实现的
 * netty的反应器类型为NioEventLoopGroup : 下面有两个实现：boss是包工头（负责服务器通道的新连接），worker是工人（负责传输通道的IO事件处理），
 * handler处理器（也称为处理程序）handler处理器的作用是对应到IO事件，实现IO事件的业务处理（）
 *
 * netty服务的启动类是：ServerBootstrap
 *
 */

/***
 * 这个例子中有netty的组件：
 * 服务启动器、缓冲区、反应器、Handler业务处理器、Future异步任务监听、数据传输通道
 *
 * @author 韩明轩
 * <br/>date 2022-11-14
 */
public class NettyDiscardServer {

    private final int serverPort;

    ServerBootstrap b=new ServerBootstrap();
    public NettyDiscardServer(int port){
        this.serverPort=port;
    }

    public void runServer(){
        //创建反应器线程组
        EventLoopGroup bossLoopGroup=new NioEventLoopGroup(1);
        EventLoopGroup workerLoopGroup=new NioEventLoopGroup();

        try{
            //1.设置反应器线程组
            b.group(bossLoopGroup,workerLoopGroup);
            //2.设置nio类型的通道
            b.channel(NioSctpServerChannel.class);
            //3.设置监听端口
            b.localAddress(serverPort);
            //4.设置通道的参数
            b.option(ChannelOption.SO_KEEPALIVE,true);
            b.option(ChannelOption.ALLOCATOR , PooledByteBufAllocator.DEFAULT);

            //5 装配子通道流水线
            b.childHandler(new ChannelInitializer<SocketChannel>() {
                //有连接到达时会创建一个通道
                @Override
                protected void initChannel(SocketChannel ch) throws Exception{
                    // 流水线管理子通道中的handler处理器
                    //向子通道流水线添加一个handler处理器
                    ch.pipeline().addLast(new NettyDiscardHandler());
                }
            });

            //6 开始绑定服务器
            // 通过调用sync同步方法阻塞直到绑定成功
            ChannelFuture channelFuture=b.bind().sync();
            System.out.println("服务器启动成功，监听端口："+ channelFuture.channel().localAddress());
            //7 等待通道关闭的异步任务结束
            // 服务监听通道会一直等到通道关闭的异步任务结束
            ChannelFuture closeFuture=channelFuture.channel().closeFuture();
            closeFuture.sync();


        }catch(Exception e){

        }finally {
            //8 关闭EventLoopGroup
            // 释放掉所有资源包括创建的线程
            workerLoopGroup.shutdownGracefully();
            bossLoopGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        int port=8899;
        new NettyDiscardServer(port).runServer();
    }

}
