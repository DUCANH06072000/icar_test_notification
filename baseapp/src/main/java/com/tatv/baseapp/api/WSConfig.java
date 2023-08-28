package com.tatv.baseapp.api;

/**
 * Created by tatv on 05/12/2022.
 */
public class WSConfig {
    public static final String BASE_LOG = "https://logger.icar.vn";

    public class Api {
        public static final String LOGIN = "/auth/login";
        public static final String LOGOUT = "/auth/logout";
        public static final String REFRESH_TOKEN = "/auth/refresh-token";
        public static final String LOGOUT_DEVICE_THEN_LOGIN = "/auth/logout-app-id-then-login";

        public static final String REGISTER_SEND_OTP = "/auth/register/send-otp";
        public static final String REGISTER_VERITY_OTP = "/auth/register/verify-otp";
        public static final String REGISTER_CREATE_CUSTOMER = "/auth/register/create-customer";

        public static final String FORGOT_PASSWORD_SEND_OTP = "/auth/lost-password/send-otp";
        public static final String FORGOT_PASSWORD_VERITY_OTP = "/auth/lost-password/verify-otp";
        public static final String FORGOT_PASSWORD_CREATE_NEW_PASSWORD = "/auth/lost-password/create-new-password";
        public static final String CHANGE_PASSWORD = "/auth/change-password";
    }
}
