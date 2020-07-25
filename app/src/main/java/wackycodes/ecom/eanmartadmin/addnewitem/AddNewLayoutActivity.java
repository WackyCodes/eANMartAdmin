package wackycodes.ecom.eanmartadmin.addnewitem;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.skydoves.colorpickerview.ColorEnvelope;
import com.skydoves.colorpickerview.ColorPickerDialog;
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import wackycodes.ecom.eanmartadmin.R;
import wackycodes.ecom.eanmartadmin.other.UpdateImages;
import wackycodes.ecom.eanmartadmin.mainpage.homesection.categorysection.ShopListModel;
import wackycodes.ecom.eanmartadmin.mainpage.homesection.categorysection.ShopsViewActivity;
import wackycodes.ecom.eanmartadmin.database.DBQuery;
import wackycodes.ecom.eanmartadmin.other.CheckInternetConnection;
import wackycodes.ecom.eanmartadmin.other.DialogsClass;
import wackycodes.ecom.eanmartadmin.mainpage.homesection.BannerAndCatModel;
import wackycodes.ecom.eanmartadmin.mainpage.homesection.HomeListModel;
import wackycodes.ecom.eanmartadmin.mainpage.homesection.SecondActivity;

import static wackycodes.ecom.eanmartadmin.database.DBQuery.firebaseFirestore;
import static wackycodes.ecom.eanmartadmin.other.StaticMethods.getRandomNumAccordingToDate;
import static wackycodes.ecom.eanmartadmin.other.StaticValues.BANNER_CLICK_TYPE_SHOP;
import static wackycodes.ecom.eanmartadmin.other.StaticValues.BANNER_CLICK_TYPE_WEBSITE;
import static wackycodes.ecom.eanmartadmin.other.StaticValues.BANNER_SLIDER_CONTAINER_ITEM;
import static wackycodes.ecom.eanmartadmin.other.StaticValues.BANNER_SLIDER_LAYOUT_CONTAINER;
import static wackycodes.ecom.eanmartadmin.other.StaticValues.CURRENT_CITY_CODE;
import static wackycodes.ecom.eanmartadmin.other.StaticValues.GALLERY_CODE;
import static wackycodes.ecom.eanmartadmin.other.StaticValues.CATEGORY_ITEMS_LAYOUT_CONTAINER;
import static wackycodes.ecom.eanmartadmin.other.StaticValues.READ_EXTERNAL_MEMORY_CODE;
import static wackycodes.ecom.eanmartadmin.other.StaticValues.STRIP_AD_LAYOUT_CONTAINER;

public class AddNewLayoutActivity extends AppCompatActivity implements View.OnClickListener {

    private Dialog dialog;

    // Add New Banner in Slider....
    private LinearLayout bannerDialogLayoutFrame;
    private ImageView bannerDialogBannerImage;
    private TextView bannerDialogAddImage;
    private EditText bannerClickLink;
    private Spinner bannerSelectType;
    private AutoCompleteTextView bannerSelectShopID;
    private LinearLayout bannerClickLinkLayout;
    private LinearLayout bannerShopIDLayout;
    private TextView bannerDialogCancelBtn;
    private TextView bannerDialogOkBtn;
    private TextView bannerDialogUploadImage;

    private int bannerDialogType;
    private int bannerClickType = -1;
    private String bannerClickID;
    private Uri bannerImageUri = null;
    // Add New Banner in Slider....

    private int catIndex = -1;
    private String categoryTitle;
    private String categoryID;
    private int layoutType;
    private int layoutIndex = -1;
    private String layoutId = null;
    private boolean isTaskIsUpdate = false;
    private final String fileCode = getRandomNumAccordingToDate();

    List<HomeListModel> homePageList;
    public static Boolean isHomePage = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_add_new_layout );
        dialog = DialogsClass.getDialog( this );
        dialog.show();

        // get Value from Intent
        categoryID = getIntent().getStringExtra( "CAT_ID" );
        layoutType = getIntent().getIntExtra( "LAY_TYPE", -1 ); // TO know about which type of layout is requested ( Slider, Strip Ad, Or others..)
        layoutIndex = getIntent().getIntExtra( "LAY_INDEX", -1 ); // To know what is index of layout...( To use Update..

