package com.xiaohong.vrcontroller.ui;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.xiaohong.vrcontroller.Constants;
import com.xiaohong.vrcontroller.Interface.JsCallJavaInterface;
import com.xiaohong.vrcontroller.R;
import com.xiaohong.vrcontroller.base.BaseFragment;
import com.xiaohong.vrcontroller.utils.DialogUtils;
import com.xiaohong.vrcontroller.utils.Utils;

/**
 * Created by Lpoint on 2017/5/16.
 * <p>
 * Copyright (c) 2017 Shanghai Xiaohong Technology company. All rights reserved.
 */

public class FragmentWebView extends BaseFragment {
    private WebView webView;
    private String mUrl = "";
    private ProgressBar webViewProgressBar;

    public static FragmentWebView newInstance(String url) {
        FragmentWebView instance = new FragmentWebView();
        Bundle args = new Bundle();
        args.putString(Constants.EXTERNAL_URL, url);
        instance.setArguments(args);
        return instance;
    }

    public FragmentWebView() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_webview, null);
        mUrl = getArguments().getString(Constants.EXTERNAL_URL);
        initView(view);
        if (webView != null)
            webView.loadUrl("javascript:OnCreate()");
        return view;
    }

    private void initView(View view) {
        webView = (WebView) view.findViewById(R.id.webview);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.setVerticalScrollBarEnabled(false);
        webViewProgressBar = (ProgressBar) view.findViewById(R.id.webview_progress);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(false);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setAllowFileAccess(true);// 设置允许访问文件数据
        webSettings.setSupportZoom(false);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setAppCacheEnabled(false);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUserAgentString(webView.getSettings().getUserAgentString() + "; Android/VRControlClient/"
                + Utils.getVersion() + "/" + Utils.getVersionCode());
        webView.addJavascriptInterface(new JsCallJavaInterface(getContext()), "JsCallJavaInterface");
        webView.setWebChromeClient(new MyWebChromeClient());
        webView.setWebViewClient(new WebViewClient() {
            // 重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                mUrl = url;
                if (url.startsWith("http:") || url.startsWith("https:") || url.startsWith("file:")) {
                    view.loadUrl(url);
                    return true;
                } else {
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(intent);
                    } catch (ActivityNotFoundException e) {
                        if (url.startsWith("weixin://")) {
                            Utils.showToastStr(getActivity(), "未安装微信");
                            webView.loadUrl("http://weixin.qq.com");
                            return false;
                        }
                    }
                }
                return true;
            }
        });
        webView.loadUrl(mUrl);
    }

    private class MyWebChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            // TODO 自动生成的方法存根
            if (newProgress == 100) {
                webViewProgressBar.setVisibility(View.GONE);// 加载完网页进度条消失
            } else {
                webViewProgressBar.setVisibility(View.VISIBLE);// 开始加载网页时显示进度条
                webViewProgressBar.setProgress(newProgress);// 设置进度值
            }
        }

        @Override
        public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
            DialogUtils.getDialog(getActivity(), "提示信息", message, "确认", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    result.cancel();
                }
            }, "", null).setCancelable(false).show();
            return true;
            // return super.onJsAlert(view, url, message, result);
        }

        @Override
        public boolean onJsConfirm(WebView arg0, String url, String message, final JsResult result) {
            // TODO Auto-generated method stub
            DialogUtils.getDialog(getActivity(), "提示信息", message, "确认", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    result.confirm();
                }
            }, "取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    result.cancel();
                }
            }).setCancelable(false).show();
            return true;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (webView != null)
            webView.loadUrl("javascript:OnPause()");
    }

    @Override
    public void onResume() {
        super.onResume();
        if (webView != null)
            webView.loadUrl("javascript:OnResume()");
    }

}
