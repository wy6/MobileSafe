package com.epan.mobilesafe.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.epan.mobilesafe.R;


/**
 * Created by WY on 2017/5/1.
 */

public class SettingClickView extends RelativeLayout {

    private TextView tv_title;
    private TextView tv_des;

    public SettingClickView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    // 在布局文件中使用
    public SettingClickView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    // 在代码中使用
    public SettingClickView(Context context) {
        super(context);
        init();
    }

    private void init() {
        View view = View.inflate(getContext(), R.layout.setting_click_view, this); // 孩子创建好了
        tv_title = (TextView) view.findViewById(R.id.tv_title);
        tv_des = (TextView) view.findViewById(R.id.tv_des);
    }

    // 暴漏一些方法 方便我们使用自定义组合控件

    public void setTile(String title) {
        tv_title.setText(title);
    }

    public void setDes(String des) {
        tv_des.setText(des);
    }

}


