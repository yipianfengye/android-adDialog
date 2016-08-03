package com.uuch.adlibrary;

import android.app.Activity;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.flyco.pageindicator.indicator.FlycoPageIndicaor;
import com.uuch.adlibrary.bean.AdInfo;
import com.uuch.adlibrary.utils.DisplayUtil;

import java.util.List;

/**
 * Created by Administrator on 2015/10/20 0020.
 * 首页广告管理类
 */
public class AdManager {

    private Activity context;
    private DisplayMetrics displayMetrics = new DisplayMetrics();
    private View contentView;
    private ViewPager viewPager;
    private RelativeLayout adRootContent;
    private AdAdapter adAdapter;
    private FlycoPageIndicaor mIndicator;
    private AnimDialogUtils animDialogUtils;
    List<AdInfo> advInfoListList;
    /**
     * 广告弹窗距离两侧的距离-单位(dp)
     */
    private int padding = 44;
    /**
     * 广告弹窗的宽高比
     */
    private float widthPerHeight = 0.75f;

    // 弹窗背景是否透明
    private boolean isAnimBackViewTransparent = false;
    // 弹窗是否可关闭
    private boolean isDialogCloseable = true;
    // 弹窗关闭点击事件
    private View.OnClickListener onCloseClickListener = null;
    // 设置弹窗背景颜色
    private int backViewColor = Color.parseColor("#bf000000");
    // 弹性动画弹性参数
    private double bounciness = AdConstant.BOUNCINESS;
    // 弹性动画速度参数
    private double speed = AdConstant.SPEED;

    private OnImageClickListener onImageClickListener = null;


    public AdManager(Activity context, List<AdInfo> advInfoListList) {
        this.context = context;
        this.advInfoListList = advInfoListList;
    }

    private View.OnClickListener imageOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            AdInfo advInfo = (AdInfo) view.getTag();
            if (advInfo != null && onImageClickListener != null) {
                onImageClickListener.onImageClick(view, advInfo);
            }
        }
    };

    /**
     * 开始执行显示广告弹窗的操作
     * @param animType
     */
    public void showAdDialog(final int animType) {

        contentView = LayoutInflater.from(context).inflate(R.layout.ad_dialog_content_layout, null);
        adRootContent = (RelativeLayout) contentView.findViewById(R.id.ad_root_content);

        viewPager = (ViewPager) contentView.findViewById(R.id.viewPager);
        mIndicator = (FlycoPageIndicaor) contentView.findViewById(R.id.indicator);

        adAdapter = new AdAdapter();
        viewPager.setAdapter(adAdapter);

        mIndicator.setViewPager(viewPager);
        isShowIndicator();

        animDialogUtils = AnimDialogUtils.getInstance(context)
                .setAnimBackViewTransparent(isAnimBackViewTransparent)
                .setDialogCloseable(isDialogCloseable)
                .setDialogBackViewColor(backViewColor)
                .setOnCloseClickListener(onCloseClickListener)
                .initView(contentView);
        setRootContainerHeight();

        // 延迟1s展示，为了避免ImageLoader还为加载完缓存图片时就展示了弹窗的情况
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                animDialogUtils.show(animType, bounciness, speed);
            }
        }, 1000);
    }

    /**
     * 开始执行销毁弹窗的操作
     */
    public void dismissAdDialog() {
        animDialogUtils.dismiss(AdConstant.ANIM_STOP_DEFAULT);
    }


    private void setRootContainerHeight() {

        context.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int widthPixels = displayMetrics.widthPixels;
        int totalPadding = DisplayUtil.dip2px(context, padding * 2);
        int width = widthPixels - totalPadding;
        final int height = (int) (width / widthPerHeight);
        ViewGroup.LayoutParams params = adRootContent.getLayoutParams();
        params.height = height;
    }

    /**
     * 根据页面数量，判断是否显示Indicator
     */
    private void isShowIndicator() {
        if (advInfoListList.size() > 1) {
            mIndicator.setVisibility(View.VISIBLE);
        } else {
            mIndicator.setVisibility(View.INVISIBLE);
        }
    }

    class AdAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return advInfoListList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            AdInfo advInfo = advInfoListList.get(position);

            // SimpleDraweeView simpleDraweeView = new SimpleDraweeView(context);
            ImageView imageView = new ImageView(context);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            container.addView(imageView, params);
            imageView.setTag(advInfo);
            imageView.setOnClickListener(imageOnClickListener);


            /*Uri uri = Uri.parse(advInfo.getActivityImg());
            simpleDraweeView.setImageURI(uri);*/
            imageView.setBackgroundResource(R.drawable.adrawable);


            return imageView;
        }
    }



    // ######################## 点击事件处理操作类 ########################

    /**
     * ViewPager每一项的单击事件
     */
    public interface OnImageClickListener {

        public void onImageClick(View view, AdInfo advInfo);

    }

    // ######################## get set方法 #########################

    /**
     * 设置弹窗距离屏幕左右两侧的距离
     * @param padding
     * @return
     */
    public AdManager setPadding(int padding) {
        this.padding = padding;

        return this;
    }

    /**
     * 设置弹窗宽高比
     * @param widthPerHeight
     * @return
     */
    public AdManager setWidthPerHeight(float widthPerHeight) {
        this.widthPerHeight = widthPerHeight;

        return this;
    }

    /**
     * 设置ViewPager Item点击事件
     * @param onImageClickListener
     * @return
     */
    public AdManager setOnImageClickListener(OnImageClickListener onImageClickListener) {
        this.onImageClickListener = onImageClickListener;

        return this;
    }

    /**
     * 设置背景是否透明
     * @param animBackViewTransparent
     * @return
     */
    public AdManager setAnimBackViewTransparent(boolean animBackViewTransparent) {
        isAnimBackViewTransparent = animBackViewTransparent;

        return this;
    }

    /**
     * 设置弹窗关闭按钮是否可见
     * @param dialogCloseable
     * @return
     */
    public AdManager setDialogCloseable(boolean dialogCloseable) {
        isDialogCloseable = dialogCloseable;

        return this;
    }

    /**
     * 设置弹窗关闭按钮点击事件
     * @param onCloseClickListener
     * @return
     */
    public AdManager setOnCloseClickListener(View.OnClickListener onCloseClickListener) {
        this.onCloseClickListener = onCloseClickListener;

        return this;
    }

    /**
     * 设置弹窗背景颜色
     * @param backViewColor
     * @return
     */
    public AdManager setBackViewColor(int backViewColor) {
        this.backViewColor = backViewColor;

        return this;
    }

    /**
     * 设置弹窗弹性动画弹性参数
     * @param bounciness
     * @return
     */
    public AdManager setBounciness(double bounciness) {
        this.bounciness = bounciness;

        return this;
    }

    /**
     * 设置弹窗弹性动画速度参数
     * @param speed
     * @return
     */
    public AdManager setSpeed(double speed) {
        this.speed = speed;

        return this;
    }
}
