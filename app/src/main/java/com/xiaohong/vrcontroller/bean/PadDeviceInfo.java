package com.xiaohong.vrcontroller.bean;

/**
 * Created by Lpoint on 2017/5/18.
 * <p>
 * Copyright (c) 2017 Shanghai Xiaohong Technology company. All rights reserved.
 */

import java.util.ArrayList;

/**
 * Pad UDP广播
 */
public class PadDeviceInfo {
    private String ip;
    private Integer tcpPort;
    private ArrayList<String> eggChairList;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getTcpPort() {
        return tcpPort;
    }

    public void setTcpPort(Integer tcpPort) {
        this.tcpPort = tcpPort;
    }

    public ArrayList<String> getEggChairList() {
        return eggChairList;
    }

    public void setEggChairList(ArrayList<String> eggChairList) {
        this.eggChairList = eggChairList;
    }
}
