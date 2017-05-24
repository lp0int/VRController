package com.xiaohong.vrcontroller.bean;

import java.net.Socket;

/**
 * Created by Lpoint on 2017/5/18.
 * <p>
 * Copyright (c) 2017 Shanghai Xiaohong Technology company. All rights reserved.
 */

public class DeviceBean {
    private Socket deviceSocket;
    private VRDeviceInfo mVRDeviceInfo;
    private Integer devicePlayVideoStatus;          //0x00:panoramaVideo       0x01:ordinaryVideo

    public DeviceBean() {
        mVRDeviceInfo = new VRDeviceInfo();
        devicePlayVideoStatus = 0x01;
    }

    public Socket getDeviceSocket() {
        return deviceSocket;
    }

    public void setDeviceSocket(Socket deviceSocket) {
        this.deviceSocket = deviceSocket;
    }

    public VRDeviceInfo getVRDeviceInfo() {
        return mVRDeviceInfo;
    }

    public void setVRDeviceInfo(VRDeviceInfo VRDeviceInfo) {
        mVRDeviceInfo = VRDeviceInfo;
    }

    public Integer getDevicePlayVideoStatus() {
        return devicePlayVideoStatus;
    }

    public void setDevicePlayVideoStatus(Integer devicePlayVideoStatus) {
        this.devicePlayVideoStatus = devicePlayVideoStatus;
    }
}
