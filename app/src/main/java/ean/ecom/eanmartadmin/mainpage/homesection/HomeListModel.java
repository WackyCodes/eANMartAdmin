package ean.ecom.eanmartadmin.mainpage.homesection;

import java.util.List;

public class HomeListModel {
    private int layoutType;
    private String layoutID;
    //------ Layout for Strip and Banner ad....
    private String stripAndBannerAdImg;
    private String stripAndBannerClickID;
    private String stripBannerDeleteID;
    private int stripAndBannerClickType;
    //------- Layout for Category icon and Banner Slider...
    private List<BannerAndCatModel> bannerAndCatModelList;


    // Add New Layout....
    public HomeListModel(int layoutType) {
        this.layoutType = layoutType;
    }
    public HomeListModel(int layoutType, String layoutID, List <BannerAndCatModel> bannerAndCatModelList) {
        this.layoutType = layoutType;
        this.layoutID = layoutID;
        this.bannerAndCatModelList = bannerAndCatModelList;
    }
    //------- Layout for Category icon and Banner Slider...

    public HomeListModel(int layoutType, String layoutID, String stripAndBannerAdImg, String stripAndBannerClickID, String stripBannerDeleteID,  int stripAndBannerClickType) {
        this.layoutType = layoutType;
        this.layoutID = layoutID;
        this.stripAndBannerAdImg = stripAndBannerAdImg;
        this.stripAndBannerClickID = stripAndBannerClickID;
        this.stripBannerDeleteID = stripBannerDeleteID;
        this.stripAndBannerClickType = stripAndBannerClickType;
    }

    public int getLayoutType() {
        return layoutType;
    }

    public void setLayoutType(int layoutType) {
        this.layoutType = layoutType;
    }

    public String getLayoutID() {
        return layoutID;
    }

    public void setLayoutID(String layoutID) {
        this.layoutID = layoutID;
    }
//    private  List<BannerAndCatModel> categoryList;

    public List <BannerAndCatModel> getBannerAndCatModelList() {
        return bannerAndCatModelList;
    }

    public void setBannerAndCatModelList(List <BannerAndCatModel> bannerAndCatModelList) {
        this.bannerAndCatModelList = bannerAndCatModelList;
    }

// ---------------------------------

    public String getStripAndBannerAdImg() {
        return stripAndBannerAdImg;
    }

    public void setStripAndBannerAdImg(String stripAndBannerAdImg) {
        this.stripAndBannerAdImg = stripAndBannerAdImg;
    }

    public String getStripAndBannerClickID() {
        return stripAndBannerClickID;
    }

    public void setStripAndBannerClickID(String stripAndBannerClickID) {
        this.stripAndBannerClickID = stripAndBannerClickID;
    }

    public String getStripBannerDeleteID() {
        return stripBannerDeleteID;
    }

    public void setStripBannerDeleteID(String stripBannerDeleteID) {
        this.stripBannerDeleteID = stripBannerDeleteID;
    }

    public int getStripAndBannerClickType() {
        return stripAndBannerClickType;
    }

    public void setStripAndBannerClickType(int stripAndBannerClickType) {
        this.stripAndBannerClickType = stripAndBannerClickType;
    }
}
