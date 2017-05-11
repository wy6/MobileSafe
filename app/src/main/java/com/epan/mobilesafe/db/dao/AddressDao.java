package com.epan.mobilesafe.db.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;

/**
 * Created by WY on 2017/5/11.
 */

public class AddressDao {
    public String queryAddress(Context context, String num) {
        File file = new File(context.getFilesDir(), "address.db");
        String location = "";
        // 打开数据库
        // "file:///android_asset/address.db"
        // "^1[34578]\d{9}$"  正则表达
        SQLiteDatabase sql = SQLiteDatabase.openDatabase(
                file.getAbsolutePath(), null, SQLiteDatabase.OPEN_READONLY);
        if (num.matches("^1[34578]\\d{9}$")) {
            Cursor cursor = sql
                    .rawQuery(
                            "select location from data2 where id=(select outkey from data1 where id=?);",
                            new String[]{num.substring(0, 7)});
            if (cursor.moveToNext()) {
                location = cursor.getString(0);
            }
            cursor.close();

        } else {
            switch (num.length()) {
                case 3:   //110  119  120  999    911
                    switch (num) {
                        case "110":
                            location = "报警电话";
                            break;
                        case "119":
                            location = "火警电话";
                            break;
                        case "120":
                            location = "急救电话";
                            break;
                        case "999":
                            location = "急救电话";
                            break;
                        default:
                            location = "特殊电话";
                            break;
                    }
                    break;
                case 4:
                    location = "虚拟电话";
                    break;
                case 5: // 10086  10010  10000
                    switch (num) {
                        case "10086":
                            location = "移动客服";
                            break;
                        case "10010":
                            location = "联通客服";
                            break;
                        case "10000":
                            location = "电信客服";
                            break;
                        default:
                            location = "客服电话";
                            break;
                    }
                    break;
                case 7:  //本地电话
                case 8: //本地电话
                    location = "本地电话";
                    break;
                default:
                    if (num.length() >= 10 && num.startsWith("0")) {
                        // 如果区号是3位的时候
                        String result = num.substring(1, 3);// 3位区号
                        Cursor c = sql.rawQuery("select location from data2 where area=?", new String[]{result});
                        if (c.moveToNext()) {
                            String str = c.getString(0);
                            location = str.substring(0, str.length() - 2);
                            c.close();

                        } else {
                            result = num.substring(1, 4);//  4位区号的情况
                            c = sql.rawQuery("select location from data2 where area=?", new String[]{result});
                            if (c.moveToNext()) {
                                String str = c.getString(0);
                                location = str.substring(0, str.length() - 2);
                                c.close();
                            }
                        }
                    }
                    break;
            }
        }
        sql.close();
        return location;
    }
}
