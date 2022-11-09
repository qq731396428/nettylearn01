package com.code.chapter03FutureAysn;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 * callable接口：call方法是有返回值的，runable的run方法没有返回值
 * callable是一个泛型接口，是一个函数式接口
 * callable的实例不能直接作为Thread的target来开启thread，so callable不能在线程里直接跑。
 */

/**
 * futureTask：是线程和callable的桥
 * futureTask 的结果保存在outcome中
 * 但是！ 使用futureTask的get取异步结果时也会阻塞线程
 *
 * future接口：（1）判断并发任务是否执行完成 （2）获取并发的任务完成后的结果  （3）取消并发执行中的任务
 */

public class JavaFutureDemo {

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

    public static void drinkTea(boolean warterOk,boolean cupOk){
        if(warterOk && cupOk){
            System.out.println("泡茶喝");
        }else {
            System.out.println("不喝了");
        }
    }

    public static void main(String[] args) {
        Callable<Boolean> hjob=new HotWarterJob();
        FutureTask<Boolean> hTask=new FutureTask<Boolean>(hjob);
        Thread hThread=new Thread(hTask,"####烧水线程");

        Callable<Boolean> wjob=new WashJob();
        FutureTask<Boolean> wTask=new FutureTask<Boolean>(wjob);
        Thread wThread=new Thread(wTask,"####洗杯子线程");

        hThread.start();
        wThread.start();

        Thread.currentThread().setName("主线程");

        try{
            boolean warterOk=hTask.get();
            boolean cupOk=wTask.get();
            drinkTea(warterOk,cupOk);
        }catch (Exception e){

        }
        System.out.printf(getCurThreadName()+"运行结束");
    }


}
