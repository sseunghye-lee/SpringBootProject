package com.example.project.springbootproject.controller;

import com.example.project.springbootproject.domain.User;
import com.example.project.springbootproject.repository.UserRepository;
import com.example.project.springbootproject.service.UserService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Controller
public class LoginController {

    private final UserService userService;

    @PostMapping("/login")
    public String login(String username, String password, HttpServletRequest request, Model model) {
        boolean loginValidation = userService.validationLogin(username, password);

        if(loginValidation == false) {
            return "redirect:/";
        }
        User user = userService.user(username);

        if(user == null) {
            return "redirect:/";
        }
        HttpSession session = request.getSession();
        session.setAttribute("user", user);

        return "redirect:/mainPage";
    }

    @RequestMapping("/user/logout")
    public String logout(HttpServletRequest request)  {
        HttpSession session = request.getSession();
        session.invalidate();

        return "redirect:/";
    }
}
