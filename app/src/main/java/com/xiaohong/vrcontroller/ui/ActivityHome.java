package com.xiaohong.vrcontroller.ui;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiaohong.vrcontroller.Constants;
import com.xiaohong.vrcontroller.Interface.SubscriberOnNextListener;
import com.xiaohong.vrcontroller.R;
import com.xiaohong.vrcontroller.Variable;
import com.xiaohong.vrcontroller.base.BaseActivity;
import com.xiaohong.vrcontroller.bean.UpdateDeviceBean;
import com.xiaohong.vrcontroller.bean.DeviceBean;
import com.xiaohong.vrcontroller.bean.EditUserBean;
import com.xiaohong.vrcontroller.bean.FindUsersBean;
import com.xiaohong.vrcontroller.bean.PadDeviceInfo;
import com.xiaohong.vrcontroller.bean.RequestObject;
import com.xiaohong.vrcontroller.utils.DebugTools;
import com.xiaohong.vrcontroller.utils.NetworkRequestMethods;
import com.xiaohong.vrcontroller.utils.ProgressSubscriber;
import com.xiaohong.vrcontroller.utils.Utils;
import com.xiaohong.vrcontroller.utils.net.MsgFactory;
import com.xiaohong.vrcontroller.utils.net.ServerSocketThread;
import com.xiaohong.vrcontroller.utils.net.UdpHelper;
import com.xiaohong.vrcontroller.utils.widget.PopWindowEditMyPwd;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import static java.lang.Thread.sleep;

/**
 * Created by Lpoint on 2017/5/16.
 * <p>
 * Copyright (c) 2017 Shanghai Xiaohong Technology company. All rights reserved.
 */

