package com.bing.lan.alipayview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.OvershootInterpolator;
import android.widget.RelativeLayout;

/**
 * Created by 520 on 2017/6/20.
 * <p>
 * <p>
 * https://github.com/lanboys/AndroidStudy
 * http://blog.csdn.net/wingichoy/article/details/50500479
 */

public class YouZanQrcodeView extends RelativeLayout {

    protected final LogUtil log = LogUtil.getLogUtil(getClass(), LogUtil.LOG_VERBOSE);
    private final int triangleColor = Color.parseColor("#f39800");                        //边角的颜色
    private final int triangleLength = dp2px(20);                                         //每个角的点距离
    private final int triangleWidth = dp2px(2);                                           //每个角的点宽度
    private final int centerPointRadius = dp2px(8);                                           //每个角的点宽度
    private final int backgroundColor = Color.parseColor("#60000000");                          //蒙在摄像头上面区域的半透明颜色
    private final int qrcodeColor = Color.parseColor("#ffffffff");                          //蒙在摄像头上面区域的半透明颜色
    private final int lineColor = Color.parseColor("#f39800");                            //中间线的颜色
    private final int textColor = Color.parseColor("#CCCCCC");                            //文字的颜色
    private final int textMarinTop = dp2px(30);                                           //文字距离识别框的距离
    private float startX, startY, inX, inY;

    private OnTopBottomClickListener mOnTopBottomClickListener;
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
    private Paint trianglePaint;
    private Paint centerPointPaint;
    private Paint textPaint;
    private Paint linePaint;
    private Line firstLine;// 第一条线
    private Line secondLine;// 第二条线

    //private float toplimitY;//以初始中心位置为准

    //private float lowerlimitY;//以初始中心位置为准

    //getX()是表示Widget相对于自身左上角的x坐标,
    //而getRawX()是表示相对于屏幕左上角的x坐标值
    //(注意:这个屏幕左上角是手机屏幕左上角,不管activity是否有titleBar或是否全屏幕)
    private float onClickStartX, onClickStartY;
    private OnRectCenterScrollListener mOnRectCenterScrollListener;
    private boolean isActionMoveQrcode = false;
    private int lineOffsetCount = 0;

    public YouZanQrcodeView(Context context) {
        super(context);
    }

    public YouZanQrcodeView(Context context, @Nullable AttributeSet attrs) {
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

        // 四个角落的三角
        mTrianglePath = new Path();

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
    public boolean dispatchTouchEvent(MotionEvent event) {
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        log.i("onTouchEvent(): ---------------------------");
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                log.i("onTouchEvent()status:状态  按下");
                isActionMoveQrcode = false;

                startX = event.getX();
                startY = event.getY();

                onClickStartX = startX;
                onClickStartY = startY;

                break;

            case MotionEvent.ACTION_MOVE:
                log.i("onTouchEvent()status:状态 移动");
                isActionMoveQrcode = true;

                inX = event.getX() - startX;
                inY = event.getY() - startY;

                startX = event.getX();
                startY = event.getY();
                // 重新计算位置
                resetView();
                invalidate();

                break;
            case MotionEvent.ACTION_UP:
                log.i("onTouchEvent()status:状态 松手");

                //复原
                inY = 0;
                inX = 0;

                startX = event.getX();
                startY = event.getY();

                // 起始位置都在范围内才起作用
                if (startY < mViewHeight * 0.25f && onClickStartY < mViewHeight * 0.25f) {
                    if (mOnTopBottomClickListener != null) {
                        mOnTopBottomClickListener.onTopClick(this);
                    }
                    onTopBottomClickReset();
                }
                // 起始位置都在范围内才起作用
                if (startY > mViewHeight * 0.75f && onClickStartY > mViewHeight * 0.75f) {
                    if (mOnTopBottomClickListener != null) {
                        mOnTopBottomClickListener.onBottomClick(this);
                    }
                    onTopBottomClickReset();
                }
                if (isActionMoveQrcode) {
                    resetLocation();
                }
                break;
        }
        //log.i("onTouchEvent(): inX: " + inX);
        //log.i("onTouchEvent(): inY: " + inY);
        //
        //log.i("onTouchEvent(): startX: " + startX);
        //log.i("onTouchEvent(): startY: " + startY);
        //boolean b = super.onTouchEvent(event);
        return true;
    }

