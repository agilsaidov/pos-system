package com.app.pos.system.exception;

import lombok.Getter;

@Getter
public class DuplicateException extends RuntimeException{

    private final String error;

    public DuplicateException(String error, String message){
        super(message);
        this.error = error;
    }
}
