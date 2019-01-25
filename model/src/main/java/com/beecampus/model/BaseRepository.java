package com.beecampus.model;

import com.beecampus.model.remote.http.RetrofitManager;

/*******************************************************************
 * BaseRepository.java  2018/12/7
 * <P>
 * 数据仓库类<br/>
 * <br/>
 * </p>
 * Copyright2018 by GNNT Company. All Rights Reserved.
 *
 * @author:zhoupeng
 *
 ******************************************************************/
public class BaseRepository {

    /**
     * 网络请求类，这里相当于远程仓库
     */
    protected RetrofitManager mRetrofitManager;

    public BaseRepository(RetrofitManager retrofitManager) {
        this.mRetrofitManager = retrofitManager;
    }
}
