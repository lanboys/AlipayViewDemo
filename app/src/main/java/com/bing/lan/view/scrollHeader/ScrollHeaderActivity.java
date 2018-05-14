package com.bing.lan.view.scrollHeader;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.bing.lan.view.R;
import com.bing.lan.view.utils.LogUtil;

public class ScrollHeaderActivity extends AppCompatActivity {

    protected final LogUtil log = LogUtil.getLogUtil(getClass(), LogUtil.LOG_VERBOSE);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll_header);
    }

    public void onHeadClick(View view) {

    }
}
