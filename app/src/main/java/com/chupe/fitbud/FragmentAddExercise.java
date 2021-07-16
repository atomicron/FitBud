package com.chupe.fitbud;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.chupe.fitbud.database.ExerciseDAO;
import com.chupe.fitbud.helpers.Helpers;
import com.chupe.fitbud.types.Exercise;
import com.chupe.fitbud.views.CustomTimePicker;

public class FragmentAddExercise extends Fragment {
    private Switch switchDuration;
    private LinearLayout pickers;
    private RelativeLayout layout_two;
    private Button btnAddExercise;
    private EditText editName;
    private Fragment parent;

    private CustomTimePicker picker1;
    private CustomTimePicker picker2;
    private CustomTimePicker picker3;

    public FragmentAddExercise() {}
    public FragmentAddExercise(Fragment parent) {
        this.parent = parent;
    }
//    public static FragmentAddExercise newInstance(String param1, String param2) {
//        FragmentAddExercise fragment = new FragmentAddExercise();
//        Bundle args = new Bundle();
//        fragment.setArguments(args);
//        return fragment;
//    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    OnBackPressedCallback callback = new OnBackPressedCallback(true) {
        @Override
        public void handleOnBackPressed() {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.nav_host_fragment, parent);
            fragmentTransaction.commit();
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_exercise, container, false);

        switchDuration = view.findViewById(R.id.switchDuration);
        pickers = view.findViewById(R.id.layout_pickers);
        layout_two = view.findViewById(R.id.layout_two);
        btnAddExercise = view.findViewById(R.id.btn_add);
        editName = view.findViewById(R.id.editExercise);
        picker1 = view.findViewById(R.id.timePicker1);
        picker2 = view.findViewById(R.id.timePicker2);
        picker3 = view.findViewById(R.id.timePicker3);


        getActivity().getOnBackPressedDispatcher().addCallback(getActivity(), callback);

        switchDuration.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {

                    ObjectAnimator animation = ObjectAnimator.ofFloat(layout_two, "translationY", pickers.getHeight());
                    animation.setDuration(500);
                    animation.start();

                    pickers.setVisibility(View.VISIBLE);

                }
                else {


                    ObjectAnimator animation = ObjectAnimator.ofFloat(layout_two, "translationY", 0);
                    animation.setDuration(500);
                    animation.start();

                    pickers.setVisibility(View.INVISIBLE);

                }

            }
        });

        btnAddExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editName.getText().toString();
                if (name.isEmpty()) {
                    Toast.makeText(getContext(), "Text field is empty!", Toast.LENGTH_SHORT).show();
                    return;
                }

                Exercise ex;
                if (switchDuration.isChecked()) {
                    int duration = picker1.getCurrent()*60*60 + picker2.getCurrent()*60 + picker3.getCurrent();
                    ex = new Exercise(name, duration);
                } else {
                    ex = new Exercise(name);
                }

                MainActivity activity = (MainActivity) getActivity();
                ExerciseDAO exerciseDAO = activity.db.exerciseDAO();

                Runnable runnable = new Runnable(){
                    public void run() {
                        exerciseDAO.insertAll(ex);
                    }
                };

                Thread thread = new Thread(runnable);
                thread.start();

                Helpers.hideKeyboard(getActivity());
                getActivity().onBackPressed();
            }
        });

        return view;
    }
}