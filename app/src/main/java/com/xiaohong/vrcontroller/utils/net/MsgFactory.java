package com.xiaohong.vrcontroller.utils.net;

import android.app.Activity;
import android.content.Context;
import android.os.Looper;

import com.google.gson.Gson;
import com.xiaohong.vrcontroller.Constants;
import com.xiaohong.vrcontroller.Interface.SubscriberOnNextListener;
import com.xiaohong.vrcontroller.Variable;
import com.xiaohong.vrcontroller.bean.CacheTcpMsgBean;
import com.xiaohong.vrcontroller.bean.DeviceBean;
import com.xiaohong.vrcontroller.bean.RequestObject;
import com.xiaohong.vrcontroller.bean.ResponcePlayCommand;
import com.xiaohong.vrcontroller.bean.UpdateDeviceBean;
import com.xiaohong.vrcontroller.bean.VRDeviceInfo;
import com.xiaohong.vrcontroller.utils.DebugTools;
import com.xiaohong.vrcontroller.utils.NetworkRequestMethods;
import com.xiaohong.vrcontroller.utils.ProgressSubscriber;
import com.xiaohong.vrcontroller.utils.Utils;

import java.util.ArrayList;

/**
 * Created by Lpoint on 2017/5/18.
 * <p>
 * Copyright (c) 2017 Shanghai Xiaohong Technology company. All rights reserved.
 */

public class MsgFactory {
    private Context mContext;
    private static MsgFactory mInstance;
    public static Gson mGson;
    private SubscriberOnNextListener updateDeviceInfoListener;
    private ArrayList<CacheTcpMsgBean> cacheTcpMsgList = new ArrayList<CacheTcpMsgBean>();

    public MsgFactory(Context context) {
        mContext = context;
        mGson = new Gson();
        updateDeviceInfoListener = new SubscriberOnNextListener<UpdateDeviceBean>() {
            @Override
            public void onNext(UpdateDeviceBean updateDeviceBean) {
                DebugTools.showDebugLog("updateDevice", updateDeviceBean.getContent());
            }

            @Override
            public void onError(Throwable e) {

            }
        };
    }

    public static MsgFactory getInstance(Context mContext) {
        if (mInstance == null)
            mInstance = new MsgFactory(mContext);
        return mInstance;
    }


