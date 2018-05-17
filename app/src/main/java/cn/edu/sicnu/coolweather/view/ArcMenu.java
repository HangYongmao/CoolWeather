package cn.edu.sicnu.coolweather.view;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

import cn.edu.sicnu.coolweather.R;
import cn.edu.sicnu.coolweather.SettingActivity;
import cn.edu.sicnu.coolweather.WeatherActivity;
import cn.edu.sicnu.coolweather.gson.Weather;

/**
 * Created by HYM on 2018/5/15 0015.
 */

public class ArcMenu extends ViewGroup implements View.OnClickListener {

    private static final String TAG = "ArcMenu";

    private static final int POS_LEF_TOP = 0;
    private static final int POS_LEF_BOTTOM = 1;
    private static final int POS_RIGHT_TOP = 2;
    private static final int POS_RIGHT_BOTTOM = 3;
    private Position mPosition = Position.RIGHT_BOTTOM;
    private int mRadius;

    // 菜单的状态
    private Status mCurrentStatus = Status.CLOSE;

    // 菜单的主按钮
    private View mCButton;

    //
    private OnMenuItemClickListener mMenuItemClickListener;

    public enum Status {
        OPEN, CLOSE;
    }

    // 菜单的位置枚举类
    public enum Position {
        LEF_TOP, LEFT_BOTTOM, RIGHT_TOP, RIGHT_BOTTOM;
    }

    // 点击子菜单项的回调接口
    public interface OnMenuItemClickListener {
        void onClick(View view, int pos);
    }

    public void setOnMenuItemClickListener(OnMenuItemClickListener mMenuItemClickListener) {
        this.mMenuItemClickListener = mMenuItemClickListener;
    }

    public ArcMenu(Context context) {
        this(context, null);
    }

