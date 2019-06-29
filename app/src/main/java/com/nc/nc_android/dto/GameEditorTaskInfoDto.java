package com.nc.nc_android.dto;

/**
 * Created by fastn on 20.11.2017.
 */

import java.util.List;

public class GameEditorTaskInfoDto {

    private long taskId;

    public long getTaskId() {
        return taskId;
    }

    public void setTaskId(long taskId) {
        this.taskId = taskId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFindCondition() {
        return findCondition;
    }

    public void setFindCondition(String findCondition) {
        this.findCondition = findCondition;
    }

    public String getCheckConditionDescription() {
        return checkConditionDescription;
    }

    public void setCheckConditionDescription(String checkConditionDescription) {
        this.checkConditionDescription = checkConditionDescription;
    }

    public String getCheckConditionCode() {
        return checkConditionCode;
    }

    public void setCheckConditionCode(String checkConditionCode) {
        this.checkConditionCode = checkConditionCode;
    }

    public GamePointDto getPoint() {
        return point;
    }

    public void setPoint(GamePointDto point) {
        this.point = point;
    }

    public List<GameEditorTaskInfoDto> getPrev() {
        return prev;
    }

    public void setPrev(List<GameEditorTaskInfoDto> prev) {
        this.prev = prev;
    }

    private String name;
    private String description;
    private String findCondition;
    private String checkConditionDescription;
    private String checkConditionCode;
    private GamePointDto point;
    private List<GameEditorTaskInfoDto> prev;
}