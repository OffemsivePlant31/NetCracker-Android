package com.nc.nc_android.dto;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GameTaskDto {


    private long id;

    private GamePointDto point;
    private String findCondition;
    private String checkCondition;
    private String checkConditionType;
    private Date timeDone;
    private List<Long> prevTasks;
    private boolean inValidation;

    public GameTaskDto(){

    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public GamePointDto getPoint() {
        return point;
    }

    public void setPoint(GamePointDto point) {
        this.point = point;
    }

    public String getFindCondition() {
        return findCondition;
    }

    public void setFindCondition(String findCondition) {
        this.findCondition = findCondition;
    }

    public String getCheckCondition() {
        return checkCondition;
    }

    public void setCheckCondition(String checkCondition) {
        this.checkCondition = checkCondition;
    }

    public String getCheckConditionType() {
        return checkConditionType;
    }

    public void setCheckConditionType(String checkConditionType) {
        this.checkConditionType = checkConditionType;
    }

    public Date getTimeDone() {
        return timeDone;
    }

    public void setTimeDone(Date timeDone) {
        this.timeDone = timeDone;
    }

    public List<Long> getPrevTasks() {
        return prevTasks;
    }

    public void setPrevTasks(List<Long> prevTasks) {
        this.prevTasks = prevTasks;
    }

    public boolean isInValidation() {
        return inValidation;
    }

    public void setInValidation(boolean inValidation) {
        this.inValidation = inValidation;
    }
}
