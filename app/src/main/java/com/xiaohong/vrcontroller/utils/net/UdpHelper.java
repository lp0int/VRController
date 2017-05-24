package com.xiaohong.vrcontroller.utils.net;

import android.util.Log;

import com.xiaohong.vrcontroller.utils.DebugTools;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * Created by Lpoint on 2017/5/18.
 * <p>
 * Copyright (c) 2017 Shanghai Xiaohong Technology company. All rights reserved.
 */

public class UdpHelper {
    public static void send(int udpSendPort, String message, String udpAddress) {
        message = (message == null ? "Hello IdeasAndroid!" : message);
        int server_port = udpSendPort;
        DebugTools.showDebugLog("UDP Demo", "UDP发送数据:" + message);
        DatagramSocket s = null;
        try {
            s = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
        }
        InetAddress local = null;
        try {
            local = InetAddress.getByName(udpAddress);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        byte[] messageByte = null;
        try {
            messageByte = message.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        DatagramPacket p = new DatagramPacket(messageByte, messageByte.length, local, server_port);
        try {
            s.send(p);
            s.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
