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

public class RoutinesAdapter extends BaseAdapter {
    private List<RoutineModel> routinesData;

    public RoutinesAdapter(List<RoutineModel> listaRutinas) {
        this.routinesData = new LinkedList<RoutineModel>();
        this.routinesData.addAll(listaRutinas);
    }

    @Override
    public int getCount() {
        return this.routinesData.size();
    }

    @Override
    public Object getItem(int i) {
        return this.routinesData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null){
            LayoutInflater inflater = (LayoutInflater) viewGroup.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.routine_item,null);
        }
        TextView name = view.findViewById(R.id.txt_card_routine_item_title);
        name.setText(routinesData.get(i).getName());
        TextView timestamp = view.findViewById(R.id.txt_card_routine_item_fecha);
        timestamp.setText(Long.toString(routinesData.get(i).getTimestamp()));
        return view;
    }

    public void updateRoutines(List<RoutineModel> routines){
        this.routinesData.clear();
        this.routinesData.addAll(routines);
        super.notifyDataSetChanged();
    }
}
