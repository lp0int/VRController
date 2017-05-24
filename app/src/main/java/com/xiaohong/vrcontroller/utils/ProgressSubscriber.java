package com.xiaohong.vrcontroller.utils;


import android.content.Context;

import com.xiaohong.vrcontroller.Constants;
import com.xiaohong.vrcontroller.Interface.ProgressCancelListener;
import com.xiaohong.vrcontroller.Interface.SubscriberOnNextListener;
import com.xiaohong.vrcontroller.handler.ProgressDialogHandler;

import rx.Subscriber;

/**
 * Created by Lpoint on 2017/5/16.
 * <p>
 * Copyright (c) 2017 Shanghai Xiaohong Technology company. All rights reserved.
 */

public class ProgressSubscriber<T> extends Subscriber<T> implements ProgressCancelListener {
    private SubscriberOnNextListener mSubscriberOnNextListener;
    private ProgressDialogHandler mProgressDialogHandler;
    private Context mContext;
    private String mProgressMsg;

    public ProgressSubscriber(SubscriberOnNextListener mSubscriberOnNextListener, Context mContext, String... progressMsg) {
        this.mSubscriberOnNextListener = mSubscriberOnNextListener;
        this.mContext = mContext;
        mProgressMsg = progressMsg.length > 0 ? progressMsg[0] : Constants.DEFAULT_PROGRESS_MESSAGE;
        mProgressDialogHandler = new ProgressDialogHandler(mContext, this, true, mProgressMsg);
    }

    private void showProgressDialog() {
        if (mProgressDialogHandler != null)
            mProgressDialogHandler.obtainMessage(ProgressDialogHandler.SHOW_PROGRESS_DIALOG).sendToTarget();
    }

    private void dismissProgressDialog() {
        if (mProgressDialogHandler != null) {
            mProgressDialogHandler.obtainMessage(ProgressDialogHandler.DISMISS_PROGRESS_DIALOG).sendToTarget();
            mProgressDialogHandler = null;
        }
    }

    @Override
    public void onStart() {
        showProgressDialog();
    }

    @Override
    public void onCompleted() {
        dismissProgressDialog();
    }

    @Override
    public void onError(Throwable throwable) {
        dismissProgressDialog();
        DebugTools.showDebugToast(mContext, "error:" + throwable.getMessage());
        mSubscriberOnNextListener.onError(throwable);
    }

    @Override
    public void onNext(T t) {
        mSubscriberOnNextListener.onNext(t);
    }

    @Override
    public void onCancelProgress() {
        if (!this.isUnsubscribed())
            this.unsubscribe();
    }
}
