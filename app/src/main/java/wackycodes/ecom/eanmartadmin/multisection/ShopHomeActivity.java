package wackycodes.ecom.eanmartadmin.multisection;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import de.hdodenhof.circleimageview.CircleImageView;
import wackycodes.ecom.eanmartadmin.R;
import wackycodes.ecom.eanmartadmin.other.DialogsClass;

import static wackycodes.ecom.eanmartadmin.database.DBQuery.firebaseFirestore;
import static wackycodes.ecom.eanmartadmin.database.DBQuery.shopListModelArrayList;
import static wackycodes.ecom.eanmartadmin.other.StaticValues.SHOP_TYPE_NON_VEG;
import static wackycodes.ecom.eanmartadmin.other.StaticValues.SHOP_TYPE_NO_SHOW;
import static wackycodes.ecom.eanmartadmin.other.StaticValues.SHOP_TYPE_VEG;
import static wackycodes.ecom.eanmartadmin.other.StaticValues.SHOP_TYPE_VEG_NON;

public class ShopHomeActivity extends AppCompatActivity {


    private static AboutShopModel shopHomeActivityModel = new AboutShopModel();

    private ImageView shopImage;
    private CircleImageView shopLogo;
    private TextView shopIdText;
    private TextView shopName;
    private TextView shopAddress;
    private TextView shopCategoryText;
    private TextView shopTagLine;
    // Rating...
    private TextView shopRating;
    private LinearLayout shopVegLayout;
    private LinearLayout shopNonVegLayout;

    // Open Close...
    private TextView shopOpenCloseText;
    private TextView shopOpenCloseTiming;

    // Service Available..
    private Switch shopActiveSwitch;
    private ImageView verifyTagImage;

    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_shop_home );
        dialog = DialogsClass.getDialog( this );

        String shopID = getIntent().getStringExtra( "SHOP_ID" );
        shopIdText = findViewById( R.id.shop_id_text );
        shopIdText.setText( shopID );

        // Assign....
        shopImage = findViewById( R.id.shop_image );
        shopLogo = findViewById( R.id.shop_logo_circle_imageView );
        shopName = findViewById( R.id.shop_name_text );
        shopAddress = findViewById( R.id.shop_address );
        shopCategoryText = findViewById( R.id.shop_category_text );
        shopTagLine = findViewById( R.id.shop_tag_line_text );
        shopRating = findViewById( R.id.shop_rating_text );
        shopVegLayout = findViewById( R.id.shop_veg_layout );
        shopNonVegLayout = findViewById( R.id.shop_non_veg_layout );
        shopOpenCloseText = findViewById( R.id.shop_open_close_text );
        shopOpenCloseTiming = findViewById( R.id.shop_opening_timing );
        shopActiveSwitch = findViewById( R.id.shop_service_available_switch );
        verifyTagImage = findViewById( R.id.verify_tag_image );

