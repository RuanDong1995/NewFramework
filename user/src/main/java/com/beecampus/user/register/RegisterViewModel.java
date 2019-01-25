package com.beecampus.user.register;

import android.app.Application;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.beecampus.common.App;
import com.beecampus.model.dto.user.RegisterDTO;
import com.beecampus.model.remote.ApiRequest;
import com.beecampus.model.remote.ResponseTransformer;
import com.beecampus.user.CaptchaViewModel;
import com.beecampus.user.R;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/*******************************************************************
 * RegisterViewModel.java  2018/12/17
 * <P>
 * 注册使用的ViewModel<br/>
 * <br/>
 * </p>
 *
 * @author:zhoupeng
 *
 ******************************************************************/
public class RegisterViewModel extends CaptchaViewModel {
    /**
     * 请求注册
     */
    public static int REQUEST_REGISTER = 1;

    public RegisterViewModel(@NonNull Application application) {
        super(application);
    }

    /**
     * 注册
     *
     * @param phone    手机号
     * @param password 密码
     * @param captcha  验证码
     */
    public void register(String phone, String password, String captcha) {
        // 如果已经是加载中，则本次不处理
        if (mRegisterObserver.loadStatus == LOADING) {
            setLoadStatus(LOADING, mRegisterObserver.getRequestCode());
            return;
        }

        // 表单校验未通过，则不处理
        if (!validForm(phone, password, captcha)) {
            return;
        }

        // 请求包
        RegisterDTO.Request params = new RegisterDTO.Request();
        params.phone = phone;
        params.password = password;
        params.captcha = captcha;
        ApiRequest request = new ApiRequest(params);
        // 调用登录
        mUserApi.register(request)
                .compose(new ResponseTransformer<Void>())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mRegisterObserver);
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
     * 注册回调
     */
    private ViewModelLoadObserver<Void> mRegisterObserver = new ViewModelLoadObserver<Void>() {
        @Override
        public int getRequestCode() {
            return REQUEST_REGISTER;
        }
    };

}
