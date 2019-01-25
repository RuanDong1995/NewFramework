package com.beecampus.common.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatDialog;

import com.beecampus.common.R;

/*******************************************************************
 * ProgressDialog.java  2018/12/18
 * <P>
 * 进度弹窗<br/>
 * <br/>
 * </p>
 *
 * @author:zhoupeng
 *
 ******************************************************************/
public class ProgressDialog extends AppCompatDialog {

    public ProgressDialog(@NonNull Context context) {
        super(context, R.style.NoFrameDialog);
        setContentView(R.layout.dialog_progress);
    }
}
