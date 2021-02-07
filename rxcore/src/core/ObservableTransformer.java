package core;

public interface ObservableTransformer<P, R> {
    Observable<R> apply(Observable<P> upstream);
}