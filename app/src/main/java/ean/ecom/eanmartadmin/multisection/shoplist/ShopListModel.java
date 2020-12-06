package ean.ecom.eanmartadmin.multisection.shoplist;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.GeoPoint;

import java.util.List;

/**
 * Created by Shailendra (WackyCodes) on 06/12/2020 18:11
 * ( To Know more, Click : https://linktr.ee/wackycodes )
 */
public class ShopListModel implements Parcelable {

    private String shop_id; // shopID
    private String shop_name; //  shopName
    private String shop_category_name; // shopSubTitle
    private String shop_address; //  shopAddress
    private String shop_cat_main; //  shopCategory
    private String shop_tag_line; // shopTagLine
    private GeoPoint shop_geo_point; // Location Point

    private String shop_logo; //  shopLogo
    private String shop_image; //  shopImage

    private boolean available_service; //  isServiceAvailable
    private boolean is_open; //  isOpen

    private String shop_verify_code;

    private String shop_area_code; //  shopAreaCode
    private String shop_area_name; //  shopAreaName
    private String shop_city_name; //  shopCity
    private String shop_landmark; //  shopLandMark

    private String shop_veg_non_type; //  shopVegNonCode
    private String shop_open_time; //  shopOpenTime
    private String shop_close_time; //  shopCloseTime
    private String shop_close_msg;

    private double shop_ratings_stars;
    private int shop_ratings_peoples;

    private List <Boolean> shop_days_schedule; // Schedule List...

    private List <String> shop_categories; //  shopCategories

    private String shop_help_line;
    private String shop_email;
    private String shop_website;

    // Licence...
    private boolean is_licence_available;
    private String shop_licence_type;
    private String shop_licence_number;

    // Social Media Accounts...
    private String shop_facebook;
    private String shop_instagram;
    private String shop_twitter;

    // Contacts...
    private String shop_owner_name; //  shopOwnerName
    private String shop_owner_address; // shopOwnerAddress
    private String shop_owner_mobile; // shopOwnerMobile
    private String shop_owner_email;  //  shopOwnerEmail


    public ShopListModel() {
    }

    protected ShopListModel(Parcel in) {
        shop_id = in.readString();
        shop_name = in.readString();
        shop_category_name = in.readString();
        shop_address = in.readString();
        shop_cat_main = in.readString();
        shop_tag_line = in.readString();
        shop_logo = in.readString();
        shop_image = in.readString();
        available_service = in.readByte() != 0;
        is_open = in.readByte() != 0;
        shop_verify_code = in.readString();
        shop_area_code = in.readString();
        shop_area_name = in.readString();
        shop_city_name = in.readString();
        shop_landmark = in.readString();
        shop_veg_non_type = in.readString();
        shop_open_time = in.readString();
        shop_close_time = in.readString();
        shop_close_msg = in.readString();
        shop_ratings_stars = in.readDouble();
        shop_ratings_peoples = in.readInt();
        shop_categories = in.createStringArrayList();
        shop_help_line = in.readString();
        shop_email = in.readString();
        shop_website = in.readString();
        is_licence_available = in.readByte() != 0;
        shop_licence_type = in.readString();
        shop_licence_number = in.readString();
        shop_facebook = in.readString();
        shop_instagram = in.readString();
        shop_twitter = in.readString();
        shop_owner_name = in.readString();
        shop_owner_address = in.readString();
        shop_owner_mobile = in.readString();
        shop_owner_email = in.readString();
    }

