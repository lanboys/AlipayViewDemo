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

    /**
     * 检测按下到抬起时使用的时间
     */
    private long mDownTime;
    private float mDownStartY;
    /**
     * 自动滚动线程
     */
    private AngleRunnable mAngleRunnable;
    /**
     * 判断是否正在自动滚动
     */
    private boolean isMove;

    private boolean isMoveUp;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        log.i("onInterceptTouchEvent(): ---------------------------");
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                log.i("onInterceptTouchEvent()status:状态  按下");
                startY = event.getY();
                mDownTime = System.currentTimeMillis();
                mDownStartY = startY;

                // 如果当前已经在快速滚动
                if (isMove) {
                    // 移除快速滚动的回调
                    removeCallbacks(mAngleRunnable);
                    isMove = false;
                    return true;
                }

                break;
            case MotionEvent.ACTION_MOVE:
                log.i("onInterceptTouchEvent()status:状态 移动");
                inY = event.getY() - startY;
                log.i("onInterceptTouchEvent(): inY: " + inY);

                if (inY < 0) {
                    // 向上滑动
                    isMoveUp = true;
                    if (mInterceptTouchEventCallBack != null) {
                        if (mInterceptTouchEventCallBack.isInterceptUpTouchEvent()) {
                            return true;
                        }
                    }
                } else if (inY > 0) {
                    //向下滑动
                    isMoveUp = false;
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

                // 获取每秒移动的角度
                float anglePerSecond = (event.getY() - mDownStartY) * 100
                        / (System.currentTimeMillis() - mDownTime);
                log.i("onTouchEvent() :每秒移动的距离 :" + anglePerSecond);

                // 如果达到最大速度
                if (Math.abs(anglePerSecond) > 100 && !isMove) {
                    // 惯性滚动
                    log.i("onTouchEvent() :启动惯性");
                    post(mAngleRunnable = new AngleRunnable(anglePerSecond));
                    return true;
                }

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

    /**
     * 惯性滚动 https://www.jianshu.com/p/f50074c2da20
     */
    private class AngleRunnable implements Runnable {

        private float angelPerSecond;
        private float angelPerSecondxx;

        public AngleRunnable(float velocity) {
            this.angelPerSecond = velocity;

            float abs = Math.abs(angelPerSecond);

            if (abs > 300) {
                angelPerSecondxx = abs - 260;
            } else if (abs > 200) {
                angelPerSecondxx = abs - 140;
            } else {
                angelPerSecondxx = abs - 30;
            }
        }

        public void run() {
            //小于20停止
            if ((int) Math.abs(angelPerSecond) < angelPerSecondxx) {
                isMove = false;
                return;
            }
            isMove = true;
            // 滚动时候不断修改滚动角度大小
            //mStartAngle  = mStartAngle + (angelPerSecond / 30);

            if (mOnScrollChangeListener != null) {
                float t = 30;
                if (isMoveUp) {
                    t = -1 * t;
                }

                float v = angelPerSecond * 15 / 100;
                log.i("onTouchEvent() :惯性移动的距离 :" + v);
                log.i("onTouchEvent() :惯性移动的距离 angelPerSecond:" + angelPerSecond);

                mOnScrollChangeListener.onScrollChange(ScrollHeaderView.this, v);
            }

            //逐渐减小这个值
            angelPerSecond /= 1.0666F;
            postDelayed(this, 15);
        }
    }
}
