package com.be_planfortrips.exception;

import lombok.Getter;

@Getter
public class AppException extends RuntimeException{

    private ErrorType errorType;

    AppException(ErrorType errorType) {
            super(errorType.getMessage());
    }

}
