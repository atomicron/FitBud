package com.chupe.fitbud.types;

// Todo I wish there was a UNION in Java
public class Action {
    public boolean isBreak;
    public boolean isExercise;

    public Exercise exercise;
    public Break brek;

    public Action(Break b) {
        this.brek = b;
        isBreak = true;
        isExercise = false;
    }
    public Action(Exercise e) {
        this.exercise = e;
        isExercise = true;
        isBreak = false;
    }
}
