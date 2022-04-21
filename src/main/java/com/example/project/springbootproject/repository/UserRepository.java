package com.example.project.springbootproject.repository;

import com.example.project.springbootproject.domain.Board;
import com.example.project.springbootproject.domain.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);
    User findByUsernameAndPassword(String username, String password);
    boolean existsByUsername(String username);

//    Optional<User> insertUser(User user);
}
