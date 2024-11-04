package com.be_planfortrips.exceptions;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum ErrorType {

    // Bắt mọi lỗi khác và trả về Internal Server Error
    internalServerError(500, "Lỗi hệ thống", HttpStatus.INTERNAL_SERVER_ERROR),

    // Định nghĩa lỗi ngày giờ "yyyy-MM-dd HH:mm:ss"
    notValidDateFormat(101, "Định dạng ngày giờ không hợp lệ", HttpStatus.BAD_REQUEST),

    notMatchPassword(800, "Mật khẩu không khớp", HttpStatus.BAD_REQUEST),

    notVerified(400, "OTP không hợp lệ hoặc đã hết hạn", HttpStatus.BAD_REQUEST),

    // Định nghĩa lỗi chung
    notFound(404, "Đối tượng không tồn tại", HttpStatus.NOT_FOUND),
    // Riêng từng case quan trọng (Yêu cầu rõ case)
    usernameExisted(600, "Username đã tồn tại", HttpStatus.BAD_REQUEST),
    routeExisted(601, "Tuyến này đã tồn tại, vui lòng thêm tuyến đi khác!", HttpStatus.BAD_REQUEST),
    routeCodeExisted(602, "Mã nhà ga này đã tồn tại", HttpStatus.BAD_REQUEST),
    couponIsExpired(603, "Voucher đã hết hạn hoặc không còn khả dụng", HttpStatus.BAD_REQUEST),
    percentIsUnprocessed(604, "Phần trăm không hợp lệ", HttpStatus.UNPROCESSABLE_ENTITY),
    // Case Not Found
    // (Trong trường hợp tạo đối tượng từ đối tượng khác, cần biết rõ đối tượng nào
    // không tồn tại)
    roomIdNotFound(700, "RoomId không tồn tại", HttpStatus.NOT_FOUND),
    userIdNotFound(701, "User không tồn tại", HttpStatus.NOT_FOUND),
    paymentIdNotFound(702, "PaymentId không tồn tại", HttpStatus.NOT_FOUND),
    routeIdNotFound(703, "RouteId ", HttpStatus.NOT_FOUND),
    vehicleCodeNotFound(704, "VehicleCode không tồn tại", HttpStatus.NOT_FOUND),
    typeEnterpriseIdNotFound(705, "typeEnterpriseId không tồn tại", HttpStatus.NOT_FOUND),
    roleNameNotFound(706, "RoleName không tồn tại", HttpStatus.NOT_FOUND),
    ratingInvalid(707,"Đánh giá phaải từ 0 - 5 sao",HttpStatus.BAD_REQUEST),
    ;

    private static String getString() {
        return "không tồn tại";
    }

    int code;
    String message;
    HttpStatus httpStatus;

    ErrorType(Integer code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
