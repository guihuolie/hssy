package com.hssy.hssy.module.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hssy.hssy.LoginActivity;
import com.hssy.hssy.R;
import com.hssy.hssy.base.BaseActivity;
import com.hssy.hssy.config.ConstantsImageUrl;
import com.hssy.hssy.module.home.HomeActivity;


import java.util.Random;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 闪屏页面
 *
 */

public class SplashActivity extends BaseActivity {
    private boolean isIn;

    @BindView(R.id.splash_iv_pic)
    ImageView mIvPic;
    @BindView(R.id.splash_tv_jump)
    TextView mTvJump;
    @BindView(R.id.splash_iv_defult_pic)
    ImageView mIvDefultPic;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        int i = new Random().nextInt(ConstantsImageUrl.TRANSITION_URLS.length);
        // 先显示默认图
        mIvDefultPic.setImageDrawable(getResources().getDrawable(R.mipmap.img_transition_default));
        Glide.with(this)
                .load(ConstantsImageUrl.TRANSITION_URLS[i])
                .placeholder(R.mipmap.img_transition_default)
                .error(R.mipmap.img_transition_default)
                .into(mIvPic);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mIvDefultPic.setVisibility(View.GONE);
            }
        }, 1500);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                toMainActivity();
            }
        }, 3500);
    }

    @Override
    public boolean isSupportSwipeBack() {
        return false;
    }

    /**
     * 跳转到主页面
     */
    private void toMainActivity() {
        if (isIn) {
            return;
        }
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.screen_zoom_in, R.anim.screen_zoom_out);
        finish();
        isIn = true;
    }



    @OnClick(R.id.splash_tv_jump)
    public void onClick() {
        toMainActivity();
    }
}
