package com.code.chapter03FutureAysn;


/**
 * 使用join实现泡茶
 * 在java，线程Thread的合并流程：假设线程A调用了线程B的B.join方法，合并B线程。
 * 那么，A线程进入阻塞，直到B线程执行完成
 *
 */

/**
 * join的使用场景：A线程调用B线程的join，等待B线程执行完；B线程没有完成之前，A线程阻塞
 * join的三种重载：
 * void join() : A线程等待B线程执行结束后，A线程重新恢复执行
 * void join(long mills) A线程等待B线程执行，如果等待超过了mills毫秒的话，不论B是否结束，A线程都会恢复执行
 * void join(long mills，int nanos) 同上，等待时间为mills毫秒+nanos纳秒
 */

/**
 * join是实例方法，不是静态方法
 * join调用时，不是线程所指向的目标线程阻塞，二十当前线程阻塞
 * 当前线程需要等到阻塞结束
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
                //睡一下，代表烧水
                Thread.sleep(SLEEP_GAP);
                System.out.println("水开了");
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