//        categoryTitle = getIntent().getStringExtra( "CAT_TITLE" ); // Not Usable...
        catIndex = getIntent().getIntExtra( "CAT_INDEX", -1 ); //  To know about Which Index list is gonna use ( Use for Category List )
        isTaskIsUpdate = getIntent().getBooleanExtra( "TASK_UPDATE", false );

        if (isHomePage){
            this.homePageList = DBQuery.homePageList;
        }else{
            // Assign Category List..
            this.homePageList = DBQuery.categoryList.get( catIndex );
        }

        // Add New Banner in Slider....
        bannerDialogLayoutFrame = findViewById( R.id.add_new_banner_item_LinearLay );
        bannerDialogBannerImage = findViewById( R.id.banner_image );
        bannerDialogAddImage = findViewById( R.id.change_banner );
        bannerClickLink = findViewById( R.id.banner_link_edit_text );
        bannerDialogUploadImage = findViewById( R.id.upload_banner );
        bannerDialogCancelBtn = findViewById( R.id.banner_cancel_txt );
        bannerDialogOkBtn = findViewById( R.id.banner_ok_txt );

        bannerSelectType = findViewById( R.id.select_banner_type_spinner );
        bannerSelectShopID = findViewById( R.id.select_shop_id_auto_text );
        bannerClickLinkLayout = findViewById( R.id.banner_link_layout );
        bannerShopIDLayout = findViewById( R.id.banner_shop_id_layout );

        bannerDialogAddImage.setOnClickListener( this );
        bannerDialogUploadImage.setOnClickListener( this );
        bannerDialogCancelBtn.setOnClickListener( this );
        bannerDialogOkBtn.setOnClickListener( this );
        // Add New Banner in Slider....

        if (isTaskIsUpdate){
            layoutId = homePageList.get( layoutIndex ).getLayoutID();
        }else{
            layoutId = getLayoutId();
        }

        switch (layoutType){
            case BANNER_SLIDER_LAYOUT_CONTAINER:
            case CATEGORY_ITEMS_LAYOUT_CONTAINER:
                dialog.dismiss();
                addNewLayout(layoutType);
                break;
            case STRIP_AD_LAYOUT_CONTAINER:
            case BANNER_SLIDER_CONTAINER_ITEM:
                dialog.dismiss();
                // show the Banner Dialog...
                bannerDialogLayoutFrame.setVisibility( View.VISIBLE );
                bannerDialogType = layoutType;
                if (isTaskIsUpdate ){
                    if ( UpdateImages.uploadImageBgColor != null){
                        bannerClickLink.setText( UpdateImages.uploadImageBgColor.substring( 1 ) );
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            Glide.with( this ).load( UpdateImages.uploadImageLink )
                                    .apply( new RequestOptions().placeholder( R.drawable.ic_panorama_black_24dp ) ).into( bannerDialogBannerImage );

                        }
                    }
                }
                else{
                    bannerDialogBannerImage.setVisibility( View.VISIBLE );
                    bannerDialogBannerImage.setImageResource( R.drawable.ic_panorama_black_24dp );
                    UpdateImages.uploadImageLink = null;
                    bannerImageUri = null;
                    bannerClickLink.setText( "" );
                }
                break;
            default:
                break;
        }

        // Spinner..
        setSpinnerList();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (UpdateImages.uploadImageLink != null ){
            showAlertDialogForBanner();
        }else{
            finish(); // Finish this Activity..
        }
    }

    @Override
    public void onClick(View v) {
        // --------- Add New Banner in Slider or Add ad layout of Strip or Banner...!
        if (v == bannerDialogAddImage){
            // Add Image..
            if (isPermissionGranted( )){
                Intent galleryIntent = new Intent( Intent.ACTION_PICK );
                galleryIntent.setType( "image/*" );
                startActivityForResult( galleryIntent, GALLERY_CODE );
            }
        }
        else if (v == bannerDialogUploadImage){
            if (bannerImageUri != null && CheckInternetConnection.isInternetConnected( this )){
                Dialog perDialog = DialogsClass.getDialog( this );
                perDialog.show();
                switch (bannerDialogType){
                    case STRIP_AD_LAYOUT_CONTAINER:
                        UpdateImages.uploadImageOnFirebaseStorage( this, perDialog, bannerImageUri, bannerDialogBannerImage
                                , CURRENT_CITY_CODE + "/HOME/ads"
                                , "strip_ad_"+ fileCode  );
                        break;
                    case BANNER_SLIDER_CONTAINER_ITEM:
                        UpdateImages.uploadImageOnFirebaseStorage( this, perDialog, bannerImageUri, bannerDialogBannerImage
                                , CURRENT_CITY_CODE + "/HOME/banners"
                                , "banner_" +  fileCode );
                        break;
                    default:
                        break;
                }
            }else {
                showToast( "Please select Image First..!" );
            }
        }
        else if (v == bannerDialogCancelBtn){
            if (UpdateImages.uploadImageLink != null ){
                showAlertDialogForBanner();
            } else{
                finish(); // Finish this Activity..
            }
        }
        else if (v == bannerDialogOkBtn){
            if (UpdateImages.uploadImageLink != null && bannerClickType != -1  ){
                switch (bannerClickType){
                    case BANNER_CLICK_TYPE_WEBSITE:
                        if (TextUtils.isEmpty( bannerClickLink.getText().toString() )){
                            bannerClickLink.setError( "Enter Link.!" );
                        }else{
                            bannerClickID = bannerClickLink.getText().toString().trim();
                            // Upload Data on Database...
                            addNewLayout( bannerDialogType );
                        }
                        break;
                    case BANNER_CLICK_TYPE_SHOP:
                        if ( bannerClickID != null ){
                            // Upload Data on Database...
                            addNewLayout( bannerDialogType );
                        }else{
                            bannerSelectShopID.setError( "Not Found!" );
                            showToast( "Enter Shop ID.!" );
                        }
                    default:
                        break;
                }

//                finish(); // Finish this Activity..
            } else
            if (UpdateImages.uploadImageLink == null){
                showToast( "Upload Image First..!" );
            }else
            if ( bannerClickType == -1 ){
                showToast( "Select Banner Type..!!" );
            }else{
                showToast( "Something Went Wrong ! Please Check Again!" );
            }
        }
        // --------- Add New Banner in Slider or Add ad layout of Strip or Banner...!
    }

    public void setSpinnerList(){
        // Select Banner Type...
        ArrayAdapter<String> dataAdapter = new ArrayAdapter <String>(this,
                android.R.layout.simple_spinner_item, getResources().getStringArray( R.array.banner_type ));
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bannerSelectType.setAdapter(dataAdapter);
        bannerSelectType.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView <?> parent, View view, int position, long id) {
                if ( position != 0){
                    switch (position){
                        case 1:
                            bannerClickLinkLayout.setVisibility( View.VISIBLE );
                            bannerShopIDLayout.setVisibility( View.GONE );
                            bannerClickType = BANNER_CLICK_TYPE_WEBSITE;
                            bannerClickID = null;
                            bannerClickLink.setText( "" );
                            break;
                        case 2:
                            bannerClickLinkLayout.setVisibility( View.GONE );
                            bannerShopIDLayout.setVisibility( View.VISIBLE );
                            bannerClickType = BANNER_CLICK_TYPE_SHOP;
                            bannerClickID = null;
                            bannerSelectShopID.setText( "" );
                            break;
                        default:
                                break;
                    }
                }
                else{
                    bannerClickID = null;
                    bannerClickType = -1;
                }
            }
            @Override
            public void onNothingSelected(AdapterView <?> parent) {
//
            }
        } );
        // Select Banner Type...

        // Select Shop ID...
        ArrayList<ShopListModel> shopListModelArrayList = new ArrayList <>();
        shopListModelArrayList.addAll( DBQuery.shopListModelArrayList );
