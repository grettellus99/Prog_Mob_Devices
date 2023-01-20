package com.example.workoutic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.workoutic.data.WorkouticDBHelper;
import com.example.workoutic.models.ExercisesModel;
import com.example.workoutic.models.ExercisesRoutineModel;
import com.example.workoutic.models.RoutineModel;
import com.example.workoutic.util.DatabasesUtil;

public class ExercisesManage extends AppCompatActivity {
    ExercisesRoutineModel exerciseRoutine;
    ExercisesModel exercise;
    EditText series;
    EditText rep;
    EditText weight;
    EditText time;
    public static final String MODE_MOD = "Modificar";
    public static final String MODE_ADD = "Adicionar";
    public static final String MODE_ADD_R = "Adicionar a rutina";
    String mode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercises_manage);

        Intent i = getIntent();
        exerciseRoutine = (ExercisesRoutineModel) i.getSerializableExtra("exerciseRoutine");
        exercise = (ExercisesModel) i.getSerializableExtra("exercise");

        if(i.getStringExtra("caller") != null && i.getStringExtra("caller").equals("RoutineEspecific")){
            mode = MODE_MOD;
            LinearLayout ly = findViewById(R.id.ly_exer_manage_categoria);
            ly.setVisibility(View.GONE);
            View div = findViewById(R.id.dv_exer_manage_categoria);
            div.setVisibility(View.GONE);
            ly = findViewById(R.id.ly_day_of_week);
            ly.setVisibility(View.GONE);
            div = findViewById(R.id.div_day_of_week);
            div.setVisibility(View.GONE);
        }else if(i.getStringExtra("callerActivity")!= null && i.getStringExtra("callerActivity").equals("SelExerEspecific")){
            mode = MODE_ADD_R;
            TextView txt = findViewById(R.id.txt_exercises_manage_day_of_week);
            txt.setText(RoutineEspecific.MONDAY);
            LinearLayout ly = findViewById(R.id.ly_exer_manage_categoria);
            ly.setVisibility(View.GONE);
            View div = findViewById(R.id.dv_exer_manage_categoria);
            div.setVisibility(View.GONE);
        } else{
            mode = MODE_ADD;
            LinearLayout ly = findViewById(R.id.ly_day_of_week);
            ly.setVisibility(View.GONE);
            View div = findViewById(R.id.div_day_of_week);
            div.setVisibility(View.GONE);
        }

        // inicializacion del ejercicio
        TextView name = findViewById(R.id.txt_exercises_manage_exercise);
        name.setText(exercise.getName());
        TextView category = findViewById(R.id.txt_exercises_manage_category);
        category.setText(i.getStringExtra("category"));

        // Inicializacion con datos recomendados
        series = findViewById(R.id.txt_exercises_manage_series);
        series.setText(String.valueOf(exerciseRoutine.getSeries()));
        rep = findViewById(R.id.txt_exercises_manage_repetitions);
        rep.setText(String.valueOf(exerciseRoutine.getRepetitions()));
        weight = findViewById(R.id.txt_exercises_manage_weight);
        weight.setText(String.valueOf(exerciseRoutine.getWeightKg()));
        time = findViewById(R.id.txt_exercises_manage_time);
        time.setText(String.valueOf(exerciseRoutine.getTimeMinutes()));

    }

    public void goBack(View view) {
        if(mode.equals(MODE_MOD)){
            Intent intent = new Intent(getApplicationContext(),RoutineEspecific.class);
            intent.putExtra("routine",getIntent().getSerializableExtra("routine"));
            startActivity(intent);
        }else if(mode.equals(MODE_ADD_R)){
            Intent i = getIntent();
            Intent intent = new Intent(this,Routine_Selection.class);
            intent.putExtra("exerciseRoutine",i.getSerializableExtra("exerciseRoutine"));
            intent.putExtra("exercise",exercise);
            intent.putExtra("caller",i.getStringExtra("caller"));
            intent.putExtra("callerActivity","SelExerEspecific");
            startActivity(intent);
        }
        else{
            Intent intent = new Intent(getApplicationContext(),RoutineSelExercises.class);
            intent.putExtra("category",getIntent().getStringExtra("category"));
            intent.putExtra("day",exerciseRoutine.getDayOfWeek());
            intent.putExtra("fitnessLevel",getIntent().getStringExtra("fitnessLevel"));
            startActivity(intent);
        }
    }

    public void goMenu(View view) {

        Intent intMenu = new Intent(getApplicationContext(),Menu.class);
        intMenu.putExtra("exerciseRoutine",exerciseRoutine);
        intMenu.putExtra("exercise",exercise);

        if(mode.equals(MODE_ADD)){
            intMenu.putExtra("category",getIntent().getStringExtra("category"));
            intMenu.putExtra("fitnessLevel",getIntent().getStringExtra("fitnessSelection"));
        }else if(mode.equals(MODE_ADD_R)){
            Intent i = getIntent();
            intMenu.putExtra("routine",i.getSerializableExtra("routine"));
            intMenu.putExtra("caller2",i.getSerializableExtra("caller"));
            intMenu.putExtra("callerActivity","SelExerEspecific");
        } else{
            intMenu.putExtra("routine",getIntent().getSerializableExtra("routine"));
        }
        intMenu.putExtra("caller","ExercisesManage");
        startActivity(intMenu);
    }

    public void goMain(View view) {
        if(mode.equals(MODE_ADD)){
            WorkouticDBHelper dbExtra = new WorkouticDBHelper(this, DatabasesUtil.NR_DATABASE_NAME,null,DatabasesUtil.NR_DATABASE_VERSION);
            dbExtra.deleteDB(); // borrar la BD extra
            removeNumElmSP();
        }
        Intent intMain = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intMain);
    }

    // Shared preferences de num de elementos
    private long getNumElmSP(){
        // Obtiene la cantidad de elementos
        SharedPreferences sp = getSharedPreferences(NewRoutine.PREF, Context.MODE_PRIVATE);
        return sp.getLong("numElem",-1);
    }
    private void removeNumElmSP(){
        // Elimina la cant de elementos
        if(getNumElmSP() != -1){
            SharedPreferences sp = getSharedPreferences(NewRoutine.PREF, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.remove("numElem");
            editor.apply();
        }
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
                        if(mode.equals(MODE_ADD)){
                            WorkouticDBHelper helper = new WorkouticDBHelper(this,DatabasesUtil.NR_DATABASE_NAME,null,DatabasesUtil.NR_DATABASE_VERSION);
                            long idE = helper.addExercises(exercise,false); // Agregar el ejercicio a la BD extra
                            RoutineModel rout = helper.getFirstRoutine(); // Obtener la rutina para obtener su id.
                            exerciseRoutine.setRoutine_id(rout.getId());
                            exerciseRoutine.setExercise_id(idE);
                            helper.addExerciseRoutine(exerciseRoutine,false); // Agregar el EjercicioRutina a la BD extra
                            // Nueva actividad de Nueva Rutina
                            Intent i = new Intent(this,NewRoutine.class);
                            i.putExtra("caller","ExercisesManage");
                            Toast.makeText(this, R.string.msg_sucsess_add, Toast.LENGTH_SHORT).show();
                            startActivity(i);
                        }else if(mode.equals(MODE_ADD_R)){
                            if(isDay()){
                                WorkouticDBHelper helper = new WorkouticDBHelper(this,DatabasesUtil.DATABASE_NAME,null,DatabasesUtil.DATABASE_VERSION);
                                helper.addExerciseRoutine(exerciseRoutine,false);
                                Toast.makeText(this, R.string.msg_sucsess_add, Toast.LENGTH_SHORT).show();
                                goMain(null);
                            }else{
                                Toast.makeText(this, R.string.msg_err_day, Toast.LENGTH_SHORT).show();
                            }
                        }
                        else{
                            WorkouticDBHelper helper = new WorkouticDBHelper(this,DatabasesUtil.DATABASE_NAME,null,DatabasesUtil.DATABASE_VERSION);
                            helper.updateExerciseRoutine(exerciseRoutine);
                            Intent intent = new Intent(getApplicationContext(),RoutineEspecific.class);
                            intent.putExtra("routine",getIntent().getSerializableExtra("routine"));
                            Toast.makeText(this, R.string.msg_sucsess_act, Toast.LENGTH_SHORT).show();
                            startActivity(intent);
                        }
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
            series.setText(String.valueOf(s));
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
            series.setText(String.valueOf(s));
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
            rep.setText(String.valueOf(r));
        }else{
            Toast.makeText(this, R.string.msg_err_rep_no_int, Toast.LENGTH_SHORT).show();
        }

    }

    public void addRep(View view) {
        String repTxt = rep.getText().toString();
        if(isNumberInteger(repTxt)){
            int r = Integer.parseInt(repTxt);
            r +=1;
            rep.setText(String.valueOf(r));
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

    public void addDay(View view) {
        TextView txt = findViewById(R.id.txt_exercises_manage_day_of_week);
        txt.setText(getDay(true));
    }

    public void decreaseDay(View view) {
        TextView txt = findViewById(R.id.txt_exercises_manage_day_of_week);
        txt.setText(getDay(false));
    }

    private String getDay(boolean next){
        TextView txt = findViewById(R.id.txt_exercises_manage_day_of_week);
        String day = txt.getText().toString();
        switch (day){
            case RoutineEspecific.MONDAY:
                if(next){
                    return RoutineEspecific.TUESDAY;
                }else{
                    return RoutineEspecific.SUNDAY;
                }
            case RoutineEspecific.TUESDAY:
                if(!next){
                    return RoutineEspecific.MONDAY;
                }else{
                    return RoutineEspecific.WEDNESDAY;
                }
            case RoutineEspecific.WEDNESDAY:
                if(!next){
                    return RoutineEspecific.TUESDAY;
                }else{
                    return RoutineEspecific.THURSDAY;
                }
            case RoutineEspecific.THURSDAY:
                if(!next){
                    return RoutineEspecific.WEDNESDAY;
                }else{
                    return RoutineEspecific.FRIDAY;
                }
            case RoutineEspecific.FRIDAY:
                if(!next){
                    return RoutineEspecific.THURSDAY;
                }else{
                    return RoutineEspecific.SATURDAY;
                }
            case RoutineEspecific.SATURDAY:
                if(!next){
                    return RoutineEspecific.FRIDAY;
                }else{
                    return RoutineEspecific.SUNDAY;
                }
            default:
                if(!next){
                    return RoutineEspecific.SATURDAY;
                }else{
                    return RoutineEspecific.MONDAY;
                }
        }
    }

    private boolean isDay(){
        TextView txt = findViewById(R.id.txt_exercises_manage_day_of_week);
        String day = txt.getText().toString();
        switch (day){
            case RoutineEspecific.MONDAY:
            case RoutineEspecific.TUESDAY:
            case RoutineEspecific.WEDNESDAY:
            case RoutineEspecific.THURSDAY:
            case RoutineEspecific.FRIDAY:
            case RoutineEspecific.SATURDAY:
            case RoutineEspecific.SUNDAY:
                exerciseRoutine.setDayOfWeek(day);
                return  true;
            default:
               return  false;
        }

    }
}