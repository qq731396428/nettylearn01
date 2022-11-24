package com.code.chapter04Netty.pipelineDemo;


import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Test;

/**
 * 演示出站，出站处理器的添加顺序A->B->C
 * 结论：出站处理器被调用顺序：C->B->A
 */
public class OutPipeline {

    static class SimpleOutHandlerA extends ChannelOutboundHandlerAdapter {
        @Override
        public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
            System.out.println("出站处理器A：被回调");
            super.write(ctx, msg, promise);
        }
    }
    static class SimpleOutHandlerB extends ChannelOutboundHandlerAdapter {
        @Override
        public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
            System.out.println("出站处理器B：被回调");
            super.write(ctx, msg, promise);
        }
    }

    static class SimpleOutHandlerC extends ChannelOutboundHandlerAdapter {
        @Override
        public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
            System.out.println("出站处理器C：被回调");
            super.write(ctx, msg, promise);
        }
    }

    @Test
    public void testPipelineOutBound(){
        ChannelInitializer i=new ChannelInitializer() {
            @Override
            protected void initChannel(Channel ch) throws Exception {
                ch.pipeline().addLast(new SimpleOutHandlerA());
                ch.pipeline().addLast(new SimpleOutHandlerB());
                ch.pipeline().addLast(new SimpleOutHandlerC());
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
