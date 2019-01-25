package mvvm.com.test.storyDetail;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.webkit.WebView;

import com.beecampus.common.viewModel.BaseActivity;

import butterknife.BindView;
import mvvm.com.test.R;
import mvvm.com.test.R2;
import mvvm.com.test.vo.StoryDetailEntity;

public class StoryDetailActivity extends BaseActivity<StoryDetailViewModel> {


    @BindView (R2.id.web_story_detail)
    WebView webStoryDetail;

    private String mStoryId;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_story_detail;
    }

    @NonNull
    @Override
    protected Class<StoryDetailViewModel> getViewModelClass() {
        return StoryDetailViewModel.class;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
    }

    @Override
    protected void setupView() {
        super.setupView ();

    }

    @Override
    protected void setupViewModel(@Nullable StoryDetailViewModel viewModel) {
        super.setupViewModel (viewModel);
        Intent intent = getIntent ();
        mStoryId = intent.getStringExtra ("id");
        viewModel.getStoryDetail (mStoryId);
        viewModel.setLoadWebViewListener (new StoryDetailViewModel.OnLoadWebViewListener () {
            @Override
            public void loadDetail(StoryDetailEntity storyDetailEntity) {
                webStoryDetail.loadData (storyDetailEntity.getBody (), "text/html; charset=UTF-8", null);
            }
        });
    }
}
