package com.xiaohong.vrcontroller.utils.net;

import android.content.Context;

import com.xiaohong.vrcontroller.Variable;
import com.xiaohong.vrcontroller.utils.DebugTools;
import com.xiaohong.vrcontroller.utils.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Lpoint on 2017/5/18.
 * <p>
 * Copyright (c) 2017 Shanghai Xiaohong Technology company. All rights reserved.
 */

public class ReceiveThread extends Thread {
    private InputStream mInputStream;
    protected OutputStream mOutputStream;
    private Context mContext;
    private int port;
    private String ip;

    public ReceiveThread(InputStream inputStream,OutputStream mOutputStream, Context mContext,int port, String ip) {
        mInputStream = inputStream;
        this.mOutputStream = mOutputStream;
        this.mContext = mContext;
        this.port = port;
        this.ip = ip;
    }

    @Override
    public void run() {
        while (true) {
            try {
                final byte[] buf = new byte[1024];
                final int len = mInputStream.read(buf);
                if (len == -1) {
                    DebugTools.showDebugLog("TcpServer","Tcp连接断开，ip：" + ip);
                    Variable.removeDevicesByIp(ip);
                    return;
                }
                byte[] data = new byte[len];
                System.arraycopy(buf,0,data,0,len);
                MsgFactory.getInstance(mContext).handleTcpMsg(ip,data);
                byte[] data_length = new byte[4];
                System.arraycopy(buf, 0, data_length, 0, 4);
                DebugTools.showDebugLog("tcp", "收到TCP消息：" + Utils.byte2int(data_length) + new String(buf,4,Utils.byte2int(data_length)));
            } catch (Exception e) {
                DebugTools.showDebugLog("tcpServerException", "tcpServer receiveMsg error:" + e.getMessage());
            }
        }
    }
}