    public static final Creator <ShopListModel> CREATOR = new Creator <ShopListModel>() {
        @Override
        public ShopListModel createFromParcel(Parcel in) {
            return new ShopListModel( in );
        }

        @Override
        public ShopListModel[] newArray(int size) {
            return new ShopListModel[size];
        }
    };

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
        dest.writeString( shop_cat_main );
        dest.writeString( shop_tag_line );
        dest.writeString( shop_logo );
        dest.writeString( shop_image );
        dest.writeByte( (byte) (available_service ? 1 : 0) );
        dest.writeByte( (byte) (is_open ? 1 : 0) );
        dest.writeString( shop_verify_code );
        dest.writeString( shop_area_code );
        dest.writeString( shop_area_name );
        dest.writeString( shop_city_name );
        dest.writeString( shop_landmark );
        dest.writeString( shop_veg_non_type );
        dest.writeString( shop_open_time );
        dest.writeString( shop_close_time );
        dest.writeString( shop_close_msg );
        dest.writeDouble( shop_ratings_stars );
        dest.writeInt( shop_ratings_peoples );
        dest.writeStringList( shop_categories );
        dest.writeString( shop_help_line );
        dest.writeString( shop_email );
        dest.writeString( shop_website );
        dest.writeByte( (byte) (is_licence_available ? 1 : 0) );
        dest.writeString( shop_licence_type );
        dest.writeString( shop_licence_number );
        dest.writeString( shop_facebook );
        dest.writeString( shop_instagram );
        dest.writeString( shop_twitter );
        dest.writeString( shop_owner_name );
        dest.writeString( shop_owner_address );
        dest.writeString( shop_owner_mobile );
        dest.writeString( shop_owner_email );
    }

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

    public String getShop_cat_main() {
        return shop_cat_main;
    }

    public void setShop_cat_main(String shop_cat_main) {
        this.shop_cat_main = shop_cat_main;
    }

    public String getShop_tag_line() {
        return shop_tag_line;
    }

    public void setShop_tag_line(String shop_tag_line) {
        this.shop_tag_line = shop_tag_line;
    }

    public GeoPoint getShop_geo_point() {
        return shop_geo_point;
    }

    public void setShop_geo_point(GeoPoint shop_geo_point) {
        this.shop_geo_point = shop_geo_point;
    }

    public String getShop_logo() {
        return shop_logo;
    }

    public void setShop_logo(String shop_logo) {
        this.shop_logo = shop_logo;
    }

    public String getShop_image() {
        return shop_image;
    }

    public void setShop_image(String shop_image) {
        this.shop_image = shop_image;
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

    public String getShop_verify_code() {
        return shop_verify_code;
    }

    public void setShop_verify_code(String shop_verify_code) {
        this.shop_verify_code = shop_verify_code;
    }

    public String getShop_area_code() {
        return shop_area_code;
    }

    public void setShop_area_code(String shop_area_code) {
        this.shop_area_code = shop_area_code;
    }

    public String getShop_area_name() {
        return shop_area_name;
    }

    public void setShop_area_name(String shop_area_name) {
        this.shop_area_name = shop_area_name;
    }

    public String getShop_city_name() {
        return shop_city_name;
    }

    public void setShop_city_name(String shop_city_name) {
        this.shop_city_name = shop_city_name;
    }

    public String getShop_landmark() {
        return shop_landmark;
    }

    public void setShop_landmark(String shop_landmark) {
        this.shop_landmark = shop_landmark;
    }

    public String getShop_veg_non_type() {
        return shop_veg_non_type;
    }

    public void setShop_veg_non_type(String shop_veg_non_type) {
        this.shop_veg_non_type = shop_veg_non_type;
    }

    public String getShop_open_time() {
        return shop_open_time;
    }

    public void setShop_open_time(String shop_open_time) {
        this.shop_open_time = shop_open_time;
    }

    public String getShop_close_time() {
        return shop_close_time;
    }

    public void setShop_close_time(String shop_close_time) {
        this.shop_close_time = shop_close_time;
    }

    public String getShop_close_msg() {
        return shop_close_msg;
    }

    public void setShop_close_msg(String shop_close_msg) {
        this.shop_close_msg = shop_close_msg;
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

    public List <Boolean> getShop_days_schedule() {
        return shop_days_schedule;
    }

    public void setShop_days_schedule(List <Boolean> shop_days_schedule) {
        this.shop_days_schedule = shop_days_schedule;
    }

    public List <String> getShop_categories() {
        return shop_categories;
    }

    public void setShop_categories(List <String> shop_categories) {
        this.shop_categories = shop_categories;
    }

    public String getShop_help_line() {
        return shop_help_line;
    }

    public void setShop_help_line(String shop_help_line) {
        this.shop_help_line = shop_help_line;
    }

    public String getShop_email() {
        return shop_email;
    }

    public void setShop_email(String shop_email) {
        this.shop_email = shop_email;
    }

    public String getShop_website() {
        return shop_website;
    }

    public void setShop_website(String shop_website) {
        this.shop_website = shop_website;
    }

    public boolean isIs_licence_available() {
        return is_licence_available;
    }

    public void setIs_licence_available(boolean is_licence_available) {
        this.is_licence_available = is_licence_available;
    }

    public String getShop_licence_type() {
        return shop_licence_type;
    }

    public void setShop_licence_type(String shop_licence_type) {
        this.shop_licence_type = shop_licence_type;
    }

    public String getShop_licence_number() {
        return shop_licence_number;
    }

    public void setShop_licence_number(String shop_licence_number) {
        this.shop_licence_number = shop_licence_number;
    }

    public String getShop_facebook() {
        return shop_facebook;
    }

    public void setShop_facebook(String shop_facebook) {
        this.shop_facebook = shop_facebook;
    }

    public String getShop_instagram() {
        return shop_instagram;
    }

    public void setShop_instagram(String shop_instagram) {
        this.shop_instagram = shop_instagram;
    }

    public String getShop_twitter() {
        return shop_twitter;
    }

    public void setShop_twitter(String shop_twitter) {
        this.shop_twitter = shop_twitter;
    }

    public String getShop_owner_name() {
        return shop_owner_name;
    }

    public void setShop_owner_name(String shop_owner_name) {
        this.shop_owner_name = shop_owner_name;
    }

    public String getShop_owner_address() {
        return shop_owner_address;
    }

    public void setShop_owner_address(String shop_owner_address) {
        this.shop_owner_address = shop_owner_address;
    }

    public String getShop_owner_mobile() {
        return shop_owner_mobile;
    }

    public void setShop_owner_mobile(String shop_owner_mobile) {
        this.shop_owner_mobile = shop_owner_mobile;
    }

    public String getShop_owner_email() {
        return shop_owner_email;
    }

    public void setShop_owner_email(String shop_owner_email) {
        this.shop_owner_email = shop_owner_email;
    }
}
