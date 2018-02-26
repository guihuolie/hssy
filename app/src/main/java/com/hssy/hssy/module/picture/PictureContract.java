package com.hssy.hssy.module.picture;

import android.graphics.Bitmap;

import com.hssy.hssy.base.BasePresenter;
import com.hssy.hssy.base.BaseView;




public interface PictureContract {

    interface PictureView extends BaseView {
        void hideProgress();
        void showProgress();
    }

    interface Presenter extends BasePresenter {
        void saveGirl(String url, Bitmap bitmap, String title);
    }
}
