package com.beecampus.user.resetPassword;

import android.app.Application;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.beecampus.common.App;
import com.beecampus.model.dto.user.ResetPasswordDTO;
import com.beecampus.model.remote.ResponseTransformer;
import com.beecampus.user.CaptchaViewModel;
import com.beecampus.user.R;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/*******************************************************************
 * ResetPasswordViewModel.java  2018/12/17
 * <P>
 * 重置密码ViewModel<br/>
 * <br/>
 * </p>
 *
 * @author:zhoupeng
 *
 ******************************************************************/
public class ResetPasswordViewModel extends CaptchaViewModel {

    /**
     * 请求重置密码
     */
    public static int REQUEST_RESET_PASSWORD = 1;

    public ResetPasswordViewModel(@NonNull Application application) {
        super(application);
    }

    /**
     * 注册
     *
     * @param phone    手机号
     * @param password 密码
     * @param captcha  验证码
     */
    public void resetPassword(String phone, String password, String captcha) {
        // 如果已经是加载中，则本次不处理
        if (mResetPasswordObserver.loadStatus == LOADING) {
            setLoadStatus(LOADING,mResetPasswordObserver.getRequestCode());
            return;
        }
        if (!validForm(phone, password, captcha)) {
            return;
        }
        // 请求包
        ResetPasswordDTO.Request params = new ResetPasswordDTO.Request();
        params.phone = phone;
        params.password = password;
        params.captcha = captcha;
        // 调用登录
        mUserApi.resetPassword(getTokenRequest(params))
                .doOnSuccess(getApplication().getLoginInvalidFilter())
                .compose(new ResponseTransformer<Void>())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mResetPasswordObserver);
    }

    /**
     * 校验表单
     *
     * @return 是否通过校验
     */
    private boolean validForm(String phone, String password, String captcha) {
        // 手机号为空
        if (TextUtils.isEmpty(phone)) {
            setMessage(R.string.user_input_phone_hint);
            return false;
        }

        // 密码为空
        if (TextUtils.isEmpty(password)) {
            setMessage(R.string.user_input_password_hint);
            return false;
        }

        // 密码小于6位
        if (password.length() < 6) {
            setMessage(R.string.user_password_length_error);
            return false;
        }

        // 验证码为空
        if (TextUtils.isEmpty(captcha)) {
            setMessage(R.string.user_input_captcha_hint);
            return false;
        }
        return true;
    }

    /**
     * 登录回调
     */
    private ViewModelLoadObserver<Void> mResetPasswordObserver = new ViewModelLoadObserver<Void>() {
        @Override
        public int getRequestCode() {
            return REQUEST_RESET_PASSWORD;
        }
    };
}