//        shopHomeActivityModel = new AboutShopModel( shopID );
//        if (shopHomeActivityModel == null || !shopHomeActivityModel.getShopID().equals( shopID )){
//            shopHomeActivityModel = new AboutShopModel( shopID );
//        }
        getShopData(shopID);

    }

    // Set Shop data...
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setShopData(){

        // Set Image...
        Glide.with( this ).load( shopHomeActivityModel.getShopImage() )
                .apply( new RequestOptions().placeholder( R.drawable.ic_account_circle_black_24dp ) )
                .into( shopImage );
        // Set Logo...
        Glide.with( this ).load( shopHomeActivityModel.getShopLogo() )
                .apply( new RequestOptions().placeholder( R.drawable.ic_account_circle_black_24dp ) )
                .into( shopLogo );

        // Set Shop Information...
        shopName.setText( shopHomeActivityModel.getShopName() );
        shopAddress.setText( shopHomeActivityModel.getShopAddress() );
        shopCategoryText.setText( shopHomeActivityModel.getShopCategory() );
        shopTagLine.setText( shopHomeActivityModel.getShopTagLine() );

        shopRating.setText( shopHomeActivityModel.getShopRatingStars() );
        // Veg Non...
        if (shopHomeActivityModel.getShopVegNonCode() == SHOP_TYPE_VEG ){
            shopVegLayout.setVisibility( View.VISIBLE );
            shopNonVegLayout.setVisibility( View.GONE );
        }else if (shopHomeActivityModel.getShopVegNonCode() == SHOP_TYPE_NON_VEG ){
            shopVegLayout.setVisibility( View.GONE );
            shopNonVegLayout.setVisibility( View.VISIBLE );
        }else if (shopHomeActivityModel.getShopVegNonCode() == SHOP_TYPE_VEG_NON ){
            shopVegLayout.setVisibility( View.VISIBLE );
            shopNonVegLayout.setVisibility( View.VISIBLE );
        }else if (shopHomeActivityModel.getShopVegNonCode() == SHOP_TYPE_NO_SHOW ){
            shopVegLayout.setVisibility( View.GONE );
            shopNonVegLayout.setVisibility( View.GONE );
        }

        // Set Close Open...
        if (shopHomeActivityModel.isOpen()){
            shopOpenCloseText.setText( "Open" );
            shopOpenCloseText.setBackgroundTintList( getResources().getColorStateList( R.color.colorGreen ) );
        }else{
            shopOpenCloseText.setText( "Close" );
            shopOpenCloseText.setBackgroundTintList( getResources().getColorStateList( R.color.colorRed ) );
        }

        shopOpenCloseTiming.setText( shopHomeActivityModel.getShopOpenTime() + "-" + shopHomeActivityModel.getShopCloseTime() );

        // Service Active...
        if (shopHomeActivityModel.isServiceAvailable()){
            shopActiveSwitch.setChecked( true );
            shopActiveSwitch.setText( "Active" );
            shopActiveSwitch.setBackgroundTintList( getResources().getColorStateList( R.color.colorGreen ) );
        }else{
            shopActiveSwitch.setChecked( false );
            shopActiveSwitch.setText( "DeActive" );
            shopActiveSwitch.setBackgroundTintList( getResources().getColorStateList( R.color.colorRed ) );
        }
    }

    private void getShopData(final String shopID){
        dialog.show();
        firebaseFirestore.collection( "SHOPS" ).document( shopID )
                .get().addOnCompleteListener( new OnCompleteListener <DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task <DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();

                    Boolean available_service = documentSnapshot.getBoolean( "available_service" );
                    Boolean is_open = documentSnapshot.getBoolean( "is_open" );
                    String shop_address = documentSnapshot.get( "shop_address" ).toString();
                    String shop_area_code = documentSnapshot.get( "shop_area_code" ).toString();
//                    String shop_area_name = documentSnapshot.get( "shop_area_name" ).toString();
                    String shop_cat_main = documentSnapshot.get( "shop_cat_main" ).toString();

                    // shop_categories

                    String shop_city_name = documentSnapshot.get( "shop_city_name" ).toString();
//                    String shop_close_msg = documentSnapshot.get( "shop_close_msg" ).toString();
//                    String shop_id = documentSnapshot.get( "shop_id" ).toString();
                    String shop_landmark = documentSnapshot.get( "shop_landmark" ).toString();
                    String shop_logo = documentSnapshot.get( "shop_logo" ).toString();
                    String shop_name = documentSnapshot.get( "shop_name" ).toString();

                    String shop_owner_address = documentSnapshot.get( "shop_owner_address" ).toString();
                    String shop_owner_name = documentSnapshot.get( "shop_owner_name" ).toString();
                    String shop_owner_mobile = documentSnapshot.get( "shop_owner_mobile" ).toString();
                    String shop_owner_email = documentSnapshot.get( "shop_owner_email" ).toString();
                    String shop_veg_non_type = documentSnapshot.get( "shop_veg_non_type" ).toString();
                    String shop_image = documentSnapshot.get( "shop_image" ).toString();
                    String shop_rating = documentSnapshot.get( "shop_rating" ).toString();
                    String shop_open_time;
                    String shop_close_time;

                    if ( documentSnapshot.get( "shop_open_time" )!=null && documentSnapshot.get( "shop_close_time" ) != null ){
                        shop_open_time = documentSnapshot.get( "shop_open_time" ).toString();
                        shop_close_time = documentSnapshot.get( "shop_close_time" ).toString();
                    }else{
                        shop_open_time = "AM";
                        shop_close_time = "PM";
                    }


                    // tags

                    shopHomeActivityModel.setShopID( shopID );
                    shopHomeActivityModel.setServiceAvailable( available_service );
                    shopHomeActivityModel.setOpen( is_open );
                    shopHomeActivityModel.setShopAddress( shop_address );
                    shopHomeActivityModel.setShopAreaCode( shop_area_code );
                    shopHomeActivityModel.setShopCategory( shop_cat_main );
                    shopHomeActivityModel.setShopCity( shop_city_name );
//                    shopHomeActivityModel.setShopCloseTime( shop_close_msg );
                    shopHomeActivityModel.setShopLandMark( shop_landmark );
                    shopHomeActivityModel.setShopLogo( shop_logo );
                    shopHomeActivityModel.setShopName( shop_name );
                    shopHomeActivityModel.setShopOwnerAddress( shop_owner_address );
                    shopHomeActivityModel.setShopOwnerName( shop_owner_name );
                    shopHomeActivityModel.setShopOwnerMobile( shop_owner_mobile );
                    shopHomeActivityModel.setShopOwnerEmail( shop_owner_email );
                    shopHomeActivityModel.setShopVegNonCode( Integer.parseInt( shop_veg_non_type ) );
                    shopHomeActivityModel.setShopImage( shop_image );
                    shopHomeActivityModel.setShopRatingStars( shop_rating );
                    shopHomeActivityModel.setShopOpenTime( shop_open_time );
                    shopHomeActivityModel.setShopCloseTime( shop_close_time );


                    // Set Data...
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        setShopData();
                    }
                    dialog.dismiss();
                }
                else{
                    // Failed...
                    dialog.dismiss();
                }
            }
        } );

    }

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
     * 14. shop_map_latitude :
     * 15. shop_map_longitude :
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
