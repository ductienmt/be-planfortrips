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
    statusInvalid(801, "Trạng thái không hợp lệ", HttpStatus.BAD_REQUEST),
    emailNotValid(802, "Email không hợp lệ", HttpStatus.BAD_REQUEST),
    phoneNotValid(803, "Số điện thoại không hợp lệ", HttpStatus.BAD_REQUEST),
    typeEnterpriseDetailNotFound(804, "TypeEnterpriseDetail không tồn tại", HttpStatus.NOT_FOUND),
    inputFieldInvalid(805, "Trường nhập không hợp lệ", HttpStatus.BAD_REQUEST),
    exitedPlans(806, "Bạn đã có kế hoạch trong khoảng thời gian này, không thể tạo thêm.", HttpStatus.BAD_REQUEST),


    notVerified(400, "OTP không hợp lệ hoặc đã hết hạn", HttpStatus.BAD_REQUEST),

    // Định nghĩa lỗi chung
    notFound(404, "Đối tượng không tồn tại", HttpStatus.NOT_FOUND),
    // Riêng từng case quan trọng (Yêu cầu rõ case)
    usernameExisted(600, "Username đã tồn tại", HttpStatus.BAD_REQUEST),
    routeExisted(601, "Tuyến này đã tồn tại, vui lòng thêm tuyến đi khác!", HttpStatus.BAD_REQUEST),
    routeCodeExisted(602, "Mã nhà ga này đã tồn tại", HttpStatus.BAD_REQUEST),
    couponIsExpired(603, "Voucher đã hết hạn hoặc không còn khả dụng", HttpStatus.BAD_REQUEST),
    percentIsUnprocessed(604, "Phần trăm không hợp lệ", HttpStatus.UNPROCESSABLE_ENTITY),
    UploadFailed(605,"Upload ảnh thất bại",HttpStatus.INTERNAL_SERVER_ERROR),
    DeleteImageFailed(606,"Xóa ảnh thất bại",HttpStatus.BAD_REQUEST),
    nameTypeExisted(607, "NameType Existed", HttpStatus.BAD_REQUEST),
    hasEtp(608,"TypeEnterprise Contain AccountEnterprise", HttpStatus.BAD_REQUEST),
    emailExisted(609,"Email đã tồn tại",HttpStatus.BAD_REQUEST),
    phoneExisted(610,"Số điện thoại đã tồn tại",HttpStatus.BAD_REQUEST),
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
    AccountEnterpriseIdNotFound(708,"AccountEnterpriseId không tồn tại",HttpStatus.NOT_FOUND),
    VehicleCodeNotFound(709, "VehicleCode không tồn tại",HttpStatus.NOT_FOUND),
    RouteIdNotFound(710, "RouteId không tồn tại",HttpStatus.NOT_FOUND),
    HotelIdNotFound(711,"HotelId không tồn tại",HttpStatus.NOT_FOUND),
    CityIdNotFound(712, "CityId không tồn tại", HttpStatus.NOT_FOUND),
    TagNameIsExist(713, "Tag đã tồn tại", HttpStatus.BAD_REQUEST),
    HotelHaveNotRoomAvailable(714,"Khách sạn không có phòng có sẵn",HttpStatus.BAD_REQUEST),
    CarCompanyHaveNotScheduleAvailable(715,"Nhà xe không có chuyến có sẵn",HttpStatus.BAD_REQUEST),
    CarCompanyHaveNotSeatAvailable(716,"Nhà xe không có ghes có sẵn",HttpStatus.BAD_REQUEST),
    TicketOrBookingHotelIsRequired(717,"Vé xe hoặc vé xe cần phải có",HttpStatus.BAD_REQUEST),
    TourIdNotFound(718,"TourId Not found",HttpStatus.NOT_FOUND),
    ScheduleIdNotFound(719, "ScheduleId Not Found",HttpStatus.NOT_FOUND),
    SeatIdNotFound(720, "SeatId Not Found",HttpStatus.NOT_FOUND),
    PhoneNumberNotExist(721, "PhoneNumber Not Found", HttpStatus.NOT_FOUND),
    EmailNotExist(722, "Email Not Found", HttpStatus.NOT_FOUND)
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
