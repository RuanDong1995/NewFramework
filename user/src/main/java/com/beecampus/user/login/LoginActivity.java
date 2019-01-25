package com.beecampus.user.login;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.EditText;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.beecampus.common.App;
import com.beecampus.common.RouteMap;
import com.beecampus.common.viewModel.BaseActivity;
import com.beecampus.common.viewModel.BaseViewModel;
import com.beecampus.common.util.InputMethodUtils;
import com.beecampus.user.R;
import com.beecampus.user.R2;

import butterknife.BindView;
import butterknife.OnClick;

/*******************************************************************
 * LoginActivity.java  2018/12/17
 * <P>
 * 登录界面<br/>
 * <br/>
 * </p>
 *
 * @author:zhoupeng
 *
 ******************************************************************/
@Route(path = RouteMap.User.LoginPage)
public class LoginActivity extends BaseActivity<LoginViewModel> {

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

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_login;
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
    }

    @Override
    protected void onLoadStatusChanged(int status, int requestCode) {
        // 如果是加载中，则显示
        if (status == BaseViewModel.LOADING) {
            showProgress(false);
        } else {
            hideProgress();
        }
        // 如果是登录请求成功，则关闭页面
        if (status == BaseViewModel.LOAD_COMPLETE && requestCode == LoginViewModel.REQUEST_LOGIN) {
            finish();
        }
    }

    @OnClick(R2.id.btn_login)
    protected void onLoginClick() {
        InputMethodUtils.hiddenInputMethod(this, getCurrentFocus());
        String phone = mEdtPhone.getText().toString();
        String password = mEdtPassword.getText().toString();
        mViewModel.login(phone, password);
    }

    @OnClick(R2.id.btn_register)
    protected void onRegisterClick() {
        // 跳转注册页面
        ARouter.getInstance().build(RouteMap.User.RegisterPage).navigation();
    }

    @OnClick(R2.id.btn_login_with_message)
    protected void onLoginWithMessageClick() {
        // 跳转短信登录界面
        ARouter.getInstance().build(RouteMap.User.LoginWithMessagePage).navigation();
    }

    @OnClick(R2.id.btn_forgot_password)
    protected void onForgetPasswordClick() {
        // 跳转忘记密码页面
        ARouter.getInstance().build(RouteMap.User.ResetPasswordPage).navigation();
    }

}
