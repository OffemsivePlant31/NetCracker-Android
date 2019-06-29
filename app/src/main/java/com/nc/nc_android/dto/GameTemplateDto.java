package com.nc.nc_android.dto;



public class GameTemplateDto {

    private long id;
    private String name;
    private String description;
    private int maxPlayers;
    private String author;
    private String gameMode;
    // TODO: 29.11.2017 Некорректное название поля, с глаголов начинаются названия методов
    private String getGameModeDescription;
    private float rating;

    public GameTemplateDto(){

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

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getGameMode() {
        return gameMode;
    }

    public void setGameMode(String gameMode) {
        this.gameMode = gameMode;
    }

    public String getGetGameModeDescription() {
        return getGameModeDescription;
    }

    public void setGetGameModeDescription(String getGameModeDescription) {
        this.getGameModeDescription = getGameModeDescription;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}
