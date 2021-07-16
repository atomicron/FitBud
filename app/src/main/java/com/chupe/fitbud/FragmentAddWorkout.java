package com.chupe.fitbud;

import android.content.Context;
import android.os.Bundle;
import android.telephony.BarringInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.chupe.fitbud.helpers.Helpers;
import com.chupe.fitbud.types.Action;
import com.chupe.fitbud.types.Exercise;
import com.chupe.fitbud.types.Workout;
import com.chupe.fitbud.views.TimePickerDialog;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.internal.schedulers.ExecutorScheduler;

public class FragmentAddWorkout extends Fragment {

    private Fragment parent;
    private ListView listView;
    private List<Exercise> list = new ArrayList<>();
    private Workout workout;

    private MainActivity mainActivity;

    private Button btnAddExercise;
    private Button btnAddBreak;
    private Button btnDone;

    private FragmentAddWorkout currentFragment;

    public void onDialogAcceptClick(TimePickerDialog dialog) {
        list.add(new Exercise("Break", dialog.getDuration()));
        adapter.notifyDataSetChanged();
        dialog.dismiss();
    }

    public void onDialogExerciseClick(ExercisePickerDialog dialog) {
        list.add(dialog.getExercise());
        adapter.notifyDataSetChanged();
        dialog.dismiss();
    }

    public void remove(int position) {
        list.remove(position);
        adapter.notifyDataSetChanged();
    }

    static class CustomAdapter extends BaseAdapter {
        Context context;
        FragmentAddWorkout parentFragment;
        List<Exercise> list;
        private LayoutInflater inflater;
        public CustomAdapter(FragmentAddWorkout parent, Context context, List<Exercise> list) {
            this.parentFragment = parent;
            this.context = context;
            this.list = list;
            inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View vi = convertView;
            if (vi == null)
                vi = inflater.inflate(R.layout.layout_add_workout_adapter, null);
            TextView text = vi.findViewById(R.id.text);
            text.setText(list.get(position).getName());
            TextView textDuration = vi.findViewById(R.id.text_duration);
            textDuration.setText(Helpers.toTime(list.get(position).getDuration()));

            Button btnDelete = vi.findViewById(R.id.btn_remove);
            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    parentFragment.remove(position);
                }
            });

            return vi;
        }
        @Override
        public int getCount() {
            return list.size();
        }
        @Override
        public Object getItem(int position) {
            return list.get(position);
        }
        @Override
        public long getItemId(int position) {
            return position;
        }
    };

    private CustomAdapter adapter;

    public FragmentAddWorkout() {}
    public FragmentAddWorkout(Fragment parent) {
        this.parent = parent;
    }
    public FragmentAddWorkout(Fragment parent, Workout workout) {
        this.parent = parent;
        this.workout = workout;
    }
    @Override
    public void onCreate(@Nullable  Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable  ViewGroup container, @Nullable  Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_add_workout, container, false);

        getActivity().getOnBackPressedDispatcher().addCallback(getActivity(), callback);

        mainActivity = (MainActivity) getActivity();

        listView = view.findViewById(R.id.list_add_workout);
        btnAddBreak = view.findViewById(R.id.btn_add_break);
        btnAddExercise = view.findViewById(R.id.btn_add_exercise);
        btnDone = view.findViewById(R.id.btn_done);

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (workout == null)
                    return;

                String[] arr = workout.getActivities();
                List<Exercise> ex = mainActivity.db.exerciseDAO().getAll();

                list.clear();

                for (int i = 0; i < arr.length; i++) {
                    for (Exercise e : ex) {
                        if (String.valueOf(e.id).equals(arr[i])) {
                            list.add(e);
                        }
                    }
                }
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        adapter = new CustomAdapter(this, getContext(), list );
        listView.setAdapter(adapter);

        adapter.notifyDataSetChanged();

        btnAddBreak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog dialog = new TimePickerDialog();
                dialog.show(getActivity().getSupportFragmentManager(), "Some string?");
            }
        });
        btnAddExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExercisePickerDialog dialog = new ExercisePickerDialog();
                dialog.show(getActivity().getSupportFragmentManager(), "Some string?");
            }
        });
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ...
                // TODO on done click return a workout to the list and save to db
                Runnable runnable_update = new Runnable() {
                    @Override
                    public void run() {
                        mainActivity.db.workoutDAO().updateWorkout(workout);
                    }
                };

                Runnable runnable_insert = new Runnable() {
                    @Override
                    public void run() {
                        mainActivity.db.workoutDAO().insertAll(workout);
                    }
                };

                // 1. if name is not set, ask for name
                Thread thread;
                if (workout == null) {
                    System.out.println("new workout");
                    workout = new Workout("New workout");
                    thread = new Thread(runnable_insert);
                } else {
                    thread = new Thread(runnable_update);
                }

                // 2. if name is set, get current list of activities and set for workout
                workout.setActivities(list);

                System.out.println("Saving workout with name: " + workout.getName());
                for (Exercise e : list)
                    System.out.println("Saving exercise: " + e.getName());

                // 3. Save workout (update db)


                thread.start();


                mainActivity.onBackPressed();
            }
        });

        return view;
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


}