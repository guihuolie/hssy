package com.hssy.hssy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.hssy.hssy.module.home.HomeActivity;
import com.hssy.hssy.module.web.WebViewActivity;
import com.hssy.hssy.utils.EditTextClearTools;
import com.hssy.hssy.utils.ToastyUtil;

public class LoginActivity extends AppCompatActivity {

    private EditText etUserName;
    private EditText etUserPassword;

    private String userName;
    private String userPassword;

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
        etUserName.setText("admin");
        etUserPassword.setText("1234");
    }

    public void login(View view){
        userName=etUserName.getText().toString();
        userPassword=etUserPassword.getText().toString();
        if("admin".equals(userName)&&"1234".equals(userPassword)){
            Intent intent_login = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent_login);
        }else {
            ToastyUtil.showError("用户名或密码错误");
        }
    }
}
