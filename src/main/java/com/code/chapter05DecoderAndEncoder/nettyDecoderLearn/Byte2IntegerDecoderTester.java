package com.code.chapter05DecoderAndEncoder.nettyDecoderLearn;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Test;

public class Byte2IntegerDecoderTester {

    /**
     * 入站处理器的处理顺序是从前到后：
     * 解码器在前，处理器在后
     *
     */
    @Test
    public void testByteToIntegerDecoder(){
        ChannelInitializer i=new ChannelInitializer<EmbeddedChannel>() {
            @Override
            protected void initChannel(EmbeddedChannel ch) throws Exception {
                ch.pipeline().addLast(new NettyDecoderIntegerDemo());
                ch.pipeline().addLast(new IntegerProcessHandler());
            }
        };
        EmbeddedChannel channel=new EmbeddedChannel(i);
        for(int j=0;j<100;j++){
            ByteBuf buf= Unpooled.buffer();
            buf.writeInt(j);
            channel.writeInbound(buf);
        }
        try{
            Thread.sleep(Integer.MAX_VALUE);

        }catch (Exception e){

        }



    }





}
