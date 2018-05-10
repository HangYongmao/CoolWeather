package cn.edu.sicnu.coolweather.gson;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Created by HYM on 2018/5/10 0010.
 */

public class Weather {

    public String status;
    public Basic basic;
    public AQI aqi;
    public Now now;
    public Suggestion suggestion;

    @SerializedName("daily_forecast")
    public List<Forecast> forecastList;
}
