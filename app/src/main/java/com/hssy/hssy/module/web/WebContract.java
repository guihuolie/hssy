package com.hssy.hssy.module.web;

import android.app.Activity;

import com.hssy.hssy.base.BasePresenter;
import com.hssy.hssy.base.BaseView;


/**
 * WebContract
 *

 */

public interface WebContract {

    interface IWebView extends BaseView {
        Activity getWebViewContext();

        void setGankTitle(String title);

        void loadGankUrl(String url);

        void initWebView();
    }

    interface IWebPresenter extends BasePresenter {
        String getGankUrl();
    }
}
