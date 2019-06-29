package com.nc.nc_android.pojo;

import java.util.ArrayList;

public class GameInfo {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    private String name;
    private String mode;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    private Long id;

    public Integer getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(Integer maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    private Integer maxPlayers;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private String description;

    public GameInfo(String name, String description,String mode, Integer maxPlayers, Long id) {
        this.name = name;
        this.description = description;
        this.mode = mode;
        this.maxPlayers = maxPlayers;
        this.id = id;
    }

    private static int lastId = 0;

    public static ArrayList<GameInfo> createContactsList(int numGames) {
        ArrayList<GameInfo> contacts = new ArrayList<GameInfo>();

        for (int i = 1; i <= numGames; i++) {
            contacts.add(new GameInfo("Person " + ++lastId, "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.", "Any mode", i, Long.valueOf(i)));
        }

        return contacts;
    }
}