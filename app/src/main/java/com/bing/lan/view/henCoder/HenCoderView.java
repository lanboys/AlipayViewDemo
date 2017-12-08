package com.bing.lan.view.henCoder;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.bing.lan.view.R;
import com.bing.lan.view.utils.LogUtil;

/**
 * Created by 蓝兵 on 2017/12/8.
 * http://hencoder.com/
 */

public class HenCoderView extends View {

    protected final LogUtil log = LogUtil.getLogUtil(getClass(), LogUtil.LOG_VERBOSE);
    private Paint mPaint;
    //View 的大小
    private float mViewHeight, mViewWidth;
    //View 的中心点
    private float mViewCenterX, mViewCenterY;

    public HenCoderView(Context context) {
        super(context);
    }

    public HenCoderView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
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
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float[] points = {
                20, 20,
                120, 20,
                70, 20,
                70, 120,
                20, 120,
                120, 120,
                //150, 20,
                //250, 20,
                //150, 20,
                //150, 120,
                //250, 20,
                //250, 120,
                //150, 120,
                //250, 120
        };
        //http://hencoder.com/ui-1-1/
        canvas.drawLines(points, mPaint);
        canvas.save();
        canvas.translate(0, 300);
        //canvas.drawPoints(points, mPaint);
        canvas.drawPoints(points, 2, 8, mPaint);
        canvas.restore();
    }
}
