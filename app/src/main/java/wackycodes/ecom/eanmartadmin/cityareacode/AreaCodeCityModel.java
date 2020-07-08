package wackycodes.ecom.eanmartadmin.cityareacode;

public class AreaCodeCityModel {
    private String areaCode;
    private String areaName;
    private String cityName;
    private String cityCode;

    public AreaCodeCityModel(String areaCode, String areaName, String cityName, String cityCode) {
        this.areaCode = areaCode;
        this.areaName = areaName;
        this.cityName = cityName;
        this.cityCode = cityCode;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }
}
