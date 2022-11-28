package com.code.chapter04Netty.duplicateDemo;


/**
 * ByteBuf的浅层复制分两种，slice切片 ；duplicate浅层复制
 */

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import org.junit.Test;


public class NettyByteBufSliceDemo {
    /**
     * ByteBuf 的slice方法可以获取到一个ByteBuf的一个切片。一个ByteBuf可以进行多次的切片浅层复制；多次切片后的ByteBuf对象可以共享一个存储区域
     * slice有两个重载版本：
     * ByteBuf slice():实际上就是调用了buf.slice(buf.readerIndex(), buf.readableBytes())
     * ByteBuf slice(int index,int length)
     */
    @Test
    public void testSlice(){
        ByteBuf buf= ByteBufAllocator.DEFAULT.buffer(9,100);
        buf.writeBytes(new byte[]{1,2,3,4});
        System.out.println("写入"+buf);
        ByteBuf slice=buf.slice();
        System.out.println("复制"+slice);
        /**
         * 调用slice之后，返回的byteBuf是一个新的对象：
         * readerIndex 为 0
         * writerIndex 为 源 readableBytes() 可读字节数
         * maxCapacity 为 源 readableBytes() 可读字节数
         * 切片不可写入
         * 切片不会复制ByteBuf的底层数据，底层byte[] 的指针是同一个堆内存指针
         * 切片不会改变源ByteBuf的引用计数refCnt
         */
    }

    /**
     * duplicate 整体浅层复制：
     * 和slice不同，duplicate()返回的是源ByteBuf的整个对象的一个浅层复制，包括：
     * duplicate 的读写指针、最大容量值，与源ByteBuf的读写指针相同
     * duplicate() 不会改变源ByteBuf的引用计数
     * duplicate() 不会复制源ByteBuf的底层数据
     *
     */



}
