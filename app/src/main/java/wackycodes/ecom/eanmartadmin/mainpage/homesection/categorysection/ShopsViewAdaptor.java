package wackycodes.ecom.eanmartadmin.mainpage.homesection.categorysection;

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
import wackycodes.ecom.eanmartadmin.mainpage.ViewAllActivity;
import wackycodes.ecom.eanmartadmin.multisection.aboutshop.ShopHomeActivity;
import wackycodes.ecom.eanmartadmin.other.DialogsClass;
import wackycodes.ecom.eanmartadmin.other.MyImageView;
import wackycodes.ecom.eanmartadmin.other.StaticMethods;
import wackycodes.ecom.eanmartadmin.other.UpdateImages;
import wackycodes.ecom.eanmartadmin.mainpage.homesection.BannerAndCatModel;
import wackycodes.ecom.eanmartadmin.mainpage.homesection.BannerItemAdaptor;
import wackycodes.ecom.eanmartadmin.mainpage.homesection.HomeListModel;
import wackycodes.ecom.eanmartadmin.mainpage.homesection.SecondActivity;

import static wackycodes.ecom.eanmartadmin.database.DBQuery.categoryList;
import static wackycodes.ecom.eanmartadmin.database.DBQuery.firebaseFirestore;
import static wackycodes.ecom.eanmartadmin.other.StaticValues.BANNER_CLICK_TYPE_SHOP;
import static wackycodes.ecom.eanmartadmin.other.StaticValues.BANNER_CLICK_TYPE_WEBSITE;
import static wackycodes.ecom.eanmartadmin.other.StaticValues.BANNER_SLIDER_LAYOUT_CONTAINER;
import static wackycodes.ecom.eanmartadmin.other.StaticValues.CURRENT_CITY_CODE;
import static wackycodes.ecom.eanmartadmin.other.StaticValues.SHOP_ITEMS_LAYOUT_CONTAINER;
import static wackycodes.ecom.eanmartadmin.other.StaticValues.STRIP_AD_LAYOUT_CONTAINER;

public class ShopsViewAdaptor extends RecyclerView.Adapter  {
    private int catIndex; // category index
    private String catTitle; // category name or title
    private RecyclerView.RecycledViewPool recycledViewPool;
    private List <HomeListModel> homePageList;
    private String collectionID;

    public ShopsViewAdaptor(List<HomeListModel> homePageList, int catIndex, String catTitle, String catID ) {
        this.homePageList = homePageList;
        this.catIndex = catIndex;
        this.catTitle = catTitle;
        collectionID = catID;
        recycledViewPool = new RecyclerView.RecycledViewPool();
    }

    @Override
    public int getItemViewType(int position) {
        switch (homePageList.get( position ).getLayoutType()) {

            case BANNER_SLIDER_LAYOUT_CONTAINER:
                return BANNER_SLIDER_LAYOUT_CONTAINER;
            case STRIP_AD_LAYOUT_CONTAINER:
                return STRIP_AD_LAYOUT_CONTAINER;
            case SHOP_ITEMS_LAYOUT_CONTAINER:
                return SHOP_ITEMS_LAYOUT_CONTAINER;
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
            case SHOP_ITEMS_LAYOUT_CONTAINER:
                // TODO : GridLayout viewHolder
                View gridLayoutView = LayoutInflater.from( parent.getContext() ).inflate(
                        R.layout.grid_view_layout, parent, false );
                return new ShopsViewHolder( gridLayoutView );
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
                ((StripAdViewHolder)holder).setStripAdData( stripAdImg, layoutID, clickID, clickType, position, deleteID );
                break;

            case SHOP_ITEMS_LAYOUT_CONTAINER:
                List <BannerAndCatModel> categoryList =
                        homePageList.get( position ).getBannerAndCatModelList();
                String catLayoutID =   homePageList.get( position ).getLayoutID();
                ((ShopsViewHolder)holder).setDataGridLayout( catLayoutID, categoryList,position );
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
        private void setBannerData( final String layoutID, final List<BannerAndCatModel> bannerAndCatModelList, final int index){
            layoutPosition = 1 + index;
            indexNo.setText( "position : "+ layoutPosition);
            headTitle.setText( "Slider Banners " + " (" + bannerAndCatModelList.size() + ")" );
            if (bannerAndCatModelList.size()>=2){
                warningText.setVisibility( View.GONE );
            }else{
                warningText.setVisibility( View.VISIBLE );
                warningText.setText( "Add 2 or more banner to visible this layout to the customers!!" );
            }
            BannerItemAdaptor bannerItemAdaptor = new BannerItemAdaptor( collectionID, catIndex, bannerAndCatModelList , false, index );
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager( itemView.getContext() );
            linearLayoutManager.setOrientation( RecyclerView.HORIZONTAL );
            bannerRecycler.setLayoutManager( linearLayoutManager );
            bannerRecycler.setAdapter( bannerItemAdaptor );
            bannerItemAdaptor.notifyDataSetChanged();

            viewAllBtn.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ViewAllActivity.viewAllList = bannerAndCatModelList;
                    Intent viewAllIntent = new Intent( itemView.getContext(), ViewAllActivity.class);
                    viewAllIntent.putExtra( "TYPE", BANNER_SLIDER_LAYOUT_CONTAINER );
                    viewAllIntent.putExtra( "CAT_COLL_ID", collectionID );
                    viewAllIntent.putExtra( "CAT_INDEX", catIndex );
                    viewAllIntent.putExtra( "LAY_INDEX", index );
                    itemView.getContext().startActivity( viewAllIntent );
//                    showToast( "Code Not Found", itemView.getContext() );
                }
            } );

