package com.beecampus.model;


import android.arch.lifecycle.LiveData;
import android.support.annotation.Nullable;

import com.beecampus.model.dto.GetAllSchoolDTO;
import com.beecampus.model.local.SchoolDao;
import com.beecampus.model.remote.ApiRequest;
import com.beecampus.model.remote.ConfigApi;
import com.beecampus.model.remote.ResponseTransformer;
import com.beecampus.model.remote.http.RetrofitManager;
import com.beecampus.model.vo.Campus;
import com.beecampus.model.vo.SchoolCampus;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/*******************************************************************
 * SchoolRepository.java  2018/12/7
 * <P>
 * 学校相关数据仓库<br/>
 * <br/>
 * </p>
 * Copyright2018 by GNNT Company. All Rights Reserved.
 *
 * @author:zhoupeng
 *
 ******************************************************************/
public class SchoolRepository extends BaseRepository {


    /**
     * 数据库操作类
     */
    private SchoolDao mSchoolDao;

    /**
     * 上次数据更新时间
     */
    private long mLastUpdateTime;

    /**
     * 加载开关
     */
    private Disposable mLoadDisposable;

    public SchoolRepository(RetrofitManager retrofitManager, SchoolDao schoolDao) {
        super(retrofitManager);
        this.mSchoolDao = schoolDao;
    }

    /**
     * 获取所有学校和校区
     *
     * @return 返回学校校区 LiveData
     */
    public LiveData<List<SchoolCampus>> getAllSchoolCampus(@Nullable LoadCallback callback) {
        refreshSchoolIfNeed(callback);
        // 从数据库获取
        return mSchoolDao.getAllSchoolCampus();
    }

    /**
     * 在需要时更新本地数据
     */
    public void refreshSchoolIfNeed(@Nullable final LoadCallback loadCallback) {
        long currentTime = System.currentTimeMillis();
        // 如果当前没有正在加载，且配置已过期，则从远端获取
        if (mLoadDisposable == null && (currentTime - mLastUpdateTime) > ModelConfig.CONFIG_UPDATE_INTERVAL) {
            mRetrofitManager.getApi(ConfigApi.class)
                    .getAllSchools(new ApiRequest("", new GetAllSchoolDTO.Request()))
                    .compose(new ResponseTransformer<GetAllSchoolDTO.Response>())
                    .subscribeOn(Schedulers.newThread()) // 异步执行
                    .map(mSchoolTransformer) // 将返回包转换为本地存储对象
                    .observeOn(Schedulers.io()) // 切换到io线程写数据库
                    .doOnSuccess(new Consumer<List<SchoolCampus>>() {
                        @Override
                        public void accept(List<SchoolCampus> schoolCampusList) throws Exception {
                            // 重置更新时间
                            mLastUpdateTime = System.currentTimeMillis();
                            // 插入数据库
                            mSchoolDao.insertSchoolCampus(schoolCampusList);
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread()) // 在主线程做回调
                    .subscribe(new SingleObserver<List<SchoolCampus>>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            mLoadDisposable = d;
                            if (loadCallback != null) {
                                loadCallback.onLoading();
                            }
                        }

                        @Override
                        public void onSuccess(List<SchoolCampus> schoolCampusList) {
                            if (loadCallback != null) {
                                loadCallback.onComplete();
                            }
                            mLoadDisposable = null;
                        }

                        @Override
                        public void onError(Throwable e) {
                            if (loadCallback != null) {
                                loadCallback.onError(e.getMessage());
                            }
                            mLoadDisposable = null;
                        }
                    });
        }
    }

    /**
     * 将服务端返回的 school 转为本地存储
     */
    public Function<GetAllSchoolDTO.Response, List<SchoolCampus>> mSchoolTransformer = new Function<GetAllSchoolDTO.Response, List<SchoolCampus>>() {
        @Override
        public List<SchoolCampus> apply(GetAllSchoolDTO.Response response) {
            List<SchoolCampus> schoolCampusList = new ArrayList<>();
            if (response.schools != null) {
                for (GetAllSchoolDTO.School school : response.schools) {
                    SchoolCampus schoolCampus = new SchoolCampus();
                    schoolCampus.id = school.id;
                    schoolCampus.name = school.name;
                    schoolCampus.pinyin = school.pinyin;
                    schoolCampus.campusList = new ArrayList<>();
                    if (school.campus != null) {
                        for (GetAllSchoolDTO.Campus campus : school.campus) {
                            Campus dbCampus = new Campus();
                            dbCampus.id = campus.campus_id;
                            dbCampus.schoolId = schoolCampus.id;
                            dbCampus.name = campus.campus_name;
                            dbCampus.address = campus.address;
                            dbCampus.province = campus.province;
                            dbCampus.city = campus.city;
                            dbCampus.township = campus.township;
                            dbCampus.pinyin = campus.pinyin;
                            schoolCampus.campusList.add(dbCampus);
                        }
                    }
                    schoolCampusList.add(schoolCampus);
                }
            }
            return schoolCampusList;
        }
    };
}
