package com.be_planfortrips.exceptions;

import lombok.Getter;

@Getter
public class AppException extends RuntimeException{

    private ErrorType errorType;

    AppException(ErrorType errorType) {
            super(errorType.getMessage());
    }

}
