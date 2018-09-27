package com.android.projeto.handsoff.domain;

import java.io.Serializable;

public class Status implements Serializable {

    private int id;
    private String title;
    //private int time;
    private String description;

    public Status(int id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }

    public Status() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    /*public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }*/

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
