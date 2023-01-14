package com.example.workoutic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class NewRoutine extends AppCompatActivity {
    private String nombreRutina;
    private LinearLayout editName;
    private LinearLayout showName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_routine);

        editName = findViewById(R.id.ly_routine_name_edit);
        editName.setVisibility(View.VISIBLE);
        showName = findViewById(R.id.ly_routine_name_display);
        showName.setVisibility(View.GONE);


    }

    public void goMain(View view) {
        Intent intMain = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intMain);
    }

    public void goMenu(View view) {
        Intent intMenu = new Intent(getApplicationContext(),Menu.class);
        intMenu.putExtra("caller","New_Routine");
        startActivity(intMenu);

    }

    public void goBack(View view) {
        Intent intRoutineMain = new Intent(getApplicationContext(),Routine_Main.class);
        startActivity(intRoutineMain);
    }

    public void addRoutine(View view) {
    }

    public void validateName(View view) {
        EditText name = (EditText) findViewById(R.id.txt_edit_new_routine_name_routine);
        nombreRutina =  name.getText().toString();
        if(isNotVoid(nombreRutina)){
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
        name.setText(nombreRutina);

        Button btn = findViewById(R.id.btn_new_routine_add_routine);
        btn.setAlpha(0.6F);
        btn.setClickable(false);

        editName.setVisibility(View.VISIBLE);
        showName.setVisibility(View.GONE);
    }
}