package com.example.project.springbootproject.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BoardDTO {

    private Long boardId;
    private String title;
    private String content;
    private String username;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InsertDto{

        private String title;
        private String content;
        private String username;
    }

}
