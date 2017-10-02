package com.kerryapp.goldenage.common;

import okhttp3.MediaType;

/**
 * Created by mojet on 2017/10/2.
 */

public class Global {
    public static final String HOST = "http://mojicai.com:8085/";
    public static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");
    public static final String DEBUG_TAG = Global.DEBUG_TAG;
    public static String AUTH_TOKEN;
    public static final int LOGIN_SUCCESS = 1;

}
