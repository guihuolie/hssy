package com.hssy.hssy;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hssy.hssy.base.BaseActivity;
import com.hssy.hssy.config.ServletUrl;
import com.hssy.hssy.module.home.HomeActivity;
import com.hssy.hssy.sqldatautil.DatabaseUtils;
import com.hssy.hssy.sqldatautil.MyOpenHelper;
import com.hssy.hssy.sqldatautil.dataModel.User;
import com.hssy.hssy.utils.BASE64;
import com.hssy.hssy.utils.DesUtils;
import com.hssy.hssy.utils.EditTextClearTools;
import com.hssy.hssy.utils.HttpUtils;
import com.hssy.hssy.utils.PackageUtil;
import com.hssy.hssy.utils.ToastyUtil;
import com.hssy.hssy.utils.ValidateUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;

import butterknife.BindView;

public class RegisterActivity extends BaseActivity {
    @BindView(R.id.register_toolbar)
    Toolbar mToolbar;

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_register);
//
//    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_register;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        init();
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private EditText etUserName;
    private EditText etUserPassword;
    private EditText etUserPassword2;
    private EditText etUserTel;
    private EditText etUserMail;

    private TextView etUserName_check;
    private TextView etUserPassword_check;
    private TextView etUserPassword2_check;
    private TextView etUserTel_check;
    private TextView etUserMail_check;


    private String userName;
    private String userPassword;
    private String userPassword2;
    private String UserTel;
    private String UserMail;

    private void init(){
        etUserName = (EditText) findViewById(R.id.et_userName);
        etUserPassword = (EditText) findViewById(R.id.et_password);
        etUserPassword2 = (EditText) findViewById(R.id.et_password2);
        etUserTel = (EditText) findViewById(R.id.et_userTel);
        etUserMail = (EditText) findViewById(R.id.et_userMail);

        etUserName_check=(TextView) findViewById(R.id.username_sm);
        etUserPassword_check=(TextView) findViewById(R.id.pwd_sm);
        etUserPassword2_check=(TextView) findViewById(R.id.pwd2_sm);
        etUserTel_check=(TextView) findViewById(R.id.tel_sm);
        etUserMail_check=(TextView) findViewById(R.id.mail_sm);

        ImageView usernameClear = (ImageView) findViewById(R.id.iv_unameClear);
        ImageView pwdClear = (ImageView) findViewById(R.id.iv_pwdClear);
        ImageView pwdClear2 = (ImageView) findViewById(R.id.iv_pwdClear2);
        ImageView userTelClear = (ImageView) findViewById(R.id.iv_userTelClear);
        ImageView userMailClear = (ImageView) findViewById(R.id.iv_userMailClear);


        EditTextClearTools.addClearListener(etUserName,usernameClear);
        EditTextClearTools.addClearListener(etUserPassword,pwdClear);
        EditTextClearTools.addClearListener(etUserPassword2,pwdClear2);
        EditTextClearTools.addClearListener(etUserTel,userTelClear);
        EditTextClearTools.addClearListener(etUserMail,userMailClear);


        etUserName.addTextChangedListener(new EditChangedListener(etUserName,etUserName_check,"用户名不符合规则"));
        etUserPassword.addTextChangedListener(new EditChangedListener(etUserPassword,etUserPassword_check,"密码不符合规则"));
        etUserPassword2.addTextChangedListener(new EditChangedListener(etUserPassword2,etUserPassword2_check,"两次密码不一致"));
        etUserTel.addTextChangedListener(new EditChangedListener(etUserTel,etUserTel_check,"手机号错误"));
        etUserMail.addTextChangedListener(new EditChangedListener(etUserMail,etUserMail_check,"邮箱错误"));

    }

    private Boolean user_name_flag=false;
    private Boolean user_pwd_flag=false;
    private Boolean user_pwd2_flag=false;
    private Boolean user_tel_flag=false;
    private Boolean user_email_flag=false;
    public void register_submit(View view){
        if(user_name_flag&&user_pwd_flag&&user_pwd2_flag&&user_tel_flag&&user_email_flag){

        }else{
            ToastyUtil.showError("填写信息有误！");
            return;
        }
        userName=etUserName.getText().toString();
        userPassword=etUserPassword.getText().toString();
        userPassword2=etUserPassword2.getText().toString();
        UserTel=etUserTel.getText().toString();
        UserMail=etUserMail.getText().toString();

        JSONObject json=new JSONObject();

        try{
            json.put("idear","register");
            json.put("userName",userName);
            json.put("userPassword",userPassword);
            json.put("userPassword2",userPassword2);
            json.put("userTel",UserTel);
            json.put("userMail",UserMail);


            DesUtils des = new DesUtils("sssss");//自定义密钥
//            final String url= ServletUrl.REGISTER+"?pams_json="+URLEncoder.encode(json.toString(),"UTF-8");
            final String url= ServletUrl.REGISTER+"?pams_json="+des.encrypt(json.toString());
            new Thread(new Runnable(){
                @Override
                public void run() {
                    try{
                        String ss=BASE64.getFromBase64(HttpUtils.connect(new URL(url)));
                        JSONObject resObj = new JSONObject(ss);
                        String returnCode=resObj.get("returnCode").toString();

                        Message msg = new Message();
                        Bundle data = new Bundle();
                        data.putString("from","register");
                        data.putString("value",returnCode);

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


//        Intent intent_login = new Intent(RegisterActivity.this, LoginActivity.class);
//        startActivity(intent_login);
    }


    class EditChangedListener implements TextWatcher {

        private EditText input_text;
        private TextView check_input;
        private String str;

        EditChangedListener(EditText input_text,TextView check_input,String str){
            this.input_text=input_text;
            this.check_input=check_input;
            this.str=str;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//            if(TextUtils.isEmpty(charSequence)){
//                mContent.setText("木有变，木有变");
//            }else{
//                mContent.setText("变化中，变化中");
//            }

        }

        @Override
        public void afterTextChanged(Editable editable) {
            Boolean flag=false;
            if("用户名不符合规则".equals(str)){
                check_input.setText("*请填写数字或英文字母");
                flag=ValidateUtil.isUserName(this.input_text.getText().toString());
                user_name_flag=flag;
            }else if("密码不符合规则".equals(str)){
                check_input.setText("*请填写数字或英文字母");
                flag=ValidateUtil.isPassword(this.input_text.getText().toString());
                user_pwd_flag=flag;
            }else if("两次密码不一致".equals(str)){
                check_input.setText("");
                if(this.input_text.getText().toString().equals(etUserPassword.getText().toString())){
                    flag=true;
                }
                user_pwd2_flag=flag;
            }else if("手机号错误".equals(str)){
                check_input.setText("");
                flag=ValidateUtil.isTel(this.input_text.getText().toString());
                user_tel_flag=flag;
            }else if("邮箱错误".equals(str)){
                check_input.setText("");
                flag=ValidateUtil.isEmail(this.input_text.getText().toString());
                user_email_flag=flag;
            }
            if(!flag){
                check_input.append("("+str+")");
            }

        }
    };


    private Handler handler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            // TODO Auto-generated method stub
            Bundle data = msg.getData();
            String from = data.getString("from");
            if("register".equals(from)){
                String val = data.getString("value");
                if("1000".equals(val)){
                    ToastyUtil.showError("注册失败：请稍后再试！");
                }else if("1001".equals(val)){
                    ToastyUtil.showSuccess("注册成功：账号为手机号");
                    try{

                        Intent intent_login = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(intent_login);
                    }catch (Exception e){
                        Log.d("1","==========");
                    }

                }else if("1002".equals(val)){
                    ToastyUtil.showError("注册失败：手机号已注册！");
                }
            }
            return true;
        }
    });


}
