package com.hssy.hssy.module.navabout;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;


import com.hssy.hssy.R;
import com.hssy.hssy.base.BaseActivity;
import com.hssy.hssy.module.web.WebViewActivity;
import com.hssy.hssy.utils.PackageUtil;
import com.hssy.hssy.utils.Utils;

import butterknife.BindView;
import butterknife.OnClick;

public class NavAboutActivity extends AppCompatActivity {




    @OnClick({R.id.tv_new_version, R.id.tv_function, R.id.tv_about_star, R.id.tv_gankio})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_new_version:
                Utils.openLink(this,getResources().getString(R.string.string_url_new_version));
                break;
            case R.id.tv_function:
//                Utils.openLink(this,getResources().getString(R.string.string_url_other));
                Intent intentOther = new Intent(this, WebViewActivity.class);
                intentOther.putExtra(WebViewActivity.GANK_TITLE,"其他开源");
                intentOther.putExtra(WebViewActivity.GANK_URL,getResources().getString(R.string.string_url_other));
                startActivity(intentOther);
                break;
            case R.id.tv_about_star:
                Intent intent = new Intent(this, WebViewActivity.class);
                intent.putExtra(WebViewActivity.GANK_TITLE,"给个Star吧");
                intent.putExtra(WebViewActivity.GANK_URL,getResources().getString(R.string.string_url_AiYaGirl));
                startActivity(intent);
                break;
        }
    }
}
