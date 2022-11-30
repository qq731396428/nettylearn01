package com.code.chapter05DecoderAndEncoder.nettyDecoderLearn;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * 这是一个拿到Integer打印出来的业务处理器
 */
public class IntegerProcessHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)  throws  Exception{
        Integer i=(Integer) msg;
        System.out.println("打印一个整数："+i);

    }


}
