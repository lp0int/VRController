package com.xiaohong.vrcontroller.utils.net;

import android.content.Context;

import com.google.gson.Gson;
import com.xiaohong.vrcontroller.Constants;
import com.xiaohong.vrcontroller.Interface.DevicesNotifyRefresh;
import com.xiaohong.vrcontroller.Variable;
import com.xiaohong.vrcontroller.bean.DeviceBean;
import com.xiaohong.vrcontroller.bean.PadDeviceInfo;
import com.xiaohong.vrcontroller.bean.RequestObject;
import com.xiaohong.vrcontroller.bean.ResponcePlayCommand;
import com.xiaohong.vrcontroller.bean.VRDeviceInfo;
import com.xiaohong.vrcontroller.ui.FragmentDeviceManagement;
import com.xiaohong.vrcontroller.utils.DebugTools;
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
    boolean inSendingUdp = false;
    private byte[] cacheMsgByte = null;
    private ArrayList<String> ipList = new ArrayList<String>();
    private Thread videoPlayThread;

    public MsgFactory(Context context) {
        mContext = context;
        mGson = new Gson();
    }

    public static MsgFactory getInstance(Context mContext) {
        if (mInstance == null)
            mInstance = new MsgFactory(mContext);
        return mInstance;
    }


    public void handleMsg(String ip, String msg) {
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
            Variable.setDeviceInfoByIp(ip,deviceBean.getVRDeviceInfo());
            return;
        }
        switch (mRequestObject.getRequestObjectType()) {
            case 0x00:
                final VRDeviceInfo mVrDeviceInfo = mGson.fromJson(mRequestObjectJson, VRDeviceInfo.class);
                Variable.setDeviceInfoByIp(ip, mVrDeviceInfo);
                break;
            case 0x01:
                break;
            case 0x02:
                break;
            case 0x03:
                ResponcePlayCommand mResponcePlayCommand = mGson.fromJson(mRequestObjectJson, ResponcePlayCommand.class);
                if (mResponcePlayCommand.getVideoType() == 0x00) {
                    Variable.setVideoCurrentTimeByEggNum(Variable.getDevicesByIp(ip).getVRDeviceInfo().getEggChairNum(),0);
                    Variable.setDevicePlayVideoStatusByIp(ip, Constants.DEVICES_PLAYVIDEO_STATUS_OK);
                } else {
                    Variable.setVideoCurrentTimeByEggNum(Variable.getDevicesByIp(ip).getVRDeviceInfo().getEggChairNum(),-1);
                    Variable.setDevicePlayVideoStatusByIp(ip, Constants.DEVICES_PLAYVIDEO_STATUS_STANDBY);
                }
                Variable.setVideoTimeByDeviceIp(ip,mResponcePlayCommand.getVideoTime());
                break;
            case 0x04:
                String time = mRequestObjectJson.split("\\|")[0];
                String power = mRequestObjectJson.split("\\|")[1];
                DeviceBean deviceBean = Variable.getDevicesByIp(ip);
                deviceBean.getVRDeviceInfo().setRemainingPower(Integer.parseInt(power));
                Variable.setDeviceInfoByIp(ip,deviceBean.getVRDeviceInfo());
                break;
            default:
                break;
        }
    }

    public void handleTcpMsg(String ip, byte[] msg) {
        ipList.add(ip);
        if (cacheMsgByte == null || cacheMsgByte.length == 0) {
            cacheMsgByte = msg;
        } else {
            byte[] data = new byte[cacheMsgByte.length + msg.length];
            System.arraycopy(cacheMsgByte, 0, data, 0, cacheMsgByte.length);
            System.arraycopy(msg, 0, data, cacheMsgByte.length, msg.length);
            cacheMsgByte = data;
        }
        while (true) {
            if (cacheMsgByte == null || cacheMsgByte.length <= 4) {
                return;
            }
            byte[] data_length = new byte[4];
            DebugTools.showDebugLog("cacheTcpMsg", cacheMsgByte.length + "|" + new String(cacheMsgByte, 0, cacheMsgByte.length));
            System.arraycopy(cacheMsgByte, 0, data_length, 0, 4);
            int dataLength = Utils.byte2int(data_length);
            if (cacheMsgByte.length < dataLength + 4)
                return;
            String data_content = new String(cacheMsgByte, 4, Utils.byte2int(data_length));
            handleMsg(ip, data_content);
            ipList.remove(0);
            if (data_content.length() + 4 == cacheMsgByte.length) {
                cacheMsgByte = new byte[0];
                return;
            }
            byte[] remainingData = new byte[cacheMsgByte.length - data_content.length() - 4];
            System.arraycopy(cacheMsgByte, data_content.length() + 4, remainingData, 0, remainingData.length);
            cacheMsgByte = remainingData;
        }
    }

}
