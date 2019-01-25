package com.beecampus.common.event;

import android.arch.lifecycle.MutableLiveData;

/*******************************************************************
 * RealChangeLiveData.java  2018/11/30
 * <P>
 * 真实改变才会触发的LiveData，当set相同值时，不会触发观察者<br/>
 * <br/>
 * </p>
 *
 * @author:zhoupeng
 *
 ******************************************************************/
public class RealChangeLiveData<T> extends MutableLiveData<T> {

    @Override
    public void setValue(T value) {
        // 如果值相同，则不处理
        if (value == getValue()){
            return;
        }
        super.setValue(value);
    }
}
