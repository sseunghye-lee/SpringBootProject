package com.example.project.springbootproject.controller;

import com.example.project.springbootproject.domain.Board;
import com.example.project.springbootproject.domain.BoardDTO;
import com.example.project.springbootproject.domain.User;
import com.example.project.springbootproject.repository.BoardRepository;
import com.example.project.springbootproject.service.PostService;
import java.util.List;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@Controller
public class PostController {

    private final PostService postService;

    EntityManager em;

    @GetMapping("/post")
    public String post(Model model, HttpServletRequest request) {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        session.setAttribute("user", user);
        List<Board> boardList = postService.boardList();
        model.addAttribute("userSession", user);
        model.addAttribute("boardList", boardList);

        return "post";
    }

    @GetMapping("/post_new")
    public String postNew(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        session.setAttribute("user", user);
        model.addAttribute("userSession", user);
        return "post_new";
    }

    @GetMapping("/post_detail")
    public String postDetail(@RequestParam(value="boardId") int boardId, Model model) {
        Board board = postService.findBoard(boardId);
        model.addAttribute("board", board);

        return "post_detail";
    }

    @GetMapping("/post_update")
    public String postUpdate(@RequestParam(value="boardId") int boardId, Model model) {
        Board board = postService.findBoard(boardId);
        model.addAttribute("myBoard", board);
        return "post_update";
    }

    @PostMapping("/updatePost")
    public String updatePost(@RequestParam(value="boardId") int boardId, @RequestParam(value = "title") String title,
        @RequestParam(value = "content") String content, Model model) {
        postService.updateBoard(boardId, title, content);

        return "my_post";

    }

    @GetMapping("/post_delete")
    public String postDelete(@RequestParam(value="boardId") int boardId, Model model, HttpServletRequest request) {
        postService.deleteBoard(boardId);

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        session.setAttribute("user", user);
        model.addAttribute("userSession", user);
        model.addAttribute("myPostList", myPostList(request));

        return "redirect:/my_post";
    }

    @GetMapping("/my_post")
    public String myPost(Model model, HttpServletRequest request) {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        session.setAttribute("user", user);
        model.addAttribute("userSession", user);

        return "my_post";
    }

    @ModelAttribute("myPostList")
    public List<Board> myPostList(HttpServletRequest request) {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        return postService.myPostList(user.getUsername());
    }

    @PostMapping("/post_insert")
    public String postInsert(BoardDTO boardDTO, Model model, HttpServletRequest request) {
        postService.insertPost(boardDTO);
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        session.setAttribute("user", user);
        List<Board> boardList = postService.boardList();
        model.addAttribute("userSession", user);
        model.addAttribute("boardList", boardList);
        return "post";
    }


}
