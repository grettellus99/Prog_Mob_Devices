package com.example.workoutic;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.workoutic.chat.ChatActivity;
import com.example.workoutic.chat.MessageActivity;
import com.example.workoutic.chat.UserActivity;
import com.example.workoutic.database.WorkouticDBHelper;
import com.example.workoutic.exercises.Exercises;
import com.example.workoutic.exercises.SelExerEspecific;
import com.example.workoutic.exercises.SelectionExercises;
import com.example.workoutic.models.ExercisesModel;
import com.example.workoutic.models.RoutineModel;
import com.example.workoutic.routines.CategorySelection;
import com.example.workoutic.routines.ExercisesManage;
import com.example.workoutic.routines.FitnessLevelSelection;
import com.example.workoutic.routines.NewRoutine;
import com.example.workoutic.routines.RoutineEspecific;
import com.example.workoutic.routines.RoutineSelExercises;
import com.example.workoutic.routines.Routine_Main;
import com.example.workoutic.routines.Routine_Selection;
import com.example.workoutic.signin_signup.LoginActivity;
import com.example.workoutic.signin_signup.RegisterActivity;
import com.example.workoutic.util.DatabasesUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class Menu extends AppCompatActivity {
    private String caller;
    List<String> casesDeleteBD;

    TextView login;
    TextView logout;

    // Logged ?
    FirebaseAuth auth;
    FirebaseUser currentUser;
    FirebaseAuth.AuthStateListener authStateListener;

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

        login = findViewById(R.id.txt_menu_profile);
        logout = findViewById(R.id.txt_menu_log_out);

        auth = FirebaseAuth.getInstance();

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                currentUser = firebaseAuth.getCurrentUser();
                if(currentUser != null){
                   logout.setVisibility(View.VISIBLE);
                    login.setVisibility(View.GONE);
                }else{
                    login.setVisibility(View.VISIBLE);
                    logout.setVisibility(View.GONE);
                }
            }
        };

    }
    @Override
    protected void onStart() {
        super.onStart();
        currentUser = auth.getCurrentUser();
        auth.addAuthStateListener(authStateListener);
    }

    public void goBack(View view) {
        Intent i;
        switch (caller){
            case "MainActivity":
                Intent intMain = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intMain);
                break;
            case "Exercises":
                Intent intExercises = new Intent(getApplicationContext(), Exercises.class);
                startActivity(intExercises);
                break;
            case "SelectionExercises":
                Intent intSelExercises = new Intent(getApplicationContext(), SelectionExercises.class);
                i = getIntent();
                intSelExercises.putExtra("caller",i.getStringExtra("category"));
                startActivity(intSelExercises);
                break;
            case "EspecificExercises":
                Intent intExercEspec = new Intent(getApplicationContext(), SelExerEspecific.class);
                i = getIntent();
                intExercEspec.putExtra("exercise",(ExercisesModel) i.getSerializableExtra("exercise"));
                String fit = i.getStringExtra("fitnessLevel");
                RoutineModel rout = (RoutineModel) i.getSerializableExtra("routine");
                if(fit != null){
                    intExercEspec.putExtra("caller","RoutineSelExercises");
                    intExercEspec.putExtra("exerciseRoutine",i.getSerializableExtra("exerciseRoutine"));
                    intExercEspec.putExtra("fitnessLevel",fit);
                    intExercEspec.putExtra("day",i.getStringExtra("day"));
                    intExercEspec.putExtra("category",i.getStringExtra("category"));
                }else if(rout != null){
                    intExercEspec.putExtra("routine",rout);
                    if(i.getStringExtra("callerActivity")!= null && i.getStringExtra("callerActivity").equals("SelExerEspecific")){
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
                break;
            case "RoutineMain":
                Intent intRoutine = new Intent(getApplicationContext(), Routine_Main.class);
                startActivity(intRoutine);
                break;
            case "RoutineSelection":
                Intent intRoutSel = new Intent(getApplicationContext(), Routine_Selection.class);
                Intent in = getIntent();
                if(in.getStringExtra("callerActivity") != null && in.getStringExtra("callerActivity").equals("SelExerEspecific")){
                    intRoutSel.putExtra("exerciseRoutine",in.getSerializableExtra("exerciseRoutine"));
                    intRoutSel.putExtra("exercise",in.getSerializableExtra("exercise"));
                    intRoutSel.putExtra("caller",in.getStringExtra("caller2"));
                    intRoutSel.putExtra("callerActivity","SelExerEspecific");
                }
                startActivity(intRoutSel);
                break;
            case "RoutineEspecific":
                Intent routEspe = new Intent(getApplicationContext(), RoutineEspecific.class);
                routEspe.putExtra("routine",getIntent().getSerializableExtra("routine"));
                startActivity(routEspe);
                break;
            case "New_Routine":
                Intent intNewRoutine = new Intent(getApplicationContext(), NewRoutine.class);
                startActivity(intNewRoutine);
                break;
            case "CategorySelection":
                Intent intCategory = new Intent(getApplicationContext(), CategorySelection.class);
                intCategory.putExtra("day",getIntent().getStringExtra("day"));
                startActivity(intCategory);
                break;
            case "FitnessLevel":
                Intent intFit = new Intent(getApplicationContext(), FitnessLevelSelection.class);
                i = getIntent();
                intFit.putExtra("category",i.getStringExtra("category"));
                intFit.putExtra("day",i.getStringExtra("day"));
                startActivity(intFit);
                break;
            case "RoutineSelExercises":
                Intent intRoutSelExer = new Intent(getApplicationContext(), RoutineSelExercises.class);
                i = getIntent();
                intRoutSelExer.putExtra("category",i.getStringExtra("category"));
                intRoutSelExer.putExtra("day",i.getStringExtra("day"));
                intRoutSelExer.putExtra("fitnessLevel",i.getStringExtra("fitnessLevel"));
                startActivity(intRoutSelExer);
                break;
            case "ExercisesManage":
                Intent intExercisManage = new Intent(getApplicationContext(), ExercisesManage.class);
                intExercisManage.putExtra("exerciseRoutine",getIntent().getSerializableExtra("exerciseRoutine"));
                intExercisManage.putExtra("exercise",getIntent().getSerializableExtra("exercise"));
                intExercisManage.putExtra("category",getIntent().getStringExtra("category"));
                intExercisManage.putExtra("fitnessLevel",getIntent().getStringExtra("fitnessSelection"));
                startActivity(intExercisManage);
                break;
            case "Login":
                Intent intLog = new Intent(getApplicationContext(), LoginActivity.class);
                String c = getIntent().getStringExtra("caller2");
                intLog.putExtra("caller",c);
                if(c != null && c.equals("Message")){
                    intLog.putExtra("userid",getIntent().getStringExtra("userid"));
                    intLog.putExtra("caller2",getIntent().getStringExtra("caller3"));
                }
                startActivity(intLog);
                break;
            case "Register":
                Intent intReg = new Intent(getApplicationContext(), RegisterActivity.class);
                intReg.putExtra("caller",getIntent().getStringExtra("caller2"));
                intReg.putExtra("caller3",getIntent().getStringExtra("caller3"));
                intReg.putExtra("userid",getIntent().getStringExtra("userid"));
                startActivity(intReg);
                break;
            case "Chat":
                Intent intChat = new Intent(getApplicationContext(), LoginActivity.class);
                intChat.putExtra("caller","Chat");
                startActivity(intChat);
                break;
            case "User":
                Intent intentUser = new Intent(getApplicationContext(), LoginActivity.class);
                intentUser.putExtra("caller","User");
                startActivity(intentUser);
                break;
            case "Message":
                Intent intMessage = new Intent(getApplicationContext(), LoginActivity.class);
                intMessage.putExtra("caller","Message");
                intMessage.putExtra("userid",getIntent().getStringExtra("userid"));
                intMessage.putExtra("caller2",getIntent().getStringExtra("caller2"));
                startActivity(intMessage);
                break;
        }
    }

    public void goExercises(View view) {
        if(casesDeleteBD.contains(caller)){
            WorkouticDBHelper dbExtra = new WorkouticDBHelper(this, DatabasesUtil.NR_DATABASE_NAME,null,DatabasesUtil.NR_DATABASE_VERSION);
            dbExtra.deleteDB(); // borrar la BD extra
            removeNumElmSP();

        }
        Intent intExercises = new Intent(getApplicationContext(),Exercises.class);
        intExercises.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intExercises);

    }

    public void goMain(View view) {
        if(casesDeleteBD.contains(caller)){
            WorkouticDBHelper dbExtra = new WorkouticDBHelper(this, DatabasesUtil.NR_DATABASE_NAME,null,DatabasesUtil.NR_DATABASE_VERSION);
            dbExtra.deleteDB(); // borrar la BD extra
            removeNumElmSP();
        }
        Intent intMain = new Intent(getApplicationContext(), MainActivity.class);
        intMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intMain);

    }

    public void goLogin(View view) {
        if(casesDeleteBD.contains(caller)){
            WorkouticDBHelper dbExtra = new WorkouticDBHelper(this, DatabasesUtil.NR_DATABASE_NAME,null,DatabasesUtil.NR_DATABASE_VERSION);
            dbExtra.deleteDB(); // borrar la BD extra
            removeNumElmSP();
        }
        Intent intLog = new Intent(getApplicationContext(),LoginActivity.class);
        intLog.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intLog);
    }

    public void goRoutine(View view) {
        if(casesDeleteBD.contains(caller)){
            WorkouticDBHelper dbExtra = new WorkouticDBHelper(this, DatabasesUtil.NR_DATABASE_NAME,null,DatabasesUtil.NR_DATABASE_VERSION);
            dbExtra.deleteDB(); // borrar la BD extra
            removeNumElmSP();
        }
        Intent intRoutine = new Intent(getApplicationContext(),Routine_Main.class);
        intRoutine.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intRoutine);
    }

    public void goChat(View view) {
        if(casesDeleteBD.contains(caller)){
            WorkouticDBHelper dbExtra = new WorkouticDBHelper(this, DatabasesUtil.NR_DATABASE_NAME,null,DatabasesUtil.NR_DATABASE_VERSION);
            dbExtra.deleteDB(); // borrar la BD extra
            removeNumElmSP();
        }
        Intent intChat= new Intent(getApplicationContext(), LoginActivity.class);
        intChat.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intChat);
    }

    // Shared preferences de num de elementos
    private long getNumElmSP(){
        // Obtiene la cantidad de elementos
        SharedPreferences sp = getSharedPreferences(NewRoutine.PREF,Context.MODE_PRIVATE);
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

    public void goLogOut(View view) {
        if(currentUser != null && auth != null){
            auth.signOut();
        }
    }
}
