package mvvm.com.test.story;

import android.content.Context;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import mvvm.com.test.R;

public class StoryAdapter extends BaseQuickAdapter<StoryAdapter.StoryItem, BaseViewHolder> {

    public StoryAdapter(Context context) {
        super (R.layout.item_story);
        this.mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, StoryAdapter.StoryItem item) {
        helper.setText (R.id.tv_story_title, item.title);
        Glide.with (mContext).load (item.imgUrl).into ((ImageView) helper.getView (R.id.iv_story_img));
    }


    public static class StoryItem {
        String title;
        String imgUrl;
        String content;
        String id;
    }
}
