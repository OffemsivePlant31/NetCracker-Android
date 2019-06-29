package com.nc.nc_android.retrofit;

import com.nc.nc_android.dto.AsyncValidationDto;
import com.nc.nc_android.dto.CreateGameInstanceRequest;
import com.nc.nc_android.dto.GameInstanceDto;
import com.nc.nc_android.dto.GamePointDto;
import com.nc.nc_android.dto.GameTaskDto;
import com.nc.nc_android.dto.TaskValidationRequest;
import com.nc.nc_android.dto.TaskValidationResponse;
import com.nc.nc_android.dto.TeamResultDto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface OrganizerApi {


    @POST("organizer/start")
    Call<Void> start(@Body CreateGameInstanceRequest createGameInstanceRequest);

    @GET("organizer/game/id")
    Call<Long> gameId(@Query("userid") long userid);



    @GET("organizer/task/next")
    Call<GameTaskDto> getNextTask(@Query("userid") long userid,
                                  @Query("gameid") long gameid);

    @POST("organizer/validate")
    Call<TaskValidationResponse> validateTask(@Body TaskValidationRequest request,
                                              @Query("userid") long userid,
                                              @Query("gameid") long gameid);

    @POST("organizer/leave")
    Call<Void> leaveGame(@Query("userid") long userid,
                         @Query("gameid") long gameid);

    @POST("organizer/subscribe")
    Call<Void> subscribe(@Query("userId") long userid,
                         @Query("gameId") long gameid);

    @POST("demodata/photo")
    Call<Long> sendPhoto(@Body byte[] photo);

    @GET("organizer/validate/async/{taskid}/result")
    Call<TaskValidationResponse> getAsyncValidationResult(@Path("taskid") long taskid);

    @GET("organizer/validate/async")
    Call<List<AsyncValidationDto>> getAsyncValidationRequests(@Query("userid") long userid);

    @POST("organizer/validate/async")
    Call<Void> sendAsyncValidationResult(@Body AsyncValidationDto dto);

    @POST("organizer/invite/response")
    Call<Void> sendInviteResponse(@Query("inviteid") long inviteId, @Query("confirmed") boolean confirmed);

    @GET("game/{gameid}/point")
    Call<List<GamePointDto>> getGamePoints(@Path("gameid") long gameid);
}
