package wackycodes.ecom.eanmartadmin.mainpage.homesection;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import wackycodes.ecom.eanmartadmin.R;
import wackycodes.ecom.eanmartadmin.addnewitem.AddNewLayoutActivity;
import wackycodes.ecom.eanmartadmin.multisection.aboutshop.ShopHomeActivity;
import wackycodes.ecom.eanmartadmin.other.DialogsClass;
import wackycodes.ecom.eanmartadmin.other.StaticMethods;
import wackycodes.ecom.eanmartadmin.other.UpdateImages;

import static wackycodes.ecom.eanmartadmin.database.DBQuery.firebaseFirestore;
import static wackycodes.ecom.eanmartadmin.database.DBQuery.homePageList;
import static wackycodes.ecom.eanmartadmin.other.StaticValues.BANNER_CLICK_TYPE_SHOP;
import static wackycodes.ecom.eanmartadmin.other.StaticValues.BANNER_CLICK_TYPE_WEBSITE;
import static wackycodes.ecom.eanmartadmin.other.StaticValues.BANNER_SLIDER_CONTAINER_ITEM;
import static wackycodes.ecom.eanmartadmin.other.StaticValues.CURRENT_CITY_CODE;

public class BannerItemAdaptor extends RecyclerView.Adapter<BannerItemAdaptor.ViewHolder> {

    List <BannerAndCatModel> bannerAndCatModelList;
    boolean isViewAll;
    int layoutIndex;
    String catOrCollectionID;
    int catIndex;
    String layoutID;

    public BannerItemAdaptor( String catOrCollectionID, int catIndex, List <BannerAndCatModel> bannerAndCatModelList, boolean isViewAll, int layoutIndex ) {
        this.catOrCollectionID = catOrCollectionID;
        this.catIndex = catIndex;
        this.bannerAndCatModelList = bannerAndCatModelList;
        this.isViewAll = isViewAll;
        this.layoutIndex = layoutIndex;
        layoutID = homePageList.get( catIndex ).getLayoutID();
    }

