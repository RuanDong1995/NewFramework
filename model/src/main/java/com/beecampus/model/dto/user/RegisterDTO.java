package com.beecampus.model.dto.user;

/*******************************************************************
 * RegisterDTO.java  2018/11/29
 * <P>
 * 注册请求<br/>
 * <br/>
 * </p>
 *
 * @author:zhoupeng
 *
 ******************************************************************/
public interface RegisterDTO {

    class Request {
        /**
         * 电话
         */
        public String phone;

        /**
         * 验证码
         */
        public String captcha;

        /**
         * 密码
         */
        public String password;
    }
}
