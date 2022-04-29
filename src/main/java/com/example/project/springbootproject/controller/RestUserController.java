package com.example.project.springbootproject.controller;

import static com.example.project.springbootproject.util.ApiUtils.error;
import static com.example.project.springbootproject.util.ApiUtils.success;

import com.example.project.springbootproject.domain.UserDTO;
import com.example.project.springbootproject.service.UserService;
import com.example.project.springbootproject.util.ApiUtils.ApiResult;
import com.example.project.springbootproject.util.JwtUtils;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
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
    public ApiResult<String> login(@RequestBody UserDTO userDTO, HttpSession session)
        throws IOException {

        userService.login(userDTO.getUsername(), userDTO.getPassword(), session);
        String userToken = JwtUtils.createJwt(userDTO.getUsername());
        return success(userToken);
    }

    @GetMapping("/user/logout")
    public ApiResult<?> logout(@RequestHeader(value = "userToken", required = false) String userToken)
        throws UnsupportedEncodingException {
        String username = (String) JwtUtils.checkJwt(userToken).get("username");
        if("".equals(username)) {
            return success("로그아웃 성공");
        } else {
            return error("로그아웃 실패", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/register")
    public ApiResult<String> insertUser(@RequestBody UserDTO user) {
        userService.insertUser(user);
        return success("REGIST OK");
    }

    @ResponseBody
    @PostMapping("/usernameCheck")
    public boolean usernameCheck(@RequestParam("username") String username) {
        return userService.usernameCheck(username);
    }

}
