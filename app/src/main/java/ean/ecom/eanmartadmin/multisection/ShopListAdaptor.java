package ean.ecom.eanmartadmin.multisection;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import ean.ecom.eanmartadmin.R;
import ean.ecom.eanmartadmin.multisection.aboutshop.AboutShopModel;
import ean.ecom.eanmartadmin.multisection.aboutshop.ShopHomeActivity;

import static ean.ecom.eanmartadmin.MainActivity.clipboardManager;
import static ean.ecom.eanmartadmin.other.StaticValues.SHOP_TYPE_NON_VEG;
import static ean.ecom.eanmartadmin.other.StaticValues.SHOP_TYPE_NO_SHOW;
import static ean.ecom.eanmartadmin.other.StaticValues.SHOP_TYPE_VEG;
import static ean.ecom.eanmartadmin.other.StaticValues.SHOP_TYPE_VEG_NON;

public class ShopListAdaptor  extends RecyclerView.Adapter <ShopListAdaptor.ViewHolder> {

    private List <AboutShopModel> shopItemModelList;

    public ShopListAdaptor(List <AboutShopModel> shopItemModelList) {
        this.shopItemModelList = shopItemModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View shopView = LayoutInflater.from( parent.getContext() ).inflate( R.layout.shop_rectangle_layout_item, parent, false );
        return new ViewHolder( shopView );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AboutShopModel shopItemModel = shopItemModelList.get( position );
        String sID = shopItemModel.getShop_id();
        String sName = shopItemModel.getShop_name();
        String sLogo = shopItemModel.getShop_logo();
        String sCategory = shopItemModel.getShop_category_name();
        String sRating = String.valueOf( shopItemModel.getShop_ratings_stars() );
        int sVegType = Integer.parseInt( shopItemModel.getShop_veg_non_type() );
        holder.setData( sID, sLogo, sName, sCategory, sRating, sVegType );
    }

    @Override
    public int getItemCount() {
        return shopItemModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView shopLogo;
        private TextView shopName;
        private TextView shopIdText;
        private TextView shopCategory;
        private TextView shopRating;
        private ImageView shopVegType;
        private ImageView shopNonVegType;
        private ImageView shopIdCpyBtn;

        public ViewHolder(@NonNull View itemView) {
            super( itemView );
            shopLogo = itemView.findViewById( R.id.shop_logo );
            shopName = itemView.findViewById( R.id.shop_name );
            shopIdText = itemView.findViewById( R.id.shop_id );
            shopCategory = itemView.findViewById( R.id.shop_category );
            shopRating = itemView.findViewById( R.id.shop_rating );
            shopVegType = itemView.findViewById( R.id.shop_veg_type_image );
            shopNonVegType = itemView.findViewById( R.id.shop_non_veg_type_image );
            shopIdCpyBtn = itemView.findViewById( R.id.copy_shop_img_view );
        }

        @SuppressLint("NewApi")
        private void setData(final String shopID, String sLogoLink, String sName, String sCategory, String sRating, int sVegType ){

            Glide.with( itemView.getContext() ).load( sLogoLink ).apply( new RequestOptions()
                    .placeholder( R.drawable.ic_photo_black_24dp ) ).into( shopLogo );

            shopName.setText( sName );
            shopIdText.setText( shopID );
            shopCategory.setText( sCategory );
            if(sRating != null){
                shopRating.setVisibility( View.VISIBLE );
                shopRating.setText( sRating );
            }else{
                shopRating.setVisibility( View.INVISIBLE );
            }

            if (sVegType == SHOP_TYPE_VEG){
                shopVegType.setVisibility( View.VISIBLE );
                shopNonVegType.setVisibility( View.GONE );
            }else if(sVegType == SHOP_TYPE_NON_VEG){
                shopVegType.setVisibility( View.INVISIBLE );
                shopNonVegType.setVisibility( View.VISIBLE );
            }else if(sVegType == SHOP_TYPE_VEG_NON){
                shopVegType.setVisibility( View.VISIBLE );
                shopNonVegType.setVisibility( View.VISIBLE );
            }else if(sVegType == SHOP_TYPE_NO_SHOW){
                shopVegType.setVisibility( View.GONE );
                shopNonVegType.setVisibility( View.GONE );
            }

            itemView.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //  : Use Shop ID and Go TO...
//                    StaticMethods.showToast( itemView.getContext(), "Code Not Found..!" );
                    Intent intent = new Intent( itemView.getContext(), ShopHomeActivity.class );
                    intent.putExtra( "SHOP_ID", shopID );
                    itemView.getContext().startActivity( intent );

                }
            } );

            shopIdCpyBtn.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // Set Data
                    ClipData clipData = ClipData.newPlainText( "TEXT", shopID );
                    clipboardManager.setPrimaryClip( clipData );
                    // Get Data ...
//                    if (clipboardManager.hasPrimaryClip()){
//                        String data = clipboardManager.getPrimaryClip().getItemAt( 0 ).getText().toString();
//                    }
                    if (clipboardManager.hasPrimaryClip()){
                        Toast.makeText( itemView.getContext(), "Copied Shop ID!", Toast.LENGTH_SHORT ).show();
                    }

                }
            } );


        }

    }


}
