package com.example.project.springbootproject.controller;

import com.example.project.springbootproject.domain.Board;
import com.example.project.springbootproject.domain.BoardDTO;
import com.example.project.springbootproject.domain.User;
import com.example.project.springbootproject.domain.UserDTO;
import com.example.project.springbootproject.exception.MyPostException;
import com.example.project.springbootproject.repository.BoardRepository;
import com.example.project.springbootproject.service.PostService;
import java.util.List;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.jaxb.SpringDataJaxb.PageRequestDto;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RequiredArgsConstructor
@Controller
public class PostController {

    private final PostService postService;

    EntityManager em;

    @GetMapping("/postList")
    public String postList(Model model, @PageableDefault(size = 10, sort ="boardId", direction = Direction.DESC)
        Pageable pageable, HttpServletRequest request) {
        
        HttpSession session = request.getSession();
        UserDTO user = (UserDTO) session.getAttribute("user");
        session.setAttribute("user", user);
        model.addAttribute("userSession", user);
        model.addAttribute("boardList", postService.getBoardList(pageable));

        return "post";
    }

//    @GetMapping("/post")
//    public String post(Model model, HttpServletRequest request) {
//
//        HttpSession session = request.getSession();
//        User user = (User) session.getAttribute("user");
//        session.setAttribute("user", user);
//        List<Board> boardList = postService.boardList();
//        model.addAttribute("userSession", user);
//        model.addAttribute("boardList", boardList);
//
//        return "post";
//    }

    @GetMapping("/post_new")
    public String postNew(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        UserDTO user = (UserDTO) session.getAttribute("user");
        session.setAttribute("user", user);
        model.addAttribute("userSession", user);
        return "post_new";
    }

    @GetMapping("/post_detail")
    public String postDetail(@RequestParam(value="boardId") int boardId, Model model, HttpServletRequest request) {
        Board board = postService.findBoard(boardId);
        model.addAttribute("board", board);

        String referer = request.getHeader("referer");
        model.addAttribute("backPage", referer);

        return "post_detail";
    }

    @GetMapping("/post_update")
    public String postUpdate(@RequestParam(value="boardId") int boardId, Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        UserDTO user = (UserDTO) session.getAttribute("user");

        Board board = postService.findBoard(boardId);
        model.addAttribute("myBoard", board);

        if(!board.getUsername().equals(user.getUsername())){
            throw new MyPostException("정상적인 경로를 통해 접근해주세요");
        }

        String referer = request.getHeader("referer");
        model.addAttribute("updateReferer", referer);


        session.setAttribute("user", user);
        model.addAttribute("userSession", user);

        return "post_update";
    }

    @PostMapping("/updatePost")
    public String updatePost(@RequestParam(value="boardId") int boardId, @RequestParam(value = "title") String title,
        @RequestParam(value = "content") String content, Model model) {
        postService.updateBoard(boardId, title, content);

        return "redirect:/my_post";

    }

    @GetMapping("/post_delete")
    public String postDelete(@RequestParam(value="boardId") int boardId, Model model, HttpServletRequest request,
        @PageableDefault(size = 2, sort ="boardId", direction = Sort.Direction.DESC) Pageable pageable) {
        postService.deleteBoard(boardId);

        HttpSession session = request.getSession();
        UserDTO user = (UserDTO) session.getAttribute("user");
        session.setAttribute("user", user);
        model.addAttribute("userSession", user);
        model.addAttribute("myPostList", myPostList(request, pageable));

        return "redirect:/my_post";
    }

    @GetMapping("/my_post")
    public String myPost(Model model, HttpServletRequest request) {

        HttpSession session = request.getSession();
        UserDTO user = (UserDTO) session.getAttribute("user");
        session.setAttribute("user", user);
        model.addAttribute("userSession", user);

        return "my_post";
    }

    @ModelAttribute("myPostList")
    public Page<Board> myPostList(HttpServletRequest request, @PageableDefault(size = 5, sort ="boardId", direction = Sort.Direction.DESC) Pageable pageable) {
        HttpSession session = request.getSession();
        UserDTO user = (UserDTO) session.getAttribute("user");

        return postService.myBoardList(user.getUsername(), pageable);
    }

    @PostMapping("/post_insert")
    public String postInsert(BoardDTO boardDTO, Model model, HttpServletRequest request) {
        postService.insertPost(boardDTO);
        HttpSession session = request.getSession();
        UserDTO user = (UserDTO) session.getAttribute("user");
        session.setAttribute("user", user);
        model.addAttribute("userSession", user);

        return "redirect:/postList";
    }

    @RequestMapping("/backPage")
    public String backPage(HttpServletRequest request) {
        String referer = request.getHeader("referer");
        return "redirect:" + referer;
    }


}
