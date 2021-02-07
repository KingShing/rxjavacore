package core.easycall;

public interface Consumer<T> {
    void accept(T data) throws Exception;
}
