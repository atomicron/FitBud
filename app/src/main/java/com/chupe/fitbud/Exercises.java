package com.chupe.fitbud;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.chupe.fitbud.database.ExerciseDAO;
import com.chupe.fitbud.helpers.Helpers;
import com.chupe.fitbud.types.Exercise;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class Exercises extends Fragment
    implements EditExerciseDialogFragment.NoticeDialogListener {
    private static ArrayList<Exercise> arrayList = new ArrayList<>();
    List<Exercise> exercises;
    private Fragment fragmentAddExercise;
    private FloatingActionButton fab;
    private ListView list;
    private CustomAdapter adapter;

    public Exercises() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        adapter = new CustomAdapter(getContext(), arrayList);
        updateExercisesList();
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exercises, container, false);

        fragmentAddExercise = new FragmentAddExercise(this);
        list = view.findViewById(R.id.list_exercises);
        fab = view.findViewById(R.id.floating_action_button);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment, fragmentAddExercise);
                fragmentTransaction.commit();
            }
        });

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Exercise tmp = arrayList.get(position);
                EditExerciseDialogFragment dialog = new EditExerciseDialogFragment();
                dialog.setExercise(tmp);
                dialog.show(getActivity().getSupportFragmentManager(), "Some string?");

                return false;
            }
        });

        updateExercisesList();

        list.setAdapter(adapter);


        return view;
    }

    @Override
    public void onDialogPositiveClick(EditExerciseDialogFragment dialog) {
        Exercise ex = dialog.getExercise();
        ex.setName(dialog.getName());
        ex.setDuration(dialog.getDuration());
        // update database
        MainActivity activity = (MainActivity) getActivity();
        ExerciseDAO exerciseDAO = activity.db.exerciseDAO();

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                exerciseDAO.updateExercise(ex);
            }
        };

        Thread thread = new Thread(runnable);
        thread.start();
        checkToInvalidate(thread);
        updateExercisesList();
    }

    @Override
    public void onDialogNegativeClick(EditExerciseDialogFragment dialog) {
        Exercise ex = dialog.getExercise();
        // update database
        MainActivity activity = (MainActivity) getActivity();
        ExerciseDAO exerciseDAO = activity.db.exerciseDAO();

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                exerciseDAO.deleteExercise(ex);
            }
        };

        Thread thread = new Thread(runnable);
        thread.start();
        checkToInvalidate(thread);
        updateExercisesList();
    }


    void updateExercisesList() {
        MainActivity activity = (MainActivity) getActivity();
        ExerciseDAO exerciseDAO = activity.db.exerciseDAO();
        Runnable runnable = new Runnable(){
            public void run() {
                exercises = exerciseDAO.getAll();
                arrayList.clear();
                for (int i = 0; i < exercises.size(); ++i)
                    arrayList.add(exercises.get(i));

            }
        };
        Thread thread = new Thread(runnable);
        thread.start();

        checkToInvalidate(thread);
    }

    private void checkToInvalidate(Thread thread) {
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        adapter.notifyDataSetChanged();
    }

    static class CustomAdapter extends BaseAdapter {
        Context context;
        ArrayList<Exercise> exArr;
        private LayoutInflater inflater = null;
        public CustomAdapter(Context context, ArrayList<Exercise> exArr) {
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