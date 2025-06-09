package com.globalpayments.scale.dao;


import com.globalpayments.scale.model.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {
    String create(User user);
    Optional<User> findById(String userId);
    Optional<User> findByEmail(String email);
    List<User> findAll();
    void update(User user);
    void updatePassword(String userId, String password);
    boolean delete(String userId);
    boolean existsByEmail(String email);
}