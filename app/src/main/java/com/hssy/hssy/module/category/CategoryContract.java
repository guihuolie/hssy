package com.hssy.hssy.module.category;



import com.hssy.hssy.base.BasePresenter;
import com.hssy.hssy.base.BaseView;
import com.hssy.hssy.model.CategoryResult;

import java.util.List;



public interface CategoryContract {

    interface ICategoryView extends BaseView {
        
        void getCategoryItemsFail(String failMessage);

        void setCategoryItems(List<CategoryResult.ResultsBean> data);

        void addCategoryItems(List<CategoryResult.ResultsBean> data);

        void showSwipeLoading();

        void hideSwipeLoading();

        void setLoading();

        String getCategoryName();

        void setNoMore();
    }
    
    interface ICategoryPresenter extends BasePresenter {
        
        void getCategoryItems(boolean isRefresh);
    }
}
