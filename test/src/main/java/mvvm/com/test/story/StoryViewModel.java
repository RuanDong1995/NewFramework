package mvvm.com.test.story;

import android.app.Application;
import android.support.annotation.NonNull;

import com.beecampus.common.viewModel.BaseViewModel;
import com.beecampus.model.ModelConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import mvvm.com.test.vo.ConfigUrl;
import mvvm.com.test.vo.StoryEntity;
import mvvm.com.test.vo.ZhiHuApi;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class StoryViewModel extends BaseViewModel {

    private Retrofit mRetrofit;
    private OnDataCompleteListener listener;

    public StoryViewModel(@NonNull Application application) {
        super (application);
        initRetrofit ();
        getStoryList ();
    }

    private List<StoryAdapter.StoryItem> mStoryList = new ArrayList<> ();


    public void getStoryList() {
        mRetrofit.create (ZhiHuApi.class).getLatestNews ()
                .subscribeOn (Schedulers.io ())//指定被观察着所在的线程
                .observeOn (AndroidSchedulers.mainThread ())//指定观察者所在的线程
                .subscribe (new DisposableObserver<StoryEntity> () {
                    @Override
                    public void onNext(StoryEntity storyEntity) {
                        for (StoryEntity.Stories stories : storyEntity.getStories ()) {
                            StoryAdapter.StoryItem item = new StoryAdapter.StoryItem ();
                            item.title = stories.getTitle ();
                            item.content = stories.getGa_prefix ();
                            item.id = String.valueOf (stories.getId ());
                            item.imgUrl = stories.getImages ().get (0);
                            mStoryList.add (item);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        listener.onComplete (mStoryList);
                    }
                });
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

    public interface OnDataCompleteListener {

        void onComplete(List<StoryAdapter.StoryItem> list);
    }

    public void setOnCompleteListener(OnDataCompleteListener listener) {
        this.listener = listener;
    }


}
