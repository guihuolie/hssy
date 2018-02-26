package com.hssy.hssy.module.category;



import com.hssy.hssy.config.GlobalConfig;
import com.hssy.hssy.model.CategoryResult;
import com.hssy.hssy.net.NetWork;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * ICategoryPresenter
 */

public class CategoryPresenter implements CategoryContract.ICategoryPresenter {

    private CategoryContract.ICategoryView mCategoryICategoryView;
    private int mPage = 1;
    private Subscription mSubscription;

    public CategoryPresenter(CategoryContract.ICategoryView androidICategoryView) {
        mCategoryICategoryView = androidICategoryView;
    }

    @Override
    public void subscribe() {
        getCategoryItems(true);
    }

    @Override
    public void unSubscribe() {
        if (mSubscription != null && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
    }

    @Override
    public void getCategoryItems(final boolean isRefresh) {
        if (isRefresh) {
            mPage = 1;
            mCategoryICategoryView.showSwipeLoading();
        } else {
            mPage++;
        }
        mSubscription = NetWork.getGankApi()
                .getCategoryData(mCategoryICategoryView.getCategoryName(), GlobalConfig.CATEGORY_COUNT, mPage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CategoryResult>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mCategoryICategoryView.hideSwipeLoading();
                        mCategoryICategoryView.getCategoryItemsFail(mCategoryICategoryView.getCategoryName() + " 列表数据获取失败！");
                    }

                    @Override
                    public void onNext(CategoryResult categoryResult) {
                        if (categoryResult != null && !categoryResult.error) {
                            if (categoryResult.results == null || categoryResult.results.size() == 0){
                                // 如果可以，这里可以增加占位图
                                mCategoryICategoryView.getCategoryItemsFail("获取数据为空！");
                            }else{
                                if (isRefresh) {
                                    mCategoryICategoryView.setCategoryItems(categoryResult.results);
                                    mCategoryICategoryView.hideSwipeLoading();
                                    mCategoryICategoryView.setLoading();
                                } else {
                                    mCategoryICategoryView.addCategoryItems(categoryResult.results);
                                }
                                // 如果当前获取的数据数目没有全局设定的每次获取的条数，说明已经没有更多数据
                                if (categoryResult.results.size() < GlobalConfig.CATEGORY_COUNT){
                                    mCategoryICategoryView.setNoMore();
                                }
                            }
                        } else {
                            mCategoryICategoryView.getCategoryItemsFail("获取数据失败！");
                        }

                    }
                });

    }
}
