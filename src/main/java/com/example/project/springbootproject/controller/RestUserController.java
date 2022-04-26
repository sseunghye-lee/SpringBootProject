package com.example.project.springbootproject.controller;

import com.example.project.springbootproject.domain.UserDTO;
import com.example.project.springbootproject.service.UserService;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RequiredArgsConstructor
@RestController
public class RestUserController {

    private final UserService userService;

    @PostMapping("/login")
    public void login(String username, String password, HttpSession session, HttpServletResponse response)
        throws IOException {

        userService.login(username, password, session);
        response.sendRedirect("/mainPage");
    }

    @RequestMapping("/user/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response)
        throws IOException {
        HttpSession session = request.getSession();
        session.invalidate();

        response.sendRedirect("/");
    }

    @PostMapping("/register")
    public ModelAndView insertUser(UserDTO user) {
        userService.insertUser(user);
        return new ModelAndView("login");
    }

    @ResponseBody
    @PostMapping("/usernameCheck")
    public boolean usernameCheck(@RequestParam("username") String username) {
        return userService.usernameCheck(username);
    }

}
