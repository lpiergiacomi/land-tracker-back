package com.api.landtracker.utils.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class HttpException extends RuntimeException {
    private HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

    protected HttpException(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    protected HttpException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    protected HttpException(Exception exception) {
        super(exception);
    }

    protected HttpException(Exception exception, HttpStatus httpStatus) {
        this(exception);
        this.httpStatus = httpStatus;
    }
}