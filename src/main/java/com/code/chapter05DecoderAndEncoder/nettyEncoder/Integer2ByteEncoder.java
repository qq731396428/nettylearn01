package com.code.chapter05DecoderAndEncoder.nettyEncoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class Integer2ByteEncoder extends MessageToByteEncoder<Integer> {


    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Integer msg, ByteBuf out) throws Exception {
        out.writeInt(msg);
        System.out.println("encoder Integer ="+msg);
    }
}
