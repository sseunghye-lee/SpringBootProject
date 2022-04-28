package com.example.project.springbootproject.domain;

import com.example.project.springbootproject.domain.BoardDTO.InsertDto;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Board {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "bd_id")
    private long boardId;

    @Column(name = "bd_title")
    private String title;

    @Column(name = "bd_content", columnDefinition = "TEXT")
    private String content;

    @Column(name = "bd_delete")
    private int delete;

    @Column(name = "bd_username")
    private String username;

    public Board(String title, String username, String content) {
        this.title = title;
        this.username = username;
        this.content = content;
    }

    public static Board insert(InsertDto insertDto){
        return Board.builder().title(insertDto.getTitle())
            .username(insertDto.getUsername())
            .content(insertDto.getContent())
            .build();
    }

    public void deleteBoard(){
        this.delete = 1;
    }

    public void updateBoard(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
