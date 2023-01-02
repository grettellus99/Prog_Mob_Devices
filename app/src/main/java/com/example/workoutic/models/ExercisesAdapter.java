package com.example.workoutic.models;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.workoutic.R;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ExercisesAdapter extends BaseAdapter {
    private List<ExercisesModel> exercisesModelData = new LinkedList<ExercisesModel>();

    public ExercisesAdapter(List<ExercisesModel> listaEjer) {
        this.exercisesModelData.addAll(listaEjer);
    }

    @Override
    public int getCount() {
        return exercisesModelData.size();
    }

    @Override
    public Object getItem(int i) {
        return exercisesModelData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null){
            LayoutInflater inflater = (LayoutInflater) viewGroup.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.exercises_item,null);
        }
        ImageView image = view.findViewById(R.id.img_card_item_exercises);
        image.setImageBitmap(exercisesModelData.get(i).getImageAlt());
        TextView title = view.findViewById(R.id.txt_card_item_exercises_title);
        title.setText(exercisesModelData.get(i).getName());
        TextView level = view.findViewById(R.id.txt_card_item_exercises_level);
        level.setText(concatenarLista(exercisesModelData.get(i).getLevel()));
        TextView muscles = view.findViewById(R.id.txt_card_item_exercises_muscles);
        muscles.setText(concatenarLista(exercisesModelData.get(i).getMuscles()));
        TextView equipment = view.findViewById(R.id.txt_card_item_exercises_equip);
        equipment.setText(concatenarLista(exercisesModelData.get(i).getEquipment()));
        return view;
    }

    public void updateExercises(List<ExercisesModel> exercises) {
        this.exercisesModelData.clear();
        this.exercisesModelData.addAll(exercises);
        super.notifyDataSetChanged();
    }

    public String concatenarLista(List<String> lista) {
        String res = "";
        int len = lista.size();
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
