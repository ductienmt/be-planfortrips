package com.be_planfortrips.configs;

public class API_Provider {
    public static final String[] PUBLIC_API = {
            "/api/v1/auth/**",
            "/api/v1/plans/prepare",
            "/api/v1/banners/allUser",
            "/api/v1/schedules/getStationByScheduleId",
            "/api/v1/schedules/getSchedules",
            "/api/v1/coupons/**",
            "/api/v1/tickets/**",
            "/api/v1/booking-hotels/**",
            "/api/v1/tickets/getByUserId",
            "/api/v1/users/getDetail",
            "/api/v1/feedbacks/all",
            "/api/v1/cities/all",
            "/api/v1/areas/all",
            "/api/v1/users/findByUsername",
            "/api/v1/check-in/all",
            "/api/v1/check-in/getByCityId",
            "/api/v1/check-in/upload-image",
            "/api/v1/vehicles/getById/**",
            "/api/v1/hotels/getById/**",
            "/api/v1/hotels/all",
            "/api/v1/check-in/all",
            "/api/v1/check-in/getImages",
            "/api/v1/schedules/getById/**",
            "/api/v1/type-enterprise-details/all",
            "/api/v1/payments/vnpay/return",
            "/api/v1/account-enterprises/create",
            "/api/v1/rooms/getRoomAvailable",
            "/api/v1/areas/all",
            "/api/v1/banners/all",
            "/api/v1/routes/getCityByRouteId",
            "/api/v1/hotel-amenities/get-by-hotel-id",
            "/api/v1/room-amenities/get-by-room-id",
            "/api/v1/tags/all",
            "/api/v1/tours/all",
            "/api/v1/tours/**",
            "/api/v1/hotels/**",
            "/api/v1/areas/**",
            "/api/v1/cities/**",
            "/api/v1/account-enterprises/validate-username",
            "/api/v1/account-enterprises/validate-email",
            "/api/v1/account-enterprises/validate-phone",
            "/api/v1/payments/vnpay/return-plan",
            "/api/v1/tours/**",
            "/api/v1/statistical/**",
            "/api/v1/rooms/getRoomByHotelId",
            "/api/v1/schedules/getSamePrice",
    };

    public static final String[] ADMIN_API = {
            "/api/v1/account-enterprises/change-status",
            "/api/v1/account-enterprises/all",
            "/api/v1/account-enterprises/delete/**",
            "/api/v1/account-enterprises/getById/**",
            "/api/v1/areas/create",
            "/api/v1/areas/update",
            "/api/v1/areas/delete",
            "/api/v1/areas/change-status",
            "/api/v1/banners/create",
            "/api/v1/banners/update",
            "/api/v1/banners/delete",
            "/api/v1/banners/setStage",
            "/api/v1/banners/upload",
            "/api/v1/check-in/create",
            "/api/v1/check-in/update",
            "/api/v1/check-in/delete",
            "/api/v1/check-in/upload-image",
            "/api/v1/feedbacks/all",
            "/api/v1/type-enterprises/all",
            "/api/v1/type-enterprises/accept",
            "/api/v1/type-enterprises/create",
            "/api/v1/type-enterprises/update/**",
            "/api/v1/type-enterprises/delete/**",
            "/api/v1/type-enterprises/getById/**",
            "/api/v1/type-enterprise-details/create",
            "/api/v1/type-enterprise-details/update/**",
            "/api/v1/type-enterprise-details/delete/**",
            "/api/v1/users/all",
            "/api/v1/users/delete",
            "/api/v1/users/create",
            "/api/v1/admins/findByUserName",
            "/api/v1/routes/create",
            "/api/v1/tags/create",
            "/api/v1/hotels/create",
            "/api/v1/tours/create",
            "/api/v1/tours/update/**",
            "/api/v1/tours/delete/**",
            "/api/v1/tours/findById/**",
            "/api/v1/tours/uploads/**",
            "/api/v1/tours/deleteImages/**",
    };

