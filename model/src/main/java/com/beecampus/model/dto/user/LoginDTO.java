package com.beecampus.model.dto.user;

/*******************************************************************
 * LoginDTO.java  2018/12/14
 * <P>
 * 登录传输对象<br/>
 * <br/>
 * </p>
 *
 * @author:zhoupeng
 *
 ******************************************************************/
public interface LoginDTO {

    class Request {
        /**
         * 手机号
         */
        public String phone;

        /**
         * 密码
         */
        public String password;
    }

    class Response {
        /**
         * 用户ID
         */
        public int user_id;
        /**
         * 头像
         */
        public String head_img_url;

        /**
         * 昵称
         */
        public String nick_name;

        /**
         * token
         */
        public String token;
    }
}
