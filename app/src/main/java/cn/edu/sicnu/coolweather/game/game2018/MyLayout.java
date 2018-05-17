package cn.edu.sicnu.coolweather.game.game2018;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.WindowManager;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import cn.edu.sicnu.coolweather.R;

/**
 * Created by pby on 2017/5/29.
 */

public class MyLayout extends ViewGroup {
    //handler对象，用来更新分数的
    private Handler handler = null;


    //手指按下的坐标
    private int startX = 0;
    private int startY = 0;

    //TextView背景图片
    private Drawable textViewBackground = null;

    //手指滑动的有效距离
    private static final int effectiveDistance = 10;
    //存储textView的矩阵
    private MyTextView textViewsFlag[][] = new MyTextView[4][4];

    //用来产生随机数的
    private Random random = null;

    //画笔对象，用来绘制布局上面的线条
    private Paint paint = null;
    //每一个textView显示的内容
    private List<String> textViewContent = Arrays.asList("2", "4", "8", "16", "32", "64", "128", "256", "512", "1024", "2048");

    //textView的宽度和高度
    private float textViewWidth = 0;
    private float textViewHeight = 0;
    //屏幕的宽度
    private float screenWidth = 0;

    //每一个TextView之间Margin
    private float textViewMargin = 0;
    private static final float DEFUALT_TEXTVIEW_MARGIN = 10;
    //布局上的线条宽度
    private static final float DEFAULT_STROKE_WIDTH = DEFUALT_TEXTVIEW_MARGIN;
    //线条的颜色
    private int strokeColor = 0;
    private static final int DEFAULT_STROKE_COLOR = Color.parseColor("#12000000");
    //textView显示的文字大小
    private float textSize = 0;
    private static final float  DEFAULT_TEXT_SIZE = 30;

    public MyLayout(Context context) {
        this(context, null);
    }

