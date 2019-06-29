package com.nc.nc_android.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nc.nc_android.MyApplication;
import com.nc.nc_android.dto.CreateGameInstanceRequest;
import com.nc.nc_android.dto.GameInstanceDto;
import com.nc.nc_android.dto.GameTaskDto;
import com.nc.nc_android.dto.TaskValidationRequest;
import com.nc.nc_android.dto.TaskValidationResponse;
import com.nc.nc_android.gson_converter.DateAdapter;
import com.nc.nc_android.retrofit.GamePointApi;
import com.nc.nc_android.retrofit.OrganizerApi;
import com.nc.nc_android.retrofit.RetrofitSingleton;

import java.io.IOException;
import java.util.Date;
import java.util.Properties;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by danya on 11.11.2017.
 */

public class OrganizerService {

    private OrganizerApi api;


    public OrganizerService(){
        api = RetrofitSingleton.getInstance(MyApplication.getContext()).getApi(OrganizerApi.class);
    }

    public Call<Void> start(CreateGameInstanceRequest request){
        return api.start(request);
    }



    public Call<GameTaskDto> getNextTask(long userid, long gameid){
        return api.getNextTask(userid, gameid);
    }

    public Call<TaskValidationResponse> validateTask(TaskValidationRequest request, long userid, long gameid){
        return api.validateTask(request, userid, gameid);
    }

    public Call<Void> leaveGame(long userid, long gameid){
        return api.leaveGame(userid, gameid);
    }

    public Call<Void> subscribe(long userid, long gameid){
        return api.subscribe(userid, gameid);
    }

    public Call<Long> gameId(long userid){
        return api.gameId(userid);
    }
}
