package com.code.chapter04Netty.discardDemo;


import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

import java.nio.ByteBuffer;

/**
 * 这个类：丢掉所有消息
 * netty有个概念：入站（输入），出站（输出）
 * netty的Handler处理器需要处理多种IO事件
 */

/**
 * ChannelInboundHandler这个接口，是入站处理接口，ChannelInboundHandlerAdapter这是Netty入站处理的默认实现
 * 如果要实现自己的入站处理器：继承ChannelInboundHandlerAdapter 就可以，然后重写 channelRead方法就行
 */
public class NettyDiscardHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx,Object msg) throws Exception{
        /**
         * ByteBuf 对应的是NIO的缓冲区
         */
        ByteBuf in =(ByteBuf)  msg;
        try{
            System.out.println("收到消息，丢弃ing");
            while(in.isReadable()){
                System.out.println((char)in.readByte());
            }
        }finally {
            ReferenceCountUtil.release(msg);

        }

    }

}
