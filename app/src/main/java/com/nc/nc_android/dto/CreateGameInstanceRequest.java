package com.nc.nc_android.dto;

import java.util.Date;

/**
 * Created by danya on 11.11.2017.
 */

public class CreateGameInstanceRequest {

    private long gameId;
    private long userId;
    private Date startDate;


    public CreateGameInstanceRequest() {
    }

    public CreateGameInstanceRequest(long gameId, long userId, Date startDate) {
        this.gameId = gameId;
        this.userId = userId;
        this.startDate = startDate;
    }

    public long getGameId() {
        return gameId;
    }

    public void setGameId(long gameId) {
        this.gameId = gameId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
}
