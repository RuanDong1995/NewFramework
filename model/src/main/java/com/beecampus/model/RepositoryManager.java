package com.beecampus.model;


import com.beecampus.model.local.CacheDatabase;
import com.beecampus.model.remote.http.RetrofitManager;

/*******************************************************************
 * RepositoryManager.java  2018/12/11
 * <P>
 * 数据仓库管理类<br/>
 * <br/>
 * </p>
 * Copyright2018 by GNNT Company. All Rights Reserved.
 *
 * @author:zhoupeng
 *
 ******************************************************************/
public class RepositoryManager {



    /**
     * 学校数据仓库
     */
    private SchoolRepository mSchoolRepository;
    /**
     * @param database        数据库
     * @param retrofitManager 网络连接
     */
    public RepositoryManager(CacheDatabase database, RetrofitManager retrofitManager) {
        mSchoolRepository = new SchoolRepository(retrofitManager, database.schoolDao());
    }

    /**
     * 获取学校数据仓库
     *
     * @return 返回学校数据仓库
     */
    public SchoolRepository getSchoolRepository() {
        return mSchoolRepository;
    }

}
