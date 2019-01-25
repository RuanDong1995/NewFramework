package com.beecampus.common.viewModel;


import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.beecampus.common.R;
import com.beecampus.common.util.BitmapConvertUtils;
import com.beecampus.model.dto.UploadImageDTO;
import com.beecampus.model.remote.ConfigApi;
import com.beecampus.model.remote.ResponseTransformer;
import com.bumptech.glide.Glide;

import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.functions.Function;

/*******************************************************************
 * UploadImageViewModel.java  2018/12/19
 * <P>
 * 上传图片使用的ViewModel<br/>
 * <br/>
 * </p>
 *
 * @author:zhoupeng
 *
 ******************************************************************/
public class UploadImageViewModel extends BaseViewModel {

    /**
     * 上传完成的路径
     */
    private MutableLiveData<List<String>> mUploadPath = new MutableLiveData<>();

    /**
     * 配置项api
     */
    protected ConfigApi mConfigApi;

    public UploadImageViewModel(@NonNull Application application) {
        super(application);
        mConfigApi = getApplication().getRetrofitManager().getApi(ConfigApi.class);
    }

    /**
     * 上传图片
     *
     * @param uriList 本地对应Uri列表
     */
    protected Single<List<String>> upload(List<Uri> uriList) {

        // 如果列表为空，则不处理
        if (uriList == null && uriList.isEmpty()) {
            return Single.just(Collections.<String>emptyList());
        }

        return Observable.fromIterable(uriList)
                .map(new Function<Uri, String>() {
                    @Override
                    public String apply(Uri uri) throws Exception {
                        // 直接加载为bitmap
                        Bitmap bitmap = Glide.with(getApplication())
                                .asBitmap().load(uri).submit().get();
                        if (bitmap == null) {
                            throw new RuntimeException(getApplication().getString(R.string.common_get_image_error));
                        }
                        // Bitmap 转换为 BASE64
                        String base64 = BitmapConvertUtils.bitmapToBase64(bitmap);
                        if (TextUtils.isEmpty(base64)) {
                            base64 = "";
                        }
                        return base64;
                    }
                })
                .flatMap(new Function<String, ObservableSource<UploadImageDTO.Response>>() {
                    @Override
                    public ObservableSource<UploadImageDTO.Response> apply(String s) throws Exception {
                        UploadImageDTO.Request params = new UploadImageDTO.Request();
                        params.img_type = "png";
                        params.img_data = s;
                        return mConfigApi.uploadImage(getTokenRequest(params))
                                .doOnSuccess(getApplication().getLoginInvalidFilter())
                                .compose(new ResponseTransformer<UploadImageDTO.Response>()).toObservable();
                    }
                })
                .map(new Function<UploadImageDTO.Response, String>() {
                    @Override
                    public String apply(UploadImageDTO.Response response) throws Exception {
                        return response.img_path;
                    }
                })
                .toList();

    }

}
