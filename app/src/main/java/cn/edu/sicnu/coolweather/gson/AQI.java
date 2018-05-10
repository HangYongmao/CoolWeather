package cn.edu.sicnu.coolweather.gson;

/**
 * Created by HYM on 2018/5/10 0010.
 */

public class AQI {

    public AQICity city;

    public class AQICity {
        public String aqi;
        public String pm25;
    }
}
