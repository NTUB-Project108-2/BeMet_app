package tw.com.businessmeet.function;

@FunctionalInterface
public interface Consumer<T> {
    void accept(T t);
}
