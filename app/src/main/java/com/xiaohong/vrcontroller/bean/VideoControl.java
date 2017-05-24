package com.xiaohong.vrcontroller.bean;

/**
 * Created by Lpoint on 2017/5/18.
 * <p>
 * Copyright (c) 2017 Shanghai Xiaohong Technology company. All rights reserved.
 */

/**
 * 视频播放控制
 */
public class VideoControl {
    private Integer action;          //0x00:play   0x01:stop
    private String videoId;            //ignore
    private boolean isControlDevice;

    public Integer getAction() {
        return action;
    }

    public void setAction(Integer action) {
        this.action = action;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public boolean isControlDevice() {
        return isControlDevice;
    }

    public void setControlDevice(boolean controlDevice) {
        isControlDevice = controlDevice;
    }
}
