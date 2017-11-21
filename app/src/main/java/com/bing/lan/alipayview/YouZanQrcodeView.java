package com.bing.lan.alipayview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by 520 on 2017/6/20.
 * <p>
 * <p>
 * https://github.com/lanboys/AndroidStudy
 * http://blog.csdn.net/wingichoy/article/details/50500479
 */

public class YouZanQrcodeView extends View {

    protected final LogUtil log = LogUtil.getLogUtil(getClass(), LogUtil.LOG_VERBOSE);
    private final int triangleColor = Color.parseColor("#f39800");                        //边角的颜色
    private final int triangleLength = dp2px(20);                                         //每个角的点距离
    private final int triangleWidth = dp2px(2);                                           //每个角的点宽度
    private final int backgroundColor = Color.parseColor("#60000000");                          //蒙在摄像头上面区域的半透明颜色
    private final int qrcodeColor = Color.parseColor("#ffffffff");                          //蒙在摄像头上面区域的半透明颜色
    private final int lineColor = Color.parseColor("#f39800");                            //中间线的颜色
    private final int textColor = Color.parseColor("#CCCCCC");                            //文字的颜色
    private final int textMarinTop = dp2px(30);                                           //文字距离识别框的距离
    float startX;
    float startY;
    float inX;
    float inY;
    private Paint mPaint;
    private RectF mRectF;
    private Path mPath;
    private Path mTrianglePath;
    private float mCenterX, mCenterY;
    private float mViewCenterX, mViewCenterY;
    private float originLength = 300;// 矩形长度一半
    private float currentLength = originLength;// 矩形长度一半
    private Paint mBackgroundPaint;                                                              //背景色画笔
    private Paint mQrcodePaint;                                                              //背景色画笔
    private Paint trianglePaint;
    private Paint textPaint;
    private Paint linePaint;
    private Line firstLine;// 第一条线
    private Line secondLine;// 第二条线

    //getX()是表示Widget相对于自身左上角的x坐标,
    //而getRawX()是表示相对于屏幕左上角的x坐标值
    //(注意:这个屏幕左上角是手机屏幕左上角,不管activity是否有titleBar或是否全屏幕)

    public YouZanQrcodeView(Context context) {
        super(context);
    }
    public YouZanQrcodeView(Context context, @Nullable AttributeSet attrs) {
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
        //绘制范围
        mRectF = new RectF();
        mPath = new Path();

        // 四个角落的三角
        mTrianglePath = new Path();

        //周围背景灰
        mBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBackgroundPaint.setColor(backgroundColor);
        //二维码扫描框
        mQrcodePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mQrcodePaint.setColor(qrcodeColor);

        trianglePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        trianglePaint.setColor(triangleColor);
        trianglePaint.setStrokeWidth(triangleWidth);
        trianglePaint.setStyle(Paint.Style.STROKE);

        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setColor(lineColor);

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(textColor);
        textPaint.setTextSize(dp2px(14));

        PointF point1 = new PointF(0, 0);
        PointF point2 = new PointF(0, 0);
        PointF point3 = new PointF(0, 0);
        firstLine = new Line(point1, point2);
        secondLine = new Line(point2, point3);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        log.i("onTouchEvent(): ---------------------------");
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = event.getX();
                startY = event.getY();

                log.i("onTouchEvent(): startX: " + startX);
                log.i("onTouchEvent(): startY: " + startY);

                log.i("onTouchEvent(): 按下");
                break;
            case MotionEvent.ACTION_MOVE:
                inX = event.getX() - startX;
                inY = event.getY() - startY;

                startX = event.getX();
                startY = event.getY();

                log.i("onTouchEvent(): startX: " + startX);
                log.i("onTouchEvent(): startY: " + startY);

                log.i("onTouchEvent(): inX: " + inX);
                log.i("onTouchEvent(): inY: " + inY);

                log.i("onTouchEvent(): 移动");
                break;
        }
        //mCenterX = event.getX();
        mCenterY += inY;

        resetLength();

        mRectF.set(mCenterX - currentLength, mCenterY - currentLength, mCenterX + currentLength, mCenterY + currentLength);

        invalidate();
        log.i("onTouchEvent(): ");
        log.i("onTouchEvent(): ");

        //float rawX = event.getRawX();
        //float rawY = event.getRawY();
        //log.i("onTouchEvent(): rawX: " + rawX);
        //log.i("onTouchEvent(): rawY: " + rawY);

