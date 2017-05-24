package com.xiaohong.vrcontroller.bean;

/**
 * Created by Lpoint on 2017/5/21.
 * <p>
 * Copyright (c) 2017 Shanghai Xiaohong Technology Company. All rights reserved.
 */

public class ChairStatusBean {
    private FindUsersBean.ChairBean chair;
    private long playVideoTime;
    private int playVideoType;
    private long currentTime;

    public FindUsersBean.ChairBean getChair() {
        return chair;
    }

    public void setChair(FindUsersBean.ChairBean chair) {
        this.chair = chair;
    }

    public long getPlayVideoTime() {
        return playVideoTime;
    }

    public void setPlayVideoTime(long playVideoTime) {
        this.playVideoTime = playVideoTime;
    }

    public int getPlayVideoType() {
        return playVideoType;
    }

    public void setPlayVideoType(int playVideoType) {
        this.playVideoType = playVideoType;
    }

    public long getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(long currentTime) {
        this.currentTime = currentTime;
    }
}
