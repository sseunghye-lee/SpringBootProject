package com.example.project.springbootproject.service;

import com.example.project.springbootproject.domain.User;
import com.example.project.springbootproject.domain.UserDTO;
import com.example.project.springbootproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void insertUser(UserDTO userDTO) {
        String encodedPassword = passwordEncoder.encode(userDTO.getPassword());
        User user = new User(userDTO.getUsername(), encodedPassword, userDTO.getEmail(),
            userDTO.getPhone());

        userRepository.save(user);
    }

    public User login(String username, String password) {
        return userRepository.findByUsernameAndPassword(username, password);
    }

    public User user(String username) {
        return userRepository.findByUsername(username);
    }

    public boolean validationLogin(String username, String password) {
        User user = userRepository.findByUsername(username);
        if(!passwordEncoder.matches(password, user.getPassword())) {
            return false;
        }
        return true;
    }

    public boolean usernameCheck(String username) {
        return userRepository.existsByUsername(username);
    }

}
