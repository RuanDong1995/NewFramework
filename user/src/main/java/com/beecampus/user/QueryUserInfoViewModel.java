package com.beecampus.user;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.beecampus.common.App;
import com.beecampus.common.viewModel.UploadImageViewModel;
import com.beecampus.model.dto.user.QueryPersonalInfoDTO;
import com.beecampus.model.remote.UserApi;
import com.beecampus.model.remote.ResponseTransformer;
import com.beecampus.user.vo.UserInfo;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/*******************************************************************
 * QueryUserInfoViewModel.java  2018/12/17
 * <P>
 * 查询用户信息ViewModel<br/>
 * <br/>
 * </p>
 *
 * @author:zhoupeng
 *
 ******************************************************************/
public class QueryUserInfoViewModel extends UploadImageViewModel {
    /**
     * 用户服务端接口
     */
    protected UserApi mUserApi;

    /**
     * 用户ID
     */
    protected long mUserID = -1;
    /**
     * 用户信息
     */
    protected MutableLiveData<UserInfo> mUserInfo = new MutableLiveData<>();

    public QueryUserInfoViewModel(@NonNull Application application) {
        super(application);
        mUserApi = getApplication().getRetrofitManager().getApi(UserApi.class);
    }

    /**
     * 获取用户信息
     *
     * @return 用户信息LiveData
     */
    public LiveData<UserInfo> getUserInfo() {
        return mUserInfo;
    }

    @Override
    public void refreshDataIfNeed() {
        // 如果不为空，则直接返回
        if (mUserInfo.getValue() != null && mUserInfo.getValue().userId == mUserID) {
            mUserInfo.setValue(mUserInfo.getValue());
            return;
        }
        // 请求服务器
        QueryPersonalInfoDTO.Request params = new QueryPersonalInfoDTO.Request();
        params.user_id = mUserID;
        // 请求服务端
        mUserApi.queryPersonalInfo(getTokenRequest(params))
                .doOnSuccess(getApplication().getLoginInvalidFilter())
                .compose(new ResponseTransformer<QueryPersonalInfoDTO.Response>())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ViewModelLoadObserver<QueryPersonalInfoDTO.Response>() {
                    @Override
                    public void onSuccess(QueryPersonalInfoDTO.Response response) {
                        super.onSuccess(response);
                        UserInfo userInfo = new UserInfo(mUserID, response);
                        mUserInfo.setValue(userInfo);
                    }
                });
    }

    /**
     * 设置用户ID
     *
     * @param userID 用户ID
     */
    public void setUserID(long userID) {
        this.mUserID = userID;
        refreshDataIfNeed();
    }
}

