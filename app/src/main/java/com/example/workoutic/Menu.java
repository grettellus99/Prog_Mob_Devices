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
        Intent i;
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
                i = getIntent();
                intSelExercises.putExtra("caller",i.getStringExtra("category"));
                startActivity(intSelExercises);
                break;
            case "EspecificExercises":
                Intent intExercEspec = new Intent(getApplicationContext(),SelExerEspecific.class);
                i = getIntent();
                intExercEspec.putExtra("exercise",(ExercisesModel) i.getSerializableExtra("exercise"));
                String fit = i.getStringExtra("fitnessLevel");
                if(fit != null || fit.equals("")){
                    intExercEspec.putExtra("caller","RoutineSelExercises");
                    intExercEspec.putExtra("fitnessLevel",fit);
                    intExercEspec.putExtra("category",i.getStringExtra("category"));
                }else{
                    intExercEspec.putExtra("caller",i.getStringExtra("category"));
                }

                startActivity(intExercEspec);
            case "RoutineMain":
                Intent intRoutine = new Intent(getApplicationContext(),Routine_Main.class);
                startActivity(intRoutine);
            case "RoutineSelection":
                Intent intRoutSel = new Intent(getApplicationContext(),Routine_Selection.class);
                startActivity(intRoutSel);
            case "New_Routine":
                Intent intNewRoutine = new Intent(getApplicationContext(),NewRoutine.class);
                startActivity(intNewRoutine);
            case "CategorySelection":
                Intent intCategory = new Intent(getApplicationContext(),CategorySelection.class);
                startActivity(intCategory);
            case "FitnessLevel":
                Intent intFit = new Intent(getApplicationContext(),FitnessLevelSelection.class);
                i = getIntent();
                intFit.putExtra("category",i.getStringExtra("category"));
                startActivity(intFit);
            case "RoutineSelExercises":
                Intent intRoutSelExer = new Intent(getApplicationContext(),RoutineSelExercises.class);
                i = getIntent();
                intRoutSelExer.putExtra("category",i.getStringExtra("category"));
                i.putExtra("fitnessLevel",i.getStringExtra("fitnessLevel"));
                startActivity(intRoutSelExer);
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

    public void goLogin(View view) {
    }

    public void goRoutine(View view) {
        Intent intRoutine = new Intent(getApplicationContext(),Routine_Main.class);
        startActivity(intRoutine);
    }

    public void goChat(View view) {
    }
}
