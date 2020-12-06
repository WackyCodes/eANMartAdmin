package ean.ecom.eanmartadmin.multisection.aboutshop;


import android.os.Parcel;
import android.os.Parcelable;

public class AboutShopModel implements Parcelable {

    private String shop_id; // shopID
    private String shop_name; //  shopName
    private String shop_category_name; // shopSubTitle
    private String shop_address; //  shopAddress
//    private String shop_cat_main; //  shopCategory

    private double shop_ratings_stars;
    private int shop_ratings_peoples;

    private String shop_logo; //  shopLogo
//    private String shop_image; //  shopImage

    private boolean available_service; //  isServiceAvailable
    private boolean is_open; //  isOpen

    private String shop_veg_non_type; //  shopVegNonCode


    public AboutShopModel() {
    }

    protected AboutShopModel(Parcel in) {
        shop_id = in.readString();
        shop_name = in.readString();
        shop_category_name = in.readString();
        shop_address = in.readString();
        shop_ratings_stars = in.readDouble();
        shop_ratings_peoples = in.readInt();
        shop_logo = in.readString();
        available_service = in.readByte() != 0;
        is_open = in.readByte() != 0;
        shop_veg_non_type = in.readString();
    }

    public static final Creator <AboutShopModel> CREATOR = new Creator <AboutShopModel>() {
        @Override
        public AboutShopModel createFromParcel(Parcel in) {
            return new AboutShopModel( in );
        }

        @Override
        public AboutShopModel[] newArray(int size) {
            return new AboutShopModel[size];
        }
    };

    public String getShop_id() {
        return shop_id;
    }

    public void setShop_id(String shop_id) {
        this.shop_id = shop_id;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public String getShop_category_name() {
        return shop_category_name;
    }

    public void setShop_category_name(String shop_category_name) {
        this.shop_category_name = shop_category_name;
    }

    public String getShop_address() {
        return shop_address;
    }

    public void setShop_address(String shop_address) {
        this.shop_address = shop_address;
    }

    public double getShop_ratings_stars() {
        return shop_ratings_stars;
    }

    public void setShop_ratings_stars(double shop_ratings_stars) {
        this.shop_ratings_stars = shop_ratings_stars;
    }

    public int getShop_ratings_peoples() {
        return shop_ratings_peoples;
    }

    public void setShop_ratings_peoples(int shop_ratings_peoples) {
        this.shop_ratings_peoples = shop_ratings_peoples;
    }

    public String getShop_logo() {
        return shop_logo;
    }

    public void setShop_logo(String shop_logo) {
        this.shop_logo = shop_logo;
    }

    public boolean isAvailable_service() {
        return available_service;
    }

    public void setAvailable_service(boolean available_service) {
        this.available_service = available_service;
    }

    public boolean isIs_open() {
        return is_open;
    }

    public void setIs_open(boolean is_open) {
        this.is_open = is_open;
    }

    public String getShop_veg_non_type() {
        return shop_veg_non_type;
    }

    public void setShop_veg_non_type(String shop_veg_non_type) {
        this.shop_veg_non_type = shop_veg_non_type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString( shop_id );
        dest.writeString( shop_name );
        dest.writeString( shop_category_name );
        dest.writeString( shop_address );
        dest.writeDouble( shop_ratings_stars );
        dest.writeInt( shop_ratings_peoples );
        dest.writeString( shop_logo );
        dest.writeByte( (byte) (available_service ? 1 : 0) );
        dest.writeByte( (byte) (is_open ? 1 : 0) );
        dest.writeString( shop_veg_non_type );
    }
}
