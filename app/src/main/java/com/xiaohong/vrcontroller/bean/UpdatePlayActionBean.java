package com.xiaohong.vrcontroller.bean;

import java.util.List;

/**
 * Created by Lpoint on 2017/5/25.
 * <p>
 * Copyright (c) 2017 Shanghai Xiaohong Technology Company. All rights reserved.
 */

public class UpdatePlayActionBean {

    /**
     * playAction : []
     * State : success
     * Content : 状态更新成功
     * StatusCode : 300
     */

    private String State;
    private String Content;
    private int StatusCode;
    private List<?> playAction;

    public String getState() {
        return State;
    }

    public void setState(String State) {
        this.State = State;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String Content) {
        this.Content = Content;
    }

    public int getStatusCode() {
        return StatusCode;
    }

    public void setStatusCode(int StatusCode) {
        this.StatusCode = StatusCode;
    }

    public List<?> getPlayAction() {
        return playAction;
    }

    public void setPlayAction(List<?> playAction) {
        this.playAction = playAction;
    }
}
