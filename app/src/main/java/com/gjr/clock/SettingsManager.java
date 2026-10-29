package com.gjr.clock;

import android.content.Context;
import android.content.SharedPreferences;

public class SettingsManager {
    private static final String SP_NAME = "clock_sp";
    private static final String KEY_24H = "is_24h";
    private static final String KEY_NIGHT = "night_mode";
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
    // ✅ 这里！使用传入的b，不再写死true！
    public void setNightMode(boolean b) {
        sp.edit().putBoolean(KEY_NIGHT, b).apply();
    }
}
