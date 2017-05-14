package com.epan.mobilesafe.untils;

import android.os.Handler;

/**
 * Created by WY on 2017/5/14.
 */

public abstract class MyAsynckTask {
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            postTask();
        }

        ;
    };

    /**
     * 在子线程之前执行的代码
     */
    public abstract void preTask();

    /**
     * 在子线程之中执行的代码
     */
    public abstract void doInBack();

    /**
     * 在子线程之后执行的代码
     */
    public abstract void postTask();

    public void execute() {
        preTask();
        new Thread() {
            public void run() {
                doInBack();
                handler.sendEmptyMessage(0);
            }

            ;
        }.start();

    }
}
