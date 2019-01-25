package com.beecampus.common.util;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/*******************************************************************
 * InputMethodUtils.java  2018/12/4
 * <P>
 * 输入法工具类<br/>
 * <br/>
 * </p>
 * Copyright2018 by GNNT Company. All Rights Reserved.
 *
 * @author:zhoupeng
 *
 ******************************************************************/
public class InputMethodUtils {

    /**
     * 隐藏输入法
     *
     * @param context   上下文
     * @param focusView 当前文本框
     */
    public static void hiddenInputMethod(Context context, View focusView) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (focusView != null && focusView.getWindowToken() != null) {
            imm.hideSoftInputFromWindow(focusView.getWindowToken(), 0);
        }
    }
}