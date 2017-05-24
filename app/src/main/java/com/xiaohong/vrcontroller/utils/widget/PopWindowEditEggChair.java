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
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.xiaohong.vrcontroller.Constants;
import com.xiaohong.vrcontroller.Interface.SubscriberOnNextListener;
import com.xiaohong.vrcontroller.R;
import com.xiaohong.vrcontroller.Variable;
import com.xiaohong.vrcontroller.bean.EditChairBean;
import com.xiaohong.vrcontroller.ui.ActivityHome;
import com.xiaohong.vrcontroller.utils.NetworkRequestMethods;
import com.xiaohong.vrcontroller.utils.ProgressSubscriber;
import com.xiaohong.vrcontroller.utils.Utils;

/**
 * Created by Lpoint on 2017/5/19.
 * <p>
 * Copyright (c) 2017 Shanghai Xiaohong Technology Company. All rights reserved.
 */

public class PopWindowEditEggChair extends PopupWindow implements View.OnClickListener {
    private TextView txtTitle;
    private View conentView;
    private Activity context;
    private ListView list;
    private Button btnSave;
    private ChairListAdapter mAdapter;
    private String myEggChair;
    private SubscriberOnNextListener editChairListener;
    private String userName;

    public PopWindowEditEggChair(final Activity context, String userName, SubscriberOnNextListener editChairListener) {
        this.context = context;
        this.editChairListener = editChairListener;
        this.userName = userName;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        conentView = inflater.inflate(R.layout.pop_edit_egg_chair, null);
        txtTitle = (TextView) conentView.findViewById(R.id.txt_title);
        txtTitle.setText("编辑蛋椅");
        int h = context.getWindowManager().getDefaultDisplay().getHeight();
        int w = context.getWindowManager().getDefaultDisplay().getWidth();
        this.setContentView(conentView);
        this.setWidth(Utils.dip2px(context, 350));
        this.setHeight(Utils.dip2px(context, 383));
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        this.update();
        ColorDrawable dw = new ColorDrawable(0000000000);
        this.setBackgroundDrawable(dw);
        this.setAnimationStyle(R.style.AnimationPreview);
        myEggChair = Variable.getChairListByUsername(userName).replace(Constants.nullEggChairId + ",", "");
        myEggChair = Variable.getChairListByUsername(userName).replace(Constants.nullEggChairId, "");

        conentView.findViewById(R.id.txt_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                dismiss();
            }
        });

        list = (ListView) conentView.findViewById(R.id.list);
        btnSave = (Button) conentView.findViewById(R.id.btn_save);
        btnSave.setOnClickListener(this);
        mAdapter = new ChairListAdapter();
        list.setAdapter(mAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectChair(position);
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
            case R.id.btn_save:
                dismiss();
                NetworkRequestMethods.getInstance().editEggChair(new ProgressSubscriber<EditChairBean>(editChairListener,
                        context, "更改请求中..."), userName, myEggChair);
                break;
            default:
                break;
        }
    }

    class ChairListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return Variable.chairs.size();
        }

        @Override
        public Object getItem(int position) {
            return Variable.chairs.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = View.inflate(context, R.layout.edit_chair_item, null);
                holder = new ViewHolder();
                holder.txtChairId = (TextView) convertView.findViewById(R.id.txt_chair_id);
                holder.imgSelect = (ImageView) convertView.findViewById(R.id.img_select);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.txtChairId.setText(Variable.chairs.get(position).getEgg_chair_num());
            if (checkInMyList(Variable.chairs.get(position).getId() + "", myEggChair))
                holder.imgSelect.setImageDrawable(getSelectDrawable(1));
            else
                holder.imgSelect.setImageDrawable(getSelectDrawable(0));
            return convertView;
        }

        class ViewHolder {
            private TextView txtChairId;
            private ImageView imgSelect;
        }
    }

    private boolean checkInMyList(String s, String strs) {
        for (String ss :
                strs.split(",")) {
            if (s.equals(ss))
                return true;
        }
        return false;
    }

    public Drawable getSelectDrawable(int status) {
        Drawable d = null;
        switch (status) {
            case 0x00:
                d = ((Activity) context).getResources().getDrawable(R.mipmap.un_choice);
                break;
            case 0x01:
                d = ((Activity) context).getResources().getDrawable(R.mipmap.choice);
                break;
            default:
                break;
        }
        return d;
    }

    private void selectChair(int position) {
        if (checkInMyList(Variable.chairs.get(position).getId() + "", myEggChair))
            removeChair(Variable.chairs.get(position).getId() + "");
        else
            addChair(Variable.chairs.get(position).getId() + "");
    }

    public boolean addChair(String s) {
        for (String chair :
                myEggChair.split(",")) {
            if (chair.equals(s))
                return false;
        }
        if (TextUtils.isEmpty(myEggChair))
            myEggChair = s;
        else
            myEggChair += "," + s;
        mAdapter.notifyDataSetChanged();
        return true;
    }

    public boolean removeChair(String s) {
        String ss = "";
        for (String chair :
                myEggChair.split(",")) {
            if (!chair.equals(s))
                ss += chair + ",";
        }
        if (ss.length() == 0) {
            myEggChair = "";
            mAdapter.notifyDataSetChanged();
            return true;
        }
        ss = ss.substring(0, ss.length() - 1);
        if (ss.equals(myEggChair))
            return false;
        myEggChair = ss;
        mAdapter.notifyDataSetChanged();
        return true;
    }
}
