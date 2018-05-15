package com.bing.lan.view.scrollHeader;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bing.lan.view.R;
import com.bing.lan.view.utils.LogUtil;

public class ScrollHeaderActivity extends AppCompatActivity implements ScrollHeaderView.OnRectCenterScrollListener {

    protected final LogUtil log = LogUtil.getLogUtil(getClass(), LogUtil.LOG_VERBOSE);
    private ScrollHeaderView mScrollHeaderView;
    private TextView mHeader;
    private ViewGroup.LayoutParams mLayoutParams;
    private NestedScrollView nestedScrollView;

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll_header);

        mScrollHeaderView = (ScrollHeaderView) findViewById(R.id.scroll_header);
        mHeader = (TextView) findViewById(R.id.header);
        nestedScrollView = (NestedScrollView) findViewById(R.id.nestedScrollView);

        mScrollHeaderView.setOnRectCenterScrollListener(this);

        mLayoutParams = mHeader.getLayoutParams();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            nestedScrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    //log.i("onScrollChange(): scrollX: " + scrollX);
                    //log.i("onScrollChange(): scrollY: " + scrollY);
                    ScrollHeaderActivity.this.scrollY = scrollY;
                    //log.i("onScrollChange(): oldScrollX: " + oldScrollX);
                    //log.i("onScrollChange(): oldScrollY: " + oldScrollY);
                }
            });
        }
    }

    int scrollY;

    public void onHeadClick(View view) {

    }

    @Override
    public void onRectCenterScroll(ScrollHeaderView view, float headerKeepHight) {
        mLayoutParams.height = (int) headerKeepHight;
        mHeader.requestLayout();
    }

    @Override
    public boolean isInterceptTouchEvent() {

        return scrollY == 0;
    }
}
