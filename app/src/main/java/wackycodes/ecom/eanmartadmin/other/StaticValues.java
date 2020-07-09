package wackycodes.ecom.eanmartadmin.other;

import wackycodes.ecom.eanmartadmin.admin.AdminDataModel;

public class StaticValues {


    public static String APP_VERSION = "em-1-01"; // ev-1-01

    public static final String DEFAULT_CITY_NAME = "BHOPAL";
    public static String CURRENT_CITY_NAME ;
    public static String CURRENT_CITY_CODE ;


    public static final int STORAGE_PERMISSION = 1;

    public static final AdminDataModel ADMIN_DATA_MODEL = new AdminDataModel();

    public static final int ADMIN_ROOT_FOUNDER = 11;
    public static final int ADMIN_ROOT_MANAGER = 12;
    public static final int ADMIN_SHOP_FOUNDER = 13;
    public static final int ADMIN_SHOP_MANAGER = 14;
    public static final int ADMIN_DELIVERY_BOY = 15;


    // Other Values....
    public static final int ID_UPDATE = 51;
    public static final int ID_DELETE = 52;
    public static final int ID_CLICK = 53;
    public static final int ID_MOVE = 54;
    public static final int ID_COPY = 55;

    // File Code
    public static final int GALLERY_CODE = 121;
    public static final int READ_EXTERNAL_MEMORY_CODE = 122;
    public static final int UPDATE_CODE = 123;
    public static final int ADD_CODE = 124;
    public static final int UPLOAD_CODE = 125;
    public static final int UPDATE_EMAIL = 1;
    public static final int UPDATE_PASS = 2;

    // Common Main Home Container...
    public static final int BANNER_SLIDER_LAYOUT_CONTAINER = 0;
    public static final int STRIP_AD_LAYOUT_CONTAINER = 1;
    public static final int CATEGORY_ITEMS_LAYOUT_CONTAINER = 3;
    public static final int SHOP_ITEMS_LAYOUT_CONTAINER = 4;

    public static final int GRID_PRODUCTS_LAYOUT_CONTAINNER = 5;
    public static final int HORIZONTAL_PRODUCTS_LAYOUT_CONTAINER = 5;

    public static final int BANNER_SLIDER_CONTAINER_ITEM = 20; // not stored in database...

    public static final int ADD_NEW_PRODUCT_ITEM = 7;
    public static final int ADD_NEW_STRIP_AD_LAYOUT = 8;
    public static final int ADD_NEW_HORIZONTAL_ITEM_LAYOUT = 9;
    public static final int ADD_NEW_GRID_ITEM_LAYOUT = 10;

    // Banner Click Type...
    public static final int BANNER_CLICK_TYPE_WEBSITE = 1;
    public static final int BANNER_CLICK_TYPE_SHOP = 2;


    // Shop Veg
    public static final int SHOP_TYPE_VEG = 1;
    public static final int SHOP_TYPE_NON_VEG = 2;
    public static final int SHOP_TYPE_VEG_NON = 3;
    public static final int SHOP_TYPE_NO_SHOW = 4;

    // Main Page To another...
    public static final int REQUEST_TO_ADD_SHOP = 1;
    public static final int REQUEST_TO_EDIT_SHOP = 2;
    public static final int REQUEST_TO_VIEW_SHOP = 3;
    public static final int REQUEST_TO_VIEW_HOME = 4;


}
