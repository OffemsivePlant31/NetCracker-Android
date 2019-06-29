package com.nc.nc_android;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.nc.nc_android.dto.GameTemplateDto;

import java.io.IOException;
import java.util.Properties;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Utils {


    public static String getImageUrl(long imageId){
        String url = "";
        Properties props = new Properties();
        try {
            props.load(MyApplication.getContext().getAssets().open("app.properties"));
            url = props.getProperty("backend_url");
        } catch (IOException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
        return url + "demodata/photo/" + imageId;
    }

    public static Callback<Void> emptyCallback(){

        return new Callback<Void>() {
            @Override public void onResponse(Call<Void> call, Response<Void> response) {}
            @Override public void onFailure(Call<Void> call, Throwable t) {}
        };

    }




}
