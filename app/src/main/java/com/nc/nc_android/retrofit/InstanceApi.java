package com.nc.nc_android.retrofit;


import com.nc.nc_android.dto.GameInstanceDto;
import com.nc.nc_android.dto.TeamResultDto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface InstanceApi {

    @GET("instance/{gameid}")
    Call<GameInstanceDto> gameInfo(@Path("gameid") long gameid);

    @GET("instance/{gameid}/team")
    Call<List<TeamResultDto>> teamResults(@Path("gameid") long gameid);

    @GET("instance/")
    Call<List<GameInstanceDto>> gameList(@Query("personid") Long personId);

    @GET("instance/invite/")
    Call<List<GameInstanceDto>> privateGameList(@Query("personid") long personId);

}
