package com.code.chapter03FutureAysn;

import com.google.common.util.concurrent.*;
import org.checkerframework.checker.nullness.qual.Nullable;


import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * guava的异步任务接口ListenableFuture 扩展了java的Future接口，实现了非阻塞获取异步结果的能力：
 * （1）引入了一个新的接口 ListenableFuture继承了java的Future
 * （2）引入了FutureCallback，在异步执行完成后，根据异步结果，完成不同的回调处理，可以处理异步结果
 */

/**
 * FutureCallback：
 * onSuccess方法，在异步任务执行成功后被回调，调用时，异步任务执行结果作为onSuccess方法的参数被传入
 * FutureCallback和 java的Callable的区别：
 *  callable：代表异步执行的逻辑。
 *  FutureCallback接口，代表的是Callable异步执行逻辑完成之后，根据 成功 或 异常 两种情况，所需要执行的后续工作
 */

/**
 * ListenableFuture：
 * 它仅增强了一个方法：addListener方法：他的作用是在callable异步任务完成之后，回调FutureCallback
 */

/**
 * ListenableFuture异步任务 需要通过 guava线程池 来获取
 * Guava线程池：ListeningExecutorService gPool=MoreExecutors.listeningDecorator(java线程池);
 * 通过gPool.submit 就可以拿到 ListenableFuture了
 * 总结：
 * （1）创建java线程池
 * （2）创建guava线程池
 * （3）把callable/runable的逻辑实例，通过submit提交到guava线程池中，获取ListenableFuture异步任务实例
 * （4）创建FutureCallback回调实例
 *  当callable/runable异步执行完成之后，就会回调FutureCallback的回调方法了
 */

public class GuavaFutureDemo {

    public static final int SLEEP_GAP=500;
    public static String getCurThreadName(){
        return Thread.currentThread().getName();
    }

    static class HotWarterJob implements Callable<Boolean> {
        public Boolean call(){
            try{
                System.out.println("洗水壶");
                System.out.println("管好凉水");
                System.out.println("放在火上");
                //睡一下，代表烧水
                Thread.sleep(SLEEP_GAP);
                System.out.println("水开了");
            }catch (Exception e){
                return false;
            }
            System.out.println("烧水结束");
            return true;
        }
    }

    static class WashJob implements Callable<Boolean>{
        public  Boolean call(){
            try{
                System.out.println("洗茶壶");
                System.out.println("洗茶杯");
                //sleep一下，代表清洗中
                Thread.sleep(SLEEP_GAP);
                System.out.println("拿茶叶");

                System.out.println("洗完了");
            }catch (Exception e){
                return false;
            }
            System.out.println("清洗结束");
            return true;
        }
    }



    //新建一个异步业务类型，作为泡茶喝的主线程类
    static class MainJob implements Runnable{
        boolean waterOk=false;
        boolean cupOk=false;
        int gap=SLEEP_GAP/10;

        public void run() {
            while (true){
                try{
                    Thread.sleep(gap);
                    System.out.println("主线程读书中");
                }catch (Exception e){
                    System.out.println(getCurThreadName()+"发生异常中断");
                }
                if(waterOk && cupOk){
                    drinkTea(waterOk,cupOk);
                }
            }
        }

        public void drinkTea(boolean wOk,boolean cOk){
            if(wOk && cOk){
                this.waterOk=false;
                this.gap=SLEEP_GAP *100;
                System.out.println("泡茶喝");
            }else {
                System.out.println("不喝了");
            }
        }
    }


    public static void main(String[] args) {
        //创建一个新的线程实例，作为泡茶主线程
        final MainJob mainJob=new MainJob();
        Thread mainThread=new Thread(mainJob);
        mainThread.setName("主线程");
        mainThread.start();

        //烧水的业务逻辑实例
        Callable<Boolean> hotJob=new HotWarterJob();
        //清洗的业务逻辑实例
        Callable<Boolean> washJob=new WashJob();

        //创建java线程池
        ExecutorService jPool= Executors.newFixedThreadPool(10);

        //包装java线程池，构造Guava 线程池
        ListeningExecutorService gPool= MoreExecutors.listeningDecorator(jPool);

        //提交烧水的业务实例，到Guava线程池获取异步任务
        ListenableFuture<Boolean> hotFuture=gPool.submit(hotJob);
        //绑定异步回调，烧水完成后，把喝水任务的waterOK变成true
        Futures.addCallback(hotFuture, new FutureCallback<Boolean>() {
            public void onSuccess(@Nullable Boolean result) {
                if(result){
                    mainJob.waterOk=true;
                }
            }
            public void onFailure(Throwable t) {
                System.out.printf("烧水失败");
            }
        } ,gPool);

        //提交清洗的业务逻辑实例，到Guava线程池中获取异步任务
        ListenableFuture<Boolean> washFuture=gPool.submit(washJob);
        //绑定异步回调，烧水完成后，把喝水任务的cupOK变成true
        Futures.addCallback(washFuture, new FutureCallback<Boolean>() {
            public void onSuccess(@Nullable Boolean result) {
                if(result){
                    mainJob.cupOk=true;
                }
            }
            public void onFailure(Throwable t) {
                System.out.printf("清洗失败");
            }
        }
        ,gPool );
    }

}
