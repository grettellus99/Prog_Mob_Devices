package com.example.workoutic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.example.workoutic.data.WorkouticDBHelper;
import com.example.workoutic.models.ExercisesModel;
import com.example.workoutic.models.RoutineModel;
import com.example.workoutic.models.RoutinesAdapter;
import com.example.workoutic.util.DatabasesUtil;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Routine_Selection extends AppCompatActivity {

    ListView lv;
    ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routine_selection);

        lv = findViewById(R.id.lv_routine_sel_routines);
        lv.setAdapter(new RoutinesAdapter(new LinkedList<RoutineModel>()));

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                RoutineModel routines = (RoutineModel) adapterView.getAdapter().getItem(i);
                Intent intRoutineSp = new Intent(getApplicationContext(),RoutineEspecific.class);
                intRoutineSp.putExtra("routine",routines);
                startActivity(intRoutineSp);
            }
        });
        getRoutines(0);
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

    public void getRoutines(int met){
        // Ocultar el lv hasta que se recuperen los datos
        lv.setVisibility(View.GONE);
        pb.setVisibility(View.VISIBLE);

        WorkouticDBHelper DBhelper = new WorkouticDBHelper(this, DatabasesUtil.DATABASE_NAME, null, DatabasesUtil.DATABASE_VERSION);
        ((RoutinesAdapter) lv.getAdapter()).updateRoutines(DBhelper.getAllRoutines(met));

        pb.setVisibility(View.GONE);
        lv.setVisibility(View.VISIBLE);
    }

    public void getRoutinesMethod(View view) {
        float rot = view.getRotation();
        if(rot == 0){
            getRoutines(1);
            view.setRotation(180);
        }else{
            getRoutines(0);
            view.setRotation(0);
        }
    }
}