package com.example.workoutic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

public class Exercises extends AppCompatActivity {
    public static final String CHEST = "Pecho";
    public static final String SHOULDER = "Hombros";
    public static final String ABS = "Abdominales";
    public static final String ARMS = "Brazos";
    public static final String WEIST = "Caderas";
    public static final String QUADS = "Cuádriceps";
    public static final String CALVES = "Gemelos";
    public static final String DELT = "Deltoides";
    public static final String ADUCT = "Aductores";
    public static final String GLUTES = "Glúteos";
    public static final String BACK = "Espalda";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercises_grid);
    }

    public void goBack(View view) {
        Intent intMain = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intMain);
    }

    public void goMenu(View view) {
        Intent intMenu = new Intent(getApplicationContext(),Menu.class);
        intMenu.putExtra("caller","Exercises");
        startActivity(intMenu);
    }

    public void goSelectionExercises(String caller){
        Intent intSelExercises = new Intent(getApplicationContext(),SelectionExercises.class);
        intSelExercises.putExtra("caller",caller);
        startActivity(intSelExercises);
    }

    public void goChest(View view) {
        TextView txt_view = (TextView) view;
        txt_view.setTextColor(getColor(R.color.brand_c1));
        goSelectionExercises(Exercises.CHEST);
    }

    public void goShoulder(View view) {
        TextView txt_view = (TextView) view;
        txt_view.setTextColor(getColor(R.color.brand_c1));
        goSelectionExercises(Exercises.SHOULDER);
    }

    public void goAbs(View view) {
        TextView txt_view = (TextView) view;
        txt_view.setTextColor(getColor(R.color.brand_c1));
        goSelectionExercises(Exercises.ABS);
    }

    public void goArms(View view) {
        TextView txt_view = (TextView) view;
        txt_view.setTextColor(getColor(R.color.brand_c1));
        goSelectionExercises(Exercises.ARMS);
    }

    public void goWaist(View view) {
        TextView txt_view = (TextView) view;
        txt_view.setTextColor(getColor(R.color.brand_c1));
        goSelectionExercises(Exercises.WEIST);
    }

    public void goQuads(View view) {
        TextView txt_view = (TextView) view;
        txt_view.setTextColor(getColor(R.color.brand_c1));
        goSelectionExercises(Exercises.QUADS);
    }

    public void goCalves(View view) {
        TextView txt_view = (TextView) view;
        txt_view.setTextColor(getColor(R.color.brand_c1));
        goSelectionExercises(Exercises.CALVES);
    }

    public void goDelt(View view) {
        TextView txt_view = (TextView) view;
        txt_view.setTextColor(getColor(R.color.brand_c1));
        goSelectionExercises(Exercises.DELT);
    }

    public void goGlutes(View view) {
        TextView txt_view = (TextView) view;
        txt_view.setTextColor(getColor(R.color.brand_c1));
        goSelectionExercises(Exercises.GLUTES);
    }

    public void goAduct(View view) {
        TextView txt_view = (TextView) view;
        txt_view.setTextColor(getColor(R.color.brand_c1));
        goSelectionExercises(Exercises.ADUCT);
    }

     public void goBackExercise(View view) {
         TextView txt_view = (TextView) view;
         txt_view.setTextColor(getColor(R.color.brand_c1));
        goSelectionExercises(Exercises.BACK);
    }
}