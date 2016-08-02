package com.uuch.android_addialog;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.uuch.adlibrary.AdInfo;
import com.uuch.adlibrary.AdManager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public Button button1 = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    /**
     * 初始化组件
     */
    private void initView() {
        button1 = (Button) findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<AdInfo> advList = new ArrayList<>();
                AdInfo adInfo = new AdInfo();
                adInfo.setActivityImg("http://uu-image.oss-cn-hangzhou.aliyuncs.com/160616/8409037842A6BDA500B698A99F7FE5A10C106D23.png");
                adInfo.setAdId("1");
                advList.add(adInfo);

                adInfo = new AdInfo();
                adInfo.setActivityImg("http://uu-image.oss-cn-hangzhou.aliyuncs.com/160714/443619810B576C3A0287756EF7B789EC9A11DC33.png");
                advList.add(adInfo);

                AdManager adManager = new AdManager(MainActivity.this, advList);
                adManager.showAdDialog();
            }
        });
    }
}
