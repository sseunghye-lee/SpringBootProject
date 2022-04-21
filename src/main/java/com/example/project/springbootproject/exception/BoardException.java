package com.example.project.springbootproject.exception;

import javax.persistence.EntityNotFoundException;

public class BoardException extends EntityNotFoundException {

    public BoardException(String message) {
        super(message);
    }
}
