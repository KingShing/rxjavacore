package demo;

import core.Observable;
import core.Observer;
import core.SchedulerWorker;

/**
 * 这个demo，是rxjava的核心思想，便于研究rxjava的设计原理
 *
 *  2/7 日 add 线程切换
 *
 * 如果core包下类弄懂了，线程切换和操作符等都是锦上添花的功能
 * <p>
 * Created by kingshingyeh on 2021/2/5.
 */
public class DataSource {

    public static String currThreadName() {
        return " [thread: " + Thread.currentThread().getName() + "] ";
    }

    public static Observable<String> loadData(String name) {
        return Observable.create(emitter -> {
                    // 耗时操作，工作线程
                    System.out.println("emitter.onNext: " + currThreadName());
                    emitter.onNext("hi , " + name);
                }
        );
    }

    public static void main(String[] args) {

        DataSource.loadData("kingshing")
                .compose(SchedulerWorker.ioMain())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe() {
                        // 观察者被 被观察者调用了 会回调此方法
                        System.out.println("onSubscribe: " + currThreadName());
                    }

                    @Override
                    public void onNext(String data) {
                        // 接收数据
                        System.out.println("onNext: data-> " + data + currThreadName());
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        System.out.println("onComplete: " + currThreadName());
                    }
                });
    }
}
