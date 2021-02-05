
/**
 * Created by kingshingyeh on 2021/2/5.
 */
public abstract class Observable<T> implements ObservableSource<T> {
    @Override
    public void subscribe(Observer<T> observer) {
        observer.onSubscribe();
        subscribeActual(observer);
    }

    abstract void subscribeActual(Observer<T> observer);

    public static <T> Observable<T> create(ObservableOnSubscribe<T> source) {
        return new ObservableSimple<T>(source);
    }

    static class ObservableSimple<T> extends Observable<T> {

        private final ObservableOnSubscribe<T> source;

        public ObservableSimple(ObservableOnSubscribe<T> source) {
            this.source = source;
        }

        @Override
        void subscribeActual(Observer<T> observer) {
            CreateEmitter<T> createEmitter = new CreateEmitter<T>(observer);
            source.subscribe(createEmitter);
        }
    }

}
