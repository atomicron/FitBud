package com.chupe.fitbud.views;

import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.chupe.fitbud.R;
import com.chupe.fitbud.types.Exercise;

public class TimePickerDialog extends DialogFragment {

    Button btnAccept;
    CustomTimePicker picker1;
    CustomTimePicker picker2;
    CustomTimePicker picker3;

    AlertDialog.Builder builder;

    View view;

    int duration = 0;
    public int getDuration() { return duration; }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        builder = new AlertDialog.Builder(getActivity());

        view = getLayoutInflater().inflate(R.layout.time_picker_dialog, null);

        picker1 = view.findViewById(R.id.picker_hours);
        picker2 = view.findViewById(R.id.picker_minutes);
        picker3 = view.findViewById(R.id.picker_seconds);

        builder.setView(view);

        btnAccept = view.findViewById(R.id.btn_accept);
        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                duration = picker1.getCurrent()*60*60 + picker2. getCurrent()*60 + picker3.getCurrent();
                listener.onDialogAcceptClick(com.chupe.fitbud.views.TimePickerDialog.this);
                dismiss();
            }
        });


        AlertDialog dialog = builder.create();
        return dialog;
    }


    public interface TimePickerDialogInterface {
        public void onDialogAcceptClick(com.chupe.fitbud.views.TimePickerDialog dialog);
    }
    com.chupe.fitbud.views.TimePickerDialog.TimePickerDialogInterface listener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            listener = (com.chupe.fitbud.views.TimePickerDialog.TimePickerDialogInterface) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(getActivity().toString()
                + " must implement NoticeDialogListener");
        }
    }


}