package com.xiaohong.vrcontroller.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.xiaohong.vrcontroller.Constants;
import com.xiaohong.vrcontroller.Interface.SubscriberOnNextListener;
import com.xiaohong.vrcontroller.R;
import com.xiaohong.vrcontroller.Variable;
import com.xiaohong.vrcontroller.base.BaseActivity;
import com.xiaohong.vrcontroller.bean.LoginBean;
import com.xiaohong.vrcontroller.utils.DebugTools;
import com.xiaohong.vrcontroller.utils.NetworkRequestMethods;
import com.xiaohong.vrcontroller.utils.ProgressSubscriber;
import com.xiaohong.vrcontroller.utils.SharedPreferencesUtils;
import com.xiaohong.vrcontroller.utils.Utils;

public class ActivityLogin extends BaseActivity implements View.OnClickListener {
    private EditText edtUserName;
    private EditText edtPasswd;
    private Button btnLogin;
    private String username;
    private String passwd;
    private SubscriberOnNextListener LoginListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.initTitle(ActivityLogin.this);
        setContentView(R.layout.activity_login);
        initView();
    }

    private void initView() {
        edtUserName = (EditText) findViewById(R.id.edt_username);
        edtPasswd = (EditText) findViewById(R.id.edt_passwd);
        btnLogin = (Button) findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(this);
        initRequestListener();
        String defaultUserName = SharedPreferencesUtils.getStringValue(ActivityLogin.this, Constants.LOGIN_STATUS, Constants.USER_NAME, "null");
        if (!"null".equals(defaultUserName)) {
            edtUserName.setText(defaultUserName);
            edtPasswd.requestFocus();
        }
        if (DebugTools.DEBUG_MODE)
            edtPasswd.setText("123456");
    }

    private void initRequestListener() {
        LoginListener = new SubscriberOnNextListener<LoginBean>() {
            @Override
            public void onNext(LoginBean mLoginBean) {
                Utils.showToastStr(ActivityLogin.this, mLoginBean.getContent());
                if (mLoginBean.getStatusCode() == 300) {
                    Variable.loginBean = mLoginBean;
                    SharedPreferencesUtils.setStringValue(ActivityLogin.this, Constants.LOGIN_STATUS, Constants.USER_NAME, username);
                    SharedPreferencesUtils.setStringValue(ActivityLogin.this, Constants.LOGIN_STATUS, Constants.PASSWORD, passwd);
                    SharedPreferencesUtils.setStringValue(ActivityLogin.this, Constants.LOGIN_STATUS, Constants.NICK_NAME, mLoginBean.getUser_info().get(0).getNickName());
                    SharedPreferencesUtils.setStringValue(ActivityLogin.this, Constants.LOGIN_STATUS, Constants.EGG_CHAIR_ID, mLoginBean.getUser_info().get(0).getEgg_chair_id());
                    SharedPreferencesUtils.setStringValue(ActivityLogin.this, Constants.LOGIN_STATUS, Constants.PHONE, mLoginBean.getUser_info().get(0).getPhone());
                    Variable.userName = edtUserName.getText().toString();
                    Intent intent = new Intent(ActivityLogin.this, ActivityHome.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onError(Throwable e) {

            }
        };
    }

    private void doLogin() {
        if (LoginListener == null)
            initRequestListener();
        username = edtUserName.getText().toString();
        passwd = edtPasswd.getText().toString();
        NetworkRequestMethods.getInstance().login(new ProgressSubscriber<LoginBean>(LoginListener, ActivityLogin.this, "努力登录中..."), username, passwd);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                doLogin();
                break;
        }
    }
}
