package com.example.workoutic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.workoutic.exercises.Exercises;
import com.example.workoutic.routines.Routine_Main;
import com.example.workoutic.signin_signup.LoginActivity;
import com.example.workoutic.signin_signup.RegisterActivity;



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
        Intent intExercises = new Intent(getApplicationContext(), Exercises.class);
        startActivity(intExercises);
    }

    public void goMenu(View view) {
        Intent intMenu= new Intent(getApplicationContext(),Menu.class);
        intMenu.putExtra("caller","MainActivity");
        startActivity(intMenu);
    }

    public void goRegister(View view) {
        Intent intUser = new Intent(getApplicationContext(), RegisterActivity.class);
        startActivity(intUser);
    }

    public void goRoutines(View view) {
        Intent intRoutines= new Intent(getApplicationContext(), Routine_Main.class);
        startActivity(intRoutines);
    }

    public void goChat(View view) {
        Intent intChat= new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intChat);
    }
}