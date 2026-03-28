package com.app.pos.system.exception;

import lombok.Getter;

@Getter
public class MailException extends RuntimeException {

    private String error;

    public MailException(String error, String message) {
        super(message);
        this.error=error;
    }
}
