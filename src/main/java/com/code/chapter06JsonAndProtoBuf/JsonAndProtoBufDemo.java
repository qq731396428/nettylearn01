package com.code.chapter06JsonAndProtoBuf;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;

public class JsonAndProtoBufDemo {

    /**
     * rpc http 需要接用tcp、udp这种字节流协议传输数据
     * protoBuf是一个传输效率较高的二进制序列化框架，但是需要一个完整的二进制包，不能半包or粘包
     */
    /**
     * 粘包和半包：
     * netty发送和读数据走的是ByteBuf缓冲区，每一次发送就是向通道里写一个ByteBuf。发送数据时先填好ByteBuf，然后发送
     * 粘包：指接收端（receiver）收到一个ByteBuf，包含了多个发送端（Sender）的ByteBuf，多个ByteBuf“粘”在了一起
     * 半包：接收端将一个发送端的ByteBuf“拆”开了，收到多个破碎的包。换句话说，一个接收端收到的ByteBuf是发送端的一个ByteBuf的一部分
     *
     */

    /**
     * 解决：
     * LengthFieldPrepender 编码器，可以在数据包（json）前面加上内容的二进制字节数组的长度-->用LengthFieldBasedFrameDecoder解码器配对
     * LengthFieldPrepender构造函数中可以设置 字节流头里面 表示信息 长度的head的长度
     */

    /**
     * Protobuf编码过程：使用预先定义的Message数据结构将实际的传输数据进行打包，然后编码成二进制的码流进行传输或者存储。
     * Protobuf解码过程：则刚刚好与编码过程相反：将二进制流解码成Protobuf自己定义的Message结构的POJO实例
     * 微信采用Protobuf
     * 需要生命：头部声明：proto版本（例 proto3）；java选项配置：option java_package="包名" option java_outer_classname="MsgProtos"  ；消息定义：message MSG{ uint32 id=1;string content=2}
     */
    ServerBootstrap b=new ServerBootstrap();
    public void runServer(){
        //创建反应器线程组
        EventLoopGroup bossLoopGroup=new NioEventLoopGroup(1);
        EventLoopGroup workerLoopGroup=new NioEventLoopGroup();
        try{
            //..省略：启动器的反应器线程，设置配置项
            //5 装配子通道流水线
            b.childHandler(new ChannelInitializer<SocketChannel>() {
                //有连接到达时会创建一个通道
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    // 流水线管理子通道中的Handler业务处理器
                    //向子通道流水线添加3个Handler业务处理器
                    ch.pipeline().addLast(new ProtobufVarint32FrameDecoder());
                    //ch.pipeline().addLast(new ProtobufDecoder(MsgProtos.Msg.get));
                }
            });

        }catch (Exception e){

        }



    }


}
