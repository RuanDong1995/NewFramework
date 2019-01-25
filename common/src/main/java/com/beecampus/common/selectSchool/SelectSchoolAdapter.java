package com.beecampus.common.selectSchool;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.beecampus.common.R;
import com.beecampus.model.vo.Campus;
import com.beecampus.model.vo.School;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.AbstractExpandableItem;
import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

/*******************************************************************
 * SelectSchoolAdapter.java  2018/12/12
 * <P>
 * 选择学校列表<br/>
 * <br/>
 * </p>
 * Copyright2018 by GNNT Company. All Rights Reserved.
 *
 * @author:zhoupeng
 *
 ******************************************************************/
public class SelectSchoolAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {

    /**
     * 字母项
     */
    public static final int TYPE_WORD = 0;

    /**
     * 学校项
     */
    public static final int TYPE_SCHOOL = 1;

    /**
     * 校区项
     */
    public static final int TYPE_CAMPUS = 2;

    public SelectSchoolAdapter() {
        super(null);
        addItemType(TYPE_WORD, R.layout.item_select_word);
        addItemType(TYPE_SCHOOL, R.layout.item_select_school);
        addItemType(TYPE_CAMPUS, R.layout.item_select_campus);
    }

    @Override
    protected void convert(final BaseViewHolder holder, final MultiItemEntity item) {
        switch (holder.getItemViewType()) {
            case TYPE_WORD:
                holder.setText(R.id.tv_word, ((SelectWord) item).word);
                break;
            case TYPE_SCHOOL:
                holder.setText(R.id.tv_school_name, ((SelectSchool) item).name);
                break;
            case TYPE_CAMPUS:
                holder.setText(R.id.tv_campus_name, ((SelectCampus) item).name);
                break;
        }
    }

    @Override
    public void setNewData(@Nullable List<MultiItemEntity> data) {
        // 处理默认为展开的数据
        if (data != null) {
            for (int i = data.size() - 1; i >= 0; i--) {
                MultiItemEntity entity = data.get(i);
                if (entity instanceof AbstractExpandableItem) {
                    AbstractExpandableItem item = (AbstractExpandableItem) entity;
                    if (item.isExpanded() && item.getSubItems() != null && item.getSubItems().size() > 0) {
                        data.addAll(i + 1, item.getSubItems());
                    }
                }
            }
        }
        super.setNewData(data);
    }

    /**
     * 字母栏
     */
    public static class SelectWord extends AbstractExpandableItem<SelectSchool> implements MultiItemEntity {

        /**
         * 当前分组字母
         */
        public String word;

        public SelectWord(String word) {
            this.word = word;
        }

        @Override
        public int getItemType() {
            return TYPE_WORD;
        }

        @Override
        public int getLevel() {
            return 0;
        }
    }

    /**
     * 学校栏
     */
    public static class SelectSchool extends AbstractExpandableItem<SelectCampus> implements MultiItemEntity {

        /**
         * 学校ID
         */
        public long id;
        /**
         * 学校名称
         */
        public String name;

        public SelectSchool(@NonNull School school) {
            this.id = school.id;
            this.name = school.name;
        }

        @Override
        public int getLevel() {
            return 1;
        }

        @Override
        public int getItemType() {
            return TYPE_SCHOOL;
        }
    }

    /**
     * 校区选择栏
     */
    public static class SelectCampus implements MultiItemEntity {

        /**
         * 校区ID
         */
        public long id;
        /**
         * 校区名称
         */
        public String name;

        public SelectCampus(@NonNull Campus campus) {
            this.id = campus.id;
            this.name = campus.name;
        }

        @Override
        public int getItemType() {
            return TYPE_CAMPUS;
        }
    }

}
