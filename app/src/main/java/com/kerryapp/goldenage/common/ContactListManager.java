package com.kerryapp.goldenage.common;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.kerryapp.goldenage.R;
import com.kerryapp.goldenage.dummy.ContactData;

import java.io.IOException;
import java.util.Hashtable;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by mojet on 2017/10/2.
 */

public class ContactListManager {
    void loginDialog(final Activity context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getResources().getString(R.string.login));
        //    通过LayoutInflater来加载一个xml的布局文件作为一个View对象
        View view1 = LayoutInflater.from(context).inflate(R.layout.update_contactlist_dialog, null);

        builder.setView(view1);

        //确定操作
        builder.setPositiveButton(context.getResources().getString(R.string.btnOK), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                ContactListManager.Request(context);
            }
        });

        //取消操作
        builder.setNegativeButton(context.getResources().getString(R.string.btnCancel), null);

        builder.show();
    }

    public static void Request(final Context context) {
        //创建okHttpClient对象
        OkHttpClient mOkHttpClient = new OkHttpClient();
        //创建一个Request
        Gson json = new Gson();

        ContactData.getInstance().getContacts(context);
        Hashtable subTable = new Hashtable();
        List<ContactData.ContactItem> list = ContactData.getInstance().mWholeItems;
        subTable.put("list", list);

        Hashtable table = new Hashtable();
        table.put("method", "User.UpdateContactList");
        table.put("jsonrpc", "2.0");
        table.put("id", "54321");
        table.put("params", subTable);

        String jsonStr = json.toJson(table);

        RequestBody requestBody = RequestBody.create(Global.MEDIA_TYPE_JSON, jsonStr);

        final String url = Global.CONTROL_HOST;
        final Request request = new Request.Builder()
                .url(url)
                .addHeader("Content-type", "application/json")
                .addHeader("Authorization", Global.getAuthToken())
                .post(requestBody)
                .build();

        Call mcall = mOkHttpClient.newCall(request);
        mcall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(Global.DEBUG_TAG, "onFailure---" + e.getLocalizedMessage());
            }

            @Override
            public void onResponse(final Call call, Response response) throws IOException {

                final String bodyStr = response.body().string();

                if (null != response.cacheResponse()) {
                    String str = response.cacheResponse().toString();
                    Log.d(Global.DEBUG_TAG, "cache---" + str);
                } else {
                    Log.d(Global.DEBUG_TAG, "network---" + bodyStr);

                    ((Activity) context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, context.getResources().getString(R.string.BackupSuccess), Toast.LENGTH_SHORT).show();

                        }
                    });
                }

            }
        });
    }
}
