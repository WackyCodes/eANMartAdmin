package wackycodes.ecom.eanmartadmin.mainpage.homesection;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.gridlayout.widget.GridLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import wackycodes.ecom.eanmartadmin.R;
import wackycodes.ecom.eanmartadmin.multisection.aboutshop.ShopHomeActivity;
import wackycodes.ecom.eanmartadmin.other.StaticMethods;
import wackycodes.ecom.eanmartadmin.other.UpdateImages;
import wackycodes.ecom.eanmartadmin.mainpage.homesection.categorysection.ShopsViewActivity;
import wackycodes.ecom.eanmartadmin.mainpage.ViewAllActivity;
import wackycodes.ecom.eanmartadmin.other.DialogsClass;
import wackycodes.ecom.eanmartadmin.other.MyImageView;

import static wackycodes.ecom.eanmartadmin.database.DBQuery.firebaseFirestore;
import static wackycodes.ecom.eanmartadmin.other.StaticValues.BANNER_CLICK_TYPE_SHOP;
import static wackycodes.ecom.eanmartadmin.other.StaticValues.BANNER_CLICK_TYPE_WEBSITE;
import static wackycodes.ecom.eanmartadmin.other.StaticValues.BANNER_SLIDER_LAYOUT_CONTAINER;
import static wackycodes.ecom.eanmartadmin.other.StaticValues.CURRENT_CITY_CODE;
import static wackycodes.ecom.eanmartadmin.other.StaticValues.CATEGORY_ITEMS_LAYOUT_CONTAINER;
import static wackycodes.ecom.eanmartadmin.other.StaticValues.STRIP_AD_LAYOUT_CONTAINER;
import static wackycodes.ecom.eanmartadmin.database.DBQuery.homePageList;

public class HomePageAdaptor extends RecyclerView.Adapter {
    String collectionID = "HOME";
    private int catType; // category index
    private String catTitle; // category name or title
    private RecyclerView.RecycledViewPool recycledViewPool;

    public HomePageAdaptor( int catType, String catTitle ) {
        this.catType = catType;
        this.catTitle = catTitle;
        recycledViewPool = new RecyclerView.RecycledViewPool();
    }

