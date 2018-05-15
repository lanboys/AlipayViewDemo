package com.bing.lan.view.scrollHeader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import com.bing.lan.view.R;
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

public class ScrollHeaderView extends LinearLayout {

    protected final LogUtil log = LogUtil.getLogUtil(getClass(), LogUtil.LOG_VERBOSE);
    private final int triangleColor = Color.parseColor("#fff39800");                        //边角的颜色
    private final int triangleLength = dp2px(15);                                         //每个角的点距离
    private final int triangleWidth = dp2px(2);                                           //每个角的点宽度
    private final int centerPointRadius = dp2px(8);                                           //每个角的点宽度
    private final int backgroundColor = Color.parseColor("#ff000000");                          //蒙在摄像头上面区域的半透明颜色
    private final int qrcodeColor = Color.parseColor("#ffffffff");                          //蒙在摄像头上面区域的半透明颜色
    private final int lineColor = Color.parseColor("#fff39800");                            //中间线的颜色
    private final int textColor = Color.parseColor("#CCCCCC");                            //文字的颜色
    private final int textMarinTop = dp2px(30);                                           //文字距离识别框的距离
    private float startX, startY, inX, inY;
    private OnRectCenterScrollListener mOnRectCenterScrollListener;

    private Paint mPaint;
    private RectF mRectF;
    private Path mTrianglePath;
    private float mCenterX, mCenterY;//矩形 的中心点
    private float mViewCenterX, mViewCenterY;//View 的中心点
    private float originLengthX = 300;// 矩形长度一半
    private float currentLengthX = originLengthX;// 矩形长度一半
    private float originLengthY = 300;// 矩形长度一半
    private float currentLengthY = originLengthY;// 矩形长度一半
    private float mViewHeight, mViewWidth;//View 的大小
    private Paint mBackgroundPaint;                                                              //背景色画笔
    private Paint mQrcodePaint;                                                              //背景色画笔
    private Paint mTrianglePaint;
    private Paint centerPointPaint;
    private Paint laserPointPaint;
    private Paint textPaint;
    private Paint linePaint;

    //private float toplimitY;//以初始中心位置为准

    //private float lowerlimitY;//以初始中心位置为准

    //getX()是表示Widget相对于自身左上角的x坐标,
    //而getRawX()是表示相对于屏幕左上角的x坐标值
    //(注意:这个屏幕左上角是手机屏幕左上角,不管activity是否有titleBar或是否全屏幕)
    private float onClickStartX, onClickStartY;
    private boolean isActionMoveQrcode = false;
    private int lineOffsetCount = 0;
    private Bitmap mScannerLaserBitmap;
    private RectF mTriangleRectF;

    public ScrollHeaderView(Context context) {
        super(context);
    }

    public ScrollHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);
        initView();
    }

    private int dp2px(int dp) {
        float density = getContext().getResources().getDisplayMetrics().density;
        return (int) (dp * density + 0.5f);
    }

    private void initView() {
        //初始化画笔
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(10f);
        mPaint.setColor(getResources().getColor(R.color.holo_blue_light));
        //绘制范围
        mRectF = new RectF();

        //周围背景灰
        mBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBackgroundPaint.setColor(backgroundColor);

        //二维码扫描框
        mQrcodePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mQrcodePaint.setColor(qrcodeColor);

        //中心点画笔
        centerPointPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        centerPointPaint.setColor(triangleColor);
        centerPointPaint.setStrokeWidth(centerPointRadius);
        centerPointPaint.setStyle(Paint.Style.STROKE);

        //闪烁点画笔
        laserPointPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        laserPointPaint.setColor(triangleColor);
        laserPointPaint.setStrokeWidth(dp2px(2));
        laserPointPaint.setStyle(Paint.Style.STROKE);

        // 四个角落的三角
        mTrianglePath = new Path();

        mTrianglePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTrianglePaint.setColor(triangleColor);
        mTrianglePaint.setStrokeWidth(triangleWidth);
        mTrianglePaint.setStyle(Paint.Style.STROKE);

        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setColor(lineColor);

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(textColor);
        textPaint.setTextSize(dp2px(14));

        PointF point1 = new PointF(0, 0);
        PointF point2 = new PointF(0, 0);
        PointF point3 = new PointF(0, 0);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        return super.dispatchTouchEvent(event);
    }

    //int headerKeepHeight = 300;

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
                    if (mOnRectCenterScrollListener != null) {
                        if (mOnRectCenterScrollListener.isInterceptUpTouchEvent()) {
                            return true;
                        }
                    }
                } else if (inY > 0) {
                    //向下滑动
                    if (mOnRectCenterScrollListener != null) {
                        if (mOnRectCenterScrollListener.isInterceptDownTouchEvent()) {
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

        if (mOnRectCenterScrollListener != null) {
            mOnRectCenterScrollListener.onRectCenterScroll(this, inY);
        }
        return true;
    }

    public OnRectCenterScrollListener getOnRectCenterScrollListener() {
        return mOnRectCenterScrollListener;
    }

    public void setOnRectCenterScrollListener(OnRectCenterScrollListener onRectCenterScrollListener) {
        mOnRectCenterScrollListener = onRectCenterScrollListener;
    }

    interface OnRectCenterScrollListener {

        void onRectCenterScroll(ScrollHeaderView view, float headerKeepHight);

        boolean isInterceptDownTouchEvent();

        boolean isInterceptUpTouchEvent();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mViewHeight = h;
        mViewWidth = w;

        log.i("onSizeChanged(): w " + w);
        log.i("onSizeChanged(): h " + h);
        log.i("onSizeChanged(): oldw " + oldw);
        log.i("onSizeChanged(): oldh " + oldh);
        //log.i("onSizeChanged(): " + onSizeChangedPrint());

        mViewCenterX = w / 2;
        mViewCenterY = h / 2;

        mCenterX = mViewCenterX;
        mCenterY = mViewCenterY;

        // 重置矩形
        mRectF.set(mCenterX - currentLengthX, mCenterY - currentLengthY,
                mCenterX + currentLengthX, mCenterY + currentLengthY);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //log.i("onDraw(): 绘制");
    }
}
