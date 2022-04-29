package com.example.project.springbootproject.controller;

import static com.example.project.springbootproject.util.ApiUtils.newResponse;

import com.example.project.springbootproject.exception.InsertUserException;
import com.example.project.springbootproject.exception.PasswordException;
import com.example.project.springbootproject.exception.UserException;
import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class LoginExceptionHandler {

    @ExceptionHandler(UserException.class)
    public ResponseEntity<?> userError(Model model, EntityNotFoundException e) {
        model.addAttribute("error", "사용자를 찾을 수 없습니다");
        return newResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PasswordException.class)
    public ResponseEntity<?> pwError(Model model, RuntimeException e) {
        model.addAttribute("error", "비밀번호가 틀렸습니다");
        return newResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InsertUserException.class)
    public ResponseEntity<?> insertError(Model model, RuntimeException e) {
        model.addAttribute("insertError", "이미 있는 Username입니다");
        return newResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

}
