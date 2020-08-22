package ean.ecom.eanmartadmin.database;

import android.app.Dialog;
import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ean.ecom.eanmartadmin.MainActivity;
import ean.ecom.eanmartadmin.cityareacode.servicecity.CityViewFragment;
import ean.ecom.eanmartadmin.cityareacode.servicecity.ServiceAreaFragment;
import ean.ecom.eanmartadmin.mainpage.homesection.categorysection.ShopListModel;
import ean.ecom.eanmartadmin.cityareacode.AreaCodeCityModel;
import ean.ecom.eanmartadmin.mainpage.homesection.categorysection.ShopsViewActivity;
import ean.ecom.eanmartadmin.mainpage.homesection.BannerAndCatModel;
import ean.ecom.eanmartadmin.mainpage.homesection.HomeListModel;
import ean.ecom.eanmartadmin.mainpage.homesection.SecondActivity;
import ean.ecom.eanmartadmin.multisection.aboutshop.ShopHomeActivity;

import static ean.ecom.eanmartadmin.other.StaticValues.BANNER_SLIDER_LAYOUT_CONTAINER;
import static ean.ecom.eanmartadmin.other.StaticValues.CURRENT_CITY_CODE;
import static ean.ecom.eanmartadmin.other.StaticValues.CATEGORY_ITEMS_LAYOUT_CONTAINER;
import static ean.ecom.eanmartadmin.other.StaticValues.SHOP_ITEMS_LAYOUT_CONTAINER;
import static ean.ecom.eanmartadmin.other.StaticValues.STRIP_AD_LAYOUT_CONTAINER;

public class DBQuery {

    public static FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    // get Current User Reference ...
    public static FirebaseUser currentUser = firebaseAuth.getCurrentUser();

    public static FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    public  static StorageReference storageReference = FirebaseStorage.getInstance().getReference();

    public static final ArrayList<AreaCodeCityModel> areaCodeCityModelList = new ArrayList <>();
    public static final ArrayList<AreaCodeCityModel> cityCodeAndNameList = new ArrayList <>();
    public static final ArrayList<ShopListModel> shopListModelArrayList = new ArrayList <>();

    public static List<HomeListModel> homePageList = new ArrayList <>();

    public static List <String> categoryIDList = new ArrayList <>();
    public static List<List <HomeListModel>> categoryList = new ArrayList <>();

    public static CollectionReference getCollectionRef(String collectionName){
        return firebaseFirestore.collection( "HOME_PAGE" ).document( CURRENT_CITY_CODE ).collection( collectionName );
    }

