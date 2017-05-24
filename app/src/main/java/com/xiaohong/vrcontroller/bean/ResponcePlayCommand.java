package com.xiaohong.vrcontroller.bean;

/**
 * Created by Lpoint on 2017/5/18.
 * <p>
 * Copyright (c) 2017 Shanghai Xiaohong Technology company. All rights reserved.
 */

/**
 * 一体机回馈播放指令
 */
public class ResponcePlayCommand {
    private Integer videoType;      //0x00:panoramaVideo   0x01:ordinaryVideo
    private long videoTime;         //seconds

    public Integer getVideoType() {
        return videoType;
    }

    public void setVideoType(Integer videoType) {
        this.videoType = videoType;
    }

    public long getVideoTime() {
        return videoTime;
    }

    public void setVideoTime(long videoTime) {
        this.videoTime = videoTime;
    }
}
