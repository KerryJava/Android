package com.example.lipei.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.example.lipei.myapplication.entity.RequestEntity;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by Administrator on 2017/8/4 0004.
 */
public class HttpAitivity extends AppCompatActivity {

    private ImageView ivSplash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ivSplash = (ImageView) findViewById(R.id.iv_splash);
        initDate();
    }

    private void initDate() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://app.api.gupiaoxianji.com/v5.4.8/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RequestEntity entity = new RequestEntity();
        entity.setMethod("Other.StartPage");
        IUserBiz userBiz = retrofit.create(IUserBiz.class);
        Call<ResponseBody> call = userBiz.getHttp(entity);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(response.body().string());
                    if (jsonObject.containsKey("result")) {
                        final com.alibaba.fastjson.JSONObject json = jsonObject.getJSONObject("result");
                        Glide.with(HttpAitivity.this).load(json.getString("imageurl")).into(ivSplash);
                        ivSplash.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent();
                                intent.setAction("android.intent.action.VIEW");
                                intent.setData(Uri.parse(json.getString("linkurl")));
                                startActivity(intent);

                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });


    }

    public interface IUserBiz {
        @Headers({"Content-Type: application/json","Accept: application/json"})
        @POST("main/")
        Call<ResponseBody> getHttp(@Body RequestEntity requestEntity);
    }


}
