package com.epan.mobilesafe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.epan.mobilesafe.libs.StatusBarUtil;

public class AToolsActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBar();
        setContentView(R.layout.activity_atools);
    }

    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.colorPrimary));
    }

    public void queryAddress(View v){
        Intent intent=new Intent(this, AddressActivity.class);
        startActivity(intent);
    }
}
