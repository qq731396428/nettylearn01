package com.code.chapter02Reactor.chapter01Single;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class EchoServerReactor implements Runnable{
Selector selector;
ServerSocketChannel serverSocketChannel;

EchoServerReactor() throws IOException{
    // 获取选择器、开启serverSocket服务监听通道
    //绑定AcceptorHandler新连接处理器到selectKey

}

    /**
     * 轮询和分发事件
     */
    public void run() {
        try{
            while(!Thread.interrupted()) {
                selector.select();
                Set<SelectionKey> selected = selector.selectedKeys();
                Iterator<SelectionKey> it = selected.iterator();

                while (it.hasNext()) {
                    //反应器负责dispatch收到的事件
                    SelectionKey sk = it.next();
                    dispatch(sk);
                }
                selected.clear();
            }
        }catch (Exception e){

        }
    }


    void dispatch(SelectionKey sk){
        Runnable handler=(Runnable) sk.attachment();
        //调用之前attach绑定到选择键的handler处理器对象
        if(handler!=null){
            handler.run();
        }
    }
    class AcceptorHandler implements Runnable{

        public void run() {
            try{
                SocketChannel channel=serverSocketChannel.accept();

                if(channel!=null){
                    new EchoHandler(selector,channel);
                }
            }catch (Exception e){

            }
        }
    }

    public static void main(String[] args) throws IOException {
        new Thread(new EchoServerReactor()).start();
    }

}
