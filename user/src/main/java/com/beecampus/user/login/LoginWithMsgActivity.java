package com.beecampus.user.login;

import android.arch.lifecycle.Observer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Button;
import android.widget.EditText;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.beecampus.common.App;
import com.beecampus.common.RouteMap;
import com.beecampus.common.viewModel.BaseActivity;
import com.beecampus.common.viewModel.BaseViewModel;
import com.beecampus.common.util.InputMethodUtils;
import com.beecampus.model.dto.user.SendCaptchaDTO;
import com.beecampus.user.R;
import com.beecampus.user.R2;

import butterknife.BindView;
import butterknife.OnClick;

/*******************************************************************
 * LoginWithMsgActivity.java  2018/12/18
 * <P>
 * 短信验证码登录<br/>
 * <br/>
 * </p>
 *
 * @author:zhoupeng
 *
 ******************************************************************/
@Route(path = RouteMap.User.LoginWithMessagePage)
public class LoginWithMsgActivity extends BaseActivity<LoginViewModel> {
    /**
     * 手机号输入框
     */
    @BindView(R2.id.edt_phone)
    protected EditText mEdtPhone;

    /**
     * 密码输入框
     */
    @BindView(R2.id.edt_captcha)
    protected EditText mEdtCaptcha;

    @BindView(R2.id.btn_send_captcha)
    protected Button mBtnSendCaptcha;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_login_with_msg;
    }

    @NonNull
    @Override
    protected Class<LoginViewModel> getViewModelClass() {
        return LoginViewModel.class;
    }

    @Override
    protected void setupViewModel(@Nullable LoginViewModel viewModel) {
        super.setupViewModel(viewModel);
        // 反填最后一次登录的用户
        mEdtPhone.setText(mViewModel.getLastLoginUser());

        // 验证码按钮倒计时
        viewModel.getCaptchaCountdown().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer integer) {
                if (integer > 0) {
                    mBtnSendCaptcha.setText(integer + "s");
                    mBtnSendCaptcha.setEnabled(false);
                } else {
                    mBtnSendCaptcha.setText("发送验证码");
                    mBtnSendCaptcha.setEnabled(true);
                }
            }
        });
    }

    @Override
    protected void onLoadStatusChanged(int status, int requestCode) {
        super.onLoadStatusChanged(status, requestCode);
        // 如果是登录请求成功，则关闭页面
        if (status == BaseViewModel.LOAD_COMPLETE && requestCode == LoginViewModel.REQUEST_LOGIN) {
            finish();
        }
    }

    @OnClick(R2.id.btn_send_captcha)
    protected void onSendCaptchaClick() {
        InputMethodUtils.hiddenInputMethod(this, getCurrentFocus());
        // 发送验证码
        String phone = mEdtPhone.getText().toString();
        mViewModel.sendCaptcha(phone, SendCaptchaDTO.TYPE_LOGIN);
    }

    @OnClick(R2.id.btn_login)
    protected void onLoginClick() {
        InputMethodUtils.hiddenInputMethod(this, getCurrentFocus());
        String phone = mEdtPhone.getText().toString();
        String captcha = mEdtCaptcha.getText().toString();
        mViewModel.loginWithMsg(phone, captcha);
    }


}
