package com.nc.nc_android.retrofit;


import com.nc.nc_android.dto.PersonDto;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface AuthorizationApi {

    @GET("auth/")
    Call<PersonDto> auth(@Query("email") String email, @Query("password") String password);

    @GET("auth/{id}")
    Call<PersonDto> findById(@Path("id") long id);
}
