package com.gjr.clock;

import android.content.Context;
import android.content.SharedPreferences;

public class SettingsManager {
    private static final String SP_NAME = "clock_sp";
    private static final String KEY_24H = "is_24h";
    private static final String KEY_NIGHT = "night_mode";
    private static final String KEY_FONT = "font_size";
    private static final String KEY_KEEP_ON = "keep_screen_on";
    private static final int FONT_DEFAULT = 64;
    private final SharedPreferences sp;

    public SettingsManager(Context ctx) {
        sp = ctx.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
    }

    public boolean is24Hour() {
        return sp.getBoolean(KEY_24H, true);
    }

    public void set24Hour(boolean b) {
        sp.edit().putBoolean(KEY_24H, b).apply();
    }

    public boolean isNightMode() {
        return sp.getBoolean(KEY_NIGHT, true);
    }

    public void setNightMode(boolean b) {
        sp.edit().putBoolean(KEY_NIGHT, b).apply();
    }

    /** 时间文字字号（sp），范围 32～120 */
    public int getFontSize() {
        return sp.getInt(KEY_FONT, FONT_DEFAULT);
    }

    public void setFontSize(int sizeSp) {
        sp.edit().putInt(KEY_FONT, sizeSp).apply();
    }

    /** 屏幕常亮（硬性要求，默认开） */
    public boolean isKeepScreenOn() {
        return sp.getBoolean(KEY_KEEP_ON, true);
    }

    public void setKeepScreenOn(boolean b) {
        sp.edit().putBoolean(KEY_KEEP_ON, b).apply();
    }
}
