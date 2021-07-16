package com.chupe.fitbud.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.chupe.fitbud.types.Exercise;
import com.chupe.fitbud.types.Workout;

@Database(entities = {Exercise.class, Workout.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ExerciseDAO exerciseDAO();
    public abstract WorkoutDAO workoutDAO();
}
