package com.example.workoutic;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.workoutic.activity.MessageActivity;
import com.example.workoutic.activity.UserActivity;
import com.example.workoutic.data.WorkouticDBHelper;
import com.example.workoutic.models.ExercisesModel;
import com.example.workoutic.models.RoutineModel;
import com.example.workoutic.util.DatabasesUtil;

import java.util.ArrayList;
import java.util.List;

public class Menu extends AppCompatActivity {
    private String caller;
    List<String> casesDeleteBD;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_sidebar);
        Intent i = getIntent();
        caller= i.getStringExtra("caller");
        casesDeleteBD = new ArrayList<String>();
        casesDeleteBD.add("New_Routine");
        casesDeleteBD.add("CategorySelection");
        casesDeleteBD.add("FitnessLevel");
        casesDeleteBD.add("RoutineSelExercises");
        casesDeleteBD.add("ExercisesManage");
    }
    public void goBack(View view) {
        Intent i;
        switch (caller){
            case "MainActivity":
                Intent intMain = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intMain);
                break;
            case "Exercises":
                Intent intExercises = new Intent(getApplicationContext(),Exercises.class);
                startActivity(intExercises);
                break;
            case "SelectionExercises":
                Intent intSelExercises = new Intent(getApplicationContext(),SelectionExercises.class);
                i = getIntent();
                intSelExercises.putExtra("caller",i.getStringExtra("category"));
                startActivity(intSelExercises);
                break;
            case "EspecificExercises":
                Intent intExercEspec = new Intent(getApplicationContext(),SelExerEspecific.class);
                i = getIntent();
                intExercEspec.putExtra("exercise",(ExercisesModel) i.getSerializableExtra("exercise"));
                String fit = i.getStringExtra("fitnessLevel");
                RoutineModel rout = (RoutineModel) i.getSerializableExtra("routine");
                if(fit != null || !fit.equals("")){
                    intExercEspec.putExtra("caller","RoutineSelExercises");
                    intExercEspec.putExtra("exerciseRoutine",i.getSerializableExtra("exerciseRoutine"));
                    intExercEspec.putExtra("fitnessLevel",fit);
                    intExercEspec.putExtra("day",i.getStringExtra("day"));
                    intExercEspec.putExtra("category",i.getStringExtra("category"));
                }else if(rout != null){
                    intExercEspec.putExtra("routine",rout);
                    if(i.getStringExtra("callerActivity").equals("SelExerEspecific")){
                        intExercEspec.putExtra("callerActivity",i.getStringExtra("SelExerEspecific"));
                        intExercEspec.putExtra("caller",i.getStringExtra("caller2"));
                        intExercEspec.putExtra("exercise",i.getSerializableExtra("exercise"));
                    }else{
                        intExercEspec.putExtra("caller","RoutineEspecific");
                    }
                    intExercEspec.putExtra("exerciseRoutine",i.getSerializableExtra("exerciseRoutine"));
                } else{
                    intExercEspec.putExtra("caller",i.getStringExtra("category"));
                }
                startActivity(intExercEspec);
            case "RoutineMain":
                Intent intRoutine = new Intent(getApplicationContext(),Routine_Main.class);
                startActivity(intRoutine);
            case "RoutineSelection":
                Intent intRoutSel = new Intent(getApplicationContext(),Routine_Selection.class);
                Intent in = getIntent();
                if(in.getStringExtra("callerActivity").equals("SelExerEspecific")){
                    intRoutSel.putExtra("exerciseRoutine",in.getSerializableExtra("exerciseRoutine"));
                    intRoutSel.putExtra("exercise",in.getSerializableExtra("exercise"));
                    intRoutSel.putExtra("caller",in.getStringExtra("caller"));
                    intRoutSel.putExtra("callerActivity","SelExerEspecific");
                }
                startActivity(intRoutSel);
            case "RoutineEspecific":
                Intent routEspe = new Intent(getApplicationContext(),RoutineEspecific.class);
                routEspe.putExtra("routine",getIntent().getStringExtra("routine"));
                startActivity(routEspe);
            case "New_Routine":
                Intent intNewRoutine = new Intent(getApplicationContext(),NewRoutine.class);
                startActivity(intNewRoutine);
            case "CategorySelection":
                Intent intCategory = new Intent(getApplicationContext(),CategorySelection.class);
                intCategory.putExtra("day",getIntent().getStringExtra("day"));
                startActivity(intCategory);
            case "FitnessLevel":
                Intent intFit = new Intent(getApplicationContext(),FitnessLevelSelection.class);
                i = getIntent();
                intFit.putExtra("category",i.getStringExtra("category"));
                intFit.putExtra("day",i.getStringExtra("day"));
                startActivity(intFit);
            case "RoutineSelExercises":
                Intent intRoutSelExer = new Intent(getApplicationContext(),RoutineSelExercises.class);
                i = getIntent();
                intRoutSelExer.putExtra("category",i.getStringExtra("category"));
                intRoutSelExer.putExtra("day",i.getStringExtra("day"));
                i.putExtra("fitnessLevel",i.getStringExtra("fitnessLevel"));
                startActivity(intRoutSelExer);
            case "ExercisesManage":
                Intent intExercisManage = new Intent(getApplicationContext(),ExercisesManage.class);
                intExercisManage.putExtra("exerciseRoutine",getIntent().getSerializableExtra("exerciseRoutine"));
                intExercisManage.putExtra("exercise",getIntent().getSerializableExtra("exercise"));
                intExercisManage.putExtra("category",getIntent().getStringExtra("category"));
                intExercisManage.putExtra("fitnessLevel",getIntent().getStringExtra("fitnessSelection"));
                startActivity(intExercisManage);
            case "Login":
                Intent intLog = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intLog);
            case "Register":
                Intent intReg = new Intent(getApplicationContext(),RegisterActivity.class);
                startActivity(intReg);
            case "User":
                Intent intUser = new Intent(getApplicationContext(), UserActivity.class);
                startActivity(intUser);
            case "Message":
                Intent intMessage = new Intent(getApplicationContext(), MessageActivity.class);
                startActivity(intMessage);
        }
    }

    public void goExercises(View view) {
        if(casesDeleteBD.contains(caller)){
            WorkouticDBHelper dbExtra = new WorkouticDBHelper(this, DatabasesUtil.NR_DATABASE_NAME,null,DatabasesUtil.NR_DATABASE_VERSION);
            dbExtra.deleteDB(); // borrar la BD extra
        }
        Intent intExercises = new Intent(getApplicationContext(),Exercises.class);
        startActivity(intExercises);
    }

    public void goMain(View view) {
        if(casesDeleteBD.contains(caller)){
            WorkouticDBHelper dbExtra = new WorkouticDBHelper(this, DatabasesUtil.NR_DATABASE_NAME,null,DatabasesUtil.NR_DATABASE_VERSION);
            dbExtra.deleteDB(); // borrar la BD extra
        }
        Intent intMain = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intMain);
    }

    public void goLogin(View view) {
        if(casesDeleteBD.contains(caller)){
            WorkouticDBHelper dbExtra = new WorkouticDBHelper(this, DatabasesUtil.NR_DATABASE_NAME,null,DatabasesUtil.NR_DATABASE_VERSION);
            dbExtra.deleteDB(); // borrar la BD extra
        }
        Intent intLog = new Intent(getApplicationContext(),LoginActivity.class);
        startActivity(intLog);
    }

    public void goRoutine(View view) {
        if(casesDeleteBD.contains(caller)){
            WorkouticDBHelper dbExtra = new WorkouticDBHelper(this, DatabasesUtil.NR_DATABASE_NAME,null,DatabasesUtil.NR_DATABASE_VERSION);
            dbExtra.deleteDB(); // borrar la BD extra
        }
        Intent intRoutine = new Intent(getApplicationContext(),Routine_Main.class);
        startActivity(intRoutine);
    }

    public void goChat(View view) {
        if(casesDeleteBD.contains(caller)){
            WorkouticDBHelper dbExtra = new WorkouticDBHelper(this, DatabasesUtil.NR_DATABASE_NAME,null,DatabasesUtil.NR_DATABASE_VERSION);
            dbExtra.deleteDB(); // borrar la BD extra
        }

        //////////////////// TODO /////////////////////
    }
}
