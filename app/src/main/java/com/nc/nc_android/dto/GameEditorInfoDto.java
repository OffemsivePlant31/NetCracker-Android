package com.nc.nc_android.dto;

import java.util.Date;
import java.util.List;

/**
 * Created by fastn on 20.11.2017.
 */

public class GameEditorInfoDto {
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

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public GameModeDto getMode() {
        return mode;
    }

    public void setMode(GameModeDto mode) {
        this.mode = mode;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Long getPeriodicity() {
        return periodicity;
    }

    public void setPeriodicity(Long periodicity) {
        this.periodicity = periodicity;
    }

    public List<GameTaskDto> getTasks() {
        return tasks;
    }

    public void setTasks(List<GameTaskDto> tasks) {
        this.tasks = tasks;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    private long id;
    private String name;
    private String description;
    private int maxPlayers;
    private GameModeDto mode;
    private Date startDate;
    private Long periodicity;
    private List<GameTaskDto> tasks;
    private boolean active;
}
