package com.example.workoutic.util;

import android.database.sqlite.SQLiteDatabase;

import com.example.workoutic.R;

public class DatabasesUtil {
    // Database New Routine
    public static final int NR_DATABASE_VERSION = 1;
    public static final String NR_DATABASE_NAME = "new_routines_db";


    // Database Workoutic
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "workoutic_db";

    // Variables relacionadas con la TABLA RUTINAS
    public static final String ROUTINES_TABLE_NAME = "routines";

    // Routines table columns names
    public static final String ROUTINES_KEY_ID = "_id";
    public static final String  ROUTINES_KEY_NAME = "name";
    public static final String  ROUTINES_KEY_TIMESTAMP = "timestamp";


    // Variables relacionadas con la TABLA EJERCICIOS_RUTINAS
    public static final String EXERCISES_ROUTINES_TABLE_NAME = "exercises_routines";

    // Routines table columns names
    public static final String EXERCISES_ROUTINES_KEY_ID = "_id";
    public static final String EXERCISES_ROUTINES_KEY_EXERCISE_ID = "exercise_id";
    public static final String EXERCISES_ROUTINES_KEY_SERIES = "series";
    public static final String EXERCISES_ROUTINES_KEY_REPETITIONS = "repetitions";
    public static final String EXERCISES_ROUTINES_KEY_WEIGHT = "weight_kilograms";
    public static final String EXERCISES_ROUTINES_KEY_TIME = "time_minutes";
    public static final String EXERCISES_ROUTINES_KEY_DAY_OF_WEEK = "day_of_week";
    public static final String EXERCISES_ROUTINES_KEY_ROUTINE_ID = "routine_id";

    // Variables relacionadas con la TABLA EJERCICIOS
    public static final String EXERCISES_TABLE_NAME = "exercises";

    // Exercises table columns names
    public static final String EXERCISES_KEY_ID = "_id";
    public static final String EXERCISES_KEY_NAME = "name";
    public static final String EXERCISES_KEY_DESCRIPTION = "description";
    public static final String EXERCISES_KEY_EXECUTION = "execution";
    public static final String EXERCISES_KEY_IMAGE_MAIN = "img_main";
    public static final String EXERCISES_KEY_IMAGE_ALT = "img_alt";
    public static final String EXERCISES_KEY_EQUIPMENT = "equipment";
    public static final String EXERCISES_KEY_MUSCLES = "muscles";
    public static final String EXERCISES_KEY_LEVEL = "level";
   // public static final String  EXERCISES_KEY_EXERCISES_ROUTINES_ID = "exercise_routine_id";

    // Dias
    public static final String MONDAY = "lunes";
    public static final String TUESDAY = "martes";
    public static final String WEDNESDAY = "miércoles";
    public static final String THURSDAY = "jueves";
    public static final String FRIDAY = "viernes";
    public static final String SATURDAY = "sábado";
    public static final String SUNDAY= "domingo";

}
