package com.code.chapter05DecoderAndEncoder.nettyDecoderLearn;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Test;

import java.nio.charset.Charset;
import java.util.Random;

public class StringReplayDecoderTester {

    static String content="lalalalalala";

    @Test
    public void testStringReplayDecoder(){
        ChannelInitializer i=new ChannelInitializer<EmbeddedChannel>() {
            @Override
            protected void initChannel(EmbeddedChannel ch) throws Exception {
                ch.pipeline().addLast(new StringReplayDecoder());
                ch.pipeline().addLast(new StringProcessHandler());
            }
        };
        EmbeddedChannel channel=new EmbeddedChannel(i);
        byte[] bytes=content.getBytes(Charset.forName("UTF-8"));
        for(int j=0;j<100;j++){
            //1-3之间的随机数
            int random= new Random(3).nextInt();
            ByteBuf buf= Unpooled.buffer();
            buf.writeInt(bytes.length*random);
            for(int k=0 ;k<random ;k++){
                buf.writeBytes(bytes);
            }
            channel.writeInbound(buf);
        }
        try{
            Thread.sleep(Integer.MAX_VALUE);
        }catch (Exception e){

        }

    }





}
