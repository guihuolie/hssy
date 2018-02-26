package com.hssy.hssy.module.category;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.LinearLayoutManager;


import com.hssy.hssy.R;
import com.hssy.hssy.base.BaseFragment;
import com.hssy.hssy.model.CategoryResult;
import com.hssy.hssy.widget.RecyclerViewDivider;
import com.hssy.hssy.widget.RecyclerViewWithFooter.OnLoadMoreListener;
import com.hssy.hssy.widget.RecyclerViewWithFooter.RecyclerViewWithFooter;

import java.util.List;

import butterknife.BindView;
import es.dmoral.toasty.Toasty;

/**
 * 主页轮播下面的Fragment
 * <p>

 */

public class CategoryFragment extends BaseFragment implements CategoryContract.ICategoryView, OnRefreshListener, OnLoadMoreListener {

    public static final String CATEGORY_NAME = "com.hssy.hssy.module.category.CategoryFragment.CATEGORY_NAME";
    @BindView(R.id.recyclerView)
    RecyclerViewWithFooter mRecyclerView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private String categoryName;
    private CategoryRecyclerAdapter mAdapter;
    private CategoryContract.ICategoryPresenter mICategoryPresenter;

    public static CategoryFragment newInstance(String mCategoryName) {
        CategoryFragment categoryFragment = new CategoryFragment();
        Bundle bundle = new Bundle();
        bundle.putString(CATEGORY_NAME, mCategoryName);
        categoryFragment.setArguments(bundle);
        return categoryFragment;
    }


    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_category;
    }

    @Override
    protected void init() {
        mICategoryPresenter = new CategoryPresenter(this);

        categoryName = getArguments().getString(CATEGORY_NAME);

        mSwipeRefreshLayout.setOnRefreshListener(this);

        mAdapter = new CategoryRecyclerAdapter(getActivity());

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addItemDecoration(new RecyclerViewDivider(getActivity(), LinearLayoutManager.HORIZONTAL));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setOnLoadMoreListener(this);
        mRecyclerView.setEmpty();

        mICategoryPresenter.subscribe();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mICategoryPresenter != null) {
            mICategoryPresenter.unSubscribe();
        }
    }

    @Override
    public void onRefresh() {
        mICategoryPresenter.getCategoryItems(true);
    }

    @Override
    public void onLoadMore() {
        mICategoryPresenter.getCategoryItems(false);
    }

    @Override
    public void getCategoryItemsFail(String failMessage) {
        if (getUserVisibleHint()) {
            Toasty.error(this.getContext(), failMessage).show();
        }
    }

    @Override
    public void setCategoryItems(List<CategoryResult.ResultsBean> data) {
        mAdapter.setData(data);
    }

    @Override
    public void addCategoryItems(List<CategoryResult.ResultsBean> data) {
        mAdapter.addData(data);

    }

    @Override
    public void showSwipeLoading() {
        mSwipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideSwipeLoading() {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void setLoading() {
        mRecyclerView.setLoading();
    }

    @Override
    public String getCategoryName() {
        return this.categoryName;
    }

    @Override
    public void setNoMore() {
        mRecyclerView.setEnd("没有更多数据");
    }

}
