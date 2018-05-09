package cn.edu.sicnu.coolweather.db;

import org.litepal.crud.DataSupport;

/**
 * Created by HYM on 2018/5/9 0009.
 */

public class Province extends DataSupport{

    private int id;
    // 省的名字
    private String provinceName;
    // 省的代号
    private int provinceCode;

    public int getId() {
        return id;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public int getProvinceCode() {
        return provinceCode;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public void setProvinceCode(int provinceCode) {
        this.provinceCode = provinceCode;
    }
}
