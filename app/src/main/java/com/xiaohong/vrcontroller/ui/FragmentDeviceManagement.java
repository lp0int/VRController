package com.xiaohong.vrcontroller.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xiaohong.vrcontroller.Interface.DevicesNotifyRefresh;
import com.xiaohong.vrcontroller.Interface.SubscriberOnNextListener;
import com.xiaohong.vrcontroller.R;
import com.xiaohong.vrcontroller.adapter.DeviceManagementAdapter;
import com.xiaohong.vrcontroller.base.BaseFragment;
import com.xiaohong.vrcontroller.bean.UpdatePlayActionBean;
import com.xiaohong.vrcontroller.utils.DebugTools;
import com.xiaohong.vrcontroller.utils.Utils;

/**
 * Created by Lpoint on 2017/5/17.
 * <p>
 * Copyright (c) 2017 Shanghai Xiaohong Technology company. All rights reserved.
 */

public class FragmentDeviceManagement extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
    public static DevicesNotifyRefresh mDevicesNotifyRefresh;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private DeviceManagementAdapter mDeviceManagementAdapter;
    private SubscriberOnNextListener updatePlayActionListener;

    public static FragmentDeviceManagement newInstance() {
        FragmentDeviceManagement instance = new FragmentDeviceManagement();
        return instance;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_device_management, null);
        initView(view);
        return view;
    }

    @Override
    public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
        mDevicesNotifyRefresh = new DevicesNotifyRefresh() {
            @Override
            public void notifyDeviceChange() {
                ((Activity) getContext()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mDeviceManagementAdapter.notifyDataSetChanged();
                    }
                });
            }
        };
    }

    private void initView(View view) {
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_widget);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.list_devices);
        mSwipeRefreshLayout.setColorSchemeResources(
                android.R.color.holo_blue_light,
                android.R.color.holo_green_light,
                android.R.color.holo_red_light);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    private void initData() {
        initRequestListener();
        mDeviceManagementAdapter = new DeviceManagementAdapter(getActivity(),updatePlayActionListener);
        mRecyclerView.setAdapter(mDeviceManagementAdapter);
        onRefresh();
    }

    @Override
    public void onRefresh() {
        mDeviceManagementAdapter.notifyDataSetChanged();
        mSwipeRefreshLayout.setRefreshing(false);
    }

    private void initRequestListener() {
        updatePlayActionListener = new SubscriberOnNextListener<UpdatePlayActionBean>() {
            @Override
            public void onNext(UpdatePlayActionBean o) {
                Utils.showToastStr(getContext(),o.getContent());
            }

            @Override
            public void onError(Throwable e) {

            }
        };
    }
}
