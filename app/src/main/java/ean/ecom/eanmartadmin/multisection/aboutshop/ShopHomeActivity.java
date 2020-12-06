package ean.ecom.eanmartadmin.multisection.aboutshop;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import ean.ecom.eanmartadmin.R;
import ean.ecom.eanmartadmin.database.ShopQuery;
import ean.ecom.eanmartadmin.mainpage.MainActivityGridModel;
import ean.ecom.eanmartadmin.multisection.shoplist.ShopListModel;
import ean.ecom.eanmartadmin.other.DialogsClass;
import ean.ecom.eanmartadmin.other.MyImageView;

import static ean.ecom.eanmartadmin.database.DBQuery.firebaseFirestore;
import static ean.ecom.eanmartadmin.multisection.aboutshop.UpdateShopFragment.DIALOG_ADD_NEW_MEMBER;
import static ean.ecom.eanmartadmin.multisection.aboutshop.UpdateShopFragment.DIALOG_PRODUCT_OPTIONS;
import static ean.ecom.eanmartadmin.other.StaticValues.SHOP_TYPE_NON_VEG;
import static ean.ecom.eanmartadmin.other.StaticValues.SHOP_TYPE_NO_SHOW;
import static ean.ecom.eanmartadmin.other.StaticValues.SHOP_TYPE_VEG;
import static ean.ecom.eanmartadmin.other.StaticValues.SHOP_TYPE_VEG_NON;

public class ShopHomeActivity extends AppCompatActivity implements UpdateShopListener{

    public static ShopListModel shopListModel;

    private MyImageView shopImage;
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

    // Report Layout...
    private LinearLayout orderLayout;
    private LinearLayout ratingListLayout;
    private LinearLayout checkIncomeLayout;

    private Dialog dialog;

    private boolean switchAction = false;

    private Toolbar toolbar;
    private List <MainActivityGridModel> shopHomeList = new ArrayList <>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_shop_home );
        dialog = DialogsClass.getDialog( this );

        //...
        toolbar = findViewById( R.id.appToolbar );
        setSupportActionBar( toolbar );
        try {
            getSupportActionBar().setDisplayShowTitleEnabled( true );
            getSupportActionBar().setDisplayHomeAsUpEnabled( true );
            getSupportActionBar().setTitle( "Shop Store" );
        }catch (NullPointerException ignored){ }

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

        orderLayout = findViewById( R.id.order_layout );
        ratingListLayout = findViewById( R.id.rating_layout );
        checkIncomeLayout = findViewById( R.id.income_report_layout );

