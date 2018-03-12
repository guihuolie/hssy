package com.hssy.hssy;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.hssy.hssy.config.ServletUrl;
import com.hssy.hssy.module.home.HomeActivity;
import com.hssy.hssy.sqldatautil.DatabaseUtils;
import com.hssy.hssy.sqldatautil.MyOpenHelper;
import com.hssy.hssy.model.User;
import com.hssy.hssy.updateAPP.UpdataInfo;
import com.hssy.hssy.updateAPP.UpdataInfoParser;
import com.hssy.hssy.utils.BASE64;
import com.hssy.hssy.utils.CodeUtil;
import com.hssy.hssy.utils.DesUtils;
import com.hssy.hssy.utils.EditTextClearTools;
import com.hssy.hssy.utils.HttpUtils;
import com.hssy.hssy.utils.ToastyUtil;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import static com.hssy.hssy.utils.CodeUtil.LOGIN_ERR;
import static com.hssy.hssy.utils.CodeUtil.LOGIN_FAIL;
import static com.hssy.hssy.utils.CodeUtil.LOGIN_SUCESS;
import static com.hssy.hssy.utils.CodeUtil.WRITE_EXTERNAL_STORAGE_REQUEST_CODE;
import static com.hssy.hssy.utils.PackageUtil.getVersionName;

public class LoginActivity extends AppCompatActivity {

