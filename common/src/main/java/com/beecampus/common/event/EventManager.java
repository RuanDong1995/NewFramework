package com.beecampus.common.event;

import android.content.Context;

import com.beecampus.model.local.CacheDatabase;

/*******************************************************************
 * EventManager.java  2018/12/11
 * <P>
 * 事件管理类，用来管理一些全局事件<br/>
 * <br/>
 * </p>
 * Copyright2018 by GNNT Company. All Rights Reserved.
 *
 * @author:zhoupeng
 *
 ******************************************************************/
public class EventManager {

    /**
     * 登录用户事件
     */
    private LoginUserEvent mLoginUserEvent;

    /**
     * 学校选择事件
     */
    private SelectSchoolEvent mSelectSchoolEvent;

    /**
     * 网络状态事件
     */
    private NetworkStatusEvent mNetworkStatusEvent;

    public EventManager(Context context, CacheDatabase database) {
        // 初始化登录用户事件类
        mLoginUserEvent = new LoginUserEvent(context);

        // 初始化选择学校事件类
        mSelectSchoolEvent = new SelectSchoolEvent(context, database.schoolDao());

        // 初始化网络状态事件类
        mNetworkStatusEvent = new NetworkStatusEvent(context);
    }

    /**
     * 登录用户事件
     *
     * @return 登录用户事件类
     */
    public LoginUserEvent getLoginUserEvent() {
        return mLoginUserEvent;
    }

    /**
     * 选择学校事件
     *
     * @return 选择学校事件类
     */
    public SelectSchoolEvent getSelectSchoolEvent() {
        return mSelectSchoolEvent;
    }

    /**
     * 获取网络状态事件类
     *
     * @return 网络状态事件
     */
    public NetworkStatusEvent getNetworkStatusEvent() {
        return mNetworkStatusEvent;
    }
}
