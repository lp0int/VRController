package com.xiaohong.vrcontroller.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.xiaohong.vrcontroller.Variable;

/**
 * Created by Lpoint on 2017/5/16.
 * <p>
 * Copyright (c) 2017 Shanghai Xiaohong Technology company. All rights reserved.
 */

public class DebugTools {
    public static final boolean DEBUG_MODE = true;
    public static void showDebugToast(Context context, String s) {
        if (DEBUG_MODE) {
            Utils.showToastStr(context, s);
        }
    }

    public static void showDebugLog(String tag, String info) {
        if (DEBUG_MODE) {
            Log.i(tag, info);
        }
    }
}
