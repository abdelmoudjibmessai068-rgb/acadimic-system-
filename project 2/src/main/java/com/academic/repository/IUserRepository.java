package com.academic.repository;

import com.academic.model.User;
import org.bson.types.ObjectId;
import java.util.List;

public interface IUserRepository {
    void save(User user);
    User findByUsername(String username);
    List<User> findAll();
    List<User> findByRole(String role);
    void update(User user);
    void delete(ObjectId id);
}
