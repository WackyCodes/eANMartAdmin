package wackycodes.ecom.eanmartadmin.addnewitem;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import wackycodes.ecom.eanmartadmin.R;
import wackycodes.ecom.eanmartadmin.database.DBQuery;
import wackycodes.ecom.eanmartadmin.mainpage.homesection.BannerAndCatModel;
import wackycodes.ecom.eanmartadmin.other.DialogsClass;

import static wackycodes.ecom.eanmartadmin.database.DBQuery.homePageList;
import static wackycodes.ecom.eanmartadmin.database.DBQuery.storageReference;
import static wackycodes.ecom.eanmartadmin.other.StaticMethods.showToast;
import static wackycodes.ecom.eanmartadmin.other.StaticValues.CURRENT_CITY_CODE;
import static wackycodes.ecom.eanmartadmin.other.StaticValues.GALLERY_CODE;

/**
 * Created by Shailendra (WackyCodes) on 16/08/2020 21:08
 * ( To Know more, Click : https://linktr.ee/wackycodes )
 */
public class AddNewImageActivity extends AppCompatActivity implements View.OnClickListener {
    // This Activity only used for cat Image upload and update...
    // Add New Category...
    private ImageView addNewCatImage;
    private TextView addNewCatAddImgBtn;
    private TextView addNewCatDoneBtn;
    private TextView backBtn;
    private Uri tempCatURI = null;

    private String uploadPath;

    private String layoutID;
    private int categoryIndex;
    private int layoutIndex;

    private BannerAndCatModel catItemModel;

    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_add_new_image );
        dialog = DialogsClass.getDialog( this );

        // Layout Index...
        layoutIndex = getIntent().getIntExtra( "LAYOUT_INDEX", -1 );
        // Category Index...
        categoryIndex = getIntent().getIntExtra( "CAT_INDEX", -1 );

        uploadPath = CURRENT_CITY_CODE + "/HOME/category";

        //-------------------------------------------------------------------------
        // We Need position of Layout...
        // Get Item Model...
        catItemModel = homePageList.get( layoutIndex ).getBannerAndCatModelList().get( categoryIndex );
        // Get Layout ID...
        layoutID = homePageList.get( layoutIndex ).getLayoutID();
        // Add OnDatabase..
        // -------------------------------------------------------------------------

        tempCatURI = null;

        // Add Category...
        addNewCatImage =  findViewById( R.id.add_new_cat_image );
        addNewCatAddImgBtn =  findViewById( R.id.add_new_cat_image_text );
        addNewCatDoneBtn =  findViewById( R.id.add_new_cat_add_text );
        backBtn =  findViewById( R.id.back_text );

        addNewCatAddImgBtn.setOnClickListener( this );
        // Cat Btn...
        backBtn.setOnClickListener( this );
        addNewCatDoneBtn.setOnClickListener( this );

    }

    @Override
    public void onClick(View view) {

        /// Add New Category...
        if (view == addNewCatAddImgBtn){
            // Request...
            Intent galleryIntent = new Intent( Intent.ACTION_PICK );
            galleryIntent.setType( "image/*" );
            startActivityForResult( galleryIntent, GALLERY_CODE );
        }
        else if (view == backBtn){
            onBackPressed();
        }else if (view == addNewCatDoneBtn){
            if ( tempCatURI != null){
                String name = catItemModel.getClickID();
                // Upload And Change on Database...
                uploadImageOnFirebaseStorage( this, tempCatURI, addNewCatImage, uploadPath, name  );
            }else{
                showToast( this, "Please Add Image First!" );
            }

        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
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
                    showToast( this,  "Image not Found..!" );
                }
            }
        }
        // Get Response of cropped Image method....
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap( this.getContentResolver(), resultUri);
                    startCompressImage(bitmap );
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                showToast(  this, error.getMessage() );
            }
        }

    }

    private void startCropImageActivity(Uri imageUri) {
        CropImage.activity(imageUri)
                .setGuidelines( CropImageView.Guidelines.ON)
                .setMultiTouchEnabled(true)
                .start( this );

    }
