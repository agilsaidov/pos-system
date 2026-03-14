package com.app.pos.system.exception;

import lombok.Getter;

@Getter
public class BadRequestException extends RuntimeException{
    private final String error;

    public BadRequestException(String error, String message){
        super(message);
        this.error=error;
    }
}
