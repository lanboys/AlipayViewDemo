package com.bing.lan.view.loading;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.bing.lan.view.R;
import com.bing.lan.view.utils.LogUtil;

/**
 * 大转盘
 * http://www.jianshu.com/p/dd11cdb4dd28
 * Android中Canvas绘图基础详解
 * http://blog.csdn.net/iispring/article/details/49770651
 */

public class Loading1View extends View {

    protected final LogUtil log = LogUtil.getLogUtil(getClass(), LogUtil.LOG_VERBOSE);
    private final int mPointPaintWidth = dp2px(4);
    private Paint mPointPaint;
    private int mCenterX, mCenterY;
    private Bitmap mPointBitmap;
    private float mStartAngle = 20;
    private float mSweepAngle;

    public Loading1View(Context context) {
        this(context, null);
    }

    public Loading1View(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Loading1View(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private int dp2px(int dp) {
        float density = getContext().getResources().getDisplayMetrics().density;
        return (int) (dp * density + 0.5f);
    }

    private void init() {
        //初始化画笔
        mPointPaint = new Paint();
        mPointPaint.setAntiAlias(true);
        mPointPaint.setDither(true);
        mPointPaint.setStyle(Paint.Style.STROKE);
        mPointPaint.setStrokeCap(Paint.Cap.ROUND);
        mPointPaint.setStrokeWidth(mPointPaintWidth);
        mPointPaint.setColor(getResources().getColor(R.color.holo_blue_light));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mCenterX = w / 2;
        mCenterY = h / 2;

        mPointBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas pointCanvas = new Canvas(mPointBitmap);
        int pointNum = 12;
        int radius = 150;
        mSweepAngle = (float) (360 / pointNum);
        for (int i = 0; i < pointNum; i++) {
            pointCanvas.rotate(mSweepAngle, mCenterX, mCenterY);
            mPointPaint.setStrokeWidth(mPointPaint.getStrokeWidth() + dp2px(1));
            pointCanvas.drawPoint(mCenterX, mCenterY + radius, mPointPaint);
        }
        mPointPaint.setStrokeWidth(mPointPaintWidth);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        mStartAngle += (mSweepAngle - 5);
        canvas.rotate(mStartAngle, mCenterX, mCenterY);

        if (mStartAngle > 360) {
            mStartAngle -= 360;
        }
        canvas.drawBitmap(mPointBitmap, 0, 0, null);
        postInvalidateDelayed(250L);
    }
}
