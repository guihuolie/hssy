package com.hssy.hssy;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.hssy.hssy.config.ServletUrl;
import com.hssy.hssy.module.home.HomeActivity;
import com.hssy.hssy.sqldatautil.DatabaseUtils;
import com.hssy.hssy.sqldatautil.MyOpenHelper;
import com.hssy.hssy.model.User;
import com.hssy.hssy.utils.BASE64;
import com.hssy.hssy.utils.DesUtils;
import com.hssy.hssy.utils.EditTextClearTools;
import com.hssy.hssy.utils.HttpUtils;
import com.hssy.hssy.utils.ToastyUtil;

import org.json.JSONObject;

import java.net.URL;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private EditText etUserName;
    private EditText etUserPassword;

    private String userName;
    private String userPassword;
    private MyOpenHelper myOpenHelper= DatabaseUtils.getHelper();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
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
                        Bundle data = new Bundle();
                        data.putString("from","login");
                        data.putString("value",returnCode);
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


    private Handler handler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            // TODO Auto-generated method stub
            Bundle data = msg.getData();
            String from = data.getString("from");
            if("login".equals(from)){
                String val = data.getString("value");
                if("1004".equals(val)){
                    ToastyUtil.showError("登录失败：用户名或密码错误！");
                }else if("1005".equals(val)){

                    try{
                        //保存用户名
                        User user_1 = new User(data.getString("user_code"),data.getString("pwd"));
                        myOpenHelper.save(user_1);
                        Intent intent_login = new Intent(LoginActivity.this, HomeActivity.class);
                        startActivity(intent_login);
                        overridePendingTransition(R.anim.screen_zoom_in, R.anim.screen_zoom_out);
                    }catch (Exception e){

                    }

                }else if("1003".equals(val)){
                    ToastyUtil.showError("登录超时！");
                }
            }
            return true;
        }
    });
}
