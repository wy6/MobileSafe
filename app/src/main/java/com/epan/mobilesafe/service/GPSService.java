package com.epan.mobilesafe.service;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;

import java.util.List;

/**
 * GPS定位服务
 * Created by WY on 2017/5/11.
 */

public class GPSService extends Service {
    private LocationManager manager;
    private MyLocationListener myLocationListener;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    // 当服务被开启的时候调用
    public void onCreate() {
        // 初始化了位置的管理者
        manager = (LocationManager) getSystemService(LOCATION_SERVICE);
        // 获取到了所有的定位方式  参数 true
        List<String> providers = manager.getProviders(false);
        for (String str : providers) {
            System.out.println(str);
        }
        myLocationListener = new MyLocationListener();
        //定位配置
        Criteria criteria = new Criteria();
        criteria.setAltitudeRequired(true);
        String bestProvider = manager.getBestProvider(criteria, true);
        // 参数1 定位的方式  参数2 定位的间隔时间  3 最小间隔距离
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        manager.requestLocationUpdates(bestProvider, 0, 0, myLocationListener);

    }

    private class MyLocationListener implements LocationListener {
        //  当位置发生变化的时候调用  形参 具体的位置
        @Override
        public void onLocationChanged(Location location) {
            double latitude = location.getLatitude();//  平行于赤道的  维度
            double longitude = location.getLongitude();//  垂直于赤道 经度

            SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
            SharedPreferences.Editor edit = sp.edit();
            edit.putString("latitude", latitude + "");
            edit.putString("longitude", longitude + "");
            edit.commit();

        }

        // 当定位方式状态发生变化的时候会调用     不可用 ->可用   可用->不可用
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        // 当定位可用的时候调用
        @Override
        public void onProviderEnabled(String provider) {

        }

        // 当定位不可用的时候的调用
        @Override
        public void onProviderDisabled(String provider) {

        }

    }
}
