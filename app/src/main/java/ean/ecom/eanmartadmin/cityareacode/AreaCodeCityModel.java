package ean.ecom.eanmartadmin.cityareacode;

public class AreaCodeCityModel {

    private String cityImage;
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

    // City List...
    private boolean isServiceAvailable;
    private boolean isPublicMessage;
    private int serviceAlertType;
    private int publicMessageType;
    private String serviceAlertText;
    private String serviceAlertImage;
    private String publicMessageText;
    private String publicMessageImage;

    public AreaCodeCityModel(String cityImage, String cityName, String cityCode, boolean isServiceAvailable, boolean isPublicMessage, int serviceAlertType, int publicMessageType, String serviceAlertText, String serviceAlertImage, String publicMessageText, String publicMessageImage) {
        this.cityImage = cityImage;
        this.cityName = cityName;
        this.cityCode = cityCode;
        this.isServiceAvailable = isServiceAvailable;
        this.isPublicMessage = isPublicMessage;
        this.serviceAlertType = serviceAlertType;
        this.publicMessageType = publicMessageType;
        this.serviceAlertText = serviceAlertText;
        this.serviceAlertImage = serviceAlertImage;
        this.publicMessageText = publicMessageText;
        this.publicMessageImage = publicMessageImage;
    }

    public String getCityImage() {
        return cityImage;
    }

    public void setCityImage(String cityImage) {
        this.cityImage = cityImage;
    }

    public boolean isServiceAvailable() {
        return isServiceAvailable;
    }

    public void setServiceAvailable(boolean serviceAvailable) {
        isServiceAvailable = serviceAvailable;
    }

    public boolean isPublicMessage() {
        return isPublicMessage;
    }

    public void setPublicMessage(boolean publicMessage) {
        isPublicMessage = publicMessage;
    }

    public int getServiceAlertType() {
        return serviceAlertType;
    }

    public void setServiceAlertType(int serviceAlertType) {
        this.serviceAlertType = serviceAlertType;
    }

    public int getPublicMessageType() {
        return publicMessageType;
    }

    public void setPublicMessageType(int publicMessageType) {
        this.publicMessageType = publicMessageType;
    }

    public String getServiceAlertText() {
        return serviceAlertText;
    }

    public void setServiceAlertText(String serviceAlertText) {
        this.serviceAlertText = serviceAlertText;
    }

    public String getServiceAlertImage() {
        return serviceAlertImage;
    }

    public void setServiceAlertImage(String serviceAlertImage) {
        this.serviceAlertImage = serviceAlertImage;
    }

    public String getPublicMessageText() {
        return publicMessageText;
    }

    public void setPublicMessageText(String publicMessageText) {
        this.publicMessageText = publicMessageText;
    }

    public String getPublicMessageImage() {
        return publicMessageImage;
    }

    public void setPublicMessageImage(String publicMessageImage) {
        this.publicMessageImage = publicMessageImage;
    }
}
