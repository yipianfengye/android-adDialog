package com.uuch.adlibrary;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

/**
 * Created by aaron on 16/6/30.
 */
public class AnimFly2RightUp {
    /**
     * 缩小弹窗动画时长
     */
    public static final int SCALE_ANIM_TIME = 500;
    /**
     * 向右上角移动弹窗时长
     */
    public static final int TRANSLATION_ANIM_TIME = 500;
    /**
     * 弹窗的默认缩小比例
     */
    public static final float SCALE_DEFAULT_FRACTION = 1.0f;
    /**
     * 缩小弹窗时的缩小比例
     */
    public static final float SCALE_ONE_FRACTION = 0.1f;
    /**
     * 移动弹窗时弹窗的缩小比例
     */
    public static final float SCALE_TOW_FRACTION = 0.03f;

    /**
     * 开始执行弹窗的缩小渐变动画
     * @param view
     * @param onAnimEndListener
     */
    public static void startAnimFly(final View view, final AnimDialogUtils.OnAnimEndListener onAnimEndListener) {
        /**
         * 渐变缩小弹窗
         */
        ObjectAnimator objectAlpha = ObjectAnimator.ofFloat(view, "alpha", 1.0f, 0.7f);
        ObjectAnimator objectScaleX = ObjectAnimator.ofFloat(view, "scaleX", SCALE_DEFAULT_FRACTION, SCALE_ONE_FRACTION);
        ObjectAnimator objectScaleY = ObjectAnimator.ofFloat(view, "scaleY", SCALE_DEFAULT_FRACTION, SCALE_ONE_FRACTION);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(objectAlpha, objectScaleX, objectScaleY);
        animatorSet.setDuration(SCALE_ANIM_TIME);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                startAnimFlyToRgithUp(view, onAnimEndListener);
            }
        });
        animatorSet.start();
    }


    /**
     * 将弹窗移动到右上角
     * @param view
     * @param onAnimEndListener
     */
    public static void startAnimFlyToRgithUp(final View view, final AnimDialogUtils.OnAnimEndListener onAnimEndListener) {

        ObjectAnimator objectScaleX = ObjectAnimator.ofFloat(view, "scaleX", SCALE_ONE_FRACTION, SCALE_TOW_FRACTION);
        ObjectAnimator objectScaleY = ObjectAnimator.ofFloat(view, "scaleY", SCALE_ONE_FRACTION, SCALE_TOW_FRACTION);

        // 实现抛物线效果,获取一定位移的长度
        int [] translationReuslt = getTranslationWidthAndHeight(view);
        int widthX = translationReuslt[0];
        int heightY = translationReuslt[1];

        ValueAnimator objectAnimator = ValueAnimator.ofObject(new TypeEvaluator() {
            @Override
            public Object evaluate(float fraction, Object startValue, Object endValue) {
                Point pointStart = (Point) startValue;
                Point pointEnd = (Point) endValue;

                int x = (int)(pointStart.getX() + fraction * (pointEnd.getX() - pointStart.getX()));
                int y = (int)(pointStart.getY() + fraction * fraction * (pointEnd.getY() - pointStart.getY()));
                Point pointResult = new Point(x, y);
                return pointResult;
            }
        }, new Point(0, 0), new Point(widthX, heightY));

        objectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Point pointResult = (Point) animation.getAnimatedValue();
                view.setTranslationX(pointResult.getX());
                view.setTranslationY(pointResult.getY());
            }
        });

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(TRANSLATION_ANIM_TIME);
        animatorSet.setInterpolator(new DecelerateInterpolator(2.0f));
        animatorSet.playTogether(objectAnimator, objectScaleX, objectScaleY);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                onAnimEndListener.onAnimEndListener();
            }
        });
        animatorSet.start();
    }


    /**
     * 获取弹窗位移的位置
     * @param view
     * @return
     */
    public static int[] getTranslationWidthAndHeight(View view) {
        int[] result = new int[2];
        int widthX = (int)(DisplayUtil.screenWidthPx
                - (view.getX() + view.getWidth() * (SCALE_DEFAULT_FRACTION - SCALE_ONE_FRACTION) / 2)
                - view.getWidth() * (SCALE_ONE_FRACTION - SCALE_TOW_FRACTION) / 2
                - DisplayUtil.dip2px(view.getContext(), 30));
        int heightY = (int)(- view.getTop()
                - view.getHeight() * (SCALE_DEFAULT_FRACTION - SCALE_ONE_FRACTION) / 2
                - view.getHeight() * (SCALE_ONE_FRACTION - SCALE_TOW_FRACTION) / 2
                + DisplayUtil.dip2px(view.getContext(), 50));

        result[0] = widthX;
        result[1] = heightY;

        return result;
    }


    /**
     * 定义的实体类,用于保存坐标位置
     */
    static class Point {
        public int x;
        public int y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }
    }
}
