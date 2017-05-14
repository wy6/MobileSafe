package com.epan.mobilesafe.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;

import com.android.internal.telephony.ITelephony;
import com.epan.mobilesafe.db.dao.BlackNumDao;

import java.lang.reflect.Method;

/**
 * Created by WY on 2017/5/14.
 */

public class BlackNumService extends Service {
    private SmsReceiver receiver;
    private BlackNumDao dao;
    private MyPhoneStateListener listener;
    private TelephonyManager manager;

    private class SmsReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            System.out.println("通过代码注册的广播接受者 收到了短信");
            Object[] objs = (Object[]) intent.getExtras().get("pdus");
            for (Object obj : objs) {
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) obj);
                String body = smsMessage.getMessageBody();// 短信正文内容
                String sender = smsMessage.getOriginatingAddress();// 发件人
                if (dao.queryBlackNumMode(sender) == BlackNumDao.SMS
                        || dao.queryBlackNumMode(sender) == BlackNumDao.ALL) {
                    abortBroadcast();
                }
                // 智能拦截    关键字的数据库   分词技术
            }
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        dao = new BlackNumDao(this);
        receiver = new SmsReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(Integer.MAX_VALUE);
        filter.addAction("android.provider.Telephony.SMS_RECEIVED");
        registerReceiver(receiver, filter);

        manager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        listener = new MyPhoneStateListener();
        manager.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
    }

    private class MyPhoneStateListener extends PhoneStateListener {
        @Override
        public void onCallStateChanged(int state, final String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            switch (state) {
                case TelephonyManager.CALL_STATE_RINGING: // 响铃
                    if (dao.queryBlackNumMode(incomingNumber) == BlackNumDao.TEL
                            || dao.queryBlackNumMode(incomingNumber) == BlackNumDao.ALL) {
                        endCall();
                        //  过一段时间 生成通话记录
                        // 内容观察者
                        final Uri url = Uri.parse("content://call_log/calls");
                        final ContentResolver resolver = getContentResolver();
                        // 注册内容观察者 观察内容提供者数据的变化 如果发生变化
                        resolver.registerContentObserver(url, true, new ContentObserver(new Handler()) {

                            @Override
                            public void onChange(boolean selfChange) {
                                super.onChange(selfChange);
                                //  删除通话记录
                                resolver.delete(url, "number=?", new String[]{incomingNumber});
                                resolver.unregisterContentObserver(this);// 反注册内容观察者
                            }

                        });
                    }
                    break;
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void endCall() {
        System.out.println("挂断电话...");
        // 步骤3 运行方法
        IBinder invoke;
        try {
            //  步骤1 获取类的加载器加载一个类 拿到ServiceManager 字节码
            Class<?> loadClass = BlackNumService.class.getClassLoader()
                    .loadClass("android.os.ServiceManager");
            // 步骤2 找到方法
            Method declaredMethod = loadClass.getDeclaredMethod("getService",
                    String.class);
            //步骤3 调用方法
            invoke = (IBinder) declaredMethod.invoke(null,
                    Context.TELEPHONY_SERVICE);
            // 步骤4 获取真正的代理对象
            ITelephony asInterface = ITelephony.Stub.asInterface(invoke);
            asInterface.endCall();// 挂断电话
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (receiver != null) {
            unregisterReceiver(receiver);
        }
        manager.listen(listener, PhoneStateListener.LISTEN_NONE);// 什么都不监听了
    }
}
