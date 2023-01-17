package com.example.workoutic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.workoutic.data.WorkouticDBHelper;
import com.example.workoutic.models.ExercisesModel;
import com.example.workoutic.models.ExercisesRoutineModel;
import com.example.workoutic.models.RoutineModel;
import com.example.workoutic.util.DatabasesUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class NewRoutine extends AppCompatActivity {
    private LinearLayout editName;
    private LinearLayout showName;
    public static final String PREF_ROUTINE = "new_routine";

    Map<String, List<ExercisesRoutineModel>> exercises;

    RoutineModel routine;

    public static final String MODO_MOD = "Modificar";
    public static final String MODO_ADD = "Adicionar";

    public static final String PREF = "pref_size_exercises";
    String modo;
    Long size;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_routine);

        size = 0L;
        exercises =  new HashMap<String,List<ExercisesRoutineModel>>();;
        routine = new RoutineModel();

        Intent i = getIntent();
        if(!(i.getStringExtra("caller").equals("Routine_Main"))){
            if(i.getLongExtra("numElem",-1) != -1){
                modo = MODO_MOD;
                // Guardar la cant de elementos para evitar volverlos a adicionar a la BD al final de la mod de la rutina
                SharedPreferences sp = getSharedPreferences(PREF,Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putLong("numElem",getIntent().getLongExtra("numElem",-1));
                editor.apply();

            }else{
                modo = MODO_ADD;
            }
            WorkouticDBHelper dbExtra = new WorkouticDBHelper(this, DatabasesUtil.NR_DATABASE_NAME,null,DatabasesUtil.NR_DATABASE_VERSION);
            initializeExercisesRoutine(dbExtra);
        }

        editName = findViewById(R.id.ly_routine_name_edit);
        editName.setVisibility(View.VISIBLE);
        showName = findViewById(R.id.ly_routine_name_display);
        showName.setVisibility(View.GONE);
    }

    public void goMain(View view) {
        WorkouticDBHelper dbExtra = new WorkouticDBHelper(this, DatabasesUtil.NR_DATABASE_NAME,null,DatabasesUtil.NR_DATABASE_VERSION);
        dbExtra.deleteDB(); // borrar la BD extra
        Intent intMain = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intMain);
    }

    public void goMenu(View view) {
        Intent intMenu = new Intent(getApplicationContext(),Menu.class);
        intMenu.putExtra("caller","New_Routine");
        startActivity(intMenu);

    }

    public void goBack(View view) {
        WorkouticDBHelper dbExtra = new WorkouticDBHelper(this, DatabasesUtil.NR_DATABASE_NAME,null,DatabasesUtil.NR_DATABASE_VERSION);
        dbExtra.deleteDB(); // borrar la BD extra
        Intent intRoutineMain = new Intent(getApplicationContext(),Routine_Main.class);
        startActivity(intRoutineMain);
    }

    public void addRoutine(View view) {

        WorkouticDBHelper dbHelper = new WorkouticDBHelper(this, DatabasesUtil.DATABASE_NAME,null,DatabasesUtil.DATABASE_VERSION);
        WorkouticDBHelper dbHelperExtra = new WorkouticDBHelper(this,DatabasesUtil.NR_DATABASE_NAME,null,DatabasesUtil.NR_DATABASE_VERSION);

        long idR = routine.getId(); // Si la rutina es nueva se cambia por el nuevo id mas adelante. Pero si es mod este es el id que estaba en la BD

        if(modo.equals(MODO_MOD)){
            SharedPreferences sp = getSharedPreferences(PREF,Context.MODE_PRIVATE);
            size = sp.getLong("numELem",-1);
        }else{

            idR = dbHelper.addRoutine(routine,false);
        }
        for (String k : exercises.keySet()){
           for (ExercisesRoutineModel e : Objects.requireNonNull(exercises.get(k))){
                if(modo.equals(MODO_MOD) && size > 0){
                    size-=1;
                }else{
                    ExercisesModel exer = dbHelperExtra.getExercise(e.getExercise_id());
                    long idE = dbHelper.addExercises(exer,false);
                    e.setExercise_id(idE);
                    e.setRoutine_id(idR);
                    dbHelper.addExerciseRoutine(e,false);
                }
           }
        }

        if(modo.equals(MODO_MOD)){
            Toast.makeText(this, R.string.msg_sucsess_rout_mod, Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, R.string.msg_sucsess_rout_add, Toast.LENGTH_SHORT).show();
        }
        goMenu(null);
    }

    public void validateName(View view) {
        EditText name = (EditText) findViewById(R.id.txt_edit_new_routine_name_routine);
        String nombreRutina = name.getText().toString();

        if(isNotVoid(nombreRutina)){
            if(routine.getTimestamp() == null){ // Solo entrara una vez, cuando se valida el nombre la primera vez, siempre tendrá datos si se paso por aqui una vez.
                routine.setTimestamp(routine.dateTimeToLong());
                routine.setName(nombreRutina);
                addRoutineDBExtra();
            }
            routine.setName(nombreRutina);

            Button btn = findViewById(R.id.btn_new_routine_add_routine);
            btn.setAlpha(1);
            btn.setClickable(true);

            TextView txt = findViewById(R.id.txt_new_routine_routine_name_display);
            txt.setText(nombreRutina);

            showName.setVisibility(View.VISIBLE);
            editName.setVisibility(View.GONE);
        }
        else{
            Toast.makeText(this, getResources().getString(R.string.validate_name_error_msg), Toast.LENGTH_SHORT).show();
        }

    }

    private boolean isNotVoid(String text){
        int l = text.length();
        if(text.replaceAll(" ","").equals("")){
            return false;
        }
        return true;
    }

    public void editName(View view) {
        EditText name = (EditText) findViewById(R.id.txt_edit_new_routine_name_routine);
        name.setText(routine.getName());

        Button btn = findViewById(R.id.btn_new_routine_add_routine);
        btn.setAlpha(0.6F);
        btn.setClickable(false);

        editName.setVisibility(View.VISIBLE);
        showName.setVisibility(View.GONE);
    }

    public void newExercise(View view) {
        if(routine.getName() != null) {
            Intent intent = new Intent(getApplicationContext(),CategorySelection.class);
            String day = "";
            if(view.getId() == R.id.btn_new_routine_new_monday){
                day = "Lunes";
            }else if(view.getId() == R.id.btn_new_routine_new_tuesday){
                day = "Martes";
            }else if(view.getId() == R.id.btn_new_routine_new_wednesday){
                day = "Miércoles";
            }else if(view.getId() == R.id.btn_new_routine_new_thursday){
                day = "Jueves";
            }else if(view.getId() == R.id.txt_new_routine_count_friday){
                day = "Viernes";
            }else if(view.getId() == R.id.btn_new_routine_new_saturday){
                day = "Sábado";
            }else if(view.getId() == R.id.txt_new_routine_count_sunday){
                day = "Domingo";
            }
            intent.putExtra("day",day);
            startActivity(intent);
        }else{
            Toast.makeText(this, getResources().getString(R.string.msg_nombre_rutina_toast), Toast.LENGTH_SHORT).show();
        }
    }


    private void initializeExercisesRoutine(WorkouticDBHelper helper){

        routine = helper.getFirstRoutine(); // Obtener la primera rutina que esta en la bd extra q debe ser la unica

        exercises.clear();
        exercises.put("Lunes",new ArrayList<ExercisesRoutineModel>());
        exercises.put("Martes",new ArrayList<ExercisesRoutineModel>());
        exercises.put("Miércoles",new ArrayList<ExercisesRoutineModel>());
        exercises.put("Jueves",new ArrayList<ExercisesRoutineModel>());
        exercises.put("Viernes",new ArrayList<ExercisesRoutineModel>());
        exercises.put("Sábado",new ArrayList<ExercisesRoutineModel>());
        exercises.put("Domingo",new ArrayList<ExercisesRoutineModel>());

        for (String k : exercises.keySet()){
            List<ExercisesRoutineModel> list = new ArrayList<ExercisesRoutineModel>();
            list = helper.getAllExercisesRoutinesDays(k,routine.getId());
            if(list.size()>0){
                Objects.requireNonNull(exercises.get(k)).addAll(list);
            }
        }
        actualCount();
    }

    private void actualCount(){
        TextView t = findViewById(R.id.txt_new_routine_count_monday);
        t.setText(Objects.requireNonNull(exercises.get("Lunes")).size());

        t = findViewById(R.id.txt_new_routine_count_tuesday);
        t.setText(Objects.requireNonNull(exercises.get("Martes")).size());

        t = findViewById(R.id.txt_new_routine_count_wednesday);
        t.setText(Objects.requireNonNull(exercises.get("Miércoles")).size());

        t = findViewById(R.id.txt_new_routine_count_thursday);
        t.setText(Objects.requireNonNull(exercises.get("Jueves")).size());

        t = findViewById(R.id.txt_new_routine_count_friday);
        t.setText(Objects.requireNonNull(exercises.get("Viernes")).size());

        t = findViewById(R.id.txt_new_routine_count_saturday);
        t.setText(Objects.requireNonNull(exercises.get("Sábado")).size());

        t = findViewById(R.id.txt_new_routine_count_sunday);
        t.setText(Objects.requireNonNull(exercises.get("Domingo")).size());
    }

    private void addRoutineDBExtra(){
        WorkouticDBHelper dbExtra = new WorkouticDBHelper(this, DatabasesUtil.NR_DATABASE_NAME,null,DatabasesUtil.NR_DATABASE_VERSION);
        long id = dbExtra.addRoutine(routine,false);
        routine.setId(id);
    }
}