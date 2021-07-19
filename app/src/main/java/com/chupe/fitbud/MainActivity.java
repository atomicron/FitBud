package com.chupe.fitbud;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.room.Room;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.chupe.fitbud.database.AppDatabase;
import com.chupe.fitbud.views.TimePickerDialog;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements
    EditExerciseDialogFragment.NoticeDialogListener
    , TimePickerDialog.TimePickerDialogInterface
    , ExercisePickerDialog.ExercisePickerDialogInterface
{

    public AppDatabase db;


    private NavigationView navView;
    private MenuItem navItemHome;
    private MenuItem navItemWorkouts;
    private MenuItem navItemExercises;
    private FragmentManager fragmentManager;

    private MaterialToolbar topBar;

    private DrawerLayout drawer;
    private BottomNavigationView bottomNavigationView;
    private MenuItem page1, page2;

    private Exercises fragmentExercises= new  Exercises();
    private Workouts fragmentWorkouts = new Workouts();
    private Home fragmentHome= new  Home();
    private Timer fragmentTimer = new Timer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String databaseName = "database-name";
        db = Room.databaseBuilder(this, AppDatabase.class, databaseName)
//            .fallbackToDestructiveMigration()
            .build();


        navView = findViewById(R.id.navigationView);
        navItemHome = navView.getMenu().findItem(R.id.nav_item_home);
        navItemWorkouts = navView.getMenu().findItem(R.id.nav_item_workouts);
        navItemExercises = navView.getMenu().findItem(R.id.nav_item_exercises);

        fragmentManager = getSupportFragmentManager();

        drawer = findViewById(R.id.drawer_layout);
        topBar = findViewById(R.id.topBar);

        bottomNavigationView = findViewById(R.id.bottomNavigation);
        page1 = bottomNavigationView.getMenu().findItem(R.id.page_1);
        page2 = bottomNavigationView.getMenu().findItem(R.id.page_2);

        navItemExercises.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                drawer.close();

                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment, fragmentExercises);
                fragmentTransaction.commit();

                return false;
            }
        });
        navItemWorkouts.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                drawer.close();

                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment, fragmentWorkouts);
                fragmentTransaction.commit();

                return false;
            }
        });
        navItemHome.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                drawer.close();

                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment, fragmentHome);
                fragmentTransaction.commit();

                return false;
            }
        });
        topBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(GravityCompat.START);
            }
        });
        page1.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment, fragmentHome);
                fragmentTransaction.commit();
                return false;
            }
        });
        page2.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment, fragmentTimer);
                fragmentTransaction.commit();
                return false;
            }
        });
    }


    @Override
    public void onDialogPositiveClick(EditExerciseDialogFragment dialog) {
        fragmentExercises.onDialogPositiveClick(dialog);
    }

    @Override
    public void onDialogNegativeClick(EditExerciseDialogFragment dialog) {
        fragmentExercises.onDialogNegativeClick(dialog);
    }

    @Override
    public void onDialogAcceptClick(TimePickerDialog dialog) {
        fragmentWorkouts.onDialogAcceptClick(dialog);
    }

    @Override
    public void onDialogAcceptClick(ExercisePickerDialog dialog) {
        fragmentWorkouts.onDialogExerciseAcceptClick(dialog);
    }
}
