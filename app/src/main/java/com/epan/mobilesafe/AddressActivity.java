package com.epan.mobilesafe;

import android.app.Activity;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.epan.mobilesafe.db.dao.AddressDao;
import com.epan.mobilesafe.libs.StatusBarUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class AddressActivity extends Activity {

    @ViewInject(R.id.tv_address)
    private TextView tv_address;
    @ViewInject(R.id.et_phonenum)
    private EditText et_phonenum;
    private AddressDao dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBar();
        setContentView(R.layout.activity_address);
        dao = new AddressDao();
        ViewUtils.inject(this);

        et_phonenum.addTextChangedListener(new TextWatcher() {
            // 当文本变化完成时回调该方法
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                String string = s.toString();
                // 查询号码归属地
                String queryAddress = dao.queryAddress(getApplicationContext(),
                        string);
                if (!TextUtils.isEmpty(string)) {
                    tv_address.setText(queryAddress);
                }
            }

            // 文本变化之前调用该方法
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            // 文本变化之后调用该方法
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.colorPrimary));
    }

    public void query(View v) {
        String phonenum = et_phonenum.getText().toString().trim();
        if (!TextUtils.isEmpty(phonenum)) {
            String queryAddress = dao.queryAddress(getApplicationContext(),
                    phonenum);
            tv_address.setText(queryAddress);
        } else {
            // 抖动的动画
            Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
            et_phonenum.startAnimation(shake);
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(100);// 振动0.1秒
        }
    }
}
