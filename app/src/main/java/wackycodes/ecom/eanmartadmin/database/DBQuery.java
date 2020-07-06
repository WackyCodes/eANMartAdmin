package wackycodes.ecom.eanmartadmin.database;

import android.app.Dialog;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import wackycodes.ecom.eanmartadmin.admin.AreaCodeCityModel;
import wackycodes.ecom.eanmartadmin.category.ShopsViewActivity;
import wackycodes.ecom.eanmartadmin.secondpage.BannerAndCatModel;
import wackycodes.ecom.eanmartadmin.secondpage.HomeListModel;
import wackycodes.ecom.eanmartadmin.secondpage.SecondActivity;

import static wackycodes.ecom.eanmartadmin.other.StaticValues.BANNER_SLIDER_LAYOUT_CONTAINER;
import static wackycodes.ecom.eanmartadmin.other.StaticValues.CURRENT_CITY_CODE;
import static wackycodes.ecom.eanmartadmin.other.StaticValues.CATEGORY_ITEMS_LAYOUT_CONTAINER;
import static wackycodes.ecom.eanmartadmin.other.StaticValues.SHOP_ITEMS_LAYOUT_CONTAINER;
import static wackycodes.ecom.eanmartadmin.other.StaticValues.STRIP_AD_LAYOUT_CONTAINER;

public class DBQuery {

    public static FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    // get Current User Reference ...
    public static FirebaseUser currentUser = firebaseAuth.getCurrentUser();

    public static FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    public static final ArrayList<AreaCodeCityModel> areaCodeCityModelList = new ArrayList <>();

    public static List<HomeListModel> homePageList = new ArrayList <>();

    public static List <String> categoryIDList = new ArrayList <>();
    public static List<List <HomeListModel>> categoryList = new ArrayList <>();

    public static CollectionReference getCollectionRef(String collectionName){
        return firebaseFirestore.collection( "HOME_PAGE" ).document( CURRENT_CITY_CODE ).collection( collectionName );
    }

    public static void getMainListDataQuery(Context context, @Nullable final Dialog dialog, @Nullable final SwipeRefreshLayout refreshLayout,
                                       String categoryID, final boolean isHomePage, final int index){
        if (isHomePage){
            categoryIDList.clear();
            categoryList.clear();
        }else{
            categoryList.get( index ).clear();
        }

        getCollectionRef(categoryID.toUpperCase()).orderBy( "index" )
                .get().addOnCompleteListener( new OnCompleteListener <QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task <QuerySnapshot> task) {
                if (task.isSuccessful()){
                    // Load Data...
                    for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()){
                        int viewType = Integer.parseInt( String.valueOf( documentSnapshot.getLong( "type" ) ) );

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

                                BannerAndCatModel bannerAndCatModel = new BannerAndCatModel(
                                        catID, catImage, catID, 0
                                        , catName, ""  );
                                categoryIDList.add( catID );
                                categoryList.add( new ArrayList <HomeListModel>() );
                                bannerAndCatModelList.add( bannerAndCatModel );
                            }
                            // Add Data in homeList...
                            homePageList.add( new HomeListModel( CATEGORY_ITEMS_LAYOUT_CONTAINER, layout_id, bannerAndCatModelList ) );
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
                                        , "", ""  );
                                bannerAndCatModelList.add( bannerAndCatModel );
                            }
                            // Add Data in homeList...
                            if (isHomePage){
                                homePageList.add( new HomeListModel( BANNER_SLIDER_LAYOUT_CONTAINER, layout_id, bannerAndCatModelList ) );
                            }else{
                                categoryList.get( index ).add( new HomeListModel( BANNER_SLIDER_LAYOUT_CONTAINER, layout_id, bannerAndCatModelList ) );
                            }

                        }else
                        if (viewType == STRIP_AD_LAYOUT_CONTAINER){
//                                Boolean is_visible = documentSnapshot.getBoolean( "is_visible" );
                            String layout_id = documentSnapshot.getId();
                            String deletedId = documentSnapshot.get("delete_id").toString(); // TODO : add a field in model
                            String banner_image = documentSnapshot.get("banner_image").toString();
                            String banner_click_id = documentSnapshot.get("banner_click_id").toString();
                            Long banner_click_type = documentSnapshot.getLong("banner_click_type");
                            // Add Data in homeList...
                            if (isHomePage){
                                homePageList.add( new HomeListModel( STRIP_AD_LAYOUT_CONTAINER, layout_id, banner_image, banner_click_id
                                        , Integer.parseInt( String.valueOf( banner_click_type ))) );
                            }else{
                                categoryList.get( index ).add( new HomeListModel( STRIP_AD_LAYOUT_CONTAINER, layout_id, banner_image, banner_click_id
                                        , Integer.parseInt( String.valueOf( banner_click_type ))) );
                            }
                        }else
                        if (viewType == SHOP_ITEMS_LAYOUT_CONTAINER){
                            // This is only in shop View Case... but we have to check, cause.. to ad purpose we can use this in future...
                            // TODO:

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





}
