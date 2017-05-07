package com.epan.mobilesafe;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.epan.mobilesafe.untils.StreamUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class SplashActivity extends Activity {

    private static final int MSG_UPDATE_DIALOG = 1;
    private static final int MSG_ENTER_HOME = 2;
    private static final int MSG_SERVER_ERROR = 3;
    private static final int MSG_URL_ERROR = 4;
    private static final int MSG_IO_ERROR = 5;
    private static final int MSG_JSON_ERROR = 6;
    private String code; // 版本号
    private String des; // 版本描述
    private String apkurl;// 新版本的下载地址

    private TextView tv_version;
    private TextView tv_progress;
    private SharedPreferences sp;


    /**
     * 使状态栏透明
     * <p>
     * 适用于图片作为背景的界面,此时需要图片填充到状态栏
     *
     * @param * activity 需要设置的activity
     */
    public static void setTranslucent(Activity SplashActivity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 设置状态栏透明
            SplashActivity.getWindow().addFlags(WindowManager.
                    LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // 设置根布局的参数
            ViewGroup rootView = (ViewGroup) ((ViewGroup) SplashActivity.
                    findViewById(android.R.id.content)).getChildAt(0);
            rootView.setFitsSystemWindows(true);
            rootView.setClipToPadding(true);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //无title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        sp = getSharedPreferences("config", MODE_PRIVATE);
        setContentView(R.layout.activity_splash);
        setTranslucent(this);
        tv_version = (TextView) findViewById(R.id.tv_version);
        tv_version.setText("版本号：" + getVersionName());
        tv_progress = (TextView) findViewById(R.id.tv_progress);
        if (sp.getBoolean("update", true)) {
            update();
        } else {
            new Thread() {
                public void run() {
                    //在子线程中睡眠
                    SystemClock.sleep(2000);
                    //内部封装了一个Handler
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //在主线程中执行的
                            enterHome();
                        }
                    });
                }

                ;
            }.start();
        }
    }

    /**
     * 动态获取程序版本号
     */
    public String getVersionName() {
        PackageManager pm = getPackageManager();
        PackageInfo packageInfo = null;
        try {
            packageInfo = pm.getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String versionName = packageInfo.versionName;
        return versionName;
    }

    /**
     * 更新检查操作
     */
    private void update() {
        new Thread() {
            public void run() {
                Message msg = Message.obtain();
                long startTime = System.currentTimeMillis();
                try {
                    URL url = new URL("http://192.168.2.213:8080/android/updateinfo.html");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setConnectTimeout(5000);
                    conn.setRequestMethod("GET");
                    int responseCode = conn.getResponseCode();
                    if (responseCode == 200) {
                        //连接成功
                        InputStream inputStream = conn.getInputStream();
                        String josn = StreamUtils.parserStream(inputStream);
                        JSONObject jsonObject = new JSONObject(josn);
                        code = jsonObject.getString("code");
                        des = jsonObject.getString("des");
                        apkurl = jsonObject.getString("apkurl");
                        if (code.equals(getVersionName())) {
                            msg.what = MSG_ENTER_HOME;
                        } else {
                            msg.what = MSG_UPDATE_DIALOG;
                        }
                    } else {
                        //连接失败
                        msg.what = MSG_SERVER_ERROR;
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    msg.what = MSG_URL_ERROR;
                } catch (IOException e) {
                    e.printStackTrace();
                    msg.what = MSG_IO_ERROR;
                } catch (JSONException e) {
                    e.printStackTrace();
                    msg.what = MSG_JSON_ERROR;
                } finally {
                    long endTime = System.currentTimeMillis();
                    long dTime = endTime - startTime;
                    if (dTime < 1000) {
                        SystemClock.sleep(1000 - dTime);
                    }
                    handler.sendMessage(msg);
                }

            }
        }.start();
    }

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case MSG_UPDATE_DIALOG:
                    showUpdateDialog();
                    break;
                case MSG_ENTER_HOME:
                    enterHome();
                    break;
                case MSG_SERVER_ERROR:
                    Toast.makeText(getApplicationContext(), "服务器异常",
                            Toast.LENGTH_SHORT).show();
                    enterHome();
                    break;
                case MSG_URL_ERROR:
                    Toast.makeText(getApplicationContext(), "错误号：" +
                            MSG_URL_ERROR, Toast.LENGTH_SHORT).show();
                    enterHome();
                    break;
                case MSG_IO_ERROR:
                    Toast.makeText(getApplicationContext(), "当前网络不稳定...",
                            Toast.LENGTH_SHORT).show();
                    enterHome();
                    break;
                case MSG_JSON_ERROR:
                    Toast.makeText(getApplicationContext(), "错误号：" +
                            MSG_JSON_ERROR, Toast.LENGTH_SHORT).show();
                    enterHome();
                    break;
            }
        }

        ;
    };

    /**
     * 升级对话框
     */
    private void showUpdateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("升级新版本: v" + code);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setMessage(des);
        builder.setCancelable(false);   //不能直接取消对话框
        builder.setPositiveButton("升级", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                download();
            }
        });
        builder.setNegativeButton("不升级", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                enterHome();
            }
        });
        builder.show();
    }

