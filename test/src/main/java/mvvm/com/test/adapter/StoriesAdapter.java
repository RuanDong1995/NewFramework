package mvvm.com.test.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

import mvvm.com.test.vo.StoryEntity;

public class StoriesAdapter extends BaseMultiItemQuickAdapter<StoryEntity, BaseViewHolder> {

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public StoriesAdapter(List<StoryEntity> data) {
        super (data);
    }

    @Override
    protected void convert(BaseViewHolder helper, StoryEntity item) {

    }

    @Override
    public void setNewData(@Nullable List<StoryEntity> data) {
        super.setNewData (data);
    }
}
