package com.example.project.springbootproject.service;

import com.example.project.springbootproject.domain.Board;
import com.example.project.springbootproject.domain.BoardDTO;
import com.example.project.springbootproject.domain.BoardDTO.InsertDto;
import com.example.project.springbootproject.exception.BoardException;
import com.example.project.springbootproject.repository.BoardRepository;
import com.example.project.springbootproject.repository.BoardRepositoryImpl;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class PostService {
    @Autowired
    private final BoardRepository boardRepository;
    @Autowired
    private final BoardRepositoryImpl boardRepositoryImpl;

//    public void insertPost(BoardDTO boardDTO) {
//        Board board = new Board(boardDTO.getTitle(), boardDTO.getUsername(), boardDTO.getContent());
//
//        boardRepository.save(board);
//
//    }

    public void insertPost(InsertDto insertDto) {
        Board board = Board.insert(insertDto);
        boardRepository.save(board);
    }
    public Board findBoard(long boardId) {
        return boardRepository.findById(boardId).orElseThrow(() -> new BoardException("해당 게시글이 없습니다."));
    }

    @Transactional(readOnly = true)
    public List<Board> myPostList(String username) {
        return boardRepository.findAllByUsername(username);
    }

    public void deleteBoard(long boardId) {
        Board board = this.findBoard(boardId);
        board.deleteBoard();
    }

    public void updateBoard(long boardId, String title, String content) {
        Board board = this.findBoard(boardId);
        board.updateBoard(title, content);
    }

    public Page<Board> myBoardList(String username, Pageable pageable) {
        return boardRepositoryImpl.pagingMyBoardList(username, pageable);
    }

    public Page<Board> getBoardList(String searchText, Pageable pageable) {
        return boardRepositoryImpl.pagingBoardList(searchText, pageable);
    }

}
