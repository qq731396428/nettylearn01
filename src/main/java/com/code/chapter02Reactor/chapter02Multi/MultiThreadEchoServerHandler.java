package com.code.chapter02Reactor.chapter02Multi;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 在单线程 ECHOhandler的基础上
 * 引入了线程池，业务处理的代码执行在自己的线程池中，彻底地做到业务处理线程和反应器IO事件线程的完全隔离
 */
public class MultiThreadEchoServerHandler implements Runnable{

    final SocketChannel channel;
    final SelectionKey sk;
    final ByteBuffer byteBuffer=ByteBuffer.allocate(1024);
    static final int RECIEVING=0,SENDING=1;
    int state=RECIEVING;

    //引入线程池
    static ExecutorService pool= Executors.newFixedThreadPool(4);

    MultiThreadEchoServerHandler(Selector selector,SocketChannel c)throws IOException{
        channel=c;
        c.configureBlocking(false);
        //取得选择键，设置感兴趣的IO事件
        sk=channel.register(selector,0);
        //将本Handler 作为sk选择键的附件，方便事件分发 dispatch
        sk.attach(this);
        //向sk选择键注册 Read就绪 的事件
        sk.interestOps(SelectionKey.OP_READ);
        selector.wakeup();
    }

    public void run(){
        //异步任务，在独立的线程池中执行
        pool.execute(new AsyncTask());

    }

    //业务处理，不在反应器线程中执行
    public synchronized  void asyncRun(){
        try{
            if(state==SENDING){
                //写入通道
                channel.write(byteBuffer);
                //写完后，准备开始从通道读，byteBuffer切换成写入模式
                byteBuffer.clear();
                //写完后进入接收状态
                state=RECIEVING;
            }else if(state==RECIEVING){
                //从通道中读
                int length=0;
                while((length = channel.read(byteBuffer))>0){
                    String a=new String(byteBuffer.array(),0,length);
                    System.out.println(a);
                }
                //读完后，准备开始写入通道，byteBuffer 切换成读取模式
                byteBuffer.flip();
                //读完后，注册write就绪事件
                sk.interestOps(SelectionKey.OP_WRITE);
                //进入发送状态
                state=SENDING;
            }
            //处理结束后，这里不能管理 select key，需要重复时间
            //sk.cancel();

        }catch (Exception e) {

        }
    }

    /**
     * 代码中设计了一个 asyncTask，是一个简单的异步任务的提交类。
     * 它可以使异步业务方法（asyncRun） 可以独立地提交到线程池中。
     */
    //异步任务的内部类
    class AsyncTask implements Runnable{
        public void run() {
            MultiThreadEchoServerHandler.this.asyncRun();
        }
    }

}
