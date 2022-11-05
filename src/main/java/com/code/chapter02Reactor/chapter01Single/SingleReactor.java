package com.code.chapter02Reactor.chapter01Single;


import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * 单线程Reactor反应器
 * 书上说：反射器模式类似事件驱动模式
 * 事件驱动模式：当有事件触发时，事件源会将事件dispatch分发到handler处理器进行事件处理。
 * 反应器模式：反应器角色类似于事件驱动模式中的dispatcher事件分发器角色
 *
 * Reactor：负责查询IO事件：当检测到一个IO事件，将其发送给相应的Handler处理器去处理。这里的IO事件，就是NIO选择器中监控的通道IO事件。
 * Handler：与IO事件（或选择键）绑定，负责IO事件的处理。完成真正的连接建立、通道的读取、处理业务逻辑、负责将结果写出到通道等
 */
public class SingleReactor implements Runnable{

    /**
     * 基于NIO，如何实现简单的单线程版本的的 反应器模式？需要selectionKey选择键
     * attach()：set方法
     * attachment：get方法
     * @param args
     */
    public static void main(String[] args) {
    }

    public void run() {
        //选择器轮询
        try{
            while(!Thread.interrupted()){
                selector.select();
                Set selected=selector.selectedKeys();
                Iterator<SelectionKey> it=selected.iterator();
                while(it.hasNext()){
                    //反应器负责dispatch收到的事件
                    dispatch(it.next());
                }
                selected.clear();
            }
        }catch (Exception e){

        }


    }

    Selector selector;
    ServerSocketChannel serverSocketChannel;
    void EchoServerReactor() throws IOException{
        //伪代码

        //省略：打开选择器，把channel注册到selector里面的逻辑
        //注册serverSocket得accept事件
        SelectionKey sk=serverSocketChannel.register(selector,SelectionKey.OP_ACCEPT);
        //将新连接处理器作为附件，绑定到sk选择器建
        sk.attach(new AcceptorHandler());

    }

    void dispatch(SelectionKey k){
        Runnable handler=(Runnable) (k.attachment());
        //吧handler 跑起来
        if(handler !=null){
            handler.run();
        }

    }

    /**
     * 一般有两个职责：接收新链接，为新连接创建一个 IO Handler
     */
    class AcceptorHandler implements Runnable{

        public void run(){
            //伪代码
            //接收新连接
            //需要为新连接，创建一个输入输出得handler处理器
        }
    }

    /**
     * IO Handler的构造器中，两点比较重要：
     * 新的SocketChannel传输通道，注册到了Reactor类的同一个选择器
     * channel注册完成后，将IO handler自己attach到选择器中
     */
    class IOHandler implements Runnable{
        final SocketChannel channel;
        final SelectionKey sk;
        IOHandler(SocketChannel c, Selector selector) throws IOException {
            this.channel = c;
            channel.configureBlocking(false);
            //仅仅取得选择键，稍后设置该兴趣的IO事件
            sk=channel.register(selector,0);
            //将handler处理器作为选择器的处理器
            sk.attach(this);
            //注册读写就绪事件
            sk.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);

        }
        public void run() {
            //处理输入输出
        }
    }


}