public class ActivityHome extends BaseActivity implements View.OnClickListener {
    private RelativeLayout relTab1, relTab2, relTab3, relTab4;
    private ArrayList<Fragment> fragments;
    private TextView txtNickname;
    private int lastSelectId = -1;
    private SubscriberOnNextListener findUserListener;
    private SubscriberOnNextListener editPwdInfoListener;
    private SubscriberOnNextListener updateDeviceInfoListener;
    private ImageView imgLogout, imgHelp;
    private Thread sendUdpThread;
    private boolean destroy = false;
    public static View viewCover;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.initTitle(ActivityHome.this);
        setContentView(R.layout.activity_home);
        initView();
    }

    private void initView() {
        relTab1 = (RelativeLayout) findViewById(R.id.rel_tab1);
        relTab1.setOnClickListener(this);
        relTab2 = (RelativeLayout) findViewById(R.id.rel_tab2);
        relTab2.setOnClickListener(this);
        relTab3 = (RelativeLayout) findViewById(R.id.rel_tab3);
        relTab3.setOnClickListener(this);
        relTab4 = (RelativeLayout) findViewById(R.id.rel_tab4);
        relTab4.setOnClickListener(this);
        txtNickname = (TextView) findViewById(R.id.txt_nickname);
        txtNickname.setText(TextUtils.isEmpty(Variable.loginBean.getUser_info().get(0).getNickName())
                ? "" : Variable.loginBean.getUser_info().get(0).getNickName());
        txtNickname.setOnClickListener(this);
        if (!Variable.loginBean.getUser_info().get(0).getEgg_chair_id().startsWith("0")) {
            relTab2.setVisibility(View.GONE);
            relTab4.setVisibility(View.GONE);
        } else {
            relTab3.setVisibility(View.GONE);
        }
        imgLogout = (ImageView) findViewById(R.id.img_logout);
        imgLogout.setOnClickListener(this);
        imgHelp = (ImageView) findViewById(R.id.img_help);
        imgHelp.setOnClickListener(this);
        viewCover = findViewById(R.id.view_cover);
        initRequestListenter();
        startTCPServer();
        NetworkRequestMethods.getInstance().findUser(new ProgressSubscriber<FindUsersBean>(findUserListener, this, "获取用户信息中..."));
        initFragment();
        tabSelect(0);
        sendUdpThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (destroy)
                        return;
                    RequestObject requestObject = new RequestObject();
                    requestObject.setRequestObjectType(Constants.REQUEST_PADDEVICEINFO);
                    PadDeviceInfo padDeviceInfo = new PadDeviceInfo();
                    padDeviceInfo.setTcpPort(Constants.TCP_PORT);
                    padDeviceInfo.setIp(Utils.getLocalIpAddress(ActivityHome.this));
                    String[] strs = Variable.getChairList().split(",");
                    if (strs.length == 1) {
                        ArrayList<String> arrstr = new ArrayList<String>();
                        arrstr.add(Variable.getChairNumById(Integer.parseInt(strs[0])));
                        padDeviceInfo.setEggChairList(arrstr);
                    } else {
                        ArrayList<String> chairs = new ArrayList<String>();
                        for (String s :
                                strs) {
                            chairs.add(Variable.getChairNumById(Integer.parseInt(s)));
                        }
                        padDeviceInfo.setEggChairList(chairs);
                    }
                    requestObject.setRequestObjectJson(MsgFactory.getInstance(ActivityHome.this).mGson.toJson(padDeviceInfo));
                    UdpHelper.send(Constants.UDP_SEND_PORT, MsgFactory.getInstance(ActivityHome.this).mGson.toJson(requestObject), Constants.BROADCAST_IP);
                    try {
                        sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void initFragment() {
        fragments = new ArrayList<>();
        fragments.add(FragmentWebView.newInstance("http://10.0.0.101/index.php/index/motion/dateShow?user=" + Variable.userName));
        fragments.add(FragmentStaffManagement.newInstance());
        fragments.add(FragmentDeviceManagement.newInstance());
        fragments.add(FragmentWebView.newInstance("http://10.0.0.101/index.php/index/motion/report?user=" + Variable.userName));
    }

    public void onTabSelected(int position) {
        if (fragments != null) {
            FragmentManager fm = this.getSupportFragmentManager();
            FragmentTransaction transaction = fm.beginTransaction();
            Fragment fragment = fragments.get(position % fragments.size());
            if (fragment.isAdded()) {
                transaction.show(fragment);
            } else {
                transaction.add(R.id.rel_content, fragment);
                transaction.show(fragment);
            }
            transaction.commitAllowingStateLoss();
        }
    }

    public void onTabUnselected(int position) {
        if (fragments != null && lastSelectId != -1) {
            FragmentManager fm = this.getSupportFragmentManager();
            FragmentTransaction transaction = fm.beginTransaction();
            Fragment fragment = fragments.get(position % fragments.size());
            transaction.hide(fragment);
            transaction.commit();
        }
    }

    private void tabSelect(int selectId) {
        onTabUnselected(lastSelectId);
        onTabSelected(selectId);
        try {
            for (int i = 0; i < 4; i++) {
                int rel_id = getResources().getIdentifier("rel_tab" + (i + 1), "id", "com.xiaohong.vrcontroller");
                int line_id = getResources().getIdentifier("line_tab" + (i + 1), "id", "com.xiaohong.vrcontroller");
                int txt_id = getResources().getIdentifier("txt_tab" + (i + 1), "id", "com.xiaohong.vrcontroller");
                int img_id = getResources().getIdentifier("img_tab" + (i + 1), "id", "com.xiaohong.vrcontroller");
                RelativeLayout relTab = (RelativeLayout) findViewById(rel_id);
                View lineTab = findViewById(line_id);
                TextView txtTab = (TextView) findViewById(txt_id);
                ImageView imgTab = (ImageView) findViewById(img_id);
                if (selectId == i) {
                    relTab.setBackgroundColor(Color.parseColor("#FF272929"));
                    txtTab.setAlpha(1.0f);
                    lineTab.setVisibility(View.VISIBLE);
                    imgTab.setBackgroundDrawable(getDrawable(i, 1));
                } else {
                    relTab.setBackgroundColor(Color.parseColor("#00272929"));
                    txtTab.setAlpha(0.6f);
                    lineTab.setVisibility(View.GONE);
                    imgTab.setBackgroundDrawable(getDrawable(i, 0));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        lastSelectId = selectId;
    }

    /**
     * @param id     Item的ID
     * @param status 0表示未选中  1表示选中
     * @return
     */
    private Drawable getDrawable(int id, int status) {
        Drawable d = null;
        switch (id) {
            case 0:
                if (status == 0) {
                    d = getResources().getDrawable(R.mipmap.menu_icon_home_default);
                    break;
                }
                d = getResources().getDrawable(R.mipmap.menu_icon_home_pressed);
                break;
            case 1:
                if (status == 0) {
                    d = getResources().getDrawable(R.mipmap.menu_icon_staff_management_default);
                    break;
                }
                d = getResources().getDrawable(R.mipmap.menu_icon_staff_management_pressed);
                break;
            case 2:
                if (status == 0) {
                    d = getResources().getDrawable(R.mipmap.menu_icon_device_management_default);
                    break;
                }
                d = getResources().getDrawable(R.mipmap.menu_icon_device_management_pressed);
                break;
            case 3:
                if (status == 0) {
                    d = getResources().getDrawable(R.mipmap.menu_icon_report_management_default);
                    break;
                }
                d = getResources().getDrawable(R.mipmap.menu_icon_report_management_pressed);
                break;
            default:
                break;
        }
        return d;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rel_tab1:
                tabSelect(0);
                break;
            case R.id.rel_tab2:
                tabSelect(1);
                break;
            case R.id.rel_tab3:
                tabSelect(2);
                break;
            case R.id.rel_tab4:
                tabSelect(3);
                break;
            case R.id.img_logout:
                finish();
                break;
            case R.id.txt_nickname:
                PopWindowEditMyPwd popWindowEditMyPwd = new PopWindowEditMyPwd(ActivityHome.this, editPwdInfoListener);
                popWindowEditMyPwd.showPopupWindow(v);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        destroy = true;
        sendUdpThread = null;
    }

    private void initRequestListenter() {
        findUserListener = new SubscriberOnNextListener<FindUsersBean>() {
            @Override
            public void onNext(FindUsersBean findUsersBean) {
                Variable.setFindUserBean(findUsersBean);
                if (!Variable.loginBean.getUser_info().get(0).getEgg_chair_id().startsWith("0")) {
                    sendUdpThread.start();
                    startUpdate();
                }
            }

            @Override
            public void onError(Throwable e) {
            }
        };
        editPwdInfoListener = new SubscriberOnNextListener<EditUserBean>() {
            @Override
            public void onNext(EditUserBean editUserBean) {
                Utils.showToastStr(ActivityHome.this, editUserBean.getContent());
            }

            @Override
            public void onError(Throwable e) {

            }
        };
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent home = new Intent(Intent.ACTION_MAIN);
            home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            home.addCategory(Intent.CATEGORY_HOME);
            startActivity(home);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void startTCPServer() {
        if (Variable.serverSocketThread == null) {
            Variable.serverSocketThread = new ServerSocketThread(ActivityHome.this);
        }
        if (Variable.serverSocketThread.getServerSocket() == null && !Variable.serverSocketThread.isAlive()) {
            Variable.serverSocketThread.start();
        }
    }

    private void startUpdate() {

        final Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 1) {
                    for (DeviceBean deviceBean :
                            Variable.devices) {
                        NetworkRequestMethods.getInstance().updateDevice(new ProgressSubscriber<UpdateDeviceBean>(updateDeviceInfoListener, ActivityHome.this, "数据上报中..."), deviceBean.getDeviceSocket().getInetAddress().getHostAddress(),
                                deviceBean.getVRDeviceInfo().getMac(),
                                deviceBean.getVRDeviceInfo().getMac(),
                                deviceBean.getVRDeviceInfo().getEggChairNum());
                    }
                }
            }
        };


        Timer timer = new Timer(true);

        TimerTask task = new TimerTask() {
            public void run() {
                Message msg = new Message();
                msg.what = 1;
                handler.sendMessage(msg);
            }
        };

        timer.schedule(task, 10 * 1000, 10 * 60 * 1000);
    }
}
