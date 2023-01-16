package com.example.workoutic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.workoutic.data.WorkouticDBHelper;
import com.example.workoutic.util.DatabasesUtil;

public class CategorySelection extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cathegory_selection);
    }

    public void goMenu(View view) {
        Intent intMenu = new Intent(getApplicationContext(),Menu.class);
        intMenu.putExtra("caller","CategorySelection");
        intMenu.putExtra("day",getIntent().getStringExtra("day"));
        startActivity(intMenu);
    }

    public void goMain(View view) {
        WorkouticDBHelper dbExtra = new WorkouticDBHelper(this, DatabasesUtil.NR_DATABASE_NAME,null,DatabasesUtil.NR_DATABASE_VERSION);
        dbExtra.deleteDB(); // borrar la BD extra
        Intent intMain = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intMain);
    }

    public void goBack(View view) {
        Intent intent = new Intent(getApplicationContext(),NewRoutine.class);
        startActivity(intent);
    }

    public void goArms(View view) {
        Intent intent = new Intent(getApplicationContext(),FitnessLevelSelection.class);
        intent.putExtra("day",getIntent().getStringExtra("day"));
        intent.putExtra("category",Exercises.ARMS);
        startActivity(intent);
    }

    public void goTorso(View view) {
        Intent intent = new Intent(getApplicationContext(),FitnessLevelSelection.class);
        intent.putExtra("day",getIntent().getStringExtra("day"));
        intent.putExtra("category",Exercises.TOR);
        startActivity(intent);
    }

    public void goAbs(View view) {
        Intent intent = new Intent(getApplicationContext(),FitnessLevelSelection.class);
        intent.putExtra("day",getIntent().getStringExtra("day"));
        intent.putExtra("category",Exercises.ABS);
        startActivity(intent);
    }

    public void goLegs(View view) {
        Intent intent = new Intent(getApplicationContext(),FitnessLevelSelection.class);
        intent.putExtra("day",getIntent().getStringExtra("day"));
        intent.putExtra("category",Exercises.LEGS);
        startActivity(intent);
    }

    public void goGlutes(View view) {
        Intent intent = new Intent(getApplicationContext(),FitnessLevelSelection.class);
        intent.putExtra("day",getIntent().getStringExtra("day"));
        intent.putExtra("category",Exercises.GLUTES);
        startActivity(intent);
    }
}