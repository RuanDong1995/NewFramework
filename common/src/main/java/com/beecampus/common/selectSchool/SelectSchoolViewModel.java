package com.beecampus.common.selectSchool;

import android.app.Application;
import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.beecampus.common.viewModel.BaseViewModel;
import com.beecampus.model.LoadCallback;
import com.beecampus.model.SchoolRepository;
import com.beecampus.model.vo.Campus;
import com.beecampus.model.vo.SchoolCampus;
import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/*******************************************************************
 * SelectSchoolViewModel.java  2018/12/12
 * <P>
 * 选择学校ViewModel<br/>
 * <br/>
 * </p>
 * Copyright2018 by GNNT Company. All Rights Reserved.
 *
 * @author:zhoupeng
 *
 ******************************************************************/
public class SelectSchoolViewModel extends BaseViewModel {

    /**
     * 学校数据仓库
     */
    private SchoolRepository mSchoolRepository;

    /**
     * 学校列表数据
     */
    private LiveData<List<MultiItemEntity>> mSchoolList;

    /**
     * 字母列表
     */
    private LiveData<List<String>> mWordList;

    public SelectSchoolViewModel(@NonNull Application application) {
        super(application);
        mSchoolRepository = getApplication().getRepositoryManager().getSchoolRepository();
        mSchoolList = Transformations.map(mSchoolRepository.getAllSchoolCampus(mSchoolLoadCallback), mMapSchoolData);
        mWordList = Transformations.map(mSchoolList, mMapWordData);
    }

    /**
     * 获取学校选择数据
     *
     * @return 返回学校选择数据
     */
    public LiveData<List<MultiItemEntity>> getSelectSchool() {
        return mSchoolList;
    }

    @Override
    public void refreshDataIfNeed() {
        mSchoolRepository.refreshSchoolIfNeed(mSchoolLoadCallback);
    }

    /**
     * 学校加载回调
     */
    private LoadCallback mSchoolLoadCallback = new LoadCallback() {
        @Override
        public void onLoading() {
            setLoadStatus(LOADING, 0);
        }

        @Override
        public void onComplete() {
            setLoadStatus(LOAD_COMPLETE, 0);
        }

        @Override
        public void onError(String message) {
            setLoadStatus(LOAD_ERROR, 0);
            setMessage(message);
        }
    };

    /**
     * 获取选择首字母
     *
     * @return 选择首字母
     */
    public LiveData<List<String>> getSelectWord() {
        return mWordList;
    }

    /**
     * 用于获取拼音第一个字母
     *
     * @param string 传入字符串
     * @return 返回第一个字母
     */
    public String getFirstChar(String string) {
        String word = "";
        if (!TextUtils.isEmpty(string)) {
            char c = string.charAt(0);
            if (Character.isLowerCase(c) || Character.isUpperCase(c) || Character.isDigit(c)) {
                word = Character.toString(Character.toUpperCase(c));
            }
        }
        return word;
    }


    /**
     * 将学校数据转换为界面数据
     */
    Function<List<SchoolCampus>, List<MultiItemEntity>> mMapSchoolData = new Function<List<SchoolCampus>, List<MultiItemEntity>>() {
        @Override
        public List<MultiItemEntity> apply(List<SchoolCampus> input) {
            // 一级分类是字母
            List<MultiItemEntity> selectWordList = new ArrayList<>();
            if (input != null) {
                // 按拼音排序
                Collections.sort(input);
                // 记录当前操作的字母表
                SelectSchoolAdapter.SelectWord selectWord = null;
                // 记录当前首字母
                for (SchoolCampus schoolCampus : input) {
                    // 如果当前项与当前字母分组不一致，则新建一个字母分组
                    String word = getFirstChar(schoolCampus.pinyin);
                    if (selectWord == null || !TextUtils.equals(word, selectWord.word)) {
                        selectWord = new SelectSchoolAdapter.SelectWord(word);
                        selectWordList.add(selectWord);
                        // 默认展开
                        selectWord.setExpanded(true);
                    }
                    // 新建学校分组
                    SelectSchoolAdapter.SelectSchool selectSchool = new SelectSchoolAdapter.SelectSchool(schoolCampus);
                    if (schoolCampus.campusList != null) {
                        // 按拼音排序
                        Collections.sort(schoolCampus.campusList);
                        // 将校区加入学校分组
                        for (Campus campus : schoolCampus.campusList) {
                            SelectSchoolAdapter.SelectCampus selectCampus = new SelectSchoolAdapter.SelectCampus(campus);
                            selectSchool.addSubItem(selectCampus);
                        }
                    }
                    // 将学校加入字母分组
                    selectWord.addSubItem(selectSchool);
                }
            }

            return selectWordList;
        }
    };

    /**
     * 转换界面数据为关键字
     */
    Function<List<MultiItemEntity>, List<String>> mMapWordData = new Function<List<MultiItemEntity>, List<String>>() {
        @Override
        public List<String> apply(List<MultiItemEntity> input) {
            List<String> wordList = new ArrayList<>();
            if (input != null && input.size() > 0) {
                for (MultiItemEntity entity : input) {
                    if (entity instanceof SelectSchoolAdapter.SelectWord) {
                        wordList.add(((SelectSchoolAdapter.SelectWord) entity).word);
                    }
                }
            }
            return wordList;
        }
    };
}
