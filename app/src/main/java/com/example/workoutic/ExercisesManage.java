package com.example.workoutic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.workoutic.data.WorkouticDBHelper;
import com.example.workoutic.models.ExercisesModel;
import com.example.workoutic.models.ExercisesRoutineModel;
import com.example.workoutic.util.DatabasesUtil;

public class ExercisesManage extends AppCompatActivity {
    ExercisesRoutineModel exerciseRoutine;
    ExercisesModel exercise;
    EditText series;
    EditText rep;
    EditText weight;
    EditText time;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercises_manage);

        Intent i = getIntent();
        exerciseRoutine = (ExercisesRoutineModel) i.getSerializableExtra("exerciseRoutine");
        exercise = (ExercisesModel) i.getSerializableExtra("exercise");

        // inicializacion del ejercicio
        TextView name = findViewById(R.id.txt_exercises_manage_exercise);
        name.setText(exercise.getName());
        TextView category = findViewById(R.id.txt_exercises_manage_category);
        category.setText(i.getStringExtra("category"));

        // Inicializacion con datos recomendados
        series = findViewById(R.id.txt_exercises_manage_series);
        series.setText(exerciseRoutine.getSeries());
        rep = findViewById(R.id.txt_exercises_manage_repetitions);
        rep.setText(exerciseRoutine.getRepetitions());
        weight = findViewById(R.id.txt_exercises_manage_weight);
        weight.setText(String.valueOf(exerciseRoutine.getWeightKg()));
        time = findViewById(R.id.txt_exercises_manage_time);
        time.setText(String.valueOf(exerciseRoutine.getTimeMinutes()));

    }

    public void goBack(View view) {
        Intent intent = new Intent(getApplicationContext(),RoutineSelExercises.class);
        intent.putExtra("category",getIntent().getStringExtra("category"));
        intent.putExtra("fitnessSelection",getIntent().getStringExtra("fitnessSelection"));
        startActivity(intent);
    }

    public void goMenu(View view) {
        Intent intMenu = new Intent(getApplicationContext(),Menu.class);
        intMenu.putExtra("exerciseRoutine",exerciseRoutine);
        intMenu.putExtra("exercise",exercise);
        intMenu.putExtra("category",getIntent().getStringExtra("category"));
        intMenu.putExtra("fitnessLevel",getIntent().getStringExtra("fitnessSelection"));
        intMenu.putExtra("caller","ExercisesManage");
        startActivity(intMenu);
    }

    public void goMain(View view) {
        WorkouticDBHelper dbExtra = new WorkouticDBHelper(this, DatabasesUtil.NR_DATABASE_NAME,null,DatabasesUtil.NR_DATABASE_VERSION);
        dbExtra.deleteDB(); // borrar la BD extra
        Intent intMain = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intMain);
    }

    public void addExercise(View view) {
        String seriesTxt = series.getText().toString();
        if(isNumberInteger(seriesTxt)){
            exerciseRoutine.setSeries(Integer.parseInt(seriesTxt));
            String repTxt = rep.getText().toString();
            if(isNumberInteger(repTxt)){
                exerciseRoutine.setRepetitions(Integer.parseInt(repTxt));
                String weightTxt = weight.getText().toString();
                if(isNumberDouble(weightTxt) || isNumberInteger(weightTxt)){
                    exerciseRoutine.setWeightKg(Double.parseDouble(weightTxt));
                    String timeTxt = time.getText().toString();
                    if(isNumberDouble(timeTxt) || isNumberInteger(timeTxt)){
                        exerciseRoutine.setTimeMinutes(Double.parseDouble(timeTxt));
                        WorkouticDBHelper helper = new WorkouticDBHelper(this,DatabasesUtil.NR_DATABASE_NAME,null,DatabasesUtil.NR_DATABASE_VERSION);
                        long idE = helper.addExercises(exercise); // Agregar el ejercicio a la BD extra
                        exerciseRoutine.setExercise_id(idE);
                        helper.addExerciseRoutine(exerciseRoutine); // Agregar el EjercicioRutina a la BD extra

                        // Nueva actividad de Nueva Rutina
                        Intent i = new Intent(this,NewRoutine.class);
                        i.putExtra("caller","ExercisesManage");
                        startActivity(i);
                    }else{
                        Toast.makeText(this, R.string.msg_err_time_no_number, Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(this, R.string.msg_err_peso_no_number, Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(this, R.string.msg_err_rep_no_int, Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this, R.string.msg_err_series_no_int, Toast.LENGTH_SHORT).show();
        }

    }

    public void addSeries(View view) {
        String seriesTxt = series.getText().toString();
        if(isNumberInteger(seriesTxt)){
            int s = Integer.parseInt(seriesTxt);
            s +=1;
            series.setText(s);
        }else{
            Toast.makeText(this, R.string.msg_err_series_no_int, Toast.LENGTH_SHORT).show();
        }
    }

    public void decreaseSeries(View view) {
        String seriesTxt = series.getText().toString();
        if(isNumberInteger(seriesTxt)){
            int s = Integer.parseInt(seriesTxt);
            if(s>0){
                s -=1;
            }else{
                s=0;
            }
            series.setText(s);
        }else{
            Toast.makeText(this, R.string.msg_err_series_no_int, Toast.LENGTH_SHORT).show();
        }
    }

    public void decreaseRep(View view) {
        String repTxt = rep.getText().toString();
        if(isNumberInteger(repTxt)){
            int r = Integer.parseInt(repTxt);
            if(r>0){
                r -=1;
            }else{
                r=0;
            }
            rep.setText(r);
        }else{
            Toast.makeText(this, R.string.msg_err_rep_no_int, Toast.LENGTH_SHORT).show();
        }

    }

    public void addRep(View view) {
        String repTxt = rep.getText().toString();
        if(isNumberInteger(repTxt)){
            int r = Integer.parseInt(repTxt);
            r +=1;
            rep.setText(r);
        }else{
            Toast.makeText(this, R.string.msg_err_rep_no_int, Toast.LENGTH_SHORT).show();
        }
    }

    public void decreaseWeight(View view) {
        String weightTxt = weight.getText().toString();
        if(isNumberDouble(weightTxt) || isNumberInteger(weightTxt)){
            double w = Double.parseDouble(weightTxt);
            if(w>0){
                w -=1;
            }else{
                w=0;
            }
            weight.setText(String.valueOf(w));
        }else{
            Toast.makeText(this, R.string.msg_err_peso_no_number, Toast.LENGTH_SHORT).show();
        }
    }

    public void addWeight(View view) {
        String weightTxt = weight.getText().toString();
        if(isNumberDouble(weightTxt) || isNumberInteger(weightTxt)){
            double w = Double.parseDouble(weightTxt);
            w+=1;
            weight.setText(String.valueOf(w));
        }else{
            Toast.makeText(this, R.string.msg_err_peso_no_number, Toast.LENGTH_SHORT).show();
        }
    }

    public void decreaseTime(View view) {
        String timeTxt = time.getText().toString();
        if(isNumberDouble(timeTxt) || isNumberInteger(timeTxt)){
            double t = Double.parseDouble(timeTxt);
            if(t>0){
                t -=1;
            }else{
                t=0;
            }
            time.setText(String.valueOf(t));
        }else{
            Toast.makeText(this, R.string.msg_err_time_no_number, Toast.LENGTH_SHORT).show();
        }
    }

    public void addTime(View view) {
        String timeTxt = time.getText().toString();
        if(isNumberDouble(timeTxt) || isNumberInteger(timeTxt)){
            double t = Double.parseDouble(timeTxt);
            t+=1;
            time.setText(String.valueOf(t));
        }else{
            Toast.makeText(this, R.string.msg_err_time_no_number, Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isNumberDouble(String n){
        try{
            Double.parseDouble(n);
            return  true;
        }catch (NumberFormatException e){
            return  false;
        }
    }

    private boolean isNumberInteger(String n){
        try{
            Integer.parseInt(n);
            return  true;
        }catch (NumberFormatException e){
            return  false;
        }
    }

}