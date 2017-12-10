package com.bing.lan.view.henCoder;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.icu.util.Measure;
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
    private Paint paint;
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
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(10f);
        paint.setColor(getResources().getColor(R.color.holo_blue_light));
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

    Measure

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //testDrawLineAndPoint(canvas);
        //testDrawArc(canvas);

        //canvas.drawBitmap();

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.google);

        canvas.save();

        float centerX = 250;
        float centerY = 250;

        canvas.translate(centerX, centerY); // 旋转之后把投影移动回来

        Camera camera = new Camera();
        camera.save(); // 保存 Camera 的状态
        camera.rotateX(30); // 旋转 Camera 的三维空间

        camera.applyToCanvas(canvas); // 把旋转投影到 Canvas
        camera.restore(); // 恢复 Camera 的状态

        canvas.translate(-centerX, -centerY); // 旋转之前把绘制内容移动到轴心（原点）
        canvas.drawBitmap(bitmap, 100, 100, paint);
        canvas.restore();
    }

    private void testDrawBitmapMesh(Canvas canvas) {
        //http://blog.csdn.net/danfengw/article/details/48598489  迎风飘扬的国旗
        //http://www.jianshu.com/p/11e6be1f18e6
        //http://blog.csdn.net/garyhu1/article/details/70501107 重点 水波纹

    }

    @Override
    public void setScrollX(int value) {
        super.setScrollX(value);

    }
    //https://www.cnblogs.com/xinmengwuheng/p/7070092.html
    @Override
    public void onDrawForeground(Canvas canvas) {
        super.onDrawForeground(canvas);
    }

    private void testDrawArc(Canvas canvas) {
        paint.setStyle(Paint.Style.FILL); // 填充模式
        canvas.drawArc(200, 100, 800, 500, -110, 100, true, paint); // 绘制扇形
        canvas.drawArc(200, 100, 800, 500, 20, 140, false, paint); // 绘制封口的弧形
        paint.setStyle(Paint.Style.STROKE); // 画线模式
        canvas.drawArc(200, 100, 800, 500, 180, 60, false, paint); // 绘制不封口的弧形
        canvas.drawRect(200, 100, 800, 500, paint);
    }

    private void testDrawLineAndPoint(Canvas canvas) {
        float[] points = {
                20, 20,
                120, 20,
                70, 20,
                70, 120,
                20, 120,
                120, 120,
                150, 20,
                250, 20,
                150, 20,
                150, 120,
                250, 20,
                250, 120,
                150, 120,
                250, 120
        };
        //http://hencoder.com/ui-1-1/
        canvas.drawLines(points, paint);
        canvas.drawPoints(points, 2, 8, paint);
    }
}
