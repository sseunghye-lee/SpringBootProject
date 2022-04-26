package com.example.project.springbootproject.controller;

import static com.example.project.springbootproject.util.ApiUtils.success;

import com.example.project.springbootproject.domain.Board;
import com.example.project.springbootproject.domain.BoardDTO;
import com.example.project.springbootproject.domain.UserDTO;
import com.example.project.springbootproject.exception.BoardException;
import com.example.project.springbootproject.exception.MyPostException;
import com.example.project.springbootproject.service.PostService;
import com.example.project.springbootproject.util.ApiUtils.ApiResult;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner.Mode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RequiredArgsConstructor
@RestController
public class RestPostController {

    @Autowired
    private final PostService postService;

    @GetMapping("/post/list")
    public ModelAndView post(Model model, HttpServletRequest request) {
        String page = request.getParameter("page");
        page = page == null ? "0" : page;
        model.addAttribute("page", page);
        HttpSession session = request.getSession();
        UserDTO user = (UserDTO) session.getAttribute("user");
        model.addAttribute("userSession", user);

        return new ModelAndView("post");
    }

    @GetMapping("/post-list")
    public  ApiResult<Page<Board>> postList(Model model, @PageableDefault(size = 10, sort ="boardId", direction = Direction.DESC)
        Pageable pageable, HttpServletRequest request) {

        HttpSession session = request.getSession();
        UserDTO user = (UserDTO) session.getAttribute("user");
        model.addAttribute("userSession", user);

        return success(postService.getBoardList(pageable));
    }

    @RequestMapping(value = "/detail/{boardId}", method = RequestMethod.GET)
    public Board postDetail(@PathVariable("boardId") long boardId, Model model, HttpServletRequest request) {
        Board board = postService.findBoard(boardId);
        model.addAttribute("board", board);

        String referer = request.getHeader("referer");
        model.addAttribute("backPage", referer);

        return board;
    }

    @GetMapping("/post/update/{boardId}")
    public Board postUpdate(@PathVariable("boardId") long boardId, Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        UserDTO user = (UserDTO) session.getAttribute("user");

        Board board = postService.findBoard(boardId);
        model.addAttribute("myBoard", boardId);

        if(!board.getUsername().equals(user.getUsername())){
            throw new BoardException("정상적인 경로를 통해 접근해주세요");
        }

        String referer = request.getHeader("referer");
        model.addAttribute("updateReferer", referer);
        model.addAttribute("userSession", user);

        return board;
    }

    @PutMapping("/update/{boardId}/{title}/{content}")
    public ApiResult<String> updatePost(@PathVariable("boardId") long boardId, @PathVariable("title") String title, @PathVariable("content") String content, HttpServletResponse response)
        throws IOException {
        postService.updateBoard(boardId, title, content);
        return success("UPDATE OK");
    }

    @GetMapping("/my/post")
    public ModelAndView myPost(Model model, HttpServletRequest request) {
        String page = request.getParameter("page");
        page = page == null ? "0" : page;
        model.addAttribute("page", page);
        return new ModelAndView("my_post");
    }

    @GetMapping("/my-post")
    public ApiResult<Page<Board>> myBoardList(Model model, HttpServletRequest request, @PageableDefault(size = 5, sort ="boardId", direction = Sort.Direction.DESC) Pageable pageable) {
        HttpSession session = request.getSession();
        UserDTO user = (UserDTO) session.getAttribute("user");
        model.addAttribute("userSession", user);
        return success(postService.myBoardList(user.getUsername(), pageable));
    }

    @DeleteMapping("/post/delete/{boardId}")
    public ApiResult<String> postDelete(@PathVariable("boardId") long boardId, @RequestBody Map<String, Object> request)
        throws IOException {

        postService.deleteBoard(boardId);

        return success("DELETE OK");
    }

    @PostMapping("/post/insert")
    public void postInsert(BoardDTO boardDTO, Model model, HttpServletRequest request, HttpServletResponse response)
        throws IOException {
        postService.insertPost(boardDTO);
        HttpSession session = request.getSession();
        UserDTO user = (UserDTO) session.getAttribute("user");
        model.addAttribute("userSession", user);

        response.sendRedirect("/post/list");
    }

    @RequestMapping("/backPage")
    public String backPage(HttpServletRequest request) {
        String referer = request.getHeader("referer");
        return "redirect:" + referer;
    }
}
