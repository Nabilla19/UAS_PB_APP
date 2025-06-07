package com.example.proyektorapp.api;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.proyektorapp.MyApplication;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Request original = chain.request();

                            // Ambil token dari SharedPreferences
                            SharedPreferences prefs = MyApplication.getInstance()
                                    .getSharedPreferences("user_pref", Context.MODE_PRIVATE);
                            String token = prefs.getString("TOKEN", null);

                            Request.Builder requestBuilder = original.newBuilder();
                            if (token != null) {
                                requestBuilder.addHeader("Authorization", "Bearer " + token);
                            }

                            Request request = requestBuilder.build();
                            return chain.proceed(request);
                        }
                    })
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl("http://10.2.30.7:3001/") // ganti sesuai URL API kamu
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit;
    }
}
