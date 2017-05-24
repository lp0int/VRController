package com.xiaohong.vrcontroller;

import android.content.Context;
import android.widget.Toast;

import com.xiaohong.vrcontroller.bean.ChairStatusBean;
import com.xiaohong.vrcontroller.bean.DeviceBean;
import com.xiaohong.vrcontroller.bean.FindUsersBean;
import com.xiaohong.vrcontroller.bean.LoginBean;
import com.xiaohong.vrcontroller.bean.VRDeviceInfo;
import com.xiaohong.vrcontroller.ui.FragmentDeviceManagement;
import com.xiaohong.vrcontroller.utils.Utils;
import com.xiaohong.vrcontroller.utils.net.ServerSocketThread;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lpoint on 2017/5/16.
 * <p>
 * Copyright (c) 2017 Shanghai Xiaohong Technology company. All rights reserved.
 */

public class Variable {
    public static Context BASECONTEXT;
    public static LoginBean loginBean;
    public static FindUsersBean mfindUsersBean;
    public static String userName;
    public static List<FindUsersBean.ChairBean> chairs;
    public static List<ChairStatusBean> chairsStatusBeen;
    public static ArrayList<DeviceBean> devices = new ArrayList<DeviceBean>();
    public static ServerSocketThread serverSocketThread;
    /**
     * 防止Toast重复提示的相关变量
     */
    public static Toast toast = null;
    public static long oneTime = 0;
    public static long twoTime = 0;
    public static String oldMsg = null;

    public static String getChairList() {
        return loginBean.getUser_info().get(0).getEgg_chair_id();
    }

    /**
     * @return 返回蛋椅ID列表
     */
    public static String getChairListByUsername(String UserName) {
        for (FindUsersBean.FindUserBean mFindUserBean :
                mfindUsersBean.getFindUser()) {
            if (UserName.equals(mFindUserBean.getUserName()))
                return mFindUserBean.getEgg_chair_id();

        }
        return "";
    }

    public static void setFindUserBean(FindUsersBean findUsersBean) {
        mfindUsersBean = findUsersBean;
        chairs = findUsersBean.getChair();
        ArrayList<ChairStatusBean> mChairsStatusBeen = new ArrayList<ChairStatusBean>();
        for (FindUsersBean.ChairBean chairBean :
                chairs) {
            ChairStatusBean chairStatusBean = new ChairStatusBean();
            chairStatusBean.setChair(chairBean);
            chairStatusBean.setPlayVideoTime(-1);
            chairStatusBean.setPlayVideoType(Constants.VIDEO_TYPE_ORDINARYVIDEO);
            mChairsStatusBeen.add(chairStatusBean);
        }
        chairsStatusBeen = mChairsStatusBeen;
    }

    public static boolean setChairVideoTypeByChairChairNum(String ChairNum, int videoType) {
        int index = 0;
        for (ChairStatusBean chairStatusBean :
                chairsStatusBeen) {
            if (chairStatusBean.getChair().getEgg_chair_num().equals(ChairNum)) {
                chairStatusBean.setPlayVideoType(videoType);
                chairsStatusBeen.set(index, chairStatusBean);
                return true;
            }
            index++;
        }
        return false;
    }

    public static boolean setChairVideoTimeByChairChairNum(String ChairNum, long videoTime) {
        int index = 0;
        for (ChairStatusBean chairStatusBean :
                chairsStatusBeen) {
            if (chairStatusBean.getChair().getEgg_chair_num().equals(ChairNum)) {
                chairStatusBean.setPlayVideoTime(videoTime);
                chairsStatusBeen.set(index, chairStatusBean);
                return true;
            }
            index++;
        }
        return false;
    }

    /**
     * 通过蛋椅ID查找到蛋椅编号
     */
    public static String getChairNumById(int id) {
        for (FindUsersBean.ChairBean chair :
                chairs) {
            if (chair.getId() == id)
                return chair.getEgg_chair_num();
        }
        return "";
    }

    /**
     * 添加链接的设备
     */
    public static boolean addDevices(DeviceBean mDevice) {
        if (devices == null)
            return false;
        for (DeviceBean device :
                devices) {
            if (device.getDeviceSocket().getInetAddress().getHostAddress().equals(mDevice.getDeviceSocket().getInetAddress().getHostAddress()))
                return false;
        }
        devices.add(mDevice);
        if (FragmentDeviceManagement.mDevicesNotifyRefresh != null)
            FragmentDeviceManagement.mDevicesNotifyRefresh.notifyDeviceChange();
        return true;
    }

    /**
     * 通过IP移除已链接的设备
     */
    public static boolean removeDevicesByIp(String ip) {
        if (devices == null)
            return false;
        int index = 0;
        for (DeviceBean device :
                devices) {
            if (device.getDeviceSocket().getInetAddress().getHostAddress().equals(ip)) {
                devices.remove(index);
                FragmentDeviceManagement.mDevicesNotifyRefresh.notifyDeviceChange();
                return true;
            }
            index++;
        }
        return false;
    }

