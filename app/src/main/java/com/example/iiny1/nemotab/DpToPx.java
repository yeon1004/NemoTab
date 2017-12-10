package com.example.iiny1.nemotab;

/**
 * Created by iiny1 on 2017-12-02.
 */

import android.content.Context;
import android.util.DisplayMetrics;

public class DpToPx {
    public static int getPx(Context mcontext,float dp){
        DisplayMetrics metrics = mcontext.getResources().getDisplayMetrics();
        float mdp = dp;
        float fpixels = metrics.density * mdp;
        int pixels = (int) (fpixels + 0.5f);
        return pixels;
    }
}