package com.be_planfortrips.configs;

public class ApiProvider {
    public static final String[] PUBLIC_API = {
            "/api/v1/**",
            "/api/v1/account-enterprises/all",
            "/api/v1/account-enterprises/getById",
            "/api/v1/account-enterprises/create",
            "/api/v1/account-enterprises/update/**",
            "/api/v1/account-enterprises/delete/**",
    };

    public static final String[] ADMIN_API = {
    };

    public static final String[] USER_API = {
    };

    public static final String[] ADMIN_USER_ENTERPRISE_API = {

    };

    public static final String[] ENTERPRISE_API = {
    };
}
