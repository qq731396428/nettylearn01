package com.code.chapter04Netty.pipelineDemo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Test;

/**
 * 完整演示pipeline入站处理流程，将新建三个极简地入站处理器，有桑格处理器SimpleInHandlerA SimpleInHandlerB SimpleInHandlerC 顺序为 A->B->C
 * 结论：入站处理器调用顺序A->B->C
 *
 * 但是如果B处理器没有调用，super.channelRead(), 流水线就会被截断：执行顺序：A->B
 *
 * 但但但是：出站处理器只要开始执行就不可以被截断。强行截断的话，Netty会抛出异常
 *
 */
public class InPipeline {

    static class SimpleInHandlerA extends ChannelInboundHandlerAdapter{
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            System.out.println("入站处理器A：被回调");
            super.channelRead(ctx, msg);
        }
    }

    /**
     * 截断流水线的两个方法：
     * （1）不使用基类的channelXxx方法
     * （2）不调用ctx.fireChannelXxx()
     */
    static class SimpleInHandlerB extends ChannelInboundHandlerAdapter{
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            System.out.println("入站处理器B：被回调");
            super.channelRead(ctx, msg); //不使用基类的channelRead方法：那么流水线就会被截断，可以来模拟截断case
            ctx.fireChannelRead(msg);
        }
    }

    static class SimpleInHandlerC extends ChannelInboundHandlerAdapter{
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            System.out.println("入站处理器C：被回调");
            super.channelRead(ctx, msg);
        }
    }

    /**
     * 顺序入站，顺序调用handler
     */
    @Test
    public void testPipelineInbound(){
        ChannelInitializer i=new ChannelInitializer() {
            @Override
            protected void initChannel(Channel ch) throws Exception {
                ch.pipeline().addLast(new SimpleInHandlerA());
                ch.pipeline().addLast(new SimpleInHandlerB());
                ch.pipeline().addLast(new SimpleInHandlerC());
            }
        };
        EmbeddedChannel channel=new EmbeddedChannel(i);
        ByteBuf buf= Unpooled.buffer();
        buf.writeInt(111);
        //向通道写入一个入站报文（数据包）
        try{
            Thread.sleep(Integer.MAX_VALUE);
        }catch (Exception e){

        }


    }


}
