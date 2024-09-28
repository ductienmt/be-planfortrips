//package com.be_planfortrips.exceptions;
//
//
//import com.be_planfortrips.dto.response.ApiResponse;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//
//@RestControllerAdvice
//public class CatchMyException {
//
//    @ExceptionHandler(AppException.class)
//    public ResponseEntity<ApiResponse<ErrorType>> catchMyException(AppException exception) {
//            ErrorType errorType = exception.getErrorType();
//            ApiResponse<ErrorType> response = ApiResponse.<ErrorType>builder()
//                    .code(errorType.getCode())
//                    .message(errorType.getMessage())
//                    .build();
//
//        return ResponseEntity.status(errorType.getHttpStatus()).body(response);
//    }
//}
