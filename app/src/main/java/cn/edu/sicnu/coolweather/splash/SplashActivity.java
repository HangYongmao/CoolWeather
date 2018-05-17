package cn.edu.sicnu.coolweather.splash;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.zl.reik.dilatingdotsprogressbar.DilatingDotsProgressBar;

import cn.edu.sicnu.coolweather.MainActivity;
import cn.edu.sicnu.coolweather.R;
import cn.edu.sicnu.coolweather.guide.GuideActivity;

public class SplashActivity extends AppCompatActivity {

    private Handler handler = new Handler();

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // NO Title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        preferences = getSharedPreferences("phone", Context.MODE_PRIVATE);

        // 个性化进度条
        DilatingDotsProgressBar mDilatingDotsProgressBar = (DilatingDotsProgressBar) findViewById(R.id.progress);

        // show progress bar and start animating
        mDilatingDotsProgressBar.showNow();

        // 延时
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ImageView imageView = findViewById(R.id.imageView);
                AnimationDrawable animationDrawable = (AnimationDrawable) imageView.getDrawable();
                animationDrawable.start();

                Intent intent;
                // 判断是不是首次登录，
                if (preferences.getBoolean("firststart", true)) {
                    editor = preferences.edit();
                    // 将登录标志位设置为false，下次登录时不在显示首次登录界面
                    editor.putBoolean("firststart", false);
                    editor.commit();
                    intent = new Intent(SplashActivity.this, GuideActivity.class);
                } else {
                    intent = new Intent(SplashActivity.this, MainActivity.class);
                }
                startActivity(intent);
                finish();
            }
        }, 1000);
    }
}
