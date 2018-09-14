package com.zhiyaya.zhiyayaandroidapplication.widget;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import com.zhiyaya.zhiyayaandroidapplication.R;


public class LoadingDialog extends Dialog {

    private TextView tv_load;

    public LoadingDialog(Context context) {
        this(context, R.style.recommend_dialog);
    }

    public LoadingDialog(Context context, int style) {
        super(context, style);
        View contentView = getLayoutInflater().inflate(R.layout.widget_dialog_loading, null);
        tv_load = (TextView) contentView.findViewById(R.id.tvLoad);
        setContentView(contentView);
    }

    public LoadingDialog setMessage(String msg){
        if(TextUtils.isEmpty(msg)){
            tv_load.setVisibility(View.GONE);
            return this;
        }
        tv_load.setText(msg);
        return this;
    }
}
