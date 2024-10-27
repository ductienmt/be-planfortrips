package com.be_planfortrips.configs;

public class ApiProvider {
    public static final String[] PUBLIC_API = {
            "/api/v1/auth/**",
            "/api/v1/plan/prepare",
            "/api/v1/plan/banners/allUser",



    };

    public static final String[] ADMIN_API = {
            "/api/v1/account-enterprises/**",
            "/api/v1/areas/create",
            "/api/v1/areas/update",
            "/api/v1/areas/delete",
            "/api/v1/banners/all",
            "/api/v1/banners/create",
            "/api/v1/banners/update",
            "/api/v1/banners/delete",
            "/api/v1/banners/setStage",
            "/api/v1/banners/upload",
            "/api/v1/car-companies/delete",
            "/api/v1/check-in/create",
            "/api/v1/check-in/update",
            "/api/v1/check-in/delete",
            "/api/v1/check-in/upload-image",
            "/api/v1/coupons/all",
            "/api/v1/feedbacks/all",


    };

    public static final String[] USER_API = {
            "/api/v1/plan/all",
            "/api/v1/booking-hotels/create",
            "/api/v1/feedbacks/create",
            "/api/v1/feedbacks/update/**",
            "/api/v1/feedbacks/delete/**",
    };

    public static final String[] USER_ADMIN_API = {
            "/api/v1/car-companies/all",
            "/api/v1/hotels/all",

    };

    public static final String[] USER_ENTERPRISE_API = {
            "/api/v1/booking-hotels/getById/**",
            "/api/v1/booking-hotels/user/**",
            "/api/v1/booking-hotels/update/**",
            "/api/v1/booking-hotel-details/getById/**",
    };

    public static final String[] ADMIN_USER_ENTERPRISE_API = {
            "/api/v1/areas/all",
            "/api/v1/plan/check-in/all",
            "/api/v1/plan/check-in/detail",
            "/api/v1/plan/check-in/search",
            "/api/v1/car-companies/getById/**",
            "/api/v1/hotels/getById/**",
    };

    public static final String[] ADMIN_ENTERPRISE_API = {
            "/api/v1/car-companies/update/**",
            "/api/v1/coupons/create",
            "/api/v1/coupons/update/**",
            "/api/v1/coupons/delete/**",
            "/api/v1/coupons/getById/**",
            "/api/v1/feedbacks/getById/**",
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

    };
}
