package com.beecampus.common.viewModel;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.beecampus.model.remote.ApiResponse;
import com.beecampus.model.remote.ResponseTransformer;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/*******************************************************************
 * BasePageViewModel.java  2019/1/3
 * <P>
 * 基础分页列表ViewModel<br/>
 * <br/>
 * </p>
 *
 * @author:zhoupeng
 *
 ******************************************************************/
public abstract class BasePageViewModel<Response, Data> extends BaseViewModel {


    /**
     * 页码
     */
    private int mPageNO;

    /**
     * 每页条数
     */
    private int mPageSize = 20;

    /**
     * 数据源
     */
    private MutableLiveData<List<Data>> mData = new MutableLiveData<>();

    /**
     * 是否有更多数据
     */
    private MutableLiveData<Boolean> mHasMoreData = new MutableLiveData<>();

    /**
     * 总条数
     */
    private MutableLiveData<Integer> mTotalCount = new MutableLiveData<>();

    public BasePageViewModel(@NonNull Application application) {
        super(application);
        // 设置默认值
        mData.setValue(new ArrayList<Data>());
        mHasMoreData.setValue(false);
    }

    /**
     * 加载更多
     *
     * @param pageNO   页码
     * @param pageSize 每页条数
     */
    public abstract Single<ApiResponse<Response>> onLoad(int pageNO, int pageSize);

    /**
     * 从返回包中解析数据
     *
     * @param response 返回包
     * @return 返回包解析结果
     */
    public abstract ListResponse<Data> parseResponse(Response response);


    /**
     * 获取数据
     *
     * @return 数据LiveData
     */
    @NonNull
    public LiveData<List<Data>> getData() {
        return mData;
    }

    /**
     * 是否有更多数据
     *
     * @return 监听是否有更多数据
     */
    @NonNull
    public LiveData<Boolean> hasMoreData() {
        return mHasMoreData;
    }

    /**
     * 获取总条数
     *
     * @return 总条数
     */
    public LiveData<Integer> getTotalCount() {
        return mTotalCount;
    }

    @Override
    public void refreshDataIfNeed() {
        // 如果当前页码为初始状态，且没有正在加载中，则刷新数据
        if (mPageNO == 0 && mRequestObserver.loadStatus != LOADING) {
            loadRefresh();
        }
    }

    /**
     * 刷新加载
     */
    public void loadRefresh() {
        // 重置页码
        mPageNO = 0;
        // 清除列表数据
        mData.getValue().clear();
        mData.setValue(mData.getValue());
        // 加载
        loadMore();
    }

    /**
     * 加载更多
     */
    public void loadMore() {
        // 加载下一页数据
        onLoad(mPageNO + 1, mPageSize)
                .compose(new ResponseTransformer<Response>())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mRequestObserver);
    }

    /**
     * 请求结果监听
     */
    public ViewModelLoadObserver<Response> mRequestObserver = new ViewModelLoadObserver<Response>() {

        @Override
        public void onSuccess(Response response) {
            // 将返回的数据解析后添加到末尾
            List<Data> dataList = mData.getValue();
            ListResponse listResponse = parseResponse(response);
            if (listResponse != null) {
                // 返回列表不为空
                if (listResponse.resultList != null && listResponse.resultList.size() > 0) {
                    // 增加页码
                    mPageNO++;
                    // 将返回数据添加末尾并通知更新
                    dataList.addAll(listResponse.resultList);
                    mData.setValue(dataList);
                }
                mTotalCount.setValue(listResponse.total);
                // 判断是否有更多数据
                if (dataList.size() >= listResponse.total || listResponse.resultList == null ||
                        listResponse.resultList.isEmpty()) {
                    mHasMoreData.setValue(false);
                } else {
                    mHasMoreData.setValue(true);
                }
            }
        }
    };

    public static class ListResponse<Data> {

        /**
         * 总条数，默认取最大值
         */
        public int total = Integer.MAX_VALUE;
        /**
         * 返回列表
         */
        public List<Data> resultList;

        public ListResponse(int total, List<Data> resultList) {
            this.total = total;
            this.resultList = resultList;
        }
    }
}
