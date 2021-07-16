package com.chupe.fitbud;

import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.chupe.fitbud.helpers.Helpers;
import com.chupe.fitbud.views.CustomTimePicker;

public class Timer extends Fragment {
    public Timer() {}

    long startTime;
    //runs without a timer by reposting this handler at the end of the runnable
    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            startTime -= 500;
            long millis = startTime;
            int seconds = (int) (millis / 1000);
            int minutes = seconds / 60;
            seconds = seconds % 60;
            text.setText(String.format("%d:%02d", minutes, seconds));
            timerHandler.postDelayed(this, 500);

            if (startTime <= 0) {
                timerHandler.removeCallbacks(timerRunnable);
                Helpers.playNotification(getContext());
            }
        }
    };

    // main view
    private View view;

    // views
    private CustomTimePicker seconds;
    private CustomTimePicker minutes;
    private TextView text;
    private Button btn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_timer, container, false);

        text = view.findViewById(R.id.text);
        minutes = view.findViewById(R.id.listMinutes);
        seconds = view.findViewById(R.id.listSeconds);

        btn = view.findViewById(R.id.btnStart);
        btn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                int min = minutes.getCurrent();
                int sec = seconds.getCurrent();

                startTime = sec * 1000 + (min * 60) * 1000;

                timerHandler.removeCallbacks(timerRunnable);
                timerHandler.postDelayed(timerRunnable, 0);
            }
        });

        return view;
    }
}