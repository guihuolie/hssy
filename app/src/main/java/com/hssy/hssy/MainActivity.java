package com.hssy.hssy;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.hssy.hssy.module.splash.SplashActivity;
import com.hssy.hssy.sqldatautil.DatabaseUtils;
import com.hssy.hssy.sqldatautil.MyOpenHelper;
import com.hssy.hssy.sqldatautil.dataModel.Student;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    //是否为第一次使用
    private boolean isFirst;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**
         * 启动一个延迟线程
         */
        (new Thread(){
            @Override
            public void run() {
                super.run();
                try{
                    DatabaseUtils.initHelper(MainActivity.this,"user.db");
                    Thread.sleep(2000);
                    //读取SharedPreferences中需要的数据
                    SharedPreferences preferences = getSharedPreferences("isFirst",MODE_PRIVATE);
                    isFirst = preferences.getBoolean("isFirst", true);
                    if (isFirst) {
                        //必须先初始化

                        startActivity(new Intent(MainActivity.this, SplashActivity.class));//引导界面
                    } else {
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));//登录界面
                    }
                    //实例化Editor对象
                    SharedPreferences.Editor editor = preferences.edit();
                    //存入数据
                    editor.putBoolean("isFirst", false);
                    //提交修改
                    editor.commit();
                    finish();
                }catch (Exception e){
                    System.out.print("yichang=============="+e.toString());
                }

            }
        }).start();
        //必须先初始化
//        DatabaseUtils.initHelper(this,"user.db");
//
//        //创建学生类
//        Student student1 = new Student("张三","1001",12);
//
//        //将学生类保存到数据库
//        MyOpenHelper data=DatabaseUtils.getHelper();
//        data.save(student1);
//
//        List<Student> list = new ArrayList<>();
//        list.add(new Student("李四","1002",13));
//        list.add(new Student("王五","1003",23));
//        list.add(new Student("赵六","1004",21));
//        list.add(new Student("钱七","1005",20));
//        data.saveAll(list);
//
//       List list1= data.queryAll(Student.class);
//       list1.size();
    }
}
