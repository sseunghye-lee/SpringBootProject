package com.example.project.springbootproject.controller;

import com.example.project.springbootproject.domain.Board;
import com.example.project.springbootproject.domain.BoardDTO;
import com.example.project.springbootproject.domain.User;
import com.example.project.springbootproject.domain.UserDTO;
import com.example.project.springbootproject.exception.BoardException;
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
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RequiredArgsConstructor
@Controller
public class PostController {

    private final PostService postService;

    @GetMapping("/post_new")
    public String postNew(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        UserDTO user = (UserDTO) session.getAttribute("user");
        session.setAttribute("user", user);
        model.addAttribute("userSession", user);
        return "post_new";
    }

    @GetMapping("/post_detail")
    public ModelAndView postDetail(@RequestParam(value="boardId") int boardId, Model model, HttpServletRequest request) {
        model.addAttribute("boardId", boardId);

        String referer = request.getHeader("referer");
        model.addAttribute("backPage", referer);

        return new ModelAndView("post_detail");
    }

    @GetMapping("/post_update")
    public String postUpdate(@RequestParam(value="boardId") long boardId, Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        UserDTO user = (UserDTO) session.getAttribute("user");

//        Board board = postService.findBoard(boardId);
        model.addAttribute("myBoard", boardId);

//        if(!board.getUsername().equals(user.getUsername())){
//            throw new MyPostException("정상적인 경로를 통해 접근해주세요");
//        }

        String referer = request.getHeader("referer");
        model.addAttribute("updateReferer", referer);
        model.addAttribute("userSession", user);

        return "post_update";
    }
}