    public static void getMainListDataQuery(Context context, @Nullable final Dialog dialog, @Nullable final RecyclerView recyclerView,
                                            @Nullable final SwipeRefreshLayout refreshLayout,
                                            final String categoryID, final boolean isHomePage, final int index){
        if (isHomePage){
            categoryIDList.clear();
            categoryList.clear();
            homePageList.clear();
        }else{
            categoryList.get( index ).clear();
        }

        getCollectionRef( categoryID ).orderBy( "index" )
                .get().addOnCompleteListener( new OnCompleteListener <QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task <QuerySnapshot> task) {
                if (task.isSuccessful()){
                    // Load Data...
                    for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()){
                        int viewType = Integer.parseInt( String.valueOf( (long)documentSnapshot.get( "type" ) ) );

                        if (viewType == CATEGORY_ITEMS_LAYOUT_CONTAINER){
                            // This is only in Home page case...
//                                Boolean is_visible = documentSnapshot.getBoolean( "is_visible" );
                            String layout_id = documentSnapshot.getId();
                            List<BannerAndCatModel> bannerAndCatModelList = new ArrayList <>();
                            long no_of_cat = (long)documentSnapshot.get( "no_of_cat" );
                            for(long ln = 1; ln <= no_of_cat; ln++){
                                String catID = documentSnapshot.get("cat_id_"+ln).toString();
                                String catName = documentSnapshot.get( "cat_name_"+ln ).toString();
                                String catImage = documentSnapshot.get( "cat_image_"+ln ).toString();
                                String visibleText = "0";
                                if (documentSnapshot.get( "cat_visibility_"+ ln )!=null){
                                    if ((boolean)documentSnapshot.get( "cat_visibility_"+ ln )){
                                        visibleText = "1";
                                    }
                                }

                                BannerAndCatModel bannerAndCatModel = new BannerAndCatModel(
                                        catID, catImage, catID, 0
                                        , catName, visibleText  );
                                categoryIDList.add( catID );
                                categoryList.add( new ArrayList <HomeListModel>() );
                                bannerAndCatModelList.add( bannerAndCatModel );
                            }
                            // Add Data in homeList...
                            homePageList.add( new HomeListModel( CATEGORY_ITEMS_LAYOUT_CONTAINER, layout_id, bannerAndCatModelList ) );
                            if (SecondActivity.homePageAdaptor != null)
                                SecondActivity.homePageAdaptor.notifyDataSetChanged();

                        }else
                        if (viewType == BANNER_SLIDER_LAYOUT_CONTAINER){
                            List<BannerAndCatModel> bannerAndCatModelList = new ArrayList <>();
                            Boolean is_visible = documentSnapshot.getBoolean( "is_visible" );
                            String layout_id = documentSnapshot.getId();
                            long no_of_banners = (long)documentSnapshot.get( "no_of_banners" );
                            for(long ln = 1; ln <= no_of_banners; ln++){
                                String deletedId = documentSnapshot.get("delete_id_"+ln).toString();
                                String banner_link = documentSnapshot.get( "banner_"+ln ).toString();
                                String bannerClickID = documentSnapshot.get( "banner_click_id_"+ln ).toString();
                                Long bannerClickType = documentSnapshot.getLong( "banner_click_type_"+ln );
                                BannerAndCatModel bannerAndCatModel = new BannerAndCatModel(
                                        deletedId, banner_link, bannerClickID, Integer.parseInt( String.valueOf( bannerClickType ) )
                                        , deletedId, deletedId  ); // Use Layout ID...
                                bannerAndCatModelList.add( bannerAndCatModel );
                            }
                            // Add Data in homeList...
                            if (isHomePage){
                                homePageList.add( new HomeListModel( BANNER_SLIDER_LAYOUT_CONTAINER, layout_id, bannerAndCatModelList ) );
                                if (SecondActivity.homePageAdaptor != null)
                                    SecondActivity.homePageAdaptor.notifyDataSetChanged();
                            }
                            else{
                                categoryList.get( index ).add( new HomeListModel( BANNER_SLIDER_LAYOUT_CONTAINER, layout_id, bannerAndCatModelList ) );
                                if (ShopsViewActivity.shopsViewAdaptor != null ){
                                    ShopsViewActivity.shopsViewAdaptor.notifyDataSetChanged();
                                }

//                                if (recyclerView != null && ShopsViewActivity.shopsViewAdaptor == null ){
//                                    ShopsViewActivity.shopsViewAdaptor = new ShopsViewAdaptor( categoryList.get( index ), index, "Name", categoryID );
//                                    recyclerView.setAdapter( ShopsViewActivity.shopsViewAdaptor );
//                                    ShopsViewActivity.shopsViewAdaptor.notifyDataSetChanged();
//                                }else if (ShopsViewActivity.shopsViewAdaptor != null ){
//                                    ShopsViewActivity.shopsViewAdaptor.notifyDataSetChanged();
//                                }
                            }

                        }else
                        if (viewType == STRIP_AD_LAYOUT_CONTAINER){
//                                Boolean is_visible = documentSnapshot.getBoolean( "is_visible" );
                            String layout_id = documentSnapshot.getId();
                            String deletedId = documentSnapshot.get("delete_id").toString();
                            String banner_image = documentSnapshot.get("banner_image").toString();
                            String banner_click_id = documentSnapshot.get("banner_click_id").toString();
                            long banner_click_type = (long)documentSnapshot.get("banner_click_type");
                            // Add Data in homeList...
                            if (isHomePage){
                                homePageList.add( new HomeListModel( STRIP_AD_LAYOUT_CONTAINER, layout_id, banner_image, banner_click_id, deletedId
                                        , Integer.parseInt( String.valueOf( banner_click_type ))) );
                                if (SecondActivity.homePageAdaptor != null)
                                    SecondActivity.homePageAdaptor.notifyDataSetChanged();
                            }
                            else{
                                categoryList.get( index ).add( new HomeListModel( STRIP_AD_LAYOUT_CONTAINER, layout_id, banner_image, banner_click_id, deletedId
                                        , Integer.parseInt( String.valueOf( banner_click_type ))) );
                                if (ShopsViewActivity.shopsViewAdaptor != null ){
                                    ShopsViewActivity.shopsViewAdaptor.notifyDataSetChanged();
                                }

//                                if (recyclerView != null && ShopsViewActivity.shopsViewAdaptor == null ){
//                                    ShopsViewActivity.shopsViewAdaptor = new ShopsViewAdaptor( categoryList.get( index ), index, "Name", categoryID );
//                                    recyclerView.setAdapter( ShopsViewActivity.shopsViewAdaptor );
//                                    ShopsViewActivity.shopsViewAdaptor.notifyDataSetChanged();
//                                }else if (ShopsViewActivity.shopsViewAdaptor != null ){
//                                    ShopsViewActivity.shopsViewAdaptor.notifyDataSetChanged();
//                                }
                            }

                        }else
                        if (viewType == SHOP_ITEMS_LAYOUT_CONTAINER){
                            // This is only in shop View Case... but we have to check, cause.. to ad purpose we can use this in future...
                            String layout_id = documentSnapshot.getId();
                            int layIndex = Integer.parseInt( String.valueOf( (long)documentSnapshot.get( "index" ) ) );
                            getShopsItemQuery( recyclerView, categoryID, layout_id, layIndex, index);
                        }
                        // notifyDataChanged...
                        if (isHomePage){
                            if (SecondActivity.homePageAdaptor != null)
                                SecondActivity.homePageAdaptor.notifyDataSetChanged();
                        }else{
                            if (ShopsViewActivity.shopsViewAdaptor != null)
                                ShopsViewActivity.shopsViewAdaptor.notifyDataSetChanged();
                        }
                        // Dismiss...
                        if (dialog!=null){
                            dialog.dismiss();
                        }
                        if (refreshLayout!=null){
                            refreshLayout.setRefreshing( false );
                        }
                    }
                    // Dismiss...
                    if (dialog!=null){
                        dialog.dismiss();
                    }
                    if (refreshLayout!=null){
                        refreshLayout.setRefreshing( false );
                    }
                }else{
                    // Dismiss...
                    if (dialog!=null){
                        dialog.dismiss();
                    }
                    if (refreshLayout!=null){
                        refreshLayout.setRefreshing( false );
                    }

                }

            }
        } );

    }

    public static void getShopsItemQuery( @Nullable final RecyclerView recyclerView, final String categoryID, final String layout_id, final int layIndex, final int index){
        // One More Query Needed here...
        getCollectionRef("SHOPS").whereArrayContains( "shop_categories", categoryID )
                .get().addOnCompleteListener( new OnCompleteListener <QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task <QuerySnapshot> task1) {
                if (task1.isSuccessful()) {
                    // data is loaded...
                    List<BannerAndCatModel> bannerAndCatModelList = new ArrayList <>();
                    for (QueryDocumentSnapshot documentSnapshot1 : task1.getResult()) {
                        String shop_id =  documentSnapshot1.get( "shop_id" ).toString();
                        String shop_name =  documentSnapshot1.get( "shop_name" ).toString();
                        String shop_logo =     documentSnapshot1.get( "shop_logo" ).toString();

                        BannerAndCatModel bannerAndCatModel = new BannerAndCatModel(
                                shop_id, shop_logo, shop_id, 0
                                , shop_name, ""  );
                        bannerAndCatModelList.add( bannerAndCatModel );
                    }
                    // Add Data in homeList...SHOP_ITEMS_LAYOUT_CONTAINER
                    categoryList.get( index ).add( layIndex,  new HomeListModel( SHOP_ITEMS_LAYOUT_CONTAINER, layout_id, bannerAndCatModelList ) );
                    if (ShopsViewActivity.shopsViewAdaptor != null ){
                        ShopsViewActivity.shopsViewAdaptor.notifyDataSetChanged();
                    }
//                    if (recyclerView != null && ShopsViewActivity.shopsViewAdaptor == null ){
//                        ShopsViewActivity.shopsViewAdaptor = new ShopsViewAdaptor( categoryList.get( index ), index, "Name", categoryID );
//                        recyclerView.setAdapter( ShopsViewActivity.shopsViewAdaptor );
//                        ShopsViewActivity.shopsViewAdaptor.notifyDataSetChanged();
//                    }else if (ShopsViewActivity.shopsViewAdaptor != null ){
//                        ShopsViewActivity.shopsViewAdaptor.notifyDataSetChanged();
//                    }
                }else{

                }
            }
        } );
    }

    public static void getCityListQuery(@Nullable final Dialog dialog){
        areaCodeCityModelList.clear();
        firebaseFirestore.collection( "AREA_CODE" ).orderBy( "area_code" )
                .get().addOnCompleteListener( new OnCompleteListener <QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task <QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        String areaCode = String.valueOf( (long)documentSnapshot.get( "area_code" ) );
                        String areaName = documentSnapshot.get( "area_name" ).toString();
                        String cityName = documentSnapshot.get( "area_city" ).toString();
                        String cityCode = documentSnapshot.get( "area_city_code" ).toString();

                        areaCodeCityModelList.add( new AreaCodeCityModel( areaCode, areaName, cityName, cityCode ) );
                    }
                    if (MainActivity.selectAreaCityAdaptor != null){
                        MainActivity.selectAreaCityAdaptor.notifyDataSetChanged();
                    }
                    if (dialog != null)
                        dialog.dismiss();

                }else{
                    if (dialog != null)
                        dialog.dismiss();
                }
            }
        } );

    }
    public static void queryToAddNewArea(@Nullable final Context context, @Nullable final Dialog dialog, final Map<String, Object> updateMap ){

        firebaseFirestore.collection( "AREA_CODE" )
                .document( updateMap.get( "area_id" ).toString() )
                .set( updateMap )
                .addOnCompleteListener( new OnCompleteListener <Void>() {
                    @Override
                    public void onComplete(@NonNull Task <Void> task) {
                        if (task.isSuccessful()){
                            // Create Model And Update in Temp List.....
                            AreaCodeCityModel tModel =  new AreaCodeCityModel(
                                    updateMap.get( "area_id" ).toString() ,
                                    updateMap.get( "area_name" ).toString() ,
                                    updateMap.get( "area_city" ).toString() ,
                                    updateMap.get( "area_city_code" ).toString() );

                            int tIndex;
                            for ( tIndex = 0; tIndex < areaCodeCityModelList.size(); tIndex++ ){
                                if (areaCodeCityModelList.get( tIndex ).getAreaCode().equals( updateMap.get( "area_id" ).toString() )){
                                    break;
                                }
                            }

                            if (tIndex < areaCodeCityModelList.size()){
                                areaCodeCityModelList.set( tIndex, tModel );
                            }else{
                                areaCodeCityModelList.add( tModel );
                            }

                            // Show Message...
                            if (context!=null){
                                Toast.makeText( context, "Added Successfully!", Toast.LENGTH_SHORT ).show();
                            }

                        }else{
                            if (context!=null){
                                Toast.makeText( context, "Added Failed!", Toast.LENGTH_SHORT ).show();
                            }
                        }
                        if (dialog != null)
                            dialog.dismiss();
                    }
                } );

    }

    public static void getCityAndCityCode(){
        firebaseFirestore.collection( "HOME_PAGE" )
                .orderBy( "city_id" )
                .get()
                .addOnCompleteListener( new OnCompleteListener <QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task <QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            // : Assign Data into List
                            AreaCodeCityModel areaCodeCityModel;
                            for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()){
//                                String city_image = documentSnapshot.get( "city_image" ).toString();
                                String city_name = documentSnapshot.get( "city_name" ).toString();
                                String city_id = documentSnapshot.get( "city_id" ).toString();
                                String public_message_text = documentSnapshot.get( "public_message_text" ).toString();
                                String public_message_image = documentSnapshot.get( "public_message_image" ).toString();
                                String service_alert_text = documentSnapshot.get( "service_alert_text" ).toString();
                                String service_alert_image = documentSnapshot.get( "service_alert_image" ).toString();

                                long service_alert_type = (long)documentSnapshot.get( "service_alert_type" );
                                long public_message_type = (long)documentSnapshot.get( "public_message_type" );

                                boolean available_service = (boolean)documentSnapshot.get( "available_service" );
                                boolean public_message_activate = (boolean)documentSnapshot.get( "public_message_activate" );

                                areaCodeCityModel = new AreaCodeCityModel( null, city_name, city_id, available_service, public_message_activate
                                        , Integer.parseInt( String.valueOf( service_alert_type ) )
                                        , Integer.parseInt( String.valueOf( public_message_type ) )
                                        , service_alert_text, service_alert_image, public_message_text, public_message_image );

                                cityCodeAndNameList.add( areaCodeCityModel );

                                if (CityViewFragment.cityViewAdaptor!=null){
                                    CityViewFragment.cityViewAdaptor.notifyDataSetChanged();
                                }
                            }
                        }else{

                        }
                    }
                } );
    }
    public static void updateCityService( final String cityCode, final Map <String, Object> uploadMap){
        firebaseFirestore.collection( "HOME_PAGE" )
                .document( cityCode )
                .update( uploadMap )
                .addOnCompleteListener( new OnCompleteListener <Void>() {
                    @Override
                    public void onComplete(@NonNull Task <Void> task) {
                        if (task.isSuccessful()){

                        }else{

                        }
                    }
                } );
    }

    public static void getShopListOfCurrentCity(){
        shopListModelArrayList.clear();
        getCollectionRef( "SHOPS" ).orderBy( "shop_id" ).get()
                .addOnCompleteListener( new OnCompleteListener <QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task <QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        shopListModelArrayList.add( new ShopListModel(
                                documentSnapshot.getId(), documentSnapshot.get( "shop_name" ).toString()
                        ) );
                    }

                }else{

                }
            }
        } );

    }

    // Upload Query...
    public static void setNewCategoryOnDataBase(final Context context, final Dialog dialog, String categoryID, String categoryName, final int layoutIndex
            , final Map <String, Object> uploadMap ){

        // Create A Category and Assign Default Shop_layout Page...
        Map<String, Object> firstMap = new HashMap <>();
        firstMap.put( "category_id", categoryID );
        firstMap.put( "category_name", categoryName );
        firstMap.put( "type", SHOP_ITEMS_LAYOUT_CONTAINER );
        firstMap.put( "index", 0 );
        firstMap.put( "layout_id", "shop_layout" );

        getCollectionRef( categoryID ).document( "shop_layout" ).set( firstMap )
                .addOnCompleteListener( new OnCompleteListener <Void>() {
                    @Override
                    public void onComplete(@NonNull Task <Void> task) {
                        if (task.isSuccessful()){
                            // This is Update On Main Page Category.. Which is Home Page of Customer App.!
                            getCollectionRef("HOME").document( "cat_layout" ).update( uploadMap )
                                    .addOnCompleteListener( new OnCompleteListener <Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task <Void> task1) {
                                            if (task1.isSuccessful()){
                                                Toast.makeText( context, "Successfully added!", Toast.LENGTH_SHORT ).show();

                                            }else{
                                                Toast.makeText( context, "Failed!", Toast.LENGTH_SHORT ).show();
                                                homePageList.get( layoutIndex ).getBannerAndCatModelList().remove(
                                                        homePageList.get( layoutIndex ).getBannerAndCatModelList().size() - 1 );
                                            }
                                            dialog.dismiss();
                                            SecondActivity.homePageAdaptor.notifyDataSetChanged();
                                        }
                                    } );

                        }else{
                            Toast.makeText( context, "Category ID not Created.!", Toast.LENGTH_SHORT ).show();
                            dialog.dismiss();
                        }
                    }
                } );


    }
    public static void updateCategoryOnDatabase(@Nullable final Context context, @Nullable final Dialog dialog,@NonNull String layoutID, final Map <String, Object> uploadMap ){

//        layoutID = "cat_layout";

        // This is Update On Main Page Category.. Which is Home Page of Customer App.!
        getCollectionRef("HOME").document( layoutID ).update( uploadMap )
                .addOnCompleteListener( new OnCompleteListener <Void>() {
                    @Override
                    public void onComplete(@NonNull Task <Void> task1) {
                        if (task1.isSuccessful()){
                            if (context!=null)
                                Toast.makeText( context, "Successfully Updated!", Toast.LENGTH_SHORT ).show();
                        }else{
                            if (context!=null)
                                Toast.makeText( context, "Failed!", Toast.LENGTH_SHORT ).show();
                        }
                        if (dialog!=null)
                            dialog.dismiss();
                        if (SecondActivity.homePageAdaptor != null){
                            SecondActivity.homePageAdaptor.notifyDataSetChanged();
                        }

                    }
                } );

    }


    // Update Shop Activate / Deactivate ...

    public static void queryToActivateShop(final Context context, final Dialog dialog, final String shopId, final Boolean activateVal){
        firebaseFirestore.collection( "SHOPS" ).document( shopId )
                .update( "available_service", activateVal )
                .addOnCompleteListener( new OnCompleteListener <Void>() {
                    @Override
                    public void onComplete(@NonNull Task <Void> task) {
                        if (task.isSuccessful()){
                            DBQuery.getCollectionRef( "SHOPS" ).document( shopId )
                                    .update( "available_service", activateVal )
                                    .addOnCompleteListener( new OnCompleteListener <Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task <Void> task1) {
                                            if (task1.isSuccessful()){
                                                Toast.makeText( context, "Update Successfully!", Toast.LENGTH_SHORT ).show();
                                                ShopHomeActivity.shopHomeActivityModel.setServiceAvailable( activateVal );
                                            }else{
                                                Toast.makeText( context, "Failed!", Toast.LENGTH_SHORT ).show();
                                            }
                                            dialog.dismiss();
                                        }
                                    } );
                        }else{
                            Toast.makeText( context, "Failed!", Toast.LENGTH_SHORT ).show();
                            dialog.dismiss();
                        }
                    }
                } );
    }

    public static void queryAddShopMember(final Context context, final Dialog dialog, Map<String, Object> updateMap){

        String shopId = updateMap.get( "shop_id" ).toString();
        String adminMobile = updateMap.get( "admin_mobile" ).toString();

        firebaseFirestore.collection( "SHOPS" ).document( shopId )
                .collection( "ADMINS" )
                .document( adminMobile ).set( updateMap )
                .addOnCompleteListener( new OnCompleteListener <Void>() {
                    @Override
                    public void onComplete(@NonNull Task <Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText( context, "successfully Added!", Toast.LENGTH_SHORT ).show();
                            dialog.dismiss();
                        }else{
                            Toast.makeText( context, "Failed.! Something Went Wrong!", Toast.LENGTH_SHORT ).show();
                            dialog.dismiss();
                        }
                    }
                } );

    }


}
