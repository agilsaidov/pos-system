package com.app.pos.system.exception;

public class JwtFilterException extends RuntimeException {
    public JwtFilterException(String message) {
        super(message);
    }
}
