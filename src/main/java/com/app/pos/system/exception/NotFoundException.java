package com.app.pos.system.exception;

import lombok.Getter;

@Getter
public class NotFoundException extends RuntimeException{

    private final String error;

    public NotFoundException(String error, String message){
        super(message);
        this.error = error;
    }
}
