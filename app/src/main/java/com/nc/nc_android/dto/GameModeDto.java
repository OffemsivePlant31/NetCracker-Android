package com.nc.nc_android.dto;

/**
 * Created by fastn on 20.11.2017.
 */

public class GameModeDto {
    private long id;
    private String name;
    private String description;

    public GameModeDto() {
    }


    public GameModeDto(String name, String description) {
        this.name = name;
        this.description = description;
    }

    @Deprecated
    public GameModeDto(long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
}
