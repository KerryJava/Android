package com.kerryapp.goldenage.common;

import okhttp3.MediaType;

/**
 * Created by mojet on 2017/10/2.
 */

public class Global {
    public static final String HOST = "http://mojicai.com:8085/";
    public static final String CONTROL_HOST = HOST+"control";

    public static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");
    public static final String DEBUG_TAG = "APP_DEBUG";


    public static String AUTH_TOKEN;
    public static String AUTH_TOKEN_KEY = "AUTH_TOKEN_KEY";

    public static final int LOGIN_SUCCESS = 1;

    public static final String NAME_KEY = "nameKey";
    public static final String NUM_KEY = "numKey";

    public static String getAuthToken() {
        return AUTH_TOKEN;
    }

    public static void setAuthToken(String authToken) {
        AUTH_TOKEN = authToken;
    }
}
