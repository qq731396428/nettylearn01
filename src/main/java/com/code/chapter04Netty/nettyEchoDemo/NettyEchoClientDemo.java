package com.code.chapter04Netty.nettyEchoDemo;


import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.time.format.DateTimeFormatterBuilder;
import java.util.Date;
import java.util.Scanner;

public class NettyEchoClientDemo {

    private int serverPort;
    private String serverIp;
    Bootstrap b=new Bootstrap();

    public NettyEchoClientDemo(String ip,int port){
        this.serverPort=port;
        this.serverIp=ip;
    }

    public void runClient(){
        EventLoopGroup workerLoopGroup=new NioEventLoopGroup();
        try{
            //1 设置反应器 线程组
            b.group(workerLoopGroup);
            //2 设置nio类型的通道
            b.channel(NioSocketChannel.class);
            //3 设置监听端口
            b.remoteAddress(serverIp,serverPort);
            //4 设置通道的参数
            b.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
            //5 装配子通道流水线
            b.handler(new ChannelInitializer<SocketChannel>() {
                //有连接到达时会创建一个通道
                @Override
                protected void initChannel(SocketChannel ch)throws Exception{
                    //流水线管理子通道中的Handler 业务处理器
                    //向子通道流水线添加一个Handler 业务处理器
                    ch.pipeline().addLast(NettyEchoClientHandlerDemo.INSTANCE);
                }
            });
            ChannelFuture f=b.connect();
            f.addListener((ChannelFuture futureListener)->
            {
                if(futureListener.isSuccess()){
                    System.out.println("echoClient 客户端连接成功");
                }else{
                    System.out.println("echoClient 客户端连接失败");
                }

            });
            //阻塞，直到连接成功
            f.sync();
            Channel channel=f.channel();
            Scanner scanner=new Scanner(System.in);
            System.out.println("输入内容");
            while(scanner.hasNext()){
                //获取输入的内容
                String next=scanner.next();
                byte[] bytes=(">>"+next).getBytes("UTF-8");
                //发送ByteBuf
                ByteBuf buf=channel.alloc().buffer();
                buf.writeBytes(bytes);
                channel.writeAndFlush(buf);
                System.out.println("请输入发送内容");

            }


        }catch (Exception e){

        }finally {
            //从容关闭EventLoopGroup
            //释放掉所有资源，包括创建的线程
            workerLoopGroup.shutdownGracefully();
        }
    }

    //省略main方法

}
