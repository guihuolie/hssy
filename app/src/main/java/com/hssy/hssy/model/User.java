package com.hssy.hssy.model;

/**
 * Created by chinalife on 2018/3/9.
 */

public class User {
    public String getUser_code() {
        return user_code;
    }

    public void setUser_code(String user_code) {
        this.user_code = user_code;
    }

    private String user_code;
    private String pass_word;

    public String getPass_word() {
        return pass_word;
    }

    public void setPass_word(String pass_word) {
        this.pass_word = pass_word;
    }

    public User(String user_code,String pass_word){
        this.user_code=user_code;
        this.pass_word=pass_word;
    }
}