//        showToast( "Size : "+ areaCodeCityModelArrayList.size() );
        SelectShopAdaptor selectShopAdaptorl =
                new SelectShopAdaptor( AddNewLayoutActivity.this, R.layout.select_shop_item_layout, shopListModelArrayList);

        bannerSelectShopID.setThreshold(1);
        bannerSelectShopID.setAdapter(selectShopAdaptorl);

        // handle click event and set desc on textView
        bannerSelectShopID.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ShopListModel shopListModel = (ShopListModel) adapterView.getItemAtPosition(i);
                bannerClickID = shopListModel.getShopID();
            }
        });

    }

    private void addNewLayout(int type){
        switch (type){
            case BANNER_SLIDER_LAYOUT_CONTAINER:           //-- 1
                Map <String, Object> layoutMap = new HashMap <>();
                layoutMap.put( "index", homePageList.size() ); // int
                layoutMap.put( "layout_id", layoutId); // String
//                layoutMap.put( "layout_bg", "#DADADA" ); // For sample
                layoutMap.put( "is_visible", false ); // boolean
                layoutMap.put( "no_of_banners", 0 ); // int
                layoutMap.put( "type", BANNER_SLIDER_LAYOUT_CONTAINER ); // int
                dialog.show();
                uploadNewLayoutOnDatabase( layoutMap, BANNER_SLIDER_LAYOUT_CONTAINER, dialog );
                break;
            case BANNER_SLIDER_CONTAINER_ITEM:
                Map<String, Object> bSliderItem = new HashMap <>();
                // We have to add all the list data in this map  And Set this list on the database location...
                String bannerImgLink = UpdateImages.uploadImageLink;
                homePageList.get( layoutIndex ).getBannerAndCatModelList().add( new BannerAndCatModel(
                        null, bannerImgLink, bannerClickID, bannerClickType, "banner_" +fileCode , "extra Text" ) );

                int bannerNo =  homePageList.get( layoutIndex ).getBannerAndCatModelList().size();
//                    bSliderItem.put( "index", layoutIndex );
                bSliderItem.put( "layout_id", homePageList.get( layoutIndex ).getLayoutID() );
                bSliderItem.put( "banner_"+ bannerNo, bannerImgLink ); // String
                bSliderItem.put( "banner_click_id_"+ bannerNo, bannerClickID ); // String
                bSliderItem.put( "banner_click_type_"+ bannerNo, bannerClickType ); // int
                bSliderItem.put( "delete_id_"+ bannerNo, "banner_"+fileCode ); // String
                bSliderItem.put( "no_of_banners", bannerNo ); // int
                if (bannerNo == 3){
                    bSliderItem.put( "is_visible", true );
                }
//                    for (int bsInd = 1; bsInd <= bannerSliderModelList.size(); bsInd++){
//                        bSliderItem.put( "banner_"+ bsInd, bannerSliderModelList.get( bsInd-1 ).getImageLink() );
//                        bSliderItem.put( "banner_"+ bsInd + "_bg", "#20202f" );
//                    }
                dialog.show();
                updateLayoutOnDatabase( bSliderItem, BANNER_SLIDER_CONTAINER_ITEM, dialog );
                ///
                break;
            case STRIP_AD_LAYOUT_CONTAINER:         //-- 2
                Map<String, Object> stripMap = new HashMap <>();
                stripMap.put( "index", homePageList.size() );
                stripMap.put( "layout_id", layoutId );
                stripMap.put( "is_visible", true );
                stripMap.put( "banner_image", UpdateImages.uploadImageLink );
                stripMap.put( "banner_click_id", bannerClickID );
                stripMap.put( "banner_click_type", bannerClickType );
                stripMap.put( "type", STRIP_AD_LAYOUT_CONTAINER );
                stripMap.put( "delete_id", "strip_ad_"+fileCode );
                stripMap.put( "extra_text", "New" );
                dialog.show();
                uploadNewLayoutOnDatabase( stripMap, STRIP_AD_LAYOUT_CONTAINER, dialog );
                break;

            case CATEGORY_ITEMS_LAYOUT_CONTAINER:            //-- 5
//                    addNewHrGridLayout( CATEGORY_ITEMS_LAYOUT_CONTAINER );
                break;
            default:
                break;
        }
    }

    private void uploadNewLayoutOnDatabase(final Map<String, Object> layoutMap, final int view_type, final Dialog dialog ){
        // we are set our unique Id... Because we need this id to update data...
        final String documentId = layoutMap.get( "layout_id" ).toString();

        firebaseFirestore.collection( "HOME_PAGE" ).document( CURRENT_CITY_CODE )
                    .collection( categoryID ).document( documentId ).set( layoutMap )
                    .addOnCompleteListener( new OnCompleteListener <Void>() {
                        @Override
                        public void onComplete(@NonNull Task <Void> task) {
                            if (task.isSuccessful()){
                                switch (view_type){
                                    case BANNER_SLIDER_LAYOUT_CONTAINER:
                                        homePageList.add( new HomeListModel( view_type, documentId, new ArrayList <BannerAndCatModel>() ) );
                                        break;
                                    case BANNER_SLIDER_CONTAINER_ITEM:
                                        // Notify Data Changed..  of Adaptor...
                                        break;
                                    case STRIP_AD_LAYOUT_CONTAINER:
                                        homePageList.add( new HomeListModel( view_type, documentId, layoutMap.get( "banner_image" ).toString()
                                                , layoutMap.get( "banner_click_id").toString(), layoutMap.get( "delete_id").toString(),
                                                Integer.parseInt( layoutMap.get( "banner_click_type" ).toString() )));
                                        break;
                                    default:
                                        break;
                                }
//                                CommonCatActivity.commonCatAdaptor.notifyDataSetChanged();
                                dialog.dismiss();
                                showToast( "Added Successfully..!" );
                                if (isHomePage){
                                    DBQuery.homePageList = homePageList;
                                    SecondActivity.homePageAdaptor.notifyDataSetChanged();
                                }else{
                                    // Assign Category List..
                                    DBQuery.categoryList.set( catIndex, homePageList );
                                    ShopsViewActivity.shopsViewAdaptor.notifyDataSetChanged();
                                }
                                finish();
                            }else{
                                dialog.dismiss();
                                if (view_type == BANNER_SLIDER_CONTAINER_ITEM){
//                                    commonCatList.get( catIndex ).get( layoutIndex ).getBannerAndCatModelList().remove(
//                                            commonCatList.get( catIndex ).get( layoutIndex ).getBannerAndCatModelList().size()-1 );
                                }
                                showToast( "failed..! Error : " + task.getException().getMessage() );
                            }
                        }
                    } );
//        }else{
//            dialog.dismiss();
//        }

    }
    private void updateLayoutOnDatabase(final Map<String, Object> layoutMap, final int view_type, final Dialog dialog ){
        // we are set our unique Id... Because we need this id to update data...
        final String documentId = layoutMap.get( "layout_id" ).toString();

        firebaseFirestore.collection( "HOME_PAGE" ).document( CURRENT_CITY_CODE )
                .collection( categoryID ).document( documentId ).update( layoutMap )
                .addOnCompleteListener( new OnCompleteListener <Void>() {
                    @Override
                    public void onComplete(@NonNull Task <Void> task) {
                        if (task.isSuccessful()){
                            switch (view_type){
                                case BANNER_SLIDER_LAYOUT_CONTAINER:
                                    homePageList.add( new HomeListModel( view_type, documentId, new ArrayList <BannerAndCatModel>() ) );
                                    break;
                                case BANNER_SLIDER_CONTAINER_ITEM:
                                    // Notify Data Changed..  of Adaptor...
                                    break;
                                case STRIP_AD_LAYOUT_CONTAINER:
                                    homePageList.add( new HomeListModel( view_type, documentId, layoutMap.get( "banner_image" ).toString()
                                            , layoutMap.get( "banner_click_id").toString(), layoutMap.get( "delete_id").toString(),
                                            Integer.parseInt( layoutMap.get( "banner_click_type" ).toString() )));
                                    break;
                                default:
                                    break;
                            }
//                                CommonCatActivity.commonCatAdaptor.notifyDataSetChanged();
                            dialog.dismiss();
                            showToast( "Added Successfully..!" );
                            if (isHomePage){
                                DBQuery.homePageList = homePageList;
                                SecondActivity.homePageAdaptor.notifyDataSetChanged();
                            }else{
                                // Assign Category List..
                                DBQuery.categoryList.set( catIndex, homePageList );
                                ShopsViewActivity.shopsViewAdaptor.notifyDataSetChanged();
                            }
                            finish();
                        }else{
                            dialog.dismiss();
                            if (view_type == BANNER_SLIDER_CONTAINER_ITEM){
//                                    commonCatList.get( catIndex ).get( layoutIndex ).getBannerAndCatModelList().remove(
//                                            commonCatList.get( catIndex ).get( layoutIndex ).getBannerAndCatModelList().size()-1 );
                            }
                            showToast( "failed..! Error : " + task.getException().getMessage() );
                        }
                    }
                } );
//        }else{
//            dialog.dismiss();
//        }

    }

    // ----------
    private String getLayoutId(){
        String layout = null;
        int sizeOfList = homePageList.size();
        List<String> tempIdList = new ArrayList <>();

        for (int i = 0; i < sizeOfList; i++){
            tempIdList.add( homePageList.get( i ).getLayoutID() ); //  "layout_id", "layout_"+
        }

        for (int i = 0; i <= sizeOfList; i++){
            layout = "layout_"+ i;
            if ( !tempIdList.contains( layout )){
//                layoutId = layout;
                break;
            }
        }
        return layout;
    }

    private void showAlertDialogForBanner(){
        // Show Warning dialog...
        AlertDialog.Builder builder = new AlertDialog.Builder( this );
        builder.setTitle( "Do You Want to Cancel adding Layout.?" );
        builder.setCancelable( false );
        builder.setPositiveButton( "YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogA, int which) {
                dialogA.dismiss();
                dialog.show();
                switch (bannerDialogType){
                    case STRIP_AD_LAYOUT_CONTAINER:
                        UpdateImages.deleteImageFromFirebase( AddNewLayoutActivity.this, dialog
                                , CURRENT_CITY_CODE + "/HOME/ads"
                                , "strip_ad_"+fileCode  );
                        break;

                    case BANNER_SLIDER_CONTAINER_ITEM:
                        UpdateImages.deleteImageFromFirebase( AddNewLayoutActivity.this, dialog
                                ,CURRENT_CITY_CODE + "/HOME/banners"
                                , "banner_" +fileCode  );
                        break;
                    default:
                        break;
                }// Finish this Activity..
            }
        } ).setNegativeButton( "NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogA, int which) {
                dialogA.dismiss();
            }
        } ).show();
    }
    private void showToast(String msg){
        Toast.makeText( this, msg, Toast.LENGTH_SHORT ).show();
    }

    // Permission...
    private boolean isPermissionGranted(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if ( this.checkSelfPermission( Manifest.permission.READ_EXTERNAL_STORAGE ) == PackageManager.PERMISSION_GRANTED){
                // TODO : YES
                return true;
            }else{
                // TODO : Request For Permission..!
                requestPermissions( new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXTERNAL_MEMORY_CODE );
                return false;
            }
        }else{
            return true;
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult( requestCode, permissions, grantResults );
        if (requestCode == READ_EXTERNAL_MEMORY_CODE){
            if (grantResults[0] !=  PackageManager.PERMISSION_GRANTED){
                showToast( "Permission granted..!" );
            }else{
                showToast( "Permission Denied..! Please Grant Permission first.!!");
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
                    Uri uri = data.getData();
                    startCropImageActivity(uri);
                }else{
                    showToast(  "Image not Found..!" );
                }
            }
        }
        // Get Response of cropped Image method....
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
//                Bitmap bitmap = result.getBitmap();

