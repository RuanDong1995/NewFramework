package com.beecampus.model;

/*******************************************************************
 * LoadCallback.java  2018/12/11
 * <P>
 * 加载回调<br/>
 * <br/>
 * </p>
 * Copyright2018 by GNNT Company. All Rights Reserved.
 *
 * @author:zhoupeng
 *
 ******************************************************************/
public interface LoadCallback {

    /**
     * 加载中
     */
    void onLoading();

    /**
     * 加载完成
     */
    void onComplete();

    /**
     * 加载失败
     */
    void onError(String message);
}
