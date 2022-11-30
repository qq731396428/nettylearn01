package com.code.chapter05DecoderAndEncoder.nettyDecoderLearn;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * 我们自定义一个byte 转 Integer地解码器
 * 功能：ByteBuf缓冲区的字节，解码成Integer整数类型：
 * （1）定义一个新的整数解码器，继承ByteToMessageDecoder 字节码解码器
 * （2）实现父类的decode方法，将缓冲书数据解码成一个一个的Integer对象。
 * （3）在decode方法中，将解码后得到的Integer整数，加入到父类传递过来的List 实参中
 */
public class NettyDecoderIntegerDemo extends ByteToMessageDecoder {

    /**
     * 解码器：
     * （1）他是一个inBound 入站处理器，解码器是负责处理“入站数据” 的
     * （2）他能将上一站InBound入站处理器传过来的输入（input），进行数据的解码 或 格式转换，然后输出（output）到下一站 Inbound入站处理器
     *
     * 一个标准的解码器，将输入类型为ByteBuf缓冲区的数据进行解码，输入一个一个的java POJO 对象。Netty内置了这个解码器，叫做ByteToMessageDecoder
     * 所有Netty中的解码器，都是InBound入站处理器类型，都是继承了ChannelInboundHandler接口
     */

    /**
     * ByteToMessageDecoder 是一个抽象类，继承了ChannelInboundHandlerAdapter适配器
     * 解码流程：（1）首先，他将上一站传过来的输入（input）到Bytebuf的数据进行解吗，解码出一个List<Object>对象列表；（2）迭代List<Object>列表，逐个将Java Pojo对象传入下一站Inbound入站处理器
     * 但是decode方法只是一个抽象方法，没有具体实现，所以解码方法需要我们自己来实现：
     * 但是decode方法只是一个抽象方法，没有具体实现，所以解码方法需要我们自己来实现：
     * （1）首先继承ByteToMessageDecoder抽象类
     * （2）然后实现其基类decode抽象方法。将ByteBuf到Pojo解码的逻辑写入此方法。将ByteBuf二进制数据，解码成一个一个的java POJO 对象
     * （3）在子类的decode方法中，需要将解码后的Java POJO 对象，放入decode的List<Object> 实参中。这个实参是ByteToMessageDecoder父类传入的，也就是父类的结果收集列表
     * 在流水线的过程中，ByteToMessageDecoder调用子类decode方法解码完成后，会将List<Object> 中的结果一个一个地分开传递到下一站地Inbound入站处理器
     */


    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        while (in.readableBytes()>=4){
            int i=in.readInt();
            System.out.println("解码出一个整数："+i);
            out.add(i);
        }
    }
    /**
     * 如何使用这个解码器呢？在IntegerProcessHandler 里面
     */

}
