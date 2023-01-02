package com.example.workoutic.util;

import android.graphics.Bitmap;
import java.util.Arrays;
import java.util.Locale;

import com.example.workoutic.SelectionExercises;
import com.example.workoutic.models.ExercisesModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class DownloadExercisesThreads implements Runnable{
    private SelectionExercises actSE;
    private String url = "https://us-central1-workoutic-db.cloudfunctions.net/app/ejercicios/";

    public DownloadExercisesThreads(SelectionExercises act){
        this.actSE = act;
    }

    @Override
    public void run() {
        actSE.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                actSE.prepareForDownloadExercises();
            }
        });
        String jsonExercises = "";
        try {
            String extraURL = "";
            if(SelectionExercises.EQUIPAMIENTO){
                extraURL += actSE.getCategoria().toLowerCase() + "/eq/equipamiento";
            }else{
                extraURL += actSE.getCategoria().toLowerCase() + "/eq/no_equipamiento";
            }
            jsonExercises = NetUtil.getURLText(url+extraURL);
        } catch (Exception e) {
            e.printStackTrace();
        }
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("dd/MM/yyyy hh:mm a");
        Gson gson = gsonBuilder.create();
        ExercisesModel [] exercisesData = gson.fromJson(jsonExercises,ExercisesModel[].class);

        for (ExercisesModel e : exercisesData){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Bitmap imagenAlt = NetUtil.getURLBitmap(e.getImgAltURL());
                        e.setImageMain(imagenAlt);
                        actSE.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                actSE.updateExercises(Arrays.asList(exercisesData));
                            }
                        });
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });
        }
        actSE.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                actSE.updateExercises(Arrays.asList(exercisesData));
            }
        });
    }
}
