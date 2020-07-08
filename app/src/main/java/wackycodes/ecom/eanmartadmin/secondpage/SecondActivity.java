package wackycodes.ecom.eanmartadmin.secondpage;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import wackycodes.ecom.eanmartadmin.R;
import wackycodes.ecom.eanmartadmin.addnewitem.AddNewLayoutActivity;
import wackycodes.ecom.eanmartadmin.database.DBQuery;
import wackycodes.ecom.eanmartadmin.other.CheckInternetConnection;
import wackycodes.ecom.eanmartadmin.other.DialogsClass;
import wackycodes.ecom.eanmartadmin.other.StaticMethods;
import wackycodes.ecom.eanmartadmin.other.UpdateImages;

import static wackycodes.ecom.eanmartadmin.database.DBQuery.getMainListDataQuery;
import static wackycodes.ecom.eanmartadmin.database.DBQuery.homePageList;
import static wackycodes.ecom.eanmartadmin.other.StaticValues.ADD_NEW_STRIP_AD_LAYOUT;
import static wackycodes.ecom.eanmartadmin.other.StaticValues.BANNER_SLIDER_CONTAINER_ITEM;
import static wackycodes.ecom.eanmartadmin.other.StaticValues.BANNER_SLIDER_LAYOUT_CONTAINER;
import static wackycodes.ecom.eanmartadmin.other.StaticValues.CATEGORY_ITEMS_LAYOUT_CONTAINER;
import static wackycodes.ecom.eanmartadmin.other.StaticValues.CURRENT_CITY_CODE;
import static wackycodes.ecom.eanmartadmin.other.StaticValues.GALLERY_CODE;
import static wackycodes.ecom.eanmartadmin.other.StaticValues.STRIP_AD_LAYOUT_CONTAINER;

public class SecondActivity extends AppCompatActivity {
    public static AppCompatActivity secondActivity;
    public static TextView cityName;
    public static TextView userName;

    private Toolbar toolbar;

    private RecyclerView secondRecycler;
    public static HomePageAdaptor homePageAdaptor;

    private static LinearLayout activityViewLayout;
    private static LinearLayout addNewLayoutBtn;

    private LinearLayout dialogAddLayout;
    private ConstraintLayout newBannerSlidderContainer; // add_banner_slider_layout
    private ConstraintLayout newGridLayoutContainer; // add_grid_layout
    private ConstraintLayout newStripAdLayout; // add_strip_ad_layout
    private LinearLayout addNewBannerSlidderBtn; // add_banner_slider_layout_LinearLay
    private LinearLayout addNewGirdLayoutBtn; // add_grid_layout_LinearLay
    private LinearLayout addNewStripAdLayoutBtn; // add_strip_ad_layout_LinearLay
    private ImageView closeAddLayout; //close_add_layout

    // Add New Category..
    private static LinearLayout addNewCatItemLayout; // add_new_cat_item_layout
    private TextView dialogOkBtn;
    private TextView dialogCancelBtn;
    private TextView dialogAddImage;
    private EditText dialogName;
    private TextView dialogUploadImage;
    private ImageView catImageView;
    private Uri catImageUri;
    public static int layoutIndex;
    private String newCatID;

    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_second );
        dialog = DialogsClass.getDialog( this );
        secondActivity = this;
        //...
        cityName = findViewById( R.id.user_city );
        userName = findViewById( R.id.user_name );

        activityViewLayout = findViewById( R.id.activity_view_layout);
        addNewLayoutBtn = findViewById( R.id.add_new_layout);
