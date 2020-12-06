package ean.ecom.eanmartadmin.multisection.shoplist;

import com.google.firebase.firestore.GeoPoint;

import java.util.List;

/**
 * Created by Shailendra (WackyCodes) on 06/12/2020 14:20
 * ( To Know more, Click : https://linktr.ee/wackycodes )
 */
class ShopModel {


    private String shop_id; // shopID
    private String shop_name; //  shopName
    private String shop_address; //  shopAddress
    private String shop_cat_main; //  shopCategory
    private String shop_category_name; //
    private String shop_tag_line; // shopTagLine
    private GeoPoint shop_geo_point;

    private String shop_veg_non_type; //  shopVegNonCode
    private boolean available_service; //  isServiceAvailable
    private boolean is_open; //  isOpen
    private String shop_open_time; //  shopOpenTime
    private String shop_close_time; //  shopCloseTime
    private String shop_close_msg;
    private String shop_logo; //  shopLogo
    private String shop_image; //  shopImage

    private String shop_ratings_stars;
    private String shop_ratings_peoples;

    private String shop_verify_code;

    private String shop_area_code; //  shopAreaCode
    private String shop_area_name; //  shopAreaName
    private String shop_city_name; //  shopCity
    private String shop_landmark; //  shopLandMark

    private String[] shopDaysSchedule; //

    private List<String> shop_categories; //  shopCategories

    // Contacts...
    private String shop_owner_name; //  shopOwnerName
    private String shop_owner_address; // shopOwnerAddress
    private String shop_owner_mobile; // shopOwnerMobile
    private String shop_owner_email;  //  shopOwnerEmail

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

    /**
     * Main : SHOPS>ShopId>
     *
     * 1. available_service : true
     * 2. is_open : true
     * 3. shop_address : "Shop - full - address "
     >  * 4. shop_area_code : "462021" (String)
     * 5. shop_area_name : "Ratnagiri"
     * 6. shop_cat_main : "ELECTRONICS"
     * 7. shop_categories : array ( "ELECTRONICS", "MOBILES")
     >  * 8. shop_category_name : "Name of Main Category"
     * 9. shop_city_name : "Bhopal"
     * 10. shop_close_msg : "Whatever message shop admin want to show;"
     >  * 11. shop_id : "462099009"
     * 12. shop_landmark : " Anything"
     * 13. shop_logo : "Logo Link"
     * 14. shop_map_latitude : NO
     * 15. shop_map_longitude : NO
     >  * 16. shop_name : " AN Electronics :"
     * 17. shop_owner_address : "jadsb :"
     * 18. shop_owner_name : "Name:"
     * 19. shop_owner_mobile : '20329092390"
     * 20. shop_owner_email : "alksnd!"
     * 21. shop_type : "1" (Not required)
     >  * 22. shop_veg_non_type : "2"
     *
     * 23. shop_image : " Shop image Link "
     * 24. shop_rating : "4.3"
     * 25. tags : Array (l, aos,_)
     *
     *
     *  --- is_open : true
     *  26. shop_open_time : "10:00AM"
     *  27. shop_close_time : "5:00PM"
     *
     *
     * ---------------------------
     * BHOPAL > SHOPS > shop_Id:
     *
     * 1. shop_id : "2939320"
     * 2. shop_image : "Link:"
     * 2. shop_name : 'name"
     * 4. shop_logo : "link :"
     * 5. shop_rating : "4.2"
     * 6. shop_veg_non_type :"2"
     * 7. tags : Array (l, aos,_)
     *
     * 8. shop_categories : array (ls. Lsa0)
     * 9. shop_category_name : "kugs "
     *
     *
     */


}
