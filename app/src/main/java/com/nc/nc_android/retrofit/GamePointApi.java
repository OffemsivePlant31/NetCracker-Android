package com.nc.nc_android.retrofit;

import com.nc.nc_android.pojo.GamePoint;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by danya on 19.10.2017.
 */

public interface GamePointApi {

    @GET("point/")
    Call<List<GamePoint>> findAll();

    @POST("point/")
    Call<List<GamePoint>> save(@Body GamePoint point);

    @DELETE("point/clear")
    Call<List<GamePoint>> clear();
}
