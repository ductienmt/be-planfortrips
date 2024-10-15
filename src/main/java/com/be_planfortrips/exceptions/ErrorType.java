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
    usernameExisted(100, "Username đã tồn tại", HttpStatus.BAD_REQUEST),
    routeExisted(409,"Tuyến này đã tồn tại, vui lòng thêm tuyến đi khác!",HttpStatus.BAD_REQUEST),
    routeCodeExisted(409,"Mã nhà ga này đã tồn tại",HttpStatus.BAD_REQUEST),
    couponIsExpired(400,"Voucher đã hết hạn hoặc không còn khả dụng",HttpStatus.BAD_REQUEST),
    percentIsUnprocessed(422,"Phần trăm không hợp lệ",HttpStatus.UNPROCESSABLE_ENTITY);
    int code;
    String message;
    HttpStatus httpStatus;

    ErrorType(Integer code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
