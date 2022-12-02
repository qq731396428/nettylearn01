package com.code.chapter05DecoderAndEncoder.nettyDecoderLearn;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class StringIntegerHeaderDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf buf, List<Object> out) throws Exception {
        //可读字节小于4，消息头还没有读满
        if(buf.readableBytes()<4){
            return;
        }
        //消息头已经完整
        //在真正开始从缓冲区读取数据之前，调用markReaderIndex() 设置回滚点
        //回滚点为消息头的readIndex读指针位置
        buf.markReaderIndex();
        int length=buf.readInt();

    }
}
