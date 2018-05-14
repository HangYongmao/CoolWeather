package cn.edu.sicnu.coolweather.splash;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import cn.edu.sicnu.coolweather.MainActivity;
import cn.edu.sicnu.coolweather.R;

public class SplashActivity extends AppCompatActivity {

    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // NO Title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        // 延时
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ImageView imageView = findViewById(R.id.imageView);
                AnimationDrawable animationDrawable = (AnimationDrawable) imageView.getDrawable();
                animationDrawable.start();

                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
            }
        }, 4000);
    }
}
