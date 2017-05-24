package com.xiaohong.vrcontroller;

import android.app.Application;

/**
 * Created by Lpoint on 2017/5/16.
 * <p>
 * Copyright (c) 2017 Shanghai Xiaohong Technology company. All rights reserved.
 */

public class ControlClientApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Variable.BASECONTEXT = getBaseContext();
    }
}
