package com.uuch.android_addialog;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.uuch.adlibrary.AdConstant;
import com.uuch.adlibrary.AdInfo;
import com.uuch.adlibrary.AdManager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public List<AdInfo> advList = null;
    public Button button1 = null;
    public Button button2 = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initData();

        initView();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        advList = new ArrayList<>();
        AdInfo adInfo = new AdInfo();
        adInfo.setActivityImg("http://uu-image.oss-cn-hangzhou.aliyuncs.com/160616/8409037842A6BDA500B698A99F7FE5A10C106D23.png");
        adInfo.setAdId("1");
        advList.add(adInfo);

        adInfo = new AdInfo();
        adInfo.setActivityImg("http://uu-image.oss-cn-hangzhou.aliyuncs.com/160714/443619810B576C3A0287756EF7B789EC9A11DC33.png");
        adInfo.setAdId("2");
        advList.add(adInfo);
    }

    /**
     * 初始化组件
     */
    private void initView() {
        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);

        /**
         * 从下至上弹出广告弹窗
         */
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdManager adManager = new AdManager(MainActivity.this, advList);
                adManager.showAdDialog(AdConstant.ANIM_DOWN_TO_UP);
            }
        });

        /**
         * 从上至下弹出广告弹窗
         */
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdManager adManager = new AdManager(MainActivity.this, advList);
                adManager.showAdDialog(AdConstant.ANIM_UP_TO_DOWN);
            }
        });
    }
}
