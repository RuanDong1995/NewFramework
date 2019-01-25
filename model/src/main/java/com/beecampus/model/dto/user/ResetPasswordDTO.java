package com.beecampus.model.dto.user;

/*******************************************************************
 * ResetPasswordDTO.java  2018/12/14
 * <P>
 * 重置密码<br/>
 * <br/>
 * </p>
 *
 * @author:zhoupeng
 *
 ******************************************************************/
public interface ResetPasswordDTO {

    class Request{
        /**
         * 手机号
         */
        public String phone;

        /**
         * 密码
         */
        public String password;

        /**
         * 验证码
         */
        public String captcha;
    }
}