    private EditText etUserName;
    private EditText etUserPassword;
    private String userName;
    private String userPassword;
    private MyOpenHelper myOpenHelper= DatabaseUtils.getHelper();
    private final String TAG = this.getClass().getName();
    private Button getVersion;
    private UpdataInfo info;
    private String localVersion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        try {
            localVersion = getVersionName();
            CheckVersionTask cv = new CheckVersionTask();
            new Thread(cv).start();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void init(){
        etUserName = (EditText) findViewById(R.id.et_userName);
        etUserPassword = (EditText) findViewById(R.id.et_password);
        ImageView usernameClear = (ImageView) findViewById(R.id.iv_unameClear);
        ImageView pwdClear = (ImageView) findViewById(R.id.iv_pwdClear);

        EditTextClearTools.addClearListener(etUserName,usernameClear);
        EditTextClearTools.addClearListener(etUserPassword,pwdClear);

        List list=myOpenHelper.queryAll(User.class);
        if(list!=null&&list.size()>0){
            etUserName.setText(((User)list.get(0)).getUser_code());
        }

    }

    public void login(View view){
        userName=etUserName.getText().toString();
        userPassword=etUserPassword.getText().toString();

        JSONObject json=new JSONObject();

        try{
            json.put("idear","login");
            json.put("userName",userName);
            json.put("userPassword",userPassword);

            DesUtils des = new DesUtils("sssss");//自定义密钥
//            final String url= ServletUrl.REGISTER+"?pams_json="+URLEncoder.encode(json.toString(),"UTF-8");
            final String url= ServletUrl.REGISTER+"?pams_json="+des.encrypt(json.toString());
            new Thread(new Runnable(){
                @Override
                public void run() {
                    try{
                        String ss= BASE64.getFromBase64(HttpUtils.connect(new URL(url)));
                        JSONObject resObj = new JSONObject(ss);
                        String returnCode=resObj.get("returnCode").toString();

                        Message msg = new Message();
                        msg.what = Integer.parseInt(returnCode);
                        Bundle data = new Bundle();
                        data.putString("user_code",userName);
                        data.putString("pwd",userPassword);
                        msg.setData(data);
                        handler.sendMessage(msg);
                    }catch (Exception e){
                        System.out.print("e=="+e);
                    }


                }
            }).start();

        }catch (Exception e){
            System.out.print("e=="+e);
        }
    }

    public void register(View view){
        Intent intent_login = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent_login);
        overridePendingTransition(R.anim.screen_zoom_in, R.anim.screen_zoom_out);
//        finish();R.anim.push_left_in,R.anim.push_left_out
    }


//    private Handler handler = new Handler(new Handler.Callback() {
//
//        @Override
//        public boolean handleMessage(Message msg) {
//            // TODO Auto-generated method stub
//            Bundle data = msg.getData();
//            String from = data.getString("from");
//            if("login".equals(from)){
//                String val = data.getString("value");
//                if("1004".equals(val)){
//                    ToastyUtil.showError("登录失败：用户名或密码错误！");
//                }else if("1005".equals(val)){
//
//                    try{
//                        //保存用户名
//                        User user_1 = new User(data.getString("user_code"),data.getString("pwd"));
//                        myOpenHelper.save(user_1);
//                        Intent intent_login = new Intent(LoginActivity.this, HomeActivity.class);
//                        startActivity(intent_login);
//                        overridePendingTransition(R.anim.screen_zoom_in, R.anim.screen_zoom_out);
//                    }catch (Exception e){
//
//                    }
//
//                }else if("1003".equals(val)){
//                    ToastyUtil.showError("登录超时！");
//                }
//            }
//            return true;
//        }
//    });



    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == WRITE_EXTERNAL_STORAGE_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    downLoadApk();
            } else {
                ToastyUtil.showError("SD卡权限未获取！");
            }
        }
    }
    private String getVersionName() throws Exception {
        //getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageManager packageManager = getPackageManager();
        PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(),
                0);
        return packInfo.versionName;
    }
    public class CheckVersionTask implements Runnable {
        InputStream is;
        public void run() {
            try {
                String path = getResources().getString(R.string.url_server);
                URL url = new URL(path);
                HttpURLConnection conn = (HttpURLConnection) url
                        .openConnection();
                conn.setConnectTimeout(5000);
                conn.setRequestMethod("GET");
                int responseCode = conn.getResponseCode();
                if (responseCode == 200) {
                    // 从服务器获得一个输入流
                    is = conn.getInputStream();
                }
                info = UpdataInfoParser.getUpdataInfo(is);
                if (!info.getVersion().equals(localVersion)) {
                    Log.i(TAG, "版本号不相同 ");
                    Message msg = new Message();
                    msg.what = CodeUtil.UPDATA_CLIENT;
                    handler.sendMessage(msg);
                }
            } catch (Exception e) {
                Message msg = new Message();
                msg.what = CodeUtil.GET_UNDATAINFO_ERROR;
                handler.sendMessage(msg);
                e.printStackTrace();
            }
        }
    }


    private Handler handler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            // TODO Auto-generated method stub
            switch (msg.what) {
                case CodeUtil.UPDATA_CLIENT:
                    //对话框通知用户升级程序
                    showUpdataDialog();
                    break;
                case CodeUtil.GET_UNDATAINFO_ERROR:
                    //服务器超时
                    //    Toast.makeText(getApplicationContext(), "获取服务器更新信息失败", 1).show();
                    break;
                case CodeUtil.DOWN_ERROR:
                    //下载apk失败
                    //    Toast.makeText(getApplicationContext(), "下载新版本失败", 1).show();
                    break;
                case DOWNLOAD_FINISH:
                    installApk();
                    break;
                case LOGIN_FAIL:
                    ToastyUtil.showError("登录失败：用户名或密码错误！");
                    break;
                case LOGIN_SUCESS:
                    try{
                        //保存用户名
                        Bundle data = msg.getData();
                        User user_1 = new User(data.getString("user_code"),data.getString("pwd"));
                        myOpenHelper.save(user_1);
                        Intent intent_login = new Intent(LoginActivity.this, HomeActivity.class);
                        startActivity(intent_login);
                        overridePendingTransition(R.anim.screen_zoom_in, R.anim.screen_zoom_out);
                    }catch (Exception e){

                    }
                    break;
                case LOGIN_ERR:
                    ToastyUtil.showError("登录超时！");
                    break;
            }
            return true;
        }
    });
    /*
    *
    * 弹出对话框通知用户更新程序
    *
    * 弹出对话框的步骤：
    *  1.创建alertDialog的builder.
    *  2.要给builder设置属性, 对话框的内容,样式,按钮
    *  3.通过builder 创建一个对话框
    *  4.对话框show()出来
    */
    protected void showUpdataDialog() {
        AlertDialog.Builder builer = new AlertDialog.Builder(this);
        builer.setTitle("版本升级");
        builer.setMessage(info.getDescription());
        //当点确定按钮时从服务器上下载 新的apk 然后安装   װ
        builer.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                File apkFile = new File(Environment.getExternalStorageDirectory(), "updata"+info.getVersion()+".apk");
                if(apkFile.exists()){
                   installApk();
                   return;
                }
                if (ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    //申请WRITE_EXTERNAL_STORAGE权限
                    ActivityCompat.requestPermissions(LoginActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            WRITE_EXTERNAL_STORAGE_REQUEST_CODE);
                }else{
                    downLoadApk();
                }

            }
        });
        builer.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                //do sth
            }
        });
        AlertDialog dialog = builer.create();
        dialog.show();
    }
    /*
    * 从服务器中下载APK
    */
    protected void downLoadApk() {
        final ProgressDialog pd;    //进度条对话框
        pd = new  ProgressDialog(this);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setMessage("正在下载更新");
        pd.show();
        new Thread(){
            @Override
            public void run() {
                try {
                    getFileFromServer(info.getUrl(), pd);
                } catch (Exception e) {
                    Message msg = new Message();
                    msg.what = CodeUtil.DOWN_ERROR;
                    handler.sendMessage(msg);
                    e.printStackTrace();
                }
            }}.start();
    }

