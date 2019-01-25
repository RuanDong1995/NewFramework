package com.beecampus.model.remote;

import com.beecampus.model.dto.user.LoginDTO;
import com.beecampus.model.dto.user.LoginWithMessageDTO;
import com.beecampus.model.dto.user.QueryPersonalInfoDTO;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.POST;

/*******************************************************************
 * UserApi.java  2018/11/29
 * <P>
 * 用户相关API<br/>
 * <br/>
 * </p>
 *
 * @author:zhoupeng
 *
 ******************************************************************/
public interface UserApi {

    /**
     * 注册
     *
     * @param params
     * @return
     */
    @POST("runningman/register")
    Single<ApiResponse<Void>> register(@Body ApiRequest params);

    /**
     * 发送验证码
     *
     * @param params
     * @return
     */
    @POST("runningman/send_captcha")
    Single<ApiResponse<Void>> sendCaptcha(@Body ApiRequest params);


    /**
     * 登录
     *
     * @param params
     * @return
     */
    @POST("/runningman/login")
    Single<ApiResponse<LoginDTO.Response>> login(@Body ApiRequest params);

    /**
     * 使用短信验证登录
     *
     * @param params
     * @return
     */
    @POST("/runningman/login_sms")
    Single<ApiResponse<LoginWithMessageDTO.Response>> loginWithMessage(@Body ApiRequest params);

    /**
     * 重置密码
     *
     * @param params
     * @return
     */
    @POST("/runningman/reset_password")
    Single<ApiResponse<Void>> resetPassword(@Body ApiRequest params);

    /**
     * 编辑个人信息
     *
     * @param params
     * @return
     */
    @POST("/runningman/submit/person_info")
    Single<ApiResponse<Void>> editPersonalInfo(@Body ApiRequest params);

    /**
     * 查询个人信息
     *
     * @param params
     * @return
     */
    @POST("/runningman/get_user_detail")
    Single<ApiResponse<QueryPersonalInfoDTO.Response>> queryPersonalInfo(@Body ApiRequest params);

}
