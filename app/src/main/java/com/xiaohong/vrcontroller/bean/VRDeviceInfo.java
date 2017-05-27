package com.xiaohong.vrcontroller.bean;

/**
 * Created by Lpoint on 2017/5/18.
 * <p>
 * Copyright (c) 2017 Shanghai Xiaohong Technology company. All rights reserved.
 */

/**
 * 一体机TCP广播
 */
public class VRDeviceInfo {
    private Integer remainingPower;
    private String eggChairNum;
    private String mac;
    private Integer deviceStatus;       //0x00:playing   0x01:stand by
    private Integer usbStatus;          //0x00:connection       0x01:not connected

    public VRDeviceInfo() {
        remainingPower = -1;
        eggChairNum = "-1";
        mac = "-1";
        deviceStatus = 0x01;
        usbStatus = 0x01;
    }

    public Integer getRemainingPower() {
        return remainingPower;
    }

    public void setRemainingPower(Integer remainingPower) {
        this.remainingPower = remainingPower;
    }

    public String getEggChairNum() {
        return eggChairNum;
    }

    public void setEggChairNum(String eggChairNum) {
        this.eggChairNum = eggChairNum;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public Integer getDeviceStatus() {
        return deviceStatus;
    }

    public void setDeviceStatus(Integer deviceStatus) {
        this.deviceStatus = deviceStatus;
    }

    public Integer getUsbStatus() {
        return usbStatus;
    }

    public void setUsbStatus(Integer usbStatus) {
        this.usbStatus = usbStatus;
    }
}
