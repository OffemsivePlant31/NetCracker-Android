package com.nc.nc_android.dto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by fastn on 06.12.2017.
 */

public class CreateGameDto {
    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    int year;
    int month;
    int day;
    int hour;
    int minute;

    public List<PersonDto> getUsers() {
        return users;
    }

    public void setUsers(List<PersonDto> users) {
        this.users = users;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    List<PersonDto> users;
    boolean isPublic;
}