    public void handleMsg(final String ip, String msg) {
        RequestObject mRequestObject;
        String mRequestObjectJson;
        try {
            mRequestObject = mGson.fromJson(msg, RequestObject.class);
            mRequestObjectJson = mRequestObject.getRequestObjectJson();
        } catch (Exception e) {
            String time = msg.split("\\|")[0];
            String power = msg.split("\\|")[1];
            DeviceBean deviceBean = Variable.getDevicesByIp(ip);
            deviceBean.getVRDeviceInfo().setRemainingPower(Integer.parseInt(power));
            Variable.setDeviceInfoByIp(ip, deviceBean.getVRDeviceInfo());
            return;
        }
        switch (mRequestObject.getRequestObjectType()) {
            case 0x00:
                final VRDeviceInfo mVrDeviceInfo = mGson.fromJson(mRequestObjectJson, VRDeviceInfo.class);
                Variable.setDeviceInfoByIp(ip, mVrDeviceInfo);
                ((Activity) mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        NetworkRequestMethods.getInstance().updateDevice(new ProgressSubscriber<UpdateDeviceBean>(updateDeviceInfoListener, mContext, "数据上报中..."),
                                ip,
                                mVrDeviceInfo.getMac(),
                                mVrDeviceInfo.getMac(),
                                mVrDeviceInfo.getEggChairNum());
                    }
                });
                break;
            case 0x01:
                break;
            case 0x02:
                break;
            case 0x03:
                ResponcePlayCommand mResponcePlayCommand = mGson.fromJson(mRequestObjectJson, ResponcePlayCommand.class);
                if (mResponcePlayCommand.getVideoType() == 0x00) {
                    Variable.setVideoCurrentTimeByEggNum(Variable.getDevicesByIp(ip).getVRDeviceInfo().getEggChairNum(), 0);
                    Variable.setDevicePlayVideoStatusByIp(ip, Constants.DEVICES_PLAYVIDEO_STATUS_OK);
                } else {
                    Variable.setVideoCurrentTimeByEggNum(Variable.getDevicesByIp(ip).getVRDeviceInfo().getEggChairNum(), -1);
                    Variable.setDevicePlayVideoStatusByIp(ip, Constants.DEVICES_PLAYVIDEO_STATUS_STANDBY);
                }
                Variable.setVideoTimeByDeviceIp(ip, mResponcePlayCommand.getVideoTime());
                break;
            case 0x04:
                String time = mRequestObjectJson.split("\\|")[0];
                String power = mRequestObjectJson.split("\\|")[1];
                DeviceBean deviceBean = Variable.getDevicesByIp(ip);
                deviceBean.getVRDeviceInfo().setRemainingPower(Integer.parseInt(power));
                Variable.setDeviceInfoByIp(ip, deviceBean.getVRDeviceInfo());
                break;
            default:
                break;
        }
    }

    public void handleTcpMsg(String ip, byte[] msg) {
        if (findTcpMsg(ip) == null) {
            CacheTcpMsgBean cacheTcpMsgBean = new CacheTcpMsgBean();
            cacheTcpMsgBean.setIp(ip);
            cacheTcpMsgBean.setCacheMsg(msg);
            cacheTcpMsgList.add(cacheTcpMsgBean);
        } else {
            byte[] data = new byte[findTcpMsg(ip).getCacheMsg().length + msg.length];
            System.arraycopy(findTcpMsg(ip).getCacheMsg(), 0, data, 0, findTcpMsg(ip).getCacheMsg().length);
            System.arraycopy(msg, 0, data, findTcpMsg(ip).getCacheMsg().length, msg.length);
            findTcpMsg(ip).setCacheMsg(data);
        }
        while (true) {
            if (findTcpMsg(ip).getCacheMsg() == null || findTcpMsg(ip).getCacheMsg().length <= 4) {
                return;
            }
            byte[] data_length = new byte[4];
            DebugTools.showDebugLog("cacheTcpMsg", findTcpMsg(ip).getCacheMsg().length + "|" + new String(findTcpMsg(ip).getCacheMsg(),
                    0,
                    findTcpMsg(ip).getCacheMsg().length));
            System.arraycopy(findTcpMsg(ip).getCacheMsg(), 0, data_length, 0, 4);
            int dataLength = Utils.byte2int(data_length);
            if (findTcpMsg(ip).getCacheMsg().length < dataLength + 4)
                return;
            String data_content = new String(findTcpMsg(ip).getCacheMsg(), 4, Utils.byte2int(data_length));
            handleMsg(ip, data_content);
            if (data_content.length() + 4 == findTcpMsg(ip).getCacheMsg().length) {
                findTcpMsg(ip).setCacheMsg(new byte[0]);
                return;
            }
            byte[] remainingData = new byte[findTcpMsg(ip).getCacheMsg().length - data_content.length() - 4];
            System.arraycopy(findTcpMsg(ip).getCacheMsg(), data_content.length() + 4, remainingData, 0, remainingData.length);
            findTcpMsg(ip).setCacheMsg(remainingData);
        }
    }

    private CacheTcpMsgBean findTcpMsg(String ip) {
        for (CacheTcpMsgBean cacheTcpMsgBean :
                cacheTcpMsgList) {
            if (cacheTcpMsgBean.getIp().equals(ip))
                return cacheTcpMsgBean;
        }
        return null;
    }

    private boolean addTcpMsg(CacheTcpMsgBean mCacheTcpMsgBean) {
        for (CacheTcpMsgBean cacheTcpMsgBean :
                cacheTcpMsgList) {
            if (cacheTcpMsgBean.getIp().equals(mCacheTcpMsgBean.getIp()))
                return false;
        }
        cacheTcpMsgList.add(mCacheTcpMsgBean);
        return true;
    }

    private boolean setCacheBean(CacheTcpMsgBean mCacheTcpMsgBean) {
        int index = 0;
        for (CacheTcpMsgBean cacheTcpMsgBean :
                cacheTcpMsgList) {
            if (cacheTcpMsgBean.getIp().equals(mCacheTcpMsgBean.getIp())) {
                cacheTcpMsgList.set(index, mCacheTcpMsgBean);
                return false;
            }
            index++;
        }
        return false;
    }
}
