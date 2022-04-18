package com.example.project.springbootproject.service;

import com.example.project.springbootproject.domain.Board;
import com.example.project.springbootproject.domain.BoardDTO;
import com.example.project.springbootproject.repository.BoardRepository;
import com.example.project.springbootproject.repository.BoardRepositoryImpl;
import java.util.List;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PostService {
    private final BoardRepository boardRepository;
    private final BoardRepositoryImpl boardRepositoryImpl;

    public void insertPost(BoardDTO boardDTO) {
        Board board = new Board(boardDTO.getTitle(), boardDTO.getUsername(), boardDTO.getContent());

        boardRepository.save(board);

    }

    public Board findBoard(int boardId) {
        return boardRepository.findById(boardId);
    }

    public List<Board> myPostList(String username) {
        return boardRepository.findAllByUsername(username);
    }

    public void deleteBoard(int boardId) {
        boardRepositoryImpl.deleteBoard(boardId);
    }

    public void updateBoard(int boardId, String title, String content) {
        boardRepositoryImpl.updateBoard(boardId, title, content);
    }

    public List<Board> boardList() {
        return boardRepositoryImpl.boardList();
    }

    public Page<Board> myBoardList(String username, Pageable pageable) {
        return boardRepositoryImpl.pagingMyBoardList(username, pageable);
    }

    public Page<Board> getBoardList(Pageable pageable) {
        return boardRepositoryImpl.pagingBoardList(pageable);
    }

}
