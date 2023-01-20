package com.example.workoutic.models;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.workoutic.R;
import com.example.workoutic.SelectionExercises;
import com.example.workoutic.util.DownloadExercisesThreads;

import java.util.LinkedList;
import java.util.List;

public class ExerciseRoutineAdapter extends BaseAdapter {
    private List<ExercisesRoutineModel> exercisesRoutModelData;
    private List<ExercisesModel> exercisesModelData;

    public ExerciseRoutineAdapter(List<ExercisesRoutineModel> listaEjerRout, List<ExercisesModel> listaEjer) {
        this.exercisesRoutModelData = new LinkedList<ExercisesRoutineModel>();
        this.exercisesRoutModelData.addAll(listaEjerRout);
        this.exercisesModelData = new LinkedList<ExercisesModel>();
        this.exercisesModelData.addAll(listaEjer);
    }

    @Override
    public int getCount() {
        return exercisesRoutModelData.size();
    }

    @Override
    public Object getItem(int i) {
        return exercisesRoutModelData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null){
            LayoutInflater inflater = (LayoutInflater) viewGroup.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.exercises_routine_item,null);
        }
        LinearLayout ly = view.findViewById(R.id.ly_exercise_routine_especific_exercise);
        setOnClickListener(ly,i,viewGroup);

        ExercisesModel e = findIdExercises(exercisesRoutModelData.get(i).getExercise_id());

        ImageView image = view.findViewById(R.id.img_card_item_exercises_routine);
        assert e != null;
        image.setImageBitmap(e.getImageAlt()) ;
        TextView title = view.findViewById(R.id.txt_card_item_exercises_routine_title);
        title.setText(e.getName());
        TextView equip = view.findViewById(R.id.txt_card_item_exercises_routine_equip);
        equip.setText(concatenarLista(e.getEquipment()));
        TextView series = view.findViewById(R.id.txt_card_item_exercises_routine_series);
        series.setText(String.valueOf(exercisesRoutModelData.get(i).getSeries()));
        TextView rep = view.findViewById(R.id.txt_card_item_exercises_routine_rep);
        rep.setText(String.valueOf(exercisesRoutModelData.get(i).getRepetitions()));
        TextView peso = view.findViewById(R.id.txt_card_item_exercises_weight);
        String pes = "" + exercisesRoutModelData.get(i).getWeightKg() + " kg";
        peso.setText(pes);
        TextView time = view.findViewById(R.id.txt_card_item_exercises_time);
        String tim = "" + exercisesRoutModelData.get(i).getTimeMinutes() + " min";
        time.setText(tim);
        return view;
    }

    private void setOnClickListener(View view, int i, ViewGroup parent) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // this part is important, it lets ListView handle the clicks
                ((ListView) parent).performItemClick(v, i, 0);
            }
        });
    }

    public void updateExercises(List<ExercisesRoutineModel> exercisesRoutines,List<ExercisesModel> exercises) {
        this.exercisesRoutModelData.clear();
        this.exercisesRoutModelData.addAll(exercisesRoutines);
        this.exercisesModelData.clear();
        this.exercisesModelData.addAll(exercises);
        super.notifyDataSetChanged();
    }

    public ExercisesModel findIdExercises(long id){
        for(ExercisesModel e : exercisesModelData){
            if(e.getId() == id){
                return e;
            }
        }
        return null;
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

}
