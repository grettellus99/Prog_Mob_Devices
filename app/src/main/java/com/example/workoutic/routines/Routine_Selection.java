package com.example.workoutic.routines;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.workoutic.MainActivity;
import com.example.workoutic.Menu;
import com.example.workoutic.R;
import com.example.workoutic.adapters.ExerciseRoutineViewAdapter;
import com.example.workoutic.database.WorkouticDBHelper;
import com.example.workoutic.exercises.SelExerEspecific;
import com.example.workoutic.models.ExercisesRoutineModel;
import com.example.workoutic.models.RoutineModel;
import com.example.workoutic.adapters.RoutinesAdapter;
import com.example.workoutic.util.DatabasesUtil;

import java.util.LinkedList;
import java.util.List;

public class Routine_Selection extends AppCompatActivity {

    ListView lv;
    ProgressBar pb;
    public static final String MOD_VIEW = "Ver";
    public static final String MOD_MOD = "Modificar";
    String modo;
    SearchView sv;

    TextView headerLv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routine_selection);
        pb = findViewById(R.id.progressBar_routine_selection);

        // Inicializando list view
        headerLv = findViewById(R.id.txt_header_liv_no_item_routine);
        headerLv.setVisibility(View.GONE);


        lv = findViewById(R.id.lv_routine_sel_routines);
        lv.setAdapter(new RoutinesAdapter(new LinkedList<RoutineModel>()));

        // Inicializar Search View
        sv = findViewById(R.id.sv_routine_selection_routines);
        int id = sv.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        TextView textView = (TextView) sv.findViewById(id);
        textView.setHintTextColor(getColor(R.color.hint));
        textView.setTextColor(getColor(R.color.secondary_MenuBar));

        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return onQueryTextChange(s);
            }
            @Override
            public boolean onQueryTextChange(String s) {
                ((RoutinesAdapter)lv.getAdapter()).getFilter().filter(s);
                return true;
            }
        });

        Intent i = getIntent();
        if(i.getStringExtra("callerActivity") != null && i.getStringExtra("callerActivity").equals("SelExerEspecific")){
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
                    intent.putExtra("caller",in.getStringExtra("caller"));
                    intent.putExtra("callerActivity","SelExerEspecific");
                    startActivity(intent);
                }
            }
        });
       getRoutines(0);
    }
    public void goMenu(View view) {
        Intent intMenu= new Intent(getApplicationContext(), Menu.class);
        intMenu.putExtra("caller","RoutineSelection");
        if(modo.equals(MOD_MOD)){
            Intent i = getIntent();
            intMenu.putExtra("exerciseRoutine",i.getSerializableExtra("exerciseRoutine"));
            intMenu.putExtra("exercise",i.getSerializableExtra("exercise"));
            intMenu.putExtra("caller2",i.getStringExtra("caller"));
            intMenu.putExtra("callerActivity","SelExerEspecific");
        }
        startActivity(intMenu);
    }

    public void goMain(View view) {
        Intent intMain = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intMain);
    }

    public void goBack(View view) {
        if(modo.equals(MOD_MOD)){
            Intent intent = new Intent(getApplicationContext(), SelExerEspecific.class);
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
        List<RoutineModel> r = DBhelper.getAllRoutines(met);

        if(r.size() == 0){
            pb.setVisibility(View.GONE);
            headerLv.setVisibility(View.VISIBLE);
            lv.setVisibility(View.GONE);

        }
        ((RoutinesAdapter) lv.getAdapter()).updateRoutines(r);

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