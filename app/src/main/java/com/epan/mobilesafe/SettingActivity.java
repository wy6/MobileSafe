package com.epan.mobilesafe;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.epan.mobilesafe.libs.StatusBarUtil;
import com.epan.mobilesafe.service.AddressService;
import com.epan.mobilesafe.ui.SettingClickView;
import com.epan.mobilesafe.ui.SettingView;
import com.epan.mobilesafe.untils.ServiceUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class SettingActivity extends Activity {

    @ViewInject(R.id.sv_update)
    private SettingView sv_update;
    @ViewInject(R.id.sv_address)
    private SettingView sv_address;
    @ViewInject(R.id.scv_location_bg)
    private SettingClickView scv_location_bg;
    @ViewInject(R.id.scv_change_location)
    private SettingClickView scv_change_location;

    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBar();
        setContentView(R.layout.activity_setting);
        //getSharedPreferences( 配置文件名，文件访问模式(读取权限) );
        sp = getSharedPreferences("config", Context.MODE_PRIVATE);
        ViewUtils.inject(this);
        update();

        changeBg();

        changeLoaction();
    }

    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.colorPrimary));
    }

    private void changeLoaction() {
        scv_change_location.setTile("归属地提示框位置");
        scv_change_location.setDes("设置归属地提示框显示位置");

        scv_change_location.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), DragViewActivity.class);
                startActivity(intent);
            }
        });
    }

    private void changeBg() {
        final String[] items = {"半透明", "活力橙", "卫士蓝", "金属灰", "苹果绿"};

        scv_location_bg.setTile("号码归属地提示框风格");
        scv_location_bg.setDes(items[sp.getInt("which", 0)]);
        scv_location_bg.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
                builder.setIcon(R.drawable.ic_launcher);
                builder.setTitle("归属地提示框风格");
                // 参数1  字符数组 显示的内容  参数 2 默认选中的条目
                builder.setSingleChoiceItems(items, sp.getInt("which", 0), new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences.Editor edit = sp.edit();
                        edit.putInt("which", which);
                        edit.commit();
                        dialog.dismiss();
                        scv_location_bg.setDes(items[sp.getInt("which", 0)]);
                    }
                });
                builder.setNegativeButton("取消", null);
                builder.show();
            }

        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        address();
    }

    private void address() {
        // 动态判断服务有没有开启   不应该保存到配置文件中
        if (ServiceUtils.isServiceRunning("com.epan.mobilesafe.service.AddressService", this)) {
            sv_address.setChecked(true);
        } else {
            sv_address.setChecked(false);
        }

        sv_address.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddressService.class);
                if (sv_address.isChecked()) {
                    // 停止服务
                    stopService(intent);
                    sv_address.setChecked(false);
                } else {
                    //  开启服务
                    startService(intent);
                    sv_address.setChecked(true);
                }
            }
        });
    }

    public void update() {
        //sv_update.setTile("自动更新");
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
                    // edit.apply();
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
