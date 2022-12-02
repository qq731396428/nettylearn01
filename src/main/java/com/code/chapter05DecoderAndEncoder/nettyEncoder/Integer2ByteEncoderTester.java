package com.code.chapter05DecoderAndEncoder.nettyEncoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Test;

public class Integer2ByteEncoderTester {

    @Test
    public void testIntegerToByteDecoder(){
        ChannelInitializer i=new ChannelInitializer<EmbeddedChannel>() {
            @Override
            protected void initChannel(EmbeddedChannel ch) throws Exception {
                ch.pipeline().addLast(new Integer2ByteEncoder());
            }
        };

        EmbeddedChannel channel=new EmbeddedChannel(i);
        for(int j=0;j<100;j++){
            channel.write(j);
        }
        channel.flush();
        //取得通道的数据包
        ByteBuf buf=(ByteBuf) channel.readOutbound();
        while (null!=buf){
            System.out.println("o ="+buf.readInt());
            buf=channel.readOutbound();

        }
        try{
            Thread.sleep(Integer.MAX_VALUE);
        }catch (Exception e){

        }
    }

}
