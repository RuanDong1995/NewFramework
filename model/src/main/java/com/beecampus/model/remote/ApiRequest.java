package com.beecampus.model.remote;


/*******************************************************************
 * ApiRequest.java  2018/11/29
 * <P>
 * UserApi 请求类<br/>
 * <br/>
 * </p>
 *
 * @author:zhoupeng
 *
 ******************************************************************/
public class ApiRequest {

    /**
     * 请求信息
     */
    public Request request;

    /**
     * 请求参数
     */
    public Object body;

    public ApiRequest(Object body) {
        this.request = new Request("0", "");
        this.body = body;
    }

    public ApiRequest(String token, Object body) {
        this.request = new Request("0", token);
        this.body = body;
    }

    public static class Request {
        /**
         * 加密类型
         */
        public String encrypt_type;

        /**
         * token
         */
        public String token;

        public Request(String encrypt_type, String token) {
            this.encrypt_type = encrypt_type;
            this.token = token;
        }
    }

}
