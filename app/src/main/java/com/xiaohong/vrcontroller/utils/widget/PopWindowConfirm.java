package com.xiaohong.vrcontroller.utils.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.xiaohong.vrcontroller.R;
import com.xiaohong.vrcontroller.Variable;
import com.xiaohong.vrcontroller.ui.ActivityHome;
import com.xiaohong.vrcontroller.utils.Utils;

/**
 * Created by Lpoint on 2017/5/22.
 * <p>
 * Copyright (c) 2017 Shanghai Xiaohong Technology Company. All rights reserved.
 */

public class PopWindowConfirm extends PopupWindow {
    private TextView txtTitle;
    private View conentView;
    private Activity context;
    private TextView txtContent;
    private Button btnConfirm;

    public PopWindowConfirm(final Activity context,String content) {
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        conentView = inflater.inflate(R.layout.pop_edit_confirm, null);
        txtTitle = (TextView) conentView.findViewById(R.id.txt_title);
        txtTitle.setText("操作确认");
        txtContent = (TextView) conentView.findViewById(R.id.txt_content);
        txtContent.setText(content);
        btnConfirm = (Button) conentView.findViewById(R.id.btn_confirm);
        int h = context.getWindowManager().getDefaultDisplay().getHeight();
        int w = context.getWindowManager().getDefaultDisplay().getWidth();
        this.setContentView(conentView);
        this.setWidth(Utils.dip2px(context, 350));
        this.setHeight(Utils.dip2px(context, 200));
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        this.update();
        ColorDrawable dw = new ColorDrawable(0000000000);
        this.setBackgroundDrawable(dw);
        this.setAnimationStyle(R.style.AnimationPreview);
        conentView.findViewById(R.id.txt_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                dismiss();
            }
        });
    }

    public void setConfirmAction(View.OnClickListener listener){
        btnConfirm.setOnClickListener(listener);
    }

    /**
     * 显示popupWindow
     *
     * @param parent
     */
    public void showPopupWindow(View parent) {
        if (!this.isShowing()) {
            ActivityHome.viewCover.setVisibility(View.VISIBLE);
            this.showAtLocation(((ViewGroup) context.findViewById(android.R.id.content)).getChildAt(0), Gravity.CENTER, 0, 0);
        } else {
            this.dismiss();
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
        ActivityHome.viewCover.setVisibility(View.GONE);
    }

}