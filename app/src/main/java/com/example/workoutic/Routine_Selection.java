package com.example.workoutic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Routine_Selection extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routine_selection);
    }

    public void goMenu(View view) {
        Intent intMenu= new Intent(getApplicationContext(),Menu.class);
        intMenu.putExtra("caller","RoutineSelection");
        startActivity(intMenu);
    }

    public void goMain(View view) {
        Intent intMain = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intMain);
    }

    public void goBack(View view) {
        Intent intRoutineMain = new Intent(getApplicationContext(),Routine_Main.class);
        startActivity(intRoutineMain);
    }
}