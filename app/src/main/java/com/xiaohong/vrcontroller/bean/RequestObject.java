package com.xiaohong.vrcontroller.bean;

/**
 * Created by Lpoint on 2017/5/18.
 * <p>
 * Copyright (c) 2017 Shanghai Xiaohong Technology company. All rights reserved.
 */

public class RequestObject {
    private Integer requestObjectType;      //0x00:VRDeviceInfo 0x01:PadDeviceInfo  0x02:VideoControl  0x03:ResponcePlayCommand
    private String requestObjectJson;

    public Integer getRequestObjectType() {
        return requestObjectType;
    }

    public void setRequestObjectType(Integer requestObjectType) {
        this.requestObjectType = requestObjectType;
    }

    public String getRequestObjectJson() {
        return requestObjectJson;
    }

    public void setRequestObjectJson(String requestObjectJson) {
        this.requestObjectJson = requestObjectJson;
    }
}
