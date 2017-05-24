package com.xiaohong.vrcontroller.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xiaohong.vrcontroller.R;
import com.xiaohong.vrcontroller.base.BaseFragment;

/**
 * Created by Lpoint on 2017/5/16.
 * <p>
 * Copyright (c) 2017 Shanghai Xiaohong Technology company. All rights reserved.
 */

public class FragmentTest extends BaseFragment {
    public static final String ARGS_PAGE = "args_page";
    private int mPage;

    public static FragmentTest newInstance(int page) {
        Bundle args = new Bundle();

        args.putInt(ARGS_PAGE, page);
        FragmentTest fragment = new FragmentTest();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARGS_PAGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_test, container, false);
        ((TextView) view.findViewById(R.id.txt_test)).setText("第" + mPage + "页");
        return view;
    }
}
