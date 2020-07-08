package wackycodes.ecom.eanmartadmin.category;

public class ShopListModel {

    private String shopID;
    private String shopName;
    private String shopLogo;


    public ShopListModel(String shopID, String shopName) {
        this.shopID = shopID;
        this.shopName = shopName;
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

    public String getShopLogo() {
        return shopLogo;
    }

    public void setShopLogo(String shopLogo) {
        this.shopLogo = shopLogo;
    }
}
