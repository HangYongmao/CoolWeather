package cn.edu.sicnu.coolweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by HYM on 2018/5/10 0010.
 */

public class Basic {

    @SerializedName("city")
    public String cityName;

    @SerializedName("admin_area")
    public String adminAreaName;

    @SerializedName("parent_city")
    public String parentCityName;

    @SerializedName("id")
    public String weatherId;

    public Update update;

    public class Update {
        @SerializedName("loc")
        public String updateTime;
    }
}
