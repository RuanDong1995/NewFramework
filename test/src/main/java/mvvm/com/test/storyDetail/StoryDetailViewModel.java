package mvvm.com.test.storyDetail;

import android.app.Application;
import android.support.annotation.NonNull;

import com.beecampus.common.viewModel.BaseViewModel;
import com.beecampus.model.ModelConfig;

import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import mvvm.com.test.vo.ConfigUrl;
import mvvm.com.test.vo.StoryDetailEntity;
import mvvm.com.test.vo.ZhiHuApi;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class StoryDetailViewModel extends BaseViewModel {

    private Retrofit mRetrofit;

    private StoryDetailEntity mStoryDetail;

    public StoryDetailViewModel(@NonNull Application application) {
        super (application);
    }

    private void initRetrofit() {
        // 初始化 OKHttp
        OkHttpClient.Builder builder = new OkHttpClient.Builder ();
        OkHttpClient client = builder
                .addInterceptor (new HttpLoggingInterceptor ().setLevel (HttpLoggingInterceptor.Level.BODY))
                .connectTimeout (ModelConfig.REQUEST_TIMEOUT, TimeUnit.SECONDS)
                .retryOnConnectionFailure (true)
                .build ();

        // 初始化 Retrofit
        mRetrofit = new Retrofit.Builder ()
                .client (client)
                .baseUrl (ConfigUrl.BASE_URL)
                .addCallAdapterFactory (RxJava2CallAdapterFactory.create ())
                .addConverterFactory (GsonConverterFactory.create ())
                .build ();
    }

    public void getStoryDetail(String id) {
        initRetrofit ();
        mRetrofit.create (ZhiHuApi.class).getNewsDetails (id)
                .subscribeOn (Schedulers.io ())//指定被观察着所在的线程
                .observeOn (AndroidSchedulers.mainThread ())//指定观察者所在的线程
                .subscribe (new DisposableObserver<StoryDetailEntity> () {
                    @Override
                    public void onNext(StoryDetailEntity storyDetailEntity) {
                        mStoryDetail = storyDetailEntity;
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        loadWebViewListener.loadDetail (mStoryDetail);
                    }
                });
    }

    private OnLoadWebViewListener loadWebViewListener;

    public void setLoadWebViewListener(OnLoadWebViewListener loadWebViewListener) {
        this.loadWebViewListener = loadWebViewListener;
    }

    public interface OnLoadWebViewListener {

        void loadDetail(StoryDetailEntity storyDetailEntity);
    }

}
