package com.example.workoutic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.workoutic.data.WorkouticDBHelper;
import com.example.workoutic.util.DatabasesUtil;

public class FitnessLevelSelection extends AppCompatActivity {
    public static final String ROOKIE = "Novato";
    public static final String MEDIUM = "Medio";
    public static final String EXPERT = "Experto";
    private String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fitness_level_selection);
        Intent i = getIntent();
        category = i.getStringExtra("category");

    }

    public void goMain(View view) {
        WorkouticDBHelper dbExtra = new WorkouticDBHelper(this, DatabasesUtil.NR_DATABASE_NAME,null,DatabasesUtil.NR_DATABASE_VERSION);
        dbExtra.deleteDB(); // borrar la BD extra
        Intent intMain = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intMain);
    }

    public void goMenu(View view) {
        Intent intMenu = new Intent(getApplicationContext(),Menu.class);
        intMenu.putExtra("category",category);
        intMenu.putExtra("day",getIntent().getStringExtra("day"));
        intMenu.putExtra("caller","FitnessLevel");
        startActivity(intMenu);
    }

    public void goBack(View view) {
        Intent intent = new Intent(getApplicationContext(),CategorySelection.class);
        startActivity(intent);
    }

    public void goRookie(View view) {
        Intent intent = new Intent(getApplicationContext(),RoutineSelExercises.class);
        intent.putExtra("category",category);
        intent.putExtra("day",getIntent().getStringExtra("day"));
        intent.putExtra("fitnessLevel",ROOKIE);
        startActivity(intent);

    }

    public void goMedium(View view) {
        Intent intent = new Intent(getApplicationContext(),RoutineSelExercises.class);
        intent.putExtra("category",category);
        intent.putExtra("day",getIntent().getStringExtra("day"));
        intent.putExtra("fitnessLevel",MEDIUM);
        startActivity(intent);
    }

    public void goExpert(View view) {
        Intent intent = new Intent(getApplicationContext(),RoutineSelExercises.class);
        intent.putExtra("category",category);
        intent.putExtra("day",getIntent().getStringExtra("day"));
        intent.putExtra("fitnessLevel",EXPERT);
        startActivity(intent);
    }
}