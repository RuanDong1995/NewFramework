package com.beecampus.model.vo;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import static android.arch.persistence.room.ForeignKey.CASCADE;

/*******************************************************************
 * Campus.java  2018/12/10
 * <P>
 * 校区实体类，外键指定当School表内对应school删除时，对应校区也删除<br/>
 * <br/>
 * </p>
 * Copyright2018 by GNNT Company. All Rights Reserved.
 *
 * @author:zhoupeng
 *
 ******************************************************************/
@Entity(foreignKeys = @ForeignKey(
        entity = School.class,
        parentColumns = "id",
        childColumns = "schoolId",
        onDelete = CASCADE),
        indices = {
                @Index("schoolId")})
public class Campus implements Comparable<Campus> {

    @PrimaryKey
    public Long id;

    /**
     * 所属学校 Id
     */
    public Long schoolId;

    /**
     * 校区名称
     */
    public String name;

    /**
     * 校区地址
     */
    public String address;

    /**
     * 省
     */
    public String province;

    /**
     * 市
     */
    public String city;

    /**
     * 区
     */
    public String township;

    /**
     * 名字拼音
     */
    public String pinyin;

    @Override
    public String toString() {
        return "Campus{" +
                "url='" + name + '\'' +
                '}';
    }

    @Override
    public int compareTo(@NonNull Campus o) {
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
