package cn.edu.sicnu.coolweather.db;

import org.litepal.crud.DataSupport;

/**
 * Created by HYM on 2018/5/9 0009.
 */

public class City extends DataSupport {

    private int id;
    // 市的名字
    private String cityName;
    // 市的代号
    private int cityCode;
    // 当前市所属省的id值
    private int provinceId;

    public void setId(int id) {
        this.id = id;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public void setCityCode(int cityCode) {
        this.cityCode = cityCode;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }

    public int getId() {
        return id;
    }

    public String getCityName() {
        return cityName;
    }

    public int getCityCode() {
        return cityCode;
    }

    public int getProvinceId() {
        return provinceId;
    }
}
