package com.epan.mobilesafe;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by WY on 2017/5/6.
 */

public abstract class SetupBaseActivity extends Activity {

    protected SharedPreferences sp;
    //1 定义 手势识别器
    private GestureDetector detector;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = getSharedPreferences("config", MODE_PRIVATE);
        // 2 初始化
        detector=new GestureDetector(this,new MySimpleOnGestureListener());

    }
    // 步骤3
    private  class MySimpleOnGestureListener extends GestureDetector.SimpleOnGestureListener {
        // ViewPager
        // onFling( 按下的始位， 抬起的末位， X轴速率， Y轴速率 )
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                               float velocityY) {
            float startY=e1.getRawY();
            float endY=e2.getRawY();
            if(Math.abs(startY-endY)>80){
                return true;
            }
            float startX = e1.getRawX();
            float endX = e2.getRawX();
            if((startX-endX)>100){
                next_activity();
            }else if((endX-startX)>100){
                pre_activity();
            }
            //true if the event is consumed, else false     如果true 事件执行掉了
            return true;
        }

    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 步骤4
        // 把手势识别注册到屏幕的触摸事件上
        detector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    public void pre(View v){
        pre_activity();

    }
    public void next(View v){
        next_activity();

    }

    public  abstract void next_activity();
    public  abstract void pre_activity();
    // keyCode 点击的是哪个按键
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            pre_activity();
            // 屏蔽返回按钮
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

/*	@Override
	public void onBackPressed() {
		//super.onBackPressed();
		//  返回按钮的点击事件
		pre_activity();
	}
	*/
}
