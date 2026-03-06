package com.app.pos.system.exception;

import com.app.pos.system.dto.response.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(exception = NotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleNotFoundException(NotFoundException e){
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                HttpStatus.NOT_FOUND,
                e.getError(),
                e.getMessage(),
                LocalDateTime.now()
        );

        return new ResponseEntity<>(exceptionResponse, HttpStatus.NOT_FOUND);
    }
}
