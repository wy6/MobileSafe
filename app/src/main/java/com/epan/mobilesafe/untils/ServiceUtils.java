package com.epan.mobilesafe.untils;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;

import java.util.List;

/**
 * Created by WY on 2017/5/13.
 */

public class ServiceUtils {
    /**
     * 动态判断服务有没有运行
     *
     * @param className
     * @param context
     * @return
     */
    public  static  boolean  isServiceRunning(String className,Context context){
        //创建了一个活动的管理者
        ActivityManager  am=(ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningServiceInfo> runningServices = am.getRunningServices(1000);//  获取到正在运行的服务 最多返回多少个
        for(RunningServiceInfo info:runningServices){
            String className2 = info.service.getClassName();
            if(className2.equals(className)){
                return true;
            }
        }
        return false;
    }
    //ram   断电丢失     内存
    // rom 断电不丢失的   硬盘
}
