package ean.ecom.eanmartadmin.admin;

public class AdminDataModel {

    private int adminCode;

    private String adminEmail;
    private String adminMobile;
    private String adminPhoto;


    public AdminDataModel() {
    }

    public AdminDataModel(int adminCode, String adminEmail, String adminMobile, String adminPhoto) {
        this.adminCode = adminCode;
        this.adminEmail = adminEmail;
        this.adminMobile = adminMobile;
        this.adminPhoto = adminPhoto;
    }

    public int getAdminCode() {
        return adminCode;
    }

    public void setAdminCode(int adminCode) {
        this.adminCode = adminCode;
    }

    public String getAdminEmail() {
        return adminEmail;
    }

    public void setAdminEmail(String adminEmail) {
        this.adminEmail = adminEmail;
    }

    public String getAdminMobile() {
        return adminMobile;
    }

    public void setAdminMobile(String adminMobile) {
        this.adminMobile = adminMobile;
    }

    public String getAdminPhoto() {
        return adminPhoto;
    }

    public void setAdminPhoto(String adminPhoto) {
        this.adminPhoto = adminPhoto;
    }
}
