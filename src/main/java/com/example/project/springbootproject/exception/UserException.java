package com.example.project.springbootproject.exception;

import javax.persistence.EntityNotFoundException;

public class UserException extends EntityNotFoundException {
    public UserException(String message) {
        super(message);
    }
}
