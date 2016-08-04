package com.uuch.adlibrary.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.malinskiy.materialicons.IconDrawable;
import com.malinskiy.materialicons.Iconify;
import com.uuch.adlibrary.R;

/**
 * 显示加载中、无数据、无网络时的公用界面
 */
public class ProgressLayout extends RelativeLayout {

    private static final String tag = "ProgressLayout";

    LayoutInflater inflater;
    View view;
    LayoutParams layoutParams;

    RelativeLayout loadingStateRelativeLayout;
    MetaballView loadingStateProgressBar;

    RelativeLayout emptyStateRelativeLayout;
    ImageView emptyStateImageView;

    RelativeLayout errorStateRelativeLayout;
    ImageView errorStateImageView;
    Button errorStateButton;

    int loadingStateBackgroundColor;

    int emptyStateBackgroundColor;

    int errorStateBackgroundColor;

    private State state = State.CONTENT;
    /**
     * 各种状态下的布局使用圆角的资源ID
     */
    private int cornerResId = -1;
    /**
     * 是否使用滑动返回
     */
    private boolean isUseSlideBack = false;
    /**
     * 滑动返回依附的Activity，finish()该Activity
     */
    private Activity attachActivity;

    private GestureDetector gestureDetector;

    private VelocityTracker mVelocityTracker;

    public ProgressLayout(Context context) {
        super(context);
    }

