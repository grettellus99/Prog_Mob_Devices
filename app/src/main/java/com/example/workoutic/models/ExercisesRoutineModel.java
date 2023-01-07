package com.example.workoutic.models;

public class ExercisesRoutineModel {
    private int id;
    private int exercise_id;
    private int series;
    private int repetitions;
    private double weightKg;
    private double timeMinutes;
    private String dayOfWeek;
    private int routine_id;

    public ExercisesRoutineModel() {
    }

    public ExercisesRoutineModel(int id, int exercise_id, int series, int repetitions, double weightKg, double timeMinutes, String dayOfWeek, int routine_id) {
        this.id = id;
        this.exercise_id = exercise_id;
        this.series = series;
        this.repetitions = repetitions;
        this.weightKg = weightKg;
        this.timeMinutes = timeMinutes;
        this.dayOfWeek = dayOfWeek;
        this.routine_id = routine_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getExercise_id() {
        return exercise_id;
    }

    public void setExercise_id(int exercise_id) {
        this.exercise_id = exercise_id;
    }

    public int getSeries() {
        return series;
    }

    public void setSeries(int series) {
        this.series = series;
    }

    public int getRepetitions() {
        return repetitions;
    }

    public void setRepetitions(int repetitions) {
        this.repetitions = repetitions;
    }

    public double getWeightKg() {
        return weightKg;
    }

    public void setWeightKg(double weightKg) {
        this.weightKg = weightKg;
    }

    public double getTimeMinutes() {
        return timeMinutes;
    }

    public void setTimeMinutes(double timeMinutes) {
        this.timeMinutes = timeMinutes;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public int getRoutine_id() {
        return routine_id;
    }

    public void setRoutine_id(int routine_id) {
        this.routine_id = routine_id;
    }
}
