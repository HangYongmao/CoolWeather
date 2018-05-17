package cn.edu.sicnu.coolweather;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.edu.sicnu.coolweather.gson.Forecast;
import cn.edu.sicnu.coolweather.gson.Weather;
import cn.edu.sicnu.coolweather.service.AutoUpdateService;
import cn.edu.sicnu.coolweather.tools.UITools;
import cn.edu.sicnu.coolweather.util.HttpUtil;
import cn.edu.sicnu.coolweather.util.Utility;
import cn.edu.sicnu.coolweather.view.ArcMenu;
import cn.edu.sicnu.coolweather.view.MyScrollView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherActivity extends AppCompatActivity implements MyScrollView.OnScrollListener {

    private static final String TAG = "WeatherActivity";

    public DrawerLayout drawerLayout;
    private Button navButton;

    private MyScrollView weatherLayout;
    private TextView titleCity;
    private TextView titleUpdateTime;
    private TextView degreeText;
    private TextView weatherInfoText;
    private TextView now_hum;
    private TextView now_pcpn;
    private TextView now_vis;
    private TextView now_dir;
    private TextView now_fl;
    private TextView now_spd;
    private TextView now_pres;
    private TextView now_sc;

    private TextView now_week;
    private LinearLayout forecastLayout;
    private TextView aqiText;
    private TextView pm25Text;
    private TextView qltyText;
    private TextView comfortText;
    private TextView carWashText;
    private TextView sportText;

    private ImageView bingPicImg;

    public SwipeRefreshLayout swipeRefresh;
    private String mWeatherId;

    Weather weather;

    static String titleName = "";
    static String titleTime = "";
    static String titleTmp = "";

    private static Context instance;

    private ArcMenu mArcMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 隐藏系统状态栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_weather);
        instance = this;

        // 初始化各控件
        bingPicImg = (ImageView) findViewById(R.id.bing_pic_img);
        weatherLayout = (MyScrollView) findViewById(R.id.weather_layout);
        titleCity = (TextView) findViewById(R.id.title_city);
        titleUpdateTime = (TextView) findViewById(R.id.title_update_time);
        degreeText = (TextView) findViewById(R.id.degree_text);
        weatherInfoText = (TextView) findViewById(R.id.weather_info_text);
        now_hum = (TextView) findViewById(R.id.now_hum);
        now_dir = (TextView) findViewById(R.id.now_dir);
        now_sc = (TextView) findViewById(R.id.now_sc);
        now_vis = (TextView) findViewById(R.id.now_vis);
        now_fl = (TextView) findViewById(R.id.now_fl);
        now_spd = (TextView) findViewById(R.id.now_spd);
        now_pcpn = (TextView) findViewById(R.id.now_pcpn);
        now_pres = (TextView) findViewById(R.id.now_pres);
        forecastLayout = (LinearLayout) findViewById(R.id.forecast_layout);
        aqiText = (TextView) findViewById(R.id.aqi_text);
        pm25Text = (TextView) findViewById(R.id.pm25_text);
        qltyText = (TextView) findViewById(R.id.qlty_text);
        comfortText = (TextView) findViewById(R.id.comfort_text);
        now_week = (TextView) findViewById(R.id.now_week);
        carWashText = (TextView) findViewById(R.id.car_wash_text);
        sportText = (TextView) findViewById(R.id.sport_text);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navButton = (Button) findViewById(R.id.nav_button);
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        mArcMenu = findViewById(R.id.id_arc_menu);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);

        UITools.elasticPadding((HorizontalScrollView) findViewById(R.id.horizontalScrollView), 500);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString = prefs.getString("weather", null);
        if (weatherString != null) {
            // 有缓存时直接解析天气数据
            Weather weather = Utility.handleWeatherResponse(weatherString);
            mWeatherId = weather.basic.weatherId;
            showWeatherInfo(weather);
        } else {
            // 无缓存时去服务器查询天气
            mWeatherId = getIntent().getStringExtra("weather_id");
            weatherLayout.setVisibility(View.INVISIBLE);
            requestWeather(mWeatherId);
        }

        // 下拉刷新的监听器
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestWeather(mWeatherId);
            }
        });

        String bingPic = prefs.getString("bing_pic", null);
        if (bingPic != null) {
            Glide.with(this).load(bingPic).into(bingPicImg);
        } else {
            loadBingPic();
        }

        // 侧滑菜单-->切换城市
        navButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        // 悬浮菜单
