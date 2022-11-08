package com.code.chapter03FutureAysn;


/**
 * 使用join实现泡茶
 * 在java，线程Thread的合并流程：假设线程A调用了线程B的B.join方法，合并B线程。
 * 那么，A线程进入阻塞，直到B线程执行完成
 *
 */
public class JoinDemo {
    public static final int SLEEP_GAP=500;

    public static String getCurThreadName(){
        return Thread.currentThread().getName();
    }

    static class HotWarterThread extends Thread{
        public HotWarterThread(){
            super("####烧水 线程");
        }
        @Override
        public void run(){
            try{
                System.out.println("洗水壶");
                System.out.println("管好凉水");
                System.out.println("放在火上");
            }catch (Exception e){

            }
            System.out.println("烧水结束");
        }
    }

    static class WashThread extends Thread{
        public WashThread(){
            super("$$$$清洗线程");
        }
        @Override
        public void run(){
            try{
                System.out.println("洗茶壶");
                System.out.println("洗茶杯");
                //sleep一下，代表清洗中
                Thread.sleep(SLEEP_GAP);
                System.out.println("拿茶叶");

                System.out.println("洗完了");
            }catch (Exception e){

            }
            System.out.println("清洗结束");
        }
    }

    public static void main(String[] args) {
        Thread hThread=new HotWarterThread();
        Thread wThread=new WashThread();

        hThread.start();
        wThread.start();

        try{
            //合并烧水线程
            hThread.join();
            //合并清洗线程
            wThread.join();
            Thread.currentThread().setName("主线程");
            System.out.println("泡茶喝");

        }catch (Exception e){

        }
        System.out.println(getCurThreadName()+"运行结束");
    }

}
