package com.epan.mobilesafe;

import android.content.Intent;
import android.os.Bundle;

import com.epan.mobilesafe.libs.StatusBarUtil;

public class Setup1Activity extends SetupBaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBar();
        setContentView(R.layout.activity_setup1);
    }

    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.colorPrimary));
    }

    @Override
    public void next_activity() {
        Intent intent = new Intent(this, Setup2Activity.class);
        startActivity(intent);
        finish();
        // 参数1  新的activity进入的动画
        // 参数2  旧的activity移出的动画
        overridePendingTransition(R.anim.tran_next_enter, R.anim.tran_next_exit);
    }

    @Override
    public void pre_activity() {
        finish();
    }
}

