package com.zhiyaya.zhiyayaandroidapplication.base;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import com.lemon.support.Base.BaseActivity;
import com.zhiyaya.zhiyayaandroidapplication.R;
import com.zhiyaya.zhiyayaandroidapplication.widget.LoadingDialog;
import com.zhiyaya.zhiyayaandroidapplication.widget.MessageDialog;

/**
 * Created by yerunjie on 2018/9/14
 *
 * @author yerunjie
 */
public class ZhiyayaBaseActivity extends BaseActivity {
    public static final String ICON_BACK = "&#xe625;";
    public static final String ICON_CLOSE = "&#xe626;";
    public static final String ICON_SET = "&#xe64b;";
    public static final String ICON_OK = "&#xe637;";
    public static final String ICON_DEL = "&#xe645;";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected View getErrorView() {
        return LayoutInflater.from(this).inflate(R.layout.widget_view_error_page, null);
    }

    @Override
    protected boolean needAutoRetry(Object body) {
        return false;
    }

    @Override
    protected Dialog getLoadingDialog() {
        return new LoadingDialog(this);
    }

    @Override
    protected Dialog getRetryDialog(final DialogInterface.OnClickListener onButtonListener, DialogInterface.OnCancelListener onCancelListener) {
        final MessageDialog dialog = new MessageDialog(this);
        dialog.setMessage(R.string.app_dialog_retry_msg);
        dialog.setPositiveButton(R.string.app_dialog_retry_positive_btn);
        dialog.setOnButtonClickListener(new MessageDialog.OnButtonClickListener() {
            @Override
            public void onClick(int which) {
                onButtonListener.onClick(dialog, which);
            }
        });
        dialog.setOnCancelListener(onCancelListener);
        return dialog;
    }
}
