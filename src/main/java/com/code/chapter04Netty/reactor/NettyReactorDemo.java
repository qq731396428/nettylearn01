package com.code.chapter04Netty.reactor;

public class NettyReactorDemo {

    /**
     * 一个普通的Reactor模式
     *（1）通道注册。IO源于通道，IO和通道是强相关的。一个IO事件一定属于某个通道。如果查询通道的事件，需要讲通道注册到选择器上。selector->channel->io事件
     * （2）查询选择：在反应器模式中，一个反应器（或SubReacotr子反应器）或负责一个线程；不管的轮询，查询选择器中的IO事件（选择键）
     * （3）事件分发：如果查询到IO事件，分发给 “和IO事件有绑定关系的” Handler业务处理器
     *（4）完成真正的IO操作和业务处理，由Handler业务处理器负责
     */

    /**
     * Netty中的Channel通道：
     * Netty对Nio的channel进行了封装，对于每一种通信连接协议，Netty都实现了自己的通道
     * ps：netty还能处理Java的面向流的OIO事件（阻塞IO）
     * （1）NioSocketChannel：异步非阻塞TCP socket传输通道
     * （2）NioServerSocketChannel：异步非阻塞Tcp Socket服务器端监听通道
     * （3）NioDatagramChannel：异步非阻塞UDP传输通道
     * （4）NioSctpChannel：异步非阻塞Sctp传输通道
     * （4）NioSctpServerChannel：异步非阻塞Sctp 服务器端 传输通道
     * ---------------------------------------------------------------
     * （5）OioSocketChannel：同步阻塞式TCP socket传输通道
     * （6）OioServerSocketChannel：同步阻塞式Tcp socket服务器端监听通道
     * （7）OioDatagramChannel：同步阻塞式UDP传输通道
     * （8）OioSctpChannel：同步阻塞 sctp 通道
     * （9）OioSctpServerChannel：同步阻塞 sctp服务器端 监听通道
     */
    /**
     * Netty 的 NioSocketChannel内部封装了一个Java Nio的 SelectableChannel 成员（在AbstractNioByteChannel中）。通过这个内部的Java Nio通道，
     * Netty的NioSocketChannel通道上的IO操作，最终会落到JavaNio的selectable底层通道
     */

    /**
     * Netty中的反应器有很多实现，与Channel通道类有关系。对于 NioSocketChannel 通道，Netty的反应器类为：NioEventLoop。
     * NioEventLoop 拥有一个线程：负责Java Nio selector选择器的IO事件轮询
     * 在Netty中，EventLoop反应器和NettyChannel通道是一对多的关系：一个反应器可以注册成千上万的通道
     */

    /**
     * Java Nio的IO 事件类型： SelectionKey.OP_READ OP_WRITE OP_CONNECT OP_ACCEPT
     * Netty 有两大处理器：ChannelInboundHandler 通道入站处理器；ChannelOutboundHandler 通道出站处理器。两者都继承了ChannelHandler处理器接口
     * （1）netty中的入站处理，不仅仅是OP_READ输入事件的处理，而是从通道底层触发，由netty通过层层传递，调用ChannelInboundHandler通道入站处理器进行的某个处理：
     *    以OP_READ输入事件为例：通道中发生了OP_READ事件，被EventLoop查询到，分发给ChannelInboundHandler通道入站处理器，调用他的入站处理的方法read。在ChannelInboundHandler通道入站处理器内部的read方法可以从通道中读取数据
     *    Netty中的入站处理，触发的方向为：从通道到 ChannelInboundHandler 通道入站处理器.
     * （2）Netty中的出站处理，本来就包括Nio的OP_WRITE可写事件：
     *    从ChannelOutboundHandler通道出站处理器到通道的某次IO操作，例如：在程序完成业务处理后，可以通过ChannelOutboundHandler通道出站处理器处理的结果写入底层通道。常用write()方法
     * 这两个业务处理接口的默认实现：ChannelInboundHandlerAdapter，ChannelOutboundHandlerAdapter。
     */

    /**
     * Netty的流水线 pipeline
     * 首先，netty的反应器模式中各个组件之间的关系：
     * （1）反应器（或SubReactor子反应器）和通道之间是一对多的关系：一个反应器可以查询很多个通道的IO事件。
     * （2）通道和Handler处理器实例之间，是多对多的关系：一个通道的IO事件被多个的Handler实例处理；一个Handler处理器实例也能绑定到很多的通道，处理多个通道的IO事件
     *   然后：通道和Handler处理器实例之间的绑定关系，netty是如何组织的呢？
     *   答：netty设计了一个特殊的组件，叫作ChannelPipeline（通道流水线），他像一条管道，将绑定到一个通道的多个Handler处理器实例，串在一起，形成一条流水线。
     *      channelPipeline（通道流水线）的默认实现，实际上被设计成一个双向链表。所有的Handler处理器实例被包装成了双向链表的节点，加入到了ChannelPipeline中
     *   ps:一个Netty通道拥有一条Handler处理器流水线，成员的名称叫做Pipeline
     *   问：pipeLine不是“管道”而是“流水线”的原因
     *   答：与流水线内部的Handler处理器之间处理IO事件的先后次序有关
     *   例：以入站处理为例：每一个来自通道的IO事件，都会进入一次ChannelPipeline通道流水线。在进入第一个Handler后，这个IO事件将会按照既定规则“从前向后”流向下一个Handler处理器，向后流动的过程中会有3种情况：
     *   （1）如果后面 还有 其他Handler入站处理器，那么IO事件可以交给下一个Handler处理器，向后流动
     *   （2）如果后面 没有 其他的入站处理器，这就意味着这个IO事件在此次流水线中的处理结束了
     *   （3）如果流水线中间需要“终止流动”，可以选择不将IO事件交给下一个Handler处理器，流水线的执行也被终止。
     *   结论：
     *   （1）入站处理器Handler的执行次序，从前到后；出站处理，从后到前
     *   （2）入站操作只会从Inbound入站处理器的Handler流过；出站的IO操作只能从OutBound出站处理器类型的Handler流过。pipeline是管家，handler是小弟
     *
     */
}
