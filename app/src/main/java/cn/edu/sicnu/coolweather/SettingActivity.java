package cn.edu.sicnu.coolweather;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SettingActivity extends AppCompatActivity {

    private TextView tv_cache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 隐藏系统状态栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_setting);

        Button buttonBack = findViewById(R.id.back_button);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        View about = findViewById(R.id.setting_about);
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast toast = Toast.makeText(view.getContext(), "制作人：杭永茂", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        });

        // 设置语言
        View language = findViewById(R.id.setting_language);
        language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
                startActivity(intent);
            }
        });

        // 设置网络
        View network = findViewById(R.id.setting_network);
        network.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Settings.ACTION_AIRPLANE_MODE_SETTINGS);
                startActivity(intent);
            }
        });

        // 设置时间和日期
        View dateTime = findViewById(R.id.setting_date);
        dateTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Settings.ACTION_DATE_SETTINGS);
                startActivity(intent);
            }
        });

        getSystemLanguage();
        getSystemNetworkStatus();
        getSystemDate();

        // 点击logo跳转浏览器
        ImageView setting_logo = findViewById(R.id.image_logo);
        setting_logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse("https://github.com/HangYongmao/CoolWeather");
                intent.setData(content_url);
                startActivity(intent);
            }
        });

        // 计算缓存大小
        tv_cache = findViewById(R.id.cache_size);
        //获得应用内部缓存(/data/data/com.example.androidclearcache/cache)
        File file = new File(this.getCacheDir().getPath());
        try {
            tv_cache.setText(DataCleanManager.getCacheSize(file));
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 清除缓存
        View clearCache = findViewById(R.id.setting_cache);
        clearCache.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "清理中...", Toast.LENGTH_SHORT).show();
                try {
                    DataCleanManager.cleanInternalCache(getApplicationContext());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Toast.makeText(view.getContext(), "清理完成", Toast.LENGTH_SHORT);
                tv_cache.setText("0.0KB");
            }
        });
    }

    public boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        getSystemLanguage();
        getSystemNetworkStatus();
        getSystemDate();
    }

    public void getSystemLanguage() {
        // 获取当前系统语言
        TextView languageText = findViewById(R.id.language);
        Locale locale = Locale.getDefault();
        languageText.setText(locale.getLanguage() + "-" + locale.getCountry() + "＞");
    }

    public void getSystemNetworkStatus() {
        // 网络状态
        TextView network_status = findViewById(R.id.network_status);
        if (isNetworkConnected(network_status.getContext()) == true) {
            network_status.setText("网络可用 ＞");
        } else {
            network_status.setText("网络不可用 ＞");
        }
    }

    public void getSystemDate() {
        // 获取系统时间
        TextView date_now = findViewById(R.id.date_now);
        long time = System.currentTimeMillis();
        Date date = new Date(time);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd EEEE");
        date_now.setText(format.format(date) + " ＞");
    }
}
