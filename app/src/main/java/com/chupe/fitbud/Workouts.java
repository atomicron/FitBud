package com.chupe.fitbud;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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

import com.chupe.fitbud.types.Workout;
import com.chupe.fitbud.views.TimePickerDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class Workouts extends Fragment {
    private static List<Workout> arrayList = new ArrayList<>();
    private FragmentAddWorkout fragmentAddWorkout;
    private FloatingActionButton fab;
    private ListView list;
    private CustomAdapter adapter;
    private Fragment currentFragment;
    private MainActivity mainActivity;

    public Workouts() {
        mainActivity = (MainActivity) getActivity();
//        arrayList.add(new Workout("Test"));
        fragmentAddWorkout = new FragmentAddWorkout(this);
        currentFragment = this;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_workouts, container, false);
        // ...
        MainActivity mainActivity = (MainActivity) getActivity();
        Runnable runnable = new Runnable(){
            public void run() {
                arrayList.clear();
                arrayList = mainActivity.db.workoutDAO().getAll();

                for (Workout w : arrayList)
                    System.out.println("Got workout: " + w.getName());
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        adapter = new CustomAdapter(getContext(), arrayList);

        fab = view.findViewById(R.id.floating_action_button);
        list = view.findViewById(R.id.list_workouts);

        list.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentAddWorkout = new FragmentAddWorkout(currentFragment);

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment, fragmentAddWorkout);
                fragmentTransaction.commit();
            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("Hold press item " + position);
                Workout wr = arrayList.get(position);
                fragmentAddWorkout = new FragmentAddWorkout(currentFragment, wr);

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment, fragmentAddWorkout);
                fragmentTransaction.commit();
            }
        });
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                // ask to delete
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                builder.setTitle("Confirm");
                builder.setMessage("Delete workout?");

                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing but close the dialog

                        Workout wr = arrayList.get(position);
                        arrayList.remove(position);

                        Runnable runnable = new Runnable() {
                            @Override
                            public void run() {
                                mainActivity.db.workoutDAO().deleteWorkout(wr);
                            }
                        };
                        Thread thread = new Thread(runnable);
                        thread.start();
                        try {
                            thread.join();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        adapter.notifyDataSetChanged();
                        
                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // Do nothing
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();

                return true;
            }
        });



        return view;
    }

    public void onDialogAcceptClick(TimePickerDialog dialog) {
        fragmentAddWorkout.onDialogAcceptClick(dialog);
    }

    public void onDialogExerciseAcceptClick(ExercisePickerDialog dialog) {
        fragmentAddWorkout.onDialogExerciseClick(dialog);
    }

    static class CustomAdapter extends BaseAdapter {
        Context context;
        List<Workout> wrArr;
        private LayoutInflater inflater;
        public CustomAdapter(Context context, List<Workout> wrArr) {
            this.context = context;
            this.wrArr = wrArr;
            inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return wrArr.size();
        }
        @Override
        public Object getItem(int position) {
            return wrArr.get(position);
        }
        @Override
        public long getItemId(int position) {
            return position;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View vi = convertView;
            if (vi == null)
                vi = inflater.inflate(R.layout.layout_workout_adapter, null);
            TextView textName = vi.findViewById(R.id.text_workout);
            textName.setText(wrArr.get(position).getName());
            return vi;
        }
    }
}