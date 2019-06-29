package com.nc.nc_android.retrofit;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nc.nc_android.MyApplication;
import com.nc.nc_android.gson_converter.DateAdapter;

import java.io.IOException;
import java.util.Date;
import java.util.Properties;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RetrofitSingleton {

    private static RetrofitSingleton instance;
    private Retrofit retrofit;

    public static RetrofitSingleton getInstance(Context context){
        if(instance == null){
            instance = new RetrofitSingleton(context);
        }
        return instance;
    }

    private RetrofitSingleton(Context context){
        Properties props = new Properties();
        String url = "";
        try {
            props.load(context.getAssets().open("app.properties"));
            url = props.getProperty("backend_url");
        } catch (IOException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Date.class, new DateAdapter())
                .create();

        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        clientBuilder.addInterceptor(loggingInterceptor);

        retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(clientBuilder.build())
                .build();
    }

    public <E> E getApi(Class<E> clazz){
        return retrofit.create(clazz);
    }

}
