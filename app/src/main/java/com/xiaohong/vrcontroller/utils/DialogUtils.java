package com.xiaohong.vrcontroller.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;

/**
 * Created by Lpoint on 2017/5/16.
 * <p>
 * Copyright (c) 2017 Shanghai Xiaohong Technology company. All rights reserved.
 */

public class DialogUtils {

    public static AlertDialog.Builder getDialog(Context context, String Title, String Message, String NegativeButton, DialogInterface.OnClickListener negativeButtonClick
            , String PositiveButton, DialogInterface.OnClickListener positiveButtonClick) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if (!TextUtils.isEmpty(Title))
            builder.setTitle(Title);
        if (!TextUtils.isEmpty(Message))
            builder.setMessage(Message);
        if (!TextUtils.isEmpty(NegativeButton))
            builder.setNegativeButton(NegativeButton, (negativeButtonClick == null ? null : negativeButtonClick));
        if (!TextUtils.isEmpty(PositiveButton))
            builder.setPositiveButton(PositiveButton, (positiveButtonClick == null ? null : positiveButtonClick));
        return builder;
    }
}
