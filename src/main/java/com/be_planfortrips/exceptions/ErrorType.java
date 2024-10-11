package com.be_planfortrips.exceptions;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)

public enum ErrorType {

    // Định nghĩa lỗi chung
    notFound(404, "Đối tượng không tồn tại", HttpStatus.NOT_FOUND),

    // Riêng từng case quan trọng (Yêu cầu rõ case)
    usernameExisted(100, "Username đã tồn tại", HttpStatus.BAD_REQUEST);

    int code;
    String message;
    HttpStatus httpStatus;

    ErrorType(Integer code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
