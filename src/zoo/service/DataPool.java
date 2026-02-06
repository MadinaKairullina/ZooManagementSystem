package zoo.service;

import java.util.*;
import java.util.function.Predicate;

public class DataPool<T> {
    private final List<T> data = new ArrayList<>();

    public void add(T item) {
        data.add(Objects.requireNonNull(item));
    }

    public void addAll(Collection<T> items) {
        items.forEach(this::add);
    }

    public List<T> getAll() {
        return Collections.unmodifiableList(data);
    }

    // SEARCH / FIND
    public Optional<T> findFirst(Predicate<T> predicate) {
        return data.stream().filter(predicate).findFirst();
    }

    // FILTER
    public List<T> filter(Predicate<T> predicate) {
        return data.stream().filter(predicate).toList();
    }

    // SORT
    public List<T> sort(Comparator<T> comparator) {
        return data.stream().sorted(comparator).toList();
    }
}