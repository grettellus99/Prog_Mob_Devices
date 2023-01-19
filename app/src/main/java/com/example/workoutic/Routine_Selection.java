package com.example.workoutic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.workoutic.data.WorkouticDBHelper;
import com.example.workoutic.models.ExercisesModel;
import com.example.workoutic.models.ExercisesRoutineModel;
import com.example.workoutic.models.RoutineModel;
import com.example.workoutic.models.RoutinesAdapter;
import com.example.workoutic.util.DatabasesUtil;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Routine_Selection extends AppCompatActivity {

    ListView lv;
    ProgressBar pb;
    public static final String MOD_VIEW = "Ver";
    public static final String MOD_MOD = "Modificar";
    String modo;
    SearchView sv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routine_selection);

        lv = findViewById(R.id.lv_routine_sel_routines);
        lv.setAdapter(new RoutinesAdapter(new LinkedList<RoutineModel>()));

        sv = findViewById(R.id.sv_routine_selection_routines);
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                ((RoutinesAdapter)lv.getAdapter()).getFilter().filter(s);
                return true;
            }
        });

        Intent i = getIntent();
        if(i.getStringExtra("callerActivity").equals("SelExerEspecific")){
            modo = MOD_MOD;
        }else{
            modo = MOD_VIEW;
        }

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                RoutineModel routines = (RoutineModel) adapterView.getAdapter().getItem(i);
                if(modo.equals(MOD_VIEW)){
                    Intent intRoutineSp = new Intent(getApplicationContext(),RoutineEspecific.class);
                    intRoutineSp.putExtra("routine",routines);
                    startActivity(intRoutineSp);
                }else{
                    Intent in = getIntent();
                    ExercisesRoutineModel er = (ExercisesRoutineModel) in.getSerializableExtra("exerciseRoutine");
                    er.setRoutine_id(((RoutineModel) adapterView.getAdapter().getItem(i)).getId());
                    er.setTimeMinutes(0);
                    er.setSeries(0);
                    er.setWeightKg(0);
                    er.setRepetitions(0);
                    Intent intent = new Intent(getApplicationContext(),ExercisesManage.class);
                    intent.putExtra("routine",routines);
                    intent.putExtra("exerciseRoutine",er);
                    intent.putExtra("exercise",in.getSerializableExtra("exercise"));
                    intent.putExtra("caller",in.getSerializableExtra("caller"));
                    intent.putExtra("callerActivity","SelExerEspecific");
                    startActivity(intent);
                }
            }
        });
        getRoutines(0);
    }


    public void goMenu(View view) {
        Intent intMenu= new Intent(getApplicationContext(),Menu.class);
        intMenu.putExtra("caller","RoutineSelection");
        if(modo.equals(MOD_MOD)){
            Intent i = getIntent();
            intMenu.putExtra("exerciseRoutine",i.getSerializableExtra("exerciseRoutine"));
            intMenu.putExtra("exercise",i.getSerializableExtra("exercise"));
            intMenu.putExtra("caller",i.getStringExtra("caller"));
            intMenu.putExtra("callerActivity","SelExerEspecific");
        }
        startActivity(intMenu);
    }

    public void goMain(View view) {
        Intent intMain = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intMain);
    }

    public void goBack(View view) {
        if(modo.equals(MOD_MOD)){
            Intent intent = new Intent(getApplicationContext(),SelExerEspecific.class);
            Intent i = getIntent();
            intent.putExtra("exerciseRoutine",i.getSerializableExtra("exerciseRoutine"));
            intent.putExtra("exercise",i.getSerializableExtra("exercise"));
            intent.putExtra("caller",i.getStringExtra("caller"));
            startActivity(intent);
        }else{
            Intent intRoutineMain = new Intent(getApplicationContext(),Routine_Main.class);
            startActivity(intRoutineMain);
        }

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