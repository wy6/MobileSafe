package com.epan.mobilesafe;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.epan.mobilesafe.db.dao.AddressDao;
import com.epan.mobilesafe.libs.StatusBarUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class AddressActivity extends Activity {

    @ViewInject(R.id.tv_address)
    private TextView tv_address;
    @ViewInject(R.id.et_phonenum)
    private EditText et_phonenum;
    private AddressDao dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBar();
        setContentView(R.layout.activity_address);
        dao = new AddressDao();
        ViewUtils.inject(this);
    }

    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.colorPrimary));
    }

    public void query(View v){
        String phonenum = et_phonenum.getText().toString().trim();
        String queryAddress = dao.queryAddress(getApplicationContext(), phonenum);
        tv_address.setText(queryAddress);
    }
}
