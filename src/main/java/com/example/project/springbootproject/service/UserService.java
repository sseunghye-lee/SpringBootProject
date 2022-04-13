package com.example.project.springbootproject.service;

import com.example.project.springbootproject.domain.User;
import com.example.project.springbootproject.domain.UserDTO;
import com.example.project.springbootproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    public void insertUser(UserDTO userDTO) {
        User user = new User(userDTO.getUsername(), userDTO.getPassword(), userDTO.getEmail(),
            userDTO.getPhone());

        userRepository.save(user);
    }

    public User login(String username, String password) {
        return userRepository.findByUsernameAndPassword(username, password);
    }

    public boolean usernameCheck(String username) {
        return userRepository.existsByUsername(username);
    }

}
