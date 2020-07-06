package wackycodes.ecom.eanmartadmin.homepage;


import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import wackycodes.ecom.eanmartadmin.R;
import wackycodes.ecom.eanmartadmin.other.CheckInternetConnection;
import wackycodes.ecom.eanmartadmin.other.DialogsClass;
import wackycodes.ecom.eanmartadmin.other.StaticValues;

public class MainFragment extends Fragment {

    // Image Variables...
    private Uri imageUri = null;

    // Getting Reference of CheckInternetConnection
    CheckInternetConnection checkInternetCON = new CheckInternetConnection();


    public static RecyclerView mainRecycler;
//    public static List<BannerAndCatModel> catList = new ArrayList <>();



    private FrameLayout mainFrameLayout;
    DialogsClass dialogsClass = new DialogsClass();

    public static SwipeRefreshLayout homeSwipeRefreshLayout;



    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate( R.layout.fragment_main, container, false );

        mainFrameLayout = view.findViewById( R.id.main_fragment_frameLayout );

        // Refresh Progress...
        homeSwipeRefreshLayout = view.findViewById( R.id.home_swipe_refresh_layout );
        homeSwipeRefreshLayout.setColorSchemeColors( getContext().getResources().getColor( R.color.colorPrimary ),
                getContext().getResources().getColor( R.color.colorPrimary ),
                getContext().getResources().getColor( R.color.colorPrimary ));
        // Refresh Progress...


        // Linear Layout...
        LinearLayoutManager homeCatLayoutManager = new LinearLayoutManager( getContext() );
        homeCatLayoutManager.setOrientation( RecyclerView.VERTICAL );
        mainRecycler.setLayoutManager( homeCatLayoutManager );


       /** ///////////////////////////////////////////////////////////
        // Code to add new product item....
        catImage.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProfileImage();
                // Code to add Icon image...
                changeCatIcon.setVisibility( View.VISIBLE );
                uploadCatIcon.setVisibility( View.VISIBLE );
            }
        } );
        changeCatIcon.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Change Icon...
                uploadCatIcon.setEnabled( true );
                addProfileImage();
            }
        } );
        uploadCatIcon.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Upload task...
                if (imageUri != null && ! TextUtils.isEmpty( catTitleText.getText().toString()) ){
                    Dialog perDialog = dialogsClass.progressPerDialog( getContext() );
                    perDialog.show();
//                    String uploadPath = "cat_icon/name_of_image.jpg";
                    uploadCatIcon.setEnabled( false );
                    UpdateImages.uploadImageOnFirebaseStorage( getContext(), perDialog, imageUri, catImage,
                            "cat_icon",catTitleText.getText().toString() );
                }else {
                    if (imageUri == null ){
                        showToast( "Please select Image first.!" );
                    }else{
                        showToast( "Please fill required field.!" );
                    }
                }
            }
        } );
        catOKBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call the action perform methods...
                String uploadLink = UpdateImages.uploadImageLink;
                if ( ( uploadLink != null ) && ! TextUtils.isEmpty( catTitleText.getText().toString() )){
                    // Update on database...
                    dialog.show();
                    updateImageLinkOnDatabase( dialog, uploadLink, catTitleText.getText().toString(), homeCategoryIconList.size() );
                }else{
                    // Show Error...
                    if (TextUtils.isEmpty( catTitleText.getText().toString() )){
                        catTitleText.setError( "Please fill information.!" );
                    }
                    showToast( "Please upload image or fill required information first." );
                }
            }
        } );
        catCancelBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // check condition...
                String uploadLink = UpdateImages.uploadImageLink;
                if ( uploadLink != null ){
                    Dialog dialog = dialogsClass.progressDialog( getContext() );
                    dialog.show();
                    UpdateImages.deleteImageFromFirebase( getContext(), dialog, "cat_icon", catTitleText.getText().toString());
                }
                addNewCatItemRelativeLayout.setVisibility( View.GONE );
                mainRecycler.setVisibility( View.VISIBLE );
                changeCatIcon.setVisibility( View.GONE );
                uploadCatIcon.setVisibility( View.GONE );
                // clear catch...
                UpdateImages.uploadImageLink = null;
                imageUri = null;
                catImage.setImageResource( R.drawable.ic_add_black_24dp );
                catTitleText.setText( "" );
            }
        } ); */
        ////////////////////////////////////////////////////////////////

        // ----= Refresh Layout... check is Null.?
        if (homeSwipeRefreshLayout != null)
            homeSwipeRefreshLayout.setOnRefreshListener( new SwipeRefreshLayout.OnRefreshListener(){

                @Override
                public void onRefresh() {
                    homeSwipeRefreshLayout.setRefreshing( true );

//                    if (!checkInternetCON.checkInternet( getContext() )){
////                        homeCategoryIconList.clear();
////                        commonCatList.clear();
////                        mainRecycler.setAdapter( fakeAdaptor );
////                        dialog.show();
////                        DBquery.getQueryCategoryIcon(dialog, mainRecycler);
////                    }else{
////                        MainFragment.homeSwipeRefreshLayout.setRefreshing( false );
////                    }

                }
            });
        // ----= Refresh Layout...

        return view;
    }

    @Override
    public void onDestroyView() {
        mainFrameLayout.removeAllViews();
        super.onDestroyView();
    }

    // Fragment Transaction...
    public void setFragment( Fragment showFragment ){
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
//        fragmentTransaction.setCustomAnimations( R.anim.slide_from_right, R.anim.slide_out_from_left );
        onDestroyView();
        fragmentTransaction.replace( mainFrameLayout.getId(),showFragment );
        fragmentTransaction.commit();
    }



    private void dialogAddCategory(Context context){

        final Dialog dialog = new Dialog( context );
        dialog.requestWindowFeature( Window.FEATURE_NO_TITLE );
        dialog.setContentView( R.layout.dialog_message_ok_layout );
        dialog.getWindow().setLayout( ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT );
        dialog.setCancelable( false );
        Button okBtn = dialog.findViewById( R.id.dialog_ok_btn );
        Button cancelBtn = dialog.findViewById( R.id.dialog_cancel_btn );
        ImageView addImage = dialog.findViewById( R.id.dialog_add_image );
        TextView changeImage = dialog.findViewById( R.id.dialog_change_image );
        TextView catName = dialog.findViewById( R.id.dialog_name );
        TextView uploadImage = dialog.findViewById( R.id.dialog_upload_image );

        cancelBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                // Check if image upload...?
            }
        } );

        addImage.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Add Image...
            }
        } );
        changeImage.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Change Image...
            }
        } );
        uploadImage.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Upload Image...
            }
        } );
        okBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Add Category...
            }
        } );


    }



}
