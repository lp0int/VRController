package com.xiaohong.vrcontroller.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.xiaohong.vrcontroller.Constants;
import com.xiaohong.vrcontroller.Interface.SubscriberOnNextListener;
import com.xiaohong.vrcontroller.R;
import com.xiaohong.vrcontroller.Variable;
import com.xiaohong.vrcontroller.adapter.StaffManagementAdapter;
import com.xiaohong.vrcontroller.base.BaseFragment;
import com.xiaohong.vrcontroller.bean.AddStaffBean;
import com.xiaohong.vrcontroller.bean.DeleteUserBean;
import com.xiaohong.vrcontroller.bean.EditChairBean;
import com.xiaohong.vrcontroller.bean.EditUserBean;
import com.xiaohong.vrcontroller.bean.FindUsersBean;
import com.xiaohong.vrcontroller.utils.DebugTools;
import com.xiaohong.vrcontroller.utils.NetworkRequestMethods;
import com.xiaohong.vrcontroller.utils.ProgressSubscriber;
import com.xiaohong.vrcontroller.utils.Utils;
import com.xiaohong.vrcontroller.utils.widget.PopWindowAddStaff;

import java.util.ArrayList;

/**
 * Created by Lpoint on 2017/5/17.
 * <p>
 * Copyright (c) 2017 Shanghai Xiaohong Technology company. All rights reserved.
 */

