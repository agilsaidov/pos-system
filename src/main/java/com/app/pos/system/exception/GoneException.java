package com.app.pos.system.exception;

import lombok.Getter;

@Getter
public class GoneException extends RuntimeException{

    private final String error;

    public GoneException(String error, String message){
        super(message);
        this.error = error;
    }
}