//    //安装apk
//    protected void installApk() {
//        File file = new File(Environment.getExternalStorageDirectory(), "updata"+info.getVersion()+".apk");
//        if(!file.exists()){
//            return;
//        }
//        Intent intent = new Intent();
//        //执行动作
//        intent.setAction(Intent.ACTION_VIEW);
//        //执行的数据类型
//        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
//        startActivity(intent);
//
//    }

    /**
     * 安装apk文件
     *
     *
     */
    private void installApk() {
        File apkFile = new File(Environment.getExternalStorageDirectory(), "updata"+info.getVersion()+".apk");
        if(!apkFile.exists()){
            return;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        try {
            String[] command = {"chmod", "777", apkFile.toString()};
            ProcessBuilder builder = new ProcessBuilder(command);
            builder.start();
        } catch (IOException ignored) {
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Uri uriForFile = FileProvider.getUriForFile(this, this.getApplicationContext().getPackageName() + ".provider", apkFile);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(uriForFile, this.getContentResolver().getType(uriForFile));
            try {
                this.startActivity(intent);
            } catch (Exception var5) {
                var5.printStackTrace();

            }
        } else {
            intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }


    }

    final int DOWNLOAD_FINISH=1110;
    public  void getFileFromServer(String path, ProgressDialog pd) throws Exception{

        //如果相等的话表示当前的sdcard挂载在手机上并且是可用的
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            URL url = new URL(path);
            HttpURLConnection conn =  (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            //获取到文件的大小
            pd.setMax(conn.getContentLength());
            InputStream is = conn.getInputStream();

            File file = new File(Environment.getExternalStorageDirectory(), "updata"+info.getVersion()+".apk");
            FileOutputStream fos = new FileOutputStream(file);
            BufferedInputStream bis = new BufferedInputStream(is);
            byte[] buffer = new byte[1024];
            int len =100;
            int total=0;
            while(len>0){
                len =bis.read(buffer);
                if(len<=0){
                    handler.sendEmptyMessage(DOWNLOAD_FINISH);
                    break;
                }
                fos.write(buffer, 0, len);
                total+= len;
                //获取当前下载量
                pd.setProgress(total);

            }
            pd.dismiss(); //结束掉进度条对话框
            fos.close();
            bis.close();
            is.close();

        }
    }
}
