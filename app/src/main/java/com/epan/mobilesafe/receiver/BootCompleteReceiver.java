package com.epan.mobilesafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;

/**
 * Created by WY on 2017/5/11.
 */

public class BootCompleteReceiver extends BroadcastReceiver {
    private SharedPreferences sp;

    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println("手机开机了....");
        sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        if (sp.getBoolean("protected", false)) {
            // 第一步 获取配置文件中 sim
            String sim = sp.getString("sim", "");
            // 第二步 读取当前的sim
            TelephonyManager manager = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            String simSerialNumber = manager.getSimSerialNumber();
            // 第三步 比较

            if (!sim.equals(simSerialNumber)) {
                // 获取短信的管理者 用来 发送短信
                // 4.4 以后 不能这么发短信了
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(sp.getString("safenum", "13027724321"),
                        null, "sos,防盗报警!!!", null, null);
            }
        }
    }

}
