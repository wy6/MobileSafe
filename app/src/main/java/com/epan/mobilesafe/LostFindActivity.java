package com.epan.mobilesafe;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.epan.mobilesafe.libs.StatusBarUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class LostFindActivity extends Activity {
    private SharedPreferences sp;
    @ViewInject(R.id.iv_protected)
    private ImageView iv_protected;
    @ViewInject(R.id.tv_safenum)
    private TextView tv_safenum;

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
            ViewUtils.inject(this);
        }

    }

    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.colorPrimary));
    }

    //当前界面可见的时候调用
    @Override
    protected void onStart() {
        super.onStart();
        tv_safenum.setText(sp.getString("safenum", ""));

        boolean protect = sp.getBoolean("protected", false);
        if(protect){
            iv_protected.setImageResource(R.mipmap.lock);
        }else{
            iv_protected.setImageResource(R.mipmap.unlock);
        }
    }

    public void resetup(View v) {
        Intent intent = new Intent(this, Setup1Activity.class);
        startActivity(intent);
    }
}

