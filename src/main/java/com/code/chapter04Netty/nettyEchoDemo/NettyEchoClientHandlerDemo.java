package com.code.chapter04Netty.nettyEchoDemo;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

@ChannelHandler.Sharable
public class NettyEchoClientHandlerDemo extends ChannelInboundHandlerAdapter {

    public static final NettyEchoClientHandlerDemo INSTANCE =new NettyEchoClientHandlerDemo();

    @Override
    public void channelRead(ChannelHandlerContext ctx,Object msg) throws Exception{
        ByteBuf byteBuf=(ByteBuf) msg;
        int len=byteBuf.readableBytes();
        byte[] arr=new byte[len];
        byteBuf.getBytes(0,arr);
        System.out.println("client received:"+new String(arr,"UTF-8"));
        // 释放byteBuf的两种方法
        //（1）手动释放ByteBuf
        //byteBuf.release();
        //（2）调用父类的入站方法，将msg向后传递
        super.channelRead(ctx,msg);

    }


}
