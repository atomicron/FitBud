package com.chupe.fitbud.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.chupe.fitbud.types.Exercise;

import java.util.List;

@Dao
public interface ExerciseDAO {
    @Query("SELECT * FROM exercise")
    List<Exercise> getAll();

    @Insert
    void insertAll(Exercise exercises);

    @Update
    void updateExercise(Exercise exercises);

    @Delete
    void deleteExercise(Exercise exercise);

    @Query("SELECT * FROM exercise WHERE id=:id")
    Exercise getExercise(int id);
}
