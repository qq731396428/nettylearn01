package com.code.chapter04Netty.pipelineDemo;


/**
 * 一条 Netty 通道需要很多Handler 业务处理器来处理业务。每条通道内部都有一条流水线（pipeline）将Handler装配起来。Netty的业务处理流水线ChannelPipeline是基于责任链模式（Chain of Responsibility）设计的，内部是一个双向链表，能够支持动态地店家和删除Handler
 *
 */
public class NettyPipelineDemo {

    /**
     * 在两个demo里面定义了Handler业务处理器，但是不管什么样的Handler业务处理器，他们最后都以双向链表的形式保存，但是流水线中的节点类型，不是Handler业务处理器，而是 ChannelHandlerContext--通道处理器上下文类。
     * 在Handler业务处理器被添加到流水线中时，会创建一个通道处理器上下文：ChannelHandlerContext，他代表了 ”ChannelHandler通道处理器“ 和 ”ChannelPipeline通道流水线“ 之间的关联
     * ChannelHandlerContext 包含了许多方法，主要可以分为两类：（1）获取上下文所关联的Netty组件实例（例：关联的通道、关联的流水线、上下文内部Handler业务处理器实例 等）；（2）入站和出站的处理方法
     *
     */

    /**
     * 在 Channel、ChannelPipeline、ChannelHandlerContext 三个类中，会有同样的 出站+入站 处理方法，那么 同一个方法，在不同的三个类中，功能有何不同？
     * 答：如果通过Channel和ChannelPipeline 的实例来调用这些方法，他们就会在整个流水线中传播。但是，如果只通过ChannelHandlerContext通道处理器上下文进行调用，就只会从当前节点开始执行Handler，并传播到同类型处理器的下一站
     * Channel，Handler、ChannelHandlerPipeline 三者的关系为：
     *      Channel通道拥有一条ChannelPipeline通道流水线，
     *      每一个流水线节点为一个ChannelHandlerContext通道处理器上下文，
     *      每一个上下文中包裹了一个ChannelHandler通道处理器。
     *      在ChannelHandler通道处理器的 入站\出站 处理方法中，Netty都会传递一个Context上下文实例作为实际参数。
     *      通过Context实例的实参，在业务处理中，可以获取ChannelPipeline通道流水线的实例或者Channel通道的实例
     *
     */

    /**
     * Netty的Handler可以”热插热扒“ ：方法的声明在 接口-ChannelPipeline中
     *
     */










}
