package com.xiaohong.vrcontroller.utils.net;

import android.content.Context;

import com.xiaohong.vrcontroller.Constants;
import com.xiaohong.vrcontroller.Variable;
import com.xiaohong.vrcontroller.bean.DeviceBean;
import com.xiaohong.vrcontroller.utils.DebugTools;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Lpoint on 2017/5/18.
 * <p>
 * Copyright (c) 2017 Shanghai Xiaohong Technology company. All rights reserved.
 */

public class ServerSocketThread extends Thread {
    private Context mContext;
    private ServerSocket mServerSocket = null;
    private Socket clicksSocket;
    private InputStream mInputStream;
    private int remotePort;
    private String remoteIP;
    private OutputStream mOutputStream;

    public ServerSocketThread(Context context) {
        mContext = context;
    }

    public ServerSocket getServerSocket() {
        return mServerSocket;
    }

    public Socket getClicksSocket() {
        return clicksSocket;
    }

    @Override
    public void run() {
        try {
            mServerSocket = new ServerSocket(Constants.TCP_PORT);
            DebugTools.showDebugLog("tcpServer", "tcpServer start");
        } catch (Exception e) {
            DebugTools.showDebugLog("tcpServerException", "tcpServer start fail:" + e.getMessage());
        }
        while (true) {
            try {
                clicksSocket = mServerSocket.accept();
                DeviceBean deviceBean = new DeviceBean();
                deviceBean.setDeviceSocket(clicksSocket);
                Variable.addDevices(deviceBean);
                mInputStream = clicksSocket.getInputStream();
                remotePort = clicksSocket.getPort();
                remoteIP = clicksSocket.getInetAddress().getHostAddress();
                mOutputStream = clicksSocket.getOutputStream();
                ReceiveThread receiveThread = new ReceiveThread(mInputStream, mOutputStream, mContext, remotePort, remoteIP);
                DebugTools.showDebugLog("tcpServer", "tcpServiceConnection ip: " + remoteIP + "--port:" + remotePort);
                receiveThread.start();
            } catch (Exception e) {
                DebugTools.showDebugLog("tcpServerException", "ReceiveThread fail:" + e.getMessage());
            }
        }
    }
}
