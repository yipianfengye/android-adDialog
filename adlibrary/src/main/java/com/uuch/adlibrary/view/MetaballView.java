package com.uuch.adlibrary.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Transformation;


import com.uuch.adlibrary.utils.DisplayUtil;

import java.util.ArrayList;

/**
 * 加载进度View
 */
public class MetaballView extends View {

    private Paint paint = new Paint();
    private float handle_len_rate = 2f;
    private float radius = 20;
    private final int ITEM_COUNT = 5;
    private final int ITEM_DIVIDER = 30;
    private final float SCALE_RATE = 0.3f;
    private float maxLength;
    private ArrayList<Circle> circlePaths = new ArrayList<>();
    private float mInterpolatedTime;
    private MoveAnimation wa;
    private Circle circle;

    public MetaballView(Context context) {
        super(context);
        init();
    }

    public MetaballView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MetaballView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();

    }

    private class Circle {
        float[] center;
        float radius;
    }

    public void setPaintMode(int mode) {
        paint.setStyle(mode == 0 ? Paint.Style.STROKE : Paint.Style.FILL);
        invalidate();
    }

    private void init() {
        radius = DisplayUtil.dip2px(getContext(), 8);
        paint.setColor(0xff4db9ff);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        Circle circlePath = new Circle();
        circlePath.center = new float[]{(radius + ITEM_DIVIDER), radius * (1f + SCALE_RATE)};
        circlePath.radius = radius / 4 * 3;
        circlePaths.add(circlePath);

        for (int i = 1; i < ITEM_COUNT; i++) {
            circlePath = new Circle();
            circlePath.center = new float[]{(radius * 2 + ITEM_DIVIDER) * i + getPaddingLeft(), radius * (1f + SCALE_RATE)};
            circlePath.radius = radius;
            circlePaths.add(circlePath);
        }
        maxLength = (radius * 2 + ITEM_DIVIDER) * ITEM_COUNT;
    }

    private float[] getVector(float radians, float length) {
        float x = (float) (Math.cos(radians) * length);
        float y = (float) (Math.sin(radians) * length);
        return new float[]{
                x, y
        };
    }

    private class MoveAnimation extends Animation {

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            super.applyTransformation(interpolatedTime, t);
            mInterpolatedTime = interpolatedTime;
            invalidate();
        }
    }

    /**
     * @param canvas          画布
     * @param j
     * @param i
     * @param v               控制两个圆连接时候长度，间接控制连接线的粗细，该值为1的时候连接线为直线
     * @param handle_len_rate
     * @param maxDistance
     */
    private void metaball(Canvas canvas, int j, int i, float v, float handle_len_rate, float maxDistance) {
        final Circle circle1 = circlePaths.get(i);
        final Circle circle2 = circlePaths.get(j);

        RectF ball1 = new RectF();
        ball1.left = circle1.center[0] - circle1.radius + getPaddingLeft();
        ball1.top = circle1.center[1] - circle1.radius;
        ball1.right = ball1.left + circle1.radius * 2;
        ball1.bottom = ball1.top + circle1.radius * 2;

        RectF ball2 = new RectF();
        ball2.left = circle2.center[0] - circle2.radius;
        ball2.top = circle2.center[1] - circle2.radius;
        ball2.right = ball2.left + circle2.radius * 2;
        ball2.bottom = ball2.top + circle2.radius * 2;

        float[] center1 = new float[]{
                ball1.centerX(),
                ball1.centerY()
        };
        float[] center2 = new float[]{
                ball2.centerX(),
                ball2.centerY()
        };
        float d = getDistance(center1, center2);

        float radius1 = ball1.width() / 2;
        float radius2 = ball2.width() / 2;
        float pi2 = (float) (Math.PI / 2);
        float u1, u2;


        if (d > maxDistance) {
//            canvas.drawCircle(ball1.centerX(), ball1.centerY(), circle1.radius, paint);
            canvas.drawCircle(ball2.centerX(), ball2.centerY(), circle2.radius, paint);
        } else {
            float scale2 = 1 + SCALE_RATE * (1 - d / maxDistance);
            float scale1 = 1 - SCALE_RATE * (1 - d / maxDistance);
            radius2 *= scale2;
//            radius1 *= scale1;
//            canvas.drawCircle(ball1.centerX(), ball1.centerY(), radius1, paint);
            canvas.drawCircle(ball2.centerX(), ball2.centerY(), radius2, paint);

        }

//        Log.d("Metaball_radius", "radius1:" + radius1 + ",radius2:" + radius2);
        if (radius1 == 0 || radius2 == 0) {
            return;
        }

        if (d > maxDistance || d <= Math.abs(radius1 - radius2)) {
            return;
        } else if (d < radius1 + radius2) {
            u1 = (float) Math.acos((radius1 * radius1 + d * d - radius2 * radius2) /
                    (2 * radius1 * d));
            u2 = (float) Math.acos((radius2 * radius2 + d * d - radius1 * radius1) /
                    (2 * radius2 * d));
        } else {
            u1 = 0;
            u2 = 0;
        }
//        Log.d("Metaball", "center2:" + Arrays.toString(center2) + ",center1:" + Arrays.toString(center1));
        float[] centermin = new float[]{center2[0] - center1[0], center2[1] - center1[1]};

        float angle1 = (float) Math.atan2(centermin[1], centermin[0]);
        float angle2 = (float) Math.acos((radius1 - radius2) / d);
        float angle1a = angle1 + u1 + (angle2 - u1) * v;
        float angle1b = angle1 - u1 - (angle2 - u1) * v;
        float angle2a = (float) (angle1 + Math.PI - u2 - (Math.PI - u2 - angle2) * v);
        float angle2b = (float) (angle1 - Math.PI + u2 + (Math.PI - u2 - angle2) * v);

//        Log.d("Metaball", "angle1:" + angle1 + ",angle2:" + angle2 + ",angle1a:" + angle1a + ",angle1b:" + angle1b + ",angle2a:" + angle2a + ",angle2b:" + angle2b);


        float[] p1a1 = getVector(angle1a, radius1);
        float[] p1b1 = getVector(angle1b, radius1);
        float[] p2a1 = getVector(angle2a, radius2);
        float[] p2b1 = getVector(angle2b, radius2);

        float[] p1a = new float[]{p1a1[0] + center1[0], p1a1[1] + center1[1]};
        float[] p1b = new float[]{p1b1[0] + center1[0], p1b1[1] + center1[1]};
        float[] p2a = new float[]{p2a1[0] + center2[0], p2a1[1] + center2[1]};
        float[] p2b = new float[]{p2b1[0] + center2[0], p2b1[1] + center2[1]};


//        Log.d("Metaball", "p1a:" + Arrays.toString(p1a) + ",p1b:" + Arrays.toString(p1b) + ",p2a:" + Arrays.toString(p2a) + ",p2b:" + Arrays.toString(p2b));

        float[] p1_p2 = new float[]{p1a[0] - p2a[0], p1a[1] - p2a[1]};

        float totalRadius = (radius1 + radius2);
        float d2 = Math.min(v * handle_len_rate, getLength(p1_p2) / totalRadius);
        d2 *= Math.min(1, d * 2 / (radius1 + radius2));
//        Log.d("Metaball", "d2:" + d2);
        radius1 *= d2;
        radius2 *= d2;

        float[] sp1 = getVector(angle1a - pi2, radius1);
        float[] sp2 = getVector(angle2a + pi2, radius2);
        float[] sp3 = getVector(angle2b - pi2, radius2);
        float[] sp4 = getVector(angle1b + pi2, radius1);
//        Log.d("Metaball", "sp1:" + Arrays.toString(sp1) + ",sp2:" + Arrays.toString(sp2) + ",sp3:" + Arrays.toString(sp3) + ",sp4:" + Arrays.toString(sp4));


        Path path1 = new Path();
        path1.moveTo(p1a[0], p1a[1]);
        path1.cubicTo(p1a[0] + sp1[0], p1a[1] + sp1[1], p2a[0] + sp2[0], p2a[1] + sp2[1], p2a[0], p2a[1]);
        path1.lineTo(p2b[0], p2b[1]);
        path1.cubicTo(p2b[0] + sp3[0], p2b[1] + sp3[1], p1b[0] + sp4[0], p1b[1] + sp4[1], p1b[0], p1b[1]);
        path1.lineTo(p1a[0], p1a[1]);
        path1.close();
        canvas.drawPath(path1, paint);

    }

    private float getLength(float[] b) {
        return (float) Math.sqrt(b[0] * b[0] + b[1] * b[1]);
    }

    private float getDistance(float[] b1, float[] b2) {
        float x = b1[0] - b2[0];
        float y = b1[1] - b2[1];
        float d = x * x + y * y;
        return (float) Math.sqrt(d);
    }


    //测试用
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                break;
//            case MotionEvent.ACTION_MOVE:
//                Circle circle = circlePaths.get(0);
//                circle.center[0] = event.getX();
//                circle.center[1] = event.getY();
//                invalidate();
//                break;
//            case MotionEvent.ACTION_UP:
//                break;
//        }
//
//        return true;
//    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        circle = circlePaths.get(0);
        circle.center[0] = maxLength * mInterpolatedTime;

        RectF ball1 = new RectF();
        ball1.left = circle.center[0] - circle.radius + getPaddingLeft();
        ball1.top = circle.center[1] - circle.radius;
        ball1.right = ball1.left + circle.radius * 2;
        ball1.bottom = ball1.top + circle.radius * 2;
        canvas.drawCircle(ball1.centerX(), ball1.centerY(), circle.radius, paint);


        for (int i = 1, l = circlePaths.size(); i < l; i++) {
            metaball(canvas, i, 0, 0.6f, handle_len_rate, radius * 4f);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(resolveSizeAndState((int) (ITEM_COUNT * (radius * 2 + ITEM_DIVIDER)) + getPaddingLeft() + getPaddingRight(), widthMeasureSpec, 0),
                resolveSizeAndState((int) (2 * radius * 1.4f), heightMeasureSpec, 0));
    }


    private void stopAnimation() {
        this.clearAnimation();
        postInvalidate();
    }

    private void startAnimation() {
        wa = new MoveAnimation();
        wa.setDuration(2500);
        wa.setInterpolator(new AccelerateDecelerateInterpolator());
        wa.setRepeatCount(Animation.INFINITE);
        wa.setRepeatMode(Animation.REVERSE);
        startAnimation(wa);
    }

    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);

        if (visibility == GONE || visibility == INVISIBLE) {
            stopAnimation();
        } else {
            startAnimation();
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        startAnimation();
    }

    @Override
    protected void onDetachedFromWindow() {
        stopAnimation();
        super.onDetachedFromWindow();
    }
}
