package com.code.chapter02Reactor.chapter02Multi;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * 反应器和生产消费模式对比：
 * 反应器是基于查询的，查询到io事件后反应器根据io选择键，分发事件给handler
 *
 * 生产消费模式，一个或多个生产者将事件加入到一个队列中，一个或多个消费者主动拉取事件处理
 *
 * @author 韩明轩
 * <br/>date 2022-11-08
 */

/**
 * 反应器和观察者模式
 * 反应器：handler和IO选择键的订阅关系，是事件绑定到一个handler处理器；每一个IO事件（选择键）被查询后，反应器会将事件分发给所绑定的Handler处理器；
 * 观察者：同一时刻，同一个topic可以被 订阅过的多个观察者处理
 *
 */

/**
 * reactor：
 * 优点：响应快：同一个反应器线程同步的，但是不会被单个连接同步IO阻塞
 * 可扩展
 *
 * 缺点：复杂
 * 需要IO多路复用（linux的epoll）支持
 * 同一个handler业务线程中，如果出现了一个长时间的读写操作，会营销这个反应器中其他通道的IO处理
 *
 */

/**
 * 在单线程回显服务器EchoServer 的基础上，：
 * 1 引入多个选择器
 * 2 设计一个新的子反应器（SubReactor） 一个子反应器负责查询一个选择器
 * 3 开启多个反应器的处理线程，一个线程负责执行一个子反应器SubReactor
 *
 */
public class MultiThreadEchoServerReactor {

    ServerSocketChannel serverSocketChannel;
    AtomicInteger next=new AtomicInteger(0);
    //选择器集合
    Selector[] selectors=new Selector[2];//搞两个就行了
    //引入多个子反应器
    SubReactor[] subReactors=null;

    MultiThreadEchoServerReactor() throws IOException{
        //初始化多个选择器
        selectors[0]=Selector.open();
        selectors[1]=Selector.open();

        serverSocketChannel=ServerSocketChannel.open();

        InetSocketAddress address=new InetSocketAddress(8899);

        serverSocketChannel.socket().bind(address);
        //非阻塞
        serverSocketChannel.configureBlocking(false);
        //第一个选择器，负责监控新连接事件
        SelectionKey sk=serverSocketChannel.register(selectors[0],SelectionKey.OP_ACCEPT);
        //绑定Handler ： attach新连接监控 handler处理器到SelectionKey(选择键)
        sk.attach(new AcceptorHandler());

        //第一个子反应器
        SubReactor subReactor1=new SubReactor(selectors[0]);
        //第二个子反应器
        SubReactor subReactor2=new SubReactor(selectors[1]);

        subReactors=new SubReactor[]{subReactor1,subReactor2};

    }


    private void startService(){
        //一个子反应器对应一个线程
        new Thread(subReactors[0]).start();
        new Thread(subReactors[1]).start();
    }




    //子反应器
    class SubReactor implements Runnable{

        //每个线程负责一个选择器的查询和选择
        final Selector selector;
        public SubReactor(Selector selector){
            this.selector=selector;
        }
        public void run() {
            try{
                while(!Thread.interrupted()){
                    selector.select();
                    Set<SelectionKey> keySet=selector.selectedKeys();
                    Iterator<SelectionKey> it= keySet.iterator();
                    while (it.hasNext()){
                        //反应器负责dispatch收到的事件
                        SelectionKey sk=it.next();
                        dispatch(sk);
                    }
                    keySet.clear();
                }
            }catch (Exception e){

            }
        }

        void dispatch(SelectionKey sk){
            Runnable handler=(Runnable) sk.attachment();
            //调用之前 attach绑定到选择键的handler 处理器对象
            if(handler!=null){
                handler.run();
            }
        }
    }

    //Handler 新的连接处理器
    class AcceptorHandler implements Runnable {
        public void run() {
            try{
                SocketChannel channel=serverSocketChannel.accept();
                if(channel !=null){
                    new MultiThreadEchoServerHandler(selectors[next.get()] ,channel);
                }

            }catch (Exception e){

            }
            if(next.incrementAndGet() == selectors.length){
                next.set(0);
            }
        }
    }

    public static void main(String[] args) throws IOException {
        MultiThreadEchoServerReactor server=new MultiThreadEchoServerReactor();
        server.startService();
    }

}
