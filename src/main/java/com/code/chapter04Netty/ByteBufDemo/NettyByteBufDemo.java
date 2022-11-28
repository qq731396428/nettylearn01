package com.code.chapter04Netty.ByteBufDemo;

import io.netty.buffer.*;
import org.junit.Test;

import java.nio.ByteBuffer;

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
     * ----（1）----（2）----（3）----（4）
     *
     */

    /**
     * ByteBuf的重要属性：通过这三个整型的指针，使读写没有冲突，属性在AbstractByteBuf抽象类中定义：
     * （1）readerIndex 读指针：指示读取的起始位置。每读取一个字节，readerIndex自动+1.一旦readerIndex==writerIndex 表示ByteBuf不可读
     * （2）writerIndex 写指针：指示写入的起使位置。每写一个字节，writerIndex自动+1.一旦writerIndex==capacity() 则表示ByteBuf不可写。capacity()是成员方法，表示ByteBuf中可以写入的容量；ps：capacity() != maxCapacity
     * （3）maxCapactity 最大容量：表示ByteBuf可以扩容的最大容量。当向ByteBuf写数据的时候，如果容量不足，可以进行扩容。不能超过maxCapacity
     *
     */

    /**
     * ByteBuf的”三组“方法:（1）容量系列；（2）写入系列；（3）读取系列
     */
    /**
     * 容量系列：
     * capacity()：表示ByteBuf的容量，=废弃的字节数+可读字节数+可写字节数
     * maxCapacity()：表示ByteBuf最大能够容纳的最大字节数。当向ByteBuf中写数据的时候，如果发现容量不足，进行扩容，上线为 maxCapacity
     */
    /**
     * 写入系列：
     * isWritable()：表示ByteBuf是否可写。如果capacity()容量大于writerIndex指针的位置，则表示可写，否则为不可写。ps：如果Netty发现ByteBuf中数据不可写入，会自动扩容ByteBuf
     * writableBytes()：可写入的字节数：值=容量capacity-writerIndex
     * maxWritableBytes()：取得最大的可写入字节数，值=最大容量maxCapacity-writerIndex
     * writeBytes(byte[] src)：把src字节数组的数据全部写到ByteBuf。这是最为常用的一个方法。
     * writeTYPE(TYPE value)：写入基础数据类型的数据，改变指针writerIndex的值
     * setTYPE(TYPE value)：基础数据类型的设置，不改变writerIndex指针值，包含了8个基础数据类型
     * markWriterIndex() resetWriterIndex() ：前一个方法把写指针wirterIndex保存在markedWriterIndex中，后一个方法把之前保存在markedWriterIndex的值恢复到writerIndex中。定义在AbstractByteBuf中
     */
    /**
     * 读取系列：
     * isReadable()：返回ByteBuf是否可读：writerIndex>readerIndex表示可读
     * readableBytes()：返回ByteBuf当前可读的字节数，值=writerIndex-readerIndex
     * readBytes(byte[] dst)：读取ByteBuf中的数据。将数据从ByteBuf读取到dst字节数组中，dst[]的大小通常等于readableBytes()
     * readType():读取基础数据类型
     * getTYPE(TYPE value)：读取基础数据类型，不改变读指针的值
     * markReaderIndex() resetReaderIndex() ：和写方法的差不多
     */
    @Test
    public void testWriteRead(){
        ByteBuf buf= ByteBufAllocator.DEFAULT.buffer(9,100);
        System.out.println("分配ByteBuf（9，100）"+buf);
        buf.writeBytes(new byte[]{1,2,3,4,5,6});
        getByteBuf(buf);

        readByte(buf);

    }
    //取字节 指针自动+1
    private void readByte(ByteBuf buf){
        while(buf.isReadable()){
            System.out.println("取一个字节:"+buf.readByte());
        }
    }
    //读字节，指针不动
    private void getByteBuf(ByteBuf buf){
        for (int i=0;i<buf.readableBytes();i++){
            System.out.println("读一个字节"+buf.getByte(i));
        }
    }

    /**
     * ByteBuf的引用计数
     * Netty的ByteBuf的内存回收工作是通过引用技术的方式管理的
     * 池化pooling：创建一个Buffer池，将没有用的Buffer对象，放入对象缓存池中；不需要重复创建
     * 当创建完一个ByteBuf时，他的引用为1；每一次调用retain()方法，他的引用此时就+1；每次调用release()方法，它的引用计数-1；如果引用为0，访问这个ByteBuf对象会抛错
     * 为了确保计数器不混乱：调用了一次retain，那就需要掉一次release
     */
    @Test
    public void testRef(){
        ByteBuf buf=ByteBufAllocator.DEFAULT.buffer();
        System.out.println("创建完的refCnt引用次数"+buf.refCnt());
        buf.retain();
        System.out.println("调用retain()方法,引用次数"+buf.refCnt());
        buf.release();
        System.out.println("调用release()方法,引用次数"+buf.refCnt());
        buf.release();
        System.out.println("调用release()方法,引用次数"+buf.refCnt());
        //开始报错了！！！！掉不了这个方法
        buf.retain();
        System.out.println("调用retain()方法,引用次数"+buf.refCnt());

    }

    /**
     * ByteBuf的Allocator分配器
     * ByteBufAllocator，有两个实现，Pool和 UnPooled，池化和非池化
     * 池化：PoolByteBufAllocator：将ByteBuf实例放入池中，提高了性能，将碎片减少到最小；池化分配器采用了jemalloc高效内存分配策略。-->4.1版本默认
     * 非池化：UnPooledByteBufAllocator：普通的未池化ByteBuf分配器，他没有把ByteBuf放入池中，每次被调用，会返回一个新的ByteBuf实例；通过java的垃圾回收机制回收
     */
    @Test
    public void showAlloc(){
        ByteBuf buf=null;
        //分配器分配初始容量为9，最大容量100的缓冲区
        buf=ByteBufAllocator.DEFAULT.buffer(9 , 100);
        //分配器默认分配初始容量256，最大容量Integer.max_value缓冲区
        buf=ByteBufAllocator.DEFAULT.buffer();
        //非池化分配器，分配基于java堆（heap）结构内存缓冲区
        buf= UnpooledByteBufAllocator.DEFAULT.heapBuffer();
        //池化分配器，分配基于操作系统管理的直接内存缓冲区
        buf = PooledByteBufAllocator.DEFAULT.directBuffer();
    }
    /**
     * ByteBuf 缓冲区的类型：
     * Heap ByteBuf：内部数据为一个java数组，在jvm的堆中 ; Direct ByteBuf：内部数据存储在操作系统的物理内存 ; CompositeBuffer：多个缓冲区组合
     */
    //堆缓冲区
    @Test
    public void testHeapBuffer(){
        //取得堆内存
        ByteBuf heapBuf=ByteBufAllocator.DEFAULT.buffer();
        heapBuf.writeBytes("啦啦啦啦".getBytes());
        if(heapBuf.hasArray()){
            //取得堆内存
            byte[] array=heapBuf.array();
            int offset=heapBuf.arrayOffset()+heapBuf.readerIndex();
            int length=heapBuf.readableBytes();
            System.out.println(array);
            System.out.println(offset);
            System.out.println(length);
        }
        heapBuf.release();
    }

    //直接缓冲区
    @Test
    public void testDirectBuffer(){
        ByteBuf directBuffer=ByteBufAllocator.DEFAULT.directBuffer();
        directBuffer.writeBytes("啦啦啦啦".getBytes());
        if(!directBuffer.hasArray()){
            //取得堆内存
            byte[] array=directBuffer.array();
            int offset=directBuffer.arrayOffset()+directBuffer.readerIndex();
            int length=directBuffer.readableBytes();
            System.out.println(array);
            System.out.println(offset);
            System.out.println(length);
        }
        directBuffer.release();
    }

    //混合缓冲区
    @Test
    public void testCompositeBuffer(){
        CompositeByteBuf cbuf=ByteBufAllocator.DEFAULT.compositeBuffer();
        //消息头
        ByteBuf headerBuf=Unpooled.copiedBuffer("消息头".getBytes());
        //消息体1
        ByteBuf bodyBuf=Unpooled.copiedBuffer("消息体1".getBytes());
        cbuf.addComponents(headerBuf,bodyBuf);

        sendMsg(cbuf);
        //在refCnt为0之前，retain
        headerBuf.retain();
        cbuf.release();
        cbuf=ByteBufAllocator.DEFAULT.compositeDirectBuffer();

        //消息体2
        bodyBuf=Unpooled.copiedBuffer("消息体2222".getBytes());
        cbuf.addComponents(headerBuf,bodyBuf);
        sendMsg(cbuf);
        cbuf.release();

    }
    private void sendMsg(CompositeByteBuf cbuf){
        //处理整个消息
        for(ByteBuf b:cbuf){
            int length =b.readableBytes();
            byte[] array=new byte[length];
            //将CompositeByteBuf中的数据复制到数组中
            b.getBytes(b.readerIndex(),array);
            //处理一下数组中的数据
            System.out.print(new String(array));

        }
        System.out.println();
    }

    @Test
    public void intCompositeBufComposite(){
        CompositeByteBuf cbuf=Unpooled.compositeBuffer(3);
        cbuf.addComponent(Unpooled.wrappedBuffer(new byte[]{1,2,3}));
        cbuf.addComponent(Unpooled.wrappedBuffer(new byte[]{4}));
        cbuf.addComponent(Unpooled.wrappedBuffer(new byte[]{5,6,7}));
        //合并成一个缓冲区----》nio的缓冲区不是netty的缓冲区
        ByteBuffer nioBuffer=cbuf.nioBuffer(0,6);
        byte[] bytes=nioBuffer.array();
        for (byte b:bytes){
            System.out.print(b);
        }
        cbuf.release();
    }

    /**
     * ByteBuf自动释放
     * tailHandler自动释放
     * SimpleChannelInboundHandler 自动释放
     */




}