    @NonNull
    @Override
    public BannerItemAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View bannerView =  LayoutInflater.from( parent.getContext() ).inflate( R.layout.banner_slider_item_layout, parent, false );
        return new ViewHolder( bannerView );
    }

    @Override
    public void onBindViewHolder(@NonNull BannerItemAdaptor.ViewHolder holder, int position) {
        if (position < bannerAndCatModelList.size() ){
            String imgLink = bannerAndCatModelList.get( position ).getImageLink();
//            String bgColor = bannerAndCatModelList.get( position ).getTitleOrBgColor();
            String clickID = bannerAndCatModelList.get( position ).getClickID();
            int clickType = bannerAndCatModelList.get( position ).getClickType();

            holder.setData( imgLink, clickID, clickType, position);
        }
        if ( !isViewAll && position == bannerAndCatModelList.size() ){
            holder.setAddNew();
        }
    }

    @Override
    public int getItemCount() {
        if (isViewAll){
            return bannerAndCatModelList.size();
        }else {
            return bannerAndCatModelList.size()+1;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private RoundedImageView bannerImage;
        private LinearLayout addNewItemLayout;
        private ImageView bannerUpBtn;
        private ImageView bannerDownBtn;
        private ImageView bannerDeleteBtn;

        private LinearLayout bannerUpdateLayout;

        private Dialog dialog;

        public ViewHolder(@NonNull View itemView) {
            super( itemView );
            bannerImage = itemView.findViewById( R.id.banner_slider_Image );
            addNewItemLayout = itemView.findViewById( R.id.add_new_item_layout );
            bannerUpdateLayout = itemView.findViewById( R.id.banner_update_layout );
            bannerUpBtn = itemView.findViewById( R.id.banner_index_up_img_view );
            bannerDownBtn = itemView.findViewById( R.id.banner_index_down_img_view );
            bannerDeleteBtn = itemView.findViewById( R.id.banner_delete_img_view );
            dialog = DialogsClass.getDialog( itemView.getContext() );
        }

        private void setData(String imgLink, final String clickID, final int clickType, final int index ){

            if (isViewAll){
                bannerUpdateLayout.setVisibility( View.VISIBLE );
            }else{
                bannerUpdateLayout.setVisibility( View.GONE );
            }
            bannerImage.setVisibility( View.VISIBLE );
            addNewItemLayout.setVisibility( View.GONE );
            Glide.with( itemView.getContext() ).load( imgLink )
                    .apply( new RequestOptions().placeholder( R.drawable.ic_panorama_black_24dp ) ).into( bannerImage );
            // Set Banner Up-down Btn Visibility...
            setIndexUpDownVisibility( index, bannerUpBtn, bannerDownBtn);

            bannerImage.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
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

            bannerUpBtn.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Update...
                    dialog.show();
                    updateIndex(true, index );
                }
            } );
            bannerDownBtn.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Update...
                    dialog.show();
                    updateIndex(false, index );
                }
            } );
            // DELETE Banner...
            bannerDeleteBtn.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder alertDialog = DialogsClass.alertDialog( itemView.getContext(),
                            "Alert!", "Do You want to delete this banner?" );
                    alertDialog.setPositiveButton( "Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int which) {
                            dialogInterface.dismiss();
                            dialog.show();
                            UpdateImages.deleteImageFromFirebase( itemView.getContext(), dialog
                                    ,CURRENT_CITY_CODE + "/HOME/banners"
                                    , bannerAndCatModelList.get( index ).getLayoutId()  );
                            // Update...
                            Map<String, Object > updateMap = new HashMap <>();
//                          updateMap.put( "delete_id_"+ (index + 1), bannerAndCatModelList.get( index ).getLayoutId() ); // String
                            updateMap.put( "no_of_banners", (bannerAndCatModelList.size() - 1) ); // int
                            if (bannerAndCatModelList.size() <= 2){
                                updateMap.put( "is_visible", false );
                            }
                            // Remove from Local...
                            bannerAndCatModelList.remove( index );
                            // Update On Database...
                            updateOnDatabase(itemView.getContext(), dialog, updateMap);
                        }
                    } );

                    alertDialog.setNegativeButton( "No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int which) {
                            dialogInterface.dismiss();
                        }
                    } );
                    alertDialog.show();
                }
            } );

        }
        private void setAddNew(){
            bannerUpdateLayout.setVisibility( View.GONE );
            bannerImage.setVisibility( View.GONE );
            addNewItemLayout.setVisibility( View.VISIBLE );
            addNewItemLayout.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // start Activity...
                    if (catOrCollectionID.equals( "HOME" )){
                        AddNewLayoutActivity.isHomePage = true;
                    }else{
                        AddNewLayoutActivity.isHomePage = false;
                    }
                    Intent intent = new Intent( itemView.getContext(), AddNewLayoutActivity.class );
                    intent.putExtra( "CAT_ID", catOrCollectionID );
                    intent.putExtra( "LAY_TYPE", BANNER_SLIDER_CONTAINER_ITEM );
                    intent.putExtra( "LAY_INDEX", layoutIndex );
                    intent.putExtra( "TASK_UPDATE", false );
                    intent.putExtra( "CAT_INDEX", catIndex );
                    itemView.getContext().startActivity( intent );
//                    Toast.makeText( itemView.getContext(), "Method is not written", Toast.LENGTH_SHORT ).show();
                }
            } );
        }

        private void setIndexUpDownVisibility( int index, ImageView indexUpBtn,  ImageView indexDownBtn){
            if (bannerAndCatModelList.size()>1){
                indexUpBtn.setVisibility( View.VISIBLE );
                indexDownBtn.setVisibility( View.VISIBLE );
                if (index == 0){
                    indexUpBtn.setVisibility( View.INVISIBLE );
                }else if (index == bannerAndCatModelList.size()-1){
                    indexDownBtn.setVisibility( View.INVISIBLE );
                }
            }else{
                indexUpBtn.setVisibility( View.INVISIBLE );
                indexDownBtn.setVisibility( View.INVISIBLE );
            }
        }
        private void updateIndex(boolean isMoveUp, int index){
            if (isMoveUp){
                // GoTo Up...
                createMapForUpdateIndex( index, index -1 );
            }else{
                // Goto Down...
                createMapForUpdateIndex( index, index +1 );
            }
        }

        private void createMapForUpdateIndex( int indexUp, int indexDown){
            // IndexUp : Which is Going to Up... After Update...
            // IndexDown : Which is Going to Down...
            Map<String, Object > updateMap = new HashMap <>();
            // UpIndex...
            updateMap.put( "banner_"+ (indexUp+1) , bannerAndCatModelList.get( ( indexDown ) ).getImageLink() ); // String image
            updateMap.put( "banner_click_id_"+ (indexUp+1),  bannerAndCatModelList.get( ( indexDown ) ).getClickID()  ); // String
            updateMap.put( "banner_click_type_"+ (indexUp+1),  bannerAndCatModelList.get( ( indexDown ) ).getClickType()  ); // int
            updateMap.put( "delete_id_"+ (indexUp+1), bannerAndCatModelList.get( ( indexDown ) ).getLayoutId() ); // String
            // Down Index..
            updateMap.put( "banner_"+ (indexDown+1) ,  bannerAndCatModelList.get( ( indexUp ) ).getImageLink()  ); // String image
            updateMap.put( "banner_click_id_"+ (indexDown+1),  bannerAndCatModelList.get( ( indexUp ) ).getClickID()  ); // String
            updateMap.put( "banner_click_type_"+ (indexDown+1),  bannerAndCatModelList.get( ( indexUp ) ).getClickType() ); // int
            updateMap.put( "delete_id_"+ (indexDown+1), bannerAndCatModelList.get( ( indexUp ) ).getLayoutId() ); // String

            // Update in local list...
            Collections.swap( bannerAndCatModelList, indexUp, indexDown );
            // Request to DataBase...
            updateOnDatabase( itemView.getContext(), dialog, updateMap );

        }

    }

    private void showToast(Context context, String msg){
        Toast.makeText( context, msg, Toast.LENGTH_SHORT ).show();
    }

    private void updateOnDatabase(final Context context, final Dialog dialog, Map<String, Object> updateMap){
        // Map...

        firebaseFirestore.collection( "HOME_PAGE" ).document( CURRENT_CITY_CODE )
                .collection( catOrCollectionID ).document( layoutID )
                .update( updateMap )
                .addOnCompleteListener( new OnCompleteListener <Void>() {
                    @Override
                    public void onComplete(@NonNull Task <Void> task) {
                        if (task.isSuccessful()){
                            showToast( context, "Update Successfully!" );
                        }else{
                            showToast( context, "Failed!, Something went wrong" );
                        }
                        BannerItemAdaptor.this.notifyDataSetChanged(); // Notify Data Changed...
                        dialog.dismiss();
                    }
                } );

    }


}
