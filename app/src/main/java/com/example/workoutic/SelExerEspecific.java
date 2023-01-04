package com.example.workoutic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.workoutic.models.ExercisesModel;
import com.example.workoutic.util.NetUtil;

import java.util.Arrays;

public class SelExerEspecific extends AppCompatActivity {
    String caller;
    ExercisesModel exercise;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sel_exer_especific);
        Intent i = getIntent();
        exercise = (ExercisesModel) i.getSerializableExtra("exercise");
        caller = i.getStringExtra("caller");

        TextView nameExer = findViewById(R.id.txt_sel_exerc_name_ejer);
        nameExer.setText(exercise.getName());
        TextView descExer = findViewById(R.id.txt_sel_exerc_descrip);
        descExer.setText(exercise.getDescription());
        TextView nivel = findViewById(R.id.txt_sel_exerc_nivel);
        nivel.setText(concatenarLista(exercise.getLevel()));
        TextView execut = findViewById(R.id.txt_sel_exerc_ejecucion);
        execut.setText(exercise.getExecution());
        TextView equip = findViewById(R.id.txt_sel_exerc_equip);
        equip.setText(concatenarLista(exercise.getEquipment()));

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    try {
                        Bitmap imgPrincipal = NetUtil.getURLBitmap(exercise.getImgMainURL());
                        exercise.setImageMain(imgPrincipal);
                        ImageView img_main = findViewById(R.id.img_sel_exer_espec_principal);
                        img_main.setImageBitmap(exercise.getImageMain());
                        LinearLayout ly = findViewById(R.id.ly_sel_exer_prog_bar);
                        ScrollView sv = findViewById(R.id.sc_view_sel_exerc);
                        sv.setVisibility(View.VISIBLE);
                        ly.setVisibility(View.INVISIBLE);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }).start();
    }

    public void goMenu(View view) {
        Intent intMenu= new Intent(getApplicationContext(),Menu.class);
        intMenu.putExtra("caller","EspecificExercises");
        intMenu.putExtra("category",caller);
        intMenu.putExtra("exercise",exercise);
        startActivity(intMenu);
    }

    public void goMain(View view) {
        Intent intMain = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intMain);
    }

    public String concatenarLista(String [] lista) {
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

    public void addRoutine(View view) {
    }

    public void goBack(View view) {
        Intent intBack = new Intent(getApplicationContext(),SelectionExercises.class);
        intBack.putExtra("caller",caller);
        startActivity(intBack);
    }
}