package com.gjr.clock;

import android.app.Activity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class NightModeHelper {
    public static void applyMode(Activity activity, boolean isNight) {
        Window window = activity.getWindow();
        if (isNight) {
            window.getDecorView().setSystemUiVisibility(0);
            window.setBackgroundDrawableResource(android.R.color.black);
        } else {
            window.getDecorView().setSystemUiVisibility(
                    WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS
                    | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }
}
