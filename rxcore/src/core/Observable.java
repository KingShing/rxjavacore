package core;

import core.easycall.Consumer;
import core.easycall.Functions;
import core.easycall.LambdaObserver;
import core.operator.Function;
import core.operator.ObservableMap;

/**
 * Created by kingshingyeh on 2021/2/5.
 */
public abstract class Observable<T> implements ObservableSource<T> {
    @Override
    public void subscribe(Observer<T> observer) {
        subscribeActual(observer);
    }

    public void subscribe(Consumer<T> consumer) {
        LambdaObserver<T> lambdaObserver = new LambdaObserver<>(consumer, Functions.EmptyConsumer, Functions.EmptyAction, Functions.EmptySubscribe);
        subscribeActual(lambdaObserver);
    }

    public void subscribe(Consumer<T> consumer, Consumer<? super Throwable> onError) {
        LambdaObserver<T> lambdaObserver = new LambdaObserver<>(consumer, onError, Functions.EmptyAction, Functions.EmptySubscribe);
        subscribeActual(lambdaObserver);
    }

    abstract protected void subscribeActual(Observer<T> observer);

    public final <R> Observable<R> compose(ObservableTransformer<T, R> transformer) {
        return transformer.apply(this);
    }

    public final <R> Observable<R> map(Function<? super T, ? extends R> function) {
        return new ObservableMap<T, R>(this, function);
    }

    public Observable<T> observeOn(SchedulerWorker worker) {
        return new ObservableObserveOn<>(this, worker);
    }

    public Observable<T> subscribeOn(SchedulerWorker worker) {
        return new ObservableSubscribeOn<>(this, worker);
    }

    public static <T> Observable<T> create(ObservableOnSubscribe<T> source) {
        return new ObservableSimple<>(source);
    }

    static class ObservableSubscribeOn<T> extends Observable<T> {

        private final Observable<T> source;
        private final SchedulerWorker worker;

        public ObservableSubscribeOn(Observable<T> source, SchedulerWorker worker) {
            this.source = source;
            this.worker = worker;
        }

        @Override
        protected void subscribeActual(Observer<T> observer) {
            Runnable action = () -> source.subscribe(observer);
            worker.run(action);
        }
    }

    static class ObservableObserveOn<T> extends Observable<T> {

        private final Observable<T> source;
        private final SchedulerWorker worker;

        public ObservableObserveOn(Observable<T> source, SchedulerWorker worker) {
            this.source = source;
            this.worker = worker;
        }

        @Override
        protected void subscribeActual(Observer<T> observer) {
            source.subscribe(new ObserveOnObserver<>(observer, worker));
        }


        static class ObserveOnObserver<T> implements Observer<T> {

            private final SchedulerWorker worker;
            private final Observer<T> observer;

            public ObserveOnObserver(Observer<T> observer, SchedulerWorker worker) {
                this.worker = worker;
                this.observer = observer;
            }

            @Override
            public void onSubscribe() {
                worker.run(observer::onSubscribe);
            }

            @Override
            public void onNext(T data) {
                worker.run(() -> observer.onNext(data));
            }

            @Override
            public void onError(Throwable e) {
                worker.run(() -> observer.onError(e));
            }

            @Override
            public void onComplete() {
                worker.run(observer::onComplete);
            }
        }
    }

    static class ObservableSimple<T> extends Observable<T> {

        private final ObservableOnSubscribe<T> source;

        public ObservableSimple(ObservableOnSubscribe<T> source) {
            this.source = source;
        }

        @Override
        protected void subscribeActual(Observer<T> observer) {
            CreateEmitter<T> createEmitter = new CreateEmitter<>(observer);
            observer.onSubscribe();
            source.subscribe(createEmitter);
        }
    }


}
