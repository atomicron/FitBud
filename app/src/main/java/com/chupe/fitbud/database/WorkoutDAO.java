package com.chupe.fitbud.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.chupe.fitbud.types.Workout;

import java.util.List;

@Dao
public interface WorkoutDAO {
    @Query("SELECT * FROM workout")
    List<Workout> getAll();

    @Insert
    void insertAll(Workout workout);

    @Update
    void updateWorkout(Workout workout);

    @Delete
    void deleteWorkout(Workout workout);
}
