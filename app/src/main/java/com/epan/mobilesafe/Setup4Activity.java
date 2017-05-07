package com.epan.mobilesafe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.epan.mobilesafe.libs.StatusBarUtil;

public class Setup4Activity extends SetupBaseActivity {
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBar();
        sp = getSharedPreferences("config", MODE_PRIVATE);
        setContentView(R.layout.activity_setup4);
    }

    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.colorPrimary));
    }

    @Override
    public void next_activity() {
        SharedPreferences.Editor edit = sp.edit();
        edit.putBoolean("first", false);
        edit.commit();  // 走完了设置向导了
        Intent intent = new Intent(this, LostFindActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void pre_activity() {
        Intent intent = new Intent(this, Setup3Activity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.tran_pre_enter, R.anim.tran_pre_exit);
    }

}

