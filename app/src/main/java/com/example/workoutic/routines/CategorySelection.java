package com.example.workoutic.routines;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.example.workoutic.MainActivity;
import com.example.workoutic.Menu;
import com.example.workoutic.R;
import com.example.workoutic.database.WorkouticDBHelper;
import com.example.workoutic.exercises.Exercises;
import com.example.workoutic.util.DatabasesUtil;

public class CategorySelection extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cathegory_selection);
    }

    public void goMenu(View view) {
        Intent intMenu = new Intent(getApplicationContext(), Menu.class);
        intMenu.putExtra("caller","CategorySelection");
        intMenu.putExtra("day",getIntent().getStringExtra("day"));
        startActivity(intMenu);
    }

    public void goMain(View view) {
        WorkouticDBHelper dbExtra = new WorkouticDBHelper(this, DatabasesUtil.NR_DATABASE_NAME,null,DatabasesUtil.NR_DATABASE_VERSION);
        dbExtra.deleteDB(); // borrar la BD extra
        removeNumElmSP();
        Intent intMain = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intMain);
    }

    public void goBack(View view) {
        Intent intent = new Intent(getApplicationContext(),NewRoutine.class);
        startActivity(intent);
    }

    public void goArms(View view) {
        Intent intent = new Intent(getApplicationContext(),FitnessLevelSelection.class);
        intent.putExtra("day",getIntent().getStringExtra("day"));
        intent.putExtra("category", Exercises.ARMS);
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


    // Shared preferences de num de elementos
    private long getNumElmSP(){
        // Obtiene la cantidad de elementos
        SharedPreferences sp = getSharedPreferences(NewRoutine.PREF, Context.MODE_PRIVATE);
        return sp.getLong("numElem",-1);
    }
    private void removeNumElmSP(){
        // Elimina la cant de elementos
        if(getNumElmSP() != -1){
            SharedPreferences sp = getSharedPreferences(NewRoutine.PREF, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.remove("numElem");
            editor.apply();
        }
    }
}