//        shopHomeActivityModel = new AboutShopModel( shopID );
//        if (shopHomeActivityModel == null || !shopHomeActivityModel.getShopID().equals( shopID )){
//            shopHomeActivityModel = new AboutShopModel( shopID );
//        }

        getShopData(shopID);

        // shopActiveSwitch Action....
        shopActiveSwitch.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
                // Request to Database...
                if ( switchAction ){
                    String msg = "Do You Want to DeActivate Service of this Shop?";
                    if (isChecked){
                        msg = "Do You want to Activate Service?";
                    }
                    AlertDialog.Builder alertDialog = DialogsClass.alertDialog( ShopHomeActivity.this,  "Alert!", msg );
                    switchAction = false;
                    alertDialog.setCancelable( false );
                    alertDialog.setPositiveButton( "YES", new DialogInterface.OnClickListener() {
                        @SuppressLint("NewApi")
                        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                        @Override
                        public void onClick(DialogInterface dialogInterface, int which) {
                            dialogInterface.dismiss();
                            dialog.show();
                            ShopQuery.queryToActivateShop( ShopHomeActivity.this, dialog, shopListModel.getShop_id(), isChecked );
                            // Service Active...
                            if (isChecked){
//                                shopActiveSwitch.setChecked( true );
                                shopActiveSwitch.setText( "Active" );
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    shopActiveSwitch.setBackgroundTintList( getResources().getColorStateList( R.color.colorGreen ) );
                                }
                            }else{
//                                shopActiveSwitch.setChecked( false );
                                shopActiveSwitch.setText( "DeActive" );
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    shopActiveSwitch.setBackgroundTintList( getResources().getColorStateList( R.color.colorRed ) );
                                }
                            }

                            switchAction = true;

                        }
                    } ).setNegativeButton( "NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int which) {
                            dialogInterface.dismiss();
                            shopActiveSwitch.setChecked( !isChecked );

                            switchAction = true;
                        }
                    } );
                    alertDialog.show();

                }

            }
        } );

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        return super.onCreateOptionsMenu( menu );
        getMenuInflater().inflate( R.menu.menu_options_for_shop, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home){
            finish();
            return true;
        }else if ( id == R.id.menu_add_shop_member ){
            // Add ClickListener...   // Create and show the dialog.
            showUpdateDialog( new UpdateShopFragment( DIALOG_ADD_NEW_MEMBER, this, shopListModel.getShop_id(),
                    shopListModel.getShop_name() ) );
            return true;
        }else if ( id == R.id.menu_assign_products ){
            // Add ClickListener...
            showUpdateDialog( new UpdateShopFragment( DIALOG_PRODUCT_OPTIONS, this, shopListModel.getShop_id() ) );
            return true;
        }

        return true;
    }

    // Set Shop data...
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setShopData(){

        if (shopListModel == null){
            // Not Loaded
            return;
        }

        // Set Image...
        Glide.with( this ).load( shopListModel.getShop_image() ).into( shopImage );
//                .apply( new RequestOptions().placeholder( R.drawable.ic_account_circle_black_24dp ) )

        // Set Logo...
        Glide.with( this ).load( shopListModel.getShop_logo() )
                .apply( new RequestOptions().placeholder( R.drawable.ic_store_mall_directory_black_24dp ) )
                .into( shopLogo );

        // Set Shop Information...
        shopName.setText( shopListModel.getShop_name() );
        shopAddress.setText( shopListModel.getShop_address() );
        shopCategoryText.setText( shopListModel.getShop_category_name() );
        shopTagLine.setText( shopListModel.getShop_tag_line() );

        shopRating.setText( String.valueOf( shopListModel.getShop_ratings_stars()) );
        int shopVegCode = Integer.parseInt( shopListModel.getShop_veg_non_type() );
        // Veg Non...
        if ( shopVegCode == SHOP_TYPE_VEG ){
            shopVegLayout.setVisibility( View.VISIBLE );
            shopNonVegLayout.setVisibility( View.GONE );
        }else if ( shopVegCode == SHOP_TYPE_NON_VEG ){
            shopVegLayout.setVisibility( View.GONE );
            shopNonVegLayout.setVisibility( View.VISIBLE );
        }else if ( shopVegCode == SHOP_TYPE_VEG_NON ){
            shopVegLayout.setVisibility( View.VISIBLE );
            shopNonVegLayout.setVisibility( View.VISIBLE );
        }else if ( shopVegCode == SHOP_TYPE_NO_SHOW ){
            shopVegLayout.setVisibility( View.GONE );
            shopNonVegLayout.setVisibility( View.GONE );
        }

        // Set Close Open...
        if (shopListModel.isIs_open()){
            shopOpenCloseText.setText( "Open" );
            shopOpenCloseText.setBackgroundTintList( getResources().getColorStateList( R.color.colorGreen ) );
        }else{
            shopOpenCloseText.setText( "Close" );
            shopOpenCloseText.setBackgroundTintList( getResources().getColorStateList( R.color.colorRed ) );
        }

        shopOpenCloseTiming.setText( shopListModel.getShop_open_time() + "-" + shopListModel.getShop_close_time() );

        // Service Active...
        if (shopListModel.isAvailable_service()){
            shopActiveSwitch.setChecked( true );
            shopActiveSwitch.setText( "Active" );
            shopActiveSwitch.setBackgroundTintList( getResources().getColorStateList( R.color.colorGreen ) );
        }else{
            shopActiveSwitch.setChecked( false );
            shopActiveSwitch.setText( "DeActive" );
            shopActiveSwitch.setBackgroundTintList( getResources().getColorStateList( R.color.colorRed ) );
        }

        // Action Activate...
        switchAction = true;
    }

    private void showUpdateDialog( DialogFragment newFragment ){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        // Create and show the dialog.
//        DialogFragment newFragment = new UpdateShopFragment( dialogCode, ShopHomeActivity.this, this, shopHomeActivityModel.getShopID() );
        newFragment.show( ft,"dialog");
    }

    @Override
    public void showToast(String msg) {
        Toast.makeText( this, msg, Toast.LENGTH_SHORT ).show();
    }

    @Override
    public void showDialog() {
        dialog.show();
    }

    @Override
    public void dismissDialog() {
        dialog.dismiss();
    }

    private void getShopData(final String shopID){
        dialog.show();
        firebaseFirestore.collection( "SHOPS" ).document( shopID )
                .get().addOnCompleteListener( new OnCompleteListener <DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task <DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    shopListModel = task.getResult().toObject( ShopListModel.class );
                    // Set Data...
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        setShopData();
                    }
                }else{

                }
                dialog.dismiss();
            }
        } );

    }


}


