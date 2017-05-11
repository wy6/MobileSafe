package com.epan.mobilesafe;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.epan.mobilesafe.libs.StatusBarUtil;
import com.epan.mobilesafe.ui.SettingView;

public class SettingActivity extends Activity {

    private SettingView sv_update;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBar();
        setContentView(R.layout.activity_setting);
        //getSharedPreferences( 配置文件名，文件访问模式(读取权限) );
        sp = getSharedPreferences("config", Context.MODE_PRIVATE);
        sv_update = (SettingView) findViewById(R.id.sv_update);
        intview();
    }

    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.colorPrimary));
    }

    private void intview() {
        // 控件的初始化
        if (sp.getBoolean("update", true)) {
            //sv_update.setDes("自动更新开启");
            sv_update.setChecked(true);
        } else {
            //sv_update.setDes("自动更新关闭");
            sv_update.setChecked(false);
        }

        sv_update.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                SharedPreferences.Editor edit = sp.edit();
                if (sv_update.isChecked()) {
                    sv_update.setChecked(false);
                    //sv_update.setDes("自动更新关闭");
                    edit.putBoolean("update", false);
                    // edit.apply(); //API9以后可以使用
                } else {
                    sv_update.setChecked(true);
                    //sv_update.setDes("自动更新开启");
                    edit.putBoolean("update", true);
                }
                edit.commit();
            }
        });
    }
}
