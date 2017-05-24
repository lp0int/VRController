package com.xiaohong.vrcontroller.bean;

import java.util.List;

/**
 * Created by Lpoint on 2017/5/16.
 * <p>
 * Copyright (c) 2017 Shanghai Xiaohong Technology company. All rights reserved.
 */

public class LoginBean {

    /**
     * user_info : [{"nickName":"金客辰","egg_chair_id":"0","phone":"13585768869"}]
     * State : success
     * Content : 登陆成功
     * StatusCode : 300
     */

    private String State;
    private String Content;
    private int StatusCode;
    private List<UserInfoBean> user_info;

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

    public List<UserInfoBean> getUser_info() {
        return user_info;
    }

    public void setUser_info(List<UserInfoBean> user_info) {
        this.user_info = user_info;
    }

    public static class UserInfoBean {
        /**
         * nickName : 金客辰
         * egg_chair_id : 0
         * phone : 13585768869
         */

        private String nickName;
        private String egg_chair_id;
        private String phone;

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getEgg_chair_id() {
            return egg_chair_id;
        }

        public void setEgg_chair_id(String egg_chair_id) {
            this.egg_chair_id = egg_chair_id;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }
    }
}