//    private void showUpdateDialog() {
//        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
//                .setTitleText("更新新版本: v" + code)
//                .setContentText(des)
//                .setCancelText("取消")
//                .setConfirmText("升级")
//                .showCancelButton(true)
//                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
//
//                    @Override
//                    public void onClick(SweetAlertDialog sDialog) {
//                        sDialog.dismiss();
//                        enterHome();
//
//                        // reuse previous dialog instance, keep widget user state, reset them if you need
////                        sDialog.setTitleText("Cancelled!")
////                                .setContentText("Your imaginary file is safe :)")
////                                .setConfirmText("OK")
////                                .showCancelButton(false)
////                                .setCancelClickListener(null)
////                                .setConfirmClickListener(null)
////                                .changeAlertType(SweetAlertDialog.ERROR_TYPE);
//
//                        // or you can new a SweetAlertDialog to show
//                               /* sDialog.dismiss();
//                                new SweetAlertDialog(SampleActivity.this, SweetAlertDialog.ERROR_TYPE)
//                                        .setTitleText("Cancelled!")
//                                        .setContentText("Your imaginary file is safe :)")
//                                        .setConfirmText("OK")
//                                        .show();*/
//                    }
//                })
//                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                    @Override
//                    public void onClick(SweetAlertDialog sDialog) {
//                        download();
////                        sDialog.setTitleText("Deleted!")
////                                .setContentText("Your imaginary file has been deleted!")
////                                .setConfirmText("OK")
////                                .showCancelButton(false)
////                                .setCancelClickListener(null)
////                                .setConfirmClickListener(null)
////                                .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
//                    }
//                })
//                .show();
//    }

    /**
     * 下载安装新版本
     */
    private void download() {
        final String path = Environment.getExternalStorageDirectory()
                + "/downloads/mobilesafe_new.apk";
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.download(apkurl, path, new RequestCallBack<File>() {
            @Override
            public void onSuccess(ResponseInfo<File> responseInfo) {
                Toast.makeText(getApplicationContext(), "下载完成...", Toast.LENGTH_SHORT).show();
                //apk下载完成后，调用系统的安装方法
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.fromFile(new File(path)),
                        "application/vnd.android.package-archive");
                startActivityForResult(intent, 0);  //该方法在打开的Activity退出时会回调
                // 当前Activity的onActivityResult方法
            }

            @Override
            public void onFailure(HttpException e, String s) {
                e.printStackTrace();
            }

            @Override
            public void onLoading(long total, long current,
                                  boolean isUploading) {
                super.onLoading(total, current, isUploading);
                tv_progress.setVisibility(View.VISIBLE);// 让控件可见
                tv_progress.setText(current / 1024 + " /" + total / 1024 + " KB");
            }
        });
    }

    /**
     * 取消安装的回调方法
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        enterHome();
    }

    /**
     * 进入主界面
     */
    private void enterHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();   //关闭SplashActivity
    }
}

