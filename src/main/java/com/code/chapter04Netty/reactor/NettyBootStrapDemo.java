package com.code.chapter04Netty.reactor;


import com.code.chapter04Netty.discardDemo.NettyDiscardHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

public class NettyBootStrapDemo {
/**
 * Bootstrap类是Netty提供的一个便理的工厂类，可以通过他来完成Netty的客户端 或 服务器短的Netty组件的组装，以及Netty程序的初始化。
 * 我们有两个启动类：（1）client专用的Bootstrap；（2）server专用的ServerBootStrap。都继承了AbstractBootstrap ，两个启动器仅仅使用的地方不同，配置和使用方法都是相似的
 *
 */

/**
 * 父子通道
 *   在Netty中，每一个NioSocketChannel通道所封装的是Java Nio通道，再往下就对应了操作系统的Socket描述符。底层Socket描述符分两类：
 *   （1）连接监听类型。连接监听类型的socket描述符，放在服务器端，他负责接收客户端的套接字连接；在服务端，一个“连接监听类型”的socket描述符可以接受（Accept）成千上万的传输类的Socket描述符
 *   （2）传输数据类型。数据传输类的Socket描述符负责传输数据。同一条TCP的Socket传输链路，在服务器和客户端，都会分别有一个与之相对的数据传输类型的socket描述符。
 *   例： netty中：异步非阻塞的服务器端监听通道NioServerSocketChannel，封装在Linux底层的描述符，是“连接监听类型”socket描述符 ； 而NioSocketChannel异步非阻塞Tcp Socket传输通道，封装在底层Linux的描述符是“数据传输类型”的socket描述符
 *   综上：Netty中有“接收关系”的NioServerSocketChannel和 NioSocketChannel 叫做“父子通道”。
 *   其中，NioServerSocketChannel负责服务器的“监听和接收”，叫做“父通道Parent Channel” ；对应于每一个接收到NioSocketChannel“传输类通道”，也叫子通道Child Channel
 *
 */

/**
 * EventLoopGroup 线程组
 *  Netty中，一个EventLoop相当于一个“子反应器SubReactor”。一个NioEventLoop拥有一个线程同时拥有一个Java Nio选择器。Netty使用EventLoopGroup线程组组织外层反应器。多个EventLoop组成一个EventLoopGroup线程组
 *  ps：EventLoopGroup线程组是多线程版本的反应器 ；单个EventLoop线程对应一个子反应器
 *  Netty的程序开发不会直接使用单个EventLoop线程，而是使用EventLoopGroup线程组。EventLoopGroup的构造函数有一个参数，用于指定内部线程数；如果使用EventLoopGroup的无参构造函数，EventLoopGroup的默认线程数是CPU处理器数量的2被（4核cpu开8个EventLoop）
 *      为了接收（Accept）到新连接，在服务器端，一般有两个独立的反应器，一个反应器负责新连接的监听和接受，另一个反应器负责IO事件处理。
 *      对应到Netty服务器程序中，则色设置两个EventLoopGroup，一个group负责监听和接受（包工头boss），一个group负责处理IO事件（工人worker）
 *
 */

/**
 * ChannelOption通道选项
 * NioServerSocket父通道、NioSocketChannel子通道类型，都可以设置一系列ChannelOption选项。
 * SO_RCVBUF,SO_SNDBUF：为TCP参数，每一个TCP_Socket套接字在内核中都有一个“发送缓冲区”和“接收缓冲区”，这两个选项就是用来设置TCP连接的这两个缓冲区大小的。TCP的全双工工作模式以及TCP的滑动窗口便是依赖这两个独立缓冲区及其填充的状态
 * TCP_NODELAY：此为TCP参数。表示立即发送数据，默认为True（netty默认true--即发送小报文，操作系统为false）。该值用于设置Nagle算法的启用，该算法将小的碎片数据连接成更大的报文（或数据包）来最小化所发送报文的数量，如果需要发送一些较小的报文，则需要金庸该算发。
 * SO_KEEPALIVE：此为tcp参数。表示底层TCP协议的心跳机制。true为连接保持心跳，“默认为false”。启用该功能时，TCP会主动探测空闲连接的有效性，默认的心跳间隔7200s-2小时
 * SO_REUSEADDR：tcp参数。设置为true时表示“地址复用”，默认为false
 * SO_LINGER：tcp参数，表示关闭socket的延时事件，默认-1，表示禁用该功能（即socket.close()后立即返回）
 * SO_BACKLOG：tcp参数。表示服务器端接受连接的队列长度，如果队列已满，客户端的连接会被拒绝，默认linux128，windows200。
 * SO_BROADCAST：tcp参数，设置广播模式
 *
 *
 */


