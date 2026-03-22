package com.app.pos.system.exception;

import lombok.Getter;

@Getter
public class ForbiddenException extends RuntimeException {

    private final String error;

    public ForbiddenException(String error, String message) {
        super(message);
        this.error = error;
    }
}
