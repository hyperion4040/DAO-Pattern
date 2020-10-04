package com.akozlowski.domain;

import java.util.Optional;

public interface Dao<T> {
    void save(final T t);

    Optional<T> findById(final int id);

    void update(final T t);

    void delete(final T t);
}
