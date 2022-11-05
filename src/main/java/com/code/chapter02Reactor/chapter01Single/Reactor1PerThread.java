package com.code.chapter02Reactor.chapter01Single;




import java.net.ServerSocket;
import java.net.Socket;



public class Reactor1PerThread implements Runnable{


    /**
     * 反应器模式有：Reactor反应器线程；Handlers处理器角色
     * （1）Reactor反应器线程：负责响应IO事件，分发到Handlers处理器
     * （2）Handlers处理器：非阻塞的执行业务处理逻辑
     *
     */

    public static void main(String[] args) {


    }

    public void run() {
        try{
            //服务器监听socket
            ServerSocket serverSocket=
                    new ServerSocket(9311);

            while (!Thread.interrupted()){
                Socket socket=serverSocket.accept();
                //接收一个连接后，为socket连接，新建一个专属的处理器对象
                Handler handler= new Handler(socket) ;
                //创建一个新线程，专门负责一个连接的处理
                new Thread((Runnable) handler).start();


            }

        }catch (Exception e){

        }


    }

    static class Handler implements Runnable{
        final Socket socket;
        Handler(Socket s){
            socket=s;
        }
        public void run(){
            while (true){
                try{
                    byte[] input =new byte[1024];
                    //读数据
                    socket.getInputStream().read(input);
                    //处理业务逻辑，获取处理结果
                    byte[] output=null;
                    //写入结果
                    socket.getOutputStream().write(output);
                }catch (Exception e){

                }
            }
        }
    }
}
