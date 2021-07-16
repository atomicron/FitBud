package com.chupe.fitbud.types;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class Exercise {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public int id;
    @ColumnInfo(name = "name")
    @NonNull
    public String name;
    @ColumnInfo(name = "duration")
    public int duration;
    @ColumnInfo(name = "hasDuration")
    public boolean hasDuration;

    public Exercise(String name, int duration) {
        this.name = name;
        this.duration = duration;
        if (duration > 0)
            this.hasDuration = true;
        else
            this.hasDuration = false;
    }

    @Ignore
    public Exercise(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
    public void setName(@NonNull String name) {
        this.name = name;
    }
    public int getDuration() {
        return duration;
    }
    public void setDuration(int duration) {
        this.duration = duration;
    }
}
