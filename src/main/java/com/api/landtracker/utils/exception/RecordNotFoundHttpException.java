package com.api.landtracker.utils.exception;

import org.springframework.http.HttpStatus;

public class RecordNotFoundHttpException extends HttpException {

    public RecordNotFoundHttpException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
