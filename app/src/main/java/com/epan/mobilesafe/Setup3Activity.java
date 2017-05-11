package com.epan.mobilesafe;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.epan.mobilesafe.libs.StatusBarUtil;

public class Setup3Activity extends SetupBaseActivity {
    private EditText et_safenum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBar();
        setContentView(R.layout.activity_setup3);
        et_safenum = (EditText) findViewById(R.id.et_safenum);
        String string = sp.getString("safenum", "");
        et_safenum.setText(string);
    }

    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.colorPrimary));
    }

    public void selectContacts(View v) {
//		Intent intent = new Intent(this, ContactsActivity.class);
//		startActivityForResult(intent, 0);

        // 打开系统选择联系人界面
        Intent intent = new Intent();
        intent.setAction("android.intent.action.PICK");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setType("vnd.android.cursor.dir/phone_v2");
        startActivityForResult(intent, 1);
    }

    // 当打开的activity 关闭的时候 就会调用这个方法
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//		if (data != null) {
//			String num = data.getStringExtra("num"); // null.方法
//			et_safenum.setText(num);
//		}
        if (data != null) {
            Uri uri = data.getData();
            String num = null;
            // 创建内容解析者
            ContentResolver contentResolver = getContentResolver();
            Cursor cursor = contentResolver.query(uri,
                    null, null, null, null);
            while (cursor.moveToNext()) {
                num = cursor.getString(cursor.getColumnIndex("data1"));

            }
            cursor.close();
            num = num.replaceAll("-", "");
            et_safenum.setText(num);
        }
    }

    @Override
    public void next_activity() {
        String safenum = et_safenum.getText().toString().trim();
        if (TextUtils.isEmpty(safenum)) {
            return;
        }

        SharedPreferences.Editor edit = sp.edit();
        edit.putString("safenum", safenum);
        edit.commit();


        Intent intent = new Intent(this, Setup4Activity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.tran_next_enter, R.anim.tran_next_exit);
    }

    @Override
    public void pre_activity() {
        Intent intent = new Intent(this, Setup2Activity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.tran_pre_enter, R.anim.tran_pre_exit);
    }

}

