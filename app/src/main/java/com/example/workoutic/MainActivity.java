package com.example.workoutic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity {
    private View exercisesView;
    private View routineView;
    private View progressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }


    public void goExercises(View view) {
        Intent intExercises = new Intent(getApplicationContext(),Exercises.class);
        startActivity(intExercises);
    }

    public void goMenu(View view) {
        Intent intMenu= new Intent(getApplicationContext(),Menu.class);
        intMenu.putExtra("caller","MainActivity");
        startActivity(intMenu);
    }
}