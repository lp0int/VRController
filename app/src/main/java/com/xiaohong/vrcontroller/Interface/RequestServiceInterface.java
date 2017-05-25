package com.xiaohong.vrcontroller.Interface;

import com.xiaohong.vrcontroller.bean.AddStaffBean;
import com.xiaohong.vrcontroller.bean.DeleteUserBean;
import com.xiaohong.vrcontroller.bean.EditChairBean;
import com.xiaohong.vrcontroller.bean.EditUserBean;
import com.xiaohong.vrcontroller.bean.FindUsersBean;
import com.xiaohong.vrcontroller.bean.LoginBean;


import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Lpoint on 2017/5/16.
 * <p>
 * Copyright (c) 2017 Shanghai Xiaohong Technology company. All rights reserved.
 */

public interface RequestServiceInterface {
    @GET("index.php/index/login/login")
    Observable<LoginBean> login(@Query("user") String user, @Query("password") String passwd);

    @GET("index.php/index/login/findUser")
    Observable<FindUsersBean> findUser();

    @GET("index.php/index/login/chair")
    Observable<EditChairBean> editChairBean(@Query("user") String user, @Query("chair") String chair);

    @GET("index.php/index/login/deluser")
    Observable<DeleteUserBean> deleteUser(@Query("user") String user);

    @GET("index.php/index/login/modifyPass")
    Observable<EditUserBean> editUser(@Query("user") String user, @Query("password") String password);

    @GET("index.php/index/login/register")
    Observable<AddStaffBean> addStaff(@Query("user") String user, @Query("password") String password,
                                      @Query("nick_name") String nickName, @Query("egg_chair_id") String eggChairId,
                                      @Query("phone") String phone);
}