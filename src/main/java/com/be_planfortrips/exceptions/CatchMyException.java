package com.be_planfortrips.exceptions;


import com.be_planfortrips.dto.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
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

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ApiResponse<String>> handleNotFound(NoHandlerFoundException ex) {
        ApiResponse<String> response = ApiResponse.<String>builder()
                .code(HttpStatus.NOT_FOUND.value())
                .message("The requested URL was not found on the server.")
                .build();
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<?> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
        String errorMessage = "Phương thức HTTP không được hỗ trợ cho endpoint này. Vui lòng kiểm tra lại yêu cầu.";
        ApiResponse<String> response = ApiResponse.<String>builder()
                .code(HttpStatus.METHOD_NOT_ALLOWED.value())
                .message(errorMessage)
                .build();
        return new ResponseEntity<>(response, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiResponse<String>> handleMissingServletRequestParameterException(MissingServletRequestParameterException ex) {
        String errorMessage = String.format("Thiếu tham số yêu cầu '%s' kiểu '%s'.", ex.getParameterName(), ex.getParameterType());
        log.warn("Lỗi thiếu tham số yêu cầu: {}", errorMessage);

        ApiResponse<String> response = ApiResponse.<String>builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .message(errorMessage)
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}
