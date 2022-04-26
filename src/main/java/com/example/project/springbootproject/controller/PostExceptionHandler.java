package com.example.project.springbootproject.controller;

import static com.example.project.springbootproject.util.ApiUtils.newResponse;

import com.example.project.springbootproject.exception.BoardException;
import com.example.project.springbootproject.exception.MyPostException;
import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class PostExceptionHandler {

    @ExceptionHandler(BoardException.class)
    public ResponseEntity<?> boardError(Model model, BoardException e) {
        model.addAttribute("boardError", "해당 게시글이 없습니다");
        return newResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MyPostException.class)
    public ResponseEntity<?> myBoardError(Model model, MyPostException e) {
        return newResponse("정상적인 경로를 통해 접근해주세요", HttpStatus.BAD_REQUEST);
    }
}
