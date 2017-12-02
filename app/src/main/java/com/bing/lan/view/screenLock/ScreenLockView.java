package com.bing.lan.view.screenLock;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.bing.lan.view.R;
import com.bing.lan.view.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 大转盘
 * http://www.jianshu.com/p/dd11cdb4dd28
 * Android中Canvas绘图基础详解
 * http://blog.csdn.net/iispring/article/details/49770651
 */

public class ScreenLockView extends View {

    protected final LogUtil log = LogUtil.getLogUtil(getClass(), LogUtil.LOG_VERBOSE);
    private final int mPointPaintWidth = dp2px(3);
    ScreenPoint mCurrentScreenPoint;
    private Paint mOuterPaint;
    private Paint mInnerPaint;
    private Paint mBackgroundPaint;
    private Paint mCenterPointPaint;
    private int mCenterX, mCenterY;
    private Bitmap mPointBitmap;
    private float mStartAngle = 20;
    private float mSweepAngle;
    private float length = 350;
    private float excircleRadius = 100;
    private List<ScreenPoint> mScreenPointList;
    private float currentX, currentY, inX, inY;
    private Path mPath;

    public ScreenLockView(Context context) {
        this(context, null);
    }

    public ScreenLockView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScreenLockView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private int dp2px(int dp) {
        float density = getContext().getResources().getDisplayMetrics().density;
        return (int) (dp * density + 0.5f);
    }

    private void init() {
        //初始化画笔
        mOuterPaint = new Paint();
        mOuterPaint.setAntiAlias(true);
        mOuterPaint.setDither(true);
        mOuterPaint.setStyle(Paint.Style.STROKE);
        mOuterPaint.setStrokeCap(Paint.Cap.ROUND);
        mOuterPaint.setStrokeWidth(mPointPaintWidth);
        mOuterPaint.setColor(getResources().getColor(R.color.holo_blue_light));

        mInnerPaint = new Paint();
        mInnerPaint.setAntiAlias(true);
        mInnerPaint.setDither(true);
        mInnerPaint.setStyle(Paint.Style.STROKE);
        mInnerPaint.setStrokeCap(Paint.Cap.ROUND);
        mInnerPaint.setStrokeWidth(dp2px(2));
        mInnerPaint.setColor(getResources().getColor(R.color.holo_blue_light1));

        mCenterPointPaint = new Paint();
        mCenterPointPaint.setAntiAlias(true);
        mCenterPointPaint.setDither(true);
        mCenterPointPaint.setStyle(Paint.Style.STROKE);
        mCenterPointPaint.setStrokeCap(Paint.Cap.ROUND);
        mCenterPointPaint.setStrokeWidth(dp2px(14));
        mCenterPointPaint.setColor(getResources().getColor(R.color.holo_blue_light1));

        mBackgroundPaint = new Paint();
        mBackgroundPaint.setAntiAlias(true);
        mBackgroundPaint.setDither(true);
        mBackgroundPaint.setStyle(Paint.Style.FILL);
        mBackgroundPaint.setColor(getResources().getColor(R.color.color_fff));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mCenterX = w / 2;
        mCenterY = h / 2;

        length = w / 3;

        mScreenPointList = new ArrayList<>();

        mScreenPointList.add(new ScreenPoint(new PointF(mCenterX, mCenterY), excircleRadius));
        mScreenPointList.add(new ScreenPoint(new PointF(mCenterX + length, mCenterY), excircleRadius));
        mScreenPointList.add(new ScreenPoint(new PointF(mCenterX - length, mCenterY), excircleRadius));
        mScreenPointList.add(new ScreenPoint(new PointF(mCenterX, mCenterY + length), excircleRadius));
        mScreenPointList.add(new ScreenPoint(new PointF(mCenterX, mCenterY - length), excircleRadius));
        mScreenPointList.add(new ScreenPoint(new PointF(mCenterX + length, mCenterY + length), excircleRadius));
        mScreenPointList.add(new ScreenPoint(new PointF(mCenterX - length, mCenterY - length), excircleRadius));
        mScreenPointList.add(new ScreenPoint(new PointF(mCenterX + length, mCenterY - length), excircleRadius));
        mScreenPointList.add(new ScreenPoint(new PointF(mCenterX - length, mCenterY + length), excircleRadius));

        mPath = new Path();
    }

