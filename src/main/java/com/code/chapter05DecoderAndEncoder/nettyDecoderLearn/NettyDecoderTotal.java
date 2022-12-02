package com.code.chapter05DecoderAndEncoder.nettyDecoderLearn;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import org.junit.Test;

import java.util.Random;


public class NettyDecoderTotal {
    /**
     * 开箱即用的Netty解码器：
     * FixedLengthFrameDecoder：固定长度数据包解码器-->frame数据帧，也称为数据包
     * LineBasedFrameDecoder：行分割数据包解码器，使用换行符作为数据包的边界
     * DelimiterBasedFrameDecoder：自定义分隔符数据包解码器
     * LengthFieldBasedFrameDecoder：自定义长度数据包解码器
     */

    static String spliter="\r\n";
    static String content="lalalalal:ooooooo:aaaaa";

    @Test
    public void testLineBasedFrameDecoder(){
        try{
            ChannelInitializer i=new ChannelInitializer<EmbeddedChannel>() {
                @Override
                protected void initChannel(EmbeddedChannel ch) throws Exception {
                    ch.pipeline().addLast(new LineBasedFrameDecoder(1024));
                    ch.pipeline().addLast(new StringDecoder());
                    ch.pipeline().addLast(new StringProcessHandler());
                }
            };
            EmbeddedChannel channel=new EmbeddedChannel(i);
            for(int j=0;j<100;j++){
                //1-3之间的随机数
                int random= new Random(3).nextInt();
                ByteBuf buf= Unpooled.buffer();

                for(int k=0 ;k<random ;k++){
                    buf.writeBytes(content.getBytes("UTF-8"));
                }
                buf.writeBytes(spliter.getBytes("UTF-8"));
                channel.writeInbound(buf);
            }
            Thread.sleep(Integer.MAX_VALUE);
        }catch (Exception e){

        }


    }


}
