package com.app.pos.system.exception;

import lombok.Getter;

@Getter
public class AlreadyExistsException extends RuntimeException {

    private final String error;

    public AlreadyExistsException(String error, String message) {
        super(message);
        this.error = error;
    }
}
