package com.gjr.clock;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.WindowManager;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.gjr.glassbutton.GlassCapsuleButton;
import com.gjr.glassbutton.GlassRadioButton;
import com.gjr.glassbutton.GlassNavBar;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ClockActivity extends AppCompatActivity {
    private TextView tvTime, tvDate;
    private GlassClockFaceView clockFace;
    private GlassRadioButton rb24, rb12;
    private GlassCapsuleButton btnKeepScreen, btnNightMode;
    private SeekBar seekFontSize;
    private GlassNavBar navBar;

    private final Handler mHandler = new Handler(Looper.getMainLooper());
    private SettingsManager settings;
    private boolean isRunning = true;

    private final Runnable timeRunnable = new Runnable() {
        @Override
        public void run() {
            if (isRunning) {
                updateClockUI();
            }
            mHandler.postDelayed(this, 1000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clock);
        settings = new SettingsManager(this);

        bindView();
        loadSettings();
        initEvent();
        NightModeHelper.applyMode(this, settings.isNightMode());

        mHandler.post(timeRunnable);
    }

    private void bindView() {
        tvTime = findViewById(R.id.tv_time);
        tvDate = findViewById(R.id.tv_date);
        clockFace = findViewById(R.id.clock_face);
        rb24 = findViewById(R.id.rb_24);
        rb12 = findViewById(R.id.rb_12);
        btnKeepScreen = findViewById(R.id.btn_keep_screen);
        btnNightMode = findViewById(R.id.btn_night_mode);
        seekFontSize = findViewById(R.id.seek_font);
        navBar = findViewById(R.id.glass_nav);
    }

    private void loadSettings() {
        rb24.setChecked(settings.is24Hour());
        rb12.setChecked(!settings.is24Hour());
        setKeepScreen(settings.isKeepScreenOn());
        btnNightMode.setText(settings.isNightMode() ? "日间模式" : "夜间模式");
        tvTime.setTextSize(settings.getFontSize());
        seekFontSize.setProgress((int) settings.getFontSize());
    }

    private void setKeepScreen(boolean enable) {
        if (enable) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            btnKeepScreen.setText("关闭常亮");
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            btnKeepScreen.setText("开启常亮");
        }
    }

    private void initEvent() {
        RadioGroup rg = findViewById(R.id.rg_format);
        rg.setOnCheckedChangeListener((g, id) -> {
            boolean is24 = id == R.id.rb_24;
            settings.set24Hour(is24);
            updateClockUI();
        });

        btnKeepScreen.setOnClickListener(v -> {
            boolean newVal = !settings.isKeepScreenOn();
            setKeepScreen(newVal);
            settings.setKeepScreenOn(newVal);
        });

        btnNightMode.setOnClickListener(v -> {
            boolean night = !settings.isNightMode();
            settings.setNightMode(night);
            NightModeHelper.applyMode(this, night);
            btnNightMode.setText(night ? "日间模式" : "夜间模式");
        });

        seekFontSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress < 30) progress = 30;
                tvTime.setTextSize(progress);
                settings.setFontSize(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        // 导航栏示例，自行替换图标
        navBar.addItem(getDrawable(R.drawable.ic_clock), "时钟");
        navBar.addItem(getDrawable(R.drawable.ic_timer), "计时器");
        navBar.addItem(getDrawable(R.drawable.ic_stopwatch), "秒表");
        navBar.setSelected(0);
        navBar.setOnItemSelectedListener(index -> {
        });
    }

    private void updateClockUI() {
        Date now = new Date();
        SimpleDateFormat sdfTime;
        if (settings.is24Hour()) {
            sdfTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        } else {
            sdfTime = new SimpleDateFormat("hh:mm:ss a", Locale.getDefault());
        }
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd EEEE", Locale.getDefault());
        tvTime.setText(sdfTime.format(now));
        tvDate.setText(sdfDate.format(now));
        clockFace.updateTime();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacks(timeRunnable);
    }
}
