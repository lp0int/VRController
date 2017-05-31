package com.xiaohong.vrcontroller;

/**
 * Created by DELL on 2017/5/15.
 */

public class Constants {
    public static final int MODE_OFF_LINE = 0x02;
    public static final int MODE_ONLINE = 0x01;

    public static final String BASE_URL = "http://vr.jinkechen.com/";
    public static final int DEFAULT_TIMEOUT  = 20;
    public static final String nullEggChairId = "100000000";
    public static final String USER_DEFAULT_PWD = "123456";
    /**
     * 网络请求提示信息
     */
    public static final String DEFAULT_PROGRESS_MESSAGE = "数据请求中，请稍等";
    /**
     * SharedPreferences相关数据
     */
    public static final String LOGIN_STATUS = "loginStatus";
    public static final String USER_NAME = "userName";
    public static final String PASSWORD = "password";;
    public static final String NICK_NAME = "nickName";;
    public static final String EGG_CHAIR_ID = "eggChairId";;
    public static final String PHONE = "phone";
    public static final String DEVICE_INFO = "deviceInfo";
    public static final String CHAIR_LIST = "chairList";
    /**
     * WebViewFragment相关数据
     */
    public static final String EXTERNAL_URL = "external.url";
    /**
     * 内网通信请求Type
     */
    public static final int REQUEST_VRDEVICEINFO = 0x00;
    public static final int REQUEST_PADDEVICEINFO = 0x01;
    public static final int REQUEST_VIDEOCONTROL = 0x02;
    public static final int REQUEST_RESPONCEPLAYCOMMAND = 0x03;
    /**
     * 相关网络通信端口
     */
    public static final int TCP_PORT = 10240;
    public static final int UDP_RECEIVE_PORT = 7800;
    public static final int UDP_SEND_PORT = 7900;
    public static final String BROADCAST_IP = "255.255.255.255";
    /**
     * 播放视频一体机反馈的状态
     */
    public static final Integer DEVICES_PLAYVIDEO_STATUS_OK = 0x00;
    public static final Integer DEVICES_PLAYVIDEO_STATUS_STANDBY = 0x01;
    /**
     * VideoType类型
     */
    public static final Integer VIDEO_TYPE_PANORAMAVIDEO = 0x00;
    public static final Integer VIDEO_TYPE_ORDINARYVIDEO = 0x01;
    /**
     * Test
     */
}
