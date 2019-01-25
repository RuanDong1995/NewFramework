package com.beecampus.common.searchSchool;

import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.beecampus.common.R;
import com.beecampus.common.R2;
import com.beecampus.common.selectSchool.SelectSchoolActivity;
import com.beecampus.common.selectSchool.SelectSchoolResult;
import com.beecampus.common.viewModel.BaseActivity;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;

/*******************************************************************
 * SearchSchoolActivity.java  2018/12/21
 * <P>
 * 搜索学校列表<br/>
 * <br/>
 * </p>
 *
 * @author:zhoupeng
 *
 ******************************************************************/
public class SearchSchoolActivity extends BaseActivity<SearchSchoolViewModel> {

    /**
     * 关键字文本框
     */
    @BindView (R2.id.edt_search)
    protected EditText mEdtKey;

    /**
     * 搜索结果列表
     */
    @BindView (R2.id.rv_search_result)
    protected RecyclerView mRvSearchResult;

    /**
     * 适配器
     */
    protected SearchSchoolAdapter mAdapter;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_search_school;
    }

    @NonNull
    @Override
    protected Class<SearchSchoolViewModel> getViewModelClass() {
        return SearchSchoolViewModel.class;
    }

    @Override
    protected void setupView() {
        super.setupView ();
        setupRecyclerView ();
    }

    @Override
    protected void setupViewModel(@Nullable SearchSchoolViewModel viewModel) {
        super.setupViewModel (viewModel);
        mViewModel.getSearchResult ().observe (this, new Observer<List<SearchSchoolAdapter.SearchItem>> () {
            @Override
            public void onChanged(@Nullable List<SearchSchoolAdapter.SearchItem> searchItems) {
                mAdapter.setNewData (searchItems);
                // 如果关键字为空，则设置不显示空布局，否则显示
                if(TextUtils.isEmpty (mEdtKey.getText ().toString ())) {
                    mAdapter.getEmptyView ().setVisibility (View.INVISIBLE);
                } else {
                    mAdapter.getEmptyView ().setVisibility (View.VISIBLE);
                }
            }
        });
    }

    /**
     * 设置RecyclerView
     */
    private void setupRecyclerView() {
        // 初始化适配器
        mAdapter = new SearchSchoolAdapter ();
        mRvSearchResult.setAdapter (mAdapter);
        mAdapter.bindToRecyclerView (mRvSearchResult);

        // 初始化布局管理器
        mRvSearchResult.setLayoutManager (new LinearLayoutManager (this));
        mAdapter.setEmptyView (R.layout.empty_search_school, mRvSearchResult);
        mAdapter.getEmptyView ().setVisibility (View.INVISIBLE);

        // 设置列表点击监听
        mAdapter.setOnItemClickListener (new BaseQuickAdapter.OnItemClickListener () {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                // 将选择结果返回上一页
                SearchSchoolAdapter.SearchItem searchItem = mAdapter.getItem (position);
                SelectSchoolResult result = new SelectSchoolResult ();
                result.schoolId = searchItem.schoolId;
                result.campusId = searchItem.campusId;
                result.schoolName = searchItem.schoolName;
                result.campusName = searchItem.campusName;
                Intent data = new Intent ();
                data.putExtra (SelectSchoolActivity.EXTRA_SELECT_RESULT, result);
                setResult (RESULT_OK, data);
                finish ();
            }
        });
    }

    /**
     * 取消按钮点击
     */
    @OnClick (R2.id.btn_cancel)
    protected void onCancelClick() {
        finish ();
    }

    @OnTextChanged (value = R2.id.edt_search, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    protected void onKeyChanged(CharSequence charSequence) {
        mViewModel.setSearchKey (charSequence.toString ());
    }
}
