package com.gjr.clock;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ClockActivity extends AppCompatActivity {

    private TextView tvTime, tvDate;
    private SettingsManager settingsManager;
    private boolean is24Hour;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private final Runnable timeRunnable = new Runnable() {
        @Override
        public void run() {
            updateTime();
            handler.postDelayed(this, 1000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clock);
        tvTime = findViewById(R.id.tvTime);
        tvDate = findViewById(R.id.tvDate);
        Button btn1224 = findViewById(R.id.btn1224);
        Button btnTheme = findViewById(R.id.btnTheme);

        settingsManager = new SettingsManager(this);
        is24Hour = settingsManager.is24Hour();

        btn1224.setOnClickListener(v -> {
            is24Hour = !is24Hour;
            settingsManager.set24Hour(is24Hour);
        });
        btnTheme.setOnClickListener(v -> {
            boolean dark = settingsManager.isNightMode();
            settingsManager.setNightMode(!dark);
            recreate();
        });
    }

    private void updateTime() {
        String timePattern = is24Hour ? "HH:mm:ss" : "hh:mm:ss a";
        SimpleDateFormat sdfTime = new SimpleDateFormat(timePattern, Locale.getDefault());
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy‑MM‑dd EEEE", Locale.getDefault());
        Date now = new Date();
        tvTime.setText(sdfTime.format(now));
        tvDate.setText(sdfDate.format(now));
    }

    @Override
    protected void onResume() {
        super.onResume();
        handler.post(timeRunnable);
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(timeRunnable);
    }
}
