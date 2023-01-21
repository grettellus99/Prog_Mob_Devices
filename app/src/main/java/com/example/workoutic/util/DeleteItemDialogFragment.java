package com.example.workoutic.util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.workoutic.R;
import com.example.workoutic.adapters.ExerciseRoutineViewAdapter;
import com.example.workoutic.database.WorkouticDBHelper;
import com.example.workoutic.models.ExercisesRoutineModel;

public class DeleteItemDialogFragment extends DialogFragment {
    private String message;
    private String title;

    TextView titleTV;
    TextView messageTV;

    LinearLayout btn_p;
    LinearLayout btn_n;

    ListView lv;
    ProgressBar pb;
    ExercisesRoutineModel er;
    Context c;
    int i;

    public DeleteItemDialogFragment(String message,String title,Context c ,ListView lv,ProgressBar pb,ExercisesRoutineModel er,int pos){
        this.message = message;
        this.title = title;
        this.lv = lv;
        this.pb = pb;
        this.er = er;
        this.c = c;
        this.i = pos;
    }


    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Personalizarlo
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        LinearLayout ly = (LinearLayout) inflater.inflate(R.layout.dialog,null);
        titleTV = ly.findViewById(R.id.dialog_title);
        messageTV = ly.findViewById(R.id.dialog_specific_message);
        titleTV.setText(this.title);
        messageTV.setText(this.message);
        btn_p = ly.findViewById(R.id.btn_option_positive);
        btn_n = ly.findViewById(R.id.btn_option_negative);
        btn_p.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView txt = ly.findViewById(R.id.btn_option_positive_txt);
                txt.setTextColor(getResources().getColor(R.color.edit));

                lv.setVisibility(View.INVISIBLE);

                pb.setVisibility(View.VISIBLE);
                WorkouticDBHelper helper = new WorkouticDBHelper(c,DatabasesUtil.DATABASE_NAME,null,DatabasesUtil.DATABASE_VERSION);
                helper.deleteExerciseRoutine(er);
                ((ExerciseRoutineViewAdapter) lv.getAdapter()).deleteExcercise(i);
                pb.setVisibility(View.GONE);
                lv.setVisibility(View.VISIBLE);
                dismiss();
            }
        });
        btn_n.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView txt = ly.findViewById(R.id.btn_option_negative_txt);
                txt.setTextColor(getResources().getColor(R.color.edit));
                dismiss();
            }
        });

        builder.setView(ly);
        return builder.create();
    }

}
