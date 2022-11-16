package com.code.chapter04Netty.reactor;




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
 *
 *
 */





}
