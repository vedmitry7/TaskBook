package com.example.dmitryvedmed.taskbook;


import java.io.Serializable;

public class SuperTask implements Serializable {
    private int id;
    private int position;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}