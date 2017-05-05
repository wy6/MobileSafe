package com.epan.mobilesafe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.epan.mobilesafe.libs.StatusBarUtil;

public class HomeActivity extends Activity {


    private GridView gv_home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBar();
        setContentView(R.layout.activity_home);
        gv_home = (GridView) findViewById(R.id.gv_home);
        gv_home.setAdapter(new HomeAdapter());
        gv_home.setOnItemClickListener(new OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:

                        break;
                    case 8:
                        Intent intent = new Intent(HomeActivity.this, SettingActivity.class );
                        startActivity(intent);
                        break;
                }
            }
        });
    }

    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.colorMainBar));
    }

    private class HomeAdapter extends BaseAdapter{

        int[] imageId = { R.mipmap.safe, R.mipmap.call, R.mipmap.app,
        R.mipmap.progress, R.mipmap.net, R.mipmap.virus, R.mipmap.clean,
        R.mipmap.tools, R.mipmap.setting };

        String[] names = { "手机卫士", "通讯卫士", "软件管理", "进程管理",
                "流量统计", "手机杀毒", "缓存清理", "高级工具", "设置中心" };

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
}
