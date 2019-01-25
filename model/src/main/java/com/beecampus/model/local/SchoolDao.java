package com.beecampus.model.local;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;

import com.beecampus.model.vo.Campus;
import com.beecampus.model.vo.School;
import com.beecampus.model.vo.SchoolCampus;

import java.util.List;

/*******************************************************************
 * SchoolDao.java  2018/12/7
 * <P>
 * School表操作类<br/>
 * <br/>
 * </p>
 * Copyright2018 by GNNT Company. All Rights Reserved.
 *
 * @author:zhoupeng
 *
 ******************************************************************/
@Dao
public abstract class SchoolDao {

    /**
     * 查询所有学校
     *
     * @return 学校
     */
    @Query("SELECT * FROM School")
    public abstract LiveData<List<School>> getAllSchool();

    /**
     * 查询所有校区
     *
     * @return 校区列表
     */
    @Query("SELECT * FROM Campus")
    public abstract LiveData<List<Campus>> getAllCampus();

    /**
     * 查询指定 id 的 school 信息
     *
     * @param id 学校id
     * @return 学校实例LiveData
     */
    @Query("SELECT * FROM School WHERE id = :id")
    public abstract LiveData<School> getSchoolById(int id);


    /**
     * 查询所有学校与校区信息
     *
     * @return 学校
     */
    @Transaction
    @Query("SELECT * FROM School")
    public abstract LiveData<List<SchoolCampus>> getAllSchoolCampus();

    /**
     * 查询指定 id 的 school与campus 信息
     *
     * @param id 学校id
     * @return 学校实例LiveData
     */
    @Transaction
    @Query("SELECT * FROM School WHERE id = :id")
    public abstract LiveData<SchoolCampus> getSchoolCampusById(int id);

    /**
     * 添加学校信息
     *
     * @param school 学校对象
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract long insertSchool(School school);

    /**
     * 添加校区列表
     *
     * @param campusList 校区列表
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertCampus(List<Campus> campusList);

    /**
     * 添加学校信息
     *
     * @param schoolCampusList 学校校区列表对象
     */
    @Transaction
    public void insertSchoolCampus(List<SchoolCampus> schoolCampusList) {
        if (schoolCampusList == null) {
            return;
        }
        // 循环添加所有学校
        for (SchoolCampus schoolCampus : schoolCampusList) {
            insertSchool(schoolCampus);
            insertCampus(schoolCampus.campusList);
        }
    }

    /**
     * 删除学校
     *
     * @param schools 学校对象
     */
    @Delete
    public abstract void deleteSchool(School... schools);
}
