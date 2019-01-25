package com.beecampus.common;


import android.app.Application;
import android.arch.persistence.room.Room;

import com.alibaba.android.arouter.launcher.ARouter;
import com.beecampus.common.event.EventManager;
import com.beecampus.model.ModelConfig;
import com.beecampus.model.RepositoryManager;
import com.beecampus.model.local.CacheDatabase;
import com.beecampus.model.remote.ApiResponse;
import com.beecampus.model.remote.http.RetrofitManager;
import com.beecampus.model.remote.http.ReturnCode;

import io.reactivex.functions.Consumer;
import io.reactivex.plugins.RxJavaPlugins;

/*******************************************************************
 * App.java  2018/11/29
 * <P>
 * 应用类，所以需要单例管理的对象，都在此处初始化<br/>
 * <br/>
 * </p>
 *
 * @author:zhoupeng
 *
 ******************************************************************/
public class App extends Application {

    /**
     * 单例
     */
    private static App sInstance;

    /**
     * 网络请求管理类
     */
    private RetrofitManager mRetrofitManager;

    /**
     * 数据库管理类
     */
    private CacheDatabase mDatabase;

    /**
     * 数据仓库管理类
     */
    private RepositoryManager mRepositoryManager;

    /**
     * 事件管理类
     */
    private EventManager mEventManager;


    @Override
    public void onCreate() {
        super.onCreate();
        // 保存单例
        sInstance = this;

        // 内存泄露检测
//        if (LeakCanary.isInAnalyzerProcess(this)) {
//            // This process is dedicated to LeakCanary for heap analysis.
//            // You should not init your app in this process.
//            return;
//        }
//
//        // 内存泄露检测
//        LeakCanary.install(this);

        // 设置RxJava执行错误默认处理
        RxJavaPlugins.setErrorHandler(new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) {
                throwable.printStackTrace();
            }
        });

        // ARouter 初始化
        if (BuildConfig.DEBUG) {
            ARouter.openLog();
            ARouter.openDebug(); // 没用这句不能在debug模式运行
        }
        ARouter.init(this);

        // 初始化图片加载展位图
//        GlideOptionHelper.initPlaceHolder(R.drawable.ic_launcher_background, R.drawable.ic_launcher_foreground);

        // 初始化网络请求类
        mRetrofitManager = new RetrofitManager();

        // 初始化数据库管理类
        mDatabase = Room.inMemoryDatabaseBuilder(this, CacheDatabase.class).allowMainThreadQueries().build();

        // 初始化数据仓库管理类
        mRepositoryManager = new RepositoryManager(mDatabase, mRetrofitManager);

        // 初始化事件管理类
        mEventManager = new EventManager(this, mDatabase);
    }

    /**
     * 获取单例
     *
     * @return 返回的是单例
     */
    protected static App getInstance() {
        if (sInstance == null) {
            throw new RuntimeException("Application 还未初始化");
        }
        return sInstance;
    }

    /**
     * 获取网络请求类
     *
     * @return 网络请求实例
     */
    public RetrofitManager getRetrofitManager() {
        return mRetrofitManager;
    }

    /**
     * 获取数据仓库实例
     *
     * @return 数据仓库实例
     */
    public RepositoryManager getRepositoryManager() {
        return mRepositoryManager;
    }

    /**
     * 获取数据库链接实例对象
     *
     * @return 数据库对象
     */
    public CacheDatabase getDatabase() {
        return mDatabase;
    }

    /**
     * @return
     */
    public EventManager getEventManager() {
        return mEventManager;
    }


    /**
     * 登录失效过滤器，用于需要验证用户登录的协议
     *
     * @return 登录失效过滤器
     */
    public Consumer<ApiResponse> getLoginInvalidFilter() {
        return mLoginInvalidFilter;
    }

    /**
     * 登录失效拦截过去器
     */
    private Consumer<ApiResponse> mLoginInvalidFilter = new Consumer<ApiResponse>() {
        @Override
        public void accept(ApiResponse apiResponse) throws Exception {
            if (apiResponse.response.retcode == ReturnCode.TOKEN_INVALID) {
                // 为了避免弹出提示，这里将消息设置为空
                apiResponse.response.message = "";
                // 通知登录用户登录已失效
                mEventManager.getLoginUserEvent().loginInvalid();
            }
        }
    };
}


