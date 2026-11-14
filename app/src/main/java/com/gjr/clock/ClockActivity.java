package com.gjr.clock;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.TextViewCompat;

import com.gjr.glassbutton.GlassCapsuleButton;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ClockActivity extends AppCompatActivity {

    private static final int FONT_MIN_SP = 32;
    private static final int FONT_MAX_SP = 120;

    private View root;
    private TextView tvTime, tvDate, tvFontLabel, tvFontValue;
    private GlassClockFaceView clockFace;
    private GlassCapsuleButton btn1224, btnTheme;
    private SeekBar seekFont;

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

        // 硬性要求：屏幕常亮（窗口 Flag + 布局 keepScreenOn 双保险）
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.activity_clock);

        root = findViewById(R.id.root);
        tvTime = findViewById(R.id.tvTime);
        tvDate = findViewById(R.id.tvDate);
        tvFontLabel = findViewById(R.id.tvFontLabel);
        tvFontValue = findViewById(R.id.tvFontValue);
        clockFace = findViewById(R.id.clockFace);
        btn1224 = findViewById(R.id.btn1224);
        btnTheme = findViewById(R.id.btnTheme);
        seekFont = findViewById(R.id.seekFont);

        settingsManager = new SettingsManager(this);
        is24Hour = settingsManager.is24Hour();

        rebuildFormats();
        applyTheme();
        initFontSlider();

        btn1224.setOnClickListener(v -> {
            is24Hour = !is24Hour;
            settingsManager.set24Hour(is24Hour);
            rebuildFormats();
            updateTime();
            applyTheme();
        });

        btnTheme.setOnClickListener(v -> {
            settingsManager.setNightMode(!settingsManager.isNightMode());
            applyTheme();
        });
    }

    /** 字体大小滑块：实时生效 + 松手持久化 */
    private void initFontSlider() {
        seekFont.setMax(FONT_MAX_SP - FONT_MIN_SP);

        int size = settingsManager.getFontSize();
        if (size < FONT_MIN_SP) {
            size = FONT_MIN_SP;
        } else if (size > FONT_MAX_SP) {
            size = FONT_MAX_SP;
        }
        seekFont.setProgress(size - FONT_MIN_SP);
        tvFontValue.setText(String.valueOf(size));
        applyFontSize(size);

        seekFont.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar bar, int progress, boolean fromUser) {
                int sp = FONT_MIN_SP + progress;
                tvFontValue.setText(String.valueOf(sp));
                applyFontSize(sp);
            }

            @Override
            public void onStartTrackingTouch(SeekBar bar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar bar) {
                settingsManager.setFontSize(FONT_MIN_SP + bar.getProgress());
            }
        });
    }

    /** 滑块控制时间文字的期望字号上限，宽度不够时仍会自动缩小，不会溢出 */
    private void applyFontSize(int maxSp) {
        TextViewCompat.setAutoSizeTextTypeUniformWithConfiguration(
                tvTime, 24, Math.max(maxSp, 24), 1, TypedValue.COMPLEX_UNIT_SP);
    }

    // SimpleDateFormat 会在创建时固定时区，所以每次回到前台都重建一次
    private void rebuildFormats() {
        Locale locale = Locale.getDefault();
        sdfTime = new SimpleDateFormat(is24Hour ? "HH:mm:ss" : "hh:mm:ss a", locale);
        sdfDate = new SimpleDateFormat("yyyy-MM-dd EEEE", locale);
    }

    private void applyTheme() {
        boolean night = settingsManager.isNightMode();
        NightModeHelper.applyMode(this, root, night, tvTime, tvDate, tvFontLabel, tvFontValue);
        clockFace.setNight(night);

        // 按钮文字直接显示当前状态，选中态用毛玻璃高亮
        btn1224.setText(is24Hour ? R.string.btn_24 : R.string.btn_12);
        btn1224.setGlassSelected(is24Hour);
        btnTheme.setText(night ? R.string.btn_night : R.string.btn_day);
        btnTheme.setGlassSelected(night);
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
