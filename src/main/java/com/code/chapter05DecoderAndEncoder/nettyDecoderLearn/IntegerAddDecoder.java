package com.code.chapter05DecoderAndEncoder.nettyDecoderLearn;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;


/**
 * 基于ReplayingDecoder基础解码器，编写一个整数相加解码器
 *
 * IntegerAddDecoder是有状态的，不能在不同的通道之间进行共享，更加进一步：ReplayingDecoder类型和其所有子类，都需要保存状态信息，是有状态的，不适合在不同的通道之间
 *
 */
public class IntegerAddDecoder extends ReplayingDecoder<IntegerAddDecoder.Status> {
    enum Status {
        //这表示的是截断，第一阶段读前面的整数，第二阶段读后面的整数并且相加。
        PARSE_1,
        PARSE_2
    }

    private int first;
    private int second;

    public IntegerAddDecoder(){
        super(Status.PARSE_1);
    }

    /**
     * 这里提到了一个“读断点指针”的概念：他保存着装饰器内部ReplayingDecoderBuffer成员的起始读指针，有点类似mark标记。用来记录抛出ReplayError的位置
     *
     */

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        switch (state()){
            case PARSE_1:
                //从装饰器ByteBuf中读取数据
                first=in.readInt();
                //第一步解析成功
                //进入第二部，并设置“读断点指针”为当前的读取位置
                checkpoint(Status.PARSE_2);
                break;
            case PARSE_2:
                second=in.readInt();
                Integer sum=first+second;
                out.add(sum);
                checkpoint(Status.PARSE_1);
                break;
            default:
                break;
        }


    }
}
