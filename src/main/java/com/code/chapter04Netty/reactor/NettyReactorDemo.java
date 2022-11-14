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
}
