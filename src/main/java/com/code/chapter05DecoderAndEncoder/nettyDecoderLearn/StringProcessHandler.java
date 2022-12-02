package com.code.chapter05DecoderAndEncoder.nettyDecoderLearn;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class StringProcessHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String s=(String) msg;
        System.out.println("打印一个字符串："+s);

    }
}
