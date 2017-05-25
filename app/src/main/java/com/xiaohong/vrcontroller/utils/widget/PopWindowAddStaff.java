package com.xiaohong.vrcontroller.utils.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.xiaohong.vrcontroller.Interface.SubscriberOnNextListener;
import com.xiaohong.vrcontroller.R;
import com.xiaohong.vrcontroller.Variable;
import com.xiaohong.vrcontroller.bean.AddStaffBean;
import com.xiaohong.vrcontroller.bean.EditUserBean;
import com.xiaohong.vrcontroller.ui.ActivityHome;
import com.xiaohong.vrcontroller.utils.NetworkRequestMethods;
import com.xiaohong.vrcontroller.utils.ProgressSubscriber;
import com.xiaohong.vrcontroller.utils.Utils;

import org.w3c.dom.Text;

/**
 * Created by Lpoint on 2017/5/25.
 * <p>
 * Copyright (c) 2017 Shanghai Xiaohong Technology Company. All rights reserved.
 */

public class PopWindowAddStaff extends PopupWindow implements View.OnClickListener {
    private TextView txtTitle;
    private EditText edtUserName, edtNickName, edtPasswd, edtPhone;
    private View conentView;
    private Activity context;
    private SubscriberOnNextListener addStaffListener;
    private Button btnConfirm;

    public PopWindowAddStaff(final Activity context, SubscriberOnNextListener addStaffListener) {
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        conentView = inflater.inflate(R.layout.pop_edit_personal_info, null);
        txtTitle = (TextView) conentView.findViewById(R.id.txt_title);
        txtTitle.setText("新增员工");
        edtNickName = (EditText) conentView.findViewById(R.id.edt_nickname);
        edtNickName.setText("");
        edtUserName = (EditText) conentView.findViewById(R.id.edt_username);
        edtUserName.setText("");
        edtPasswd = (EditText) conentView.findViewById(R.id.edt_password);
        edtPasswd.setText("");
        edtPhone = (EditText) conentView.findViewById(R.id.edt_phone);
        edtPhone.setText("");
        this.addStaffListener = addStaffListener;
        btnConfirm = (Button) conentView.findViewById(R.id.btn_confirm);
        btnConfirm.setOnClickListener(this);
        int h = context.getWindowManager().getDefaultDisplay().getHeight();
        int w = context.getWindowManager().getDefaultDisplay().getWidth();
        this.setContentView(conentView);
        this.setWidth(Utils.dip2px(context, 420));
        this.setHeight(Utils.dip2px(context, 400));
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_confirm:
                if (TextUtils.isEmpty(edtNickName.getText().toString())
                        || TextUtils.isEmpty(edtUserName.getText().toString())
                        || TextUtils.isEmpty(edtPasswd.getText().toString())
                        || TextUtils.isEmpty(edtPhone.getText().toString())) {
                    Utils.showToastStr(context, "请填写完整信息");
                    return;
                }
                NetworkRequestMethods.getInstance().addStaff(new ProgressSubscriber<AddStaffBean>(addStaffListener, context, "添加员工中..."),
                        edtUserName.getText().toString(), edtPasswd.getText().toString(),
                        edtNickName.getText().toString(), edtPhone.getText().toString());
                dismiss();
                break;
            default:
                break;
        }
    }
}
