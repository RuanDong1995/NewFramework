package com.beecampus.model.remote;

import com.beecampus.model.remote.http.HttpException;

import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.SingleTransformer;
import io.reactivex.functions.Function;

/*******************************************************************
 * ResponseTransformer.java  2018/11/29
 * <P>
 * 请求转换器，用于处理请求异常<br/>
 * <br/>
 * </p>
 *
 * @author:zhoupeng
 *
 ******************************************************************/
public class ResponseTransformer<R> implements SingleTransformer<ApiResponse<R>, R> {


    @Override
    public SingleSource<R> apply(Single<ApiResponse<R>> upstream) {
        return upstream
                // 统一返回网络错误提示
                .onErrorResumeNext(new Function<Throwable, SingleSource<ApiResponse<R>>>() {
                    @Override
                    public SingleSource<ApiResponse<R>> apply(Throwable throwable) throws Exception {
                        return Single.error(new HttpException("网络开小差了"));
                    }
                })
                // 取出body直接分发
                .flatMap(new Function<ApiResponse<R>, SingleSource<R>>() {
                    @Override
                    public SingleSource<R> apply(ApiResponse<R> apiResponse) throws Exception {
                        if (apiResponse.response.retcode < 0) {
                            return Single.error(new HttpException(apiResponse.response.message, apiResponse.response.retcode));
                        }
                        return Single.just(apiResponse.body);
                    }
                });
    }
}
