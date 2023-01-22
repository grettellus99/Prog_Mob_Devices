package com.example.workoutic.util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.workoutic.R;
import com.example.workoutic.adapters.ExerciseRoutineViewAdapter;
import com.example.workoutic.database.WorkouticDBHelper;
import com.example.workoutic.models.RoutineModel;
import com.example.workoutic.routines.Routine_Selection;

public class DeleteRoutineDialogFragment extends DialogFragment {
    private String message;
    private String title;

    TextView titleTV;
    TextView messageTV;

    LinearLayout btn_p;
    LinearLayout btn_n;
    Context c;
    RoutineModel r;

    public DeleteRoutineDialogFragment(String message, String title, Context c , RoutineModel r){
        this.message = message;
        this.title = title;
        this.r = r;
        this.c = c;
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
                btn_p.setAlpha(0.6F);
                WorkouticDBHelper helper = new WorkouticDBHelper(c,DatabasesUtil.DATABASE_NAME,null,DatabasesUtil.DATABASE_VERSION);
                helper.deleteRoutine(r);
                Intent intent = new Intent(c, Routine_Selection.class);
                startActivity(intent);
                dismiss();
            }
        });
        btn_n.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_n.setAlpha(0.6F);
                dismiss();
            }
        });

        builder.setView(ly);
        return builder.create();
    }

}