    public MyLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttr(context, attrs, defStyleAttr);
        init(context);
    }

    /**
     * 初始化相关的属性
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    private void initAttr(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.MyLayout, defStyleAttr, 0);
        int n = array.getIndexCount();
        for(int i = 0; i < n; i++)
        {
            int index = array.getIndex(i);
            switch(index)
            {
                case R.styleable.MyLayout_strokeColor:{
                    strokeColor = array.getColor(index, DEFAULT_STROKE_COLOR);
                    break;
                }
                case R.styleable.MyLayout_textViewMargin:{
                    //textViewMargin = dpToPX(array.getDimensionPixelSize(index, (int) DEFAULT_STROKE_WIDTH));
                    textViewMargin = (array.getDimensionPixelSize(index, (int) DEFAULT_STROKE_WIDTH));
                    break;
                }
                case R.styleable.MyLayout_textSize:{
                    //textSize = spToPX(array.getDimensionPixelSize(index, (int) DEFAULT_TEXT_SIZE));
                    textSize = (array.getDimensionPixelSize(index, (int) DEFAULT_TEXT_SIZE));

                    break;
                }
            }
        }
    }

    /**
     * 初始化相关变量
     * @param context
     */
    private void init(Context context) {

        paint = new Paint();
        //设置线条的宽度
        paint.setStrokeWidth(textViewMargin);
        //设置画笔的颜色
        paint.setColor(strokeColor);
        //设置画笔的cap和join
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeJoin(Paint.Join.ROUND);
        //设置画笔抗锯齿
        paint.setDither(true);
        paint.setAntiAlias(true);

        //初始化TextView矩阵
        initTextViewsFlag();
        //获得屏幕宽度
        screenWidth = getScreenWidth(context);
        //初始化random对象，并且将当前的时间设置为随机数的种子
        random = new Random(System.currentTimeMillis());
        //初始化TextView背景
        if (Build.VERSION.SDK_INT > 21) {
            textViewBackground = getResources().getDrawable(R.drawable.textview_bg, getContext().getTheme());
        }else{
            textViewBackground = getResources().getDrawable(R.drawable.textview_bg);
        }
    }
    /**
     * 获得当前手机屏幕的宽度
     * @param context
     * @return
     */
    private float getScreenWidth(Context context) {
        WindowManager windowManger = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        windowManger.getDefaultDisplay().getMetrics(metrics);
        return metrics.widthPixels;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        //初始化textView的宽和高
        textViewWidth = textViewHeight = (screenWidth  - 5 * textViewMargin) / 4.0F;
        initTextView();
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        super.onMeasure(widthSpec, heightSpec);
        setMeasuredDimension((int) screenWidth, (int) screenWidth);
    }

    /**
     * 初始化TextView矩阵，将所有的对象初始化为null
     */
    private void initTextViewsFlag() {
        for (int i = 0; i < textViewsFlag.length; i++) {
            Arrays.fill(textViewsFlag[i], null);
        }
    }

    /**
     * 初始化textViews
     */
    private void initTextView() {
        int index = 0;
        //随机产生4个TextView
        for (int i = 0; i < 4; i++) {
            //产生一个TextView内容在textViewContent中的索引
            index = randomGeneralContentIndex();
            //Log.i("main", "index= " + index);
            //产生一个TextView
            randomGeneralTextView(index);
        }
    }

    /**
     * 随机产生textView显示的内容在textViewContent数组中索引
     *
     * @return 产生的索引
     */
    private int randomGeneralContentIndex() {
        //range为随机数的范围(0 ~ 1)
        final int range = 2;
        int index = (int) (random.nextDouble() * range);
        return index;
    }

    /**
     * 随机产生一个TextView， 并且将产生的textView放在textViews数组中
     *
     * @param index 产生的textView显示内容的索引
     */
    private void randomGeneralTextView(int index) {
        //判断当前矩阵是否被填满，如果被填满的话，则不生成任何新的TextView
        if (isFill()) {
            return;
        }
        //range为column和row产生的范围(0 ~ 3)
        final int range = 4;
        int column = (int) (random.nextDouble() * range);
        int row = (int) (random.nextDouble() * range);
        //如果矩阵的当前位置被一个TextView填充了，则重新产生随机数
        while (textViewsFlag[row][column] != null) {
            column = (int) (random.nextDouble() * range);
            row = (int) (random.nextDouble() * range);
        }
        //创建一个TextView
        MyTextView textView = new MyTextView(getContext());
        //设置背景
        if(Build.VERSION.SDK_INT > 21) {
            textView.setBackground(textViewBackground);
        }else{
            textView.setBackgroundDrawable(textViewBackground);
        }
        //设置内容
        textView.setTextString(textViewContent.get(index));
        //设置字体大小
        textView.setTextSize1(textSize);

        //计算TextView在父布局中的位置
        //lc表示当前TextView左边离父布局左边的距离
        int lc = (int) (column * (textViewWidth + textViewMargin) + textViewMargin);
        //tc表示当前TextView上边离父布局上边的距离
        int tc = (int) (row * (textViewHeight + textViewMargin) + textViewMargin);
        //rc表示当前TextView右边离父布局左边的距离
        int rc = (int) (lc + textViewWidth);
        //bc表示当前TextView底边离父布局上边的距离
        int bc = (int) (tc + textViewHeight);
        addView(textView);
        //摆放TextView
        textView.layout(lc, tc, rc, bc);
        //将当前TextView缩放到0
        ObjectAnimator oa1 = ObjectAnimator.ofFloat(textView, "scaleX", 1F, 0F);
        oa1.setDuration(0);
        ObjectAnimator oa2 = ObjectAnimator.ofFloat(textView, "scaleY", 1F, 0F);
        oa2.setDuration(0);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(oa1, oa2);
        set.start();
        //将当前textView放在textView矩阵
        textViewsFlag[row][column] = textView;
    }

    /**
     * 判断当前textView矩阵是否被填充满
     * @return false表示未被填充满，true表示填充满
     */
    private boolean isFill() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (textViewsFlag[i][j] == null) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //绘制水平线
        drawHorizontalLine(canvas);
        //绘制竖直线
        drawVerticalLine(canvas);
    }

    /**
     * 绘制布局中水平线
     * @param canvas
     */
    private void drawHorizontalLine(Canvas canvas) {
        for (int i = 0; i < 5; i++) {
            //开始的y坐标
            int startY = (int) (i * (textViewWidth + textViewMargin) + textViewMargin / 2.0f);
            //paint.setStrokeWidth(textViewMargin);
            //绘制线条
            canvas.drawLine(0, startY, screenWidth, startY, paint);
        }
    }

    /**
     * 绘制布局中竖直线
     * @param canvas
     */
    private void drawVerticalLine(Canvas canvas) {
        for (int i = 0; i < 5; i++) {
            //开始的x坐标
            int startX = (int) (i * (textViewWidth + textViewMargin) + textViewMargin / 2.0F);
            //paint.setStrokeWidth(textViewMargin);
            canvas.drawLine(startX, 0, startX, screenWidth, paint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                //记录下手指点下的坐标
                startX = (int) event.getX();
                startY = (int) event.getY();
                break;
            }
            case MotionEvent.ACTION_UP: {
                //获得手指移动的距离
                int moveX = (int) (event.getX() - startX);
                int moveY = (int) (event.getY() - startY);
                int absMoveX = Math.abs(moveX);
                int absMoveY = Math.abs(moveY);
                //Log.i("main", "moveX = " + moveX + " moveY = " + moveY + " absMoveX = " + absMoveX + " absMoveY = " + absMoveY);
                //x方向移动的距离大于y方向移动的距离，判断为用户在在水平移动
                if (absMoveX > absMoveY) {
                    if (moveX < 0 && absMoveX > effectiveDistance) // 向左滑动
                    {
                        int count = 0;
                        for (int i = 0; i < 4; i++) {
                            count = 0;
                            for (int j = 0; j < 4; j++) {
                                if (textViewsFlag[i][j] != null) {
                                    MyTextView child = textViewsFlag[i][j];
                                    //水平移动能够移动的TextView
                                    smoothScrollHorizontal((int) (child.getX()), textViewMargin + count * (textViewWidth + textViewMargin), child);
                                    textViewsFlag[i][j] = null;
                                    textViewsFlag[i][count] = child;
                                    count++;
                                }
                            }
                        }
                        postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mergeHorizontlTextView(false);
                                int index = randomGeneralContentIndex();
                                randomGeneralTextView(index);
                                invalidate();
                            }
                        }, 300);
                    } else if (moveX > 0 && absMoveX > effectiveDistance) //向右滑动
                    {
                        int count = 0;
                        for (int i = 0; i < 4; i++) {
                            count = 0;
                            for (int j = 3; j >= 0; j--) {
                                if (textViewsFlag[i][j] != null) {
                                    MyTextView child = textViewsFlag[i][j];
                                    smoothScrollHorizontal(child.getX(), screenWidth - (count + 1) * (textViewMargin + textViewWidth), child);
                                    textViewsFlag[i][j] = null;
                                    textViewsFlag[i][3 - count] = child;
                                    count++;
                                }
                            }
                        }
                        //300毫秒之后，调用run方法
                        postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                //水平合并TextView
                                mergeHorizontlTextView(true);
                                //随机产生一个TextView
                                int index = randomGeneralContentIndex();
                                randomGeneralTextView(index);
                                //重绘界面
                                invalidate();
                            }
                        }, 300);
                    }
                } else if (absMoveX < absMoveY) {   // y方向移动的距离大于x方向移动的距离，判断为用户在竖直方向移动
                    if (moveY < 0 && absMoveY > effectiveDistance) // 向上滑动
                    {
                        int count = 0;
                        for (int i = 0; i < 4; i++) {
                            count = 0;
                            for (int j = 0; j < 4; j++) {
                                if (textViewsFlag[j][i] != null) {

                                    MyTextView child = textViewsFlag[j][i];
                                    smoothScrollVertical((int) child.getY(), textViewMargin + count * (textViewHeight + textViewMargin), child);
                                    textViewsFlag[j][i] = null;
                                    textViewsFlag[count][i] = child;
                                    count++;
                                }
                            }
                        }
                        postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mergeVerticalTextView(false);
                                int index = randomGeneralContentIndex();
                                randomGeneralTextView(index);
                                invalidate();
                            }
                        }, 300);
                    } else if (moveY > 0 && absMoveY > effectiveDistance) //向下滑动
                    {
                        int count = 0;
                        for (int i = 0; i < 4; i++) {
                            count = 0;
                            for (int j = 3; j >= 0; j--) {
                                if (textViewsFlag[j][i] != null) {
                                    MyTextView child = textViewsFlag[j][i];
                                    smoothScrollVertical((int) child.getY(), screenWidth - (count + 1) * (textViewMargin + textViewHeight), child);
                                    textViewsFlag[j][i] = null;
                                    textViewsFlag[3 - count][i] = child;
                                    count++;
                                }
                            }
                        }
                        postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mergeVerticalTextView(true);
                                int index = randomGeneralContentIndex();
                                randomGeneralTextView(index);
                                invalidate();
                            }
                        }, 300);
                    }

                }
                break;
            }
        }
        return true;
    }

    /**
     * 水平合并内容相同的相邻的TextView
     * @param direction  direction为true的话，表示向右滑动，合并从右边开始；为false，表示向左滑动，合并从左边开始
     *
     */
    private void mergeHorizontlTextView(boolean direction) {
        if (!direction) {
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 3; j++) {
                    if (textViewsFlag[i][j] != null && textViewsFlag[i][j + 1] != null && textViewsFlag[i][j].getTextString().equals(textViewsFlag[i][j + 1].getTextString())) {
                        MyTextView textView = textViewsFlag[i][j];
                        int index = textViewContent.indexOf(textView.getTextString());
                        if(index == textViewContent.size() - 1)
                        {
                            textView.setTextString(textViewContent.get(0));
                        }
                        else
                        {
                            textView.setTextString(textViewContent.get(index + 1));
                        }
                        smoothScrollHorizontal(textViewsFlag[i][j + 1].getX(), textViewsFlag[i][j + 1].getX() - (textViewMargin + textViewWidth), textViewsFlag[i][j + 1]);
                        textView.invalidate();
                        final int fi = i;
                        final int fj = j + 1;
                        final MyTextView textView1 = textViewsFlag[fi][fj];
                        postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                removeView(textView1);
                            }
                        }, 300);
                        textViewsFlag[i][j + 1] = null;
                        for (int k = j + 1; k < 4; k++) {
                            if (textViewsFlag[i][k] != null && textViewsFlag[i][k - 1] == null) {
                                textView = textViewsFlag[i][k];

                                smoothScrollHorizontal((int) textView.getX(), (int) textView.getX() - (textViewWidth + textViewMargin), textView);
                                textViewsFlag[i][k - 1] = textView;
                                textViewsFlag[i][k] = null;
                            }
                        }
                        if(handler !=  null)
                        {
                            Message message = Message.obtain();
                            message.arg1 = (index + 1) * 100;
                            handler.sendMessage(message);
                        }
                    }
                }

            }
        } else {
            for (int i = 0; i < 4; i++) {
                for (int j = 2; j >= 0; j--) {
                    if (textViewsFlag[i][j] != null && textViewsFlag[i][j + 1] != null && textViewsFlag[i][j].getTextString().equals(textViewsFlag[i][j + 1].getTextString())) {
                        MyTextView textView = textViewsFlag[i][j + 1];
                        int index = textViewContent.indexOf(textView.getTextString());
                        if(index == textViewContent.size() - 1)
                        {
                            textView.setTextString(textViewContent.get(0));
                        }
                        else
                        {
                            textView.setTextString(textViewContent.get(index + 1));
                        }
                        smoothScrollHorizontal(textViewsFlag[i][j].getX(), textViewsFlag[i][j ].getX() + (textViewMargin + textViewWidth), textViewsFlag[i][j]);
                        textView.invalidate();
                        final int fi = i;
                        final int fj = j;
                        final MyTextView textView1 = textViewsFlag[fi][fj];
                        postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                removeView(textView1);
                            }
                        }, 300);
                        textViewsFlag[i][j] = null;
                        for (int k = 0; k < j; k++) {
                            if (textViewsFlag[i][k] != null && textViewsFlag[i][k + 1] == null) {
                                textView = textViewsFlag[i][k];

                                smoothScrollHorizontal(textView.getX(), textView.getX() + (textViewWidth + textViewMargin), textView);
                                textViewsFlag[i][k + 1] = textView;
                                textViewsFlag[i][k] = null;
                            }
                        }
                        if(handler !=  null)
                        {
                            Message message = Message.obtain();
                            message.arg1 = (index + 1) * 100;
                            handler.sendMessage(message);
                        }
                    }
                }

            }
        }
        invalidate();
    }


    /**
     * 竖直合并内容相同的相邻的TextView
     * @param direction  direction为true的话，表示向下滑动，合并从下边开始；为false，表示向上滑动，合并从上边开始
     *
     */
    private void mergeVerticalTextView(boolean direction) {
        if (!direction) {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 4; j++) {
                    if (textViewsFlag[i][j] != null && textViewsFlag[i + 1][j] != null && textViewsFlag[i][j].getTextString().equals(textViewsFlag[i + 1][j].getTextString())) {
                        MyTextView textView = textViewsFlag[i][j];
                        int index = textViewContent.indexOf(textView.getTextString());
                        if(index == textViewContent.size() - 1)
                        {
                            textView.setTextString(textViewContent.get(0));
                        }
                        else
                        {
                            textView.setTextString(textViewContent.get(index + 1));
                        }
                        smoothScrollVertical(textViewsFlag[i + 1][j].getY(), textViewsFlag[i + 1][j].getY() - (textViewHeight + textViewMargin), textViewsFlag[i + 1][j]);
                        textView.invalidate();
                        final int fi = i + 1;
                        final int fj = j;
                        final MyTextView textView1 = textViewsFlag[fi][fj];
                        postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                removeView(textView1);

                            }
                        }, 300);
                        textViewsFlag[i + 1][j] = null;
                        for (int k = i + 1; k < 4; k++) {
                            if (textViewsFlag[k][j] != null && textViewsFlag[k - 1][j] == null) {
                                textView = textViewsFlag[k][j];
                                smoothScrollVertical(textView.getY(), textView.getY() - (textViewHeight + textViewMargin), textView);
                                textViewsFlag[k - 1][j] = textView;
                                textViewsFlag[k][j] = null;
                            }
                        }
                        if(handler !=  null)
                        {
                            Message message = Message.obtain();
                            message.arg1 = (index + 1) * 100;
                            handler.sendMessage(message);
                        }
                    }
                }
            }
        } else {
            for (int i = 2; i >= 0; i--) {
                for (int j = 0; j < 4; j++) {
                    if (textViewsFlag[i][j] != null && textViewsFlag[i + 1][j] != null && textViewsFlag[i][j].getTextString().equals(textViewsFlag[i + 1][j].getTextString())) {

                        MyTextView textView = textViewsFlag[i + 1][j];
                        int index = textViewContent.indexOf(textView.getTextString());
                        if(index == textViewContent.size() - 1)
                        {
                            textView.setTextString(textViewContent.get(0));
                        }
                        else
                        {
                            textView.setTextString(textViewContent.get(index + 1));
                        }
                        smoothScrollVertical(textViewsFlag[i ][j].getY(), textViewsFlag[i][j].getY() + (textViewHeight + textViewMargin), textViewsFlag[i][j]);
                        textView.invalidate();
                        final int fi = i;
                        final int fj = j;
                        final MyTextView textView1 = textViewsFlag[fi][fj];
                        postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                removeView(textView1);

                            }
                        }, 300);
                        textViewsFlag[i][j] = null;
                        for (int k = 0; k < i; k++) {
                            if (textViewsFlag[k][j] != null && textViewsFlag[k + 1][j] == null) {
                                textView = textViewsFlag[k][j];
                                smoothScrollVertical((int) textView.getY(), (int) textView.getY() + (textViewHeight + textViewMargin), textView);
                                textViewsFlag[k + 1][j] = textView;
                                textViewsFlag[k][j] = null;
                            }
                        }
                        if(handler !=  null)
                        {
                            Message message = Message.obtain();
                            message.arg1 = (index + 1) * 100;
                            handler.sendMessage(message);
                        }
                    }
                }
            }
        }
        invalidate();
    }

    /**
     * 水平滚动TextView
     * @param startX  开始的x坐标
     * @param endX  滚动后的x坐标
     * @param textView 需要滚动TextView
     */
    private void smoothScrollHorizontal(float startX, float endX, MyTextView textView) {
        ObjectAnimator oa = ObjectAnimator.ofFloat(textView, "x", startX, endX);
        oa.setDuration(200);
        oa.start();
    }
    /**
     * 竖直滚动TextView
     * @param startY  开始的y坐标
     * @param endY  滚动后的y坐标
     * @param textView 需要滚动TextView
     */
    private void smoothScrollVertical(float startY, float endY, MyTextView textView) {
        ObjectAnimator oa = ObjectAnimator.ofFloat(textView, "y", startY, endY);
        oa.setDuration(200);
        oa.start();
    }

    public void setHandler(Handler handler)
    {
        this.handler = handler;
    }
}
