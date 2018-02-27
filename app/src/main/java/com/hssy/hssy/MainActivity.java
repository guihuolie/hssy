package com.hssy.hssy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.hssy.hssy.sqldatautil.DatabaseUtils;
import com.hssy.hssy.sqldatautil.MyOpenHelper;
import com.hssy.hssy.sqldatautil.Student;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //必须先初始化
        DatabaseUtils.initHelper(this,"user.db");

        //创建学生类
        Student student1 = new Student("张三","1001",12);

        //将学生类保存到数据库
        MyOpenHelper data=DatabaseUtils.getHelper();
        data.save(student1);

        List<Student> list = new ArrayList<>();
        list.add(new Student("李四","1002",13));
        list.add(new Student("王五","1003",23));
        list.add(new Student("赵六","1004",21));
        list.add(new Student("钱七","1005",20));
        data.saveAll(list);

       List list1= data.queryAll(Student.class);
       list1.size();
    }
}
