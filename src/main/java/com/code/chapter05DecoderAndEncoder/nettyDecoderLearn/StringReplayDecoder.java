package com.code.chapter05DecoderAndEncoder.nettyDecoderLearn;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

public class StringReplayDecoder extends ReplayingDecoder<StringReplayDecoder.Status> {
    //每一个上层Header-Content内容的读取阶段
    enum Status{
        PARSE_1,
        PARSE_2
    }

    private int length;
    private byte[] inBytes;
    //构造函数需要初始化父类的state属性
    public StringReplayDecoder(){
        super(Status.PARSE_1);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        switch (state()){
            case PARSE_1:
                //从装饰器ByteBuf中读取数据长度
                length=in.readInt();
                inBytes=new byte[length];
                //第一步解析成功
                //进入第二部，并设置“读断点指针”为当前的读取位置
                checkpoint(Status.PARSE_2);
                break;
            case PARSE_2:
                in.readBytes(inBytes ,0, length);
                out.add(new String(inBytes,"UTF-8"));
                checkpoint(Status.PARSE_1);
                break;
            default:
                break;
        }
    }
}
