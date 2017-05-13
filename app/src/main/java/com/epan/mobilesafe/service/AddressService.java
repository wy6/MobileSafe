package com.epan.mobilesafe.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.epan.mobilesafe.R;
import com.epan.mobilesafe.db.dao.AddressDao;

/**
 * Created by WY on 2017/5/13.
 */

public class AddressService extends Service {
    private TelephonyManager manager;
    private AddressDao dao;
    private OutGoingCallReceiver callReceiver;
    private SharedPreferences sp;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private class OutGoingCallReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            System.out.println("广播接受者");
            String resultData = getResultData();
            System.out.println(resultData);
            String queryAddress = dao.queryAddress(getApplicationContext(),
                    resultData);
            showMyToast(queryAddress);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sp = getSharedPreferences("config", MODE_PRIVATE);
        dao = new AddressDao();
        // 通过代码注册广播接收者
        callReceiver = new OutGoingCallReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.NEW_OUTGOING_CALL");
        registerReceiver(callReceiver, filter);// 在代码中注册广播接受者

        manager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        myPhoneStateListener = new MyPhoneStateListener();
        manager.listen(myPhoneStateListener,
                PhoneStateListener.LISTEN_CALL_STATE);

    }

    private class MyPhoneStateListener extends PhoneStateListener {
        // 如果监听电话状态 会在该方法回调
        // incomingNumber 来电号码
        // state 电话状态
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            switch (state) {
                case TelephonyManager.CALL_STATE_IDLE: // 电话空闲的状态
                    hideToast();
                    break;
                case TelephonyManager.CALL_STATE_RINGING:// 响铃状态
                    String queryAddress = dao.queryAddress(getApplicationContext(),
                            incomingNumber);
                    // Toast.makeText(getApplicationContext(), queryAddress,
                    // 0).show();
                    showMyToast(queryAddress);
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK: // 通话的状态

                    break;
                default:
                    break;
            }
        }
    }

    private WindowManager wm;
    private View view;
    private WindowManager.LayoutParams params;
    private MyPhoneStateListener myPhoneStateListener;

    /**
     * 自定义Toast
     *
     * @param address 归属地
     */
    public void showMyToast(String address) {
        int[] bgcolor = new int[]{R.drawable.call_locate_white,
                R.drawable.call_locate_orange, R.drawable.call_locate_blue,
                R.drawable.call_locate_gray, R.drawable.call_locate_green};

        wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        view = View.inflate(getApplicationContext(), R.layout.toast_custom,
                null);
        view.setBackgroundResource(bgcolor[sp.getInt("which", 0)]); // 修改号码归属地的背景
        TextView tv_address = (TextView) view.findViewById(R.id.tv_address);
        tv_address.setText(address);

        touch();
        params = new WindowManager.LayoutParams();
        params.height = WindowManager.LayoutParams.WRAP_CONTENT; // 高
        params.width = WindowManager.LayoutParams.WRAP_CONTENT; // 宽
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE // 不获取焦点
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON; // 保持屏幕常亮
        params.format = PixelFormat.TRANSLUCENT; // 半透明
        params.type = WindowManager.LayoutParams.TYPE_PRIORITY_PHONE; // 高优先级的弹出框
        //
        params.gravity = Gravity.LEFT | Gravity.TOP; // 左上角对齐
        params.x = sp.getInt("x", 100);// 如果上面Gravity.LEFT 代表距离左面的距离 ,
        // 如果上面Gravity.Rgiht 代表距离右面的距离
        params.y = sp.getInt("y", 100);
        ;// Gravity.TOP 距离上面的距离 Gravity.bottom 距离下面的距离
        wm.addView(view, params); // 显示
    }

    private void touch() {
        view.setOnTouchListener(new View.OnTouchListener() {

            private int startX;
            private int startY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: // 手指按下的事件
                        // 步骤1
                        startX = (int) event.getRawX();
                        startY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:// 手指移动的事件
                        // 步骤2 记录一个新的位置
                        int newX = (int) event.getRawX();
                        int newY = (int) event.getRawY();
                        // 步骤3 计算移动的偏移量
                        int dX = newX - startX;
                        int dY = newY - startY;
                        // 步骤 4 让控件移动偏移量 重新分配位置
                        params.x += dX;
                        params.y += dY;
                        wm.updateViewLayout(view, params);
                        // 步骤5 更换开始的位置
                        startX = newX;
                        startY = newY;

                        break;
                    case MotionEvent.ACTION_UP:// 手指抬起的事件
                        SharedPreferences.Editor edit = sp.edit();
                        edit.putInt("x", params.x);
                        edit.putInt("y", params.y);
                        edit.commit();
                        break;
                    default:
                        break;
                }
                // True if the listener has consumed the event, false otherwise
                // true 事件被处理 false 没有被处理
                return true;
            }
        });
    }

    public void hideToast() {
        if (wm != null && view != null) {
            wm.removeView(view);
            wm = null;
            view = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (callReceiver != null) {
            unregisterReceiver(callReceiver);// 反注册广播接受者
            callReceiver = null;
        }
        manager.listen(myPhoneStateListener, PhoneStateListener.LISTEN_NONE);// 取消监听
    }
}
