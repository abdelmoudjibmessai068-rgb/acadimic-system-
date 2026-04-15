package com.academic.repository;

import org.bson.types.ObjectId;
import java.util.List;

public interface IRepository<T> {
    void save(T entity);
    T findById(ObjectId id);
    List<T> findAll();
    void update(T entity);
    void delete(ObjectId id);
}
