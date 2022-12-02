package com.code.chapter05DecoderAndEncoder.nettyEncoder;

public class NettyEncoderTotal {

    /**
     * 编码器，首先是一个outBound出站处理器，负责处理”出站数据“；其次，编码器将上一站OutBound出站处理器穿过来的输入（input）数据进行编码 或 格式转换，然后传递到下一站ChannelOutBoundHandler出站处理器
     * 编码器与解码器相呼应
     * 编码器是ChannelOutBoundHandler出站处理器的实现类。一个编码器将出战对象编码之后，编码后数据将被传递到下一个ChannelOutBoundHandler，进行后面的出站处理
     *
     */


}
