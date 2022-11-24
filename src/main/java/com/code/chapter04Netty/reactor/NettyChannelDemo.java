package com.code.chapter04Netty.reactor;

public class NettyChannelDemo {
    /**
     * 通道是通信的主体，可读可写 通道的抽象类：AbstractChannel:io.netty.channel.AbstractChannel#AbstractChannel(io.netty.channel.Channel)
     * AbstractChannel内部有一个pipeline属性，代表处理器流水线-->channel构造函数表示：pipeline属性初始化为DefaultChannelPipeline。每一个通道都有一条ChannelPipeline处理器流水线
     * AbstractChannel 内部有一个parent（父通道）。对于连接监听通道（例NioServerSocketChannel）父通道为null；对于传输通道（例NioSocketChannel）父通道为“接收到该连接的服务器连接监听通道”
     *
     */

    /**
     * 通道接口种定义了几个重要方法：
     * （1）ChannelFuture connect(SocketAddress address)：连接远程服务器。返回值是负责链接操作的异步任务ChannelFuture。此方法在客户端的传输通道使用
     * （2）ChannelFuture bind(SocketAddress address)：绑定监听地址，开始监听新的客户端连接。此方法在服务器的新连接和接受通道使用。
     * （3）ChannelFuture close()：关闭通道连接。如果需要再连接正式关闭后执行其他操作，则需要为异步任务设置回调方法；或者调用ChannelFuture异步任务sync()方法来阻塞当前线程，一直等到关闭通道的异步任务执行完毕
     * （4）Channel read()：读取通道数据，并且启动入站处理。具体来说，从内部的Java Nio Channel通道读取数据，然后启动内部的Pipeline流水线，开启数据读取的入站处理。此方法的返回通道自身用于链式调用。
     * （5）ChannelFuture write(Object o)：此方法的作用为：启动出站流水线处理，把处理后的最终数据写道底层JavaNio通道。此方法的返回值：出站处理的异步处理任务
     * （6）Channel flush()：将缓冲区中的数据立即写道对端。并不是每一次write操作都直接将数据写到对端，write操作的作用在大部分情况下仅仅是写入到操作系统的缓冲区。而flush是立即将缓冲区的数据写到对端
     *
     */

/**
 * EmbeddedChannel嵌入式通道
 * 通信的基础工作，netty已经完成。netty提供了一个专用通道，EmbeddedChannel 嵌入式通道
 * EmbeddedChannel 仅仅是模拟入站和出站的操作，底层不进行实际的传输，不需要启动netty服务器和客户端。使用它可以快速进行ChannelHandler业务处理器的单元测试：
 * （1）writeInbound：入站数据写到通道：测试入站处理器。调用这个方法，向EmbeddedChannel 写入一个二进制ByteBuf数据包，模拟底层入站包：向通道写入outbound出站数据，模拟通道发送数据，这些写入的数据会被流水线上的出站处理器处理
 * （2）readOutBound：读取出站数据：从EmbeddedChannel 中读取数据，返回经过流水线最后一个出站处理器处理之后的出站数据。如果没有数据，返回null
 *
 *
 */


}
