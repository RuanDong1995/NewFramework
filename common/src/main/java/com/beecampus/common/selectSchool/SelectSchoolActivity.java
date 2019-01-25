package com.beecampus.common.selectSchool;


import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.beecampus.common.R;
import com.beecampus.common.R2;
import com.beecampus.common.searchSchool.SearchSchoolActivity;
import com.beecampus.common.viewModel.BaseActivity;
import com.beecampus.common.viewModel.BaseViewModel;
import com.beecampus.common.widget.FastScrollBar;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/*******************************************************************
 * SelectSchoolActivity.java  2018/12/12
 * <P>
 * 选择学校页面<br/>
 * <br/>
 * </p>
 *
 * @author:zhoupeng
 *
 ******************************************************************/
public class SelectSchoolActivity extends BaseActivity<SelectSchoolViewModel> implements FastScrollBar.OnIndexChangeListener {

    /**
     * 选择结果
     */
    public static final String EXTRA_SELECT_RESULT = "selectResult";

    /**
     * 搜索学校请求
     */
    public static final int REQUEST_SEARCH_SCHOOL = 1;

    /**
     * 当前学校
     */
    @BindView(R2.id.tv_current_school)
    protected TextView mTvCurrentSchool;

    /**
     * 学校校区列表
     */
    @BindView(R2.id.rv_school_campus)
    protected RecyclerView mRvSchoolCampus;

    /**
     * 快速滚动控件
     */
    @BindView(R2.id.fast_scroll_bar)
    protected FastScrollBar mFastScrollBar;

    /**
     * 列表适配器
     */
    protected SelectSchoolAdapter mSelectAdapter;
    /**
     * 布局管理器
     */
    protected LinearLayoutManager mLayoutManager;

