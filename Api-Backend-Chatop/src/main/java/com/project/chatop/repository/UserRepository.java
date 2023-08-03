package com.project.chatop.repository;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.project.chatop.model.User;

@Repository
// Crud Repository pour User
public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
