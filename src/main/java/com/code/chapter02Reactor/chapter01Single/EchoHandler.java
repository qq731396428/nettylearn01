package com.code.chapter02Reactor.chapter01Single;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.logging.Logger;


public class EchoHandler implements Runnable{

    final SocketChannel channel;
    final SelectionKey sk;

    final ByteBuffer byteBuffer= ByteBuffer.allocate(1024);

    static final int RECIEVING=0,SENDING=1;

    int state=RECIEVING;
    EchoHandler(Selector selector,SocketChannel c) throws IOException{
        channel=c;
        c.configureBlocking(false);
        //取得选择键，再设置感兴趣的IO事件，
        sk=channel.register(selector,0);
        //将handler自身作为选择键的附件
        sk.attach(this);
        //注册read就绪事件
        sk.interestOps(SelectionKey.OP_READ);
        selector.wakeup();

    }


    public void run() {
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
}
