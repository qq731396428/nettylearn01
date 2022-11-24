package com.code.chapter04Netty.test;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Test;

public class InHandlerDemoTester {

    /**
     * 根据输出结果可以看到：
     * channelHandler中的回调方法的执行顺序：handlerAdded --> channelRegistered -->channelActive -->入站方法回调 -->channelInactive ->ChennelUnregistered ->handlerRemove
     * 其中，读取数据的入站回调为：channelRead() ->channelReadComplete
     * 入站方法会多次调用，每一次有ByteBuf 数据包入站都会调用到
     */
    /**
     * 还有6个方法和ChannelHandler的生命周期有关
     * handlerAdded：当业务处理器被加到流水线后，此方法被回调。也就是在完成ch.pipeline().addLast(handler)后，会回调handlerAdded方法
     * channelRegistered：当通道成功绑定一个NioEventLoop线程后，会通过流水线回调所有业务处理器的channelRegisered 方法
     * channelActive：当通道激活成功后，会通过流水线回调所有业务处理器的channelActive方法，激活通道成功指的是，所有业务处理器添加、注册的异步完成，并且NioEventLoop线程绑定的异步任务完成
     * channelInactive：当通道的底层链接已经不是Establish 状态、或底层连接已经关闭，会首先回调所有业务处理器的 channelInactive方法
     * channelUnregistered：通道和NioEventLoop线程解除绑定，一处对这条通道的事件处理之后，回调所有业务处理器的 channelUnregistered 方法
     * handlerRemoved：最后，Netty会移除通道上所有业务处理器，或回调所有的业务处理器的handlerRemove方法
     *
     */
    /**
     * Inhandler入站处理器，有两个很重要的回调方法：
     * （1）channelRead：有数据包入站，通道可读。流水线会启动入站流程，从前到后，入站处理器的channelRead 方法会被依次回调到。
     * （2）channelReadComplete：流水线完成入站处理后，会从前向后，依次回调每个入站处理器channelReadComplete方法，表示数据读取完毕。
     */
    @Test
    public void testInHandlerLifeCircle(){
        final InHandlerDemo inHandler=new InHandlerDemo();
        //初始化处理器
        ChannelInitializer i=new ChannelInitializer() {
            @Override
            protected void initChannel(Channel ch) throws Exception {
                ch.pipeline().addLast(inHandler);
            }
        };
        //创建嵌入式通道
        EmbeddedChannel channel=new EmbeddedChannel(i);
        ByteBuf buf= Unpooled.buffer();
        buf.writeInt(1);

        //模拟入栈，写入一个入站数据包
        channel.writeInbound(buf);
        channel.flush();

        //模拟入栈，再写入一个入站数据包
        channel.writeInbound(buf);
        channel.flush();

        //关闭通道
        channel.close();
        try{

            Thread.sleep(Integer.MAX_VALUE);
        }catch (Exception e){

        }

    }

}
