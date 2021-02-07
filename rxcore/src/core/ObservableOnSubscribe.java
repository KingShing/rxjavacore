package core;

public interface ObservableOnSubscribe<T> {
    void subscribe(Emitter<T> emitter);
}
