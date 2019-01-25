package com.beecampus.user.resetPassword;

import android.arch.lifecycle.Observer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Button;
import android.widget.EditText;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.beecampus.common.App;
import com.beecampus.common.RouteMap;
import com.beecampus.common.viewModel.BaseActivity;
import com.beecampus.common.util.InputMethodUtils;
import com.beecampus.model.dto.user.SendCaptchaDTO;
import com.beecampus.user.R;
import com.beecampus.user.R2;
import com.beecampus.user.login.LoginViewModel;

import butterknife.BindView;
import butterknife.OnClick;

/*******************************************************************
 * ResetPasswordActivity.java  2018/12/18
 * <P>
 * 重置密码<br/>
 * <br/>
 * </p>
 *
 * @author:zhoupeng
 *
 ******************************************************************/
@Route(path = RouteMap.User.ResetPasswordPage)
public class ResetPasswordActivity extends BaseActivity<ResetPasswordViewModel> {
    /**
     * 手机号输入框
     */
    @BindView(R2.id.edt_phone)
    protected EditText mEdtPhone;

    /**
     * 密码输入框
     */
    @BindView(R2.id.edt_password)
    protected EditText mEdtPassword;

    /**
     * 验证码
     */
    @BindView(R2.id.edt_captcha)
    protected EditText mEdtCaptcha;

    @BindView(R2.id.btn_send_captcha)
    protected Button mBtnSendCaptcha;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_reset_password;
    }

    @NonNull
    @Override
    protected Class<ResetPasswordViewModel> getViewModelClass() {
        return ResetPasswordViewModel.class;
    }

    @Override
    protected void setupViewModel(@Nullable ResetPasswordViewModel viewModel) {
        super.setupViewModel(viewModel);

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
        // 如果修改成功，则关闭页面
        if (status == LoginViewModel.LOAD_COMPLETE && requestCode == LoginViewModel.REQUEST_LOGIN) {
            finish();
        }
    }

    @OnClick(R2.id.btn_send_captcha)
    protected void onSendCaptchaClick() {
        InputMethodUtils.hiddenInputMethod(this, getCurrentFocus());
        // 发送验证码
        String phone = mEdtPhone.getText().toString();
        mViewModel.sendCaptcha(phone, SendCaptchaDTO.TYPE_RESET);
    }

    @OnClick(R2.id.btn_reset_password)
    protected void onResetPasswordClick() {
        String phone = mEdtPhone.getText().toString();
        String password = mEdtPassword.getText().toString();
        String captcha = mEdtCaptcha.getText().toString();
        mViewModel.resetPassword(phone, password, captcha);
    }
}
