package com.kerryapp.goldenage.common;

import android.content.Context;
import android.graphics.Point;
import android.telephony.TelephonyManager;
import android.view.Display;
import android.view.WindowManager;

import static android.content.Context.TELEPHONY_SERVICE;

/**
 * Created by mojet on 2017/10/2.
 */

public class Phone {
    public static int CHANNEL = 1;

    public static String GetInfo(Context context) {
        TelephonyManager mTm = (TelephonyManager)context.getSystemService(TELEPHONY_SERVICE);
        String imei = mTm.getDeviceId();
        String imsi = mTm.getSubscriberId();
        String mtype = android.os.Build.MODEL; // 手机型号
        String mtyb= android.os.Build.BRAND;//手机品牌
        String numer = mTm.getLine1Number(); // 手机号码，有的可得，有的不可得
        return "IMEI："+imei+"\nIESI："+imsi+"\nMODEL："+mtype+"\nBRAND："+mtyb+"\nNUM"+numer;
    }

    public static int[] getMetrics(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        int width = point.x;
        int height = point.y;
        int[] metrics = {width, height};
        return metrics;
    }
    public static String GetHeightAndWidth(Context context){
        int[]metrics = Phone.getMetrics(context);
        String str = metrics[0]+"*"+metrics[1];
        return str;
    }
}
