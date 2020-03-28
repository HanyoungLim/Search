package com.example.lib_api;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Field;
import java.util.concurrent.TimeUnit;

import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiCaller {

    private static ApiCaller instance;

    private Gson defaultGson = new GsonBuilder()
            .create();


    private ApiCaller() {}

    public static ApiCaller getInstance() {
        if (instance == null) {
            instance = new ApiCaller();
        }

        return instance;
    }

    public <T> T create (Class<T> service) {

        long timeout = 20000;

        Retrofit retrofit = new Retrofit.Builder()
                .client(new OkHttpClient.Builder()
                        .connectionPool(new ConnectionPool(5, 20, TimeUnit.SECONDS))
                        .connectTimeout(timeout, TimeUnit.MILLISECONDS)
                        .readTimeout(timeout, TimeUnit.MILLISECONDS)
                        .writeTimeout(timeout, TimeUnit.MILLISECONDS)
                        .retryOnConnectionFailure(true)
                        .build())
                .baseUrl(getBaseUrl(service))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(defaultGson))
                .build();

        return retrofit.create(service);
    }

    private <T> String getBaseUrl (Class<T> service) {
        Scheme scheme = Scheme.HTTPS;
        Host host = Host.TOSS_API;

        try {
            Field schemeField = service.getField("scheme");
            scheme = (Scheme) schemeField.get(service);
        } catch (Exception e) {
        }

        try {
            Field hostField = service.getField("host");
            host = (Host) hostField.get(service);
        } catch (Exception e) {
        }

        return String.format("%1$s://%2$s", scheme.name().toLowerCase(), host.getDomain());
    }
}
