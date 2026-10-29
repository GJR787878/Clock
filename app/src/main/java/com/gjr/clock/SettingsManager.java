package com.gjr.clock;

import android.content.Context;
import android.content.SharedPreferences;

public class SettingsManager {
    private static final String SP_NAME = "clock_settings";
    private static final String KEY_24H = "is_24_hour";
    private static final String KEY_KEEP_SCREEN = "keep_screen_on";
    private static final String KEY_NIGHT_MODE = "night_mode";
    private static final String KEY_FONT_SIZE = "font_size";

    private final SharedPreferences sp;

    public SettingsManager(Context context) {
        sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
    }

    public boolean is24Hour() {
        return sp.getBoolean(KEY_24H, true);
    }

    public void set24Hour(boolean value) {
        sp.edit().putBoolean(KEY_24H, value).apply();
    }

    public boolean isKeepScreenOn() {
        return sp.getBoolean(KEY_KEEP_SCREEN, true);
    }

    public void setKeepScreenOn(boolean value) {
        sp.edit().putBoolean(KEY_KEEP_SCREEN, value).apply();
    }

    public boolean isNightMode() {
        return sp.getBoolean(KEY_NIGHT_MODE, true);
    }

    public void setNightMode(boolean value) {
        sp.edit().putBoolean(KEY_NIGHT_MODE, value).apply();
    }

    public float getFontSize() {
        return sp.getFloat(KEY_FONT_SIZE, 64f);
    }

    public void setFontSize(float size) {
        sp.edit().putFloat(KEY_FONT_SIZE, size).apply();
    }
}
