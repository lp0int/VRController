package com.xiaohong.vrcontroller.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.xiaohong.vrcontroller.R;
import com.xiaohong.vrcontroller.Variable;

import static android.content.Context.WIFI_SERVICE;

/**
 * Created by DELL on 2017/5/15.
 */

public class Utils {

    public static void initTitle(Context mContext) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = ((Activity) mContext).getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        ((Activity) mContext).requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    public static void showToastStr(Context context, String s) {
        if (Variable.toast == null) {
            Variable.toast = Toast.makeText(context, s, Toast.LENGTH_SHORT);
            Variable.toast.show();
            Variable.oneTime = System.currentTimeMillis();
        } else {
            Variable.twoTime = System.currentTimeMillis();
            if (s.equals(Variable.oldMsg)) {
                if (Variable.twoTime - Variable.oneTime > Toast.LENGTH_SHORT) {
                    Variable.toast.show();
                }
            } else {
                Variable.oldMsg = s;
                Variable.toast.setText(s);
                Variable.toast.show();
            }
        }
        Variable.oneTime = Variable.twoTime;
    }

    public static String getVersion()//获取版本号
    {
        try {
            PackageInfo pi = Variable.BASECONTEXT.getPackageManager().getPackageInfo(Variable.BASECONTEXT.getPackageName(), 0);
            return pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return Variable.BASECONTEXT.getString(R.string.version_unknown);
        }
    }

    public static int getVersionCode()//获取版本号(内部识别号)
    {
        try {
            PackageInfo pi = Variable.BASECONTEXT.getPackageManager().getPackageInfo(Variable.BASECONTEXT.getPackageName(), 0);
            return pi.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static String getLocalIpAddress(Context mContext) {
        WifiManager wifiManager = (WifiManager) mContext.getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        // 获取32位整型IP地址
        int ipAddress = wifiInfo.getIpAddress();

        //返回整型地址转换成“*.*.*.*”地址
        return String.format("%d.%d.%d.%d",
                (ipAddress & 0xff), (ipAddress >> 8 & 0xff),
                (ipAddress >> 16 & 0xff), (ipAddress >> 24 & 0xff));
    }

    public static byte[] int2byte(int res) {
        byte[] targets = new byte[4];

        targets[0] = (byte) (res & 0xff);// 最低位
        targets[1] = (byte) ((res >> 8) & 0xff);// 次低位
        targets[2] = (byte) ((res >> 16) & 0xff);// 次高位
        targets[3] = (byte) (res >>> 24);// 最高位,无符号右移。
        return targets;
    }

    public static int byte2int(byte[] res) {
// 一个byte数据左移24位变成0x??000000，再右移8位变成0x00??0000
        int targets = (res[0] & 0xff) | ((res[1] << 8) & 0xff00) // | 表示安位或
                | ((res[2] << 24) >>> 8) | (res[3] << 24);
        return targets;
    }

    public static String ConversionMsToSecond(long ms) {
        long wholeSecond = ms / 1000;
        int minute = (int) wholeSecond / 60;
        int second = (int) wholeSecond % 60;
        return minute + ":" + second;
    }

}
