package core;

/**
 * 观察者
 *
 * @param <T> 数据类型
 */
public interface Observer<T> {

    void onSubscribe();

    void onNext(T data) ;

    void onError(Throwable e);

    void onComplete();
}
