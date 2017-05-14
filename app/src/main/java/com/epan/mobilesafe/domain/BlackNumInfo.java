package com.epan.mobilesafe.domain;

/**
 * Created by WY on 2017/5/14.
 */

public class BlackNumInfo {
    // 黑名单号码
    private String blackNum;
    // 拦截模式 0 1 2
    private int mode;

    public String getBlackNum() {
        return blackNum;
    }

    public void setBlackNum(String blackNum) {
        this.blackNum = blackNum;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        if (mode >= 0 && mode <= 2) {
            this.mode = mode;
        } else {
            this.mode = 0;
        }
    }

    public BlackNumInfo(String blackNum, int mode) {
        super();
        this.blackNum = blackNum;
        if (mode >= 0 && mode <= 2) {
            this.mode = mode;
        } else {
            this.mode = 0;
        }
    }

    @Override
    public String toString() {
        return "BlackNumInfo [blackNum=" + blackNum + ", mode=" + mode + "]";
    }

    public BlackNumInfo() {
        super();
    }

}
