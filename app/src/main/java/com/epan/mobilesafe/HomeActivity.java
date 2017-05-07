package com.epan.mobilesafe;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.epan.mobilesafe.libs.StatusBarUtil;
import com.epan.mobilesafe.untils.MD5Utils;

public class HomeActivity extends Activity {


    private GridView gv_home;
    private AlertDialog dialog;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBar();
        sp = getSharedPreferences("config", Context.MODE_PRIVATE);
        setContentView(R.layout.activity_home);
        gv_home = (GridView) findViewById(R.id.gv_home);
        gv_home.setAdapter(new HomeAdapter());
        gv_home.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        if (TextUtils.isEmpty(sp.getString("password", ""))) {
                            showPwSetupDialog();
                        } else {
                            showPwEnterDialog();
                        }
                        break;
                    case 8:
                        Intent intent = new Intent(HomeActivity.this, SettingActivity.class);
                        startActivity(intent);
                        break;
                }
            }
        });
    }

    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.colorMainBar));
    }

    private class HomeAdapter extends BaseAdapter {

        int[] imageId = {R.mipmap.safe, R.mipmap.call, R.mipmap.app,
                R.mipmap.progress, R.mipmap.net, R.mipmap.virus, R.mipmap.clean,
                R.mipmap.tools, R.mipmap.setting};

        String[] names = {"手机防盗", "通讯卫士", "软件管理", "进程管理",
                "流量统计", "手机杀毒", "缓存清理", "高级工具", "设置中心"};

        //显示条目的数量
        @Override
        public int getCount() {
            return 9;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        //显示每个条目的样式
        @Override
        public View getView(int i, View convertView, ViewGroup viewGroup) {
            View view = View.inflate(getApplicationContext(), R.layout.item_home, null);
            //必须用v.findViewById
            ImageView iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
            TextView tv_name = (TextView) view.findViewById(R.id.tv_name);
            iv_icon.setImageResource(imageId[i]);
            tv_name.setText(names[i]);
            return view;
        }
    }

    private void showPwSetupDialog() {
        AlertDialog.Builder builder = new Builder(this);
        View view = View.inflate(getApplicationContext(),
                R.layout.dialog_pw_setup, null);
        final EditText et_password = (EditText) view
                .findViewById(R.id.et_password);
        final EditText et_password_confirm = (EditText) view
                .findViewById(R.id.et_password_confirm);
        Button btn_ok = (Button) view.findViewById(R.id.bt_ok);
        Button btn_cancel = (Button) view.findViewById(R.id.bt_cancel);
        // 取消按钮的点击事件
        btn_cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btn_ok.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // 获取两个EditText输入的密码
                String password = et_password.getText().toString().trim();
                String password_confirm = et_password_confirm.getText()
                        .toString().trim();
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "密码不能为空",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if (password.equals(password_confirm)) {
                    // 把密码保存起来
                    SharedPreferences.Editor edit = sp.edit();
                    edit.putString("password", MD5Utils.digestPassword(password));
                    edit.commit();
                    Toast.makeText(getApplicationContext(), "密码设置成功",
                            Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                } else {
                    Toast.makeText(getApplicationContext(), "两次输入密码不一致",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialog = builder.create();
        dialog.setView(view, 0, 0, 0, 0);
        dialog.show();
    }

    int count = 0;
    int[] pw_img = {R.mipmap.pw_show, R.mipmap.pw_hide};

    private void showPwEnterDialog() {
        AlertDialog.Builder builder = new Builder(this);
        View view = View.inflate(getApplicationContext(),
                R.layout.dialog_pw_enter, null);
        final EditText et_password = (EditText) view
                .findViewById(R.id.et_password);
        Button btn_ok = (Button) view.findViewById(R.id.bt_ok);
        Button btn_cancel = (Button) view.findViewById(R.id.bt_cancel);
        final ImageView iv_show = (ImageView) view.findViewById(R.id.iv_show_password);
        iv_show.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (count % 2 == 0) {
                    //显示密码
                    iv_show.setImageResource(pw_img[0]);
                    et_password.setInputType(1);
                } else {
                    //隐藏密码
                    et_password.setInputType(129);
                    iv_show.setImageResource(pw_img[1]);
                }
                count++;
            }
        });
        // 取消按钮的点击事件
        btn_cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btn_ok.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String password = et_password.getText().toString().trim();
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "请输入密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                //  获取到了配置文件中的密码
                String password_sp = sp.getString("password", "");
                if (MD5Utils.digestPassword(password).equals(password_sp)) {
//                    Toast.makeText(getApplicationContext(), "输入密码正确", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    // 进入手机防盗界面
                    Intent intent = new Intent(getApplicationContext(), Setup1Activity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "输入密码不正确", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setView(view);
        dialog = builder.create();
        dialog.show();
    }
}
