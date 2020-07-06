package wackycodes.ecom.eanmartadmin.category;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
import wackycodes.ecom.eanmartadmin.other.DialogsClass;
import wackycodes.ecom.eanmartadmin.secondpage.BannerAndCatModel;
import wackycodes.ecom.eanmartadmin.secondpage.BannerItemAdaptor;
import wackycodes.ecom.eanmartadmin.secondpage.HomeListModel;
import wackycodes.ecom.eanmartadmin.secondpage.SecondActivity;

import static wackycodes.ecom.eanmartadmin.database.DBQuery.firebaseFirestore;
import static wackycodes.ecom.eanmartadmin.other.StaticValues.BANNER_SLIDER_LAYOUT_CONTAINER;
import static wackycodes.ecom.eanmartadmin.other.StaticValues.CURRENT_CITY_CODE;
import static wackycodes.ecom.eanmartadmin.other.StaticValues.SHOP_ITEMS_LAYOUT_CONTAINER;
import static wackycodes.ecom.eanmartadmin.other.StaticValues.STRIP_AD_LAYOUT_CONTAINER;

public class ShopsViewAdaptor extends RecyclerView.Adapter  {
    private int catType; // category index
    private String catTitle; // category name or title
    private RecyclerView.RecycledViewPool recycledViewPool;
    private List <HomeListModel> homePageList;
    private String collectionID;

    public ShopsViewAdaptor(  List<HomeListModel> homePageList, int catType, String catTitle, String catID ) {
        this.homePageList = homePageList;
        this.catType = catType;
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
                ((BannerSliderViewHolder) holder).setBannerData( bannerSliderModelList, position );
                break;

            case STRIP_AD_LAYOUT_CONTAINER:
                String stripAdImg = homePageList.get( position ).getStripAndBannerAdImg();
                String layoutID = homePageList.get( position ).getLayoutID();
                String clickID = homePageList.get( position ).getStripAndBannerClickID();
                int clickType = homePageList.get( position ).getStripAndBannerClickType();
                ((StripAdViewHolder)holder).setStripAdData( stripAdImg, layoutID, clickID, clickType, position );
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
        return 0;
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
        private void setBannerData(final List<BannerAndCatModel> bannerAndCatModelList, final int index){
            layoutPosition = 1 + index;
            indexNo.setText( "position : "+ layoutPosition);
            headTitle.setText( "Total banners " + " (" + bannerAndCatModelList.size() + ")" );
            if (bannerAndCatModelList.size()>=2){
                warningText.setVisibility( View.GONE );
            }else{
                warningText.setVisibility( View.VISIBLE );
                warningText.setText( "Add 2 or more banner to visible this layout to the customers!!" );
            }
            BannerItemAdaptor bannerItemAdaptor = new BannerItemAdaptor( collectionID, catType, bannerAndCatModelList , false, index );
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager( itemView.getContext() );
            linearLayoutManager.setOrientation( RecyclerView.HORIZONTAL );
            bannerRecycler.setLayoutManager( linearLayoutManager );
            bannerRecycler.setAdapter( bannerItemAdaptor );
            bannerItemAdaptor.notifyDataSetChanged();

            viewAllBtn.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    ViewAllActivity.bannerSliderListViewAll = bannerAndCatModelList;
//                    Intent viewAllIntent = new Intent( itemView.getContext(), ViewAllActivity.class);
//                    viewAllIntent.putExtra( "LAYOUT_CODE", VIEW_ALL_FOR_BANNER_PRODUCTS );
//                    viewAllIntent.putExtra( "CAT_INDEX", catType );
//                    viewAllIntent.putExtra( "CAT_TITLE", catTitle );
//                    viewAllIntent.putExtra( "LIST_INDEX", index );
//                    viewAllIntent.putExtra( "TITLE", layoutTitle );
//                    itemView.getContext().startActivity( viewAllIntent );
                    showToast( "Code Not Found", itemView.getContext() );
                }
            } );

            editLayoutBtn.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // delete..
                    showToast( "Code Not Found", itemView.getContext() );
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
        private ImageView stripAdImage;
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
        private void setStripAdData(String imgLink, String layoutID, String clickID, int clickType, final int index){
            layoutPosition = 1 + index;
            indexNo.setText( "position : " + layoutPosition );
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
                    PopupWindow popupWindowDogs;
                    // Delete...
                    showToast( "Code Not Found!", itemView.getContext() );
                }
            } );

            itemView.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showToast( "Code Not Found!", itemView.getContext() );
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

        private void setDataGridLayout( final String layoutID, List<BannerAndCatModel> categoryList ,final int index){
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

                ImageView img = gridLayout.getChildAt( i ).findViewById( R.id.cat_image );
                TextView name = gridLayout.getChildAt( i ).findViewById( R.id.cat_name );

                Glide.with( itemView.getContext() ).load( categoryModel.getImageLink()  )
                        .apply( new RequestOptions().placeholder( R.drawable.ic_photo_black_24dp ) ).into( img );
                name.setText( categoryModel.getName() );

                // ClickListener...
                itemLayout.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if ( v == gridLayout.getChildAt( 0 ).findViewById( R.id.product_layout )){
                            // TODO :
                        } else
                        if ( v == gridLayout.getChildAt( 1 ).findViewById( R.id.product_layout )){
                            // TODO :

                        } else
                        if ( v == gridLayout.getChildAt( 2 ).findViewById( R.id.product_layout )){
                            // TODO :

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
//                    gridViewModelListViewAll = new ArrayList <>();
//                    gridViewModelListViewAll = gridLayoutList;
//                    if (gridProductDetailsList.size() == 0){
//                        ViewAllActivity.gridViewModelListViewAll = tempHrGridList;
//                    }else{
//                        ViewAllActivity.gridViewModelListViewAll = gridProductDetailsList;
//                    }
//                    ViewAllActivity.totalProductsIdViewAll = gridLayoutProductIdList;
//                    Intent viewAllIntent = new Intent( itemView.getContext(), ViewAllActivity.class);
//                    viewAllIntent.putExtra( "LAYOUT_CODE",VIEW_ALL_FOR_GRID_PRODUCTS );
//                    viewAllIntent.putExtra( "CAT_INDEX", catType );
//                    viewAllIntent.putExtra( "CAT_TITLE", catTitle );
//                    viewAllIntent.putExtra( "LIST_INDEX", index );
//                    viewAllIntent.putExtra( "TITLE", gridTitle );
//                    itemView.getContext().startActivity( viewAllIntent );
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

        private void addNewItem(final int layIndex, int listIndex){
            // TODO:
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

    private void alertDialog(final Context context, final int index, final String layoutId){
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
                                    homePageList.remove( index );
                                    SecondActivity.homePageAdaptor.notifyDataSetChanged();
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
