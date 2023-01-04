package com.example.workoutic;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.workoutic.models.ExercisesModel;

public class Menu extends AppCompatActivity {
    private String caller;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_sidebar);
        Intent i = getIntent();
        caller= i.getStringExtra("caller");

    }


    public void goBack(View view) {
        switch (caller){
            case "MainActivity":
                Intent intMain = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intMain);
                break;
            case "Exercises":
                Intent intExercises = new Intent(getApplicationContext(),Exercises.class);
                startActivity(intExercises);
                break;
            case "SelectionExercises":
                Intent intSelExercises = new Intent(getApplicationContext(),SelectionExercises.class);
                Intent i = getIntent();
                String category = i.getStringExtra("category");
                intSelExercises.putExtra("caller",category);
                startActivity(intSelExercises);
                break;
            case "EspecificExercises":
                Intent intExercEspec = new Intent(getApplicationContext(),SelExerEspecific.class);
                Intent i2 = getIntent();
                ExercisesModel exercise = (ExercisesModel) i2.getSerializableExtra("exercise");
                String cateExer = i2.getStringExtra("category");
                intExercEspec.putExtra("exercise",exercise);
                intExercEspec.putExtra("caller",cateExer);
                startActivity(intExercEspec);
        }
    }

    public void goExercises(View view) {
        Intent intExercises = new Intent(getApplicationContext(),Exercises.class);
        startActivity(intExercises);
    }

    public void goMain(View view) {
        Intent intMain = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intMain);
    }
}
