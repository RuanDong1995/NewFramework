package com.beecampus.model.dto;


import java.util.List;

/*******************************************************************
 * GetAllSchoolDTO.java  2018/12/10
 * <P>
 * 获取所有学校列表请求包<br/>
 * <br/>
 * </p>
 * Copyright2018 by GNNT Company. All Rights Reserved.
 *
 * @author:zhoupeng
 *
 ******************************************************************/
public interface GetAllSchoolDTO {

    class Request {

        /**
         * 查询关键字
         */
        public String keys;
    }

    class Response {
        /**
         * 学校列表
         */
        public List<School> schools;

        @Override
        public String toString() {
            return "Response{" +
                    "schools=" + schools +
                    '}';
        }
    }

    class School {
        /**
         * 学校id
         */
        public long id;

        /**
         * 学校名称
         */
        public String name;

        /**
         * 名字拼音
         */
        public String pinyin;

        /**
         * 校区列表
         */
        public List<Campus> campus;

        @Override
        public String toString() {
            return "EditSchool{" +
                    "url='" + name + '\'' +
                    ", campus=" + campus +
                    '}';
        }
    }

    class Campus {

        /**
         * 校区ID
         */
        public long campus_id;

        /**
         * 校区名称
         */
        public String campus_name;

        /**
         * 地址
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
                    "campus_name='" + campus_name + '\'' +
                    '}';
        }
    }

}