    public ProgressLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProgressLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ProgressLayout);

        loadingStateBackgroundColor =
                typedArray.getColor(R.styleable.ProgressLayout_progressLoadingStateBackgroundColor, Color.TRANSPARENT);

        emptyStateBackgroundColor =
                typedArray.getColor(R.styleable.ProgressLayout_progressEmptyStateBackgroundColor, Color.TRANSPARENT);

        errorStateBackgroundColor =
                typedArray.getColor(R.styleable.ProgressLayout_progressErrorStateBackgroundColor, Color.TRANSPARENT);

        typedArray.recycle();
    }

    public void setAttachActivity(Activity activity) {
        this.attachActivity = activity;
    }

    public void setUseSlideBack(boolean useSlideBack) {
        isUseSlideBack = useSlideBack;
        if (isUseSlideBack) {
            gestureDetector = new GestureDetector(getContext(), onGestureListener);
            mVelocityTracker = VelocityTracker.obtain();
        }
    }

    public void showContent() {
        switchState(State.CONTENT, null);
    }

    public void showLoading() {
        switchState(State.LOADING, null);
    }

    String loadingText;

    public void showLoading(String loadText) {
        loadingText = loadText;
        switchState(State.LOADING_TEXT, null);
    }


    public void showEmpty() {
        switchState(State.EMPTY, null);
    }


    public void showError(OnClickListener onClickListener) {
        switchState(State.ERROR, onClickListener);
    }

    public void showError(OnClickListener onClickListener, String errorMessage, String button) {
        this.errorMessage = errorMessage;
        this.errorButton = button;
        switchState(State.ERROR_SMALL, onClickListener);
    }
    public void showErrorSmall(OnClickListener onClickListener) {
        switchState(State.ERROR_SMALL, onClickListener);
    }


    String errorMessage, errorButton;

    public void showErrorSmall(OnClickListener onClickListener, String errorMessage, String button) {
        this.errorMessage = errorMessage;
        this.errorButton = button;
        switchState(State.ERROR_SMALL, onClickListener);
    }

    /**
     * Get which state is set
     *
     * @return State
     */
    public State getState() {
        return state;
    }

    /**
     * Check if content is shown
     *
     * @return boolean
     */
    public boolean isContent() {
        return state == State.CONTENT;
    }

    /**
     * Check if loading state is shown
     *
     * @return boolean
     */
    public boolean isLoading() {
        return state == State.LOADING;
    }

    /**
     * Check if empty state is shown
     *
     * @return boolean
     */
    public boolean isEmpty() {
        return state == State.EMPTY;
    }

    /**
     * Check if error state is shown
     *
     * @return boolean
     */
    public boolean isError() {
        return state == State.ERROR;
    }

    private void switchState(State state, OnClickListener onClickListener) {
        this.state = state;

        switch (state) {
            case CONTENT:
                hideLoadingView();
                hideEmptyView();
                hideErrorView();

                break;
            case LOADING:
                hideEmptyView();
                hideErrorView();
                setLoadingView();
                break;
            case LOADING_TEXT:

                hideEmptyView();
                hideErrorView();
                setLoadingView(loadingText);
                break;
            case EMPTY:
                hideLoadingView();
                hideErrorView();

                setEmptyView();

                break;
            case ERROR:
                hideLoadingView();
                hideEmptyView();

                setErrorView(onClickListener);

                break;
            case ERROR_SMALL:
                hideLoadingView();
                hideEmptyView();

                setErrorView4SmallLayout(onClickListener);
                break;

        }
    }

    private void setLoadingView() {
        if (loadingStateRelativeLayout == null) {
            view = inflater.inflate(R.layout.progress_loading_view, null);
            loadingStateRelativeLayout = (RelativeLayout) view.findViewById(R.id.loadingStateRelativeLayout);

            loadingStateProgressBar = (MetaballView) view.findViewById(R.id.loadingStateProgressBar);

            layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
//            layoutParams.addRule(CENTER_IN_PARENT);

            if (cornerResId != -1) {
                loadingStateRelativeLayout.setBackgroundResource(cornerResId);
            }
            addView(loadingStateRelativeLayout, layoutParams);
        } else {
            loadingStateRelativeLayout.setVisibility(VISIBLE);
        }
    }

    private void setLoadingView(String loadtext) {
        if (loadingStateRelativeLayout == null) {
            view = inflater.inflate(R.layout.progress_loading_view, null);
            loadingStateRelativeLayout = (RelativeLayout) view.findViewById(R.id.loadingStateRelativeLayout);

            loadingStateProgressBar = (MetaballView) view.findViewById(R.id.loadingStateProgressBar);
            TextView text = (TextView) view.findViewById(R.id.loading_text);
            text.setText(loadtext);
            layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
//            layoutParams.addRule(CENTER_IN_PARENT);

            if (cornerResId != -1) {
                loadingStateRelativeLayout.setBackgroundResource(cornerResId);
            }

            addView(loadingStateRelativeLayout, layoutParams);
        } else {
            loadingStateRelativeLayout.setVisibility(VISIBLE);
        }
    }

    private void setEmptyView() {
        if (emptyStateRelativeLayout == null) {
            ViewGroup contentContainer = (ViewGroup) findViewById(R.id.fl_content_container);

            view = inflater.inflate(R.layout.progress_empty_view, null);
            emptyStateRelativeLayout = (RelativeLayout) view.findViewById(R.id.emptyStateRelativeLayout);
            emptyStateImageView = (ImageView) view.findViewById(R.id.emptyStateImageView);

            Drawable emptyDrawable = new IconDrawable(getContext(), Iconify.IconValue.zmdi_shopping_basket)
                    .colorRes(android.R.color.white);

            emptyStateImageView.setImageDrawable(emptyDrawable);

            //Set background color if not TRANSPARENT
            if (emptyStateBackgroundColor != Color.TRANSPARENT) {
                emptyStateRelativeLayout.setBackgroundColor(emptyStateBackgroundColor);
            }

            layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            layoutParams.addRule(CENTER_IN_PARENT);

            if (cornerResId != -1) {
                emptyStateRelativeLayout.setBackgroundResource(cornerResId);
            }

            contentContainer.addView(emptyStateRelativeLayout, layoutParams);
        } else {
            emptyStateRelativeLayout.setVisibility(VISIBLE);
        }
    }

    /**
     * custom 定制化展示
     * 设置空数据展示页面
     */
    public void setEmptyView(String showText) {
        ViewGroup contentContainer = (ViewGroup) findViewById(R.id.fl_content_container);
        this.state = State.EMPTY;
        hideLoadingView();
        hideErrorView();
        view = inflater.inflate(R.layout.progress_empty_custom_view, null);
        emptyStateRelativeLayout = (RelativeLayout) view.findViewById(R.id.emptyStateRelativeLayout);
        TextView textView = (TextView) view.findViewById(R.id.empty_textview);
        if (!TextUtils.isEmpty(showText)) {
            textView.setText(showText);
        }

        layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.addRule(CENTER_IN_PARENT);

        if (cornerResId != -1) {
            emptyStateRelativeLayout.setBackgroundResource(cornerResId);
        }

        contentContainer.addView(view, layoutParams);
    }

    private View emptyView;

    /**
     * custom 定制化展示
     * 设置空数据展示页面
     */
    public void setEmptyView(View view) {
        if (emptyView == null) {
            ViewGroup contentContainer = (ViewGroup) findViewById(R.id.fl_content_container);
            this.state = State.EMPTY;
            hideLoadingView();
            hideErrorView();
            layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            layoutParams.addRule(CENTER_IN_PARENT);

            emptyView = view;
            contentContainer.addView(emptyView, layoutParams);
        } else {
            emptyView.setVisibility(VISIBLE);
        }
    }

    private void setErrorView(OnClickListener onClickListener) {
        if (errorStateRelativeLayout == null) {
            ViewGroup contentContainer = (ViewGroup) findViewById(R.id.fl_content_container);
            view = inflater.inflate(R.layout.progress_error_view, null);
            errorStateRelativeLayout = (RelativeLayout) view.findViewById(R.id.errorStateRelativeLayout);

            errorStateImageView = (ImageView) view.findViewById(R.id.errorStateImageView);
            errorStateButton = (Button) view.findViewById(R.id.errorStateButton);

            Drawable errorDrawable = new IconDrawable(getContext(), Iconify.IconValue.zmdi_wifi_off)
                    .colorRes(R.color.mc);

            errorStateImageView.setImageDrawable(errorDrawable);

            if (onClickListener != null) {
                errorStateButton.setOnClickListener(onClickListener);
            }

            layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            layoutParams.addRule(CENTER_IN_PARENT);

            if (cornerResId != -1) {
                errorStateRelativeLayout.setBackgroundResource(cornerResId);
            }

            contentContainer.addView(errorStateRelativeLayout, layoutParams);
        } else {
            errorStateRelativeLayout.setVisibility(VISIBLE);
        }
    }

    private void setErrorView4SmallLayout(OnClickListener onClickListener) {
        if (errorStateRelativeLayout == null) {
            ViewGroup contentContainer = (ViewGroup) findViewById(R.id.fl_content_container);
            view = inflater.inflate(R.layout.progress_error_view_small, null);
            errorStateRelativeLayout = (RelativeLayout) view.findViewById(R.id.errorStateRelativeLayout);
            TextView errorStateContentTextView = (TextView) view.findViewById(R.id.errorStateContentTextView);
            errorStateImageView = (ImageView) view.findViewById(R.id.errorStateImageView);
            errorStateButton = (Button) view.findViewById(R.id.errorStateButton);

            if (errorButton != null) {
                errorStateButton.setText(errorButton);
            }
            if (errorMessage != null) {
                errorStateContentTextView.setText(errorMessage);
            }
            Drawable errorDrawable = new IconDrawable(getContext(), Iconify.IconValue.zmdi_wifi_off)
                    .colorRes(R.color.mc);

            errorStateImageView.setImageDrawable(errorDrawable);

            if (onClickListener != null) {
                errorStateButton.setOnClickListener(onClickListener);
            }

            layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            layoutParams.addRule(CENTER_IN_PARENT);

            if (cornerResId != -1) {
                errorStateRelativeLayout.setBackgroundResource(cornerResId);
            }

            contentContainer.addView(errorStateRelativeLayout, layoutParams);
        } else {
            errorStateRelativeLayout.setVisibility(VISIBLE);
        }
    }


    private void hideLoadingView() {
        if (loadingStateRelativeLayout != null) {
            loadingStateRelativeLayout.setVisibility(GONE);
        }
    }

    private void hideEmptyView() {
        if (emptyStateRelativeLayout != null) {
            emptyStateRelativeLayout.setVisibility(GONE);
        }
    }

    private void hideErrorView() {
        if (errorStateRelativeLayout != null) {
            errorStateRelativeLayout.setVisibility(GONE);
        }
    }

    public static enum State {
        CONTENT, LOADING, EMPTY, ERROR, ERROR_SMALL, LOADING_TEXT
    }

    public void setCornerResId(int cornerResId) {
        this.cornerResId = cornerResId;
    }


    private int mStartX, mStartY;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean isIntercept = false;
        if (isUseSlideBack) {
            int x = (int) ev.getX();
            int y = (int) ev.getY();

            mVelocityTracker.addMovement(ev);

            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    isIntercept = false;
                    break;
                case MotionEvent.ACTION_MOVE:
                    int distanceX = x - mStartX;
                    int distanceY = y - mStartY;
                    mVelocityTracker.computeCurrentVelocity(1000);
                    float xVelocity = mVelocityTracker.getXVelocity();
//                Log.i(tag, "xVelocity:" + xVelocity);
                    if (distanceX > 0 && Math.abs(distanceX) > Math.abs(distanceY) && Math.abs(xVelocity) >= 1000) {
                        isIntercept = true;
                    } else {
                        isIntercept = false;
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    isIntercept = false;
                    break;
            }
//        Log.i(tag, "isIntercept:" + isIntercept);
            mStartX = x;
            mStartY = y;
        }

        return isIntercept;
    }

    @Override
    protected void onDetachedFromWindow() {
        if (mVelocityTracker != null) {
            mVelocityTracker.clear();
        }
        super.onDetachedFromWindow();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (gestureDetector != null) {
            return gestureDetector.onTouchEvent(event);
        }
        return false;
    }

    GestureDetector.OnGestureListener onGestureListener = new GestureDetector.SimpleOnGestureListener() {

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
//            Log.i(tag, "velocityX:" + velocityX + "\t velocityY:" + velocityY);
            if (velocityX > 1000 && Math.abs(velocityY) < 800) {
                if (attachActivity != null) {
                    attachActivity.onBackPressed();
                }
                return true;
            }
            return false;
        }
    };
}