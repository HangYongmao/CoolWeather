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

    public class More {
        @SerializedName("txt")
        public String info;
    }
}
