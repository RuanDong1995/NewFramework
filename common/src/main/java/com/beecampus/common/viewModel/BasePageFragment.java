package com.beecampus.common.viewModel;

import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/*******************************************************************
 * BasePageFragment.java  2019/1/10
 * <P>
 * 基本分页Fragment<br/>
 * <br/>
 * </p>
 *
 * @author:zhoupeng
 *
 ******************************************************************/
public abstract class BasePageFragment<T extends BasePageViewModel, Data> extends BaseFragment<T> {
    /**
     * 适配器
     */
    protected BaseQuickAdapter<Data, BaseViewHolder> mAdapter;

    @Override
    protected void setupViewModel(@Nullable T viewModel) {
        super.setupViewModel(viewModel);

        // 设置加载更多
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                mViewModel.loadMore();
            }
        });

        // 监听数据改变
        viewModel.getData().observe(this, new Observer<List<Data>>() {
            @Override
            public void onChanged(@Nullable List<Data> infoItems) {
                mAdapter.setNewData(infoItems);
            }
        });

        // 是否有更多数据状态监听
        viewModel.hasMoreData().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean hasMore) {
                // 如果没有数据了，则通知适配器已经没有数据
                if (!hasMore) {
                    mAdapter.loadMoreEnd();
                }
            }
        });
    }

    @Override
    protected void onLoadStatusChanged(int status, int requestCode) {
        if (mAdapter.getItemCount() > 0) {
            if (status == BaseViewModel.LOAD_ERROR) {
                mAdapter.loadMoreFail();
            } else if (status == BaseViewModel.LOAD_COMPLETE) {
                mAdapter.loadMoreComplete();
            }
        }
    }
}
