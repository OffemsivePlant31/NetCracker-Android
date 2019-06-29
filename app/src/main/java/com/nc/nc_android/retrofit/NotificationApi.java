package com.nc.nc_android.retrofit;

import com.nc.nc_android.dto.NotificationDto;


import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface NotificationApi {

    @GET("notification/")
    Call<List<NotificationDto>> getAll(@Query("userid") long userid);


}
