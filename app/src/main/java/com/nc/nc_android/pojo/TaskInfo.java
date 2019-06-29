package com.nc.nc_android.pojo;

/**
 * Created by fastn on 28.11.2017.
 */

public class TaskInfo {
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    Long id;
    String name;
    String description;

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

    public String getCheckCondition() {
        return checkCondition;
    }

    public void setCheckCondition(String checkCondition) {
        this.checkCondition = checkCondition;
    }

    public String getCheckCode() {
        return checkCode;
    }

    public void setCheckCode(String checkCode) {
        this.checkCode = checkCode;
    }

    public Integer getPrev() {
        return prev;
    }

    public void setPrev(Integer prev) {
        this.prev = prev;
    }

    public Integer getNext() {
        return next;
    }

    public void setNext(Integer next) {
        this.next = next;
    }

    String findCondition;
    String checkCondition;
    String checkCode;
    Integer prev;
    Integer next;

    public TaskInfo(Long id,String name, String description, String findCondition, String checkCondition, String checkCode, Integer prev, Integer next) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.findCondition = findCondition;
        this.checkCondition = checkCondition;
        this.checkCode = checkCode;
        this.prev = prev;
        this.next = next;
    }
}
