package com.code.chapter03FutureAysn;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;


/**
 * Netty官方文档：netty的网络操作都是异步的
 * netty和Guava一样，实现了自己的异步回调体系：netty继承和扩展JDK Future系列异步回调的API，定义了自身的Future系列接口和类：
 * （1）继承JAVA Future接口得到一个属于netty自己的Future异步任务接口；该接口对原有的接口进行增强，时Netty可以以非阻塞的方式处理回调接口，ps：Netty没有修改Future的名称，只调整了所在的包名。。。。
 * （2）引入了一个新接口---GenericFutureListener，用于表示异步执行完成的监听器。这个接口和Guava的FutureCallback回调接口不同。netty使用了监听器模式，异步任务执行完成后的回调逻辑抽象成了Listener监听器接口。可以将Netty的GenericFutureListener监听器接口加入Netty异步任务Future中。
 *
 */

/**
 * 在异步非阻塞回调的设计思路上，Netty和 Guava的思路是一致的
 * （1）Netty的Future对应Guava的ListenableFuture
 * （2）Netty的 GenericFutureListener 接口对应到Guava的FutureCallback接口
 */

/**
 * GenericFutureListener拥有一个回调方法：operationComplete，表示异步任务完成操作
 */



/**
 * netty的Future：代码在 io.netty.util.concurrent 包中
 * Netty 的Future有一系列子接口，如：channelFuture等
 *
 */

public class NettyAysn {

    public void channelFutureDemo(){
        //connect是异步的，仅提交异步处理
        ChannelFuture future =null; // =bootstrap.connect("xxx.xxx.xxx.xxx:8899") ;
        //connect的异步任务真正执行完成后，future回调监听器才会执行
        future.addListener(new ChannelFutureListener() {
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                if(channelFuture.isSuccess()){
                    System.out.println("连接成功");
                }
            }
        });
    }

    /**
     * netty 的出站 和 入站 异步回调
     * 以write为例
     * 在调用write操作后，Netty没有完成对java nio的底层连接的写入
     */
    public void write(){
        //write输出方法，返回的是一个异步任务
        ChannelFuture future=null;//ctx.channel().write(msg);
        //为异步任务，加上监听器
        future.addListener(new ChannelFutureListener() {
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                //write完成后的回调代码
            }
        });
    }


}
