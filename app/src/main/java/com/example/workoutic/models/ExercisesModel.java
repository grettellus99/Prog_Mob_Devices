package com.example.workoutic.models;

import android.graphics.Bitmap;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ExercisesModel implements Serializable {
    public static int N_EXERCISES = 0;
    private int id;
    @SerializedName("nombre")
    private String name;
    @SerializedName("dificultad")
    private String [] level;
    @SerializedName("equipamiento")
    private String [] equipment;
    @SerializedName("musculos")
    private String [] muscles;
    @SerializedName("descripcion")
    private String description;
    @SerializedName("ejecucion")
    private String execution;
    @SerializedName("imagenURL")
    private String imgAltURL;
    @SerializedName("img_principal")
    private String imgMainURL;

    private transient Bitmap imageMain;
    private transient Bitmap imageAlt;

    public ExercisesModel() {
        this.id = N_EXERCISES;
        ExercisesModel.N_EXERCISES+=1;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String[] getLevel() {
        return level;
    }

    public void setLevel(String[] level) {
        this.level = level;
    }

    public String[] getEquipment() {
        return equipment;
    }

    public void setEquipment(String[] equipment) {
        this.equipment = equipment;
    }

    public String[] getMuscles() {
        return muscles;
    }

    public void setMuscles(String[] muscles) {
        this.muscles = muscles;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getExecution() {
        return execution;
    }

    public void setExecution(String execution) {
        this.execution = execution;
    }

    public String getImgAltURL() {
        return imgAltURL;
    }

    public void setImgAltURL(String imgAltURL) {
        this.imgAltURL = imgAltURL;
    }

    public String getImgMainURL() {
        return imgMainURL;
    }

    public void setImgMainURL(String imgMainURL) {
        this.imgMainURL = imgMainURL;
    }

    public Bitmap getImageMain() {
        return imageMain;
    }

    public void setImageMain(Bitmap imageMain) {
        this.imageMain = imageMain;
    }

    public Bitmap getImageAlt() {
        return imageAlt;
    }

    public void setImageAlt(Bitmap imageAlt) {
        this.imageAlt = imageAlt;
    }
}
