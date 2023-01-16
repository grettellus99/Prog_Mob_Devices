package com.example.workoutic.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.workoutic.R;
import com.example.workoutic.models.ExercisesModel;
import com.example.workoutic.models.ExercisesRoutineModel;
import com.example.workoutic.models.RoutineModel;
import com.example.workoutic.util.DatabasesUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WorkouticDBHelper extends SQLiteOpenHelper {
    public WorkouticDBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

          /*
            create table routines ( id, name)
        */
        String CREATE_ROUTINE_TABLE = "CREATE TABLE " + DatabasesUtil.ROUTINES_TABLE_NAME
                + "("
                + DatabasesUtil.ROUTINES_KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + DatabasesUtil.ROUTINES_KEY_NAME +" TEXT,"
                + DatabasesUtil.ROUTINES_KEY_TIMESTAMP +" INTEGER" +
                ");";

        String CREATE_EXERCISES_ROUTINE_TABLE = "CREATE TABLE " + DatabasesUtil.EXERCISES_ROUTINES_TABLE_NAME
                + " ("
                + DatabasesUtil.EXERCISES_ROUTINES_KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + DatabasesUtil.EXERCISES_ROUTINES_KEY_EXERCISE_ID + " INTEGER,"
                + DatabasesUtil.EXERCISES_ROUTINES_KEY_ROUTINE_ID + " INTEGER,"
                + DatabasesUtil.EXERCISES_ROUTINES_KEY_SERIES + " INTEGER,"
                + DatabasesUtil.EXERCISES_ROUTINES_KEY_REPETITIONS + " INTEGER,"
                + DatabasesUtil.EXERCISES_ROUTINES_KEY_WEIGHT + " FLOAT,"
                + DatabasesUtil.EXERCISES_ROUTINES_KEY_TIME + " FLOAT,"
                + DatabasesUtil.EXERCISES_ROUTINES_KEY_DAY_OF_WEEK + "TEXT, "
                + "FOREIGN KEY "+"(" + DatabasesUtil.EXERCISES_ROUTINES_KEY_ROUTINE_ID+ ") REFERENCES "
                + DatabasesUtil.ROUTINES_TABLE_NAME +"("+DatabasesUtil.ROUTINES_KEY_ID+"),"
                + "FOREIGN KEY "+"(" + DatabasesUtil.EXERCISES_ROUTINES_KEY_EXERCISE_ID+ ") REFERENCES "
                + DatabasesUtil.EXERCISES_TABLE_NAME +"("+DatabasesUtil.EXERCISES_KEY_ID+")"
                + ");";

        String CREATE_EXERCISE_TABLE = "CREATE TABLE "+ DatabasesUtil.EXERCISES_TABLE_NAME
                + " ("
                + DatabasesUtil.EXERCISES_KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DatabasesUtil.EXERCISES_KEY_NAME + " TEXT, "
                + DatabasesUtil.EXERCISES_KEY_DESCRIPTION + " TEXT, "
                + DatabasesUtil.EXERCISES_KEY_EXECUTION + " TEXT, "
                + DatabasesUtil.EXERCISES_KEY_IMAGE_MAIN + " TEXT, "
                + DatabasesUtil.EXERCISES_KEY_IMAGE_ALT + " TEXT, "
                + DatabasesUtil.EXERCISES_KEY_EQUIPMENT + " TEXT, "
                + DatabasesUtil.EXERCISES_KEY_MUSCLES + " TEXT, "
                + DatabasesUtil.EXERCISES_KEY_LEVEL + " TEXT"
                +");";

        // crear las tablas
        db.execSQL(CREATE_ROUTINE_TABLE);
        db.execSQL(CREATE_EXERCISE_TABLE);
        db.execSQL(CREATE_EXERCISES_ROUTINE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        String DROP_TABLE = String.valueOf(R.string.db_drop);
        db.execSQL(DROP_TABLE, new String[]{DatabasesUtil.DATABASE_NAME});
        // Create a table
        onCreate(db);

    }

    public void deleteDB(){
        String DROP_TABLE = String.valueOf(R.string.db_drop);
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(DROP_TABLE, new String[]{DatabasesUtil.NR_DATABASE_NAME});
        db.close(); // closing db connection!
    }

    /* CRUD = Create, Read, Update, Delete */
    // ADD ITEM
    // Routines
    public long addRoutine(RoutineModel routine) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DatabasesUtil.ROUTINES_KEY_NAME,routine.getName());
        values.put(DatabasesUtil.ROUTINES_KEY_TIMESTAMP,routine.getTimestamp());

        // Insertar
        long id = db.insert(DatabasesUtil.ROUTINES_TABLE_NAME,null,values);
        db.close(); // closing db connection!
        return id;
    }

    // Exercises
    public long addExercises(ExercisesModel exercise) {

        List<Long> res = isExerciseInDB(exercise);
        if (res.get(0) == -1){
            // Se necesita insertar
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(DatabasesUtil.EXERCISES_KEY_NAME,exercise.getName());
            values.put(DatabasesUtil.EXERCISES_KEY_DESCRIPTION,exercise.getDescription());
            values.put(DatabasesUtil.EXERCISES_KEY_EXECUTION,exercise.getExecution());
            values.put(DatabasesUtil.EXERCISES_KEY_EQUIPMENT,concatenarLista(exercise.getEquipment()));
            values.put(DatabasesUtil.EXERCISES_KEY_LEVEL,concatenarLista(exercise.getLevel()));
            values.put(DatabasesUtil.EXERCISES_KEY_IMAGE_MAIN,exercise.getImgMainURL());
            values.put(DatabasesUtil.EXERCISES_KEY_IMAGE_ALT,exercise.getImgAltURL());
            values.put(DatabasesUtil.EXERCISES_KEY_MUSCLES,concatenarLista(exercise.getMuscles()));
            // Insertar
            long id = db.insert(DatabasesUtil.EXERCISES_TABLE_NAME,null,values);
            db.close(); // closing db connection!
            return id;
        }
        else if (res.get(0) >= 0){
            // Update
            return updateExercise(exercise);
        }
        // Si no entra es que el resultado era -2 (no se necesita actualizar) y devolvemos el id que esta en la segunda pos de la lista
        return res.get(1);
    }

    // Exercises Routines
    public long addExerciseRoutine(ExercisesRoutineModel exercise_routine) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DatabasesUtil.EXERCISES_ROUTINES_KEY_REPETITIONS,exercise_routine.getRepetitions());
        values.put(DatabasesUtil.EXERCISES_ROUTINES_KEY_SERIES,exercise_routine.getSeries());
        values.put(DatabasesUtil.EXERCISES_ROUTINES_KEY_DAY_OF_WEEK,exercise_routine.getDayOfWeek());
        values.put(DatabasesUtil.EXERCISES_ROUTINES_KEY_WEIGHT,exercise_routine.getWeightKg());
        values.put(DatabasesUtil.EXERCISES_ROUTINES_KEY_TIME,exercise_routine.getTimeMinutes());
        values.put(DatabasesUtil.EXERCISES_ROUTINES_KEY_EXERCISE_ID,exercise_routine.getExercise_id());
        values.put(DatabasesUtil.EXERCISES_ROUTINES_KEY_ROUTINE_ID,exercise_routine.getRoutine_id());

        // Insertar
        long id = db.insert(DatabasesUtil.EXERCISES_ROUTINES_TABLE_NAME,null,values);
        db.close(); // closing db connection!
        return id;
    }

    // GET ID
    // Routines
    public RoutineModel getRoutine(long id) {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(DatabasesUtil.ROUTINES_TABLE_NAME,new String[]{DatabasesUtil.ROUTINES_KEY_NAME, DatabasesUtil.ROUTINES_KEY_TIMESTAMP}, DatabasesUtil.ROUTINES_KEY_ID + "=?",new String[]{String.valueOf(id)}, null,null,null);

        if (cursor != null){
            cursor.moveToFirst();
        }

        RoutineModel routine = new RoutineModel();
        assert cursor != null;
        routine.setId(id);
        routine.setName(cursor.getString(0));
        routine.setTimestamp(Long.parseLong(cursor.getString(1)));

        cursor.close();
        db.close();

        return routine;
    }

    // Exercises
    public  ExercisesModel getExercise(long id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(DatabasesUtil.EXERCISES_TABLE_NAME,new String[]{
                        DatabasesUtil.EXERCISES_KEY_NAME,
                        DatabasesUtil.EXERCISES_KEY_DESCRIPTION,
                        DatabasesUtil.EXERCISES_KEY_EXECUTION,
                        DatabasesUtil.EXERCISES_KEY_IMAGE_ALT,
                        DatabasesUtil.EXERCISES_KEY_IMAGE_MAIN,
                        DatabasesUtil.EXERCISES_KEY_EQUIPMENT,
                        DatabasesUtil.EXERCISES_KEY_LEVEL,
                        DatabasesUtil.EXERCISES_KEY_MUSCLES},
                DatabasesUtil.EXERCISES_KEY_ID + "=?",new String[]{String.valueOf(id)},
                null,null,null);

        if (cursor != null){
            cursor.moveToFirst();
        }

        ExercisesModel exercise = new ExercisesModel();
        assert cursor != null;
        exercise.setId(id);
        exercise.setName(cursor.getString(0));
        exercise.setDescription(cursor.getString(1));
        exercise.setExecution(cursor.getString(2));
        exercise.setImgAltURL(cursor.getString(3));
        exercise.setImgMainURL(cursor.getString(4));
        exercise.setEquipment(revertirConcatenacion(cursor.getString(5)));
        exercise.setLevel(revertirConcatenacion(cursor.getString(6)));
        exercise.setMuscles(revertirConcatenacion(cursor.getString(7)));

        cursor.close();
        db.close();

        return exercise;
    }

    // Exercises Routines
    public ExercisesRoutineModel getExerciseRoutine(long id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(DatabasesUtil.EXERCISES_ROUTINES_TABLE_NAME,new String[]{
                        DatabasesUtil.EXERCISES_ROUTINES_KEY_SERIES,
                        DatabasesUtil.EXERCISES_ROUTINES_KEY_REPETITIONS,
                        DatabasesUtil.EXERCISES_ROUTINES_KEY_TIME,
                        DatabasesUtil.EXERCISES_ROUTINES_KEY_DAY_OF_WEEK,
                        DatabasesUtil.EXERCISES_ROUTINES_KEY_WEIGHT,
                        DatabasesUtil.EXERCISES_ROUTINES_KEY_EXERCISE_ID,
                        DatabasesUtil.EXERCISES_ROUTINES_KEY_ROUTINE_ID},
                DatabasesUtil.EXERCISES_ROUTINES_KEY_ID + "=?",new String[]{String.valueOf(id)},
                null,null,null);

        if (cursor != null){
            cursor.moveToFirst();
        }

        ExercisesRoutineModel exercise_routine = new ExercisesRoutineModel();
        assert cursor != null;
        exercise_routine.setId(id);
        exercise_routine.setSeries(Integer.parseInt(cursor.getString(0)));
        exercise_routine.setRepetitions(Integer.parseInt(cursor.getString(1)));
        exercise_routine.setTimeMinutes(Float.parseFloat(cursor.getString(2)));
        exercise_routine.setDayOfWeek(cursor.getString(3));
        exercise_routine.setWeightKg(Float.parseFloat(cursor.getString(4)));
        exercise_routine.setExercise_id(Integer.parseInt(cursor.getString(5)));
        exercise_routine.setRoutine_id(Integer.parseInt(cursor.getString(6)));

        cursor.close();
        db.close();

        return exercise_routine;

    }

    // GET ALL
    // Routine
    public List<RoutineModel> getAllRoutines(int metodord) {
        List<RoutineModel> routineList = new ArrayList<RoutineModel>();
        // Select all

        String selectAll = "SELECT * FROM " + DatabasesUtil.ROUTINES_TABLE_NAME + " ORDER BY " + DatabasesUtil.ROUTINES_KEY_TIMESTAMP + (metodord == 0?" DESC":" ASC");
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectAll,null);

        if(cursor.moveToFirst()){
            do{
                RoutineModel routine = new RoutineModel();
                routine.setId(Integer.parseInt(cursor.getString(0)));
                routine.setName(cursor.getString(1));
                routine.setTimestamp(Long.parseLong(cursor.getString(2)));
                routineList.add(routine);   // add a la lista
            }while(cursor.moveToNext());
        }

        db.close();
        cursor.close();
        return routineList;
    }

    // ExerciseRoutine
    public List<ExercisesRoutineModel> getAllExercisesRoutines() {
        List<ExercisesRoutineModel> exercisesRoutineList = new ArrayList<ExercisesRoutineModel>();

        // Select all
        String selectAll = "SELECT * FROM " + DatabasesUtil.EXERCISES_ROUTINES_TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectAll,null);

        if(cursor.moveToFirst()){
            do{
                ExercisesRoutineModel exercise_routine = new ExercisesRoutineModel();
                exercise_routine.setId(Integer.parseInt(cursor.getString(0)));
                exercise_routine.setSeries(Integer.parseInt(cursor.getString(3)));
                exercise_routine.setRepetitions(Integer.parseInt(cursor.getString(4)));
                exercise_routine.setTimeMinutes(Float.parseFloat(cursor.getString(6)));
                exercise_routine.setDayOfWeek(cursor.getString(7));
                exercise_routine.setWeightKg(Float.parseFloat(cursor.getString(5)));
                exercise_routine.setExercise_id(Integer.parseInt(cursor.getString(1)));
                exercise_routine.setRoutine_id(Integer.parseInt(cursor.getString(2)));
                exercisesRoutineList.add(exercise_routine);   // add a la lista
            }while(cursor.moveToNext());
        }
        db.close();
        cursor.close();
        return exercisesRoutineList;
    }

    // ExerciseRoutine x Days
    public List<ExercisesRoutineModel> getAllExercisesRoutinesDays(String day) {
        List<ExercisesRoutineModel> exercisesRoutineList = new ArrayList<ExercisesRoutineModel>();

        // Select all
        String selectAll = "SELECT * FROM " + DatabasesUtil.EXERCISES_ROUTINES_TABLE_NAME + " WHERE (" + DatabasesUtil.EXERCISES_ROUTINES_KEY_DAY_OF_WEEK + " =? " + day + ");";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectAll,null);

        if(cursor.moveToFirst()){
            do{
                ExercisesRoutineModel exercise_routine = new ExercisesRoutineModel();
                exercise_routine.setId(Integer.parseInt(cursor.getString(0)));
                exercise_routine.setSeries(Integer.parseInt(cursor.getString(3)));
                exercise_routine.setRepetitions(Integer.parseInt(cursor.getString(4)));
                exercise_routine.setTimeMinutes(Float.parseFloat(cursor.getString(6)));
                exercise_routine.setDayOfWeek(cursor.getString(7));
                exercise_routine.setWeightKg(Float.parseFloat(cursor.getString(5)));
                exercise_routine.setExercise_id(Integer.parseInt(cursor.getString(1)));
                exercise_routine.setRoutine_id(Integer.parseInt(cursor.getString(2)));
                exercisesRoutineList.add(exercise_routine);   // add a la lista
            }while(cursor.moveToNext());
        }
        db.close();
        cursor.close();
        return exercisesRoutineList;
    }

    // UPDATE
    // Routine
    public long updateRoutine(RoutineModel routine) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DatabasesUtil.ROUTINES_KEY_NAME,routine.getName());
        values.put(DatabasesUtil.ROUTINES_KEY_TIMESTAMP,routine.getTimestamp());

        // update
        long res = db.update(DatabasesUtil.ROUTINES_TABLE_NAME,values,
                DatabasesUtil.ROUTINES_KEY_ID + "=?",
                new String[]{String.valueOf(routine.getId())});

        db.close();
        return res;
    }

    // ExerciseRoutine
    public long updateExerciseRoutine(ExercisesRoutineModel exercise_routine) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DatabasesUtil.EXERCISES_ROUTINES_KEY_REPETITIONS,exercise_routine.getRepetitions());
        values.put(DatabasesUtil.EXERCISES_ROUTINES_KEY_SERIES,exercise_routine.getSeries());
        values.put(DatabasesUtil.EXERCISES_ROUTINES_KEY_DAY_OF_WEEK,exercise_routine.getDayOfWeek());
        values.put(DatabasesUtil.EXERCISES_ROUTINES_KEY_WEIGHT,exercise_routine.getWeightKg());
        values.put(DatabasesUtil.EXERCISES_ROUTINES_KEY_TIME,exercise_routine.getTimeMinutes());
        values.put(DatabasesUtil.EXERCISES_ROUTINES_KEY_EXERCISE_ID,exercise_routine.getExercise_id());
        values.put(DatabasesUtil.EXERCISES_ROUTINES_KEY_ROUTINE_ID,exercise_routine.getRoutine_id());

        // update
        long res = db.update(DatabasesUtil.EXERCISES_ROUTINES_TABLE_NAME,values,
                DatabasesUtil.EXERCISES_ROUTINES_KEY_ID + "=?",
                new String[]{String.valueOf(exercise_routine.getId())});

        db.close();
        return res;

    }

    // Exercise
    public long updateExercise(ExercisesModel exercise) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DatabasesUtil.EXERCISES_KEY_NAME,exercise.getName());
        values.put(DatabasesUtil.EXERCISES_KEY_DESCRIPTION,exercise.getDescription());
        values.put(DatabasesUtil.EXERCISES_KEY_EXECUTION,exercise.getExecution());
        values.put(DatabasesUtil.EXERCISES_KEY_EQUIPMENT,concatenarLista(exercise.getEquipment()));
        values.put(DatabasesUtil.EXERCISES_KEY_LEVEL,concatenarLista(exercise.getLevel()));
        values.put(DatabasesUtil.EXERCISES_KEY_IMAGE_MAIN,exercise.getImgMainURL());
        values.put(DatabasesUtil.EXERCISES_KEY_IMAGE_ALT,exercise.getImgAltURL());
        values.put(DatabasesUtil.EXERCISES_KEY_MUSCLES,concatenarLista(exercise.getMuscles()));

        // update
        long res= db.update(DatabasesUtil.EXERCISES_TABLE_NAME,values,
                DatabasesUtil.EXERCISES_KEY_ID + "=?",
                new String[]{String.valueOf(exercise.getId())});

        db.close();
        return res;
    }

    // DELETE
    // Routine
    public void deleteRoutine(RoutineModel routine){

        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(DatabasesUtil.ROUTINES_TABLE_NAME,DatabasesUtil.ROUTINES_KEY_ID + "=?",
                new String[]{String.valueOf(routine.getId())});

        db.close();
    }
    // ExerciseRoutine
    public void deleteExerciseRoutine(ExercisesRoutineModel exercises_routine){

        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(DatabasesUtil.EXERCISES_ROUTINES_TABLE_NAME,DatabasesUtil.EXERCISES_ROUTINES_KEY_ID + "=?",
                new String[]{String.valueOf(exercises_routine.getId())});

        db.close();
    }
    // Exercise
    public void deleteExercises(ExercisesModel exercise){

        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(DatabasesUtil.ROUTINES_TABLE_NAME,DatabasesUtil.EXERCISES_KEY_ID + "=?",
                new String[]{String.valueOf(exercise.getId())});

        db.close();
    }


    // GET COUNT
    // Routine
    public int getCountRoutines(){
        String countQuery = "SELECT * FROM " + DatabasesUtil.ROUTINES_TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery,null);

        int res = cursor.getCount();
        cursor.close();
        return res;

    }
    // ExerciseRoutine
    public int getCountExercisesRoutines(){
        String countQuery = "SELECT * FROM " + DatabasesUtil.EXERCISES_ROUTINES_TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery,null);

        int res = cursor.getCount();
        cursor.close();
        return res;

    }
    // Exercise
    public int getCountExercises(){
        String countQuery = "SELECT * FROM " + DatabasesUtil.EXERCISES_TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery,null);

        int res = cursor.getCount();
        cursor.close();
        return res;

    }

    // GET COUNT x DAYS
    public int getCountExercisesRoutinesDays(String day){
        String countQuery = "SELECT * FROM " + DatabasesUtil.EXERCISES_ROUTINES_TABLE_NAME + " WHERE (" + DatabasesUtil.EXERCISES_ROUTINES_KEY_DAY_OF_WEEK + " =? " + day + ");";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery,null);

        int res = cursor.getCount();
        cursor.close();
        return res;
    }


    /* Metodos auxiliares */

    private List<Long> isExerciseInDB(ExercisesModel exercise){
        List<Long> res = new ArrayList<Long>();
        long resNE = -1; // No esta en la BD
        long resEA = -2; // Esta y no necesita actualización

        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(DatabasesUtil.EXERCISES_TABLE_NAME,
                new String[]{
                            DatabasesUtil.EXERCISES_KEY_ID,
                            DatabasesUtil.EXERCISES_KEY_DESCRIPTION,
                            DatabasesUtil.EXERCISES_KEY_EXECUTION,
                            DatabasesUtil.EXERCISES_KEY_IMAGE_ALT,
                            DatabasesUtil.EXERCISES_KEY_IMAGE_MAIN,
                            DatabasesUtil.EXERCISES_KEY_EQUIPMENT,
                            DatabasesUtil.EXERCISES_KEY_LEVEL,
                            DatabasesUtil.EXERCISES_KEY_MUSCLES},
                DatabasesUtil.EXERCISES_KEY_NAME + "=?",new String[]{String.valueOf(exercise.getName())},
                null,null,null);

        if (cursor == null) {
            db.close();
            res.add(resNE);
            return res;
        }

        // Comprobar que los ultimos datos recuperados del ejercicio del Servidor API sean los mismos que en BD local
        cursor.moveToFirst();
        if(exercise.getDescription().equals(cursor.getString(1))){
           if(exercise.getExecution().equals(cursor.getString(2))){
               if(exercise.getImgAltURL().equals(cursor.getString(3))){
                   if(exercise.getImgMainURL().equals(cursor.getString(4))){
                       if(concatenarLista(exercise.getEquipment()).equals(cursor.getString(5))){
                           if(concatenarLista(exercise.getLevel()).equals(cursor.getString(6))){
                               if(concatenarLista(exercise.getMuscles()).equals(cursor.getString(7))){
                                   db.close();
                                   res.add(resEA);
                                   res.add(Long.parseLong(cursor.getString(0))); // Poner el id como segundo item de la lista
                                   cursor.close();
                                   return res;
                               }
                           }
                       }
                   }
               }
           }
        }
        db.close();
        cursor.close();
        res.add(Long.parseLong(cursor.getString(0))); // retornar el id >= 0 para indicar que esta en la BD y se necesita actualizar;
        return  res;

    }

    public String concatenarLista(String [] lista) {
        String res = "";
        int len = lista.length;
        for(String elem : lista){
            res += elem;
            len --;
            if(len == 1){
                res += " y ";
            }
            else if(len != 0){
                res += ", ";
            }
        }
        return res;
    }

    public String [] revertirConcatenacion(String lista){
        String [] res;
        res = lista.split(", ");
        int l = res.length;

        if(res[l-1].contains(" y ")){

            String [] lastSplit = res[l-1].split(" y ");
            res[l-1] = lastSplit[0];
            res = Arrays.copyOf(res,l+1);
            res[l] = lastSplit[1];
        }
        return res;
    }

}
