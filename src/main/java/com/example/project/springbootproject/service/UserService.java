package com.example.project.springbootproject.service;

import com.example.project.springbootproject.domain.User;
import com.example.project.springbootproject.domain.UserDTO;
import com.example.project.springbootproject.exception.InsertUserException;
import com.example.project.springbootproject.exception.PasswordException;
import com.example.project.springbootproject.exception.UserException;
import com.example.project.springbootproject.repository.UserRepository;
import java.util.Optional;
import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
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
        if(userRepository.existsByUsername(userDTO.getUsername())) {
            throw new InsertUserException("이미 있는 Username입니다");
        } else {
            userRepository.save(user);
        }
    }

    public User login(String username, String password) {
        return userRepository.findByUsernameAndPassword(username, password);
    }

    public User getByUsername(String username) {

        return userRepository.findByUsername(username).orElseThrow(() -> new UserException("사용자를 찾을 수 없습니다."));
    }

    public User validationLogin(String username, String password) {
        User user = this.getByUsername(username);
        if(!passwordEncoder.matches(password, user.getPassword())) {
            throw new PasswordException("비밀번호가 틀렸습니다.");
        }
        return user;
    }

    public boolean usernameCheck(String username) {
        return userRepository.existsByUsername(username);
    }

    public void login(String username, String password, HttpSession session) {
        User user = this.validationLogin(username, password);
        user.login(session);
    }
}
