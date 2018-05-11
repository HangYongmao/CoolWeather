package cn.edu.sicnu.coolweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by HYM on 2018/5/10 0010.
 */

public class Now {

    @SerializedName("tmp")
    public String temperature;

    @SerializedName("cond")
    public More more;

    // 湿度
    @SerializedName("hum")
    public String now_hum;

    // 降雨量mm
    @SerializedName("pcpn")
    public String now_pcpn;

    // 能见度
    @SerializedName("vis")
    public String now_vis;

    // 风向
    @SerializedName("wind_dir")
    public String now_dir;

    // 风力等级
    @SerializedName("wind_sc")
    public String now_sc;

    public class More {
        @SerializedName("txt")
        public String info;
    }
}
