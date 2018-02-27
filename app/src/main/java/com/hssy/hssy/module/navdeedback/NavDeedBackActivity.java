package com.hssy.hssy.module.navdeedback;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;


import com.hssy.hssy.R;
import com.hssy.hssy.base.BaseActivity;
import com.hssy.hssy.module.web.WebViewActivity;
import com.hssy.hssy.utils.Utils;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;


/**
 * 问题反馈页面
 * <p>
 */
public class NavDeedBackActivity extends BaseActivity {

    @BindView(R.id.nav_deed_back_toolbar)
    Toolbar mToolbar;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_nav_deed_back;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mToolbar.setNavigationOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    @OnClick({R.id.tv_qq, R.id.tv_email})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
//            case R.id.tv_issues:
//                intent = new Intent(this, WebViewActivity.class);
//                intent.putExtra(WebViewActivity.GANK_TITLE, "爱吖妹纸");
//                intent.putExtra(WebViewActivity.GANK_URL, "https://github.com/nanchen2251/AiYaGirl");
//                startActivity(intent);
//                break;
//            case R.id.tv_other:
//                intent = new Intent(this, WebViewActivity.class);
//                intent.putExtra(WebViewActivity.GANK_TITLE, "nanchen2251");
//                intent.putExtra(WebViewActivity.GANK_URL, "https://github.com/nanchen2251");
//                startActivity(intent);
//                break;
            case R.id.tv_qq:
                if (isQQClientAvailable()) {
                    String url = "mqqwpa://im/chat?chat_type=wpa&uin=351616836";
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                } else
                    Toasty.error(this, "当前设备未安装QQ").show();
                break;
            case R.id.tv_email:
//                intent = new Intent(Intent.ACTION_SENDTO);
//                intent.setData(Uri.parse("351616836@qq.com"));
//                startActivity(intent);
                break;
//            case R.id.tv_blog:
//                intent = new Intent(this, WebViewActivity.class);
//                intent.putExtra(WebViewActivity.GANK_TITLE, "博客园");
//                intent.putExtra(WebViewActivity.GANK_URL, "http://www.cnblogs.com/liushilin/");
//                startActivity(intent);
//                break;
        }
    }

    /**
     * 判断qq是否可用
     */
    public static boolean isQQClientAvailable() {
        final PackageManager packageManager = Utils.getContext().getPackageManager();
        List<PackageInfo> packageInfo = packageManager.getInstalledPackages(0);
        if (packageInfo != null) {
            for (int i = 0; i < packageInfo.size(); i++) {
                String pn = packageInfo.get(i).packageName;
                if (pn.equals("com.tencent.mobileqq")) {
                    return true;
                }
            }
        }
        return false;
    }
}
