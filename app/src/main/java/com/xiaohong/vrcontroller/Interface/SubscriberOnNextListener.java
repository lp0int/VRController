package com.xiaohong.vrcontroller.Interface;

/**
 * Created by Lpoint on 2017/5/16.
 * <p>
 * Copyright (c) 2017 Shanghai Xiaohong Technology company. All rights reserved.
 */

public interface SubscriberOnNextListener<T> {
    void onNext(T t);

    void onError(Throwable e);
}