    /**
     * 通过IP获取设备信息
     */
    public static DeviceBean getDevicesByIp(String ip) {
        if (devices == null)
            return null;
        for (DeviceBean device :
                devices) {
            if (device.getDeviceSocket().getInetAddress().getHostAddress().equals(ip)) {
                return device;
            }
        }
        return null;
    }

    /**
     * 通过蛋椅ID获取到对应链接的一体机列表
     */
    public static ArrayList<DeviceBean> getDevicesByEggNum(String eggNum) {
        if (devices == null)
            return null;
        ArrayList<DeviceBean> mDevices = new ArrayList<DeviceBean>();
        for (DeviceBean device :
                devices) {
            if (device.getVRDeviceInfo().getEggChairNum().equals(eggNum)) {
                mDevices.add(device);
            }
        }
        return mDevices;
    }

    public static void resetDevicesPlayVideStatus() {
        if (devices == null)
            return;
        int index = 0;
        for (DeviceBean device :
                devices) {
            FragmentDeviceManagement.mDevicesNotifyRefresh.notifyDeviceChange();
            device.setDevicePlayVideoStatus(Constants.DEVICES_PLAYVIDEO_STATUS_STANDBY);
            devices.set(index, device);
            checkAllDeviceStatus(device.getVRDeviceInfo().getEggChairNum());
            index++;
        }
    }

    public static boolean setDevicePlayVideoStatusByIp(String ip, Integer status) {
        if (devices == null)
            return false;
        int index = 0;
        for (DeviceBean device :
                devices) {
            if (device.getDeviceSocket().getInetAddress().getHostAddress().equals(ip)) {
                device.setDevicePlayVideoStatus(status);
                FragmentDeviceManagement.mDevicesNotifyRefresh.notifyDeviceChange();
                devices.set(index, device);
                checkAllDeviceStatus(device.getVRDeviceInfo().getEggChairNum());
                return true;
            }
            index++;
        }
        return false;
    }

    /**
     * 通过设备ID找到设备后设置设备信息
     */
    public static boolean setDeviceInfoByIp(String ip, VRDeviceInfo mVrDeviceInfo) {
        if (devices == null)
            return false;
        for (DeviceBean device :
                devices) {
            if (device.getDeviceSocket().getInetAddress().getHostAddress().equals(ip)) {
                device.setVRDeviceInfo(mVrDeviceInfo);
                FragmentDeviceManagement.mDevicesNotifyRefresh.notifyDeviceChange();
                return true;
            }
        }
        return false;
    }

    /**
     * 通过设备ID设置对应蛋椅播放时长
     */
    public static boolean setVideoTimeByDeviceIp(String ip, long videoTime) {
        int index = 0;
        for (ChairStatusBean chairStatusBean :
                chairsStatusBeen) {
            String chairNum = getDevicesByIp(ip).getVRDeviceInfo().getEggChairNum();
            if (chairNum.equals(chairStatusBean.getChair().getEgg_chair_num())) {
                chairStatusBean.setPlayVideoTime(videoTime);
                chairsStatusBeen.set(index, chairStatusBean);
                return true;
            }
            index++;
        }
        return false;
    }

    public static ChairStatusBean getChairStatusBeanByEggChairNum(String eggChairNum) {
        for (ChairStatusBean chairStatusBean :
                chairsStatusBeen) {
            if (chairStatusBean.getChair().getEgg_chair_num().equals(eggChairNum))
                return chairStatusBean;
        }
        return null;
    }

    public static boolean setChairStatusBeanCurrentTimeByChairNum(String eggChairNum, long time) {
        int index = 0;
        for (ChairStatusBean chairStatusBean :
                chairsStatusBeen) {
            if (chairStatusBean.getChair().getEgg_chair_num().equals(eggChairNum)) {
                chairStatusBean.setPlayVideoTime(time);
                chairsStatusBeen.set(index, chairStatusBean);
                return true;
            }
            index++;
        }
        return false;
    }

    public static void checkAllDeviceStatus(String eggNum) {
        ArrayList<DeviceBean> devicesBean = getDevicesByEggNum(eggNum);
        int i = 0, j = 0;
        for (DeviceBean deviceBean :
                devicesBean) {
            if (deviceBean.getDevicePlayVideoStatus() == Constants.VIDEO_TYPE_PANORAMAVIDEO)
                i++;
            else
                j++;
        }
        if(i > 0)
            setChairVideoTypeByChairChairNum(eggNum,Constants.VIDEO_TYPE_PANORAMAVIDEO);
        else if(j == devicesBean.size())
            setChairVideoTypeByChairChairNum(eggNum,Constants.VIDEO_TYPE_ORDINARYVIDEO);
    }

    public static void setVideoCurrentTimeByEggNum(String eggChairNum,long currentTime){
        int index = 0;
        for (ChairStatusBean chairStatusBean :
                chairsStatusBeen) {
            if (chairStatusBean.getChair().getEgg_chair_num().equals(eggChairNum)) {
                chairStatusBean.setCurrentTime(currentTime);
                chairsStatusBeen.set(index, chairStatusBean);
            }
            index++;
        }
    }
}
