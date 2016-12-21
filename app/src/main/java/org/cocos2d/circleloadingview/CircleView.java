package org.cocos2d.circleloadingview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class CircleView extends View {

    private Path path;
    private PathMeasure mPathMeasure;
    private float[] mCurrentPosition = new float[2];
    private float length;
    private Paint paint;
    private RectF rectF;
    private Paint circlePaint;
    private float endSweep;
    private float mCurrentSweep;
    private Paint arcPaint;

    public CircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    private void initPaint() {
        path = new Path();
        rectF = new RectF(40, 40, 800, 800);
        path.addOval(rectF, Path.Direction.CCW);
        mPathMeasure = new PathMeasure(path, false);
        length = mPathMeasure.getLength();
        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        circlePaint = new Paint();
        circlePaint.setStyle(Paint.Style.FILL);
        circlePaint.setColor(Color.GREEN);
        arcPaint = new Paint();
        arcPaint.setStyle(Paint.Style.STROKE);
        arcPaint.setColor(Color.YELLOW);
        arcPaint.setStrokeWidth(10);
        mPathMeasure.getPosTan(0, mCurrentPosition, null);
        mCurrentSweep=360/length;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(path, paint);
        canvas.drawArc(rectF, 0, -endSweep, false, arcPaint);
        canvas.drawCircle(mCurrentPosition[0], mCurrentPosition[1], 20, circlePaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startAnimation();
                break;
        }
        return true;
    }

    private void startAnimation() {
        ValueAnimator value = ValueAnimator.ofFloat(0, length);
        value.setDuration((int) length * 5);
        value.start();
        value.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (Float) animation.getAnimatedValue();
                // 获取当前点坐标封装到mCurrentPosition
                endSweep=value*mCurrentSweep;
                mPathMeasure.getPosTan(value, mCurrentPosition, null);
                invalidate();
            }
        });
    }
}
