package com.example.workoutic.models;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.workoutic.R;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class RoutinesAdapter extends BaseAdapter implements Filterable {
    private List<RoutineModel> routinesData;
    private List<RoutineModel> routinesDataFiltered;

    public RoutinesAdapter(List<RoutineModel> listaRutinas) {
        this.routinesData = new LinkedList<RoutineModel>();
        this.routinesData.addAll(listaRutinas);
        this.routinesDataFiltered = new LinkedList<RoutineModel>();
        this.routinesDataFiltered.addAll(listaRutinas);
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
        name.setText(routinesDataFiltered.get(i).getName());
        TextView timestamp = view.findViewById(R.id.txt_card_routine_item_fecha);
        timestamp.setText((routinesDataFiltered.get(i).LongToDateTimeString()));
        return view;
    }

    public void updateRoutines(List<RoutineModel> routines){
        this.routinesData.clear();
        this.routinesDataFiltered.clear();
        this.routinesData.addAll(routines);
        this.routinesDataFiltered.addAll(routines);
        super.notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {

        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                FilterResults filterResults = new FilterResults();
                if(constraint == null || constraint.length() == 0){
                    filterResults.count = routinesData.size();
                    filterResults.values = routinesData;

                }else{
                    List<RoutineModel> resultsModel = new ArrayList<RoutineModel>();
                    String searchStr = constraint.toString().toLowerCase();

                    for(RoutineModel itemsModel:routinesData){
                        if(itemsModel.getName().contains(searchStr)){
                            resultsModel.add(itemsModel);
                        }
                        filterResults.count = resultsModel.size();
                        filterResults.values = resultsModel;
                    }
                }
                return filterResults;
            }
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                routinesDataFiltered = (List<RoutineModel>) results.values;
                notifyDataSetChanged();
            }
        };
        return filter;
    }
}
