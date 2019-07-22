package ru.ponomarevaa.moneyexchange.repository.inmemory;

import ru.ponomarevaa.moneyexchange.model.AbstractBaseEntity;
import ru.ponomarevaa.moneyexchange.model.Account;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryBaseRepository<T extends AbstractBaseEntity> {

    AtomicLong counter;

    Map<Integer, T> entryMap;

    public InMemoryBaseRepository() {
        this.counter = new AtomicLong(0);
        this.entryMap = new ConcurrentHashMap<>();
    }

    public T save(T t) {
        Objects.requireNonNull(t, "Entry must not be null");
        if (t.isNew()) {
            t.setId((int)counter.incrementAndGet());
            entryMap.put(t.getId(), t);
            return t;
        }
        return entryMap.computeIfPresent(t.getId(), (id, oldT) -> t);
    }

    public boolean delete(int id) {
        return entryMap.remove(id) != null;
    }

    public T get(int id) {
        return entryMap.get(id);
    }

    public List<T> getAll() {
        return new ArrayList<>(entryMap.values());
    }
}
