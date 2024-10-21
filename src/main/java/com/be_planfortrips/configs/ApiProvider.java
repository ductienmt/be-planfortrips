package com.be_planfortrips.configs;

public class ApiProvider {
    public static final String[] PUBLIC_API = {
            "/api/v1/auth/**",
    };

    public static final String[] ADMIN_API = {
            "/api/v1/accounts/",
            "/api/v1/users/**",

    };

    public static final String[] USER_API = {
            "/api/v1/plans/prepare",
    };

    public static final String[] ADMIN_USER_ENTERPRISE_API = {

    };

    public static final String[] ENTERPRISE_API = {
            "/api/v1/account-enterprises/**"
    };
}
