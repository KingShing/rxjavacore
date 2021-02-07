package core.operator;

import core.Observable;
import core.Observer;

public class ObservableMap<T, R> extends Observable<R> {

    private final Observable<T> source;
    private final Function<? super T, ? extends R> function;

    public ObservableMap(Observable<T> source, Function<? super T, ? extends R> function) {
        this.source = source;
        this.function = function;
    }

    @Override
    protected void subscribeActual(Observer<R> observer) {
        source.subscribe(new MapObserver<>(observer, function));
    }

    static class MapObserver<T, R> implements Observer<T> {
        private final Observer<R> observer;

        private final Function<? super T, ? extends R> function;

        public MapObserver(Observer<R> observer, Function<? super T, ? extends R> function) {
            this.observer = observer;
            this.function = function;
        }

        @Override
        public void onSubscribe() {
            observer.onSubscribe();
        }

        @Override
        public void onNext(T data) {
            try {
                R apply = function.apply(data);
                observer.onNext(apply);
            } catch (Exception e) {
                onError(e);
            }
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
}