    private void onTopBottomClickReset() {
        float centerLine = getTopLimitY() + (getLowerLimitY() - getTopLimitY()) / 2;
        if (mCenterY < centerLine) {
            doAnimator(getLowerLimitY() - getTopLimitY(), false);
        } else {
            doAnimator(getLowerLimitY() - getTopLimitY(), true);
        }
    }

    private void resetLocation() {
        float centerLine = getTopLimitY() + (getLowerLimitY() - getTopLimitY()) / 2;
        if (mCenterY < centerLine) {
            doAnimator(mCenterY - getTopLimitY(), true);
        } else {
            doAnimator(getLowerLimitY() - mCenterY, false);
        }
    }

    private void doAnimator(float dy, final boolean isToUp) {
        if (dy == 0) {
            return;
        }
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(dy, 0);
        log.i("onAnimationUpdate() dy: " + dy);

        valueAnimator.setDuration(500);

        //http://easings.net/zh-cn
        //http://blog.csdn.net/gzejia/article/details/51063564
        //http://blog.csdn.net/harvic880925/article/details/50598322
        //https://my.oschina.net/banxi/blog/135633?fromerr=uv67kzf9#OSC_h2_7
        //http://www.wolframalpha.com/input/?i=x%5E(2*3)(0%3Cx%3C%3D1)
        //http://blog.csdn.net/u011835956/article/details/51783025

        //valueAnimator.setInterpolator(new LinearInterpolator());
        //valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        //valueAnimator.setInterpolator(new AnticipateInterpolator());
        //valueAnimator.setInterpolator(new AnticipateOvershootInterpolator());
        valueAnimator.setInterpolator(new OvershootInterpolator());
        //valueAnimator.setInterpolator(new LinearInterpolator());

        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float animatedValue = (float) animation.getAnimatedValue();

                log.i("onAnimationUpdate():------------------ ");
                log.i("onAnimationUpdate() animatedValue: " + animatedValue);
                log.i("onAnimationUpdate() mCenterY: " + mCenterY);

                if (isToUp) {
                    mCenterY = getTopLimitY() + animatedValue;
                    log.i("onAnimationUpdate() getTopLimitY: " + getTopLimitY());
                } else {
                    mCenterY = getLowerLimitY() - animatedValue;
                    log.i("onAnimationUpdate() getLowerLimitY: " + getLowerLimitY());
                }

                log.i("onAnimationUpdate() mCenterY: " + mCenterY);

                resetView();

                postInvalidate();
            }
        });

        valueAnimator.start();
    }

    private void resetView() {
        resetRectCenter();
        resetLength();
        resetRect();
    }

    private void resetRectCenter() {

        mCenterY += inY;
        log.i("onTouchEvent() getTopLimitY: " + getTopLimitY());
        log.i("onTouchEvent() getLowerLimitY: " + getLowerLimitY());
        log.i("onTouchEvent() mCenterY: " + mCenterY);

        // 控制y轴中心上下限
        //if (mCenterY < getTopLimitY()) {
        //    mCenterY = getTopLimitY();
        //}
        //
        //if (mCenterY > getLowerLimitY()) {
        //    mCenterY = getLowerLimitY();
        //}
    }

    /**
     * 重置长度
     */
    private void resetLength() {
        currentLengthX = (mCenterY / mViewCenterY) * originLengthX;
        currentLengthY = (mCenterY / mViewCenterY) * originLengthY;
        log.i("resetLength(): mViewCenterY: " + mViewCenterY);
        log.i("resetLength(): mCenterY: " + mCenterY);
        log.i("resetLength(): currentLengthX: " + currentLengthX);
        //log.i("resetLength(): currentLengthY: " + currentLengthY);

        if (currentLengthX < 80) {
            currentLengthX = 80;
        } else if (currentLengthX > originLengthX) {
            currentLengthX = originLengthX;
        }

        if (currentLengthY < 80) {
            currentLengthY = 80;
        } else if (currentLengthY > originLengthY) {
            currentLengthY = originLengthY;
        }

        float v = currentLengthX / originLengthX * 100;
        log.i("resetLength(): currentLength11111111111111111: " + v);
        log.i("resetLength(): currentLength11111111111111111: " + (int) v);

        mBackgroundPaint.setAlpha((int) v);
    }

    private void resetRect() {
        float left = mCenterX - currentLengthX;
        float top = mCenterY - currentLengthY;
        float right = mCenterX + currentLengthX;
        float bottom = mCenterY + currentLengthY;

        log.i("onTouchEvent() left: " + left);
        log.i("onTouchEvent() top:  " + top);
        log.i("onTouchEvent() right: " + right);
        log.i("onTouchEvent() bottom: " + bottom);

        // 控制y轴四边形 上下限
        //if (top < toplimit) {
        //    top = toplimit;
        //}
        //
        //if (bottom > lowerlimit) {
        //    bottom = lowerlimit;
        //}

        mRectF.set(left, top, right, bottom);

        if (mOnRectCenterScrollListener != null) {

            float centerYRatio = (mCenterY - getTopLimitY()) / (getLowerLimitY() - getTopLimitY());
            log.i("onRectCenterScroll() ----------------------");
            log.i("onRectCenterScroll() centerY: " + mCenterY);
            log.i("onRectCenterScroll() centerYRatio: " + centerYRatio);

            mOnRectCenterScrollListener.onRectCenterScroll(this, mCenterX, mCenterY, 0, centerYRatio);
        }
    }

    private float getTopLimitY() {
        return mViewHeight * 0.2f;
    }

    private float getLowerLimitY() {
        return mViewHeight * 0.5f;
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
        mRectF.set(mCenterX - currentLengthX, mCenterY - currentLengthY, mCenterX + currentLengthX, mCenterY + currentLengthY);
    }

    /**
     * ViewGroup为什么不会调用onDraw
     * http://blog.csdn.net/leehong2005/article/details/7299471
     * 1）ViewGroup默认情况下，会被设置成WILL_NOT_DRAW，这是从性能考虑，这样一来，onDraw就不会被调用了。
     * 2）如果我们要重要一个ViweGroup的onDraw方法，有两种方法：
     * 1，在构造函数里面，给其设置一个颜色，如#00000000。
     * 2，在构造函数里面，调用setWillNotDraw(false)，去掉其WILL_NOT_DRAW flag。
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        log.i("onDraw(): 绘制");
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

        canvas.drawPoint(mCenterX, mCenterY, centerPointPaint);//方点
        //canvas.drawCircle(mCenterX, mCenterY, 15, centerPointPaint);//圆点

        canvas.drawLine(0, getLowerLimitY(), mViewWidth, getLowerLimitY(), centerPointPaint);
        canvas.drawLine(0, getTopLimitY(), mViewWidth, getTopLimitY(), centerPointPaint);

        RectF frame = mRectF;

        //循环划线，从上到下
        if (lineOffsetCount > frame.bottom - frame.top - dp2px(10)) {
            lineOffsetCount = 0;
        } else {
            lineOffsetCount = lineOffsetCount + 6;
            //            canvas.drawLine(frame.left, frame.top + lineOffsetCount, frame.right, frame.top + lineOffsetCount, linePaint);    //画一条红色的线
            RectF lineRect = new RectF();
            lineRect.left = frame.left;
            lineRect.top = frame.top + lineOffsetCount;
            lineRect.right = frame.right;
            lineRect.bottom = lineRect.top + dp2px(10);

            //mBitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_guide1).copy(Bitmap.Config.ARGB_8888, true);
            BitmapDrawable drawable = (BitmapDrawable) (getResources().getDrawable(R.drawable.scanline_yellow));
            Bitmap bitmap = drawable.getBitmap();

            canvas.drawBitmap(bitmap, null, lineRect, linePaint);
        }
        //postInvalidateDelayed(10L);//区别？？
        postInvalidateDelayed(10L, (int) frame.left, (int) frame.top, (int) frame.right, (int) frame.bottom);
    }

    private void drawLine(Canvas canvas, Paint paint, Path path, Line firstLine, Line secondLine) {
        path.reset();
        path.moveTo(firstLine.startPoint.x, firstLine.startPoint.y);
        path.lineTo(firstLine.endPoint.x, firstLine.endPoint.y);
        path.lineTo(secondLine.endPoint.x, secondLine.endPoint.y);
        canvas.drawPath(path, paint);
    }

    public void setOnTopBottomClickListener(OnTopBottomClickListener onTopBottomClickListener) {
        mOnTopBottomClickListener = onTopBottomClickListener;
    }

    public void setOnRectCenterScrollListener(OnRectCenterScrollListener onRectCenterScrollListener) {
        mOnRectCenterScrollListener = onRectCenterScrollListener;
    }

    interface OnTopBottomClickListener {

        void onTopClick(YouZanQrcodeView view);

        void onBottomClick(YouZanQrcodeView view);
    }

    interface OnRectCenterScrollListener {

        void onRectCenterScroll(YouZanQrcodeView view, float centerX, float centerY, float centerXRatio, float centerYRatio);
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
