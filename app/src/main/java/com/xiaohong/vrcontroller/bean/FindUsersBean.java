package com.xiaohong.vrcontroller.bean;

import java.util.List;

/**
 * Created by Lpoint on 2017/5/17.
 * <p>
 * Copyright (c) 2017 Shanghai Xiaohong Technology company. All rights reserved.
 */

public class FindUsersBean {

    /**
     * findUser : [{"nickName":"金客辰","egg_chair_id":"0","phone":"13585768869","userName":"jinkechen","vr_num":"16"},{"nickName":"陈朋","egg_chair_id":"1,6","phone":"13585768869","userName":"chenpeng","vr_num":"8"},{"nickName":"aaaa","egg_chair_id":"3","phone":"aa","userName":"aaaa","vr_num":"4"}]
     * chair : [{"id":3,"egg_chair_num":"1"},{"id":1,"egg_chair_num":"EggChair1"},{"id":4,"egg_chair_num":"EggChair2"},{"id":5,"egg_chair_num":"(null)"},{"id":6,"egg_chair_num":"100"}]
     * State : success
     * Content : 成功
     * StatusCode : 300
     */

    private String State;
    private String Content;
    private int StatusCode;
    private List<FindUserBean> findUser;
    private List<ChairBean> chair;

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

    public List<FindUserBean> getFindUser() {
        return findUser;
    }

    public void setFindUser(List<FindUserBean> findUser) {
        this.findUser = findUser;
    }

    public List<ChairBean> getChair() {
        return chair;
    }

    public void setChair(List<ChairBean> chair) {
        this.chair = chair;
    }

    public static class FindUserBean {
        /**
         * nickName : 金客辰
         * egg_chair_id : 0
         * phone : 13585768869
         * userName : jinkechen
         * vr_num : 16
         */

        private String nickName;
        private String egg_chair_id;
        private String phone;
        private String userName;
        private String vr_num;

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

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getVr_num() {
            return vr_num;
        }

        public void setVr_num(String vr_num) {
            this.vr_num = vr_num;
        }
    }

    public static class ChairBean {
        /**
         * id : 3
         * egg_chair_num : 1
         */

        private int id;
        private String egg_chair_num;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getEgg_chair_num() {
            return egg_chair_num;
        }

        public void setEgg_chair_num(String egg_chair_num) {
            this.egg_chair_num = egg_chair_num;
        }
    }
}
