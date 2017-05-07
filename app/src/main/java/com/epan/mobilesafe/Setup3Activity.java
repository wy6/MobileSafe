package com.epan.mobilesafe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;

import com.epan.mobilesafe.libs.StatusBarUtil;

public class Setup3Activity extends SetupBaseActivity {
    private EditText et_safenum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBar();
        setContentView(R.layout.activity_setup3);
        et_safenum = (EditText) findViewById(R.id.et_safenum);
        String string = sp.getString("safenum", "");
        et_safenum.setText(string);
    }

    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.colorPrimary));
    }

    @Override
    public void next_activity() {
        String safenum = et_safenum.getText().toString().trim();
        if (TextUtils.isEmpty(safenum)) {
            return;
        }

        SharedPreferences.Editor edit = sp.edit();
        edit.putString("safenum", safenum);
        edit.commit();


        Intent intent = new Intent(this, Setup4Activity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.tran_next_enter, R.anim.tran_next_exit);
    }

    @Override
    public void pre_activity() {
        Intent intent = new Intent(this, Setup2Activity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.tran_pre_enter, R.anim.tran_pre_exit);
    }

}

