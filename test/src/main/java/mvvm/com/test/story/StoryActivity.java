package mvvm.com.test.story;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.beecampus.common.viewModel.BaseActivity;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import mvvm.com.test.R;
import mvvm.com.test.R2;
import mvvm.com.test.storyDetail.StoryDetailActivity;

public class StoryActivity extends BaseActivity<StoryViewModel> {

    @BindView (R2.id.rv_stories)
    RecyclerView rvStories;

    private StoryAdapter mStoryAdapter;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_story;
    }

    @NonNull
    @Override
    protected Class<StoryViewModel> getViewModelClass() {
        return StoryViewModel.class;
    }


    @Override
    protected void setupView() {
        super.setupView ();
        mStoryAdapter = new StoryAdapter (this);
        rvStories.setLayoutManager (new LinearLayoutManager (this));
        rvStories.setAdapter (mStoryAdapter);
        mStoryAdapter.bindToRecyclerView (rvStories);

    }

    @Override
    protected void setupViewModel(@Nullable final StoryViewModel viewModel) {
        super.setupViewModel (viewModel);
        viewModel.setOnCompleteListener (new StoryViewModel.OnDataCompleteListener () {
            @Override
            public void onComplete(List<StoryAdapter.StoryItem> list) {
                mStoryAdapter.setNewData (list);
            }
        });

        mStoryAdapter.setOnItemClickListener (new BaseQuickAdapter.OnItemClickListener () {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                StoryAdapter.StoryItem item = mStoryAdapter.getItem (position);
                Intent intent = new Intent ();
                intent.putExtra ("id",item.id);
                intent.setClass (StoryActivity.this,StoryDetailActivity.class);
                startActivity (intent);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        ButterKnife.bind (this);
    }
}
