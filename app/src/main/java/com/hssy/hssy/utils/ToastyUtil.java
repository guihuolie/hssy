package com.hssy.hssy.utils;

import android.widget.Toast;

import es.dmoral.toasty.Toasty;



public class ToastyUtil {

    public static void showError(String msg){
        Toasty.error(Utils.getContext(),msg, Toast.LENGTH_SHORT,true).show();
    }

    public static void showSuccess(String msg){
        Toasty.success(Utils.getContext(),msg, Toast.LENGTH_SHORT,true).show();
    }
}
