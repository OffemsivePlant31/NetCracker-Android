package com.nc.nc_android.service;

import android.app.Application;
import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nc.nc_android.MyApplication;
import com.nc.nc_android.gson_converter.DateAdapter;
import com.nc.nc_android.pojo.GamePoint;
import com.nc.nc_android.retrofit.GamePointApi;
import com.nc.nc_android.retrofit.RetrofitSingleton;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by danya on 19.10.2017.
 */

public class GamePointService {

    private GamePointApi api;


    public GamePointService(){
        api = RetrofitSingleton.getInstance(MyApplication.getContext()).getApi(GamePointApi.class);
    }

    public Call<List<GamePoint>> findAllAsync(){
        return api.findAll();
    }

    public List<GamePoint> findAll() throws IOException {
        return api.findAll().execute().body();
    }

    public Call<List<GamePoint>> saveAsync(GamePoint gp){
        return api.save(gp);
    }

    public Call<List<GamePoint>> clear(){
        return api.clear();
    }


}