        boolean b = super.onTouchEvent(event);
        //log.i("onTouchEvent(): boolean: " + b);
        return true;
    }

    private void resetLength() {
        currentLength = (mCenterY / mViewCenterY) * originLength;
        log.i("resetLength(): mViewCenterY: " + mViewCenterY);
        log.i("resetLength(): mCenterY: " + mCenterY);
        log.i("resetLength(): currentLength: " + currentLength);

        if (currentLength < 80) {
            currentLength = 80;
        } else if (currentLength > originLength) {
            currentLength = originLength;
        }

        float v = currentLength / originLength*100;
        log.i("resetLength(): currentLength11111111111111111: " + v);
        log.i("resetLength(): currentLength11111111111111111: " + (int) v);

        mBackgroundPaint.setAlpha((int) v);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
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
        mRectF.set(mCenterX - currentLength, mCenterY - currentLength, mCenterX + currentLength, mCenterY + currentLength);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawCenterRect(canvas);
    }

    private void drawCenterRect(Canvas canvas) {
        int width = canvas.getWidth();
        int height = canvas.getHeight();

        //log.i("drawCenterRect(): width: " + width);
        //log.i("drawCenterRect(): height: " + height);

        // 除了中间的识别区域，其他区域都将蒙上一层半透明的图层
        canvas.drawRect(0, 0, width, mRectF.top, mBackgroundPaint);// 上面
        canvas.drawRect(0, mRectF.top, mRectF.left, mRectF.bottom + 1, mBackgroundPaint);// 左边
        canvas.drawRect(mRectF.right + 1, mRectF.top, width, mRectF.bottom + 1, mBackgroundPaint); // 右边
        canvas.drawRect(0, mRectF.bottom + 1, width, height, mBackgroundPaint);// 下面

        //功能类似上面 但是有区别
        //canvas.drawRect(0, 0, width, height, mBackgroundPaint);
        //canvas.drawRect(mRectF.left, mRectF.top, mRectF.right, mRectF.bottom, mQrcodePaint);

        // 四个角落的三角
        //firstLine.startPoint.x = mRectF.left + triangleLength;
        //firstLine.startPoint.y = mRectF.top + triangleWidth / 2;
        //
        //firstLine.endPoint.x = mRectF.left + triangleWidth / 2;
        //firstLine.endPoint.y = mRectF.top + triangleWidth / 2;
        //
        //secondLine.endPoint.x = mRectF.left + triangleWidth / 2;
        //secondLine.endPoint.y = mRectF.top + triangleLength;
        //
        //drawLine(canvas, trianglePaint, mTrianglePath, firstLine, secondLine);
        mTrianglePath.reset();
        mTrianglePath.moveTo(mRectF.left + triangleLength, mRectF.top + triangleWidth / 2);
        mTrianglePath.lineTo(mRectF.left + triangleWidth / 2, mRectF.top + triangleWidth / 2);
        mTrianglePath.lineTo(mRectF.left + triangleWidth / 2, mRectF.top + triangleLength);
        canvas.drawPath(mTrianglePath, trianglePaint);

        mTrianglePath.moveTo(mRectF.right - triangleLength, mRectF.top + triangleWidth / 2);
        mTrianglePath.lineTo(mRectF.right - triangleWidth / 2, mRectF.top + triangleWidth / 2);
        mTrianglePath.lineTo(mRectF.right - triangleWidth / 2, mRectF.top + triangleLength);
        canvas.drawPath(mTrianglePath, trianglePaint);

        mTrianglePath.moveTo(mRectF.left + triangleWidth / 2, mRectF.bottom - triangleLength);
        mTrianglePath.lineTo(mRectF.left + triangleWidth / 2, mRectF.bottom - triangleWidth / 2);
        mTrianglePath.lineTo(mRectF.left + triangleLength, mRectF.bottom - triangleWidth / 2);
        canvas.drawPath(mTrianglePath, trianglePaint);

        mTrianglePath.moveTo(mRectF.right - triangleLength, mRectF.bottom - triangleWidth / 2);
        mTrianglePath.lineTo(mRectF.right - triangleWidth / 2, mRectF.bottom - triangleWidth / 2);
        mTrianglePath.lineTo(mRectF.right - triangleWidth / 2, mRectF.bottom - triangleLength);
        canvas.drawPath(mTrianglePath, trianglePaint);
    }

    private void drawLine(Canvas canvas, Paint paint, Path path, Line firstLine, Line secondLine) {
        path.reset();
        path.moveTo(firstLine.startPoint.x, firstLine.startPoint.y);
        path.lineTo(firstLine.endPoint.x, firstLine.endPoint.y);
        path.lineTo(secondLine.endPoint.x, secondLine.endPoint.y);
        canvas.drawPath(path, paint);
    }

    class Line {

        PointF startPoint;
        PointF endPoint;

        float k;//比例系数
        float b;//常数

        Line(PointF startPoint, PointF endPoint) {
            this.startPoint = startPoint;
            this.endPoint = endPoint;

            k = (endPoint.y - startPoint.y) / (endPoint.x - startPoint.x);
            b = startPoint.y - k * startPoint.x;
        }

        float getY(float x) {
            return k * x + b;
        }
    }
}
