package com.beecampus.model.dto.user;

/*******************************************************************
 * LoginWithMessageDTO.java  2018/12/14
 * <P>
 * 使用短信验证码登录<br/>
 * <br/>
 * </p>
 *
 * @author:zhoupeng
 *
 ******************************************************************/
public interface LoginWithMessageDTO {

    class Request {
        /**
         * 手机号
         */
        public String phone;

        /**
         * 验证码
         */
        public String captcha;
    }

    class Response extends LoginDTO.Response{
    }
}
