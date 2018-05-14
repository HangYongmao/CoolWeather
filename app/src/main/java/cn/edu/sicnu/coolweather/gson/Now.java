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

    // 体感温度
    @SerializedName("fl")
    public String now_fl;

    // 风速
    @SerializedName("wind_spd")
    public String now_spd;

    // 风力等级
    @SerializedName("wind_sc")
    public String now_sc;

    // 风向
    @SerializedName("wind_dir")
    public String now_dir;

    // 降雨量
    @SerializedName("pcpn")
    public String now_pcpn;

    // 湿度
    @SerializedName("hum")
    public String now_hum;

    // 气压
    @SerializedName("pres")
    public String now_pres;

    // 能见度
    @SerializedName("vis")
    public String now_vis;

    public class More {
        @SerializedName("txt")
        public String info;
    }
}
