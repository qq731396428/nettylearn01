package com.code.chapter04Netty.nettyEchoDemo;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class NettyEchoServerHandlerDemo extends ChannelInboundHandlerAdapter {

    /**
     *
     * Netty版本的EchoServerHandler 回显服务器处理器，继承自ChannelInboundHandlerAdapter，然后覆盖了channelRead方法，这个方法在可读IO事件到来时，被流水线回调
     * 这个回显服务器处理器的逻辑分两步：
     * （1）从channelRead方法的msg参数-->msg的类型有流水线的上一站决定-->netty4.1之后bytebuf的默认类型为Direct ByteBuf直接内存
     * （2）调用ctx.channel().writeAndFlush()把数据写回客户端
     */

    public static final NettyEchoServerHandlerDemo INSTANCE=new NettyEchoServerHandlerDemo();

    @Override
    public void channelRead(ChannelHandlerContext ctx,Object msg) throws Exception{
        ByteBuf in=(ByteBuf) msg;
        System.out.println("msg.type:"+(in.hasArray()?"堆内存":"直接内存"));
        int len=in.readableBytes();
        byte[] arr=new byte[len];
        in.getBytes(0,arr);
        System.out.println("server received:"+ new String(arr,"UTF-8"));
        System.out.println("回写前，msg.refCnt:"+((ByteBuf) msg).refCnt());
        //写回任务，异步数据
        ChannelFuture f=ctx.writeAndFlush(msg);
        f.addListener((ChannelFutureListener)->{
            System.out.println("写回后，msg.refCnt:"+((ByteBuf) msg).refCnt());
        });

    }


}
