package com.uuch.adlibrary;

/**
 * Created by aaron on 16/8/2.
 * 广告、活动弹窗静态变量
 */
public class AdConstant {

    // ####################### 弹出动画效果 ###########################
    /**
     * 广告活动弹窗动画-从上至下
     */
    public static final int ANIM_UP_TO_DOWN = -11;
    /**
     * 广告活动弹窗动画-从下至上
     */
    public static final int ANIM_DOWN_TO_UP = -12;
    /**
     * 广告活动弹窗动画-从左至右
     */
    public static final int ANIM_LEFT_TO_RIGHT = -13;
    /**
     * 广告活动弹窗动画-从右至左
     */
    public static final int ANIM_RIGHT_TO_LEFT = -14;
    /**
     * 广告活动弹窗动画-从左上弹出
     */
    public static final int ANIM_UPLEFT_TO_CENTER = -15;
    /**
     * 广告活动弹窗动画-从右上弹出
     */
    public static final int ANIM_UPRIGHT_TO_CENTER = -16;
    /**
     * 广告活动弹窗动画-从左下弹出
     */
    public static final int ANIM_DOWNLEFT_TO_CENTER = -17;
    /**
     * 广告活动弹窗动画-从右下弹出
     */
    public static final int ANIM_DOWNRIGHT_TO_CENTER = -18;

    /**
     * 判断是否是常量类型动画
     * @param animType
     * @return
     */
    public static boolean isConstantAnim(int animType) {
        if (animType == ANIM_UP_TO_DOWN || animType == ANIM_DOWN_TO_UP
                || animType == ANIM_LEFT_TO_RIGHT || animType == ANIM_RIGHT_TO_LEFT
                || animType == ANIM_UPLEFT_TO_CENTER || animType == ANIM_UPRIGHT_TO_CENTER
                || animType == ANIM_DOWNLEFT_TO_CENTER || animType == ANIM_DOWNRIGHT_TO_CENTER) {
            return true;
        }

        return false;
    }

    /**
     * 判断是否是自定义角度动画
     * @param animType
     * @return
     */
    public static boolean isCircleAnim(int animType) {
        if (animType >= 0 && animType <= 360) {
            return true;
        }

        return false;
    }

    // ########################## 弹出动画效果 #################################

    // ########################## 退出动画效果 #################################

    /**
     * 退出动画-默认无动画效果
     */
    public static final int ANIM_STOP_DEFAULT = 1;
    /**
     * 退出动画-默认渐变透明效果
     */
    public static final int ANIM_STOP_TRANSPARENT = 2;

    // ########################## 退出动画效果 #################################


    // ########################## 弹性动画参数 ##################################
    /**
     * 弹性动画弹性参数
     */
    public static final double BOUNCINESS = 8;
    /**
     * 弹性动画速度参数
     */
    public static final double SPEED = 2;


}
