package com.xiaohong.vrcontroller.bean;

import java.util.List;

/**
 * Created by Lpoint on 2017/5/19.
 * <p>
 * Copyright (c) 2017 Shanghai Xiaohong Technology Company. All rights reserved.
 */

public class DeleteUserBean
{
    /**
     * Register : []
     * State : success
     * Content : 成功
     * StatusCode : 300
     */

    private String State;
    private String Content;
    private int StatusCode;
    private List<?> Register;

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

    public List<?> getRegister() {
        return Register;
    }

    public void setRegister(List<?> Register) {
        this.Register = Register;
    }
}
