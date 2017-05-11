package com.epan.mobilesafe.receiver;

import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.text.TextUtils;

import com.epan.mobilesafe.R;
import com.epan.mobilesafe.service.GPSService;

/**
 * 短信的广播接受者
 * Created by WY on 2017/5/11.
 */

public class SmsReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences sp = context.getSharedPreferences("config",
                Context.MODE_PRIVATE);
        // 初始化设备方案管理者
        DevicePolicyManager manager = (DevicePolicyManager) context
                .getSystemService(Context.DEVICE_POLICY_SERVICE);

        // 70汉字 一条短信 71个汉字
        Object[] objs = (Object[]) intent.getExtras().get("pdus");
        for (Object obj : objs) {
            SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) obj);
            String body = smsMessage.getMessageBody();// 短信正文内容
            String sender = smsMessage.getOriginatingAddress();// 发件人
            boolean who = sender == sp.getString("safenum", "") ? true : false;
            System.out.println(body + "..." + sender);
            // abortBroadcast(); // 中断短信
            if ("#*location*#".equals(body) && who) {
                System.out.println("定位");
                Intent intent2 = new Intent(context, GPSService.class);
                context.startService(intent2);

                String latitude = sp.getString("latitude", "");
                String longitude = sp.getString("longitude", "");
                if (!TextUtils.isEmpty(latitude)
                        && !TextUtils.isEmpty(longitude)) {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(sp.getString("safenum", "13027724321"),
                            null, "latitude:" + latitude + "....longtitude:"
                                    + longitude, null, null);
                }
                abortBroadcast();
            } else if ("#*alarm*#".equals(body)) {
                System.out.println("播放报警音乐");

                AudioManager audioManager = (AudioManager) context
                        .getSystemService(Context.AUDIO_SERVICE);
                int max = audioManager
                        .getStreamMaxVolume(AudioManager.STREAM_MUSIC);
                // 参数1 声音的类型 参数2 声音的大小
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, max, 0);
                // 创建了一个音乐资源
                MediaPlayer mediaPlayer = MediaPlayer.create(context,
                        R.raw.ylzs);
                mediaPlayer.start();

                abortBroadcast();
            } else if ("#*wipe*#".equals(body) && who) {
                System.out.println("远程擦除数据");
                ComponentName componentName = new ComponentName(context,
                        Admin.class);
                if (manager.isAdminActive(componentName)) {
                    manager.wipeData(0); // 把数据清空了
                }
                abortBroadcast();
            } else if ("#*lockScreen*#".equals(body) && who) {
                System.out.println("远程锁屏");
                ComponentName componentName = new ComponentName(context,
                        Admin.class);
                if (manager.isAdminActive(componentName)) {
                    manager.lockNow();// 锁屏了
                }
                abortBroadcast();
            }
        }
    }

}
