package com.code.chapter05DecoderAndEncoder.nettyEncoder;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

public class String2IntegerEncoder extends MessageToMessageEncoder<String> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, String s, List<Object> list) throws Exception {
        char[] array=s.toCharArray();
        for(char a:array){
            //48 是0，57是9
            if(a>=48 && a<=57){
                list.add(new Integer(a));
            }
        }

    }
}
