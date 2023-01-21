package com.example.workoutic.routines;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.workoutic.MainActivity;
import com.example.workoutic.Menu;
import com.example.workoutic.R;
import com.example.workoutic.database.WorkouticDBHelper;
import com.example.workoutic.exercises.SelExerEspecific;
import com.example.workoutic.adapters.ExerciseRoutineViewAdapter;
import com.example.workoutic.models.ExercisesModel;
import com.example.workoutic.models.ExercisesRoutineModel;
import com.example.workoutic.models.RoutineModel;
import com.example.workoutic.util.DatabasesUtil;
import com.example.workoutic.util.DeleteItemDialogFragment;
import com.example.workoutic.util.DeleteRoutineDialogFragment;
import com.example.workoutic.util.NetUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class RoutineEspecific extends AppCompatActivity {
    public static final String MONDAY = "Lunes";
    public static final String TUESDAY = "Martes";
    public static final String WEDNESDAY = "Miércoles";
    public static final String THURSDAY = "Jueves";
    public static final String FRIDAY = "Viernes";
    public static final String SATURDAY = "Sábado";
    public static final String SUNDAY = "Domingo";

    RoutineModel routine;
    String day;
    ListView lv;
    CardView menu;
    LinearLayout ly_data;

    TextView headerLv;

    private final static String MODE_MENU = "menu";
    private final static String MODE_DATA = "data";

    String mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routine_especific);
        Intent i = getIntent();
        routine = (RoutineModel) i.getSerializableExtra("routine");
        TextView txt = findViewById(R.id.txt_routine_esp_name_routine);
        txt.setText(routine.getName());
        day = MONDAY;

        // Inicializacion del menu de rutina
        menu = findViewById(R.id.cv_routine_esp_menu);
        ly_data = findViewById(R.id.ly_routine_esp_exercises_ly);
        mode = MODE_DATA;
        changeMode();

        // Inicializando list view
        headerLv = findViewById(R.id.txt_header_liv_no_item);
        headerLv.setVisibility(View.GONE);

        lv = findViewById(R.id.lv_routine_esp);
        lv.setVisibility(View.INVISIBLE);

        LayoutInflater inflater = getLayoutInflater();
        lv.setAdapter(new ExerciseRoutineViewAdapter(new LinkedList<ExercisesRoutineModel>(),new LinkedList<ExercisesModel>()));

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(view.getId() == R.id.ly_exercise_routine_especific_exercise_view){
                    Intent intent = new Intent(getApplicationContext(), SelExerEspecific.class);
                    intent.putExtra("routine",routine);
                    intent.putExtra("exercise",((ExerciseRoutineViewAdapter)lv.getAdapter()).getExercise(i));
                    intent.putExtra("caller","RoutineEspecific");
                    startActivity(intent);

                }else if(view.getId() == R.id.ic_exercises_routine_item_view_menu_view){
                    LinearLayout lyMenu = adapterView.getRootView().findViewById(R.id.ly_card_item_exercises_routine_menu_view);
                    LinearLayout lyData = adapterView.getRootView().findViewById(R.id.ly_card_item_exercises_routine_data_view);
                    if(lyMenu.getVisibility() == View.VISIBLE){
                        lyData.setVisibility(View.VISIBLE);
                        lyMenu.setVisibility(View.GONE);
                    }else{
                        lyMenu.setVisibility(View.VISIBLE);
                        lyData.setVisibility(View.GONE);
                    }
                }else if(view.getId() == R.id.btn_menu_item_exercise_routine_share){
                    ExercisesRoutineModel er = (ExercisesRoutineModel) adapterView.getAdapter().getItem(i);
                    ExercisesModel e = (ExercisesModel) ((ExerciseRoutineViewAdapter) adapterView.getAdapter()).getExercise(i);
                    Intent intShare = new Intent(Intent.ACTION_SEND);
                    intShare.setType("text/plain");
                    intShare.putExtra(Intent.EXTRA_TEXT,getExerciseFormatedToShare(e,er));
                    startActivity(intShare);
                }
                else if(view.getId() == R.id.btn_menu_item_exercise_routine_edit){
                    ExercisesRoutineModel er = (ExercisesRoutineModel) adapterView.getAdapter().getItem(i);
                    ExercisesModel exercisesModel = (ExercisesModel) ((ExerciseRoutineViewAdapter) adapterView.getAdapter()).getExercise(i);

                    Intent intent = new Intent(getApplicationContext(),ExercisesManage.class);
                    intent.putExtra("exercise",exercisesModel);
                    intent.putExtra("exerciseRoutine",er);
                    intent.putExtra("routine",routine);
                    intent.putExtra("caller","RoutineEspecific");
                    startActivity(intent);
                }
                else if(view.getId() == R.id.btn_menu_item_exercise_routine_delete){
                    ExercisesModel e = ((ExerciseRoutineViewAdapter) adapterView.getAdapter()).getExercise(i);
                    String title = "Estás seguro de que quieres eliminar el ejercicio";
                    String message = "Perderás todas las especificaciones introducidas para " + e.getName();

                    ProgressBar pb = findViewById(R.id.progressBar_routine_esp);
                    ExercisesRoutineModel er = (ExercisesRoutineModel) adapterView.getAdapter().getItem(i);

                    DialogFragment newDialog = new DeleteItemDialogFragment(message,title,getApplicationContext(),lv,pb,er,i);
                    newDialog.show(getSupportFragmentManager(),getString(R.string.tag_dialog_delete_er));

                }

            }
        });
       getExercisesFromDB();

    }

    public void goMain(View view) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    public void goMenu(View view) {
        Intent intent = new Intent(getApplicationContext(), Menu.class);
        intent.putExtra("caller","RoutineEspecific");
        intent.putExtra("routine",routine);
        startActivity(intent);
    }

    public void goBack(View view) {
        Intent intent = new Intent(getApplicationContext(),Routine_Selection.class);
        startActivity(intent);
    }

    public void toggleMenuRoutine(View view) {
        if(mode.equals(MODE_DATA)){
            mode = MODE_MENU;
        }else{
            mode = MODE_DATA;
        }
        changeMode();
    }

    public void shareRoutine(View view) {
        WorkouticDBHelper helper = new WorkouticDBHelper(this,DatabasesUtil.DATABASE_NAME,null,DatabasesUtil.DATABASE_VERSION);
        List<ExercisesRoutineModel> erList = helper.getAllExercisesRoutines(routine.getId());
        List<ExercisesModel> eList = new ArrayList<ExercisesModel>();
        Map<Long,Integer> ids =  new HashMap<Long,Integer>();
        String message = "";
        for(ExercisesRoutineModel exer : erList){
            int pos = ids.containsKey(exer.getExercise_id()) ? ids.get(exer.getExercise_id()) : -1;
            if(pos != -1){
                eList.add(eList.get(pos));
            }else{
                eList.add(helper.getExercise(exer.getExercise_id()));
                ids.put(exer.getExercise_id(), (eList.size() - 1));
            }
            message += getExerciseFormatedToShare(eList.get(eList.size() - 1),exer); // Formar el mensaje final con todos los ejercicios de la rutina
        }
        Intent intShare = new Intent(Intent.ACTION_SEND);
        intShare.setType("text/plain");
        String subject = "Rutina : " + routine.getName() + "\nFecha de creación: " + routine.LongToDateTimeString();
        intShare.putExtra(Intent.EXTRA_SUBJECT,subject);
        intShare.putExtra(Intent.EXTRA_TEXT,message);
        startActivity(intShare);

    }

    public void modRoutine(View view) {
        WorkouticDBHelper dbHelper = new WorkouticDBHelper(this, DatabasesUtil.DATABASE_NAME,null,DatabasesUtil.DATABASE_VERSION);
        List<ExercisesRoutineModel> exeRoutList = dbHelper.getAllExercisesRoutines(routine.getId());
        List<ExercisesModel> exer = new LinkedList<ExercisesModel>();
        List<Long> ids = new LinkedList<Long>();
        for(ExercisesRoutineModel e : exeRoutList){
            if(!(ids.contains(e.getExercise_id()))){
                exer.add(dbHelper.getExercise(e.getExercise_id()));
                ids.add(e.getExercise_id());
            }
        }
        WorkouticDBHelper dbExtra = new WorkouticDBHelper(this, DatabasesUtil.NR_DATABASE_NAME,null,DatabasesUtil.NR_DATABASE_VERSION);
        dbExtra.addRoutine(routine,true);
        for(ExercisesRoutineModel e : exeRoutList){
            dbExtra.addExerciseRoutine(e,true);
            dbExtra.addExercises(findIdExercises(e.getExercise_id(),exer),true);
        }
        Intent intent = new Intent(getApplicationContext(),NewRoutine.class);
        intent.putExtra("numElem",Long.parseLong(String.valueOf(exeRoutList.size())));
        startActivity(intent);
    }

    public ExercisesModel findIdExercises(long id,List<ExercisesModel> exerList){
        for(ExercisesModel e : exerList){
            if(e.getId() == id){
                return e;
            }
        }
        return null;
    }

    public void deleteRoutine(View view) {
        String title = "Estás seguro de que quieres eliminar la rutina";
        String message = "Perderás todas las especificaciones introducidas para " + routine.getName();

        DialogFragment newDialog = new DeleteRoutineDialogFragment(message,title,getApplicationContext(),routine);
        newDialog.show(getSupportFragmentManager(),getString(R.string.tag_delete_routine));
    }

    public void getExercisesMonday(View view) {
        desactivarDiaActual();
        LinearLayout ly = findViewById(R.id.ly_routines_esp_monday_colored);
        ly.setBackgroundColor(getColor(R.color.primary_MenuBar));
        day = MONDAY;
        getExercisesFromDB();
    }

    public void getExercisesTuesday(View view) {
        desactivarDiaActual();
        LinearLayout ly = findViewById(R.id.ly_routines_esp_tuesday_colored);
        ly.setBackgroundColor(getColor(R.color.primary_MenuBar));
        day = TUESDAY;
        getExercisesFromDB();
    }

    public void getExercisesWednesday(View view) {
        desactivarDiaActual();
        LinearLayout ly = findViewById(R.id.ly_routines_esp_wednesday_colored);
        ly.setBackgroundColor(getColor(R.color.primary_MenuBar));
        day = WEDNESDAY;
        getExercisesFromDB();
    }

    public void getExercisesThursday(View view) {
        desactivarDiaActual();
        LinearLayout ly = findViewById(R.id.ly_routines_esp_thursday_colored);
        ly.setBackgroundColor(getColor(R.color.primary_MenuBar));
        day = THURSDAY;
        getExercisesFromDB();
    }

    public void getExercisesFriday(View view) {
        desactivarDiaActual();
        LinearLayout ly = findViewById(R.id.ly_routines_esp_friday_colored);
        ly.setBackgroundColor(getColor(R.color.primary_MenuBar));
        day = FRIDAY;
        getExercisesFromDB();
    }

    public void getExercisesSaturday(View view) {
        desactivarDiaActual();
        LinearLayout ly = findViewById(R.id.ly_routines_esp_saturday_colored);
        ly.setBackgroundColor(getColor(R.color.primary_MenuBar));
        day = SATURDAY;
        getExercisesFromDB();
    }

    public void getExercisesSunday(View view) {
        desactivarDiaActual();
        LinearLayout ly = findViewById(R.id.ly_routines_esp_sunday_colored);
        ly.setBackgroundColor(getColor(R.color.primary_MenuBar));
        day = SUNDAY;
        getExercisesFromDB();
    }

    public void desactivarDiaActual(){
        LinearLayout ly;
        if (day.equals(MONDAY)){
            ly = findViewById(R.id.ly_routines_esp_monday_colored);
        }else if(day.equals(TUESDAY)){
            ly = findViewById(R.id.ly_routines_esp_tuesday_colored);
        }else if(day.equals(WEDNESDAY)){
            ly = findViewById(R.id.ly_routines_esp_wednesday_colored);
        }else if(day.equals(THURSDAY)){
            ly = findViewById(R.id.ly_routines_esp_thursday_colored);
        }else if(day.equals(FRIDAY)){
            ly = findViewById(R.id.ly_routines_esp_friday_colored);
        }else if(day.equals(SATURDAY)){
            ly = findViewById(R.id.ly_routines_esp_saturday_colored);
        }else{
            ly = findViewById(R.id.ly_routines_esp_sunday_colored);
        }
        ly.setBackground(null);
    }

    public void getExercisesFromDB(){
        lv.setVisibility(View.INVISIBLE);
        headerLv.setVisibility(View.GONE);
        ProgressBar pb = findViewById(R.id.progressBar_routine_esp);
        pb.setVisibility(View.VISIBLE);

        WorkouticDBHelper helper = new WorkouticDBHelper(this,DatabasesUtil.DATABASE_NAME,null,DatabasesUtil.DATABASE_VERSION);

        List<ExercisesRoutineModel> exerRoutList = helper.getAllExercisesRoutinesDays(day,routine.getId());
        List<ExercisesModel> exercisesModList = new LinkedList<ExercisesModel>();

        if(exerRoutList.size() == 0){
            pb.setVisibility(View.GONE);
            headerLv.setVisibility(View.VISIBLE);
            lv.setVisibility(View.INVISIBLE);
            ((ExerciseRoutineViewAdapter) lv.getAdapter()).updateExercises(exerRoutList,exercisesModList);
        }else{
            for(ExercisesRoutineModel e : exerRoutList){
                exercisesModList.add(helper.getExercise(e.getExercise_id()));
            }
            RoutineEspecific re = this;
            for (ExercisesModel e : exercisesModList){
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Bitmap imgAlt;
                        try {
                            e.setImageAlt(NetUtil.getURLBitmap(e.getImgAltURL()));
                            re.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    re.updateExercises(exerRoutList,exercisesModList);
                                }
                            });
                        } catch (Exception ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }).start();
            }
        }
    }

    private void updateExercises(List<ExercisesRoutineModel> er,List<ExercisesModel> e){
        ((ExerciseRoutineViewAdapter) lv.getAdapter()).updateExercises(er,e);
        ProgressBar pb = findViewById(R.id.progressBar_routine_esp);
        pb.setVisibility(View.GONE);
        headerLv.setVisibility(View.GONE);
        lv.setVisibility(View.VISIBLE);
    }

    private void changeMode(){
        LinearLayout.LayoutParams p = (LinearLayout.LayoutParams) ly_data.getLayoutParams();
        if(mode.equals(MODE_MENU)){
            menu.setVisibility(View.VISIBLE);
            p.setMargins(0,-410,0,0);
        }else{
            menu.setVisibility(View.GONE);
            p.setMargins(0,0,0,0);
        }
    }

    private String getExerciseFormatedToShare(ExercisesModel e, ExercisesRoutineModel er){
        String message = "";
        message += "Nombre del ejercicio: " + e.getName() + "\n";
        message +="Descripción: " + e.getDescription();
        message += "\nDía de la semana: " + er.getDayOfWeek();
        message += "\nCantidad de: \n" + "\tSeries: " + er.getSeries();
        double time = er.getTimeMinutes();
        if(time > 0){
            message += "\n\tTiempo en min: " + er.getTimeMinutes();
        }else{
            message += "\n\tRepeticiones: " + er.getRepetitions();
            message += "\n\tPeso en kg: " + er.getWeightKg();
        }
        message +="\nEquipamiento: " + concatenarLista(e.getEquipment());
        message +="\nMúsculos: " + concatenarLista(e.getMuscles());
        message +="\nEjecución: " + e.getExecution();
        message +="\n";
        return  message;
    }

    private String concatenarLista(String [] lista) {
        String res = "";
        int len = lista.length;
        for(String elem : lista){
            res += elem;
            len --;
            if(len == 1){
                res += " y ";
            }
            else if(len != 0){
                res += ", ";
            }
        }
        return res;
    }



}