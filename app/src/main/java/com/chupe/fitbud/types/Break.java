package com.chupe.fitbud.types;

public class Break {
    int duration;
    int name; // ?

    public Break(int d) {
        this.duration = d;
    }

    public int getDuration() { return duration; }
    void setDuration(int d) { this.duration = d; }

    public String getName() {
        return "Break";
    }
}
