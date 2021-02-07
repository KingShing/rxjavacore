package demo;

import core.Observable;
import core.Observer;
import core.SchedulerWorker;
import core.easycall.Consumer;
import core.operator.Function;

/**
 * 这个demo，是rxjava的核心思想，便于研究rxjava的设计原理
 * <p>
 * 2/7 日 add 线程切换
 * <p>
 * 如果core包下类弄懂了，线程切换和操作符等都是锦上添花的功能
 * <p>
 * Created by kingshingyeh on 2021/2/5.
 */
public class DataSource {

    public static String currThreadName() {
        return " [thread: " + Thread.currentThread().getName() + "] ";
    }

    public static void printlnData(String data) {
        String res = " [data: " + data + "] ";
        System.out.println(res);
    }

    public static Observable<String> loadData(String name) {
        return Observable.create(emitter -> {
                    // 耗时操作，工作线程
                    printlnData("emitter.onNext: " + currThreadName());
                    emitter.onNext(name);
                }
        );
    }

    public static void main(String[] args) {

        // simple 1
//        DataSource.loadData("kingshing")
//                .compose(SchedulerWorker.ioMain())
//                .subscribe(new Observer<String>() {
//                    @Override
//                    public void onSubscribe() {
//                        // 观察者被 被观察者调用了 会回调此方法
//                        System.out.println("onSubscribe: " + currThreadName());
//                    }
//
//                    @Override
//                    public void onNext(String data) {
//                        // 接收数据
//                        System.out.println("onNext: data-> " + data + currThreadName());
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        System.out.println("onComplete: " + currThreadName());
//                    }
//                });

        // simple 2
//        DataSource.loadData("kingshing")
//                .compose(SchedulerWorker.ioMain())
//                .subscribe(new Consumer<String>() {
//                    @Override
//                    public void accept(String data) throws Exception {
//                        printlnData(data);
//                    }
//                });


        // simple 3
        DataSource.loadData("123")
                .map(Integer::parseInt)
                .compose(SchedulerWorker.ioMain())
                .subscribe(number -> {
                    printlnData((++number).toString());
                }, throwable -> {
                    printlnData("异常：" + throwable.toString());
                });
    }
}