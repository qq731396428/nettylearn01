package com.code.chapter04Netty.ByteBufDemo;

/**
 * Netty提供了ByteBuf 来代替java Nio的ByteBufer 缓冲区，以操作内存缓冲区
 */
public class NettyByteBufDemo {
    /**
     * ByteBuf 的优势：
     * （1）pooling（池化，减少了内存复制和Gc）
     * （2）复合缓冲区类型，支持零复制
     * （3）不需要调用Flip() 方法去切换读写模式
     * （4）扩展性好，例如StringBuffer
     * （5）可以自定义缓冲区类型
     * （6）读取和写入的索引分开
     * （7）方法的链式调用
     * （8）可以进行引用计数，方便重复使用
     */

    /**
     * ByteBuf是一个字节容器，内部是一个字节数组。逻辑上有四部分：
     * （1）第一部分是已用字节，表示已使用完的废弃的无效字节；
     * （2）第二部分是可读字节，这部分数据是ByteBuf保存的有效数据，用ByteBuf中读取的数据都来自这一部分
     * （3）第三部分是可写字节，写入到ByteBuf的数据会存到这一部分中；
     * （4）第四部分是可扩容字节，表示该ByteBuf最多还能扩容的大小
     *
     */





}
