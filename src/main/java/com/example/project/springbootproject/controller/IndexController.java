package com.example.project.springbootproject.controller;

import com.example.project.springbootproject.domain.User;
import com.example.project.springbootproject.domain.UserDTO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {
    @GetMapping("/")
    public String index() {
        return "login";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @GetMapping("/mainPage")
    public String mainPage(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        UserDTO user = (UserDTO) session.getAttribute("user");
        session.setAttribute("user", user);
        model.addAttribute("userSession", user);
        return "mainPage";
    }

}
