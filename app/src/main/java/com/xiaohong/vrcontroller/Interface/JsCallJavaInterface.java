package com.xiaohong.vrcontroller.Interface;

import android.content.Context;
import android.webkit.JavascriptInterface;

import com.xiaohong.vrcontroller.Variable;

/**
 * Created by Lpoint on 2017/5/16.
 * <p>
 * Copyright (c) 2017 Shanghai Xiaohong Technology company. All rights reserved.
 */

public class JsCallJavaInterface {
    private Context mContext;

    public JsCallJavaInterface(Context context) {
        mContext = context;
    }

    @JavascriptInterface
    public String getUserName() {
        return Variable.userName;
    }
}
