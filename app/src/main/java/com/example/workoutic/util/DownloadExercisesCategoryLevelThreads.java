package com.example.workoutic.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;

import com.example.workoutic.R;
import com.example.workoutic.RoutineSelExercises;
import com.example.workoutic.SelectionExercises;
import com.example.workoutic.models.ExercisesModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

public class DownloadExercisesCategoryLevelThreads implements Runnable {
    private RoutineSelExercises actRSE;
    private String url = "https://us-central1-workoutic-db.cloudfunctions.net/app/ejercicios/";

    public DownloadExercisesCategoryLevelThreads(RoutineSelExercises act){
        this.actRSE = act;
    }

    @Override
    public void run() {
        actRSE.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                actRSE.prepareForDownloadExercises();
            }
        });
        String jsonExercises = downloadJSON();
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("dd/MM/yyyy hh:mm a");
        Gson gson = gsonBuilder.create();
        ExercisesModel[] exercisesData = gson.fromJson(jsonExercises,ExercisesModel[].class);
        for (ExercisesModel e : exercisesData){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Bitmap imagenAlt = NetUtil.getURLBitmap(e.getImgAltURL());
                        e.setImageAlt(imagenAlt);
                        actRSE.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                actRSE.updateExercises(Arrays.asList(exercisesData));
                            }
                        });
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }).start();
        }
        actRSE.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                actRSE.updateExercises(Arrays.asList(exercisesData));
            }
        });
    }

    private String downloadJSON() {
       String id = actRSE.getCategoria().toLowerCase();
        String extraURL = id + "/dif/" + actRSE.getLevel();
        id += "_"+actRSE.getLevel();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String dateActual = formatter.format(LocalDateTime.now());

        SharedPreferences sharedPrefExercises = actRSE.getSharedPreferences(id, Context.MODE_PRIVATE);

        String date =  sharedPrefExercises.getString("date",actRSE.getResources().getString(R.string.dont));

        String jsonExercises = "";

        // Comprueba que exista la entrada de fecha en las preferencias y si es del 'dia de hoy' para no tener que actualizar el JSON
        if(!date.equals(actRSE.getResources().getString(R.string.dont)) || date.equals(dateActual)){
            jsonExercises = sharedPrefExercises.getString(id,actRSE.getResources().getString(R.string.dont));
        }else{
            try {
                // Si no existe la entrada o no es del dia actual hace la peticion a la URL
                jsonExercises = NetUtil.getURLText(url+extraURL);

                // Guardar el nuevo JSON en las preferencias
                SharedPreferences.Editor editor = sharedPrefExercises.edit();
                editor.putString("date", dateActual);     // Adicionar fecha del 'dia de hoy'.
                editor.putString(id,jsonExercises);     // Adicionar valor del JSON
                editor.apply();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return jsonExercises;
    }



}
