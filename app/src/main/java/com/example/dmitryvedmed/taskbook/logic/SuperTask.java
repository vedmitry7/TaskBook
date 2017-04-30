package com.example.dmitryvedmed.taskbook.logic;


import java.io.Serializable;

public class SuperTask implements Serializable {
    private int id;
    private int position;
    private int color;
    private long reminderTime;
    private boolean remind;
    private int repeatingPeriod;
    private boolean repeating;

    public boolean isRepeating() {
        return repeating;
    }

    public void setRepeating(boolean repeating) {
        this.repeating = repeating;
    }

    public int getRepeatingPeriod() {
        return repeatingPeriod;
    }

    public void setRepeatingPeriod(int repeatingPeriod) {
        this.repeatingPeriod = repeatingPeriod;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

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

    public long getReminderTime() {
        return reminderTime;
    }

    public void setReminderTime(long reminderTime) {
        this.reminderTime = reminderTime;
    }

    public boolean isRemind() {
        return remind;
    }

    public void setRemind(boolean remind) {
        this.remind = remind;
    }
}
