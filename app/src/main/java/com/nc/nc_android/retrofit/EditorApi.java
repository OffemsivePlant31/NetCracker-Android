package com.nc.nc_android.retrofit;

import com.nc.nc_android.dto.CreateGameDto;
import com.nc.nc_android.dto.GameEditorInfoDto;
import com.nc.nc_android.dto.GameEditorTaskInfoDto;
import com.nc.nc_android.dto.GameModeDto;
import com.nc.nc_android.dto.GamePointDto;
import com.nc.nc_android.dto.GameTaskDto;
import com.nc.nc_android.dto.PersonDto;

import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by fastn on 20.11.2017.
 */

public interface EditorApi {
    @GET("edit/")
    Call<List<GameEditorInfoDto>> getUserTemplates(@Query("userId") long userId);

    @GET("edit/points/{id}")
    Call<GamePointDto> getPoint(@Path("id") long pointId);

    @POST("edit/new")
    Call<Long> createNewEmptyGame(@Body long userId);

    @POST("edit/{id}/tasks/new")
    Call<Long> createNewTaskInGame(
            @Path("id") long gameId
    );

    @PUT("edit/{gameId}/tasks")
    Call<Void> updateTaskInfo(
            @Body GameEditorTaskInfoDto task
    );

    @POST("edit/points/new")
    Call<Long> createNewPointInTask(@Body long taskId, @Query("userId") long userId);

    @PUT("edit/{gameId}/tasks/reorder")
    Call<Void> reorderTasks(
            @Path("gameId") long gameId,
            @Body List<Long> newIdOrder
    );

    @GET("edit/modes")
    Call<List<GameModeDto>> getModes();

    @POST("edit/points")
    Call<Void> createPoint(@Body GamePointDto point);

    @PUT("edit/points/{id}")
    Call<Void> changePoint(
            @Path("id") long pointId,
            @Body GamePointDto point
    );

    @DELETE("edit/points/{id}")
    Call<Void> deletePoint(@Path("id") long pointId);

    @GET("edit/store")
    Call<List<GameEditorInfoDto>> getTemplatesFromStore(@Query("userId") long userId);

    @GET("edit/{id}/tasks")
    Call<List<GameEditorTaskInfoDto>> getGameTasks(
            @Path("id") long gameId
    );

    // TODO: 21.11.2017
    // improve
    @GET("edit/1/tasks/{taskId}")
    Call<GameEditorTaskInfoDto> getGameTasksById(
            @Path("taskId") long taskId
    );

    @POST("edit/add")
    Call<Void> addTemplateFromStore(@Query("userId") long userId, @Body long gameId);

    @POST("edit/create")
    Call<Void> createGameInstance(
            @Query("userId") long userId,
            @Body long gameId,
            @Body Date startDate,
            @Body Date endDate
    );

    @GET("edit/users")
    Call<List<PersonDto>> getUsers();

    @GET("edit/{id}")
    Call<GameEditorInfoDto> getInfo(
            @Path("id") long id,
            @Query("userId") long userId
    );

    @POST("edit/{id}")
    Call<Void> publishGame(
            @Path("id") long id,
            @Query("userId") long userId,
            @Body  GameEditorInfoDto gameDto
    );

    @DELETE("edit/{id}")
    Call<Void> deleteGame(
            @Path("id") long id,
            @Query("userId") long userId
    );

    @POST("edit/{id}/create")
    Call<Void> createGameInstance(
            @Path("id") long gameId,
            @Query("userId") long userId,
            @Body CreateGameDto create
    );

    @DELETE("edit/{id}/tasks/delete")
    Call<Void> deleteTask(
        @Query("userId") long userId,
        @Query("taskId") long taskId
    );
}
