package com.code.chapter05DecoderAndEncoder.nettyDecoderLearn;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

public class Byte2IntegerReplayingDecoderDemo extends ReplayingDecoder {

    /**
     * Byte2IntegerDecoder整数解码器的问题：需要检查ByteBuf长度，如果有足够的字节数，才可以进行Integer的读取
     * ReplayingDecoder 可以省去长度的判断
     * ReplayingDecoder 类 是ByteToMessageDecoder的子类，作用：
     * （1）在读取ByteBuf缓冲区的数据之前，需要检查缓冲区是否有足够的字节
     * （2）若ByteBuf中有足够的字节，则会正常读取；反之，如果没有足够的字节，则会停止解码。
     */

    /**
     * 它内部重新定义了一个新的二进制缓冲区类：ReplayingDecoderBuffer，特点是：
     * 在缓冲区真正读数据之前，首先进行长度的判断，如果长度合格->会读取数据；否则，抛出ReplayError。ReplayingDecoder捕获到ReplayError后，会留着数据，等待下一次IO时间到来时读取
     * ReplayingDecoder的作用，远远不止进行长度判断，他更重要的作用是用于分包传输的场景
     * @param ctx
     * @param in
     * @param out
     * @throws Exception
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        int i=in.readInt();
        System.out.println("解码出一个整数");
        out.add(i)

    }

}
