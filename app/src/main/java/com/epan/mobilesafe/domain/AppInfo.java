package com.epan.mobilesafe.domain;

import android.graphics.drawable.Drawable;

/**
 * Created by WY on 2017/5/14.
 */

public class AppInfo {
    private String name;
    private String versionName;
    private Drawable icon;
    private String packageName;
    private boolean isSD;
    private boolean isUser;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public boolean isSD() {
        return isSD;
    }

    public void setSD(boolean isSD) {
        this.isSD = isSD;
    }

    public boolean isUser() {
        return isUser;
    }

    public void setUser(boolean isUser) {
        this.isUser = isUser;
    }

    public AppInfo(String name, String versionName, Drawable icon,
                   String packageName, boolean isSD, boolean isUser) {
        super();
        this.name = name;
        this.versionName = versionName;
        this.icon = icon;
        this.packageName = packageName;
        this.isSD = isSD;
        this.isUser = isUser;
    }

    public AppInfo() {
        super();
    }

    @Override
    public String toString() {
        return "AppInfo [name=" + name + ", versionName=" + versionName
                + ", icon=" + icon + ", packageName=" + packageName + ", isSD="
                + isSD + ", isUser=" + isUser + "]";
    }


}
