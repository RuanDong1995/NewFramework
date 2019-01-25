package com.beecampus.model.vo;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/*******************************************************************
 * EditSchool.java  2018/12/7
 * <P>
 * 学校名称<br/>
 * <br/>
 * </p>
 * Copyright2018 by GNNT Company. All Rights Reserved.
 *
 * @author:zhoupeng
 *
 ******************************************************************/

@Entity
public class School {

    /**
     * 学校id
     */
    @PrimaryKey
    public Long id;

    /**
     * 学校名称
     */
    public String name;

    /**
     * 名字拼音
     */
    public String pinyin;
}
