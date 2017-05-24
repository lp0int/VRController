package com.xiaohong.vrcontroller.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiaohong.vrcontroller.Constants;
import com.xiaohong.vrcontroller.Interface.SubscriberOnNextListener;
import com.xiaohong.vrcontroller.R;
import com.xiaohong.vrcontroller.Variable;
import com.xiaohong.vrcontroller.bean.DeleteUserBean;
import com.xiaohong.vrcontroller.bean.EditUserBean;
import com.xiaohong.vrcontroller.utils.NetworkRequestMethods;
import com.xiaohong.vrcontroller.utils.ProgressSubscriber;
import com.xiaohong.vrcontroller.utils.Utils;
import com.xiaohong.vrcontroller.utils.widget.PopWindowConfirm;
import com.xiaohong.vrcontroller.utils.widget.PopWindowEditEggChair;

import java.util.ArrayList;

/**
 * Created by Lpoint on 2017/5/17.
 * <p>
 * Copyright (c) 2017 Shanghai Xiaohong Technology company. All rights reserved.
 */

public class StaffManagementAdapter extends BaseAdapter {
    private ArrayList<String> mList;
    private Context mContext;
    private ArrayList<Boolean> isTitle;
    private SubscriberOnNextListener editChairListener;
    private SubscriberOnNextListener deleteUserListener;
    private SubscriberOnNextListener resetUserPwdListener;

    public StaffManagementAdapter(ArrayList<String> list, Context context, ArrayList<Boolean> isTitle,
                                  SubscriberOnNextListener editChairListener, SubscriberOnNextListener deleteUserListener,
                                  SubscriberOnNextListener resetUserPwdListener) {
        mList = list;
        mContext = context;
        this.isTitle = isTitle;
        this.editChairListener = editChairListener;
        this.deleteUserListener = deleteUserListener;
        this.resetUserPwdListener = resetUserPwdListener;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.fragment_staff_management_item, null);
            holder = new ViewHolder();
            convertView.setTag(holder);
            holder.linItem = (RelativeLayout) convertView.findViewById(R.id.lin_item);
            holder.txtItem = (TextView) convertView.findViewById(R.id.txt_item);
            holder.lineBottom = convertView.findViewById(R.id.line_bottom);
            holder.lineLeft = convertView.findViewById(R.id.line_left);
            holder.imgEdit = (ImageView) convertView.findViewById(R.id.img_edit);
            holder.txtDel = (TextView) convertView.findViewById(R.id.txt_del);
            holder.txtDel.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
            holder.txtReset = (TextView) convertView.findViewById(R.id.txt_reset);
            holder.txtReset.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.txtItem.setText(mList.get(position));
        if (isTitle.get(position)) {
            holder.txtItem.setTextSize(14);
            holder.txtItem.setTextColor(Color.parseColor("#666666"));
            holder.linItem.setBackgroundColor(Color.parseColor("#F6F6F6"));
            RelativeLayout.LayoutParams linearParams = (RelativeLayout.LayoutParams) holder.txtItem.getLayoutParams(); //取控件textView当前的布局参数
            linearParams.height = Utils.dip2px(mContext, 40);
            holder.txtItem.setLayoutParams(linearParams);
            holder.lineBottom.setVisibility(View.INVISIBLE);
            if (position == 0)
                holder.lineLeft.setVisibility(View.VISIBLE);
        } else {
            if (position % 6 == 2)
                holder.imgEdit.setVisibility(View.VISIBLE);
            else
                holder.imgEdit.setVisibility(View.GONE);
            if (position % 6 == 5) {
                holder.txtDel.setVisibility(View.VISIBLE);
                holder.txtReset.setVisibility(View.VISIBLE);
                holder.txtItem.setVisibility(View.INVISIBLE);
            } else {
                holder.txtDel.setVisibility(View.GONE);
                holder.txtReset.setVisibility(View.GONE);
                holder.txtItem.setVisibility(View.VISIBLE);
            }
            holder.txtItem.setTextSize(15);
            holder.txtItem.setTextColor(Color.parseColor("#393A3C"));
            holder.linItem.setBackgroundColor(Color.parseColor("#FFFFFF"));
            RelativeLayout.LayoutParams linearParams = (RelativeLayout.LayoutParams) holder.txtItem.getLayoutParams(); //取控件textView当前的布局参数
            linearParams.height = Utils.dip2px(mContext, 84);
            holder.txtItem.setLayoutParams(linearParams);
            final ImageView img = holder.imgEdit;
            holder.imgEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopWindowEditEggChair pop = new PopWindowEditEggChair((Activity) mContext, Variable.mfindUsersBean.getFindUser().get(position / 6).getUserName(), editChairListener);
                    pop.showPopupWindow(img);
                }
            });
        }
        holder.txtDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { final PopWindowConfirm popWindowConfirm = new PopWindowConfirm((Activity) mContext, "确认删除此员工？");
                popWindowConfirm.showPopupWindow(v);
                popWindowConfirm.setConfirmAction(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popWindowConfirm.dismiss();
                        NetworkRequestMethods.getInstance().deleteUser(new ProgressSubscriber<DeleteUserBean>(deleteUserListener, mContext, "删除请求中..."),
                                Variable.mfindUsersBean.getFindUser().get(position / 6).getUserName());
                    }
                });
            }
        });
        holder.txtReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final PopWindowConfirm popWindowConfirm = new PopWindowConfirm((Activity) mContext, "确认重置密码？");
                popWindowConfirm.showPopupWindow(v);
                popWindowConfirm.setConfirmAction(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popWindowConfirm.dismiss();
                        NetworkRequestMethods.getInstance().editUserPassword(new ProgressSubscriber<EditUserBean>(resetUserPwdListener, mContext, "重置中..."),
                                Variable.mfindUsersBean.getFindUser().get(position / 6).getUserName(), Constants.USER_DEFAULT_PWD);
                    }
                });
            }
        });
        return convertView;
    }

    class ViewHolder {
        private TextView txtItem;
        private RelativeLayout linItem;
        private View lineLeft;
        private View lineBottom;
        private ImageView imgEdit;
        private TextView txtDel;
        private TextView txtReset;
    }
}
