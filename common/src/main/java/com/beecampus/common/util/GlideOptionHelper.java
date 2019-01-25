package com.beecampus.common.util;


import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

/*******************************************************************
 * GlideOptionHelper.java  2018/11/30
 * <P>
 * 图片加载进一步的封装，便于切换图片加载库<br/>
 * <br/>
 * </p>
 *
 * @author:zhoupeng
 *
 ******************************************************************/
public final class GlideOptionHelper {

    /**
     * 配置项
     */
    static RequestOptions mOptions = new RequestOptions();

    static {
        mOptions.fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
    }


    /**
     * 初始化占位符
     *
     * @param loading 加载中
     * @param error   错误
     */
    public static void initPlaceHolder(int loading, int error) {
        mOptions.placeholder(loading).error(error);
    }

    /**
     * 获取默认配置类
     *
     * @return 返回默认配置类
     */
    public static RequestOptions option() {
        return mOptions;
    }

    /**
     * 获取配置类，从默认配置克隆
     *
     * @return 配置类
     */
    public static RequestOptions newOption() {
        return option().clone();
    }

}
