package com.beecampus.model.dto.user;

/*******************************************************************
 * SendCaptchaDTO.java  2018/11/29
 * <P>
 * 发送短信验证码请求<br/>
 * <br/>
 * </p>
 *
 * @author:zhoupeng
 *
 ******************************************************************/
public interface SendCaptchaDTO {

    /**
     * 注册
     */
    String TYPE_REGISTER = "0";

    /**
     * 登录
     */
    String TYPE_LOGIN = "1";

    /**
     * 重置密码
     */
    String TYPE_RESET = "2";

    class Request {

        /**
         * 手机号
         */
        public String phone;

        /**
         * 类型，0 注册，1 登录，2 重置
         */
        public String type;
    }
}
