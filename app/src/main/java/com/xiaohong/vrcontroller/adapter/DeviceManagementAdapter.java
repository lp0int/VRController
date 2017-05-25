package com.xiaohong.vrcontroller.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiaohong.vrcontroller.Constants;
import com.xiaohong.vrcontroller.Interface.SubscriberOnNextListener;
import com.xiaohong.vrcontroller.R;
import com.xiaohong.vrcontroller.Variable;
import com.xiaohong.vrcontroller.bean.ChairStatusBean;
import com.xiaohong.vrcontroller.bean.DeviceBean;
import com.xiaohong.vrcontroller.bean.RequestObject;
import com.xiaohong.vrcontroller.bean.UpdatePlayActionBean;
import com.xiaohong.vrcontroller.bean.VideoControl;
import com.xiaohong.vrcontroller.utils.DebugTools;
import com.xiaohong.vrcontroller.utils.NetworkRequestMethods;
import com.xiaohong.vrcontroller.utils.ProgressSubscriber;
import com.xiaohong.vrcontroller.utils.Utils;
import com.xiaohong.vrcontroller.utils.net.MsgFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Handler;

import static java.lang.Thread.sleep;

/**
 * Created by Lpoint on 2017/5/17.
 * <p>
 * Copyright (c) 2017 Shanghai Xiaohong Technology company. All rights reserved.
 */

public class DeviceManagementAdapter extends RecyclerView.Adapter<DeviceManagementAdapter.DeviceViewHolder> {
    private Context mContext;
    private SubscriberOnNextListener updatePlayActionListener;

    public DeviceManagementAdapter(Context context, SubscriberOnNextListener updatePlayActionListener) {
        mContext = context;
        this.updatePlayActionListener = updatePlayActionListener;
    }

