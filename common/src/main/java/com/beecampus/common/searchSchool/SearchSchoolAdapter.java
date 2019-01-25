package com.beecampus.common.searchSchool;

import android.support.annotation.NonNull;

import com.beecampus.common.R;
import com.beecampus.model.vo.Campus;
import com.beecampus.model.vo.School;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

/*******************************************************************
 * SearchSchoolAdapter.java  2018/12/21
 * <P>
 * 学校搜索适配器<br/>
 * <br/>
 * </p>
 *
 * @author:zhoupeng
 *
 ******************************************************************/
public class SearchSchoolAdapter extends BaseQuickAdapter<SearchSchoolAdapter.SearchItem, BaseViewHolder> {


    public SearchSchoolAdapter() {
        super(R.layout.item_search_school);
    }

    @Override
    protected void convert(BaseViewHolder holder, SearchItem item) {
        holder.setText(R.id.tv_school_campus, String.format("%s %s", item.schoolName, item.campusName));
    }

    /**
     * 搜索项
     */
    public static class SearchItem {

        /**
         * 学校ID
         */
        public long schoolId;

        /**
         * 学校名称
         */
        public String schoolName;

        /**
         * 校区Id
         */
        public long campusId;

        /**
         * 校区名称
         */
        public String campusName;

        public SearchItem(@NonNull School school, @NonNull Campus campus) {
            schoolId = school.id;
            schoolName = school.name;
            campusId = campus.id;
            campusName = campus.name;
        }
    }
}
