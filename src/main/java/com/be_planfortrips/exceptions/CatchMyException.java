package com.be_planfortrips.exceptions;


import com.be_planfortrips.dto.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@ControllerAdvice
public class CatchMyException {

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ApiResponse<?>> catchMyException(AppException exception) {
        ErrorType errorType = exception.getErrorType();
        ApiResponse<?> response = ApiResponse.builder()
                .code(errorType.getCode())
                .message(errorType.getMessage())
                .data(exception.getDataError())
                .build();

        return ResponseEntity.status(errorType.getHttpStatus()).body(response);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<ErrorType>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        ErrorType errorType = ErrorType.notValidDateFormat;
        ApiResponse<ErrorType> response = ApiResponse.<ErrorType>builder()
                .code(errorType.getCode())
                .message("Định dạng thời gian không hợp lệ: " + ex.getValue())
                .build();
        return ResponseEntity.status(errorType.getHttpStatus()).body(response);
    }
}
