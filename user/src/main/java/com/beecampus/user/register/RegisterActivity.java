package com.beecampus.user.register;

import android.arch.lifecycle.Observer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Button;
import android.widget.EditText;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.beecampus.common.App;
import com.beecampus.common.RouteMap;
import com.beecampus.common.viewModel.BaseActivity;
import com.beecampus.common.util.InputMethodUtils;
import com.beecampus.model.dto.user.SendCaptchaDTO;
import com.beecampus.user.R;
import com.beecampus.user.R2;

import butterknife.BindView;
import butterknife.OnClick;

/*******************************************************************
 * RegisterActivity.java  2018/12/17
 * <P>
 * 注册页面<br/>
 * <br/>
 * </p>
 *
 * @author:zhoupeng
 *
 ******************************************************************/
@Route(path = RouteMap.User.RegisterPage)
public class RegisterActivity extends BaseActivity<RegisterViewModel> {

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
        return R.layout.activity_register;
    }

    @NonNull
    @Override
    protected Class<RegisterViewModel> getViewModelClass() {
        return RegisterViewModel.class;
    }

    @Override
    protected void setupViewModel(@Nullable RegisterViewModel viewModel) {
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
        // 注册请求成功
        if (status == RegisterViewModel.LOAD_COMPLETE && requestCode == RegisterViewModel.REQUEST_REGISTER) {
            // 跳转信息完善界面
            ARouter.getInstance().build(RouteMap.User.EditUserInfoPage).navigation();
            // 关闭当前页面
            finish();
        }
    }

    @OnClick(R2.id.btn_send_captcha)
    protected void onSendCaptchaClick() {
        InputMethodUtils.hiddenInputMethod(this, getCurrentFocus());
        // 发送验证码
        String phone = mEdtPhone.getText().toString();
        mViewModel.sendCaptcha(phone, SendCaptchaDTO.TYPE_REGISTER);
    }

    @OnClick(R2.id.btn_register)
    protected void onRegisterClick() {
        String phone = mEdtPhone.getText().toString();
        String password = mEdtPassword.getText().toString();
        String captcha = mEdtCaptcha.getText().toString();
        mViewModel.register(phone, password, captcha);
    }
}
