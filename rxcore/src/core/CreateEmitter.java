package core;

/**
 * Created by kingshingyeh on 2021/2/5.
 */
class CreateEmitter<T> implements Emitter<T> {

    final private Observer<T> observer;

    public CreateEmitter(Observer<T> observer) {
        this.observer = observer;
    }

    @Override
    public void onNext(T data) {
        observer.onNext(data);
    }

    @Override
    public void onError(Throwable e) {
        observer.onError(e);
    }

    @Override
    public void onComplete() {
        observer.onComplete();
    }
}
