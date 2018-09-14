package com.zhiyaya.zhiyayaandroidapplication.widget;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.zhiyaya.zhiyayaandroidapplication.R;

public class MessageDialog extends Dialog {
    LinearLayout ll_title_group;
    TextView tv_title;
    TextView tv_msg;
    Button btn_cancel;
    Button btn_ok;

    private boolean mCanAutoDismiss = true;

    /**
     * Negative Button : Cancel
     */
    public static final int WHICH_BUTTON_NEGATIVE = 0;
    /**
     * Positive Button : OK
     */
    public static final int WHICH_BUTTON_POSITIVE = 1;

    public interface OnButtonClickListener {
        /**
         * @param which : {@link MessageDialog#WHICH_BUTTON_NEGATIVE},
         *              {@link MessageDialog#WHICH_BUTTON_POSITIVE}
         */
        void onClick(int which);
    }

    private OnButtonClickListener mClickListener;

    public MessageDialog(Context context) {
        super(context, R.style.recommend_dialog);
        setContentView(R.layout.widget_dialog_message);
        init();
    }

    private void init() {
        ll_title_group = findViewById(R.id.ll_title_group);
        tv_title = findViewById(R.id.tv_title);
        tv_msg = findViewById(R.id.tv_msg);
        btn_cancel = findViewById(R.id.btn_cancel);
        btn_ok = findViewById(R.id.btn_ok);
        tv_msg.setVisibility(View.GONE);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mClickListener != null) {
                    mClickListener.onClick(WHICH_BUTTON_NEGATIVE);
                }
                if (mCanAutoDismiss) {
                    dismiss();
                }
            }
        });
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mClickListener != null) {
                    mClickListener.onClick(WHICH_BUTTON_POSITIVE);
                }
                if (mCanAutoDismiss) {
                    dismiss();
                }
            }
        });
    }

    public void setCanAutoDismiss(boolean canAutoDismiss) {
        mCanAutoDismiss = canAutoDismiss;
    }

    @Override
    public void setTitle(CharSequence title) {
        if (TextUtils.isEmpty(title)) {
            ll_title_group.setVisibility(View.GONE);
        } else {
            tv_title.setText(title);
            ll_title_group.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void setTitle(int titleId) {
        setTitle(getContext().getString(titleId));
    }

    public void setMessage(CharSequence message) {
        if (TextUtils.isEmpty(message)) {
            tv_msg.setVisibility(View.GONE);
        } else {
            tv_msg.setText(message);
            if (tv_msg.getLineCount() > 2) {
                tv_msg.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.START);
            } else {
                tv_msg.setGravity(Gravity.CENTER_HORIZONTAL);
            }
            tv_msg.setVisibility(View.VISIBLE);
        }
    }

    public void setMessage(CharSequence message, boolean nochange) {
        if (TextUtils.isEmpty(message)) {
            tv_msg.setVisibility(View.GONE);
        } else {
            tv_msg.setText(message);
            tv_msg.setVisibility(View.VISIBLE);
        }
    }

    public void setMessage(int messageId) {
        setMessage(getContext().getString(messageId));
    }

    public void setPositiveButton(String text) {
        btn_ok.setText(text);
    }

    /**
     * set ok btn text
     *
     * @param id
     */
    public void setPositiveButton(int id) {
        btn_ok.setText(id);
    }

    public void setNegativeButton(String text) {
        btn_cancel.setText(text);
    }

    /**
     * set cancel btn text
     *
     * @param id
     */
    public void setNegativeButton(int id) {
        btn_cancel.setText(id);
    }

    public void hideNegativeButton() {
        btn_cancel.setVisibility(View.GONE);
    }

    public void showNegativeButton() {
        btn_cancel.setVisibility(View.VISIBLE);
    }

    public void setOnButtonClickListener(OnButtonClickListener l) {
        mClickListener = l;
    }
}