    /**
     * 列表为空时的ViewHolder
     */
    protected EmptyViewHolder mEmptyHolder;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_select_school;
    }

    @NonNull
    @Override
    protected Class<SelectSchoolViewModel> getViewModelClass() {
        return SelectSchoolViewModel.class;
    }

    @Override
    protected void setupView() {
        // 初始化各个控件
        setupRecyclerView();
        setupFastScrollBar();
    }

    /**
     * 设置列表相关配置
     */
    private void setupRecyclerView() {
        // 初始化适配器
        mSelectAdapter = new SelectSchoolAdapter();
        mSelectAdapter.expandAll();
        mRvSchoolCampus.setAdapter(mSelectAdapter);

        // 初始化布局管理
        mLayoutManager = new LinearLayoutManager(this);
        mRvSchoolCampus.setLayoutManager(mLayoutManager);
        mRvSchoolCampus.setItemAnimator(null);

        // 设置为空显示布局
        mEmptyHolder = new EmptyViewHolder();
        View emptyView = LayoutInflater.from(this).inflate(R.layout.empty_select_school, mRvSchoolCampus, false);
        ButterKnife.bind(mEmptyHolder, emptyView);
        mSelectAdapter.setEmptyView(emptyView);

        // 监听列表点击事件
        mSelectAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                int type = mSelectAdapter.getItemViewType(position);
                if (type == SelectSchoolAdapter.TYPE_SCHOOL) {
                    if (((SelectSchoolAdapter.SelectSchool) mSelectAdapter.getItem(position)).isExpanded()) {
                        mSelectAdapter.collapse(position);
                    } else {
                        mSelectAdapter.expand(position);
                    }
                } else if (type == SelectSchoolAdapter.TYPE_CAMPUS) {
                    // 选择学校返回
                    MultiItemEntity entity = mSelectAdapter.getItem(position);
                    SelectSchoolAdapter.SelectCampus campus = (SelectSchoolAdapter.SelectCampus) entity;
                    SelectSchoolAdapter.SelectSchool school = (SelectSchoolAdapter.SelectSchool) mSelectAdapter.getItem(mSelectAdapter.getParentPosition(entity));
                    // 创建返回结果
                    SelectSchoolResult result = new SelectSchoolResult();
                    result.schoolId = school.id;
                    result.schoolName = school.name;
                    result.campusId = campus.id;
                    result.campusName = campus.name;
                    Intent data = new Intent();
                    data.putExtra(EXTRA_SELECT_RESULT, result);
                    setResult(RESULT_OK, data);
                    finish();
                }
            }
        });
    }

    /**
     * 设置快速滚动栏显示
     */
    private void setupFastScrollBar() {
        mFastScrollBar.setOnIndexChangeListener(this);

    }

    @Override
    protected void setupViewModel(@Nullable SelectSchoolViewModel viewModel) {
        super.setupViewModel(viewModel);

        // 监听当前学校选择
        viewModel.getApplication().getEventManager().getSelectSchoolEvent().observeSelectSchool(this, new Observer<SelectSchoolResult>() {
            @Override
            public void onChanged(@Nullable SelectSchoolResult selectSchoolResult) {
                if (selectSchoolResult != null) {
                    mTvCurrentSchool.setText(getString(R.string.common_select_school_format, selectSchoolResult.schoolName, selectSchoolResult.campusName));
                } else {
                    mTvCurrentSchool.setText(R.string.common_unselect_school);
                }
            }
        });

        // 监听学校列表选择数据
        viewModel.getSelectSchool().observe(this, new Observer<List<MultiItemEntity>>() {
            @Override
            public void onChanged(@Nullable List<MultiItemEntity> selectSchools) {
                mSelectAdapter.setNewData(selectSchools);
            }
        });

        // 监听首字母改变
        viewModel.getSelectWord().observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(@Nullable List<String> strings) {
                mFastScrollBar.setSectionList(strings);
            }
        });
    }

    @Override
    public void onIndexChanged(int currentIndex, String select) {
        // 选中对应位置数据
        List<MultiItemEntity> dataList = mSelectAdapter.getData();
        if (dataList != null) {
            for (int i = 0; i < dataList.size(); i++) {
                MultiItemEntity entity = dataList.get(i);
                if (entity instanceof SelectSchoolAdapter.SelectWord
                        && TextUtils.equals(((SelectSchoolAdapter.SelectWord) entity).word, select)) {
                    mLayoutManager.scrollToPositionWithOffset(i, 0);
                }
            }
        }
    }


    @Override
    protected void onLoadStatusChanged(int status, int requestCode) {
        super.onLoadStatusChanged(status, requestCode);
        if (status == BaseViewModel.LOADING) {
            mEmptyHolder.mBtnRetry.setVisibility(View.INVISIBLE);
            mEmptyHolder.mTvEmpty.setVisibility(View.INVISIBLE);
        } else if (status == BaseViewModel.LOAD_ERROR) {
            mEmptyHolder.mBtnRetry.setVisibility(View.VISIBLE);
            mEmptyHolder.mTvEmpty.setVisibility(View.INVISIBLE);
        } else {
            mEmptyHolder.mBtnRetry.setVisibility(View.INVISIBLE);
            mEmptyHolder.mTvEmpty.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 返回按钮点击
     */
    @OnClick(R2.id.imgBtn_back)
    protected void onBackClick() {
        finish();
    }

    /**
     * 搜索栏点击
     */
    @OnClick(R2.id.tv_search)
    protected void onSearchClick() {
        Intent intent = new Intent(this, SearchSchoolActivity.class);
        startActivityForResult(intent, REQUEST_SEARCH_SCHOOL);
    }


    public static @Nullable
    SelectSchoolResult getResult(Intent data) {
        SelectSchoolResult result = null;

        if (data != null) {
            result = data.getParcelableExtra(EXTRA_SELECT_RESULT);
        }
        return result;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // 如果搜索页面返回，则直接传递给上一页面
        if (requestCode == REQUEST_SEARCH_SCHOOL && resultCode == RESULT_OK && data != null) {
            setResult(RESULT_OK, data);
            finish();
        }
    }

    /**
     * list为空时的ViewHolder
     */
    protected class EmptyViewHolder {

        /**
         * 重试按钮
         */
        @BindView(R2.id.btn_retry)
        Button mBtnRetry;

        /**
         * 为空显示
         */
        @BindView(R2.id.tv_empty)
        TextView mTvEmpty;

        @OnClick(R2.id.btn_retry)
        protected void onRetryClick() {
            mViewModel.refreshDataIfNeed();
        }
    }

}
