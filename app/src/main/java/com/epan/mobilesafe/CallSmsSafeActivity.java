package com.epan.mobilesafe;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.epan.mobilesafe.db.dao.BlackNumDao;
import com.epan.mobilesafe.domain.BlackNumInfo;
import com.epan.mobilesafe.libs.StatusBarUtil;
import com.epan.mobilesafe.untils.MyAsynckTask;

import java.util.List;

public class CallSmsSafeActivity extends Activity {
    private ListView lv_call_sms;
    private BlackNumDao dao;
    private List<BlackNumInfo> blackNumInfos;
    private BlackNumAdapter adapter;
    private AlertDialog dialog;
    private ProgressBar pb_progress;
    public static final int MAX_NUM = 20;
    public static int startIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBar();
        setContentView(R.layout.activity_call_sms_safe);
        lv_call_sms = (ListView) findViewById(R.id.lv_call_sms);
        pb_progress = (ProgressBar) findViewById(R.id.pb_progress);
        dao = new BlackNumDao(this);

        fillData();
        // ListView 滑动事件的监听
        lv_call_sms.setOnScrollListener(new OnScrollListener() {
            // 当ListView 滑动状态发生改变的时候调用
            // 静止 慢慢滑动的状态 惯性滑动
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
                    // 当ListView 是一个静止状态 当看到的条目时最后一个条目的时候 加载剩余的数据
                    // 获取到最后一个可见的条目
                    int lastVisiblePosition = lv_call_sms
                            .getLastVisiblePosition(); // 0
                    // 最后可见的条目 确实是集合最后的一个条目
                    if (lastVisiblePosition == blackNumInfos.size() - 1) {
                        // 加载下一波数据
                        startIndex += MAX_NUM;
                        fillData();
                    }
                }
            }

            // 当listView 滑动的时候调用
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {

            }
        });

    }

    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.colorPrimary));
    }

    public void fillData() {
        new MyAsynckTask() {

            @Override
            public void preTask() {
                pb_progress.setVisibility(View.VISIBLE);
            }

            @Override
            public void postTask() {
                if (adapter == null) {
                    adapter = new BlackNumAdapter();
                    lv_call_sms.setAdapter(adapter);
                } else {
                    adapter.notifyDataSetChanged();// 如果Adapter已经创建了 直接更新界面 不需要重新设置Adapter
                }
                pb_progress.setVisibility(View.INVISIBLE);
            }

            @Override
            public void doInBack() {
                if (blackNumInfos == null) {
                    blackNumInfos = dao.getPartBlackNum(startIndex, MAX_NUM);
                } else {
                    // 把新查出来的数据 添加到 之前的集合上
                    blackNumInfos.addAll(dao.getPartBlackNum(startIndex,
                            MAX_NUM));
                }
            }
        }.execute();
    }

    public void add(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = View.inflate(getApplicationContext(),
                R.layout.dialog_add_blacknum, null);
        final EditText et_blacknum = (EditText) view
                .findViewById(R.id.et_blacknum);
        final RadioGroup rg_checked = (RadioGroup) view
                .findViewById(R.id.rg_checked);
        Button btn_ok = (Button) view.findViewById(R.id.bt_ok);
        Button btn_cancel = (Button) view.findViewById(R.id.bt_cancel);

        btn_cancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btn_ok.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                String blackNum = et_blacknum.getText().toString().trim();
                int mode = 0;
                int checkedRadioButtonId = rg_checked.getCheckedRadioButtonId();
                if (checkedRadioButtonId == R.id.rb_tel) {
                    mode = BlackNumDao.TEL;
                } else if (checkedRadioButtonId == R.id.rb_sms) {
                    mode = BlackNumDao.SMS;
                } else if (checkedRadioButtonId == R.id.rb_all) {
                    mode = BlackNumDao.ALL;
                }
                dao.addBlackNum(blackNum, mode); // 添加到数据库
                blackNumInfos.add(0, new BlackNumInfo(blackNum, mode));// 添加到界面的集合上
                adapter.notifyDataSetChanged();// 刷新界面
                dialog.dismiss();
            }
        });

        builder.setView(view);
        dialog = builder.create();
        dialog.show();
    }

    private class BlackNumAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return blackNumInfos.size();
        }

        // convertView 刚刚移除屏幕的view对象
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final BlackNumInfo blackNumInfo = blackNumInfos.get(position);
            View view;
            ViewHolder holder;
            if (convertView != null) {
                view = convertView;
                // 取读holder信息
                holder = (ViewHolder) view.getTag();
                System.out.println("复用了convertView:" + position);
            } else {
                System.out.println("创建了view对象:" + position);
                view = View.inflate(getApplicationContext(),
                        R.layout.item_call_sms_safe, null);
                // 创建holder用来记录每个view对象的信息
                holder = new ViewHolder();
                // 把view的信息记录到本上
                holder.tv_num = (TextView) view.findViewById(R.id.tv_num);
                holder.tv_mode = (TextView) view.findViewById(R.id.tv_mode);
                holder.iv_delete = (ImageView) view
                        .findViewById(R.id.iv_delete);
                // 把信息记录在holder里
                view.setTag(holder);
            }
            holder.iv_delete.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(
                            CallSmsSafeActivity.this);
                    builder.setTitle("是否删除黑名单" + blackNumInfo.getBlackNum());
                    builder.setPositiveButton("确定",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {

                                    // 从界面中删除
                                    blackNumInfos.remove(blackNumInfo);
                                    // 刷新界面
                                    adapter.notifyDataSetChanged();
                                    dao.deleteBlackNum(blackNumInfo
                                            // 在数据库中删除
                                            .getBlackNum());
                                    dialog.dismiss();
                                }
                            });
                    builder.setNegativeButton("取消", null);
                    builder.show();
                            }
                });
            holder.tv_num.setText(blackNumInfo.getBlackNum());
                int mode = blackNumInfo.getMode();
            switch(mode)

                {
                    case BlackNumDao.TEL:
                        holder.tv_mode.setText("电话拦截");
                        break;
                    case BlackNumDao.SMS:
                        holder.tv_mode.setText("短信拦截");
                        break;
                    case BlackNumDao.ALL:
                        holder.tv_mode.setText("全部拦截");
                        break;
                }
            return view;
            }

            @Override
            public Object getItem ( int position){
                return blackNumInfos.get(position);
            }

            @Override
            public long getItemId ( int position){
                return position;
            }

        }

        // 创建Holder用来记录信息
        static class ViewHolder {
            private TextView tv_num;
            private TextView tv_mode;
            private ImageView iv_delete;
        }
    }
