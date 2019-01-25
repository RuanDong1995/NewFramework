package com.beecampus.user.editInfo;

import android.Manifest;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.beecampus.common.App;
import com.beecampus.common.Constant;
import com.beecampus.common.RouteMap;
import com.beecampus.common.event.LoginUserEvent;
import com.beecampus.common.inputValue.InputValueActivity;
import com.beecampus.common.selectSchool.SelectSchoolActivity;
import com.beecampus.common.selectSchool.SelectSchoolResult;
import com.beecampus.common.util.PermissionUtils;
import com.beecampus.common.viewModel.BaseActivity;
import com.beecampus.common.viewModel.BaseViewModel;
import com.beecampus.user.R;
import com.beecampus.user.R2;
import com.beecampus.user.vo.UserInfo;
import com.bumptech.glide.Glide;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import butterknife.BindView;
import butterknife.OnClick;

/*******************************************************************
 * EditInfoActivity.java  2018/12/18
 * <P>
 * 编辑信息界面<br/>
 * <br/>
 * </p>
 *
 * @author:zhoupeng
 *
 ******************************************************************/
@Route(path = RouteMap.User.EditUserInfoPage)
public class EditInfoActivity extends BaseActivity<EditInfoViewModel> {

    /**
     * 请求昵称
     */
    public static final int REQUEST_NICK_NAME = 1;

    /**
     * 选择学校
     */
    public static final int REQUEST_SELECT_SCHOOL = 2;
    /**
     * 头像
     */
    @BindView(R2.id.img_head)
    protected ImageView mImgHead;
    /**
     * 昵称
     */
    @BindView(R2.id.tv_nick)
    protected TextView mTvNick;

    /**
     * 签名
     */
    @BindView(R2.id.tv_sign)
    protected TextView mTvSign;

    /**
     * 性别
     */
    @BindView(R2.id.layout_sex)
    protected RadioGroup mRgSex;

    /**
     * 学校
     */
    @BindView(R2.id.tv_school)
    protected TextView mTvSchool;

    /**
     * 目前状态
     */
    @BindView(R2.id.tv_status)
    protected TextView mTvStatus;

    /**
     * 目前学历
     */
    @BindView(R2.id.tv_education)
    protected TextView mTvEducation;

    /**
     * 保存按钮
     */
    @BindView(R2.id.btn_save)
    protected Button mBtnSave;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_edit_info;
    }

    @NonNull
    @Override
    protected Class<EditInfoViewModel> getViewModelClass() {
        return EditInfoViewModel.class;
    }

    @Override
    protected void setupViewModel(@Nullable EditInfoViewModel viewModel) {
        super.setupViewModel(viewModel);
        // 监听用户信息加载
        viewModel.getUserInfo().observe(this, new Observer<UserInfo>() {
            @Override
            public void onChanged(@Nullable UserInfo userInfo) {
                initWithUserInfo(userInfo);
            }
        });
    }

    @Override
    protected void onLoadStatusChanged(int status, int requestCode) {
        super.onLoadStatusChanged(status, requestCode);

        if (status == BaseViewModel.LOADING) {
            // 加载时禁用界面操作
            mBtnSave.setEnabled(false);
            setTouchable(false);
        } else if (status == BaseViewModel.LOAD_COMPLETE) {

            // 加载完成，如果是更新信息，则关闭界面
            if (requestCode == EditInfoViewModel.REQUEST_UPDATE_USER_INFO) {
                finish();
            } else {
                // 加载完成开启界面操作
                mBtnSave.setEnabled(true);
                setTouchable(true);
            }
        } else if (status == BaseViewModel.LOAD_ERROR) {
            // 如果是更新用户信息失败，则开启界面操作
            if (requestCode == EditInfoViewModel.REQUEST_UPDATE_USER_INFO) {
                mBtnSave.setEnabled(true);
                setTouchable(true);
            }
        }
    }

    /**
     * 根据用户信息初始化界面
     *
     * @param userInfo 用户信息
     */
    private void initWithUserInfo(UserInfo userInfo) {
        if (userInfo == null) {
            return;
        }
        // 加载头像
        Glide.with(this).load(userInfo.headUrl).into(mImgHead);
        // 昵称
        mTvNick.setText(userInfo.nickName);
    }

    @Override
    protected void onLoginUserChanged(LoginUserEvent.LoginUser loginUser) {
        super.onLoginUserChanged(loginUser);
        if (loginUser != null) {
            mViewModel.setUserID(loginUser.userId);
        } else {
            finish();
        }
    }

    @OnClick(R2.id.layout_head)
    protected void onHeadClick() {
        if (PermissionUtils.hadReadExternalPermission(this) && PermissionUtils.hadWriteExternalPermission(this)) {
            // 跳转头像选择页面
            CropImage.activity()
                    .setMultiTouchEnabled(false) // 禁用多点触控
                    .setGuidelines(CropImageView.Guidelines.ON_TOUCH) // 触摸时有网格
                    .setCropShape(CropImageView.CropShape.RECTANGLE) // 剪裁形状
                    .setActivityTitle(getString(R.string.user_select_head)) // 标题
                    .setRequestedSize(Constant.CONFIG_HEAD_SIZE, Constant.CONFIG_HEAD_SIZE) // 尺寸
                    .setAspectRatio(1, 1) // 长宽比
                    .start(this);
        } else if (PermissionUtils.shouldRequestReadWritePermission(this)) {
            requestPermission(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE});
        } else {
            showMessage(getString(R.string.user_select_image_permission));
        }
    }

    @OnClick(R2.id.layout_nick)
    protected void onNickClick() {
        InputValueActivity.ParamsBuilder builder = new InputValueActivity.ParamsBuilder(this);
        builder.setTitle(getString(R.string.user_setting_nick))
                .setHint(getString(R.string.user_please_input_nick))
                .setDefaultValue(mTvNick.getText().toString())
                .start(this, REQUEST_NICK_NAME);
    }

    @OnClick(R2.id.layout_school)
    protected void onSchoolClick() {
        Intent intent = new Intent(this, SelectSchoolActivity.class);
        startActivityForResult(intent, REQUEST_SELECT_SCHOOL);
    }

    @OnClick(R2.id.btn_save)
    protected void onSaveClick() {
        mViewModel.updateUserInfo();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (result != null && result.getUri() != null) {
                    Glide.with(this).load(result.getUri()).into(mImgHead);
                    // 将选择的头像设置给ViewModel
                    mViewModel.setHeadUri(result.getUri());
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                showMessage(getString(R.string.user_select_image_error));
            }
        } else if (requestCode == REQUEST_NICK_NAME) {
            if (resultCode == RESULT_OK && data != null) {
                String resultNick = InputValueActivity.getResult(data);
                if (!TextUtils.isEmpty(resultNick)) {
                    mViewModel.setNickName(resultNick);
                    mTvNick.setText(resultNick);
                }
            }
        } else if (requestCode == REQUEST_SELECT_SCHOOL) {
            if (resultCode == RESULT_OK && data != null) {
                SelectSchoolResult result = SelectSchoolActivity.getResult(data);
                if (result != null) {
                    mTvSchool.setText(String.format("%s%s", result.schoolName, result.campusName));
                    mViewModel.setSelectSchool(result);
                }
            }
        }
    }
}
