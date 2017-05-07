package com.epan.mobilesafe;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.epan.mobilesafe.libs.StatusBarUtil;

public class LostFindActivity extends Activity {
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBar();
        sp = getSharedPreferences("config", MODE_PRIVATE);
        if (sp.getBoolean("first", true)) {
            // 跳转到下一个界面
            Intent intent = new Intent(this, Setup1Activity.class);
            startActivity(intent);
            finish();
        } else {

            setContentView(R.layout.activity_lost_find);

        }

    }

    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.colorPrimary));
    }

    public void resetup(View v) {
        Intent intent = new Intent(this, Setup1Activity.class);
        startActivity(intent);
    }
}

