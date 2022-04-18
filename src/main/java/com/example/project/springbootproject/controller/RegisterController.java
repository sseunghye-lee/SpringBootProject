package com.example.project.springbootproject.controller;

import com.example.project.springbootproject.domain.User;
import com.example.project.springbootproject.domain.UserDTO;
import com.example.project.springbootproject.repository.UserRepository;
import com.example.project.springbootproject.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@RequiredArgsConstructor
@Controller
public class RegisterController {

    private final UserService userService;

    @PostMapping("/register")
    public String insertUser(UserDTO user) {
        userService.insertUser(user);

        return "login";
    }

    @ResponseBody
    @PostMapping("/usernameCheck")
    public boolean usernameCheck(@RequestParam("username") String username) {
        return userService.usernameCheck(username);
    }

}
