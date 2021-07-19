package com.chupe.fitbud.types;

import android.app.Activity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.chupe.fitbud.MainActivity;

import java.util.ArrayList;
import java.util.Arrays;
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

    @Ignore
    private List<Action> actions;

    public void setActions(List<Action> tmp_actions) {
        databaseActivities="";
        for (Action a : tmp_actions)
            if (a.isBreak)
                databaseActivities += String.valueOf(0) + "." + a.brek.getDuration() + ";";
            else
                databaseActivities += String.valueOf(a.exercise.id) + "." + a.exercise.getName() + "." + a.exercise.getDuration() + ";";
    }

    public List<Action> getActions(MainActivity mainActivity) {
        List<Action> tmp_actions = new ArrayList<Action>();
        for (String str : databaseActivities.split(";")) {
            System.out.println("Str: " + str);
            if (str.startsWith("0.")) {
                tmp_actions.add(new Action(new Break(Integer.valueOf(str.substring(2)))));
            } else {
                String[] arr = str.split(".");
                System.out.println("Arr[0]: " + arr[0]);
                Integer id = Integer.valueOf(arr[0]);
                System.out.println("ID: " + id);
                Exercise e = mainActivity.db.exerciseDAO().getExercise(id);
                tmp_actions.add(new Action(e));
            }
        }
        actions = tmp_actions;
        return actions;
    }
    public Workout(String name) { this.name = name; }

}
