package com.be_planfortrips.exceptions;


import com.be_planfortrips.dto.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@Slf4j
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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        // Duyệt qua các lỗi và lưu tên trường cùng thông báo lỗi vào map `errors`
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }

        // Tạo phản hồi ApiResponse với thông tin lỗi chi tiết
        ApiResponse<Map<String, String>> response = ApiResponse.<Map<String, String>>builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .message("Dữ liệu không hợp lệ")
                .data(errors)
                .build();

        // Trả về phản hồi với mã trạng thái 400 BAD_REQUEST và thông tin lỗi
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleException(Exception ex) {
        log.error("Lỗi không xác định: ", ex);
        ApiResponse<?> response = ApiResponse.builder()
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message("Đã có lỗi xảy ra")
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
