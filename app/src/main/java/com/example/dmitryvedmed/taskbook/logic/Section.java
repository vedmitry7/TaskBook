package com.example.dmitryvedmed.taskbook.logic;

import java.io.Serializable;


public class Section implements Serializable {
    private String name;
    private int id;
    private int position;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
