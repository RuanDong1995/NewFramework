package com.beecampus.user.login;

import android.app.Application;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.beecampus.common.App;
import com.beecampus.common.event.LoginUserEvent;
import com.beecampus.model.dto.user.LoginDTO;
import com.beecampus.model.dto.user.LoginWithMessageDTO;
import com.beecampus.model.remote.ApiRequest;
import com.beecampus.model.remote.ResponseTransformer;
import com.beecampus.user.CaptchaViewModel;
import com.beecampus.user.R;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/*******************************************************************
 * LoginViewModel.java  2018/12/14
 * <P>
 * 登录界面ViewModel<br/>
 * <br/>
 * </p>
 *
 * @author:zhoupeng
 *
 ******************************************************************/
public class LoginViewModel extends CaptchaViewModel {
    /**
     * 请求登录
     */
    public static int REQUEST_LOGIN = 1;

    /**
     * 当前登录手机号
     */
    private String mLastLoginPhone;

    public LoginViewModel(@NonNull Application application) {
        super(application);
    }

    /**
     * 获取上次登录用户
     *
     * @return 上次登录用户手机号
     */
    public String getLastLoginUser() {
        return "";
    }

    /**
     * 登录
     *
     * @param phone    手机号
     * @param password 密码
     */
    public void login(String phone, String password) {
        // 如果已经是加载中，则本次不处理
        if (mLoginObserver.loadStatus == LOADING) {
            setLoadStatus(LOADING, mLoginObserver.getRequestCode());
            return;
        }

        // 手机号为空
        if (TextUtils.isEmpty(phone)) {
            setMessage(R.string.user_input_phone_hint);
            return;
        }

        // 密码为空
        if (TextUtils.isEmpty(password)) {
            setMessage(R.string.user_input_password_hint);
            return;
        }

        // 保存当前登录手机号
        mLastLoginPhone = phone;

        // 请求包
        LoginDTO.Request params = new LoginDTO.Request();
        params.phone = phone;
        params.password = password;
        ApiRequest request = new ApiRequest(params);
        // 调用登录
        mUserApi.login(request)
                .compose(new ResponseTransformer<LoginDTO.Response>())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mLoginObserver);
    }

    /**
     * 短信登录
     *
     * @param phone   手机号
     * @param captcha 验证码
     */
    public void loginWithMsg(String phone, String captcha) {
        // 如果已经是加载中，则本次不处理
        if (mLoginObserver.loadStatus == LOADING) {
            setLoadStatus(LOADING, mLoginObserver.getRequestCode());
            return;
        }

        // 手机号为空
        if (TextUtils.isEmpty(phone)) {
            setMessage(R.string.user_input_phone_hint);
            return;
        }

        // 验证码为空
        if (TextUtils.isEmpty(captcha)) {
            setMessage(R.string.user_input_captcha_hint);
            return;
        }

        // 请求包
        LoginWithMessageDTO.Request params = new LoginWithMessageDTO.Request();
        params.phone = phone;
        params.captcha = captcha;
        ApiRequest request = new ApiRequest(params);
        // 调用登录
        mUserApi.loginWithMessage(request)
                .compose(new ResponseTransformer<LoginWithMessageDTO.Response>())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mLoginObserver);
    }

    /**
     * 登录回调
     */
    private ViewModelLoadObserver<LoginDTO.Response> mLoginObserver = new ViewModelLoadObserver<LoginDTO.Response>() {


        @Override
        public void onSuccess(LoginDTO.Response response) {
            super.onSuccess(response);

            // 登录成功，全局通知
            App app = getApplication();
            LoginUserEvent.LoginUser loginUser = new LoginUserEvent.LoginUser(response.user_id, mLastLoginPhone, response.head_img_url
                    , response.nick_name, response.token);
            app.getEventManager().getLoginUserEvent().logged(loginUser);
        }

        @Override
        public int getRequestCode() {
            return REQUEST_LOGIN;
        }

    };

}
