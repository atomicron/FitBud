package com.chupe.fitbud;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.chupe.fitbud.helpers.Helpers;
import com.chupe.fitbud.types.Exercise;
import com.chupe.fitbud.types.Workout;
import com.chupe.fitbud.views.CustomTimePicker;

import java.util.ArrayList;
import java.util.List;

public class ExercisePickerDialog extends DialogFragment {

    private ExercisePickerDialog currentFragment;
    AlertDialog.Builder builder;
    View view;

    ListView listExercises;

    List<Exercise> arrayExercises = new ArrayList<>();

    Exercise exercise;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        builder = new AlertDialog.Builder(getActivity());
        view = getLayoutInflater().inflate(R.layout.exercise_picker_dialog, null);
        listExercises = view.findViewById(R.id.list_exercises);
        builder.setView(view);

        currentFragment = this;

        try {
            updateList();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        CustomAdapter adapter = new CustomAdapter(getContext(), arrayExercises);

        listExercises.setAdapter(adapter);

        listExercises.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                exercise = arrayExercises.get(position);
                listener.onDialogAcceptClick(currentFragment);
                dismiss();
            }
        });


        AlertDialog dialog = builder.create();
        return dialog;
    }

    public Exercise getExercise() {
        return exercise;
    }

    private void updateList() throws InterruptedException {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                // pull all exercises into a list
                MainActivity activity = (MainActivity) getActivity();
                arrayExercises = activity.db.exerciseDAO().getAll();
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
        thread.join();
    }

    public interface ExercisePickerDialogInterface {
        public void onDialogAcceptClick(com.chupe.fitbud.ExercisePickerDialog dialog);
    }
    ExercisePickerDialog.ExercisePickerDialogInterface listener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            listener = (com.chupe.fitbud.ExercisePickerDialog.ExercisePickerDialogInterface) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(getActivity().toString()
                + " must implement NoticeDialogListener");
        }
    }



    static class CustomAdapter extends BaseAdapter {
        Context context;
        List<Exercise> exArr;
        private LayoutInflater inflater;
        public CustomAdapter(Context context, List<Exercise> exArr) {
            this.context = context;
            this.exArr = exArr;
            inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return exArr.size();
        }
        @Override
        public Object getItem(int position) {
            return exArr.get(position);
        }
        @Override
        public long getItemId(int position) {
            return position;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View vi = convertView;
            if (vi == null)
                vi = inflater.inflate(R.layout.layout_exercises, null);
            TextView textName = vi.findViewById(R.id.text_exercise);
            TextView textDuration = vi.findViewById(R.id.text_duration);
            textName.setText(exArr.get(position).getName());
            int duration = exArr.get(position).getDuration();
            textDuration.setText(Helpers.toTime(duration));
            return vi;
        }
    }
}
