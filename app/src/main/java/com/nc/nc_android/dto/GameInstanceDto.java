package com.nc.nc_android.dto;


import java.util.Date;

public class GameInstanceDto{

    private long id;

    private GameTemplateDto gameTemplateDto;
    private Date startDate;
    private Date endDate;
    private String overseer;

    private boolean subscribed;

    public GameInstanceDto(){

    }

    public String getOverseer() {
        return overseer;
    }

    public void setOverseer(String overseer) {
        this.overseer = overseer;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public GameTemplateDto getGameTemplateDto() {
        return gameTemplateDto;
    }

    public void setGameTemplateDto(GameTemplateDto gameTemplateDto) {
        this.gameTemplateDto = gameTemplateDto;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public boolean isSubscribed() {
        return subscribed;
    }

    public void setSubscribed(boolean subscribed) {
        this.subscribed = subscribed;
    }
}
