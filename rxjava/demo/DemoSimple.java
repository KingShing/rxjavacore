/**
 * 这个demo，是rxjava的核心思想，便于研究rxjava的设计原理 阉割了很多功能，没有线程切换和 操作符
 * 如果core包下类弄懂了，线程切换和操作符等都是锦上添花的功能
 * <p>
 * Created by kingshingyeh on 2021/2/5.
 */
public class DemoSimple {

    public static void main(String[] args) {


        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(Emitter<String> emitter) {
                String mockData1 = "hello world";
                // 发射数据
                emitter.onNext(mockData1);

                String mockData2 = "从内存拿数据";
                emitter.onNext(mockData2);

                // 数据都发完了
                emitter.onComplete();

                // 如果失败了
                // emitter.onError(new Throwable("xxx失败"));
            }
        }).subscribe(new Observer<String>() {
            @Override
            public void onSubscribe() {
                // 观察者被 被观察者调用了 会回调此方法
            }

            @Override
            public void onNext(String data) {
                // 接收数据
                System.out.println(data);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }
}
