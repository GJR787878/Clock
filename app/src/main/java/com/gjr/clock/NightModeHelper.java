package com.gjr.clock;

import android.app.Activity;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import androidx.core.view.WindowInsetsControllerCompat;

public class NightModeHelper {
    public static void applyMode(Activity activity, View root, boolean isNight, TextView... texts) {
        int bg = isNight ? 0xFF000000 : 0xFFFFFFFF;
        int fg = isNight ? 0xFFFFFFFF : 0xFF000000;

        root.setBackgroundColor(bg);
        for (TextView t : texts) {
            t.setTextColor(fg);
        }

        Window window = activity.getWindow();
        window.setStatusBarColor(bg);
        new WindowInsetsControllerCompat(window, window.getDecorView())
                .setAppearanceLightStatusBars(!isNight);
    }
}
