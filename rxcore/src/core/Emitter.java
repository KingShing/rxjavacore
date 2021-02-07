package core;

/**
 * 发射器
 *
 * @param <T> 数据类型
 */
public interface Emitter<T> {

    void onNext(T data);

    void onError(Throwable e);

    void onComplete();
}
