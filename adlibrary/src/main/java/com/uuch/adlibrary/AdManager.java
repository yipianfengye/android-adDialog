package com.uuch.adlibrary;

import android.app.Activity;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.flyco.pageindicator.indicator.FlycoPageIndicaor;
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
    private int padding = 44;
    private AdAdapter adAdapter;
    private FlycoPageIndicaor mIndicator;
    private AnimDialogUtils animDialogUtils;
    List<AdInfo> advInfoListList;
    private boolean isShowing = false;


    public AdManager(Activity context, List<AdInfo> advInfoListList) {
        this.context = context;
        this.advInfoListList = advInfoListList;

    }

    private View.OnClickListener imageOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            AdInfo advInfo = (AdInfo) view.getTag();
            if (advInfo != null) {

                Toast.makeText(context, "点击事件!!!", Toast.LENGTH_SHORT).show();
            }
        }
    };

    View.OnClickListener closeClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            isShowing = false;
        }
    };

    public boolean isShowing() {
        return isShowing;
    }

    public void showAdDialog(final int animType) {

        isShowing = true;

        contentView = LayoutInflater.from(context).inflate(R.layout.ad_dialog_content_layout, null);
        adRootContent = (RelativeLayout) contentView.findViewById(R.id.ad_root_content);

        viewPager = (ViewPager) contentView.findViewById(R.id.viewPager);
        mIndicator = (FlycoPageIndicaor) contentView.findViewById(R.id.indicator);

        adAdapter = new AdAdapter();
        viewPager.setAdapter(adAdapter);
        mIndicator.setViewPager(viewPager);
        isShowIndicator();

        animDialogUtils = AnimDialogUtils.getInstance(context).initView(contentView, closeClickListener);
        setRootContainerHeight();

        // 延迟1s展示，为了避免ImageLoader还为加载完缓存图片时就展示了弹窗的情况
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                animDialogUtils.show(animType);

            }
        }, 1000);
    }

    public void dismissAdDialog() {
        animDialogUtils.dismiss();
    }


    /**
     * 直接删除地图页的广告弹窗，此方法用在账户异常切换后，另一个账户首页加载的界面不是地图页，而切换之前的用户在地图页，且已经弹出了广告弹窗且未关闭
     * 这种场景，使用该方法删除地图页广告弹窗
     *
     * @param context
     */
    public static void removeAdDialog(Activity context) {
        if (context != null && context instanceof Activity) {
            ViewGroup androidContentView = (ViewGroup) context.getWindow().findViewById(Window.ID_ANDROID_CONTENT);
            View rootView = androidContentView.findViewWithTag(AnimDialogUtils.ANIM_DIALOG_TAG);
            if (rootView != null) {
                androidContentView.removeView(rootView);
            }
        }
    }

    private void setRootContainerHeight() {

        context.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int widthPixels = displayMetrics.widthPixels;
        int totalPadding = DisplayUtil.dip2px(context, padding * 2);
        int width = widthPixels - totalPadding;
        final int height = (int) (width / 3.0f * 4.0f);
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


}