    public ArcMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ArcMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mRadius = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());

        // 获取自定义属性的值
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ArcMenu, defStyleAttr, 0);

        int pos = a.getInt(R.styleable.ArcMenu_position, 3);
        switch (pos) {
            case POS_LEF_TOP:
                mPosition = Position.LEF_TOP;
                break;

            case POS_LEF_BOTTOM:
                mPosition = Position.LEFT_BOTTOM;
                break;

            case POS_RIGHT_TOP:
                mPosition = Position.RIGHT_TOP;
                break;

            case POS_RIGHT_BOTTOM:
                mPosition = Position.RIGHT_BOTTOM;
                break;
        }
        mRadius = (int) a.getDimension(R.styleable.ArcMenu_radius, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics()));
        Log.d(TAG, "ArcMenu: position=" + mPosition + ", radius=" + mRadius);
        a.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            // 测量child
            measureChild(getChildAt(i), widthMeasureSpec, heightMeasureSpec);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (changed) {
            layoutCButton();
            int count = getChildCount();
            for (int i = 0; i < count - 1; i++) {
                View child = getChildAt(i + 1);

                child.setVisibility(View.GONE);

                int cl = (int) (mRadius * Math.sin(Math.PI / 2 / (count - 2) * i));
                int ct = (int) (mRadius * Math.cos(Math.PI / 2 / (count - 2) * i));

                int cWidth = child.getMeasuredWidth();
                int cHeight = child.getMeasuredHeight();

                // 如果菜单位置在底部 左下  右下
                if (mPosition == Position.LEFT_BOTTOM || mPosition == Position.RIGHT_BOTTOM) {
                    ct = getMeasuredHeight() - cHeight - ct;
                }

                // 右上 右下
                if (mPosition == Position.RIGHT_TOP || mPosition == Position.RIGHT_BOTTOM) {
                    cl = getMeasuredWidth() - cWidth - cl;
                }
                child.layout(cl, ct, cl + cWidth, ct + cHeight);
            }
        }

    }

    // 定位主菜单按钮
    private void layoutCButton() {
        mCButton = getChildAt(0);
        mCButton.setOnClickListener(this);
        int l = 0;
        int t = 0;
        int width = mCButton.getMeasuredWidth();
        int height = mCButton.getMeasuredHeight();

        switch (mPosition) {
            case LEF_TOP:
                l = 0;
                t = 0;
                break;

            case LEFT_BOTTOM:
                l = 0;
                t = getMeasuredHeight() - height;
                break;

            case RIGHT_TOP:
                l = getMeasuredWidth() - width;
                t = 0;
                break;

            case RIGHT_BOTTOM:
                l = getMeasuredWidth() - width;
                t = getMeasuredHeight() - height;
                break;
        }
        mCButton.layout(l, t, l + width, t + height);
    }

    @Override
    public void onClick(View view) {
        mCButton = findViewById(R.id.id_button);
        if (mCButton == null) {
            mCButton = getChildAt(0);
        }
//        rotateCButton(view, 0f, 360f, 300);
        toggleMenu(150);
        Log.d(TAG, "onClick: ");
    }

    public void toggleMenu(int duration) {
        // 为menuItem添加平移动画和旋转动画
        int count = getChildCount();
        for (int i = 0; i < count - 1; i++) {
            final View childView = getChildAt(i + 1);

            childView.setVisibility(View.VISIBLE);

            // end 0 0
            // start
            int cl = (int) (mRadius * Math.sin(Math.PI / 2 / (count - 2) * i));
            int ct = (int) (mRadius * Math.cos(Math.PI / 2 / (count - 2) * i));

            int xFlag = 1;
            int yFlag = 1;

            if (mPosition == Position.LEF_TOP || mPosition == Position.LEFT_BOTTOM) {
                xFlag = -1;
            }

            if (mPosition == Position.LEF_TOP || mPosition == Position.RIGHT_TOP) {
                yFlag = -1;
            }
            AnimationSet animSet = new AnimationSet(true);
            Animation tranAnim = null;
            if (mCurrentStatus == Status.CLOSE) {
                tranAnim = new TranslateAnimation(xFlag * cl, 0, yFlag * ct, 0);
                childView.setClickable(true);
                childView.setFocusable(true);
            } else {
                tranAnim = new TranslateAnimation(0, xFlag * cl, 0, yFlag * ct);
                childView.setClickable(false);
                childView.setFocusable(false);
            }

            tranAnim.setFillAfter(true);
            tranAnim.setDuration(duration);
            tranAnim.setStartOffset((i * 100) / count);

            tranAnim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    if (mCurrentStatus == Status.CLOSE) {
                        childView.setVisibility(View.GONE);
                    }
                }
            });

            // 旋转动画
            RotateAnimation rotateAnim = new RotateAnimation(0, 720, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            rotateAnim.setDuration(duration);
            rotateAnim.setFillAfter(true);

            animSet.addAnimation(rotateAnim);
            animSet.addAnimation(tranAnim);

            childView.startAnimation(animSet);

            // 子菜单点击事件
            final int pos = i + 1;
            childView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mMenuItemClickListener != null) {
                        mMenuItemClickListener.onClick(childView, pos);
                    }
                    menuItemAnim(pos - 1);
                    changeStatus();
                }
            });
        }
        // 切换菜单状态
        changeStatus();
    }

    // 添加menuItem的点击动画
    private void menuItemAnim(int pos) {
        for (int i = 0; i < getChildCount() - 1; i++) {
            View childView = getChildAt(i + 1);
            if (i == pos) {
                childView.startAnimation(scaleBigAnim(300));
            } else {
                childView.startAnimation(scaleSmallAnim(300));
            }
            childView.setClickable(false);
            childView.setFocusable(false);
        }
        switch (pos) {
            case 0:
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        new WeatherActivity().startSetting();
                    }
                }, 350);
                break;
        }
    }

    private Animation scaleSmallAnim(int duration) {
        AnimationSet animationSet = new AnimationSet(true);
        ScaleAnimation scaleAnim = new ScaleAnimation(1.0f, 0.0f, 1.0f, 0.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        AlphaAnimation alphaAnim = new AlphaAnimation(1f, 0f);

        animationSet.addAnimation(scaleAnim);
        animationSet.addAnimation(alphaAnim);

        animationSet.setDuration(duration);
        animationSet.setFillAfter(true);

        return animationSet;
    }

    // 为当前点击的Item设置变大和透明度降低的动画
    private Animation scaleBigAnim(int duration) {
        AnimationSet animationSet = new AnimationSet(true);
        ScaleAnimation scaleAnim = new ScaleAnimation(1.0f, 4.0f, 1.0f, 4.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        AlphaAnimation alphaAnim = new AlphaAnimation(1f, 0f);

        animationSet.addAnimation(scaleAnim);
        animationSet.addAnimation(alphaAnim);

        animationSet.setDuration(duration);
        animationSet.setFillAfter(true);

        return animationSet;
    }

    private void changeStatus() {
        mCurrentStatus = mCurrentStatus == Status.CLOSE ? Status.OPEN : Status.CLOSE;
    }

    public boolean isOpen() {
        return mCurrentStatus == Status.OPEN;
    }

    // 切换菜单
    private void rotateCButton(View view, float start, float end, int duration) {
        RotateAnimation anim = new RotateAnimation(start, end, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

        anim.setDuration(duration);
        anim.setFillAfter(true);
        view.startAnimation(anim);
    }
}