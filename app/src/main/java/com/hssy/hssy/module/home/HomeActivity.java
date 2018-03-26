package com.hssy.hssy.module.home;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.hssy.hssy.GlideImageLoader;
import com.hssy.hssy.LoginActivity;
import com.hssy.hssy.R;
import com.hssy.hssy.base.BaseActivity;
import com.hssy.hssy.base.adapter.CommonViewPagerAdapter;
import com.hssy.hssy.config.GlobalConfig;
import com.hssy.hssy.model.PictureModel;
import com.hssy.hssy.module.category.CategoryFragment;
import com.hssy.hssy.module.navabout.NavAboutActivity;
import com.hssy.hssy.module.navdeedback.NavDeedBackActivity;
import com.hssy.hssy.module.navhome.NavHomeActivity;
import com.hssy.hssy.module.picture.PictureActivity;
import com.hssy.hssy.module.web.WebViewActivity;
import com.hssy.hssy.utils.AlipayZeroSdk;
import com.hssy.hssy.utils.AndroidWorkaround;
import com.hssy.hssy.utils.PerfectClickListener;
import com.hssy.hssy.utils.ScreenUtil;
import com.hssy.hssy.utils.StatusBarUtil;
import com.kekstudio.dachshundtablayout.DachshundTabLayout;

import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

import java.util.List;

import butterknife.BindView;
import es.dmoral.toasty.Toasty;


/**
 * 主页面
 * <p>
 */

public class HomeActivity extends BaseActivity implements HomeContract.IHomeView,OnBannerListener{

    @BindView(R.id.main_head_img)
    ImageView mHeadImg;
    @BindView(R.id.main_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.main_tab)
    DachshundTabLayout mTabLayout;
    @BindView(R.id.main_appbar)
    AppBarLayout mAppbar;
    @BindView(R.id.main_vp)
    ViewPager mViewPager;
    @BindView(R.id.nav_view)
    NavigationView mNavView;
    @BindView(R.id.mainActivity)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.main_banner)
    Banner mBanner;
    @BindView(R.id.main_fab)
    FloatingActionButton mFab;
    // 保存用户按返回键的时间
    private long mExitTime = 0;
    private HomePresenter mHomePresenter;



    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_home;
    }

    @Override
    protected void beforeInit() {
        super.beforeInit();
        StatusBarUtil.setTranslucent(this);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mHomePresenter = new HomePresenter(this);
        mHomePresenter.subscribe();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) { // 4.4 以上版本
            // 设置 Toolbar 高度为 80dp，适配状态栏
            ViewGroup.LayoutParams layoutParams = mToolbar.getLayoutParams();
            layoutParams.height = ScreenUtil.dip2px(this,80);
            mToolbar.setLayoutParams(layoutParams);
        }

        // 华为底部导航栏适配
        if (AndroidWorkaround.checkDeviceHasNavigationBar(this)) {
            AndroidWorkaround.assistActivity(findViewById(android.R.id.content));
        }
