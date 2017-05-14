package com.epan.mobilesafe.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by WY on 2017/5/14.
 */

public class BlackNumOpenHelper extends SQLiteOpenHelper {
    public static final String TABLE = "info";

    public BlackNumOpenHelper(Context context) {
        // 2 参数 数据库的名字 4 数据库的版本
        super(context, "blacknum.db", null, 1);
    }

    // 数据库创建的时候会调用 一般在这个方法里 实现表结构的创建
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE + "(id integer primary key autoincrement,blacknum varchar(20),mode varchar(2));");

    }

    // 当数据库更新的时候调用
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
