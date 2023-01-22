package com.example.workoutic.routines;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.workoutic.MainActivity;
import com.example.workoutic.Menu;
import com.example.workoutic.R;

public class Routine_Main extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routine_main);
    }

    public void goMenu(View view) {
        Intent intMenu= new Intent(getApplicationContext(), Menu.class);
        intMenu.putExtra("caller","RoutineMain");
        startActivity(intMenu);
    }

    public void goMain(View view) {
        Intent intMain = new Intent(getApplicationContext(), MainActivity.class);
        intMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intMain);
    }

    public void goBack(View view) {
        Intent intMain = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intMain);
    }

    public void newRoutine(View view) {
        Intent intNewRoutine = new Intent(this.getApplicationContext(),NewRoutine.class);
        intNewRoutine.putExtra("caller","Routine_Main");
        startActivity(intNewRoutine);
    }

    public void myRoutine(View view) {
        Intent intRoutSelec = new Intent(this.getApplicationContext(),Routine_Selection.class);
        startActivity(intRoutSelec);
    }
}