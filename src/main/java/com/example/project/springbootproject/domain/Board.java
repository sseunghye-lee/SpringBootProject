package com.example.project.springbootproject.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
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

    public void deleteBoard(){
        this.delete = 1;
    }

    public void updateBoard(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
