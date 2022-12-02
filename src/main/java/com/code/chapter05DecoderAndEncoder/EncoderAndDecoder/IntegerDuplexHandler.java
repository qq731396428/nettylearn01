package com.code.chapter05DecoderAndEncoder.EncoderAndDecoder;

import com.code.chapter05DecoderAndEncoder.nettyDecoderLearn.Byte2IntegerDecoderDemo;
import com.code.chapter05DecoderAndEncoder.nettyEncoder.Integer2ByteEncoder;
import io.netty.channel.CombinedChannelDuplexHandler;

/**
 * CombinedChannelDuplexHandler:netty提供的组合其，可以把编码和解码捆绑使用
 */
public class IntegerDuplexHandler extends CombinedChannelDuplexHandler<Byte2IntegerDecoderDemo, Integer2ByteEncoder> {


    public IntegerDuplexHandler(){
        super(new Byte2IntegerDecoderDemo(),new Integer2ByteEncoder());
    }






}
