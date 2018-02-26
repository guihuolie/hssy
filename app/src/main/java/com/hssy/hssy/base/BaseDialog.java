package com.hssy.hssy.base;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;


import com.hssy.hssy.R;

import butterknife.ButterKnife;

/**
 * Dialog父类
 *
 */

public abstract class BaseDialog extends Dialog {
    private Context mContext;

    public BaseDialog(Context context, int layoutId) {
        this(context, layoutId, R.style.MyDialog);
    }

    public BaseDialog(Context context, int layoutId, int styleId) {
        super(context, styleId);
        this.mContext = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(layoutId, null);
        this.setContentView(view);
        ButterKnife.bind(this);
    }
}
