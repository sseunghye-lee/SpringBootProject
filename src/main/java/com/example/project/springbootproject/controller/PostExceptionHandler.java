package com.example.project.springbootproject.controller;

import com.example.project.springbootproject.exception.BoardException;
import com.example.project.springbootproject.exception.MyPostException;
import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class PostExceptionHandler {
    @ExceptionHandler(BoardException.class)
    public String boardError(Model model, BoardException e) {
        model.addAttribute("boardError", "해당 게시글이 없습니다");
        return "/error/boardError";
    }

    @ExceptionHandler(MyPostException.class)
    public String myBoardError(Model model, MyPostException e) {
        model.addAttribute("myBoardError", "정상적인 경로를 통해 접근해주세요");
        return "/error/myBoardError";
    }
}
