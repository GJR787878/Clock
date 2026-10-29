package com.gjr.clock;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ClockActivity extends AppCompatActivity {

    private View root;
    private TextView tvTime, tvDate;
    private GlassClockFaceView clockFace;
    private SettingsManager settingsManager;
    private boolean is24Hour;
    private SimpleDateFormat sdfTime;
    private SimpleDateFormat sdfDate;

    private final Handler handler = new Handler(Looper.getMainLooper());
    private final Runnable timeRunnable = new Runnable() {
        @Override
        public void run() {
            updateTime();
            // 对齐到下一个整秒，避免秒针越走越偏
            long delay = 1000 - (System.currentTimeMillis() % 1000);
            handler.postDelayed(this, delay);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clock);

        root = findViewById(R.id.root);
        tvTime = findViewById(R.id.tvTime);
        tvDate = findViewById(R.id.tvDate);
        clockFace = findViewById(R.id.clockFace);
        Button btn1224 = findViewById(R.id.btn1224);
        Button btnTheme = findViewById(R.id.btnTheme);

        settingsManager = new SettingsManager(this);
        is24Hour = settingsManager.is24Hour();
        rebuildFormats();
        applyTheme();

        btn1224.setOnClickListener(v -> {
            is24Hour = !is24Hour;
            settingsManager.set24Hour(is24Hour);
            rebuildFormats();
            updateTime();
        });
        btnTheme.setOnClickListener(v -> {
            settingsManager.setNightMode(!settingsManager.isNightMode());
            applyTheme();
        });
    }

    // SimpleDateFormat 会在创建时固定时区，所以每次回到前台都重建一次
    private void rebuildFormats() {
        Locale locale = Locale.getDefault();
        sdfTime = new SimpleDateFormat(is24Hour ? "HH:mm:ss" : "hh:mm:ss a", locale);
        sdfDate = new SimpleDateFormat("yyyy-MM-dd EEEE", locale);
    }

    private void applyTheme() {
        boolean night = settingsManager.isNightMode();
        NightModeHelper.applyMode(this, root, night, tvTime, tvDate);
        clockFace.setNight(night);
    }

    private void updateTime() {
        long ms = System.currentTimeMillis();
        Date now = new Date(ms);
        tvTime.setText(sdfTime.format(now));
        tvDate.setText(sdfDate.format(now));
        clockFace.updateTime(ms);
    }

    @Override
    protected void onResume() {
        super.onResume();
        rebuildFormats();
        handler.post(timeRunnable);
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(timeRunnable);
    }
}
