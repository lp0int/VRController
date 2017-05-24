package com.xiaohong.vrcontroller.utils;

import com.xiaohong.vrcontroller.Constants;
import com.xiaohong.vrcontroller.Interface.RequestServiceInterface;
import com.xiaohong.vrcontroller.bean.DeleteUserBean;
import com.xiaohong.vrcontroller.bean.EditChairBean;
import com.xiaohong.vrcontroller.bean.EditUserBean;
import com.xiaohong.vrcontroller.bean.FindUsersBean;
import com.xiaohong.vrcontroller.bean.LoginBean;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Lpoint on 2017/5/16.
 */

public class NetworkRequestMethods {
    private Retrofit mRetrofit;
    private RequestServiceInterface mRequestServiceInterface;

    private NetworkRequestMethods() {
        OkHttpClient.Builder httpClientBuild = new OkHttpClient.Builder();
        httpClientBuild.connectTimeout(Constants.DEFAULT_TIMEOUT, TimeUnit.SECONDS).addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                Request.Builder builder = request.newBuilder();
                Request build = builder.addHeader("Connection", "close").build();
                return chain.proceed(build);
            }
        });
        mRetrofit = new Retrofit.Builder()
                .client(httpClientBuild.build())
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        mRequestServiceInterface = mRetrofit.create(RequestServiceInterface.class);
    }

    private static class SingletonHolder {
        private static final NetworkRequestMethods INSTANCE = new NetworkRequestMethods();
    }

    public static NetworkRequestMethods getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void login(Subscriber<LoginBean> subscriber, String userName, String passwd) {
        mRequestServiceInterface.login(userName, passwd)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void findUser(Subscriber<FindUsersBean> subscriber) {
        mRequestServiceInterface.findUser()
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void editEggChair(Subscriber<EditChairBean> subscriber, String userName, String chair) {
        mRequestServiceInterface.editChairBean(userName, chair)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void deleteUser(Subscriber<DeleteUserBean> subscriber, String userName) {
        mRequestServiceInterface.deleteUser(userName)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void editUserPassword(Subscriber<EditUserBean> subscriber, String userName, String passwd) {
        mRequestServiceInterface.editUser(userName, passwd)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

}
