package com.beecampus.model.remote.http;


import com.beecampus.model.ModelConfig;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/*******************************************************************
 * RetrofitInstance.java 2017/4/21
 * <P>
 * Retrofit请求管理类<br/>
 * <br/>
 * </p>
 * Copyright2017 by GNNT CompanyVO. All Rights Reserved.
 *
 * @author:Zhoupeng
 ******************************************************************/
public class RetrofitManager {
    /**
     * 网络请求实例
     */
    private Retrofit mRetrofit;

    /**
     * 保存各个 api 实例
     */
    private Map<String, Object> mApiMap = Collections.synchronizedMap(new HashMap<String, Object>());

    /**
     * 构造方法
     */
    public RetrofitManager() {
        // 初始化 OKHttp
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        OkHttpClient client = builder
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .connectTimeout(ModelConfig.REQUEST_TIMEOUT, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .build();

        // 初始化 Retrofit
        mRetrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(ModelConfig.HOST)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }


    /**
     * 获取 API
     *
     * @return 返回 api 实例
     */
    public <T> T getApi(Class<T> apiClass) {
        Object api = mApiMap.get(apiClass.getName());
        // 如果api实例为空或api类型不是 T
        if (api == null || !api.getClass().equals(apiClass)) {
            api = mRetrofit.create(apiClass);
            mApiMap.put(apiClass.getName(), api);
        }
        return (T) api;
    }

}
