package com.beecampus.common.event;


import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.support.annotation.NonNull;

import com.beecampus.common.selectSchool.SelectSchoolResult;
import com.beecampus.common.util.SharedPreferenceUtils;
import com.beecampus.model.local.SchoolDao;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

/*******************************************************************
 * SelectSchoolEvent.java  2018/12/11
 * <P>
 * 选择的学校切换事件<br/>
 * <br/>
 * </p>
 * Copyright2018 by GNNT Company. All Rights Reserved.
 *
 * @author:zhoupeng
 *
 ******************************************************************/
public class SelectSchoolEvent {

    /**
     * 本地选择学校的键
     */
    public static final String LOCAL_SELECT_SCHOOL_KEY = "SelectSchool";

    /**
     * 上下文
     */
    private Context mContext;
    /**
     * 选中学校结果
     */
    private final MutableLiveData<SelectSchoolResult> mSelectSchool = new MutableLiveData<>();

    /**
     * 学校数据库操作类
     */
    public SchoolDao mSchoolDao;

    /**
     * json转换类
     */
    private Gson mGson = new Gson();

    public SelectSchoolEvent(@NonNull Context context, @NonNull SchoolDao schoolDao) {
        this.mContext = context;
        this.mSchoolDao = schoolDao;
        loadLocalSelectSchool();
    }

    /**
     * 初始化本地登录用户
     */
    private void loadLocalSelectSchool() {
        String selectSchoolJson = (String) SharedPreferenceUtils.getParam(mContext, LOCAL_SELECT_SCHOOL_KEY, "");
        SelectSchoolResult result = null;
        try {
            result = mGson.fromJson(selectSchoolJson, SelectSchoolResult.class);
        } catch (JsonSyntaxException e) {
            // 不处理解析失败的情况
        }
        // 解析结果不为空，则通知更新
        if (result != null) {
            mSelectSchool.setValue(result);
        }
    }

    /**
     * 保存学校选择结果
     *
     * @param selectSchoolResult 选择学校结果
     */
    private void saveSelectSchool(SelectSchoolResult selectSchoolResult) {
        SharedPreferenceUtils.setParam(mContext, LOCAL_SELECT_SCHOOL_KEY, mGson.toJson(selectSchoolResult));
    }

    /**
     * 选择学校
     *
     * @param selectSchoolResult 选择结果
     */
    public void selectSchool(SelectSchoolResult selectSchoolResult) {
        mSelectSchool.setValue(selectSchoolResult);
        saveSelectSchool(selectSchoolResult);
    }

    /**
     * 观察学校选择
     *
     * @param owner    生命周期对象
     * @param observer 观察者
     */
    public void observeSelectSchool(LifecycleOwner owner, Observer<SelectSchoolResult> observer) {
        mSelectSchool.observe(owner, observer);
    }
}
