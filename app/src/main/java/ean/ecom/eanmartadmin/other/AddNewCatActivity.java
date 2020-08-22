package ean.ecom.eanmartadmin.other;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;

import java.util.HashMap;
import java.util.Map;

import ean.ecom.eanmartadmin.R;
import ean.ecom.eanmartadmin.database.DBQuery;
import ean.ecom.eanmartadmin.mainpage.homesection.BannerAndCatModel;

import static android.app.Activity.RESULT_OK;
import static ean.ecom.eanmartadmin.database.DBQuery.homePageList;
import static ean.ecom.eanmartadmin.other.StaticValues.CATEGORY_ITEMS_LAYOUT_CONTAINER;
import static ean.ecom.eanmartadmin.other.StaticValues.CURRENT_CITY_CODE;
import static ean.ecom.eanmartadmin.other.StaticValues.GALLERY_CODE;

/**
 * To Add New Category...! But Not in Use...!
 */
public class AddNewCatActivity {

    private Context context;

    public AddNewCatActivity(Context context) {
        this.context = context;
    }
    // Add New Category..
    private ImageView catImageView;
    private Uri catImageUri;

    private Dialog dialog = DialogsClass.getDialog( context );
    private String newCatID;
    public void addNewCategoryDialog(final int layoutIndex ){
        newCatID = null;
        UpdateImages.uploadImageLink = null;
        final Dialog dialogAddCat = new Dialog( context );
        dialogAddCat.requestWindowFeature( Window.FEATURE_NO_TITLE );
        dialogAddCat.setContentView( R.layout.dialog_add_cat_one_image_item );
        dialogAddCat.getWindow().setLayout( ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT );
        dialogAddCat.setCancelable( false );
        catImageView = dialogAddCat.findViewById( R.id.dialog_add_image );
        TextView okBtn = dialogAddCat.findViewById( R.id.dialog_ok_btn );
        TextView cancelBtn = dialogAddCat.findViewById( R.id.dialog_cancel_btn );
        TextView addImage = dialogAddCat.findViewById( R.id.dialog_change_image );
        final EditText dialogName = dialogAddCat.findViewById( R.id.dialog_name );
        TextView uploadImage = dialogAddCat.findViewById( R.id.dialog_upload_image );

        // Add Image...
        addImage.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent( Intent.ACTION_PICK );
                galleryIntent.setType( "image/*" );
                ActivityCompat.startActivityForResult( (Activity) context, galleryIntent, GALLERY_CODE, null );
            }
        } );
        // Upload Images...
        uploadImage.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Request to upload Image...
                if (catImageUri != null && isValidText( dialogName )  &&
                        CheckInternetConnection.isInternetConnected( context )){
                    dialog.show();
                    // Generate Category Id...
                    newCatID = getNewCatID( dialogName.getText().toString() );

                    UpdateImages.uploadImageOnFirebaseStorage( context, dialog, catImageUri, catImageView
                            , CURRENT_CITY_CODE + "/HOME/category"
                            , newCatID );
                }else{
                    if (catImageUri == null){
                        DialogsClass.alertDialog( context, null, "Please Add Image First!" ).show();
                    }
                }
            }
        } );

        okBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UpdateImages.uploadImageLink != null && isValidText(dialogName) ){
                    dialog.show();
                    // Add OnDatabase..
                    Map <String, Object> catMap = new HashMap <>();
                    int position =  homePageList.get( layoutIndex ).getBannerAndCatModelList().size() + 1;
//                    catMap.put( "type", CATEGORY_ITEMS_LAYOUT_CONTAINER );
                    catMap.put( "no_of_cat", position );
                    catMap.put( "cat_id_"+position, newCatID );
                    catMap.put( "cat_name_"+position, dialogName.getText().toString()  );
                    catMap.put( "cat_image_"+position, UpdateImages.uploadImageLink );
                    catMap.put( "cat_visibility_"+position, false );

                    DBQuery.setNewCategoryOnDataBase( context, dialog, newCatID,  dialogName.getText().toString(), layoutIndex , catMap );
                    homePageList.get( layoutIndex ).getBannerAndCatModelList().add( new BannerAndCatModel(
                            newCatID, UpdateImages.uploadImageLink, newCatID, CATEGORY_ITEMS_LAYOUT_CONTAINER,  dialogName.getText().toString(), "0"
                    ) );
                }
            }
        } );

        cancelBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(UpdateImages.uploadImageLink != null){
                    showAlertDialogForCat(newCatID, dialogAddCat );
                }else{
                    dialogAddCat.dismiss();
                }
            }
        } );

    }

    private String getNewCatID(String catName){
        catName = catName.toLowerCase().trim();
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


    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult( requestCode, resultCode, data );
        if (requestCode == GALLERY_CODE ){
            if (resultCode == RESULT_OK){
                if (data != null){
                    catImageUri = data.getData();
                    UpdateImages.uploadImageLink = null;
                    Glide.with( context ).load( catImageUri ).into( catImageView );
                }else{
                    showToast(  "Image not Found..!" );
                }
            }
        }
        // Get Response of cropped Image method....
    }

    private void showToast(String msg){
        Toast.makeText( context, msg, Toast.LENGTH_SHORT ).show();
    }

    private void showAlertDialogForCat(final String catID, final Dialog dialogAddCat){
        // Show Warning dialog...
        AlertDialog.Builder builder = DialogsClass.alertDialog( context, "Alert!",  "Do You Want to Cancel adding Layout.?" );
        builder.setCancelable( false );
        builder.setPositiveButton( "YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogA, int which) {
                dialogA.dismiss();
                dialog.show();
                UpdateImages.deleteImageFromFirebase( context, dialog
                        , CURRENT_CITY_CODE + "/HOME/category"
                        , catID  );
                dialogAddCat.dismiss();
            }
        } ).setNegativeButton( "NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogA, int which) {
                dialogA.dismiss();
            }
        } ).show();
    }


}