    @Override
    public DeviceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        DeviceViewHolder holder = new DeviceViewHolder(LayoutInflater.from(mContext).inflate(R.layout.fragment_device_management_item, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final DeviceViewHolder holder, final int position) {
        String eggChairId = Variable.getChairList().split(",")[position];
        final String eggChairNum = Variable.getChairNumById(Integer.parseInt(eggChairId));
        holder.txtEggChairId.setText(Variable.getChairNumById(Integer.parseInt(eggChairId)));
        final ChairStatusBean chairStatusBean = Variable.getChairStatusBeanByEggChairNum(eggChairNum);
        final ArrayList<DeviceBean> arrays = Variable.getDevicesByEggNum(eggChairNum);
        if (arrays == null) {
            return;
        }
        int index = 0;
        for (DeviceBean device :
                arrays) {
            switch (index) {
                case 0:
                    holder.txtDeviceId1.setVisibility(View.VISIBLE);
                    holder.txtDeviceId1.setText(device.getVRDeviceInfo().getMac());
                    holder.imgStatus1.setVisibility(View.VISIBLE);
                    holder.imgStatus1.setImageDrawable(getDeviceStatusDrawable(device.getDevicePlayVideoStatus()));
                    holder.txtPower1.setVisibility(View.VISIBLE);
                    holder.txtPower1.setText(device.getVRDeviceInfo().getRemainingPower() + "");
                    break;
                case 1:
                    holder.txtDeviceId2.setVisibility(View.VISIBLE);
                    holder.txtDeviceId2.setText(device.getVRDeviceInfo().getMac());
                    holder.imgStatus2.setVisibility(View.VISIBLE);
                    holder.imgStatus2.setImageDrawable(getDeviceStatusDrawable(device.getDevicePlayVideoStatus()));
                    holder.txtPower2.setVisibility(View.VISIBLE);
                    holder.txtPower2.setText(device.getVRDeviceInfo().getRemainingPower() + "");
                    break;
                case 2:
                    holder.txtDeviceId3.setVisibility(View.VISIBLE);
                    holder.txtDeviceId3.setText(device.getVRDeviceInfo().getMac());
                    holder.imgStatus3.setVisibility(View.VISIBLE);
                    holder.imgStatus3.setImageDrawable(getDeviceStatusDrawable(device.getDevicePlayVideoStatus()));
                    holder.txtPower3.setVisibility(View.VISIBLE);
                    holder.txtPower3.setText(device.getVRDeviceInfo().getRemainingPower() + "");
                    break;
                case 3:
                    holder.txtDeviceId4.setVisibility(View.VISIBLE);
                    holder.txtDeviceId4.setText(device.getVRDeviceInfo().getMac());
                    holder.imgStatus4.setVisibility(View.VISIBLE);
                    holder.imgStatus4.setImageDrawable(getDeviceStatusDrawable(device.getDevicePlayVideoStatus()));
                    holder.txtPower4.setVisibility(View.VISIBLE);
                    holder.txtPower4.setText(device.getVRDeviceInfo().getRemainingPower() + "");
                    break;
                default:
                    break;
            }
            index++;
        }
        if (chairStatusBean != null) {
            holder.imgBtnPlay.setImageDrawable(getPlayBtnStatusDrawable(chairStatusBean.getPlayVideoType()));
            holder.videoProgress.setProgress(0);
            holder.videoTime.setText(Utils.ConversionMsToSecond(chairStatusBean.getCurrentTime()) +
                    "/" + Utils.ConversionMsToSecond(chairStatusBean.getPlayVideoTime()));
            if (chairStatusBean.getCurrentTime() == -1)
                holder.videoTime.setText("00:00/00:00");
        }
        holder.imgBtnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (DeviceBean device :
                        arrays) {
                    VideoControl mVideoControl = new VideoControl();
                    mVideoControl.setVideoId("0");
                    mVideoControl.setAction(chairStatusBean.getPlayVideoType() == Constants.VIDEO_TYPE_ORDINARYVIDEO ?
                            Constants.VIDEO_TYPE_PANORAMAVIDEO : Constants.VIDEO_TYPE_ORDINARYVIDEO);
                    RequestObject mRequestObject = new RequestObject();
                    mRequestObject.setRequestObjectType(0x02);
                    mRequestObject.setRequestObjectJson(MsgFactory.getInstance(mContext).mGson.toJson(mVideoControl));
                    byte[] data = MsgFactory.getInstance(mContext).mGson.toJson(mRequestObject).getBytes();
                    byte[] msg_length = Utils.int2byte(data.length);
                    byte[] send_data = new byte[data.length + msg_length.length];
                    System.arraycopy(msg_length, 0, send_data, 0, msg_length.length);
                    System.arraycopy(data, 0, send_data, msg_length.length, data.length);
                    try {
                        DebugTools.showDebugLog("tcp 发出TCP消息：", new String(send_data));
                        device.getDeviceSocket().getOutputStream().write(send_data);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (chairStatusBean.getPlayVideoType() == Constants.VIDEO_TYPE_PANORAMAVIDEO) {
                    chairStatusBean.setPlayVideoType(Constants.VIDEO_TYPE_ORDINARYVIDEO);
                } else {
                    chairStatusBean.setCurrentTime(-1);
                    chairStatusBean.setPlayVideoType(Constants.VIDEO_TYPE_PANORAMAVIDEO);
                }
                notifyDataSetChanged();
            }
        });

        Thread videoProgressThread = new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        float videoTimeSecond = chairStatusBean.getPlayVideoTime() / 1000;
                        float i = (float) chairStatusBean.getCurrentTime();
                        if (i == -1) {
                            ((Activity) mContext).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    holder.videoProgress.setProgress(0);
                                    holder.videoTime.setText("--:--/--:--");
                                }
                            });
                            return;
                        }
                        while (i <= videoTimeSecond && chairStatusBean.getPlayVideoType() == Constants.VIDEO_TYPE_PANORAMAVIDEO) {
                            int videoProgress = (int) (i / videoTimeSecond * 100);
                            holder.videoProgress.setProgress(videoProgress);
                            Variable.setVideoCurrentTimeByEggNum(chairStatusBean.getChair().getEgg_chair_num(), (int) i);
                            ((Activity) mContext).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    holder.videoTime.setText(Utils.ConversionMsToSecond(chairStatusBean.getCurrentTime() * 1000) +
                                            "/" + Utils.ConversionMsToSecond(chairStatusBean.getPlayVideoTime()));
                                }
                            });
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            if (videoTimeSecond - i == 5) {
                                for (final DeviceBean device :
                                        arrays) {
                                    ((Activity)mContext).runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            NetworkRequestMethods.getInstance().updatePlayAction(new ProgressSubscriber<UpdatePlayActionBean>(updatePlayActionListener, mContext, "上报视频播放完成..."),
                                                    device.getVRDeviceInfo().getMac(),
                                                    device.getVRDeviceInfo().getEggChairNum());
                                        }
                                    });
                                }
                            }
                            i++;
                        }
                        Variable.setVideoCurrentTimeByEggNum(chairStatusBean.getChair().getEgg_chair_num(), -1);
                    }
                });
        videoProgressThread.start();
    }

    @Override
    public int getItemCount() {
        if (TextUtils.isEmpty(Variable.getChairList()) || Variable.getChairList().equals(0))
            return 0;
        return Variable.getChairList().split(",").length;
    }

    public class DeviceViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout relTitle1, relTitle2, relTitle3, relTitle4;
        private TextView txtEggChairId, txtDeviceId1, txtDeviceId2, txtDeviceId3, txtDeviceId4;
        private ImageView imgStatus1, imgStatus2, imgStatus3, imgStatus4;
        private TextView txtPower1, txtPower2, txtPower3, txtPower4;

        private ProgressBar videoProgress;
        private ImageView imgBtnPlay;
        private TextView videoTime;

        public DeviceViewHolder(View itemView) {
            super(itemView);
            initView(this, itemView);
        }


    }

    private void initView(DeviceViewHolder holder, View view) {
        holder.imgBtnPlay = (ImageView) view.findViewById(R.id.img_play);
        holder.videoProgress = (ProgressBar) view.findViewById(R.id.video_progress);
        holder.videoTime = (TextView) view.findViewById(R.id.txt_video_time);
        holder.relTitle1 = (RelativeLayout) view.findViewById(R.id.rel_title_device1);
        holder.relTitle2 = (RelativeLayout) view.findViewById(R.id.rel_title_device2);
        holder.relTitle3 = (RelativeLayout) view.findViewById(R.id.rel_title_device3);
        holder.relTitle4 = (RelativeLayout) view.findViewById(R.id.rel_title_device4);
        holder.txtEggChairId = (TextView) view.findViewById(R.id.txt_egg_chair_id);
        holder.txtDeviceId1 = (TextView) view.findViewById(R.id.txt_device_id_1);
        holder.txtDeviceId2 = (TextView) view.findViewById(R.id.txt_device_id_2);
        holder.txtDeviceId3 = (TextView) view.findViewById(R.id.txt_device_id_3);
        holder.txtDeviceId4 = (TextView) view.findViewById(R.id.txt_device_id_4);
        holder.imgStatus1 = (ImageView) view.findViewById(R.id.img_status_1);
        holder.imgStatus2 = (ImageView) view.findViewById(R.id.img_status_2);
        holder.imgStatus3 = (ImageView) view.findViewById(R.id.img_status_3);
        holder.imgStatus4 = (ImageView) view.findViewById(R.id.img_status_4);
        holder.txtPower1 = (TextView) view.findViewById(R.id.txt_power_1);
        holder.txtPower2 = (TextView) view.findViewById(R.id.txt_power_2);
        holder.txtPower3 = (TextView) view.findViewById(R.id.txt_power_3);
        holder.txtPower4 = (TextView) view.findViewById(R.id.txt_power_4);
    }

    public Drawable getDeviceStatusDrawable(int status) {
        Drawable d = null;
        switch (status) {
            case 0x00:
                d = ((Activity) mContext).getResources().getDrawable(R.mipmap.icon_online);
                break;
            case 0x01:
                d = ((Activity) mContext).getResources().getDrawable(R.mipmap.icon_standby);
                break;
            default:
                break;
        }
        return d;
    }

    public Drawable getPlayBtnStatusDrawable(int status) {
        Drawable d = null;
        switch (status) {
            case 0x00:
                d = ((Activity) mContext).getResources().getDrawable(R.mipmap.btn_pause);
                break;
            case 0x01:
                d = ((Activity) mContext).getResources().getDrawable(R.mipmap.btn_play);
                break;
            default:
                break;
        }
        return d;
    }
}
