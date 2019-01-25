package com.beecampus.common.inputValue;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.widget.EditText;

import com.beecampus.common.R;
import com.beecampus.common.R2;
import com.beecampus.common.viewModel.BaseActivity;
import com.beecampus.common.viewModel.BaseViewModel;

import butterknife.BindView;
import butterknife.OnClick;

/*******************************************************************
 * InputValueActivity.java  2018/12/18
 * <P>
 * 编辑单个值的界面<br/>
 * <br/>
 * </p>
 *
 * @author:zhoupeng
 *
 ******************************************************************/
public class InputValueActivity extends BaseActivity {

    /**
     * 传递的参数
     */
    public static final String EXTRA_PARAMS = "extraParams";
    /**
     * 返回输入的值
     */
    public static final String EXTRA_INPUT_VALUE = "inputValue";

    /**
     * 输入文本框
     */
    @BindView(R2.id.edt_value)
    protected EditText mEdtValue;

    /**
     * 参数
     */
    protected ExtraParams mParams;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_input_value;
    }

    @NonNull
    @Override
    protected Class getViewModelClass() {
        return BaseViewModel.class;
    }

    @Override
    protected void setupViewModel(@Nullable BaseViewModel viewModel) {
        super.setupViewModel(viewModel);

        // 解析启动参数
        ExtraParams params = null;
        Intent intent = getIntent();
        if (intent != null) {
            params = intent.getParcelableExtra(EXTRA_PARAMS);
        }
        if (params == null) {
            params = new ParamsBuilder(this).build();
        }
        initWithParams(params);
    }

    /**
     * 根据params初始化界面
     *
     * @param params
     */
    private void initWithParams(@NonNull ExtraParams params) {
        mParams = params;
        mEdtValue.setText(params.defaultValue);
        mEdtValue.setSelection(params.defaultValue.length());
        mEdtValue.setHint(params.hint);
        mEdtValue.setInputType(params.inputType);
        mEdtValue.setFilters(new InputFilter[]{new InputFilter.LengthFilter(params.maxLength)});
    }

    @OnClick(R2.id.btn_complete)
    protected void onCompleteClick() {
        String inputValue = mEdtValue.getText().toString();
        // 如果输入值小于最小位数
        if (inputValue.length() < mParams.minLength) {
            showMessage(getString(R.string.common_input_min_length_format, mParams.minLength));
            return;
        }

        // 如果输入值与传入默认值相同，则标记为 RESULT_CANCELED;
        if (TextUtils.equals(inputValue, mParams.defaultValue)) {
            setResult(RESULT_CANCELED);
        } else {
            // 传入输入值
            Intent resultParams = new Intent();
            resultParams.putExtra(EXTRA_INPUT_VALUE, inputValue);
            setResult(RESULT_OK, resultParams);
        }
        // 关闭页面
        finish();
    }

    /**
     * 获取返回结果
     *
     * @param data 数据
     * @return
     */
    public static @Nullable
    String getResult(@Nullable Intent data) {
        String result = null;
        if (data != null) {
            result = data.getStringExtra(EXTRA_INPUT_VALUE);
        }
        return result;
    }

    /**
     * 参数构建者
     */
    public static class ParamsBuilder {

        /**
         * 当前参数
         */
        private ExtraParams params;

        public ParamsBuilder(@NonNull Context context) {
            params = new ExtraParams();
            // 赋值默认值
            params.title = context.getString(R.string.common_input_default_title);
            params.hint = context.getString(R.string.common_please_input);
            params.defaultValue = "";
            params.inputType = InputType.TYPE_CLASS_TEXT;
            params.minLength = 1;
            params.maxLength = 16;
        }

        public ParamsBuilder setTitle(@NonNull String title) {
            params.title = title;
            return this;
        }

        public ParamsBuilder setHint(@NonNull String hint) {
            params.hint = hint;
            return this;
        }

        public ParamsBuilder setDefaultValue(@NonNull String defaultValue) {
            params.defaultValue = defaultValue;
            return this;
        }

        public ParamsBuilder setInputType(@NonNull int inputType) {
            params.inputType = inputType;
            return this;
        }

        public ParamsBuilder setMinLength(@NonNull int minLength) {
            params.minLength = minLength;
            return this;
        }

        public ParamsBuilder setMaxLength(@NonNull int maxLength) {
            params.maxLength = maxLength;
            return this;
        }

        /**
         * 返回构建的参数
         *
         * @return 参数
         */
        public ExtraParams build() {
            return params;
        }

        /**
         * 启动
         *
         * @param activity    回传的activity
         * @param requestCode 请求码
         */
        public void start(@NonNull Activity activity, int requestCode) {
            Intent intent = new Intent(activity, InputValueActivity.class);
            intent.putExtra(EXTRA_PARAMS, build());
            activity.startActivityForResult(intent, requestCode);
        }
    }

    public static class ExtraParams implements Parcelable {
        /**
         * 标题
         */
        public String title;
        /**
         * 提示语
         */
        public String hint;
        /**
         * 默认值
         */
        public String defaultValue;
        /**
         * 输入类型
         */
        public int inputType;
        /**
         * 最小长度
         */
        public int minLength;
        /**
         * 最大长度
         */
        public int maxLength;


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.title);
            dest.writeString(this.hint);
            dest.writeString(this.defaultValue);
            dest.writeInt(this.inputType);
            dest.writeInt(this.minLength);
            dest.writeInt(this.maxLength);
        }

        public ExtraParams() {
        }

        protected ExtraParams(Parcel in) {
            this.title = in.readString();
            this.hint = in.readString();
            this.defaultValue = in.readString();
            this.inputType = in.readInt();
            this.minLength = in.readInt();
            this.maxLength = in.readInt();
        }

        public static final Parcelable.Creator<ExtraParams> CREATOR = new Parcelable.Creator<ExtraParams>() {
            @Override
            public ExtraParams createFromParcel(Parcel source) {
                return new ExtraParams(source);
            }

            @Override
            public ExtraParams[] newArray(int size) {
                return new ExtraParams[size];
            }
        };
    }
}
