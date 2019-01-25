package com.beecampus.model.dto.user;

/*******************************************************************
 * EditPersonalInfoDTO.java  2018/12/14
 * <P>
 * 编辑个人信息<br/>
 * <br/>
 * </p>
 *
 * @author:zhoupeng
 *
 ******************************************************************/
public interface EditPersonalInfoDTO {

    class Request {
        /**
         * 真实姓名
         */
        public String real_name;

        /**
         * 昵称
         */
        public String nick_name;

        /**
         * 头像
         */
        public String head_pic;

        /**
         * 身份证
         */
        public String id_no;

        /**
         * 邮箱
         */
        public String email;

        /**
         * 学校信息
         */
        public EditSchool school;

    }

    class EditSchool {
        /**
         * 是否在读
         */
        public int is_last_school;

        /**
         * 是否最后一个校区
         */
        public int recent_campus;

        /**
         * 学号
         */
        public String student_id;

        /**
         * 校区id
         */
        public long campus_id;

        /**
         * 学历
         */
        public String education;
    }
}
