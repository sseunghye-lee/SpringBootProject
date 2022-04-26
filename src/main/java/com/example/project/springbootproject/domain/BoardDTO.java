package com.example.project.springbootproject.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BoardDTO {

    private long boardId;
    private String title;
    private String content;
    private String username;

}
