package com.chupe.fitbud.types;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity
public class Workout {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public int id;

    @ColumnInfo(name = "exercises")
    public String databaseActivities = "";

    @ColumnInfo(name = "name")
    private String name;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public void setActivities(List<Exercise> activities) {
        databaseActivities="";
        for (Exercise e : activities)
            databaseActivities+= String.valueOf(e.id)+ ";";
    }

    public String[] getActivities() { return databaseActivities.split(";"); }
    public Workout(String name) { this.name = name; }

}
