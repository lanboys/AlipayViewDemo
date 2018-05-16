package com.bing.lan.view.scrollHeader;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import com.bing.lan.view.utils.LogUtil;

/**
 * Created by 520 on 2017/6/20.
 * <p>
 * <p>
 * https://github.com/lanboys/AndroidStudy
 * http://blog.csdn.net/wingichoy/article/details/50500479
 * <p>
 * http://blog.csdn.net/m075097/article/details/78533141
 * http://blog.csdn.net/zhh_csdn_ard/article/details/54943766
 * http://blog.csdn.net/hupei/article/details/51859171
 */

public class ScrollHeaderView extends FrameLayout {

    protected final LogUtil log = LogUtil.getLogUtil(getClass(), LogUtil.LOG_VERBOSE);
    private float startX, startY, inX, inY;
    private OnInterceptTouchEventCallBack mInterceptTouchEventCallBack;
    private OnScrollChangeListener mOnScrollChangeListener;

    public ScrollHeaderView(Context context) {
        super(context);
    }

    public ScrollHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);
    }

    public void setInterceptTouchEventCallBack(OnInterceptTouchEventCallBack interceptTouchEventCallBack) {
        mInterceptTouchEventCallBack = interceptTouchEventCallBack;
    }

    public void setOnScrollChangeListener(OnScrollChangeListener onScrollChangeListener) {
        mOnScrollChangeListener = onScrollChangeListener;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        log.i("onInterceptTouchEvent(): ---------------------------");
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                log.i("onInterceptTouchEvent()status:状态  按下");
                startY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                log.i("onInterceptTouchEvent()status:状态 移动");
                inY = event.getY() - startY;
                log.i("onInterceptTouchEvent(): inY: " + inY);

                if (inY < 0) {
                    // 向上滑动
                    if (mInterceptTouchEventCallBack != null) {
                        if (mInterceptTouchEventCallBack.isInterceptUpTouchEvent()) {
                            return true;
                        }
                    }
                } else if (inY > 0) {
                    //向下滑动
                    if (mInterceptTouchEventCallBack != null) {
                        if (mInterceptTouchEventCallBack.isInterceptDownTouchEvent()) {
                            return true;
                        }
                    }
                }

                break;
            case MotionEvent.ACTION_UP:
                log.i("onInterceptTouchEvent()status:状态 松手");
                break;
            default:
                log.i("onInterceptTouchEvent()status:状态 default");
                break;
        }
        return super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        log.i("onTouchEvent(): ---------------------------");
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                log.i("onTouchEvent()status:状态  按下");
                //
                startX = event.getX();
                startY = event.getY();

                break;

            case MotionEvent.ACTION_MOVE:
                log.i("onTouchEvent()status:状态 移动");
                //
                inX = event.getX() - startX;
                inY = event.getY() - startY;

                startX = event.getX();
                startY = event.getY();

                break;
            case MotionEvent.ACTION_UP:
                log.i("onTouchEvent()status:状态 松手");
                //复原
                inY = 0;
                inX = 0;

                break;
            default:
                break;
        }
        //log.i("onTouchEvent(): inX: " + inX);
        //log.i("onTouchEvent(): inY: " + inY);

        //log.i("onTouchEvent(): startX: " + startX);
        //log.i("onTouchEvent(): startY: " + startY);
        //boolean b = super.onTouchEvent(event);
        //log.i("onTouchEvent(): b: " + b);
        //
        ////return b;

        if (mOnScrollChangeListener != null) {
            mOnScrollChangeListener.onScrollChange(this, inY);
        }
        return true;
    }

    interface OnScrollChangeListener {

        void onScrollChange(ScrollHeaderView view, float headerKeepHight);
    }

    interface OnInterceptTouchEventCallBack {

        boolean isInterceptDownTouchEvent();

        boolean isInterceptUpTouchEvent();
    }
}
