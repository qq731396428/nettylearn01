package com.code.chapter05DecoderAndEncoder.EncoderAndDecoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;

import java.util.List;

public class Byte2IntegerCodec extends ByteToMessageCodec<Integer> {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Integer msg, ByteBuf out) throws Exception {
        out.writeInt(msg);
        System.out.println("write Integer ="+msg);
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf in, List<Object> out) throws Exception {
        for(in.readableBytes()>=4){
            int i=in.readInt();
            System.out.println("decoder ="+i);
            out.add(i);
        }
    }
}