// Refresh Progress...
//        homeSwipeRefreshLayout = view.findViewById( R.id.home_swipe_refresh_layout );
//        homeSwipeRefreshLayout.setColorSchemeColors( getContext().getResources().getColor( R.color.colorPrimary ),
//                getContext().getResources().getColor( R.color.colorPrimary ),
//                getContext().getResources().getColor( R.color.colorPrimary ));
        // Refresh Progress...

        // Add Layout...
        dialogAddLayout = findViewById( R.id.dialog_add_layout );
        newBannerSlidderContainer = findViewById( R.id.add_banner_slider_layout );
        newGridLayoutContainer = findViewById( R.id.add_grid_layout );
        newStripAdLayout = findViewById( R.id.add_strip_ad_layout );
        addNewBannerSlidderBtn = findViewById( R.id.add_banner_slider_layout_LinearLay );
        addNewGirdLayoutBtn = findViewById( R.id.add_grid_layout_LinearLay );
        addNewStripAdLayoutBtn = findViewById( R.id.add_strip_ad_layout_LinearLay );
        closeAddLayout = findViewById( R.id.close_add_layout );

        // Add new Category...
        addNewCatItemLayout = findViewById( R.id.add_new_cat_item_layout );
        catImageView = findViewById( R.id.dialog_add_image );
        dialogOkBtn = findViewById( R.id.dialog_ok_btn );
        dialogCancelBtn = findViewById( R.id.dialog_cancel_btn );
        dialogAddImage = findViewById( R.id.dialog_change_image );
        dialogName = findViewById( R.id.dialog_name );
        dialogUploadImage = findViewById( R.id.dialog_upload_image );

        //...
        String toolTitle = "Title";
        toolbar = findViewById( R.id.appToolbar );
        setSupportActionBar( toolbar );
        try {
            getSupportActionBar().setDisplayShowTitleEnabled( true );
            getSupportActionBar().setTitle( toolTitle );
        }catch (NullPointerException ignored){ }

        // -------------
        secondRecycler = findViewById( R.id.second_activity_recycler );
        // Linear Layout...
        LinearLayoutManager homeCatLayoutManager = new LinearLayoutManager( this );
        homeCatLayoutManager.setOrientation( RecyclerView.VERTICAL );
        secondRecycler.setLayoutManager( homeCatLayoutManager );
        // Adaptor...
        homePageAdaptor = new HomePageAdaptor( 0, "Home Page" );
        secondRecycler.setAdapter( homePageAdaptor );
        homePageAdaptor.notifyDataSetChanged();
        if (homePageList.size()==0){
            dialog.show();
            getMainListDataQuery( this, dialog, null, "HOME", true, 0);
        }

        addNewLayoutBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDialogVisibility(true);
                newGridLayoutContainer.setVisibility( View.GONE );
            }
        } );
        addNewBannerSlidderBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDialogVisibility(false);
                AddNewLayoutActivity.isHomePage = true;
                Intent intent = new Intent( SecondActivity.this, AddNewLayoutActivity.class );
                intent.putExtra( "CAT_ID", "HOME" );
                intent.putExtra( "LAY_TYPE", BANNER_SLIDER_LAYOUT_CONTAINER );
                intent.putExtra( "LAY_INDEX", homePageList.size() );
                intent.putExtra( "TASK_UPDATE", false );
                intent.putExtra( "CAT_INDEX", 0 );
                startActivity( intent );
            }
        } );
        addNewStripAdLayoutBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDialogVisibility(false);
                AddNewLayoutActivity.isHomePage = true;
                Intent intent = new Intent( SecondActivity.this, AddNewLayoutActivity.class );
                intent.putExtra( "CAT_ID", "HOME" );
                intent.putExtra( "LAY_TYPE", STRIP_AD_LAYOUT_CONTAINER );
                intent.putExtra( "LAY_INDEX", homePageList.size() );
                intent.putExtra( "TASK_UPDATE", false );
                intent.putExtra( "CAT_INDEX", 0 );
                startActivity( intent );
            }
        } );

        closeAddLayout.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDialogVisibility(false);
            }
        } );

        // Add New Cat....
        addNewCategoryDialog();

    }

    @Override
    public void onBackPressed() {
        if (addNewCatItemLayout.getVisibility() == View.VISIBLE){
            if (UpdateImages.uploadImageLink != null){
                showAlertDialogForCat(newCatID );
            }else{
                super.onBackPressed();
            }
        }else{
            super.onBackPressed();
        }
    }

    private void setDialogVisibility(boolean isVisible){
        if(!isVisible){
            activityViewLayout.setVisibility( View.VISIBLE );
            addNewLayoutBtn.setVisibility( View.VISIBLE );
            dialogAddLayout.setVisibility( View.GONE );
        }else{
            activityViewLayout.setVisibility( View.GONE );
            addNewLayoutBtn.setVisibility( View.GONE );
            dialogAddLayout.setVisibility( View.VISIBLE );
        }
    }

    public static void setAddCatLayoutVisibility(boolean isVisible){
        if(!isVisible){
            activityViewLayout.setVisibility( View.VISIBLE );
            addNewLayoutBtn.setVisibility( View.VISIBLE );
            addNewCatItemLayout.setVisibility( View.GONE );
        }else{
            activityViewLayout.setVisibility( View.GONE );
            addNewLayoutBtn.setVisibility( View.GONE );
            addNewCatItemLayout.setVisibility( View.VISIBLE );
        }
    }

    public void addNewCategoryDialog(){
        newCatID = null;
        UpdateImages.uploadImageLink = null;
//        final Dialog dialogAddCat = new Dialog( this );
//        dialogAddCat.requestWindowFeature( Window.FEATURE_NO_TITLE );
//        dialogAddCat.setContentView( R.layout.dialog_add_cat_one_image_item );
//        dialogAddCat.getWindow().setLayout( ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT );
//        dialogAddCat.setCancelable( false );

        // Add Image...
        dialogAddImage.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent( Intent.ACTION_PICK );
                galleryIntent.setType( "image/*" );
                startActivityForResult( galleryIntent, GALLERY_CODE );
            }
        } );
        // Upload Images...
        dialogUploadImage.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Request to upload Image...
                if (catImageUri != null && isValidText( dialogName )  &&
                        CheckInternetConnection.isInternetConnected( SecondActivity.this )){
                    dialog.show();
                    // Generate Category Id...
                    newCatID = getNewCatID( dialogName.getText().toString() );

                    UpdateImages.uploadImageOnFirebaseStorage( SecondActivity.this, dialog, catImageUri, catImageView
                            , CURRENT_CITY_CODE + "/HOME/category"
                            , newCatID );
                }else{
                    if (catImageUri == null){
                        DialogsClass.alertDialog( SecondActivity.this, null, "Please Add Image First!" ).show();
                    }
                }
            }
        } );

        dialogOkBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UpdateImages.uploadImageLink != null && isValidText(dialogName) ){
                    dialog.show();
                    // Add OnDatabase..
                    Map<String, Object> catMap = new HashMap <>();
                    int position =  homePageList.get( layoutIndex ).getBannerAndCatModelList().size() + 1;
//                    catMap.put( "type", CATEGORY_ITEMS_LAYOUT_CONTAINER );
                    catMap.put( "no_of_cat", position );
                    catMap.put( "cat_id_"+position, newCatID );
                    catMap.put( "cat_name_"+position, dialogName.getText().toString()  );
                    catMap.put( "cat_image_"+position, UpdateImages.uploadImageLink );


                    homePageList.get( layoutIndex ).getBannerAndCatModelList().add( new BannerAndCatModel(
                            newCatID, UpdateImages.uploadImageLink, newCatID, CATEGORY_ITEMS_LAYOUT_CONTAINER,  dialogName.getText().toString(), ""
                    ) );
                    setAddCatLayoutVisibility(false);
                    DBQuery.setNewCategoryOnDataBase( SecondActivity.this, dialog, newCatID,  dialogName.getText().toString(), layoutIndex , catMap );
                }
            }
        } );

        dialogCancelBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(UpdateImages.uploadImageLink != null){
                    showAlertDialogForCat(newCatID );
                }else{
                   setAddCatLayoutVisibility( false );
                }
            }
        } );

    }

    private String getNewCatID(String catName){
        catName = catName.toUpperCase().trim();
        if (catName.length() < 6){
            catName = catName + "_" + StaticMethods.getTwoDigitRandom();
            return catName;
        }else{
            return catName;
        }

    }
    private boolean isValidText(EditText ref){
        if (TextUtils.isEmpty( ref.getText().toString() )){
            ref.setError( "Required.!" );
            return false;
        }else{
            String ct = ref.getText().toString().toUpperCase().trim();
            if ((ct.equals( "HOME" )) || (ct.equals( "SHOPS" ))){
                ref.setError( "Not Valid.! Already Reserved!" );
                return false;
            }else{
                return true;
            }
        }
    }

    // Get Result of Image...
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult( requestCode, resultCode, data );
        if (requestCode == GALLERY_CODE ){
            if (resultCode == RESULT_OK){
                if (data != null){
                    catImageUri = data.getData();
                    UpdateImages.uploadImageLink = null;
                    Glide.with( this ).load(  data.getData().toString() ).into( catImageView );
                }else{
                    showToast(  "Image not Found..!" );
                }
            }
        }
        // Get Response of cropped Image method....
    }

    private void showToast(String msg){
        Toast.makeText( this, msg, Toast.LENGTH_SHORT ).show();
    }

    private void showAlertDialogForCat(final String catID){
        // Show Warning dialog...
        AlertDialog.Builder builder = DialogsClass.alertDialog( this, "Alert!",  "Do You Want to Cancel adding Layout.?" );
        builder.setCancelable( false );
        builder.setPositiveButton( "YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogA, int which) {
                dialogA.dismiss();
                dialog.show();
                UpdateImages.deleteImageFromFirebase( SecondActivity.this, dialog
                        , CURRENT_CITY_CODE + "/HOME/category"
                        , catID  );
                setAddCatLayoutVisibility( false );
            }
        } ).setNegativeButton( "NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogA, int which) {
                dialogA.dismiss();
            }
        } ).show();
    }

}

