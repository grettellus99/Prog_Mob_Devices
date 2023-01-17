package com.example.workoutic.models;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.workoutic.R;

import java.util.LinkedList;
import java.util.List;

public class ExerciseRoutineViewAdapter extends BaseAdapter {
    private List<ExercisesRoutineModel> exercisesRoutModelData;
    private List<ExercisesModel> exercisesModelData;

    public ExerciseRoutineViewAdapter(List<ExercisesRoutineModel> listaEjerRout, List<ExercisesModel> listaEjer) {
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

        ExercisesModel e = findIdExercises(exercisesRoutModelData.get(i).getExercise_id());

        ImageView image = view.findViewById(R.id.img_card_item_exercises_routine_view);
        assert e != null;
        image.setImageBitmap(e.getImageAlt()) ;
        TextView title = view.findViewById(R.id.txt_card_item_exercises_routine_title_view);
        title.setText(e.getName());
        TextView series = view.findViewById(R.id.txt_card_item_exercises_routine_series_view);
        series.setText(exercisesRoutModelData.get(i).getSeries());
        TextView rep = view.findViewById(R.id.txt_card_item_exercises_routine_rep_view);
        rep.setText(exercisesRoutModelData.get(i).getRepetitions());
        TextView peso = view.findViewById(R.id.txt_card_item_exercises_weight_view);
        String pes = "" + exercisesRoutModelData.get(i).getWeightKg() + " kg";
        peso.setText(pes);
        TextView time = view.findViewById(R.id.txt_card_item_exercises_time_view);
        String tim = "" + exercisesRoutModelData.get(i).getTimeMinutes() + " min";
        time.setText(tim);
        return view;
    }

    public void updateExercises(List<ExercisesRoutineModel> exercisesRoutines,List<ExercisesModel> exercises) {
        this.exercisesRoutModelData.clear();
        this.exercisesRoutModelData.addAll(exercisesRoutines);
        this.exercisesModelData.clear();
        this.exercisesModelData.addAll(exercises);
        super.notifyDataSetChanged();
    }

    public void deleteExcercise(int i){
        exercisesRoutModelData.remove(i);
        exercisesModelData.remove(i);
        super.notifyDataSetChanged();
    }

    public  ExercisesModel getExercise(int i){
        return exercisesModelData.get(i);
    }

    public ExercisesModel findIdExercises(long id){
        for(ExercisesModel e : exercisesModelData){
            if(e.getId() == id){
                return e;
            }
        }
        return null;
    }
}
