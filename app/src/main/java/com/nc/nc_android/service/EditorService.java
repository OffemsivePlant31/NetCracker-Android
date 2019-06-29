package com.nc.nc_android.service;

import com.nc.nc_android.MyApplication;
import com.nc.nc_android.dto.GameEditorInfoDto;
import com.nc.nc_android.retrofit.EditorApi;
import com.nc.nc_android.retrofit.RetrofitSingleton;

import java.util.Date;
import java.util.List;

import retrofit2.Call;

/**
 * Created by fastn on 20.11.2017.
 */

public class EditorService {
    EditorApi editorApi;

    public EditorService() {
        editorApi = RetrofitSingleton.getInstance(MyApplication.getContext()).getApi(EditorApi.class);
    }

    public Call<List<GameEditorInfoDto>> getUserTemplates(long userId){
        return editorApi.getUserTemplates(userId);
    }

    public Call<List<GameEditorInfoDto>> getTemplatesFromStore(long userId){
        return editorApi.getTemplatesFromStore(userId);
    }

    public Call<Void> addTemplateFromStore(long userId, long gameId){
        return editorApi.addTemplateFromStore(userId, gameId);
    }

    public Call<Void> createGameInstance(
            long userId,
            long gameId,
            Date startDate,
            Date endDate
    ){
        return editorApi.createGameInstance(userId, gameId, startDate, endDate);
    }

    public Call<GameEditorInfoDto> getInfo(
            long userId,
            long id
    ){
        return editorApi.getInfo(userId, id);
    }

    public Call<Void> publishGame(
            long userId,
            long id,
            GameEditorInfoDto gameDto
    ){
        return editorApi.publishGame(userId, id, gameDto);
    }

    public Call<Void> deleteGame(
            long userId,
            long id
    ){
        return editorApi.deleteGame(userId, id);
    }
}