    @Override
    public int getItemViewType(int position) {
        switch (homePageList.get( position ).getLayoutType()) {

            case BANNER_SLIDER_LAYOUT_CONTAINER:           //-- 1
                return BANNER_SLIDER_LAYOUT_CONTAINER;
            case STRIP_AD_LAYOUT_CONTAINER:         //-- 2
                return STRIP_AD_LAYOUT_CONTAINER;
            case CATEGORY_ITEMS_LAYOUT_CONTAINER:            //-- 5
                return CATEGORY_ITEMS_LAYOUT_CONTAINER;
            // Add New Items...
            default:
                return -1;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {

            case BANNER_SLIDER_LAYOUT_CONTAINER:
                // TODO : banner Items
                View bannerView = LayoutInflater.from( parent.getContext() ).inflate(
                        R.layout.horizontal_view_layout, parent, false );
                return new BannerSliderViewHolder( bannerView );
            case STRIP_AD_LAYOUT_CONTAINER:
                // TODO : Strip ad Layout
                View stripAdView = LayoutInflater.from( parent.getContext() ).inflate(
                        R.layout.strip_ad_item_layout, parent, false );
                return new StripAdViewHolder( stripAdView );
            case CATEGORY_ITEMS_LAYOUT_CONTAINER:
                // TODO : GridLayout viewHolder
                View gridLayoutView = LayoutInflater.from( parent.getContext() ).inflate(
                        R.layout.grid_view_layout, parent, false );
                return new CategoryViewHolder( gridLayoutView );
//                // Add New Item View...
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        // Layout index num is actually : Position... here...
        switch (homePageList.get( position ).getLayoutType()) {

            case BANNER_SLIDER_LAYOUT_CONTAINER:
                List <BannerAndCatModel> bannerSliderModelList =
                        homePageList.get( position ).getBannerAndCatModelList();
                String sliderLayoutID = homePageList.get( position ).getLayoutID();
                ((BannerSliderViewHolder) holder).setBannerData( sliderLayoutID, bannerSliderModelList, position );
                break;

            case STRIP_AD_LAYOUT_CONTAINER:
                String stripAdImg = homePageList.get( position ).getStripAndBannerAdImg();
                String layoutID = homePageList.get( position ).getLayoutID();
                String clickID = homePageList.get( position ).getStripAndBannerClickID();
                int clickType = homePageList.get( position ).getStripAndBannerClickType();
                String deleteID = homePageList.get( position ).getStripBannerDeleteID();
                ((StripAdViewHolder)holder).setStripAdData( stripAdImg, layoutID, clickID, clickType, deleteID, position );
                break;

            case CATEGORY_ITEMS_LAYOUT_CONTAINER:
                List <BannerAndCatModel> categoryList =
                        homePageList.get( position ).getBannerAndCatModelList();
                String catLayoutID =   homePageList.get( position ).getLayoutID();
                ((CategoryViewHolder)holder).setDataGridLayout( catLayoutID, categoryList,position );
                break;
            default:
                return;
        }

    }

    @Override
    public int getItemCount() {
        return homePageList.size();
    }


    //____________________________ View Holder Class _______________________________________________

    //============  Banner Slider View Holder ============
    public class BannerSliderViewHolder extends RecyclerView.ViewHolder {

        private TextView headTitle;
        private TextView indexNo;
        private Button viewAllBtn;
        private RecyclerView bannerRecycler;
        private TextView warningText;
        private ImageView indexUpBtn;
        private ImageView indexDownBtn;
        private ImageView editLayoutBtn;
        private Switch visibleBtn;
        private int layoutPosition;
        private Dialog dialog;

        public BannerSliderViewHolder(@NonNull View itemView) {
            super( itemView );
            headTitle = itemView.findViewById( R.id.hrViewTotalItemText );
            indexNo = itemView.findViewById( R.id.hrViewIndexText );
            viewAllBtn = itemView.findViewById( R.id.hrViewAllBtn );
            bannerRecycler = itemView.findViewById( R.id.horizontal_view_recycler );
            warningText = itemView.findViewById( R.id.hr_warning_text );
            indexUpBtn = itemView.findViewById( R.id.hrViewUpImgView );
            indexDownBtn = itemView.findViewById( R.id.hrViewDownImgView );
            visibleBtn = itemView.findViewById( R.id.hrViewVisibilitySwitch );
            editLayoutBtn = itemView.findViewById( R.id.edit_layout_imgView );
            dialog = DialogsClass.getDialog( itemView.getContext() );
            visibleBtn.setVisibility( View.INVISIBLE );
        }

        private void setBannerData(final String layoutID, final List<BannerAndCatModel> bannerAndCatModelList, final int index){
            layoutPosition = 1 + index;
            indexNo.setText( "position : "+ layoutPosition);
            headTitle.setText( "Slider Banners " + " (" + bannerAndCatModelList.size() + ")" );
            if (bannerAndCatModelList.size()>=2){
                warningText.setVisibility( View.GONE );
            }else{
                warningText.setVisibility( View.VISIBLE );
                warningText.setText( "Add 2 or more banner to visible this layout to the customers!!" );
            }
            BannerItemAdaptor bannerItemAdaptor = new BannerItemAdaptor( collectionID,0 , bannerAndCatModelList , false, index );
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager( itemView.getContext() );
            linearLayoutManager.setOrientation( RecyclerView.HORIZONTAL );
            bannerRecycler.setLayoutManager( linearLayoutManager );
            bannerRecycler.setAdapter( bannerItemAdaptor );
            bannerItemAdaptor.notifyDataSetChanged();

            if (bannerAndCatModelList.size() > 0){
                viewAllBtn.setVisibility( View.VISIBLE );
            }else {
                viewAllBtn.setVisibility( View.INVISIBLE );
            }
            viewAllBtn.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ViewAllActivity.viewAllList = bannerAndCatModelList;
                    Intent viewAllIntent = new Intent( itemView.getContext(), ViewAllActivity.class);
                    viewAllIntent.putExtra( "TYPE", BANNER_SLIDER_LAYOUT_CONTAINER );
                    viewAllIntent.putExtra( "CAT_COLL_ID", collectionID );
                    viewAllIntent.putExtra( "CAT_INDEX", catType );
                    viewAllIntent.putExtra( "LAY_INDEX", index );
                    itemView.getContext().startActivity( viewAllIntent );
                }
            } );

            editLayoutBtn.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // delete layout
                    if (bannerAndCatModelList.size() <= 1){
                        if (bannerAndCatModelList.size() == 0)
                            alertDialog( itemView.getContext(), index, layoutID, null,null  );
                        else
                            alertDialog( itemView.getContext(), index, layoutID, "/HOME/banners", bannerAndCatModelList.get( 0 ).getName()  );
                    }else{
                        showToast( "You have more than 1 banner in this layout!", itemView.getContext() );
                    }

                }
            } );

            // -------  Update Layout...
            setIndexUpDownVisibility( index, indexUpBtn, indexDownBtn ); // set Up and Down Btn Visibility...
            indexUpBtn.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateIndex(true, index, dialog );
                }
            } );
            indexDownBtn.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateIndex(false, index, dialog );
                }
            } );

            visibleBtn.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if( isChecked ){
                        // Code to Show TabView...
                        String layoutId = homePageList.get( index ).getLayoutID();
                        Map <String, Object> updateMap = new HashMap <>();
                        updateMap.put( "is_visible", true );
                        dialog.show();
                        updateOnDocument(dialog, layoutId, updateMap);
                    }else{
                        // Hide the TabView...
                        String layoutId = homePageList.get( index ).getLayoutID();
                        Map <String, Object> updateMap = new HashMap <>();
                        updateMap.put( "is_visible", false );
                        dialog.show();
                        updateOnDocument(dialog, layoutId, updateMap);
                    }
                }
            } );

        }

    }
    //============  Banner Slider View Holder ============

    //============  Strip ad  View Holder ============
    public class StripAdViewHolder extends RecyclerView.ViewHolder{
        private MyImageView stripAdImage;
        private TextView indexNo;
        private ImageView editLayoutBtn;
        private int defaultColor;
        private int layoutPosition;
        private ImageView indexUpBtn;
        private ImageView indexDownBtn;
        private Switch visibleBtn;
        private Dialog dialog;

        public StripAdViewHolder(@NonNull View itemView) {
            super( itemView );
            stripAdImage = itemView.findViewById( R.id.strip_ad_image );
            indexNo = itemView.findViewById( R.id.strip_ad_index );
            editLayoutBtn = itemView.findViewById( R.id.edit_layout_imgView );
            indexUpBtn = itemView.findViewById( R.id.hrViewUpImgView );
            indexDownBtn = itemView.findViewById( R.id.hrViewDownImgView );
            visibleBtn = itemView.findViewById( R.id.hrViewVisibilitySwitch );
            defaultColor = ContextCompat.getColor( itemView.getContext(), R.color.colorGray);
            dialog = DialogsClass.getDialog( itemView.getContext() );
            visibleBtn.setVisibility( View.INVISIBLE );
        }
        private void setStripAdData(String imgLink, final String layoutID, final String clickID, final int clickType, final String deleteID, final int index){
            layoutPosition = 1 + index;
            indexNo.setText( "Ad Banner position : " + layoutPosition );
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                stripAdImage.setBackgroundTintList( ColorStateList.valueOf( Color.parseColor( colorCode ) ));
//            }
//            stripAdImage.setImageResource( R.drawable.strip_ad_a );
            // set Image Resouice from database..
            Glide.with( itemView.getContext() ).load( imgLink )
                    .apply( new RequestOptions().placeholder( R.drawable.ic_panorama_black_24dp ) ).into( stripAdImage );

            editLayoutBtn.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Delete...
                    alertDialog( itemView.getContext(), index, layoutID, "/HOME/ads",  deleteID );
                }
            } );

            itemView.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    showToast( "Code Not Found!", itemView.getContext() );
                    switch ( clickType ){
                        case BANNER_CLICK_TYPE_WEBSITE:
                            StaticMethods.gotoURL( itemView.getContext(), clickID );
                            break;
                        case BANNER_CLICK_TYPE_SHOP:
                            Intent intent = new Intent( itemView.getContext(), ShopHomeActivity.class );
                            intent.putExtra( "SHOP_ID", clickID );
                            itemView.getContext().startActivity( intent );
                            break;
                    }
                }
            } );

            // -------  Update Layout...
            setIndexUpDownVisibility( index, indexUpBtn, indexDownBtn ); // set Up and Down Btn Visibility...
            indexUpBtn.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateIndex(true, index, dialog );
                }
            } );
            indexDownBtn.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateIndex(false, index, dialog );
                }
            } );
            visibleBtn.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if( isChecked ){
                        // Code to Show TabView...
                        String layoutId = homePageList.get( index ).getLayoutID();
                        Map <String, Object> updateMap = new HashMap <>();
                        updateMap.put( "is_visible", true );
                        dialog.show();
                        updateOnDocument(dialog, layoutId, updateMap);
                    }else{
                        // Hide the TabView...
                        String layoutId = homePageList.get( index ).getLayoutID();
                        Map <String, Object> updateMap = new HashMap <>();
                        updateMap.put( "is_visible", false );
                        dialog.show();
                        updateOnDocument(dialog, layoutId, updateMap);
                    }
                }
            } );

        }

    }
    //============  Strip ad  View Holder ============

   //==============  GridProduct Grid Layout View Holder =================
    public class CategoryViewHolder extends  RecyclerView.ViewHolder{
        private GridLayout gridLayout;
        private TextView gridLayoutTitle;
        private TextView indexNo;
        private Button gridLayoutViewAllBtn;
        private TextView warningText;
        private ImageView indexUpBtn;
        private ImageView indexDownBtn;
        private ImageView editLayoutBtn;
        private Switch visibleBtn;
        private int temp = 0;
        private Dialog dialog;
        private int layoutPosition;

        // Layout///
        public CategoryViewHolder(@NonNull View itemView) {
            super( itemView );
            gridLayout = itemView.findViewById( R.id.product_grid_layout );
            gridLayoutTitle = itemView.findViewById( R.id.gridLayoutTitle );
            indexNo = itemView.findViewById( R.id.gridIndexNo );
            gridLayoutViewAllBtn = itemView.findViewById( R.id.gridViewAllBtn );
            warningText = itemView.findViewById( R.id.grid_warning_text );
            indexUpBtn = itemView.findViewById( R.id.hrViewUpImgView );
            indexDownBtn = itemView.findViewById( R.id.hrViewDownImgView );
            visibleBtn = itemView.findViewById( R.id.hrViewVisibilitySwitch );
            editLayoutBtn = itemView.findViewById( R.id.edit_layout_imgView );
            dialog = DialogsClass.getDialog( itemView.getContext() );
            visibleBtn.setVisibility( View.INVISIBLE );
        }

        private void setDataGridLayout(final String layoutID, final List<BannerAndCatModel> categoryList , final int index){
            layoutPosition = 1 + index;
            indexNo.setText( "position : " + layoutPosition );
            gridLayoutTitle.setText( "Total Cat : " + " (" + categoryList.size() + ")" );
            warningText.setVisibility( View.GONE );
            int gridRange;

            if (categoryList.size()>3){
                gridRange = 3;
            }else{
                gridRange = categoryList.size();
            }

            for (int i = 0; i < gridRange; i++ ){
                BannerAndCatModel categoryModel = categoryList.get( i );
                LinearLayout itemLayout = gridLayout.getChildAt( i ).findViewById( R.id.cat_item );
                gridLayout.getChildAt( i ).findViewById( R.id.add_new_cat_item ).setVisibility( View.GONE );
                itemLayout.setVisibility( View.VISIBLE );
                // Set Update Visibility Gone For Home....
                gridLayout.getChildAt( i ).findViewById( R.id.update_category_layout ).setVisibility( View.GONE );
                gridLayout.getChildAt( i ).findViewById( R.id.visibility_layout ).setVisibility( View.GONE );

                ImageView img = gridLayout.getChildAt( i ).findViewById( R.id.cat_image );
                TextView name = gridLayout.getChildAt( i ).findViewById( R.id.cat_name );

                Glide.with( itemView.getContext() ).load( categoryModel.getImageLink()  )
                        .apply( new RequestOptions().placeholder( R.drawable.ic_photo_black_24dp ) ).into( img );
                name.setText( categoryModel.getName() );

                // ClickListener...
                itemLayout.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if ( v == gridLayout.getChildAt( 0 ).findViewById( R.id.cat_item )){
                            onCatClick(categoryList.get( 0 ).getName(), categoryList.get( 0 ).getClickID());
                        } else
                        if ( v == gridLayout.getChildAt( 1 ).findViewById( R.id.cat_item )){
                            onCatClick(categoryList.get( 1 ).getName(), categoryList.get( 1 ).getClickID());
                        } else
                        if ( v == gridLayout.getChildAt( 2 ).findViewById( R.id.cat_item )){
                            onCatClick(categoryList.get( 2 ).getName(), categoryList.get( 2 ).getClickID());
                        }
                    }
                } );
            }
            // Add new Product in Grid Layout...
            for (int k = 0; k < 4; k++ ) {
                LinearLayout itemLayout = gridLayout.getChildAt( k ).findViewById( R.id.cat_item );
                LinearLayout addNewItemLayout = gridLayout.getChildAt( k ).findViewById( R.id.add_new_cat_item );
                if ( k < gridRange ){
                    itemLayout.setVisibility( View.VISIBLE );
                    addNewItemLayout.setVisibility( View.GONE );
                }else{
                    itemLayout.setVisibility( View.GONE );
                    addNewItemLayout.setVisibility( View.VISIBLE );
                }

                addNewItemLayout.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if ( v == gridLayout.getChildAt( 0 ).findViewById( R.id.add_new_cat_item )){
                            addNewItem( index, 0 );
                        } else
                        if ( v == gridLayout.getChildAt( 1 ).findViewById( R.id.add_new_cat_item )){
                            addNewItem( index, 1 );
                        } else
                        if ( v == gridLayout.getChildAt( 2 ).findViewById( R.id.add_new_cat_item )){
                            addNewItem( index, 2 );
                        } else
                        if ( v == gridLayout.getChildAt( 3 ).findViewById( R.id.add_new_cat_item )){
                            addNewItem( index, 3 );
                        }
                    }
                } );

            }

            gridLayoutViewAllBtn.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ViewAllActivity.viewAllList = categoryList;
                    Intent viewAllIntent = new Intent( itemView.getContext(), ViewAllActivity.class);
                    viewAllIntent.putExtra( "TYPE", CATEGORY_ITEMS_LAYOUT_CONTAINER );
                    itemView.getContext().startActivity( viewAllIntent );
                }
            } );

            // -------  Update Layout...
            setIndexUpDownVisibility( index, indexUpBtn, indexDownBtn ); // set Up and Down Btn Visibility...
            indexUpBtn.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateIndex(true, index, dialog );
                }
            } );
            indexDownBtn.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateIndex(false, index, dialog );
                }
            } );

        }
        private void onCatClick(String catName, String catID){
            Intent intent = new Intent( itemView.getContext(), ShopsViewActivity.class );
            intent.putExtra( "CAT_ID", catID );
            intent.putExtra( "CAT_NAME", catName );
            itemView.getContext().startActivity( intent );
        }

       private void addNewItem(final int layoutIndex, int listIndex){
            SecondActivity.layoutIndex = layoutIndex;
            SecondActivity.setAddCatLayoutVisibility( true );
       }

   }
    //==============  GridProduct Grid Layout View Holder =================

    //                            _________________________________________
    //___________________________ Others Methods to Updates the Layouts ____________________________

    private void setIndexUpDownVisibility( int index, ImageView indexUpBtn,  ImageView indexDownBtn){
        if (homePageList.size()>1){
            indexUpBtn.setVisibility( View.VISIBLE );
            indexDownBtn.setVisibility( View.VISIBLE );
            if (index == 0){
                indexUpBtn.setVisibility( View.INVISIBLE );
            }else if (index == homePageList.size()-1){
                indexDownBtn.setVisibility( View.INVISIBLE );
            }
        }else{
            indexUpBtn.setVisibility( View.INVISIBLE );
            indexDownBtn.setVisibility( View.INVISIBLE );
        }
    }
    private void updateIndex(boolean isMoveUp, int index, Dialog dialog){
        dialog.show();
        if (isMoveUp){
            // GoTo Up...
            updateIndexOnDatabase(index, (index - 1), dialog );
        }else{
            // Goto Down..
            updateIndexOnDatabase(index, (index + 1), dialog );
        }
    }
    private void updateIndexOnDatabase(final int startInd, final int endInd, final Dialog dialog){
        String[] layoutId = new String[]{ homePageList.get( startInd ).getLayoutID(), homePageList.get( endInd ).getLayoutID() };

        for ( String tempId : layoutId){
            if (!dialog.isShowing()){
                dialog.show();
            }
            Map <String, Object> updateMap = new HashMap <>();
            updateMap.clear();
            if (tempId.equals( layoutId[0] )){
                updateMap.put( "index", ( endInd ) );
            }else{
                updateMap.put( "index", ( startInd ) );
            }
            updateOnDocument(dialog, tempId, updateMap);
        }
        Collections.swap( homePageList, startInd, endInd );
        SecondActivity.homePageAdaptor.notifyDataSetChanged();

    }
    private void updateOnDocument(final Dialog dialog, String layoutId, Map <String, Object> updateMap){
        firebaseFirestore.collection( "HOME_PAGE" ).document( CURRENT_CITY_CODE )
                .collection( collectionID ).document( layoutId ).update( updateMap )
                .addOnCompleteListener( new OnCompleteListener <Void>() {
                    @Override
                    public void onComplete(@NonNull Task <Void> task) {
                        dialog.dismiss();
                    }
                } );
    }

    private void showToast(String msg, Context context){
        Toast.makeText( context, msg, Toast.LENGTH_SHORT ).show();
    }
    private void alertDialog(final Context context, final int index, final String layoutId, @Nullable final String deletePath, @Nullable final String deleteID){
        AlertDialog.Builder alertD = new AlertDialog.Builder( context );
        alertD.setTitle( "Do You want to delete this Layout.?" );
        alertD.setMessage( "If you delete this layout, you will loose all the inside data of the layout.!" );
        alertD.setCancelable( false );
        alertD.setPositiveButton( "DELETE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                final Dialog pDialog = DialogsClass.getDialog( context );
                pDialog.show();
                // Query to delete the Layout...!
                firebaseFirestore.collection( "HOME_PAGE" ).document( CURRENT_CITY_CODE )
                        .collection( collectionID ).document( layoutId ).delete()
                        .addOnCompleteListener( new OnCompleteListener <Void>() {
                            @Override
                            public void onComplete(@NonNull Task <Void> task) {
                                if (task.isSuccessful()){
                                    showToast( "Deleted Layout Successfully.!", context );
                                    // : Update in local list..!
                                    homePageList.remove( index );
                                    SecondActivity.homePageAdaptor.notifyDataSetChanged();

                                    // Delete Process...
                                    if (deleteID != null){
                                        UpdateImages.deleteImageFromFirebase( context, null
                                                , CURRENT_CITY_CODE + deletePath
                                                , deleteID  );
                                    }
                                }else {
                                    showToast( "Failed.! Something went wrong.!", context );
                                }
                                pDialog.dismiss();
                            }
                        } );

            }
        } );
        alertD.setNegativeButton( "CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        } );
        alertD.show();
    }

    // Add Category...




}

// WackyCodes - (Shailendra Lodhi) ... //

