package com.beecampus.user;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.beecampus.common.Constant;
import com.beecampus.common.viewModel.BaseViewModel;
import com.beecampus.model.dto.user.SendCaptchaDTO;
import com.beecampus.model.remote.ApiRequest;
import com.beecampus.model.remote.ResponseTransformer;
import com.beecampus.model.remote.UserApi;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/*******************************************************************
 * CaptchaViewModel.java  2018/12/17
 * <P>
 * 验证码ViewModel<br/>
 * <br/>
 * </p>
 *
 * @author:zhoupeng
 *
 ******************************************************************/
public class CaptchaViewModel extends BaseViewModel {
    /**
     * 用户服务端接口
     */
    protected UserApi mUserApi;

    /**
     * 验证码倒计时
     */
    protected MutableLiveData<Integer> mCaptchaCountdown = new MutableLiveData<>();


    public CaptchaViewModel(@NonNull Application application) {
        super(application);
        mUserApi = getApplication().getRetrofitManager().getApi(UserApi.class);
    }

    /**
     * 验证码倒计时
     *
     * @return 倒计时LiveData
     */
    public LiveData<Integer> getCaptchaCountdown() {
        return mCaptchaCountdown;
    }

    /**
     * 发送验证码
     *
     * @param phone 手机号
     * @param type  类型，详细见{@link SendCaptchaDTO}
     */
    public void sendCaptcha(String phone, String type) {
        // 如果已经是加载中，则本次不处理
        if (mSendCaptchaObserver.loadStatus == LOADING) {
            return;
        }

        // 手机号为空
        if (TextUtils.isEmpty(phone)) {
            setMessage(R.string.user_input_phone_hint);
            return;
        }

        // 请求包
        SendCaptchaDTO.Request params = new SendCaptchaDTO.Request();
        params.phone = phone;
        params.type = type;
        ApiRequest request = new ApiRequest(params);

        // 调用发送验证码
        mUserApi.sendCaptcha(request)
                .compose(new ResponseTransformer<Void>())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mSendCaptchaObserver);
    }

    /**
     * 发送验证码监听
     */
    private ViewModelLoadObserver<Void> mSendCaptchaObserver = new ViewModelLoadObserver<Void>() {

        @Override
        public void onSuccess(Void aVoid) {
            super.onSuccess(aVoid);
            // 提示验证码已发送
            setMessage(R.string.user_already_send_captcha);
            // 开启倒计时
            startCountDown(Constant.MESSAGE_COUNTDOWN);
        }
    };

    /**
     * 开启倒计时
     */
    public void startCountDown(final int countdown) {
        Observable.interval(0, 1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<Long, Integer>() {
                    @Override
                    public Integer apply(Long aLong) {
                        return countdown - aLong.intValue();
                    }
                })
                .take(countdown + 1)
                .subscribe(mCountdownObservable);
    }

    /**
     * 倒计时监听
     */
    private Observer<Integer> mCountdownObservable = new Observer<Integer>() {
        @Override
        public void onSubscribe(Disposable d) {
            addDisposable(d);
        }

        @Override
        public void onNext(Integer countdown) {
            mCaptchaCountdown.setValue(countdown);
        }

        @Override
        public void onError(Throwable e) {
        }

        @Override
        public void onComplete() {
        }
    };
}
