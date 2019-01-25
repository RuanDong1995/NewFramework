package com.beecampus.model.remote;

/*******************************************************************
 * ApiResponse.java  2018/11/29
 * <P>
 * 返回包<br/>
 * <br/>
 * </p>
 *
 * @author:zhoupeng
 *
 ******************************************************************/
public class ApiResponse<T> {

    /**
     * 返回包
     */
    public Response response;

    /**
     * 返回内容
     */
    public T body;

    public ApiResponse() {
    }

    public ApiResponse(int retCode, String message) {
        this(retCode, message, null);
    }

    public ApiResponse(int retCode, String message, T body) {
        this.response = new Response(retCode, message);
        this.body = body;
    }

    public static class Response {

        /**
         * 返回码
         */
        public int retcode;

        /**
         * 错误信息
         */
        public String message;

        public Response(int retcode, String message) {
            this.retcode = retcode;
            this.message = message;
        }
    }
}