//        ImageView fab_button = findViewById(R.id.fab_button);
//        fab_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(view.getContext(), SettingActivity.class);
//                intent.putExtra("weather_temperature", titleTmp + "℃");
//                intent.putExtra("weather_time", titleTime);
//                intent.putExtra("weather_city", titleName);
//                Log.d(TAG, "onClick: " + titleTmp + "℃");
//                Log.d(TAG, "onClick: " + titleTime);
//                Log.d(TAG, "onClick: " + titleName);
//                startActivity(intent);
//            }
//        });


        // 点击空白处 收起卫星式菜单
        LinearLayout linearLayout = findViewById(R.id.linearLayout);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mArcMenu.isOpen()) {
                    mArcMenu.toggleMenu(600);
                }
            }
        });

        weatherLayout.setOnScrollListener(this);
    }

    // ScrollView滚动事件
    @Override
    public void onScroll(int scrollY) {
        if (mArcMenu.isOpen()) {
            mArcMenu.toggleMenu(600);
        }
        Log.d(TAG, "onScroll: ");
    }


    // 启动设置界面
    public static void startSetting() {
        Intent intent = new Intent(instance, SettingActivity.class);
        intent.putExtra("weather_temperature", titleTmp + "℃");
        intent.putExtra("weather_time", titleTime);
        intent.putExtra("weather_city", titleName);
        Log.d(TAG, "onClick: " + titleTmp + "℃");
        Log.d(TAG, "onClick: " + titleTime);
        Log.d(TAG, "onClick: " + titleName);
        instance.startActivity(intent);
    }


    // 根据天气id请求城市天气信息
    public void requestWeather(final String weatherId) {
        String weatherUrl = "http://guolin.tech/api/weather?cityid=" + weatherId + "&key=2b940224cf05488ababbee882214e315";
        Log.d(TAG, "requestWeather: " + weatherUrl);
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                final Weather weather = Utility.handleWeatherResponse(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (weather != null && "ok".equals(weather.status)) {
                            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                            editor.putString("weather", responseText);
                            editor.apply();
                            mWeatherId = weather.basic.weatherId;
                            showWeatherInfo(weather);
                        } else {
                            Toast.makeText(WeatherActivity.this, "获取天气信息失败", Toast.LENGTH_SHORT).show();
                        }
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this, "获取天气信息失败", Toast.LENGTH_SHORT).show();
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        });
//        loadBingPic();
        loadRandomPic();
    }

    // 加载必应每日一图
    private void loadBingPic() {
        String requestBingPic = "http://guolin.tech/api/bing_pic";
        HttpUtil.sendOkHttpRequest(requestBingPic, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bingPic = response.body().string();
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                editor.putString("bing_pic", bingPic);
                editor.apply();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(WeatherActivity.this).load(bingPic).into(bingPicImg);
                    }
                });
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
        });
    }

    // 随机加载图片
    String randomPic = "";
    private void loadRandomPic() {
        // 获取网络重定向文件的真实URL
        new Thread(new Runnable() {
            @Override
            public void run() {
                String str = "https://source.unsplash.com/random";
                URL url = null;
                try {
                    url = new URL(str);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.getResponseCode();
                    final String realUrl = conn.getURL().toString();
                    conn.disconnect();
                    Log.e("asd", "真实URL:" + realUrl);
                    randomPic = realUrl;
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
        editor.putString("bing_pic", randomPic);
        editor.apply();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Glide.with(WeatherActivity.this).load(randomPic).into(bingPicImg);
            }
        });

    }


    // 处理并展示Weather实体类中的数据
    private void showWeatherInfo(Weather weather) {
        String cityName = weather.basic.cityName;
        String adminAreaName = weather.basic.adminAreaName;
        String parentCityName = weather.basic.parentCityName;
        String updateTime = weather.basic.update.updateTime.split(" ")[1];
        titleTime = weather.basic.update.updateTime.split(" ")[1];
        String degree = weather.now.temperature + "℃";
        titleTmp = weather.now.temperature;

        String weatherInfo = weather.now.more.info;
        String st_now_dir = weather.now.now_dir;
        String st_now_hum = weather.now.now_hum;
        String st_now_sc = weather.now.now_sc;
        String st_now_vis = weather.now.now_vis;
        String st_now_fl = weather.now.now_fl;
        String st_now_spd = weather.now.now_spd;
        String st_now_pcpn = weather.now.now_pcpn;
        String st_now_pres = weather.now.now_pres;
        if (adminAreaName.equals(parentCityName))
            titleName = adminAreaName;
        else
            titleName = adminAreaName + " " + parentCityName;
        if (!cityName.equals(parentCityName))
            titleName = titleName + " " + cityName;
        titleCity.setText(titleName);
        titleUpdateTime.setText(updateTime);
        degreeText.setText(degree);
        weatherInfoText.setText(weatherInfo);
        now_dir.setText(st_now_dir);
        now_vis.setText(st_now_vis + "km");
        now_sc.setText(st_now_sc);
        now_hum.setText(st_now_hum + "%");
        now_fl.setText(st_now_fl + "℃");
        now_spd.setText(st_now_spd + "Kmph");
        now_pcpn.setText(st_now_pcpn + "mm");
        now_pres.setText(st_now_pres + "Pa");

        long time = System.currentTimeMillis();
        Date date = new Date(time);
        SimpleDateFormat format = new SimpleDateFormat("EEEE");
        now_week.setText(format.format(date) + " 今天");
        forecastLayout.removeAllViews();
        for (Forecast forecast : weather.forecastList) {
            View view = LayoutInflater.from(this).inflate(R.layout.forecast_item, forecastLayout, false);
            TextView dateText = (TextView) view.findViewById(R.id.date_text);
            TextView infoText = (TextView) view.findViewById(R.id.info_text);
            TextView maxText = (TextView) view.findViewById(R.id.max_text);
            TextView minText = (TextView) view.findViewById(R.id.min_text);
            dateText.setText(forecast.date);
            infoText.setText(forecast.more.info);
            maxText.setText(forecast.temperature.max);
            minText.setText(forecast.temperature.min);
            forecastLayout.addView(view);
        }
        if (weather.aqi != null) {
            aqiText.setText(weather.aqi.city.aqi);
            pm25Text.setText(weather.aqi.city.pm25);
            qltyText.setText(weather.aqi.city.qlty);
        }
        String comfort = "舒适度：" + weather.suggestion.comfort.info;
        String carWash = "洗车指数：" + weather.suggestion.carWash.info;
        String sport = "运动建议：" + weather.suggestion.sport.info;
        comfortText.setText(comfort);
        carWashText.setText(carWash);
        sportText.setText(sport);
        weatherLayout.setVisibility(View.VISIBLE);

        Intent intent = new Intent(this, AutoUpdateService.class);
        startService(intent);
    }

    // 声明一个long类型变量：用于存放上一点击“返回键”的时刻
    private long mExitTime;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 判断用户是否点击了“返回键”
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // 与上次点击返回键时刻作差
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                // 大于2000ms则认为是误操作，使用Toast进行提示
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                // 并记录下本次点击“返回键”的时刻，以便下次进行判断
                mExitTime = System.currentTimeMillis();
            } else {
                // 小于2000ms则认为是用户确实希望退出程序
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    // 点击空白处
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (mArcMenu.isOpen()) {
//                    mArcMenu.toggleMenu(600);
                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }
}
