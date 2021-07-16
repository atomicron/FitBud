package com.chupe.fitbud;

import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;

import com.chupe.fitbud.types.Exercise;
import com.chupe.fitbud.views.CustomTimePicker;
import com.chupe.fitbud.views.TimePickerDialog;
import com.google.android.material.switchmaterial.SwitchMaterial;

public class EditExerciseDialogFragment extends DialogFragment {

    Exercise exercise;
    View view;
    EditText editText;
    Switch durationSwitch;
    CustomTimePicker picker1;
    CustomTimePicker picker2;
    CustomTimePicker picker3;
    Button btnAdd;
    Button btnRemove;

    RelativeLayout layoutMain;
    RelativeLayout layoutFirst;
    private LinearLayout pickers;
    private RelativeLayout layout_two;
    RelativeLayout layoutToMove;

    AlertDialog.Builder builder;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        builder = new AlertDialog.Builder(getActivity());

        view = getLayoutInflater().inflate(R.layout.dialog_edit_exercise, null);
        editText = view.findViewById(R.id.edit_text);
        durationSwitch = view.findViewById(R.id.duration_switch);
        pickers = view.findViewById(R.id.layout_pickers);
        layoutMain = view.findViewById(R.id.layout_main);
        layoutFirst = view.findViewById(R.id.layout_first);
        btnAdd = view.findViewById(R.id.btn_add);
        btnRemove = view.findViewById(R.id.btn_remove);
        layoutToMove = view.findViewById(R.id.layout_to_move);

        picker1 = view.findViewById(R.id.time_picker_1);
        picker2 = view.findViewById(R.id.time_picker_2);
        picker3 = view.findViewById(R.id.time_picker_3);

        editText.setText(exercise.getName());

        builder.setView(view);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onDialogPositiveClick(EditExerciseDialogFragment.this);
                dismiss();
            }
        });

        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onDialogNegativeClick(EditExerciseDialogFragment.this);
                dismiss();
            }
        });

        // TODO complete this shit
        if (exercise.hasDuration) durationSwitch.toggle();
        int duration = exercise.getDuration();
        picker1.setCurrentByValue(duration/60);
        duration/=60;
        picker2.setCurrentByValue(duration);
        duration/=60;
        picker3.setCurrentByValue(duration);

        AlertDialog dialog = builder.create();
        durationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ObjectAnimator animation = ObjectAnimator.ofFloat(layoutToMove, "translationY", pickers.getHeight());
                    animation.setDuration(500);
                    animation.start();

                    pickers.setVisibility(View.VISIBLE);

                    ViewGroup.LayoutParams params = layoutMain.getLayoutParams();
                    params.height = 900;
                    layoutMain.setLayoutParams(params);
                }
                else {
                    ObjectAnimator animation = ObjectAnimator.ofFloat(layoutToMove, "translationY", 0);
                    animation.setDuration(500);
                    animation.start();

                    ViewGroup.LayoutParams params = layoutMain.getLayoutParams();
                    params.height = 300;
                    layoutMain.setLayoutParams(params);

                    pickers.setVisibility(View.INVISIBLE);
                }
            }
        });
        return dialog;
    }

    public void setExercise(Exercise exercise) {
        this.exercise = exercise;
    }

    /* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    public interface NoticeDialogListener {
        public void onDialogPositiveClick(EditExerciseDialogFragment dialog);
        public void onDialogNegativeClick(EditExerciseDialogFragment dialog);
    }

    // Use this instance of the interface to deliver action events
    NoticeDialogListener listener;

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            listener = (NoticeDialogListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(getActivity().toString()
                + " must implement NoticeDialogListener");
        }
    }

    public Exercise getExercise() { return exercise; }

    public String getName() {
        return editText.getText().toString();
    }

    public int getDuration() {
        int duration = picker1.getCurrent()*60*60 + picker2. getCurrent()*60 + picker3.getCurrent();
        return duration;
    }
}

