package com.beecampus.model.local;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.beecampus.model.vo.Campus;
import com.beecampus.model.vo.School;

/*******************************************************************
 * CacheDatabase.java  2018/12/7
 * <P>
 * 学校数据库操作<br/>
 * <br/>
 * </p>
 * Copyright2018 by GNNT Company. All Rights Reserved.
 *
 * @author:zhoupeng
 *
 ******************************************************************/
@Database(entities = {School.class, Campus.class}, version = 1, exportSchema = false)
public abstract class CacheDatabase extends RoomDatabase {

    /**
     * 返回学校数据库操作类
     *
     * @return
     */
    public abstract SchoolDao schoolDao();
}
