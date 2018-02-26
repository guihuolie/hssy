package com.hssy.hssy.module.home;



import com.hssy.hssy.model.CategoryResult;
import com.hssy.hssy.model.PictureModel;
import com.hssy.hssy.net.NetWork;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;



public class HomePresenter implements HomeContract.IHomePresenter {

    private Subscription mSubscription;

    private HomeContract.IHomeView mHomeView;

    private List<PictureModel> mModels;

    HomePresenter(HomeContract.IHomeView homeView){
        this.mHomeView = homeView;
        mModels = new ArrayList<>();
    }

    @Override
    public void subscribe() {
        getBannerData();
    }

    public List<PictureModel> getBannerModel(){
        return this.mModels;
    }

    @Override
    public void unSubscribe() {
        if (mSubscription != null && !mSubscription.isUnsubscribed()){
            mSubscription.unsubscribe();
        }
    }

    @Override
    public void getBannerData() {
        mSubscription = NetWork.getGankApi()
                .getCategoryData("福利",5,1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CategoryResult>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mHomeView.showBannerFail("Banner 图加载失败");
                    }

                    @Override
                    public void onNext(CategoryResult categoryResult) {
                        if (categoryResult != null && categoryResult.results != null
                                && categoryResult.results.size() > 0){
                            List<String> imgUrls = new ArrayList<>();
                            for (CategoryResult.ResultsBean result : categoryResult.results) {
                                if (!result.url.isEmpty()){
                                    imgUrls.add(result.url);
                                }
                                PictureModel model = new PictureModel();
                                model.desc = result.desc;
                                model.url = result.url;
                                mModels.add(model);
                            }
                            mHomeView.setBanner(imgUrls);

                        }else{
                            mHomeView.showBannerFail("Banner 图加载失败");
                        }
                    }
                });
    }

}
