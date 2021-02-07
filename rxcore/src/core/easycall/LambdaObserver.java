package core.easycall;

import core.Observer;

public class LambdaObserver<T> implements Observer<T> {

    final Consumer<? super T> onNext;
    final Consumer<? super Throwable> onError;
    final Action onComplete;
    final Subscribe onSubscribe;

    public LambdaObserver(Consumer<? super T> onNext, Consumer<? super Throwable> onError, Action onComplete, Subscribe onSubscribe) {
        this.onNext = onNext;
        this.onError = onError;
        this.onComplete = onComplete;
        this.onSubscribe = onSubscribe;
    }

    @Override
    public void onSubscribe() {
        onSubscribe.onSubscribe();
    }

    @Override
    public void onNext(T data) {

        try {
            onNext.accept(data);
        } catch (Exception e) {
            onError(e);
        }
    }

    @Override
    public void onError(Throwable e) {
        try {
            onError.accept(e);
        } catch (Exception ignored) {
        }
    }

    @Override
    public void onComplete() {
        onComplete.onComplete();
    }
}