//                if (task.isSuccessful()){
//                    DocumentSnapshot documentSnapshot = task.getResult();
//
//                    Boolean available_service = documentSnapshot.getBoolean( "available_service" );
//                    Boolean is_open = documentSnapshot.getBoolean( "is_open" );
//                    String shop_address = documentSnapshot.get( "shop_address" ).toString();
//                    String shop_area_code = documentSnapshot.get( "shop_area_code" ).toString();
////                    String shop_area_name = documentSnapshot.get( "shop_area_name" ).toString();
////                    String shop_cat_main = documentSnapshot.get( "shop_cat_main" ).toString();
//                    String shop_category_name = documentSnapshot.get( "shop_category_name" ).toString();
//
//                    // shop_categories
//
//                    String shop_city_name = documentSnapshot.get( "shop_city_name" ).toString();
////                    String shop_close_msg = documentSnapshot.get( "shop_close_msg" ).toString();
////                    String shop_id = documentSnapshot.get( "shop_id" ).toString();
//                    String shop_landmark = documentSnapshot.get( "shop_landmark" ).toString();
//                    String shop_logo = documentSnapshot.get( "shop_logo" ).toString();
//                    String shop_name = documentSnapshot.get( "shop_name" ).toString();
//
//                    String shop_owner_address = documentSnapshot.get( "shop_owner_address" ).toString();
//                    String shop_owner_name = documentSnapshot.get( "shop_owner_name" ).toString();
//                    String shop_owner_mobile = documentSnapshot.get( "shop_owner_mobile" ).toString();
//                    String shop_owner_email = documentSnapshot.get( "shop_owner_email" ).toString();
//                    String shop_veg_non_type = documentSnapshot.get( "shop_veg_non_type" ).toString();
//                    String shop_image = documentSnapshot.get( "shop_image" ).toString();
//                    String shop_rating = documentSnapshot.get( "shop_rating" ).toString();
//                    String shop_open_time;
//                    String shop_close_time;
//
//                    if ( documentSnapshot.get( "shop_open_time" )!=null && documentSnapshot.get( "shop_close_time" ) != null ){
//                        shop_open_time = documentSnapshot.get( "shop_open_time" ).toString();
//                        shop_close_time = documentSnapshot.get( "shop_close_time" ).toString();
//                    }else{
//                        shop_open_time = "AM";
//                        shop_close_time = "PM";
//                    }
//
//
//                    // tags
//
//                    shopHomeActivityModel.setShopID( shopID );
//                    shopHomeActivityModel.setServiceAvailable( available_service );
//                    shopHomeActivityModel.setOpen( is_open );
//                    shopHomeActivityModel.setShopAddress( shop_address );
//                    shopHomeActivityModel.setShopAreaCode( shop_area_code );
//                    shopHomeActivityModel.setShopCategory( shop_category_name );
//                    shopHomeActivityModel.setShopCity( shop_city_name );
////                    shopHomeActivityModel.setShopCloseTime( shop_close_msg );
//                    shopHomeActivityModel.setShopLandMark( shop_landmark );
//                    shopHomeActivityModel.setShopLogo( shop_logo );
//                    shopHomeActivityModel.setShopName( shop_name );
//                    shopHomeActivityModel.setShopOwnerAddress( shop_owner_address );
//                    shopHomeActivityModel.setShopOwnerName( shop_owner_name );
//                    shopHomeActivityModel.setShopOwnerMobile( shop_owner_mobile );
//                    shopHomeActivityModel.setShopOwnerEmail( shop_owner_email );
//                    shopHomeActivityModel.setShopVegNonCode( Integer.parseInt( shop_veg_non_type ) );
//                    shopHomeActivityModel.setShopImage( shop_image );
//                    shopHomeActivityModel.setShopRatingStars( shop_rating );
//                    shopHomeActivityModel.setShopOpenTime( shop_open_time );
//                    shopHomeActivityModel.setShopCloseTime( shop_close_time );
//
//
//                    // Set Data...
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                        setShopData();
//                    }
//                    dialog.dismiss();
//                }
//                else{
//                    // Failed...
//                    dialog.dismiss();
//                }
