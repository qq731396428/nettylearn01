package com.code.chapter04Netty.reactor;

public class NettyHandlerDemo {

    /**
     * 在Reactor 反应器模型中，反应器查询到IO事件后，分发到Handler业务处理器，由Handler完成IO操作和业务处理。
     * 整个的IO处理操作环节包括：（1）从通道中读数据包、（2）数据包解码、（3）业务处理、（4）目标数据编码、（5）把数据包写道通道、（5.1）由通道发送到对端
     * 用户程序主要在 Handler 业务处理器中，Handler涉及的环节为：（1）数据包解码、（2）业务处理、（3）目标数据编码、（4）把数据包写到通道中。
     * 从应用程序开发人员的角度来说，有“入站”和“出站”两种操作类型。
     *  （1）入站处理，触发的方向为：自底向上，netty的内部（如通道）到ChannelInboundHandler入站处理器
     *  （2）出站处理：触发的方向为：自定向下，从ChannelOunboundHandler出站处理器到Netty的内部（如通道）
     */

    /**
     * ChannelInboundHandler：通道入站处理器：核心方法有：
     * （1）channelRegistered：当通道注册完成后，netty会调用fireChannelRegistered
     * （2）channelActive：当通道激活完成后，Netty会调用fireChannelActive，出发通道激活时间。通道会启动该入站操作的流水先处理，在通道注册过的入站处理器Handler的channelActive方法，会被调用到
     * （3）channelRead：当通道缓冲区可读，Netty会调用fireChannelRead，触发通道可读事件。通道会启动该入站操作的流水线处理，在通道注册过的入站处理器Handler的channelRead方法，会被调用到
     * （4）channelReadComplete：当通道缓冲区读完，Netty会调用fireChannelReadComplete，触发通道读完事件。通道会启动该入站操作的流水线处理，在通道注册过的入站处理器Handler的channelReadComplete方法，会被调用到
     * （5）channelInactive：当连接被断开或者不可用，Netty会调用fireChannelInactive，触发连接不可用事件。通道会启动对应的流水线处理，在通道注册过的入站处理器Handler的channelInactive方法，会被调用到
     * （6）exceptionCaught：当通道处理过程发生异常时，Netty会调用fireExceptionCaught，触发异常捕获事件。
     * 开发时，继承ChannelInboundHandlerAdapter，重写需要的方法就行
     *
     */

    /**
     * ChannelOutboundHandler 通道出站处理器：出站的处理方向：通过上层Netty通道，去操作底层java io通道。outBound的操作有：
     * （1）bind：监听地址（ip+端口）绑定：完成底层Java Io通道的IP地址绑定。如果使用tcp传输协议，这个方法用于服务器端。
     * （2）connect：连接服务器，完成底层Java IO通道的服务器端的连接操作。如果使用tcp传输协议，这个方法用于客户端
     * （2.1）disConnect：断开服务器连接：断开底层Java io通道的服务器端连接。如果使用tcp传输协议，这个方法用于客户端
     * （3）write：写数据到底层：完成Netty通道向底层Java io 通道的数据写入操作。该方法仅仅触发操作，不执行实际写入
     * （4）flush：腾空缓冲区，把数据写道对端
     * （5）read：从底层读数据，完成netty通道从Java io通道的数据读取
     * （6）close：关闭通道，例：关闭服务器端的新连接监听通道
     *  实际开发中，继承ChannelOutBoundHandlerAdapter
     */

    /**
     * ChannelInitializer 通道初始化处理器：向流水线中装配业务处理器
     * initChannel()方法是ChannelInitializer定义的一个抽象方法，这个方法需要开发人员自己实现。父通道调用initChannel() 方法时，会将新接收的通道作为参数，传递给initChannel()方法。
     * initChannel()方法内部大致的业务代码是：拿到新连接通道作为实际参数，往他的流水线中专配Handler业务处理器
     *
     */




}
