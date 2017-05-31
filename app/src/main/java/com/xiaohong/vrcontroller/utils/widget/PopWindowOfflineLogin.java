package com.xiaohong.vrcontroller.utils.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.xiaohong.vrcontroller.Constants;
import com.xiaohong.vrcontroller.Interface.SubscriberOnNextListener;
import com.xiaohong.vrcontroller.R;
import com.xiaohong.vrcontroller.Variable;
import com.xiaohong.vrcontroller.bean.EditUserBean;
import com.xiaohong.vrcontroller.bean.FindUsersBean;
import com.xiaohong.vrcontroller.ui.ActivityHome;
import com.xiaohong.vrcontroller.ui.ActivityLogin;
import com.xiaohong.vrcontroller.utils.NetworkRequestMethods;
import com.xiaohong.vrcontroller.utils.ProgressSubscriber;
import com.xiaohong.vrcontroller.utils.SharedPreferencesUtils;
import com.xiaohong.vrcontroller.utils.Utils;
import com.xiaohong.vrcontroller.utils.net.MsgFactory;

/**
 * Created by Lpoint on 2017/5/31.
 * <p>
 * Copyright (c) 2017 Shanghai Xiaohong Technology Company. All rights reserved.
 */

public class PopWindowOfflineLogin extends PopupWindow implements View.OnClickListener {
    private TextView txtTitle;
    private EditText edtAdmin, edtUserName;
    private View conentView;
    private Activity context;
    private Button btnConfirm;

    public PopWindowOfflineLogin(final Activity context) {
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        conentView = inflater.inflate(R.layout.pop_offline_login, null);
        txtTitle = (TextView) conentView.findViewById(R.id.txt_title);
        txtTitle.setText("离线登录");

        edtAdmin = (EditText) conentView.findViewById(R.id.edt_admin);
        edtUserName = (EditText) conentView.findViewById(R.id.edt_username);

        btnConfirm = (Button) conentView.findViewById(R.id.btn_confirm);
        btnConfirm.setOnClickListener(this);
        int h = context.getWindowManager().getDefaultDisplay().getHeight();
        int w = context.getWindowManager().getDefaultDisplay().getWidth();
        this.setContentView(conentView);
        this.setWidth(Utils.dip2px(context, 420));
        this.setHeight(Utils.dip2px(context, 280));
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

    /**
     * 显示popupWindow
     *
     * @param parent
     */
    public void showPopupWindow(View parent) {
        if (!this.isShowing()) {
            this.showAtLocation(((ViewGroup) context.findViewById(android.R.id.content)).getChildAt(0), Gravity.CENTER, 0, 0);
        } else {
            this.dismiss();
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_confirm:
                Variable.userName = edtUserName.getText().toString();
                dismiss();
                String findUsersBeanStr = SharedPreferencesUtils.getStringValue(context, Constants.DEVICE_INFO, Constants.CHAIR_LIST, "");
                Variable.setFindUserBean(MsgFactory.getInstance(context).mGson.fromJson(findUsersBeanStr, FindUsersBean.class));
                PopWindowEditEggChairOffLine popWindowEditEggChairOffLine = new PopWindowEditEggChairOffLine(context,Variable.userName);
                popWindowEditEggChairOffLine.showPopupWindow(v);
                break;
            default:
                break;
        }
    }
}
