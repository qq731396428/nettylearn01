package com.code.chapter05DecoderAndEncoder.nettyDecoderLearn;


import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import org.junit.Test;

import java.nio.charset.Charset;

/**
 * head-content协议：简化版的数据包中包含了 长度（4字节） --> 版本字段（2字节） --> content字段（52字节）
 * 使用LengthFieldBasedFrameDecoder 解码器 解这个协议
 * 构造器参数：
 * maxFrameLength可以为1024，表示数据包最大长度1024
 * lengthFieldOffset为0，表示长度字段处于数据包的起始位置
 * lengthFieldLength实例中的值为4，表示长度为4字节。
 * lengthAdjustment为2，长度调整值的计算方法：内容字段偏移量-长度字段偏移量-长度字段长度=6-0-4=2
 * initialBytesToStrip为6，表示content内容的字节数组时，抛弃最前面的6个字节数据。人话：长度、版本字段的值被抛弃
 */
public class NettyOpenBoxDecoder {

    public static final int VERSION=100;
    public static String content="发送消息：发送消息：消息";

    @Test
    public void testLengthFieldBasedFrameDecoder2(){
        try{
            final LengthFieldBasedFrameDecoder spliter=new LengthFieldBasedFrameDecoder(1024,0,4,2,6);
            ChannelInitializer i =new ChannelInitializer<EmbeddedChannel>() {
                @Override
                protected void initChannel(EmbeddedChannel ch) throws Exception {
                    ch.pipeline().addLast(spliter);
                    ch.pipeline().addLast(new StringDecoder(Charset.forName("UTF-8")));
                    ch.pipeline().addLast(new StringProcessHandler());
                }
            };
            EmbeddedChannel channel=new EmbeddedChannel(i);
            for(int j=1;j<=100;j++){
                ByteBuf buf= Unpooled.buffer();
                String s=j+"次发送->"+content;
                byte[] bytes=s.getBytes("UTF-8");
                buf.writeInt(bytes.length);
                buf.writeChar(VERSION);
                buf.writeBytes(bytes);
                channel.writeInbound(buf);
            }
            Thread.sleep(Integer.MAX_VALUE);
        }catch (Exception e){

        }


    }


}
