package com.epan.mobilesafe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.epan.mobilesafe.libs.StatusBarUtil;
import com.epan.mobilesafe.ui.SettingView;

public class Setup2Activity extends SetupBaseActivity {
    private SettingView sv_bind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBar();
        setContentView(R.layout.activity_setup2);

        sv_bind = (SettingView) findViewById(R.id.sv_bind);

        String string = sp.getString("sim", "");
        if (TextUtils.isEmpty(string)) {
            sv_bind.setChecked(false);
        } else {
            sv_bind.setChecked(true);
        }

        sv_bind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor edit = sp.edit();
                if (!sv_bind.isChecked()) {
                    // 电话的管理者 获取所有的电话信息
                    TelephonyManager manager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
                    // String line1Number = manager.getLine1Number();//获取到电话号码
                    // 获取到sim卡的串号 唯一存在
                    String simSerialNumber = manager.getSimSerialNumber();
                    // 读取sim串号 保存到配置文件中
                    edit.putString("sim", simSerialNumber);
                    sv_bind.setChecked(true);
                    Toast.makeText(Setup2Activity.this,simSerialNumber,Toast.LENGTH_SHORT).show();
                } else {
                    // 解绑
                    edit.putString("sim", "");

                    sv_bind.setChecked(false);
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
        if (sv_bind.isChecked()) {

            Intent intent = new Intent(this, Setup3Activity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.tran_next_enter,
                    R.anim.tran_next_exit);
        } else {
            Toast.makeText(getApplicationContext(), "请绑定SIM卡", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void pre_activity() {
        Intent intent = new Intent(this, Setup1Activity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.tran_pre_enter, R.anim.tran_pre_exit);
    }

}

