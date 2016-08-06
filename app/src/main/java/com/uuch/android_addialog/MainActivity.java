package com.uuch.android_addialog;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.uuch.adlibrary.AdConstant;
import com.uuch.adlibrary.bean.AdInfo;
import com.uuch.adlibrary.AdManager;
import com.uuch.adlibrary.transformer.DepthPageTransformer;
import com.uuch.adlibrary.transformer.RotateDownPageTransformer;
import com.uuch.adlibrary.transformer.ZoomOutPageTransformer;

import java.util.ArrayList;
import java.util.List;

/**
 * 测试广告活动弹窗demo
 */
public class MainActivity extends AppCompatActivity {

    private List<AdInfo> advList = null;
    private Spinner spinner = null;
    private Button button1 = null;
    private EditText editText = null;
    private Button button2 = null;
    private Button button3 = null;
    private Button button4 = null;
    private Button button5 = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initData();

        initView();
        initListener();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        advList = new ArrayList<>();
        AdInfo adInfo = new AdInfo();
        adInfo.setActivityImg("https://raw.githubusercontent.com/yipianfengye/android-adDialog/master/images/testImage1.png");
        advList.add(adInfo);

        adInfo = new AdInfo();
        adInfo.setActivityImg("https://raw.githubusercontent.com/yipianfengye/android-adDialog/master/images/testImage2.png");
        advList.add(adInfo);
    }

    /**
     * 初始化组件
     */
    private void initView() {
        spinner = (Spinner) findViewById(R.id.spinner);
        List<DataBean> mList = new ArrayList<>();
        mList.add(new DataBean(-1, "请选择广告弹窗动画类型"));
        mList.add(new DataBean(AdConstant.ANIM_DOWN_TO_UP, "从下至上弹出广告弹窗"));
        mList.add(new DataBean(AdConstant.ANIM_UP_TO_DOWN, "从上至下弹出广告弹窗"));
        mList.add(new DataBean(AdConstant.ANIM_LEFT_TO_RIGHT, "从左至右弹出广告弹窗"));
        mList.add(new DataBean(AdConstant.ANIM_RIGHT_TO_LEFT, "从右至左弹出广告弹窗"));
        mList.add(new DataBean(AdConstant.ANIM_UPLEFT_TO_CENTER, "从左上弹出广告弹窗"));
        mList.add(new DataBean(AdConstant.ANIM_UPRIGHT_TO_CENTER, "从右上弹出广告弹窗"));
        mList.add(new DataBean(AdConstant.ANIM_DOWNLEFT_TO_CENTER, "从左下弹出广告弹窗"));
        mList.add(new DataBean(AdConstant.ANIM_DOWNRIGHT_TO_CENTER, "从右下弹出广告弹窗"));
        SpinnerAdapter spinnerAdapter = new SpinnerAdapter(mList, this);
        spinner.setAdapter(spinnerAdapter);


        editText = (EditText) findViewById(R.id.edittext);
        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);
        button3 = (Button) findViewById(R.id.button3);
        button4 = (Button) findViewById(R.id.button4);
        button5 = (Button) findViewById(R.id.button5);

    }

    /**
     * 初始化事件监听
     */
    private void initListener() {

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                AdManager adManager = new AdManager(MainActivity.this, advList);
                adManager.setOverScreen(true)
                        .setPageTransformer(new DepthPageTransformer());
                switch (position) {
                    /**
                     * 从下至上弹出广告弹窗
                     */
                    case 1:
                        adManager.showAdDialog(AdConstant.ANIM_DOWN_TO_UP);
                        break;
                    /**
                     * 从上至下弹出广告弹窗
                     */
                    case 2:
                        adManager.showAdDialog(AdConstant.ANIM_UP_TO_DOWN);
                        break;
                    /**
                     * 从左向右弹窗广告弹窗
                     */
                    case 3:
                        adManager.showAdDialog(AdConstant.ANIM_LEFT_TO_RIGHT);
                        break;
                    /**
                     * 从右向左弹出广告弹窗
                     */
                    case 4:
                        adManager.showAdDialog(AdConstant.ANIM_RIGHT_TO_LEFT);
                        break;
                    /**
                     * 从左上弹出广告弹窗
                     */
                    case 5:
                        adManager.showAdDialog(AdConstant.ANIM_UPLEFT_TO_CENTER);
                        break;
                    /**
                     * 从右上弹出广告弹窗
                     */
                    case 6:
                        adManager.showAdDialog(AdConstant.ANIM_UPRIGHT_TO_CENTER);
                        break;
                    /**
                     * 从左下弹窗广告弹窗
                     */
                    case 7:
                        adManager.showAdDialog(AdConstant.ANIM_DOWNLEFT_TO_CENTER);
                        break;
                    /**
                     * 从右下弹出广告弹窗
                     */
                    case 8:
                        adManager.showAdDialog(AdConstant.ANIM_DOWNRIGHT_TO_CENTER);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdManager adManager = new AdManager(MainActivity.this, advList);
                String result = editText.getText().toString();
                if (TextUtils.isEmpty(result)) {
                    Toast.makeText(MainActivity.this, "请输入弹出动画的角度!", Toast.LENGTH_SHORT).show();
                    return;
                }

                adManager.setOnImageClickListener(new AdManager.OnImageClickListener() {
                    @Override
                    public void onImageClick(View view, AdInfo advInfo) {
                        Toast.makeText(MainActivity.this, "您点击了ViewPagerItem...", Toast.LENGTH_SHORT).show();
                    }
                });
                adManager.showAdDialog(Integer.parseInt(result));
            }
        });

        /**
         * 自定义设置广告活动弹窗距离屏幕两侧距离以及宽高比
         */
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdManager adManager = new AdManager(MainActivity.this, advList);

                adManager.setOnImageClickListener(new AdManager.OnImageClickListener() {
                    @Override
                    public void onImageClick(View view, AdInfo advInfo) {
                        Toast.makeText(MainActivity.this, "您点击了ViewPagerItem...", Toast.LENGTH_SHORT).show();
                    }
                })
                .setPadding(100)
                .setWidthPerHeight(0.5f)
                .showAdDialog(AdConstant.ANIM_UP_TO_DOWN);
            }
        });

        /**
         * 自定义弹窗背景颜色,弹窗是否覆盖全屏,关闭按钮是否显示等
         */
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdManager adManager = new AdManager(MainActivity.this, advList);

                adManager.setOnImageClickListener(new AdManager.OnImageClickListener() {
                    @Override
                    public void onImageClick(View view, AdInfo advInfo) {
                        Toast.makeText(MainActivity.this, "您点击了ViewPagerItem...", Toast.LENGTH_SHORT).show();
                    }
                })
                .setBackViewColor(Color.parseColor("#AA333333"))
                .setDialogCloseable(false)
                .showAdDialog(AdConstant.ANIM_UP_TO_DOWN);
            }
        });

        /**
         * 自定义设定弹窗弹性参数和速度参数
         */
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdManager adManager = new AdManager(MainActivity.this, advList);

                adManager.setOnImageClickListener(new AdManager.OnImageClickListener() {
                    @Override
                    public void onImageClick(View view, AdInfo advInfo) {
                        Toast.makeText(MainActivity.this, "您点击了ViewPagerItem...", Toast.LENGTH_SHORT).show();
                    }
                })
                .setBounciness(20)
                .setSpeed(4)
                .showAdDialog(AdConstant.ANIM_UP_TO_DOWN);
            }
        });

        /**
         * 自定义设置弹窗ViewPager滑动动画
         */
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdManager adManager = new AdManager(MainActivity.this, advList);

                adManager.setOnImageClickListener(new AdManager.OnImageClickListener() {
                    @Override
                    public void onImageClick(View view, AdInfo advInfo) {
                        Toast.makeText(MainActivity.this, "您点击了ViewPagerItem...", Toast.LENGTH_SHORT).show();
                    }
                })
                .setPageTransformer(new RotateDownPageTransformer())
                .showAdDialog(AdConstant.ANIM_UP_TO_DOWN);
            }
        });
    }

}
