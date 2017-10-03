package com.kerryapp.goldenage.common;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by mojet on 2017/10/2.
 */

public class PreferenceHelper {
    private static String IDENTIFY = "com.kerryapp.goldenage";

    public static void putString(String key, String value, Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(IDENTIFY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getString(String key, Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(IDENTIFY, Context.MODE_PRIVATE);
        return sharedPref.getString(key, "");
    }
}
