package com.bing.lan.view.scrollHeader;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.bing.lan.view.R;
import com.bing.lan.view.utils.LogUtil;

public class ScrollHeaderActivity extends AppCompatActivity implements
        ScrollHeaderView.OnScrollChangeListener, ScrollHeaderView.OnInterceptTouchEventCallBack {

    protected final LogUtil log = LogUtil.getLogUtil(getClass(), LogUtil.LOG_VERBOSE);
    private ScrollHeaderView mScrollHeaderView;
    private FrameLayout mHeader;
    private LinearLayout ll_container;
    private ViewGroup.LayoutParams mLayoutParams;
    private NestedScrollView nestedScrollView;

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll_header);

        mScrollHeaderView = (ScrollHeaderView) findViewById(R.id.scroll_header);
        mHeader = (FrameLayout) findViewById(R.id.ll_text);
        ll_container = (LinearLayout) findViewById(R.id.ll_container);
        nestedScrollView = (NestedScrollView) findViewById(R.id.nestedScrollView);

        mScrollHeaderView.setOnScrollChangeListener(this);
        mScrollHeaderView.setInterceptTouchEventCallBack(this);

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
    public void onScrollChange(ScrollHeaderView view, float headerKeepHight) {

        mLayoutParams.height += (int) headerKeepHight;

        if (mLayoutParams.height > 800) {
            mLayoutParams.height = 800;
        } else if (mLayoutParams.height < 100) {
            mLayoutParams.height = 100;
        }

        //ll_container 高度值不能动态变化(match_parent) 即 需要设置固定值 否则 translationY 跟设定的值不同步 ？？
        ll_container.setTranslationY((mLayoutParams.height - 800));
        mHeader.requestLayout();
    }

    @Override
    public boolean isInterceptDownTouchEvent() {
        return scrollY == 0;
    }

    @Override
    public boolean isInterceptUpTouchEvent() {
        return mLayoutParams.height <= 800 && mLayoutParams.height > 100;
    }
}