public class FragmentStaffManagement extends BaseFragment implements OnClickListener,SwipeRefreshLayout.OnRefreshListener{
    private ArrayList<String> mList, mHeaderList;
    private ArrayList<Boolean> booleans, booleansHeader;
    private StaffManagementAdapter mStaffManagementAdapter, mStaffManagementHeaderAdapter;
    private GridView mGridView, mGridViewHeader;
    private SubscriberOnNextListener findUserListener;
    private SubscriberOnNextListener editChairListener;
    private SubscriberOnNextListener deleteUserListener;
    private SubscriberOnNextListener resetUserPwdListener;
    private SubscriberOnNextListener addStaffListener;
    private SubscriberOnNextListener addDeviceListener;
    private Button btnAddStaff;
    private Button btnAddDevice;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    public static FragmentStaffManagement newInstance() {
        FragmentStaffManagement instance = new FragmentStaffManagement();
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_staff_management, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        initRequestListenter();
        mList = new ArrayList<String>();
        booleans = new ArrayList<Boolean>();
        mHeaderList = new ArrayList<String>();
        booleansHeader = new ArrayList<Boolean>();
        mGridView = (GridView) view.findViewById(R.id.gridview);
        mGridViewHeader = (GridView) view.findViewById(R.id.gridview_header);
        btnAddDevice = (Button) view.findViewById(R.id.btn_add_device);
        btnAddDevice.setOnClickListener(this);
        btnAddStaff = (Button) view.findViewById(R.id.btn_add_staff);
        btnAddStaff.setOnClickListener(this);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_widget);
        mSwipeRefreshLayout.setColorSchemeResources(
                android.R.color.holo_blue_light,
                android.R.color.holo_green_light,
                android.R.color.holo_red_light);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mStaffManagementAdapter = new StaffManagementAdapter(mList, getContext(), booleans, editChairListener, deleteUserListener, resetUserPwdListener);
        mStaffManagementHeaderAdapter = new StaffManagementAdapter(mHeaderList, getContext(), booleansHeader, null, null, null);
        mGridView.setAdapter(mStaffManagementAdapter);
        mGridViewHeader.setAdapter(mStaffManagementHeaderAdapter);
        addHeader();
        if (Variable.mfindUsersBean == null) {
            NetworkRequestMethods.getInstance().findUser(new ProgressSubscriber<FindUsersBean>(findUserListener, getContext(), "获取用户信息中..."));
        } else {
            addData(Variable.mfindUsersBean);
        }
    }

    private void initRequestListenter() {
        findUserListener = new SubscriberOnNextListener<FindUsersBean>() {
            @Override
            public void onNext(FindUsersBean findUsersBean) {
                Variable.mfindUsersBean = findUsersBean;
                mList.clear();
                booleans.clear();
                mSwipeRefreshLayout.setRefreshing(false);
                addData(findUsersBean);
            }

            @Override
            public void onError(Throwable e) {
            }
        };
        editChairListener = new SubscriberOnNextListener<EditChairBean>() {
            @Override
            public void onNext(EditChairBean editChairBean) {
                switch (editChairBean.getStatusCode()) {
                    case 300:
                        NetworkRequestMethods.getInstance().findUser(new ProgressSubscriber<FindUsersBean>(findUserListener, getContext(), "获取用户信息中..."));
                        Utils.showToastStr(getContext(), "修改成功...");
                        break;
                    default:
                        Utils.showToastStr(getContext(), editChairBean.getContent());
                        break;
                }
            }

            @Override
            public void onError(Throwable e) {

            }
        };
        deleteUserListener = new SubscriberOnNextListener<DeleteUserBean>() {
            @Override
            public void onNext(DeleteUserBean deleteUserBean) {
                if (deleteUserBean.getStatusCode() == 300)
                    NetworkRequestMethods.getInstance().findUser(new ProgressSubscriber<FindUsersBean>(findUserListener, getContext(), "获取用户信息中..."));
                Utils.showToastStr(getContext(), deleteUserBean.getContent());
            }

            @Override
            public void onError(Throwable e) {

            }
        };
        resetUserPwdListener = new SubscriberOnNextListener<EditUserBean>() {
            @Override
            public void onNext(EditUserBean editUserBean) {
                Utils.showToastStr(getContext(), editUserBean.getContent());
            }

            @Override
            public void onError(Throwable e) {

            }
        };
        addStaffListener = new SubscriberOnNextListener<AddStaffBean>() {
            @Override
            public void onNext(AddStaffBean addStaffBean) {
                Utils.showToastStr(getContext(),addStaffBean.getContent());
                onRefresh();
            }

            @Override
            public void onError(Throwable e) {

            }
        };
    }

    public void addHeader() {
        String items[] = {"姓名", "账号", "蛋椅", "一体机", "手机", "操作"};
        for (String strText : items) {
            //booleans列表记录状态，改变表格颜色
            booleansHeader.add(true);
            mHeaderList.add(strText);
        }
        mStaffManagementHeaderAdapter.notifyDataSetChanged(); //更新数据
    }

    public void addData(FindUsersBean findUsersBean) {
        for (FindUsersBean.FindUserBean findUserBean :
                findUsersBean.getFindUser()) {
            mList.add(findUserBean.getNickName());
            mList.add(findUserBean.getUserName());
            String eggChairIds = findUserBean.getEgg_chair_id().replace(Constants.nullEggChairId, "");
            if (TextUtils.isEmpty(eggChairIds))
                mList.add("0");
            else
                mList.add(eggChairIds.equals("0") ? findUsersBean.getChair().size() + "" : eggChairIds.split(",").length + "");
            mList.add(findUserBean.getVr_num() + "");
            mList.add(findUserBean.getPhone());
            mList.add("");
            booleans.add(false);
            booleans.add(false);
            booleans.add(false);
            booleans.add(false);
            booleans.add(false);
            booleans.add(false);
        }
        mStaffManagementAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add_staff:
                PopWindowAddStaff popWindowAddStaff = new PopWindowAddStaff((Activity) getContext(),addStaffListener);
                popWindowAddStaff.showPopupWindow(v);
                break;
            case R.id.btn_add_device:
                break;
        }
    }

    @Override
    public void onRefresh() {
        NetworkRequestMethods.getInstance().findUser(new ProgressSubscriber<FindUsersBean>(findUserListener, getContext(), "获取用户信息中..."));
    }
}
