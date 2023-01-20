package com.example.workoutic.exercises;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.workoutic.MainActivity;
import com.example.workoutic.Menu;
import com.example.workoutic.R;
import com.example.workoutic.adapters.ExercisesAdapter;
import com.example.workoutic.models.ExercisesModel;
import com.example.workoutic.util.DownloadExercisesThreads;

import java.util.LinkedList;
import java.util.List;

public class SelectionExercises extends AppCompatActivity {
    private String caller;
    TextView txtCategoria;
    ImageView imgCategory;
    String description;
    LinearLayout botonCE;
    LinearLayout botonSE;
    public static Boolean EQUIPAMIENTO = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection_exercises);

        // Recuperar los extra del intent que creo la actividad
        Intent i = getIntent();
        caller= i.getStringExtra("caller");

        // Inicializar el listview
        ListView lv = findViewById(R.id.lv_sel_exerc);
        lv.setAdapter(new ExercisesAdapter(new LinkedList<ExercisesModel>()));


        // Listener de cada item en el listview. Llama a la actividad de Ejercicio Especifico
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ExercisesModel exercise = (ExercisesModel) adapterView.getAdapter().getItem(i);
                Intent intSelExerEsp = new Intent(getApplicationContext(),SelExerEspecific.class);
                intSelExerEsp.putExtra("exercise",exercise);
                intSelExerEsp.putExtra("caller",caller);
                startActivity(intSelExerEsp);
            }
        });

        // Ocultar el lv hasta que se recuperen los datos
        lv.setVisibility(View.GONE);

        // Inicializar las vistas de la imagen de la categoria y el nombre
        imgCategory = findViewById(R.id.img_sel_exerc_principal);
        txtCategoria = findViewById(R.id.txtView_sel_exerc_categoria);
        txtCategoria.setText(caller);

        // Asignar la imagen principal y sus descripciones
        switch (caller){

            case Exercises.CHEST:
                description = getString(R.string.img_chest_descrip);
                imgCategory.setContentDescription(description);
                imgCategory.setImageBitmap(drawableToBitmap(getDrawable(R.drawable.pecho)));
                break;
            case Exercises.SHOULDER:
            case Exercises.ARMS:
                description = getString(R.string.img_arms_descrip);
                imgCategory.setContentDescription(description);
                imgCategory.setImageBitmap(drawableToBitmap(getDrawable(R.drawable.brazos)));
                break;
            case Exercises.GLUTES:
                description = getString(R.string.img_glutes_descrip);
                imgCategory.setContentDescription(description);
                imgCategory.setImageBitmap(drawableToBitmap(getDrawable(R.drawable.glutes)));
                break;
            case Exercises.ABS:
                description = getString(R.string.img_abs_descrip);
                imgCategory.setContentDescription(description);
                imgCategory.setImageBitmap(drawableToBitmap(getDrawable(R.drawable.abdominales)));
                break;
            case Exercises.ADUCT:
            case Exercises.QUADS:
            case Exercises.CALVES:
                description = getString(R.string.img_legs_descrip);
                imgCategory.setContentDescription(description);
                imgCategory.setImageBitmap(drawableToBitmap(getDrawable(R.drawable.piernas)));
                break;
            case Exercises.WEIST:
                description = getString(R.string.img_waist_descrip);
                imgCategory.setContentDescription(description);
                imgCategory.setImageBitmap(drawableToBitmap(getDrawable(R.drawable.caderas)));
                break;
            case Exercises.DELT:
            case Exercises.BACK:
                description = getString(R.string.img_back_descrip);
                imgCategory.setContentDescription(description);
                imgCategory.setImageBitmap(drawableToBitmap(getDrawable(R.drawable.espalda)));
        }


        // Inicializando los botones
        botonSE = findViewById(R.id.ly_sel_exerc_sin_equipo);
        botonCE = findViewById(R.id.ly_sel_exerc_con_equipo);

        botonCE.setBackgroundColor(getColor(R.color.primary_MenuBar));

        ///////////// DOWNLOAD EXERCISES ///////////////
        downloadExercises();
    }

    // onClick listeners en los 'iconos' del layout
    public void goMain(View view) {
        Intent intMain = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intMain);

    }
    public void goMenu(View view) {
        Intent intMenu= new Intent(getApplicationContext(), Menu.class);
        intMenu.putExtra("caller","SelectionExercises");
        intMenu.putExtra("category",caller);
        startActivity(intMenu);
    }
    public void goBack(View view) {
        Intent intExercises = new Intent(getApplicationContext(),Exercises.class);
        startActivity(intExercises);
    }

    ////////////////////////////////////////////////////////////////////

    // Descargar los datos del servidor y actualizar las vistas
    public void downloadExercises() {
        DownloadExercisesThreads download = new DownloadExercisesThreads(SelectionExercises.this);
        Thread th = new Thread(download);
        th.start();
    }

    public void prepareForDownloadExercises() {
        ListView lv = findViewById(R.id.lv_sel_exerc);
        ProgressBar pb = findViewById(R.id.progressBar_welcome);
        lv.setVisibility(View.GONE);
        pb.setVisibility(View.VISIBLE);
    }

    public void updateExercises(List<ExercisesModel> exercises) {
        ListView lv = findViewById(R.id.lv_sel_exerc);
        ((ExercisesAdapter)lv.getAdapter()).updateExercises(exercises);

        ProgressBar pb = findViewById(R.id.progressBar_welcome);
        pb.setVisibility(View.GONE);
        lv.setVisibility(View.VISIBLE);

    }
    //////////////////////////////////////////////////////////////////

   // Cambiar de equipamiento a sin equipamiento y viceversa
    public void sinEquipamiento(View view) {

        botonSE.setBackgroundColor(getColor(R.color.primary_MenuBar));
        botonCE.setBackgroundColor(getColor(R.color.secondary_MenuBar));
        SelectionExercises.EQUIPAMIENTO = false;
        downloadExercises();
    }

    public void conEquipamiento(View view) {
        botonCE.setBackgroundColor(getColor(R.color.primary_MenuBar));
        botonSE.setBackgroundColor(getColor(R.color.secondary_MenuBar));
        SelectionExercises.EQUIPAMIENTO = true;
        downloadExercises();
    }
    /////////////////////////////////////////////////////////////////

    // Metodos auxiliares
    public static Bitmap drawableToBitmap (Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable)drawable).getBitmap();
        }

        int width = drawable.getIntrinsicWidth();
        width = width > 0 ? width : 1;
        int height = drawable.getIntrinsicHeight();
        height = height > 0 ? height : 1;

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    public String getCategoria() {
        return caller;
    }
}