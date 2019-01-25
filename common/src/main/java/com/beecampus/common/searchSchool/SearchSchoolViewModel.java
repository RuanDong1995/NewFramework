package com.beecampus.common.searchSchool;

import android.app.Application;
import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.beecampus.common.viewModel.BaseViewModel;
import com.beecampus.model.LoadCallback;
import com.beecampus.model.SchoolRepository;
import com.beecampus.model.vo.Campus;
import com.beecampus.model.vo.SchoolCampus;

import java.util.ArrayList;
import java.util.List;

/*******************************************************************
 * SearchSchoolViewModel.java  2018/12/21
 * <P>
 * 搜索学校ViewModel<br/>
 * <br/>
 * </p>
 *
 * @author:zhoupeng
 *
 ******************************************************************/
public class SearchSchoolViewModel extends BaseViewModel {
    /**
     * 学校数据仓库
     */
    private SchoolRepository mSchoolRepository;


    /**
     * 搜索关键字
     */
    private MutableLiveData<String> mSearchKey = new MutableLiveData<>();

    /**
     * 搜索结果
     */
    private LiveData<List<SearchSchoolAdapter.SearchItem>> mSearchResult = Transformations.switchMap(mSearchKey, new Function<String, LiveData<List<SearchSchoolAdapter.SearchItem>>>() {
        @Override
        public LiveData<List<SearchSchoolAdapter.SearchItem>> apply(final String key) {
            return Transformations.map(mSchoolRepository.getAllSchoolCampus(null), new Function<List<SchoolCampus>, List<SearchSchoolAdapter.SearchItem>>() {
                @Override
                public List<SearchSchoolAdapter.SearchItem> apply(List<SchoolCampus> input) {
                    return searchWithKey(key, input);
                }
            });
        }
    });

    public SearchSchoolViewModel(@NonNull Application application) {
        super(application);
        mSchoolRepository = getApplication().getRepositoryManager().getSchoolRepository();
    }


    /**
     * 获取搜索结果
     *
     * @return 搜索结果LiveData
     */
    public LiveData<List<SearchSchoolAdapter.SearchItem>> getSearchResult() {
        return mSearchResult;
    }

    /**
     * 设置搜索关键字
     *
     * @param searchKey 搜索关键字
     */
    public void setSearchKey(String searchKey) {
        mSearchKey.setValue(searchKey);
    }

    /**
     * 根据key从列表中搜索返回结果
     *
     * @param key   关键字
     * @param input 列表
     * @return 返回搜索结果
     */
    private List<SearchSchoolAdapter.SearchItem> searchWithKey(String key, List<SchoolCampus> input) {
        List<SearchSchoolAdapter.SearchItem> resultList = new ArrayList<>();
        // 关键字与列表都不为空
        if (!TextUtils.isEmpty(key) && input != null && !input.isEmpty()) {
            // 转换为小写
            key = key.toLowerCase();
            for (SchoolCampus schoolCampus : input) {
                // 如果校区列表为空，则不处理
                if (schoolCampus.campusList == null || schoolCampus.campusList.isEmpty()) {
                    continue;
                }
                // 标记是否学校名称匹配
                boolean schoolNameMatched = false;
                // 如果学校名称或拼音匹配，则将校区全部添加
                if (isMatchedKey(key, schoolCampus.name) || isMatchedKey(key, schoolCampus.pinyin)) {
                    schoolNameMatched = true;
                }
                for (Campus campus : schoolCampus.campusList) {
                    // 如果学校名称匹配，或校区名称与拼音匹配，则加入搜索结果
                    if (schoolNameMatched || isMatchedKey(key, campus.name) || isMatchedKey(key, campus.pinyin)) {
                        SearchSchoolAdapter.SearchItem searchItem = new SearchSchoolAdapter.SearchItem(schoolCampus, campus);
                        resultList.add(searchItem);
                    }
                }
            }
        }
        return resultList;
    }

    /**
     * 内容与key是否匹配
     *
     * @param key     关键字
     * @param content 内容
     * @return true 匹配，false 不匹配
     */
    private boolean isMatchedKey(String key, String content) {
        if (key == null || content == null) {
            return false;
        }
        content = content.toLowerCase();
        return content.toLowerCase().contains(key);
    }

    /**
     * 重新获取学校数据
     */
    public void retryGetSelectSchool() {
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

}