    @Override
    protected void onDraw(Canvas canvas) {

        if (mCurrentScreenPoint != null) {
            canvas.drawLine(mCurrentScreenPoint.getCenterPoint().x, mCurrentScreenPoint.getCenterPoint().y, currentX, currentY, mCenterPointPaint);
        }

        canvas.drawPath(mPath, mCenterPointPaint);
        for (ScreenPoint screenPoint : mScreenPointList) {
            canvas.drawBitmap(screenPoint.onDraw(mOuterPaint, mInnerPaint, mCenterPointPaint), 0, 0, null);
        }
        //postInvalidateDelayed(250L);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        log.i("onTouchEvent(): ---------------------------");
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                log.i("onTouchEvent()status:状态  按下");

                currentX = event.getX();
                currentY = event.getY();

                //mPath.reset();

                for (ScreenPoint screenPoint : mScreenPointList) {
                    if (screenPoint.isInCircle(currentX, currentY)) {
                        mPath.moveTo(screenPoint.getCenterPoint().x, screenPoint.getCenterPoint().y);
                        mCurrentScreenPoint = screenPoint;
                        break;
                    }
                }

                break;

            case MotionEvent.ACTION_MOVE:
                log.i("onTouchEvent()status:状态 移动");
                currentX = event.getX();
                currentY = event.getY();

                for (ScreenPoint screenPoint : mScreenPointList) {
                    if (screenPoint.isInCircle(currentX, currentY)) {
                        if (mCurrentScreenPoint == null) {
                            mPath.moveTo(screenPoint.getCenterPoint().x, screenPoint.getCenterPoint().y);
                        } else {
                            mPath.lineTo(screenPoint.getCenterPoint().x, screenPoint.getCenterPoint().y);
                        }
                        mCurrentScreenPoint = screenPoint;
                        break;
                    }
                }

                if (mCurrentScreenPoint != null) {
                    int rotationBetweenLines = getRotationBetweenLines(mCurrentScreenPoint.getCenterPoint().x, mCurrentScreenPoint.getCenterPoint().y, currentX, currentY);

                    mCurrentScreenPoint.setStartAngle(rotationBetweenLines);
                }

                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                log.i("onTouchEvent()status:状态 松手");
                mCurrentScreenPoint = null;

                currentX = event.getX();
                currentY = event.getY();
                mPath.reset();
                invalidate();
                break;
        }
        return true;
    }

    /**
     * 获取两条线的夹角
     *
     * @param centerX
     * @param centerY
     * @param xInView
     * @param yInView
     */
    public int getRotationBetweenLines(float centerX, float centerY, float xInView, float yInView) {
        double rotation = 0;

        double k1 = (double) (centerY - centerY) / (centerX * 2 - centerX);
        double k2 = (double) (yInView - centerY) / (xInView - centerX);
        double tmpDegree = Math.atan((Math.abs(k1 - k2)) / (1 + k1 * k2)) / Math.PI * 180;

        if (xInView > centerX && yInView < centerY) {  //第一象限
            rotation = 90 - tmpDegree;
        } else if (xInView > centerX && yInView > centerY) //第二象限
        {
            rotation = 90 + tmpDegree;
        } else if (xInView < centerX && yInView > centerY) { //第三象限
            rotation = 270 - tmpDegree;
        } else if (xInView < centerX && yInView < centerY) { //第四象限
            rotation = 270 + tmpDegree;
        } else if (xInView == centerX && yInView < centerY) {
            rotation = 0;
        } else if (xInView == centerX && yInView > centerY) {
            rotation = 180;
        }

        return (int) rotation;
    }

    class ScreenPoint {

        private PointF centerPoint;
        private float radius;
        private RectF mRectF;
        private float radius1;

        private float startAngle = -100;
        private float sweepAngle = 200;

        private Canvas canvas = new Canvas();
        private Bitmap mBitmap;

        private boolean hasModify = true;

        ScreenPoint(PointF centerPoint, float radius) {
            this.centerPoint = centerPoint;
            this.radius = radius;
            radius1 = radius - 20;
            mRectF = new RectF(centerPoint.x - radius1, centerPoint.y - radius1, centerPoint.x + radius1, centerPoint.y + radius1);
        }

        public Canvas getCanvas() {
            return canvas;
        }

        public void setCanvas(Canvas canvas) {
            // 动态代理
            hasModify = true;
            this.canvas = canvas;
        }

        public PointF getCenterPoint() {
            return centerPoint;
        }

        public void setCenterPoint(PointF centerPoint) {
            hasModify = true;

            this.centerPoint = centerPoint;
        }

        public Bitmap getBitmap() {
            return mBitmap;
        }

        public void setBitmap(Bitmap bitmap) {
            hasModify = true;

            mBitmap = bitmap;
        }

        public RectF getRectF() {
            return mRectF;
        }

        public void setRectF(RectF rectF) {
            hasModify = true;
            mRectF = rectF;
        }

        public float getRadius1() {
            return radius1;
        }

        public void setRadius1(float radius1) {
            hasModify = true;

            this.radius1 = radius1;
        }

        public float getRadius() {
            return radius;
        }

        public void setRadius(float radius) {
            hasModify = true;

            this.radius = radius;
        }

        public float getStartAngle() {
            return startAngle;
        }

        public void setStartAngle(float startAngle) {
            hasModify = true;

            this.startAngle = startAngle;
        }

        public float getSweepAngle() {
            return sweepAngle;
        }

        public void setSweepAngle(float sweepAngle) {
            hasModify = true;
            this.sweepAngle = sweepAngle;
        }

        public Bitmap onDraw(Paint mOuterPaint, Paint mInnerPaint, Paint mCenterPointPaint) {

            if (mBitmap == null || hasModify) {
                hasModify = false;
                mBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
                canvas.setBitmap(mBitmap);
                canvas.drawCircle(centerPoint.x, centerPoint.y, radius, mBackgroundPaint);
                canvas.drawCircle(centerPoint.x, centerPoint.y, radius, mOuterPaint);
                canvas.drawPoint(centerPoint.x, centerPoint.y, mCenterPointPaint);

                canvas.drawArc(mRectF, startAngle, sweepAngle, false, mInnerPaint);

                canvas.save();
                canvas.rotate(startAngle - 10, centerPoint.x, centerPoint.y);
                canvas.drawPoint(centerPoint.x + radius1, centerPoint.y, mInnerPaint);
                canvas.rotate(sweepAngle + 20, centerPoint.x, centerPoint.y);
                canvas.drawPoint(centerPoint.x + radius1, centerPoint.y, mInnerPaint);
                canvas.restore();
            }

            return mBitmap;
        }

        public boolean isInCircle(float x, float y) {
            //点击位置x坐标与圆心的x坐标的距离
            float distanceX = Math.abs(centerPoint.x - x);
            //点击位置y坐标与圆心的y坐标的距离
            float distanceY = Math.abs(centerPoint.y - y);
            //点击位置与圆心的直线距离
            int distanceZ = (int) Math.sqrt(Math.pow(distanceX, 2) + Math.pow(distanceY, 2));

            //如果点击位置与圆心的距离大于圆的半径，证明点击位置没有在圆内
            return distanceZ <= radius;
        }
    }
}