//    public Uri getImageUri( Bitmap inImage) {
//        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
//        String path = MediaStore.Images.Media.insertImage(this.getContentResolver(), inImage, "Title", null);
////        MediaStore.Images.Media.getContentUri(  );
//        return Uri.parse(path);
//    }

    private void startCompressImage(@NonNull Bitmap bitmap){
//        Bitmap bitmap = ((BitmapDrawable)view.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
//        byte[] imageInByte = stream.toByteArray();
//        long sizeOfImage = imageInByte.length/1024;

        bitmap.compress( Bitmap.CompressFormat.JPEG,35, stream);
        byte[] BYTE = stream.toByteArray();
        Bitmap newBitmap = BitmapFactory.decodeByteArray(BYTE,0,BYTE.length);

        String path = MediaStore.Images.Media.insertImage(this.getContentResolver(), newBitmap, "Title", null);
//        String imgLink = getImageUri(newBitmap).toString();
        tempCatURI = Uri.parse(path);
        Glide.with( this ).load( Uri.parse(path) ).into( addNewCatImage );

    }

    public String uploadImageLink = null;

    private void uploadImageOnFirebaseStorage(final Context context, final Uri imageUri,
                                              final ImageView imageView, final String uploadPath, String fileName){
        dialog.show();
        if (imageUri != null){
            final StorageReference storageRef = storageReference.child( uploadPath + "/" + fileName + ".jpg" );

//            Glide.with( context ).asBitmap().load( imageUri ).optionalCenterCrop().into( new ImageViewTarget <Bitmap>( imageView  ){});

            Glide.with( context ).asBitmap().load( imageUri ).into( new ImageViewTarget <Bitmap>( imageView ){
                @Override
                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition <? super Bitmap> transition) {
                    super.onResourceReady( resource, transition );

//                    final TextView perText = dialog.findViewById( R.id.process_per_complete_text );
//                    perText.setVisibility( View.VISIBLE );

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    resource.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] data = baos.toByteArray();

                    UploadTask uploadTask = storageRef.putBytes(data);
                    uploadTask.addOnCompleteListener( new OnCompleteListener <UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task <UploadTask.TaskSnapshot> task) {
                            if (task.isSuccessful()){
                                // Query To get Download Link...
                                storageRef.getDownloadUrl().addOnCompleteListener( new OnCompleteListener <Uri>() {
                                    @Override
                                    public void onComplete(@NonNull Task <Uri> task) {
                                        if (task.isSuccessful()){
//                                                imageUri = task.getResult();
                                            uploadImageLink = task.getResult().toString();
                                            Glide.with( context ).load( uploadImageLink ).into( imageView );

                                            // Create Map And Update On Database...
                                            Map <String, Object> catMap = new HashMap <>();
                                            categoryIndex = categoryIndex+1; // Cause this is start from 1 .. not 0
//                                            catMap.put( "cat_name_"+categoryIndex, "Category Name..." );
                                            catMap.put( "cat_image_" + categoryIndex, uploadImageLink );
//                                            catMap.put( "cat_visibility_"+categoryIndex, false );
                                            DBQuery.updateCategoryOnDatabase( null, null, layoutID, catMap );

                                            // Update In Local List...
                                            homePageList.get( layoutIndex ).getBannerAndCatModelList().get( categoryIndex ).setImageLink( uploadImageLink );

                                            dialog.dismiss();
                                            finish();
                                        }else{
                                            // Failed Query to getDownload Link...
                                            // TODO : Again Request to Get Download Link Or Query to Delete The Image....
//                                            deleteImageFromFirebase(context, dialog, uploadPath, fileName);
                                            dialog.dismiss();
                                            showToast( context, task.getException().getMessage().toString() );
                                        }
                                    }
                                } );
                            }else{
                                dialog.dismiss();
                            }
                        }
                    } ).addOnFailureListener( new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            dialog.dismiss();
                            showToast( context, "Failed to upload..! " );
                        }
                    } ).addOnProgressListener(new OnProgressListener <UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            int progress = (int)((100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount());
//                            perText.setText("Uploading " + progress + "% completed");
                        }
                    }).addOnPausedListener(new OnPausedListener <UploadTask.TaskSnapshot>() {
                        @Override
                        public void onPaused(UploadTask.TaskSnapshot taskSnapshot) {
//                            perText.setText( "Uploading Pause.! \n Check your net connection.!" );
                        }
                    });
                }
                @Override
                protected void setResource(@Nullable Bitmap resource) {
                    // Set Default image...
//                        profileCircleImage.setImageResource( R.drawable.profile_placeholder );
                }
            } );
        }
        else {
            dialog.dismiss();
            showToast(context, "Please Select Image First...!!" );
        }

    }

}
