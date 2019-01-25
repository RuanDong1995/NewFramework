package mvvm.com.test.vo;


import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ZhiHuApi {
    @GET ("/api/4/news/latest")
    Observable<StoryEntity> getLatestNews();

    //传入id查看详细信息
    @GET ("/api/4/news/{id}")
    Observable<StoryDetailEntity> getNewsDetails(@Path ("id") String id);
}
