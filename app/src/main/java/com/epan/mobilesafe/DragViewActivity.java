package com.epan.mobilesafe;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class DragViewActivity extends Activity {
    private LinearLayout ll_toast;
    private SharedPreferences sp;
    private int widthPixels;
    private int heightPixels;
    @ViewInject(R.id.tv_bottom)
    private TextView tv_bottom;
    @ViewInject(R.id.tv_top)
    private TextView tv_top;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = getSharedPreferences("config", MODE_PRIVATE);
        setContentView(R.layout.activity_dragview);

        ll_toast = (LinearLayout) findViewById(R.id.ll_toast);
        ViewUtils.inject(this);
        //回显位置
        int x = sp.getInt("x", 0);
        int y = sp.getInt("y", 0);
        System.out.println("x:" + x + "..Y:" + y);
        //  返回值 取决于控件所在的父容器
        RelativeLayout.LayoutParams params = (LayoutParams) ll_toast.getLayoutParams();  // 得到了控件在布局里的参数
        params.leftMargin = x;
        params.topMargin = y;
        ll_toast.setLayoutParams(params);//  把修改后的布局参数 设置给控件  在测量之前调用的

        // 获取屏幕的宽和高
        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();// 白纸
        wm.getDefaultDisplay().getMetrics(outMetrics);// 测量   在白纸上写了宽和高
        widthPixels = outMetrics.widthPixels;
        heightPixels = outMetrics.heightPixels;


        touch();
        click();
    }

    /**
     * 双击事件处理
     */
    long[] mHits = new long[2];
    private void click() {
        ll_toast.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
                mHits[mHits.length - 1] = SystemClock.uptimeMillis();  // 距离开机的时间
                if (mHits[0] >= (SystemClock.uptimeMillis() - 500)) {
                    int l = (widthPixels - ll_toast.getWidth()) / 2;
                    int t = (heightPixels - 25 - ll_toast.getHeight()) / 2;
                    ll_toast.layout(l, t, l + ll_toast.getWidth(), t + ll_toast.getHeight());
                    SharedPreferences.Editor edit = sp.edit();
                    edit.putInt("x", l);
                    edit.putInt("y", t);
                    edit.commit();
                }

            }
        });
    }

    //  注册触摸事件
    public void touch() {
        ll_toast.setOnTouchListener(new View.OnTouchListener() {

            private int startX;
            private int startY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:  // 手指按下的事件
                        //步骤1
                        startX = (int) event.getRawX();
                        startY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE://  手指移动的事件
                        // 步骤2  记录一个新的位置
                        int newX = (int) event.getRawX();
                        int newY = (int) event.getRawY();
                        // 步骤3   计算移动的偏移量
                        int dX = newX - startX;
                        int dY = newY - startY;
                        // 步骤 4  让控件移动偏移量  重新分配位置
                        int l = ll_toast.getLeft();
                        int t = ll_toast.getTop();
                        l = l + dX;
                        t = t + dY;
                        int r = l + ll_toast.getWidth();
                        int b = t + ll_toast.getHeight();
                        if (l < 0 || r > widthPixels || t < 0 || b > heightPixels - 25) {
                            break;
                        }
                        ll_toast.layout(l, t, r, b);
                        int top = ll_toast.getTop();
                        if (top > heightPixels / 2) {
                            tv_top.setVisibility(View.VISIBLE);
                            tv_bottom.setVisibility(View.INVISIBLE);
                        } else {
                            tv_top.setVisibility(View.INVISIBLE);
                            tv_bottom.setVisibility(View.VISIBLE);
                        }
                        // 步骤5  更换开始的位置
                        startX = newX;
                        startY = newY;

                        break;
                    case MotionEvent.ACTION_UP://  手指抬起的事件
                        SharedPreferences.Editor edit = sp.edit();
                        edit.putInt("x", ll_toast.getLeft());
                        edit.putInt("y", ll_toast.getTop());
                        edit.commit();
                        break;
                    default:
                        break;
                }
                //True if the listener has consumed the event, false otherwise
                // true的时候 把事件给处理掉了  false 没有处理掉
                return false;
            }
        });
    }
}
