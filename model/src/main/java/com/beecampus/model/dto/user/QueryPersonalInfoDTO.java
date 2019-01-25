package com.beecampus.model.dto.user;

/*******************************************************************
 * QueryPersonalInfoDTO.java  2018/12/14
 * <P>
 * 查询个人信息<br/>
 * <br/>
 * </p>
 *
 * @author:zhoupeng
 *
 ******************************************************************/
public interface QueryPersonalInfoDTO {

    class Request {
        /**
         * 用户ID
         */
        public long user_id;
    }

    class Response {
        /**
         * 用户ID
         */
        public int user_id;

        /**
         * 手机号
         */
        public String phone;
        /**
         * 真实姓名
         */
        public String real_name;

        /**
         * 昵称
         */
        public String nick_name;

        /**
         * 是否实名
         */
        public int is_certificate;

        /**
         * 是否注销
         */
        public int is_write_off;

        /**
         * 是否被冻结
         */
        public int is_black;

        /**
         * 头像地址
         */
        public String head_pic;

        /**
         * 学校信息
         */
        public SchoolInfo school;
    }

    class SchoolInfo {
        /**
         * 学校id
         */
        public int school_id;

        /**
         * 学校名称
         */
        public String school;

        /**
         * 校区ID
         */
        public int campus_id;

        /**
         * 校区名称
         */
        public String campus_name;

        /**
         * 校区地址
         */
        public String campus_address;

        /**
         * 省
         */
        public String campus_province;

        /**
         * 市
         */
        public String campus_city;

        /**
         * 区
         */
        public String campus_township;

        /**
         * 是否毕业
         */
        public int is_graduate;
    }
}
