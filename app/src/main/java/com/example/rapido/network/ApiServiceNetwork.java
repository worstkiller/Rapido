package com.example.rapido.network;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.rapido.interfaces.WebServiceInterface;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by OFFICE on 4/13/2017.
 */

public class ApiServiceNetwork {

    public WebServiceInterface getNetworkService(@Nullable final String token, @NonNull final String url) throws IOException {

        OkHttpClient.Builder okHttpClientTwitter = new OkHttpClient.Builder();
        okHttpClientTwitter.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                Request.Builder builder;
                if (token != null) {
                    builder = request.newBuilder();
                    builder.addHeader("Authorization", "" + token);
                } else {
                    builder = request.newBuilder();
                }
                return chain.proceed(builder.build());
            }
        });

        Retrofit retrofit = new Retrofit.Builder().baseUrl(url)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClientTwitter.build()).build();
        return retrofit.create(WebServiceInterface.class);
    }

}