//                Glide.with( this ).load( resultUri ).into( croppedImage );
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), resultUri);
                    startCompressImage(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                showToast( error.getMessage() );
            }
        }

    }

    private void startCropImageActivity(Uri imageUri) {

        if (bannerDialogType == BANNER_SLIDER_CONTAINER_ITEM){
            CropImage.activity(imageUri)
                    .setGuidelines( CropImageView.Guidelines.ON)
                    .setAspectRatio( 2,1 )
                    .setMultiTouchEnabled(true)
                    .start(this);
        }else{
            CropImage.activity(imageUri)
                    .setGuidelines( CropImageView.Guidelines.ON)
                    .setMultiTouchEnabled(true)
                    .start(this);
        }

    }
    public Uri getImageUri( Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(this.getContentResolver(), inImage, "Title", null);
//        MediaStore.Images.Media.getContentUri(  );
        return Uri.parse(path);
    }

    private void startCompressImage(@NonNull Bitmap bitmap){
//        Bitmap bitmap = ((BitmapDrawable)view.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
//        byte[] imageInByte = stream.toByteArray();
//        long sizeOfImage = imageInByte.length/1024;

        bitmap.compress( Bitmap.CompressFormat.JPEG,35, stream);
        byte[] BYTE = stream.toByteArray();
        Bitmap newBitmap = BitmapFactory.decodeByteArray(BYTE,0,BYTE.length);

//        String imgLink = getImageUri(newBitmap).toString();
        bannerImageUri = getImageUri(newBitmap);
        UpdateImages.uploadImageLink = null;
        Glide.with( this ).load( bannerImageUri ).into( bannerDialogBannerImage );

    }

    // Color Picker...Dialog.
    private void bannerDialogColorPicker(){
        new ColorPickerDialog.Builder( this, AlertDialog.THEME_HOLO_DARK )
                .setTitle("ColorPicker Dialog")
                .setPreferenceName("MyColorPickerDialog")
                .setCancelable( false )
                .setPositiveButton(("Confirm"),
                        new ColorEnvelopeListener() {
                            @Override
                            public void onColorSelected(ColorEnvelope envelope, boolean fromUser) {
                                String color = "#" + envelope.getHexCode();
                                bannerClickLink.setText( envelope.getHexCode() );
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    bannerDialogBannerImage.setBackgroundTintList( ColorStateList.valueOf( Color.parseColor( color ) ));
                                }
                            }
                        })
                .setNegativeButton(("Cancel"),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                .attachAlphaSlideBar(true) // default is true. If false, do not show the AlphaSlideBar.
                .attachBrightnessSlideBar(true)  // default is true. If false, do not show the BrightnessSlideBar.
                .show();
    }


}
