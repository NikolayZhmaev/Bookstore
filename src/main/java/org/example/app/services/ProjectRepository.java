package org.example.app.services;

import java.util.List;
import java.util.Set;

public interface ProjectRepository<T> {
    List<T> retreiveAll();

    void store(T name);

    boolean removeItemById(Integer bookIdToRemove);

    boolean remove(T object);

    Set<T> filter(T object);

    T search(String name);
}