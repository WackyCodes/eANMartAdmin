package wackycodes.ecom.eanmartadmin.multisection;

public class AboutShopModel {

    private String shopID;
    private String shopName;
    private String shopAddress;
    private String shopCategory;
    private String shopTagLine;

    private int shopVegNonCode;
    private boolean isOpen;
    private String shopOpenTime;
    private String shopCloseTime;
    private String shopLogo;
    private String shopImage;

    private String shopRatingStars;
    private String shopRatingPeoples;
    private int verifyCode;

    private String[] shopDaysSchedule;

    // Contacts...
    private String shopOwnerName;
    private String shopHelpLine;
    private String shopEmail;
    private String shopWebsite;

    // Social Media Accounts...
    private String shopFacebook;
    private String shopInstagram;
    private String shopTwitter;

    // Licence...
    private boolean isLicenceAvailable;
    private int shopLicenceType;
    private String shopLicenceNumber;


    public AboutShopModel(String shopID) {
        this.shopID = shopID;
    }



    public String getShopID() {
        return shopID;
    }

    public void setShopID(String shopID) {
        this.shopID = shopID;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopAddress() {
        return shopAddress;
    }

    public void setShopAddress(String shopAddress) {
        this.shopAddress = shopAddress;
    }

    public String getShopCategory() {
        return shopCategory;
    }

    public void setShopCategory(String shopCategory) {
        this.shopCategory = shopCategory;
    }

    public String getShopTagLine() {
        return shopTagLine;
    }

    public void setShopTagLine(String shopTagLine) {
        this.shopTagLine = shopTagLine;
    }

    public int getShopVegNonCode() {
        return shopVegNonCode;
    }

    public void setShopVegNonCode(int shopVegNonCode) {
        this.shopVegNonCode = shopVegNonCode;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public String getShopOpenTime() {
        return shopOpenTime;
    }

    public void setShopOpenTime(String shopOpenTime) {
        this.shopOpenTime = shopOpenTime;
    }

    public String getShopCloseTime() {
        return shopCloseTime;
    }

    public void setShopCloseTime(String shopCloseTime) {
        this.shopCloseTime = shopCloseTime;
    }

    public String getShopLogo() {
        return shopLogo;
    }

    public void setShopLogo(String shopLogo) {
        this.shopLogo = shopLogo;
    }

    public String getShopImage() {
        return shopImage;
    }

    public void setShopImage(String shopImage) {
        this.shopImage = shopImage;
    }

    public String getShopRatingStars() {
        return shopRatingStars;
    }

    public void setShopRatingStars(String shopRatingStars) {
        this.shopRatingStars = shopRatingStars;
    }

    public String getShopRatingPeoples() {
        return shopRatingPeoples;
    }

    public void setShopRatingPeoples(String shopRatingPeoples) {
        this.shopRatingPeoples = shopRatingPeoples;
    }

    public int getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(int verifyCode) {
        this.verifyCode = verifyCode;
    }

    public String[] getShopDaysSchedule() {
        return shopDaysSchedule;
    }

    public void setShopDaysSchedule(String[] shopDaysSchedule) {
        this.shopDaysSchedule = shopDaysSchedule;
    }

    public String getShopOwnerName() {
        return shopOwnerName;
    }

    public void setShopOwnerName(String shopOwnerName) {
        this.shopOwnerName = shopOwnerName;
    }

    public String getShopHelpLine() {
        return shopHelpLine;
    }

    public void setShopHelpLine(String shopHelpLine) {
        this.shopHelpLine = shopHelpLine;
    }

    public String getShopEmail() {
        return shopEmail;
    }

    public void setShopEmail(String shopEmail) {
        this.shopEmail = shopEmail;
    }

    public String getShopWebsite() {
        return shopWebsite;
    }

    public void setShopWebsite(String shopWebsite) {
        this.shopWebsite = shopWebsite;
    }

    public String getShopFacebook() {
        return shopFacebook;
    }

    public void setShopFacebook(String shopFacebook) {
        this.shopFacebook = shopFacebook;
    }

    public String getShopInstagram() {
        return shopInstagram;
    }

    public void setShopInstagram(String shopInstagram) {
        this.shopInstagram = shopInstagram;
    }

    public String getShopTwitter() {
        return shopTwitter;
    }

    public void setShopTwitter(String shopTwitter) {
        this.shopTwitter = shopTwitter;
    }

    public boolean isLicenceAvailable() {
        return isLicenceAvailable;
    }

    public void setLicenceAvailable(boolean licenceAvailable) {
        isLicenceAvailable = licenceAvailable;
    }

    public int getShopLicenceType() {
        return shopLicenceType;
    }

    public void setShopLicenceType(int shopLicenceType) {
        this.shopLicenceType = shopLicenceType;
    }

    public String getShopLicenceNumber() {
        return shopLicenceNumber;
    }

    public void setShopLicenceNumber(String shopLicenceNumber) {
        this.shopLicenceNumber = shopLicenceNumber;
    }
}
