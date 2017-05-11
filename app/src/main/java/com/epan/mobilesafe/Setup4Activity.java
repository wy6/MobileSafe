package com.epan.mobilesafe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.epan.mobilesafe.libs.StatusBarUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;


public class Setup4Activity extends SetupBaseActivity {
    private SharedPreferences sp;
    @ViewInject(R.id.cb_protected)
    private CheckBox cb_protected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBar();
        sp = getSharedPreferences("config", MODE_PRIVATE);
        setContentView(R.layout.activity_setup4);
        ViewUtils.inject(this);

        if (sp.getBoolean("protected", false)) {
            cb_protected.setChecked(true);
            cb_protected.setText("您开启了手机防盗保护");
        } else {
            cb_protected.setChecked(false);
            cb_protected.setText("您没有开启手机防盗保护");
        }

        cb_protected.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            //当选中状态被改变的时候调用
            // 参数1  代表当前checkbox
            // 参数2 修改后的选中的状态
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                SharedPreferences.Editor edit = sp.edit();
                if (isChecked) {
                    cb_protected.setText("您开启了手机防盗保护");
                    edit.putBoolean("protected", true);
                } else {
                    cb_protected.setText("您没有开启手机防盗保护");
                    edit.putBoolean("protected", false);
                }
                edit.commit();
            }
        });
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