    public static final String[] USER_API = {
            "/api/v1/plans/all",
            "/api/v1/plans/detail",
            "/api/v1/booking-hotels/create",
            "/api/v1/feedbacks/create",
            "/api/v1/feedbacks/update/**",
            "/api/v1/feedbacks/delete/**",
            "/api/v1/tickets/create",
            "/api/v1/tickets/getByUserId/**",
            "/api/v1/users/update",
            "/api/v1/users/changePassword",
            "/api/v1/users/upload",
            "/api/v1/users/verify-password",
            "/api/v1/users/getImage",
            "/api/v1/users/detail",
            "/api/v1/payments/vnpay/create-payment",
            "/api/v1/users/verify-email",
            "/api/v1/email/send",
            "/api/v1/email/validate",
            "/api/v1/coupons/all",
            "/api/v1/plans/save",
            "/api/v1/vietqr/**",
            "/api/v1/payments/vnpay/create-payment-plan",



    };

    public static final String[] USER_ADMIN_API = {
            "/api/v1/car-companies/all/**",
            "/api/v1/hotels/all",
            "/api/v1/users/getDetail",
    };

    public static final String[] USER_ENTERPRISE_API = {
            "/api/v1/booking-hotels/getById/**",
            "/api/v1/booking-hotels/user/**",
            "/api/v1/booking-hotels/update/**",
            "/api/v1/booking-hotel-details/getById/**",
            "/api/v1/rooms/getById/**",
            "/api/v1/tickets/update/**",
            "/api/v1/tickets/getById/**",
            "/api/v1/schedules/getById/**",
    };

    public static final String[] ADMIN_USER_ENTERPRISE_API = {
            "/api/v1/areas/all",
            "/api/v1/plan/check-in/detail",
            "/api/v1/plan/check-in/search",
            "/api/v1/car-companies/getById/**",
            "/api/v1/hotels/getById/**",
            "/api/v1/coupons/getByCode",
    };

    public static final String[] ADMIN_ENTERPRISE_API = {
            "/api/v1/car-companies/update/**",
            "/api/v1/coupons/create",
            "/api/v1/coupons/update/**",
            "/api/v1/coupons/delete/**",
            "/api/v1/coupons/getById/**",
            "/api/v1/feedbacks/getById/**",
            "/api/v1/routes/all",
            "/api/v1/routes/getById/**",
            "/api/v1/type-enterprise-details/getById/**",
            "/api/v1/account-enterprises/detail",
            "/api/v1/account-enterprises/update/**",
            "/api/v1/coupons/getByEnterpriseId",
            "/api/v1/users/findByCarCompany/**",
            "/api/v1/users/findByHotel/**",
    };

    public static final String[] ENTERPRISE_API = {
            "/api/v1/booking-hotels/all",
            "/api/v1/booking-hotels/delete/**",
            "/api/v1/booking-hotel-details/all",
            "/api/v1/car-companies/uploads/**",
            "/api/v1/car-companies/deleteImages/**",
            "/api/v1/car-companies/create",
            "/api/v1/car-companies/delete",
            "/api/v1/hotels/create",
            "/api/v1/hotels/update/**",
            "/api/v1/hotels/uploads/**",
            "/api/v1/hotels/deleteImages/**",
            "/api/v1/hotels/detail",
            "/api/v1/rooms/create",
            "/api/v1/rooms/all",
            "/api/v1/rooms/update/**",
            "/api/v1/rooms/delete/**",
            "/api/v1/routes/create",
            "/api/v1/routes/update/**",
            "/api/v1/routes/delete/**",
            "/api/v1/schedules/create",
            "/api/v1/schedules/update/**",
            "/api/v1/schedules/delete/**",
            "/api/v1/seats/create",
            "/api/v1/seats/update/**",
            "/api/v1/seats/delete/**",
            "/api/v1/seats/getById/**",
            "/api/v1/seats/all",
            "/api/v1/tickets/delete/**",
            "/api/v1/tickets/all",
            "/api/v1/tickets/getByScheduleId/**",
            "/api/v1/vehicles/all",
            "/api/v1/vehicles/create",
            "/api/v1/vehicles/update/**",
            "/api/v1/vehicles/delete/**",
            "/api/v1/hotel-amenities/create",
            "/api/v1/hotel-amenities/update",
            "/api/v1/hotel-amenities/delete",
            "/api/v1/hotel-amenities/change-status",
            "/api/v1/hotel-amenities/upload-icon",
            "/api/v1/hotel-amenities/delete-icon",
            "/api/v1/room-amenities/create",
            "/api/v1/room-amenities/update",
            "/api/v1/room-amenities/delete",
            "/api/v1/room-amenities/change-status",
            "/api/v1/room-amenities/upload-icon",
            "/api/v1/room-amenities/delete-icon",
    };
}
