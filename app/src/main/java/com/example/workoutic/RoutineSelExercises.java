package com.example.workoutic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.workoutic.data.WorkouticDBHelper;
import com.example.workoutic.models.ExerciseRoutineAdapter;
import com.example.workoutic.models.ExercisesModel;
import com.example.workoutic.models.ExercisesRoutineModel;
import com.example.workoutic.util.DatabasesUtil;
import com.example.workoutic.util.DownloadExercisesCategoryLevelThreads;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class RoutineSelExercises extends AppCompatActivity {
    private String category;
    private String fitnessLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routine_sel_exercises);

        Intent i = getIntent();

        category = i.getStringExtra("category");
        fitnessLevel = i.getStringExtra("fitnessLevel");

        TextView txt = findViewById(R.id.txt_routine_sel_exerc_category_level);
        String t = category + " y " + fitnessLevel;
        txt.setText(t);

        String description;
        ImageView imgCategory = findViewById(R.id.img_routine_sel_exerc_principal);

        // Inicializar el listview
        ListView lv = findViewById(R.id.lv_routine_sel_exerc);
        lv.setAdapter(new ExerciseRoutineAdapter(new LinkedList<ExercisesRoutineModel>(), new LinkedList<ExercisesModel>()));

        // Listener de cada item en el listview. Llama a la actividad de Nuevo Ejercicio
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ExercisesRoutineModel exerciseRoutine = (ExercisesRoutineModel) adapterView.getAdapter().getItem(i);
                ExercisesModel exercise = (ExercisesModel) ((ExerciseRoutineAdapter) adapterView.getAdapter()).findIdExercises(exerciseRoutine.getExercise_id());
                exerciseRoutine.setDayOfWeek(getIntent().getStringExtra("day"));
                if(view.getId() != R.id.ly_exercise_routine_especific_exercise){
                    Intent intNewExercise = new Intent(getApplicationContext(),ExercisesManage.class);
                    intNewExercise.putExtra("exerciseRoutine",exerciseRoutine);
                    intNewExercise.putExtra("exercise",exercise);
                    intNewExercise.putExtra("category",category);
                    intNewExercise.putExtra("fitnessLevel",fitnessLevel);
                    intNewExercise.putExtra("caller","RoutineSelExercises");
                    startActivity(intNewExercise);
                }else{
                    Intent intSpeExercise = new Intent(getApplicationContext(),SelExerEspecific.class);
                    intSpeExercise.putExtra("caller","RoutineSelExercises");
                    intSpeExercise.putExtra("exerciseRoutine",exerciseRoutine);
                    intSpeExercise.putExtra("exercise",exercise);
                    intSpeExercise.putExtra("category",category);
                    intSpeExercise.putExtra("day",getIntent().getStringExtra("day"));
                    intSpeExercise.putExtra("fitnessLevel",fitnessLevel);
                    startActivity(intSpeExercise);
                }
            }
        });

        // Asignar la imagen principal y sus descripciones
        switch (category){
            case Exercises.ARMS:
                description = getString(R.string.img_arms_descrip);
                imgCategory.setContentDescription(description);
                imgCategory.setImageBitmap(drawableToBitmap(getDrawable(R.drawable.brazos)));
                break;
            case Exercises.GLUTES:
                description = getString(R.string.img_glutes_descrip);
                imgCategory.setContentDescription(description);
                imgCategory.setImageBitmap(drawableToBitmap(getDrawable(R.drawable.glutes)));
                break;
            case Exercises.ABS:
                description = getString(R.string.img_abs_descrip);
                imgCategory.setContentDescription(description);
                imgCategory.setImageBitmap(drawableToBitmap(getDrawable(R.drawable.abdominales)));
                break;
            case Exercises.LEGS:
                description = getString(R.string.img_legs_descrip);
                imgCategory.setContentDescription(description);
                imgCategory.setImageBitmap(drawableToBitmap(getDrawable(R.drawable.piernas)));
                break;
            case Exercises.TOR:
                description = getString(R.string.img_back_descrip);
                imgCategory.setContentDescription(description);
                imgCategory.setImageBitmap(drawableToBitmap(getDrawable(R.drawable.espalda)));
        }

        ///////////// DOWNLOAD EXERCISES ///////////////
        downloadExercises();
    }

    public void goMain(View view) {
        WorkouticDBHelper dbExtra = new WorkouticDBHelper(this, DatabasesUtil.NR_DATABASE_NAME,null,DatabasesUtil.NR_DATABASE_VERSION);
        dbExtra.deleteDB(); // borrar la BD extra
        Intent intMain = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intMain);
    }

    public void goMenu(View view) {
        Intent intMenu = new Intent(getApplicationContext(),Menu.class);
        intMenu.putExtra("category",category);
        intMenu.putExtra("day",getIntent().getStringExtra("day"));
        intMenu.putExtra("fitnessLevel",fitnessLevel);
        intMenu.putExtra("caller","RoutineSelExercises");
        startActivity(intMenu);
    }

    public void goBack(View view) {
        Intent intent = new Intent(getApplicationContext(),FitnessLevelSelection.class);
        intent.putExtra("day",getIntent().getStringExtra("day"));
        intent.putExtra("category",category);
        startActivity(intent);
    }
    // Descargar los datos del servidor y actualizar las vistas
    public void downloadExercises() {
        DownloadExercisesCategoryLevelThreads download = new DownloadExercisesCategoryLevelThreads(RoutineSelExercises.this);
        Thread th = new Thread(download);
        th.start();
    }
    public void prepareForDownloadExercises() {
        ListView lv = findViewById(R.id.lv_routine_sel_exerc);
        ProgressBar pb = findViewById(R.id.progressBar_routine_sel_exerc);
        lv.setVisibility(View.GONE);
        pb.setVisibility(View.VISIBLE);
    }

    public void updateExercises(List<ExercisesModel> exercisesAsList) {
        ListView lv = findViewById(R.id.lv_routine_sel_exerc);

       ((ExerciseRoutineAdapter)lv.getAdapter()).updateExercises(getExercisesRoutine(exercisesAsList),exercisesAsList);

        ProgressBar pb = findViewById(R.id.progressBar_routine_sel_exerc);
        pb.setVisibility(View.GONE);
        lv.setVisibility(View.VISIBLE);
    }

    // Metodos auxiliares
    public static Bitmap drawableToBitmap (Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable)drawable).getBitmap();
        }

        int width = drawable.getIntrinsicWidth();
        width = width > 0 ? width : 1;
        int height = drawable.getIntrinsicHeight();
        height = height > 0 ? height : 1;

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }
    public String getCategoria() {
        return this.category.toLowerCase();
    }

    public String getLevel() {
        return this.fitnessLevel.toLowerCase();
    }

    private List<ExercisesRoutineModel> getExercisesRoutine(List<ExercisesModel> eList){
        List<ExercisesRoutineModel> exercisesRoutinesList = new LinkedList<ExercisesRoutineModel>();
        int id = 1;
        for (ExercisesModel e : eList){
            List<Double> datos;
            if(fitnessLevel.equals(FitnessLevelSelection.ROOKIE)){
                if(!(e.getName().equals("Plancha"))){
                    datos = getRandomRookie(false);
                }else{
                    datos = getRandomRookie(true);
                }
            }else if(fitnessLevel.equals(FitnessLevelSelection.MEDIUM)){
                if(!(e.getName().equals("Plancha"))){
                    datos = getRandomMedium(false);
                }else{
                    datos = getRandomMedium(true);
                }
            }else{
                if(!(e.getName().equals("Plancha"))){
                    datos = getRandomExpert(false);
                }else{
                    datos = getRandomExpert(true);
                }
            }
            ExercisesRoutineModel er = new ExercisesRoutineModel();
            er.setId(id);
            id++;
            er.setSeries(Integer.parseInt(String.valueOf(Math.round(datos.get(0)))));
            er.setRepetitions(Integer.parseInt(String.valueOf(Math.round(datos.get(1)))));
            er.setWeightKg(datos.get(2));
            er.setTimeMinutes(datos.get(3));
            er.setExercise_id(e.getId());

            exercisesRoutinesList.add(er);
        }
        return exercisesRoutinesList;
    }

    private List<Double> getRandomRookie(boolean time){

        List<Double> datosRecomendados = new ArrayList<Double>();
        Random ran = new Random();
        double series = 0;
        boolean valido = false;

        if(!time){
            while(!valido){
                series = ran.nextInt(4);
                if(series>1){
                    valido = true;
                }
            }
            datosRecomendados.add(series);
            if(series == 2){
                datosRecomendados.add(10.0);
            }else{
                datosRecomendados.add(8.0);
            }
            datosRecomendados.add(0.0);
            datosRecomendados.add(0.0);
        }else{
            datosRecomendados.add(1.0); // 1 serie
            datosRecomendados.add(0.0); // 0 repeticiones
            datosRecomendados.add(0.0); // peso
            datosRecomendados.add(0.5); // minutos
        }

        return  datosRecomendados;
    }

    private List<Double> getRandomMedium(boolean time){

        List<Double> datosRecomendados = new ArrayList<Double>();
        Random ran = new Random();
        double series = 0;
        boolean valido = false;

        if(!time){
            while(!valido){
                series = ran.nextInt(5);
                if(series>2){
                    valido = true;
                }
            }
            datosRecomendados.add(series);
            if(series == 3){
                datosRecomendados.add(20.0);
            }else{
                datosRecomendados.add(15.0);
            }
            datosRecomendados.add(5.0);
            datosRecomendados.add(0.0);
        }else{
            datosRecomendados.add(2.0); // 1 series
            datosRecomendados.add(0.0); // 0 repeticiones
            datosRecomendados.add(0.0); // peso
            datosRecomendados.add(1.0); // minutos
        }
        return  datosRecomendados;
    }

    private List<Double> getRandomExpert(boolean time){

        List<Double> datosRecomendados = new ArrayList<Double>();
        Random ran = new Random();
        double series = 0;
        boolean valido = false;

        if(!time){
            while(!valido){
                series = ran.nextInt(6);
                if(series>2){
                    valido = true;
                }
            }
            datosRecomendados.add(series);
            if(series == 4){
                datosRecomendados.add(30.0);
            }else{
                datosRecomendados.add(25.0);
            }
            datosRecomendados.add(20.0);
            datosRecomendados.add(0.0);
        }else{
            datosRecomendados.add(3.0); // 1 series
            datosRecomendados.add(0.0); // 0 repeticiones
            datosRecomendados.add(0.0); // peso
            datosRecomendados.add(2.0); // minutos
        }
        return  datosRecomendados;
    }
}