            editLayoutBtn.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // delete..
//                    showToast( "Code Not Found", itemView.getContext() );
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
        private void setStripAdData(String imgLink, final String layoutID, final String clickID, final int clickType, final int index, final String deleteID){
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
                    alertDialog( itemView.getContext(), index, layoutID, "/HOME/ads", deleteID );
                    // Delete...
//                    showToast( "Code Not Found!", itemView.getContext() );
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
    public class ShopsViewHolder extends  RecyclerView.ViewHolder{
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
        public ShopsViewHolder(@NonNull View itemView) {
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
            final int gridRange  = categoryList.size();

            for (int i = 0; i < gridRange && i < 4; i++ ){
                BannerAndCatModel categoryModel = categoryList.get( i );

                ImageView img = gridLayout.getChildAt( i ).findViewById( R.id.cat_image );
                TextView name = gridLayout.getChildAt( i ).findViewById( R.id.cat_name );

                Glide.with( itemView.getContext() ).load( categoryModel.getImageLink()  )
                        .apply( new RequestOptions().placeholder( R.drawable.ic_photo_black_24dp ) ).into( img );
                name.setText( categoryModel.getName() );

            }
            for (int i = 0; i < 4; i++ ){
                gridLayout.getChildAt( i ).findViewById( R.id.add_new_cat_item ).setVisibility( View.GONE );

                LinearLayout itemLayout = gridLayout.getChildAt( i ).findViewById( R.id.cat_item );
                itemLayout.setVisibility( View.VISIBLE );
                // ClickListener...
                if ( gridRange <= i){
                    ((TextView)gridLayout.getChildAt( i ).findViewById( R.id.cat_name )).setText( "Not Found!" );
                }
                itemLayout.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if ( v == gridLayout.getChildAt( 0 ).findViewById( R.id.cat_item )){
                            // TODO :
                            if(gridRange>=1){
                                setOnClick(  categoryList.get( 0 ).getClickID(), index);
                            }
                        } else
                        if ( v == gridLayout.getChildAt( 1 ).findViewById( R.id.cat_item )){
                            // TODO :
                            if(gridRange>=2){
                                setOnClick(  categoryList.get( 1 ).getClickID(), index);
                            }
                        } else
                        if ( v == gridLayout.getChildAt( 2 ).findViewById( R.id.cat_item )){
                            // TODO :
                            if(gridRange>=3){
                                setOnClick(  categoryList.get( 2 ).getClickID(), index);
                            }
                        }else
                        if ( v == gridLayout.getChildAt( 3 ).findViewById( R.id.cat_item )){
                            // TODO :
                            if(gridRange>=4){
                                setOnClick(  categoryList.get( 3 ).getClickID(), index);
                            }
                        }
                    }
                } );
            }

            gridLayoutViewAllBtn.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ViewAllActivity.viewAllList = categoryList;
                    Intent viewAllIntent = new Intent( itemView.getContext(), ViewAllActivity.class);
                    viewAllIntent.putExtra( "TYPE",SHOP_ITEMS_LAYOUT_CONTAINER );
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

        private void setOnClick( String clickID, int index){
            Intent intent = new Intent( itemView.getContext(), ShopHomeActivity.class );
            intent.putExtra( "SHOP_ID", clickID );
            intent.putExtra( "LAY_INDEX", index );
            itemView.getContext().startActivity( intent );
        }

    }
    //==============  GridProduct Grid Layout View Holder =================
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
//        SecondActivity.homePageAdaptor.notifyDataSetChanged();

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
        alertD.setPositiveButton( "OK", new DialogInterface.OnClickListener() {
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
//                                    homePageList.remove( index );
                                    categoryList.get( catIndex ).remove( index ); // To remove From Cat Home List...
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
        alertD.setNegativeButton( "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        } );
        alertD.show();
    }


}
