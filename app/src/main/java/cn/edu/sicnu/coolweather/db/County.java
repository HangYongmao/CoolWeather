package cn.edu.sicnu.coolweather.db;

/**
 * Created by HYM on 2018/5/9 0009.
 */

public class County {

    private int id;
    // 县的名字
    private String countyName;
    // 县所对应的天气id
    private String weatherId;
    // 当前县所属市的id值
    private int cityId;

    public int getId() {
        return id;
    }

    public String getCountyName() {
        return countyName;
    }

    public String getWeatherId() {
        return weatherId;
    }

    public int getCityId() {
        return cityId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public void setWeatherId(String weatherId) {
        this.weatherId = weatherId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }
}
