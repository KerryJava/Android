package com.kerryapp.goldenage.common;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.constraint.solver.Goal;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.kerryapp.goldenage.MainActivity;
import com.kerryapp.goldenage.R;
import com.kerryapp.goldenage.model.LoginParam;
import com.kerryapp.goldenage.model.LoginRes;

import java.io.IOException;
import java.util.Hashtable;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.kerryapp.goldenage.common.Global.MEDIA_TYPE_JSON;

/**
 * Created by mojet on 2017/10/2.
 */

public class LoginAction {

    void loginDialog(final Activity context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getResources().getString(R.string.login));
        //    通过LayoutInflater来加载一个xml的布局文件作为一个View对象
        View view1 = LayoutInflater.from(context).inflate(R.layout.login_diaglog, null);

        builder.setView(view1);

        final EditText edtName = (EditText) view1.findViewById(R.id.login_passwd);
        final EditText edtPhone = (EditText) view1.findViewById(R.id.edtPhone);

        //确定操作
        builder.setPositiveButton(context.getResources().getString(R.string.btnOK), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String strPasswd = edtName.getText().toString().trim();
                String strPhone = edtPhone.getText().toString().trim();
                if (strPhone.isEmpty()) {
                    Toast.makeText(context, "电话号码为空!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (strPasswd.isEmpty()) {
                    Toast.makeText(context, "请重新输入密码!", Toast.LENGTH_SHORT).show();
                    return;
                }
                LoginAction.Request(context, strPhone, strPasswd);
            }
        });

        //取消操作
        builder.setNegativeButton(context.getResources().getString(R.string.btnCancel), null);

        builder.show();
    }

    public static void Request(final Context context, String phone, String passwd) {
        //创建okHttpClient对象
        OkHttpClient mOkHttpClient = new OkHttpClient();
        //创建一个Request

//        RequestBody formBody = new FormBody.Builder()
//                .add("method", "Other.GetWithSize")
//                .add("name", "bug")
//                .build();
        Gson json = new Gson();
        long longPhone = Long.parseLong(phone);
        LoginParam param = new LoginParam(longPhone, passwd);
        param.log.channel = Phone.CHANNEL;
        param.log.device = Phone.GetInfo(context);
        param.log.screen = Phone.GetHeightAndWidth(context);

        Hashtable table = new Hashtable();
        table.put("method", "Base.Login");
        table.put("jsonrpc", "2.0");
        table.put("id", "54321");
        table.put("params", param);

        String jsonStr = json.toJson(table);

        RequestBody requestBody = RequestBody.create(Global.MEDIA_TYPE_JSON, jsonStr);

        final String url = Global.HOST;
        final Request request = new Request.Builder()
                .url(url)
                .addHeader("Content-type", "application/json")
                .post(requestBody)
                .build();

        Call mcall = mOkHttpClient.newCall(request);
        mcall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(Global.DEBUG_TAG, "onFailure---" + e.getLocalizedMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                final String bodyStr = response.body().string();

                if (null != response.cacheResponse()) {
                    String str = response.cacheResponse().toString();
                    Log.d(Global.DEBUG_TAG, "cache---" + str);
                } else {
                    Log.d(Global.DEBUG_TAG, "network---" + bodyStr);
//                    final String cacheStr = response.cacheResponse().body().toString();
                    Log.d(Global.DEBUG_TAG, "bodyStr---" + bodyStr + " net work ");
//                    Log.d(Global.DEBUG_TAG, "cacheStr---" + cacheStr);

                    ((Activity) context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            final String str = "body" + bodyStr;
                            Gson gson = new Gson();
                            LoginRes res = gson.fromJson(bodyStr, LoginRes.class);
                            Log.d(Global.DEBUG_TAG, res.result.userdata.token);
                            if (res.result.userdata.token != "") {
                                Toast.makeText(context, context.getResources().getString(R.string.LoginSuccess), Toast.LENGTH_SHORT).show();
                                ((MainActivity)context).LoginProcess(Global.LOGIN_SUCCESS);
                            }
                        }
                    });
                }
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(getApplicationContext(), "请求成功", Toast.LENGTH_SHORT).show();
//                    }
//                });
            }
        });
    }
}
