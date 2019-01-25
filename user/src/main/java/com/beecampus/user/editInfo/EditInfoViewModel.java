package com.beecampus.user.editInfo;

import android.app.Application;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.beecampus.common.App;
import com.beecampus.common.selectSchool.SelectSchoolResult;
import com.beecampus.model.dto.user.EditPersonalInfoDTO;
import com.beecampus.model.remote.ResponseTransformer;
import com.beecampus.user.QueryUserInfoViewModel;
import com.beecampus.user.R;
import com.beecampus.user.vo.UserInfo;

import java.util.Collections;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/*******************************************************************
 * EditInfoViewModel.java  2018/12/17
 * <P>
 * 编辑信息ViewModel<br/>
 * <br/>
 * </p>
 *
 * @author:zhoupeng
 *
 ******************************************************************/
public class EditInfoViewModel extends QueryUserInfoViewModel {


    /**
     * 请求更新用户信息
     */
    public static final int REQUEST_UPDATE_USER_INFO = 1;

    /**
     * 当前选择的头像
     */
    protected Uri mHeadUri;

    public EditInfoViewModel(@NonNull Application application) {
        super(application);
    }


    /**
     * 设置选择的头像Uri
     *
     * @param headUri 头像Uri
     */
    public void setHeadUri(Uri headUri) {
        this.mHeadUri = headUri;
    }

    /**
     * 设置昵称
     *
     * @param nick 昵称
     */
    public void setNickName(String nick) {
        UserInfo userInfo = getUserInfo().getValue();
        if (userInfo != null) {
            userInfo.nickName = nick;
        }
    }

    /**
     * 设置选择的学校
     *
     * @param result 学校选择结果
     */
    public void setSelectSchool(SelectSchoolResult result) {
        UserInfo userInfo = getUserInfo().getValue();
        if (userInfo != null) {
            userInfo.schoolId = result.schoolId;
            userInfo.schoolName = result.schoolName;
            userInfo.campusId = result.campusId;
            userInfo.campusName = result.campusName;
        }
    }

    /**
     * 提交用户信息
     */
    public void updateUserInfo() {
        // 如果已经是加载中，则本次不处理
        if (mEditUserInfoObserver.loadStatus == LOADING) {
            setLoadStatus(LOADING, mEditUserInfoObserver.getRequestCode());
            return;
        }
        // 获取当前用户信息
        final UserInfo userInfo = getUserInfo().getValue();
        if (userInfo == null) {
            return;
        }
        Single<Void> updateSingle;
        if (mHeadUri != null) {
            // 先上传图片，再更新用户信息
            updateSingle = upload(Collections.singletonList(mHeadUri))
                    .flatMap(new Function<List<String>, SingleSource<Void>>() {
                        @Override
                        public SingleSource<Void> apply(List<String> strings) throws Exception {
                            // 将上传成功的图片赋值给用户信息
                            if (strings.size() > 0) {
                                userInfo.headUrl = strings.get(0);
                            }
                            return postUserInfo(userInfo);
                        }
                    });
        } else {
            // 直接更新用户信息
            updateSingle = postUserInfo(userInfo);
        }

        // 执行任务
        updateSingle.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mEditUserInfoObserver);
    }

    /**
     * 更新用户信息
     *
     * @param userInfo 用户信息
     * @return
     */
    private Single<Void> postUserInfo(UserInfo userInfo) {
        // 创建请求包
        EditPersonalInfoDTO.Request params = new EditPersonalInfoDTO.Request();
        params.nick_name = userInfo.nickName;
        params.head_pic = userInfo.headUrl;
        params.school = new EditPersonalInfoDTO.EditSchool();
        params.school.campus_id = userInfo.campusId;

        return mUserApi.editPersonalInfo(getTokenRequest(params))
                .doOnSuccess(getApplication().getLoginInvalidFilter())
                .compose(new ResponseTransformer<Void>());
    }

    /**
     * 编辑用户信息监听
     */
    private ViewModelLoadObserver<Void> mEditUserInfoObserver = new ViewModelLoadObserver<Void>() {

        @Override
        public void onSuccess(Void aVoid) {
            super.onSuccess(aVoid);
            setMessage(R.string.user_save_success);
        }

        @Override
        public int getRequestCode() {
            return REQUEST_UPDATE_USER_INFO;
        }
    };
}
