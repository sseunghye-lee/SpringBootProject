package com.example.project.springbootproject.repository;

import com.example.project.springbootproject.domain.Board;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<Board, Integer> {
    List<Board> findAllByUsername(String username);

    Board findById(int boardId);

}
