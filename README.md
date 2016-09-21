# android-adDialog

**更新日志**

- compile 1.0 2016/08/08 完成广告活动弹窗1.0版本

- compile 1.0 2016/08/08 删除demo中约束引用（否则AS2.2以下编译有问题）



在介绍具体的使用说明之前，我们先看一下简单的实现效果：
<br><br>![image](https://github.com/yipianfengye/android-adDialog/blob/master/images/ezgif.com-video-to-gif1.gif)

**使用说明**

- 显示一个默认广告弹窗，支持单广告活动、多广告活动，当弹窗显示多广告是默认显示底部小圆圈，当显示单活动时默认不显示底部小圆圈；

- 默认支持弹窗从上，下，左，右，左上，右上，左下，右下等八个方向弹出，更可以设置从任意指定的角度（0-360）弹出；

- 默认继承了弹性动画效果，可以设定弹性动画弹性参数，动画速度等；

- 支持对弹窗背景透明色的更改，支持对弹窗透明，支持设置弹窗背景全屏显示或者是只在内容区域显示；

- 支持设定弹窗宽高比，支持设置弹窗距离屏幕两侧距离，支持设置关闭按钮是否显示；

- 支持对关闭按钮点击事件的回调，对每一项广告活动点击事件的回调等；

- 支持对ViewPager滑动动画效果的设定，支持自定义ViewPager滑动动画效果；

**API说明：**

所有的设置参数均返回AdManager对象，所以可以直接链式调用。
```
AdManager adManager = new AdManager(MainActivity.this, advList);
aDMamager.
/**
 * 设置弹窗背景全屏显示还是在内容区域显示
 */
.setOverScreen(true)
/**
 * 设置ViewPager的滑动动画
 */
.setPageTransformer(new DepthPageTransformer())
/**
 * 设置弹窗距离屏幕两侧的距离（单位dp）
 */
.setPadding(100)
/**
 * 设置弹窗的宽高比
 */
.setWidthPerHeight(0.75f)
/**
 * 设置弹窗的背景色（当弹窗背景设置透明时，此设置失效）
 */
.setBackViewColor(Color.parseColor("#AA333333"))
/**
 * 设置弹窗背景是否透明
 */
.setAnimBackViewTransparent(true)
/**
 * 设置弹窗关闭图标是否可见
 */
.setDialogCloseable(false)
/**
 * 设置弹窗弹性滑动弹性值
 */
.setBounciness(15)
/**
 * 设置弹窗弹性滑动速度值
 */
.setSpeed(5)
/**
 * 设定弹窗点击事件回调
 */
.setOnImageClickListener(new AdManager.OnImageClickListener() {})
/**
 * 设定关闭按钮点击事件回调
 */
.setOnCliseClickListener(new OnClickListener() {})
/**
 * 开始执行弹窗的显示操作，可传值为0-360，0表示从右开始弹出，逆时针方向，也可以传入自定义的方向值
 */
.showAdDialog(AdConstant.ANIM_UP_TO_DOWN)
```

**使用方式：**

- 在module的build.gradle中执行compile操作

```
compile 'cn.yipianfengye.android:ad-library:1.0'
```

- 在代码中初始化数据

```
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
```
这里只要是初始化图片的UI地址信息，方便我们的后续下载操作。

- 执行弹窗的初始化与现实操作

```
/**
 * 创建广告活动管理对象
 */
AdManager adManager = new AdManager(MainActivity.this, advList);
adManager.setOverScreen(true)
         .setPageTransformer(new DepthPageTransformer());
/**
 * 执行弹窗的显示操作
 */
adManager.showAdDialog(AdConstant.ANIM_DOWN_TO_UP);
```

怎么样是不是很简单？下面我们可以来看一下具体API。

**具体的API说明：**

- （1）自定义的弹窗弹出方向API

在执行AdManager的showAdDialog方法时，需要传递一个int型的animType参数，我们默认定义了八个该类型的参数，默认如下：

```
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
```

好吧，如果你觉得还不够好，我想让弹窗从右上侧30度角的弹出可以么？这也是支持的，只需要你传递的int型的animType的时候传递30就好了，如下：

```
/**
 * 执行弹窗的显示操作（参数的范围：0-360，0表示从右侧弹出，逆时针旋转）
 */
adManager.showAdDialog(30);
```

那么我们看一下执行效果呢：

![image](https://github.com/yipianfengye/android-adDialog/blob/master/images/ezgif.com-video-to-gif2.gif)

- （2）自定义弹窗距离屏幕两侧的距离以及弹窗的宽高比：

```
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
```

然后我们看一下执行效果：

![image](https://github.com/yipianfengye/android-adDialog/blob/master/images/ezgif.com-video-to-gif3.gif)

怎么样？是不是发现弹窗的宽高比和距离屏幕两侧的距离发生了变化？

- （3）自定义背景颜色，自定义弹窗是否覆盖全屏，关闭按钮是否显示

```
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
```

然后我们来看一下具体的实现效果：

![image](https://github.com/yipianfengye/android-adDialog/blob/master/images/ezgif.com-video-to-gif4.gif)

可以看到我们更改了弹窗的背景颜色以及未显示关闭按钮

- （4）自定义设置弹性动画弹性参数和速度参数

```
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
```

然后我们看一下动画效果：
<br>![image](https://github.com/yipianfengye/android-adDialog/blob/master/images/ezgif.com-video-to-gif5.gif)

为了明显的展示出动画效果，我们设置的弹性参数和速度参数比较大，是不是比较出来了这两个参数的作用？

- （5）自定义设置弹窗ViewPager滑动动画：

```
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
```

然后我们看一下弹窗的滑动动画：

![image](https://github.com/yipianfengye/android-adDialog/blob/master/images/ezgif.com-video-to-gif6.gif)

这里我内置了三种ViewPager的滑动动画效果：

```
DepthPageTransformer；
RotateDownPageTransformer；
ZoomOutPageTransformer；
```
我们还可以自定义实现：PageTransformer自定义出自己的滑动动画效果，更多关于PageTransformer的知识，可参考鸿洋大神的：<a href="http://blog.csdn.net/lmj623565791/article/details/40411921"> Android 实现个性的ViewPager切换动画 实战PageTransformer（兼容Android3.0以下）</a>

- （6）自定义关闭按钮点击事件回调，ViewPagerItem点击事件回调

```
/**
         * 自定义设置弹窗ViewPagerItem点击事件，关闭按钮点击事件回调
         */
        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdManager adManager = new AdManager(MainActivity.this, advList);

                adManager.setOnImageClickListener(new AdManager.OnImageClickListener() {
                    @Override
                    public void onImageClick(View view, AdInfo advInfo) {
                        Toast.makeText(MainActivity.this, "您点击了ViewPagerItem...", Toast.LENGTH_SHORT).show();
                    }
                })
                .setOnCloseClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(MainActivity.this, "您点击了关闭按钮...", Toast.LENGTH_SHORT).show();
                            }
                        })
                .showAdDialog(AdConstant.ANIM_UP_TO_DOWN);
            }
        });
```

好吧，以上就是广告活动弹窗的API，除了以上还可以添加其他的一些API，欢迎提出。

以上就是这个控件的相关内容，欢迎star和follow，也可参考我的blog：<a href="http://blog.csdn.net/qq_23547831/article/details/52121633">Github项目解析（十一）-->一个简单强大的自定义广告活动弹窗</a>
