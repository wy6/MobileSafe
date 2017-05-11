package com.epan.mobilesafe.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.epan.mobilesafe.R;


/**
 * Created by WY on 2017/5/1.
 */

public class SettingView extends RelativeLayout {

    private TextView tv_title;
    private TextView tv_des;
    private CheckBox cb;
    private String title;
    private String des_on;
    private String des_off;

    public SettingView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    // 在布局文件中使用
    public SettingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        //通过属性的命名空间和属性的名字获取属性值
        title = attrs.getAttributeValue(
                "http://schemas.android.com/apk/res-auto", "title");
        des_on = attrs.getAttributeValue(
                "http://schemas.android.com/apk/res-auto", "des_on");
        des_off = attrs.getAttributeValue(
                "http://schemas.android.com/apk/res-auto", "des_off");
        // 对控件初始化
        tv_title.setText(title);
        if (cb.isChecked()) {
            tv_des.setText(des_on);
        } else {
            tv_des.setText(des_off);
        }
    }

    // 在代码中使用
    public SettingView(Context context) {
        super(context);
        init();
    }

    private void init() {
        View view = View.inflate(getContext(), R.layout.setting_view, this);
        tv_title = (TextView) view.findViewById(R.id.tv_title);
        tv_des = (TextView) view.findViewById(R.id.tv_des);
        cb = (CheckBox) view.findViewById(R.id.cb);
    }

    public void setTile(String title) {
        tv_title.setText(title);
    }

    public void setDes(String des) {
        tv_des.setText(des);
    }

    public void setChecked(boolean isChecked) {
        cb.setChecked(isChecked);
        if (cb.isChecked()) {
            tv_des.setText(des_on);
        } else {
            tv_des.setText(des_off);
        }
    }

    public boolean isChecked() {
        return cb.isChecked();
    }
}

