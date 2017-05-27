package com.xiaohong.vrcontroller.bean;

/**
 * Created by Lpoint on 2017/5/27.
 * <p>
 * Copyright (c) 2017 Shanghai Xiaohong Technology Company. All rights reserved.
 */

public class CacheTcpMsgBean {
    private String ip;
    private byte[] cacheMsg;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public byte[] getCacheMsg() {
        return cacheMsg;
    }

    public void setCacheMsg(byte[] cacheMsg) {
        this.cacheMsg = cacheMsg;
    }
}
