package com.beecampus.common.selectSchool;

import android.os.Parcel;
import android.os.Parcelable;

/*******************************************************************
 * SelectSchoolResult.java  2018/12/20
 * <P>
 * 选择学校结果<br/>
 * <br/>
 * </p>
 * Copyright2018 by GNNT Company. All Rights Reserved.
 *
 * @author:zhoupeng
 *
 ******************************************************************/
public class SelectSchoolResult implements Parcelable {
    /**
     * 学校ID
     */
    public long schoolId;

    /**
     * 校区ID
     */
    public long campusId;

    /**
     * 学校名称
     */
    public String schoolName;

    /**
     * 校区名称
     */
    public String campusName;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.schoolId);
        dest.writeLong(this.campusId);
        dest.writeString(this.schoolName);
        dest.writeString(this.campusName);
    }

    public SelectSchoolResult() {
    }

    protected SelectSchoolResult(Parcel in) {
        this.schoolId = in.readLong();
        this.campusId = in.readLong();
        this.schoolName = in.readString();
        this.campusName = in.readString();
    }

    public static final Parcelable.Creator<SelectSchoolResult> CREATOR = new Parcelable.Creator<SelectSchoolResult>() {
        @Override
        public SelectSchoolResult createFromParcel(Parcel source) {
            return new SelectSchoolResult(source);
        }

        @Override
        public SelectSchoolResult[] newArray(int size) {
            return new SelectSchoolResult[size];
        }
    };
}