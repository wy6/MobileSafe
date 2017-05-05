package com.epan.mobilesafe.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by WY on 2017/4/19.
 */

@SuppressLint("AppCompatCustomView")
public class FocusedTextView extends TextView {
    //在代码中创建控件
    public FocusedTextView(Context context) {
        super(context);
    }

    //在布局文件中创建控件
    public FocusedTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FocusedTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public boolean isFocused(){
        return true;
    }

}