//        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
//        params.setMargins(0,ScreenUtil.getStatusBarHeight(this),0,0);
//        params.gravity = Gravity.CENTER_HORIZONTAL;
//        mTabLayout.setLayoutParams(params);

        initDrawerLayout();

        mBanner.setIndicatorGravity(BannerConfig.RIGHT);
        mBanner.setOnBannerListener(this);

        String[] titles = {
                GlobalConfig.CATEGORY_NAME_APP,
                GlobalConfig.CATEGORY_NAME_ANDROID,
                GlobalConfig.CATEGORY_NAME_IOS,
                GlobalConfig.CATEGORY_NAME_FRONT_END,
                GlobalConfig.CATEGORY_NAME_RECOMMEND,
                GlobalConfig.CATEGORY_NAME_RESOURCE};

        CommonViewPagerAdapter infoPagerAdapter = new CommonViewPagerAdapter(getSupportFragmentManager(),titles);
        // App
        CategoryFragment appFragment = CategoryFragment.newInstance(titles[0]);
        // Android
        CategoryFragment androidFragment = CategoryFragment.newInstance(titles[1]);
        // iOS
        CategoryFragment iOSFragment = CategoryFragment.newInstance(titles[2]);
        // 前端
        CategoryFragment frontFragment = CategoryFragment.newInstance(titles[3]);
        // 瞎推荐
        CategoryFragment referenceFragment = CategoryFragment.newInstance(titles[4]);
        // 拓展资源s
        CategoryFragment resFragment = CategoryFragment.newInstance(titles[5]);

        infoPagerAdapter.addFragment(appFragment);
        infoPagerAdapter.addFragment(androidFragment);
        infoPagerAdapter.addFragment(iOSFragment);
        infoPagerAdapter.addFragment(frontFragment);
        infoPagerAdapter.addFragment(referenceFragment);
        infoPagerAdapter.addFragment(resFragment);

        mViewPager.setAdapter(infoPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.setCurrentItem(1);
        mViewPager.setOffscreenPageLimit(1);



    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHomePresenter != null){
            mHomePresenter.unSubscribe();
        }
    }

    @Override
    public boolean isSupportSwipeBack() {
        return false;
    }

    /**
     * inflateHeaderView 进来的布局要宽一些
     */
    private void initDrawerLayout() {
        mNavView.inflateHeaderView(R.layout.layout_main_nav);
        View headerView = mNavView.getHeaderView(0);
        headerView.findViewById(R.id.ll_nav_homepage).setOnClickListener(mListener);
//        headerView.findViewById(R.id.ll_nav_scan_address).setOnClickListener(mListener);
        headerView.findViewById(R.id.ll_nav_deedback).setOnClickListener(mListener);
        headerView.findViewById(R.id.ll_nav_login).setOnClickListener(mListener);
        headerView.findViewById(R.id.ll_nav_exit).setOnClickListener(mListener);
        headerView.findViewById(R.id.ll_nav_donation).setOnClickListener(mListener);

    }

    private PerfectClickListener mListener = new PerfectClickListener() {
        @Override
        protected void onNoDoubleClick(final View v) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
            mDrawerLayout.postDelayed(new Runnable() {
                @Override
                public void run() {
                   switch (v.getId()){
                       case R.id.ll_nav_homepage: // APP说明
                           startActivity(new Intent(HomeActivity.this, NavHomeActivity.class));
                           break;
//                       case R.id.ll_nav_scan_address: // 关于我们
//                           startActivity(new Intent(HomeActivity.this, NavAboutActivity.class));
//                           break;
                       case R.id.ll_nav_deedback: // 问题反馈
                           startActivity(new Intent(HomeActivity.this, NavDeedBackActivity.class));
                           break;
                       case R.id.ll_nav_donation: // 捐赠开发者
                           // https://fama.alipay.com/qrcode/qrcodelist.htm?qrCodeType=P  二维码地址
                           // http://cli.im/deqr/ 解析二维码
                           // aex01018hzmxqeqmcaffh96
                           if (AlipayZeroSdk.hasInstalledAlipayClient(HomeActivity.this)) {
                               AlipayZeroSdk.startAlipayClient(HomeActivity.this, "FKX09981RHCGSWKLS0WN3D");
                           } else {
                               Snackbar.make(mToolbar, "谢谢，您没有安装支付宝客户端", Snackbar.LENGTH_LONG).show();
                           }
                           break;
                       case R.id.ll_nav_login: // 登录账号
                           Intent intent_login = new Intent(HomeActivity.this, LoginActivity.class);
                           startActivity(intent_login);

//                           Intent intent_login = new Intent(HomeActivity.this, WebViewActivity.class);
//                           intent_login.putExtra(WebViewActivity.GANK_TITLE, "登录");
//                           intent_login.putExtra(WebViewActivity.GANK_URL, "https://github.com/login");
//                           startActivity(intent_login);
                           break;
                       case R.id.ll_nav_exit:
                           finish();
                           break;
                       default:
                           break;
                   }
                }
            },260);
        }
    };


    @Override
    public void onBackPressed() {
        if ((System.currentTimeMillis() - mExitTime) > 2000) {
            Snackbar.make(mDrawerLayout, R.string.exit_toast, Toast.LENGTH_SHORT).show();
            mExitTime = System.currentTimeMillis();
        } else {
            finish();
        }
    }

    @Override
    public void showBannerFail(String failMessage) {
        Toasty.error(this, failMessage).show();
    }

    @Override
    public void setBanner(List<String> imgUrls) {
        mBanner.setImages(imgUrls).setImageLoader(new GlideImageLoader()).start();
    }


    @Override
    public void OnBannerClick(int position) {
        PictureModel model = mHomePresenter.getBannerModel().get(position);
//        Intent intent = PictureActivity.newIntent(this,model.url,model.desc);
//        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
//                this,mBanner,PictureActivity.TRANSIT_PIC);
//        ActivityCompat.startActivity(this,intent,optionsCompat.toBundle());
        PictureActivity.start(this,model.url,model.desc,mBanner);
    }
}
