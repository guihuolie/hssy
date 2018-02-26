package com.hssy.hssy.module.navhome;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;


import com.hssy.hssy.R;
import com.hssy.hssy.base.BaseActivity;
import com.hssy.hssy.utils.ShareUtil;
import com.hssy.hssy.utils.StatusBarUtil;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * 项目主页面
 * <p>
 */
public class NavHomeActivity extends BaseActivity {

    @BindView(R.id.nav_home_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.nav_home_fab)
    FloatingActionButton mFab;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_nav_home;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        StatusBarUtil.setTranslucentForImageView(this, 0, mToolbar);
        mToolbar.setNavigationOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//不添加这句华为手机会出现标题显示不完整的问题
    }

    @OnClick(R.id.nav_home_fab)
    public void onClick() {
        ShareUtil.share(this, R.string.string_share_text);
    }
}
