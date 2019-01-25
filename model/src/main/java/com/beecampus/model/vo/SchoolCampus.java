package com.beecampus.model.vo;

import android.arch.persistence.room.Relation;
import android.support.annotation.NonNull;

import java.util.List;

/*******************************************************************
 * SchoolCampus.java  2018/12/10
 * <P>
 * 学校校区实例<br/>
 * <br/>
 * </p>
 * Copyright2018 by GNNT Company. All Rights Reserved.
 *
 * @author:zhoupeng
 *
 ******************************************************************/
public class SchoolCampus extends School implements Comparable<School>{

    /**
     * 校区列表
     */
    @Relation(parentColumn = "id", entityColumn = "schoolId", entity = Campus.class)
    public List<Campus> campusList;

    @Override
    public String toString() {
        return "SchoolCampus{" +
                "school=" + name +
                ", campusList=" + campusList +
                '}';
    }

    @Override
    public int compareTo(@NonNull School o) {
        String pinyin1 = this.pinyin;
        String pinyin2 = o.pinyin;

        if (pinyin1 == null) {
            pinyin1 = "";
        }
        if (pinyin2 == null) {
            pinyin2 = "";
        }
        return pinyin1.compareTo(pinyin2);
    }
}