    /**
     * Bootstrap的启动流程
     */
    public static void main(String[] args) throws InterruptedException {
        //创建一个服务器端的启动器
        ServerBootstrap b=new ServerBootstrap();
        //BootStarp启动有8个步骤：
        /**
         * 1：创建反应器线程组，把boss和worker线程组赋值给ServerBootStarp启动器实例
         */
        //boss线程组
        EventLoopGroup bossLoopGroup=new NioEventLoopGroup(1);
        //worker线程组
        EventLoopGroup workerLoopGroup=new NioEventLoopGroup();
        //把boss和worker线程组赋值给ServerBootStarp启动器实例, ps:也可以独立配置,ex:b.group(bossLoopGroup);
        b.group(bossLoopGroup,workerLoopGroup);

        /**
         * 2.设置通道io类型
         */
        //设置nio类型的通道
        b.channel(NioServerSocketChannel.class);

        /**
         * 3.设置监听端口
         */
        b.localAddress(new InetSocketAddress(8899));

        /**
         * 4.设置通道的参数
         */
        //.option() 对于服务器的Bootstrap而言，这个方法的作用是：给父通道（parent Channel）接受连接通道设置一些选项
        //设置子通道，需要使用 .childOption() 方法
        b.option(ChannelOption.SO_KEEPALIVE,true);
        b.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);

        /**
         * 5.装配子通道的pipeline流水线
         */
        //为什么不装配父通道的流水线？父通道：NioServerSocketChannel连接接受通道，他的内部业务是固定的，可以使用 b.handler(ChannelHandler handler)方法，为父通道设置ChannelInitializer初始化器。
        b.childHandler(new ChannelInitializer<SocketChannel>() {
            //有连接到达时会创建一个通道的子通道，并初始化
            @Override
            protected void initChannel(SocketChannel ch) throws Exception{
                //流水线管理子通道中的Handler业务处理器
                //向子通道流水线添加一个Handler业务处理器
                ch.pipeline().addLast(new NettyDiscardHandler());
            }
        });

        /**
         * 6：开始绑定服务器新连接的监听端口
         */
        //通过调用 sync 同步方法阻塞知道绑定成功
        ChannelFuture channelFuture=b.bind().sync();
        System.out.println("服务器启动成功，监听端口："+channelFuture.channel().localAddress());
        //至此服务器正式启动：
        //netty中所有IO操作都是异步执行的，这就意味着任何一个IO操作会立刻返回。Netty中的IO操作，都会返回异步任务实例（ChannelFuture实例），通过自我阻塞以知道ChannelFuture异步任务执行完成（或为ChannelFuture增加事件监听器）以获得Netty中的IO操作的结果

        /**
         * 7.自我阻塞，直到通道关闭
         */
        //自我阻塞，直到通道关闭的异步任务结束
        ChannelFuture closeFuture=channelFuture.channel().closeFuture();
        closeFuture.sync();
        //如果要阻塞当前线程直到通道关闭，可以使用通道的closeFuture() 方法，以获取通道关闭的异步任务。当通道被关闭时，closeFuture实例的sync()方法会返回

        /**
         * 8.关闭EventLoopGroup
         */
        //释放掉所有资源，包括创建的反应器线程
        workerLoopGroup.shutdownGracefully();
        bossLoopGroup.shutdownGracefully();
        //关闭Reactor反应器线程组，同时会关闭内部的subReactor子反应器线程，也会关闭内部的Selector选择器、内部轮询线程以及负责查询的所有子通道

    }





}
