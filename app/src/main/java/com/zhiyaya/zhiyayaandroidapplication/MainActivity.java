package com.zhiyaya.zhiyayaandroidapplication;

import android.os.Bundle;
import com.lemon.support.Base.BaseActivity;
import com.zhiyaya.zhiyayaandroidapplication.base.ZhiyayaBaseActivity;

public class MainActivity extends ZhiyayaBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
